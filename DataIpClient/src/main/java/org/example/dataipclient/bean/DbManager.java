package org.example.dataipclient.bean;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.dataipclient.entity.ClientEntity;
import org.example.dataipclient.mapper.ClientEntityToClientFunc;
import org.example.dataipclient.models.Client;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
//@Startup
public class DbManager {

    @PersistenceContext
    private EntityManager em;

    private Function<ClientEntity, Client> clientEntityListFunction = new ClientEntityToClientFunc();
//    public void init() {
//        em.getTransaction().begin();
//    }
//
//    public void close() {
//        em.getTransaction().commit();
//    }

    public void persist(Object clientEntity) {
        em.persist(clientEntity);
        em.flush();
    }
    public void remove(Object clientEntity) {
        em.remove(clientEntity);
        em.flush();
    }

    public Object merge(Object clientEntity) {
        Object obj = em.merge(clientEntity);
        em.flush();
        return obj;
    }

    public ClientEntity find(Integer id) {
        return em.find(ClientEntity.class, id);
    }

    public List<Client> findAll() {
        return em.createQuery("select c from ClientEntity c order by c.clientId" , ClientEntity.class)  //??? from CEntity
                .getResultList().stream().map(clientEntityListFunction).collect(Collectors.toList());
    }

    public List<Client> findClientsWithFilter(String filterType, String searchText) {
        StringBuilder reqToBase = new StringBuilder();

        reqToBase.append( "SELECT c FROM ClientEntity c LEFT JOIN c.addressEntities a WHERE 1=1");

        if (filterType != null && !filterType.isEmpty() && !"All".equals(filterType)) {
            reqToBase.append(" AND c.type = " + "'" + filterType + "'");
        }

        if (searchText != null && !searchText.isEmpty()) {
            reqToBase.append(" AND (LOWER (c.clientName) LIKE " + "'%" + searchText + "%'");
            reqToBase.append(" OR LOWER (a.address) LIKE " + "'%" + searchText + "%')");
        }

        return em.createQuery( reqToBase.toString(), ClientEntity.class)
                .getResultList().stream().map(clientEntityListFunction).collect(Collectors.toList());
    }
}
