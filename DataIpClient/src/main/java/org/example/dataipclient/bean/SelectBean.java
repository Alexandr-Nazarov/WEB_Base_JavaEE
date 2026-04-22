package org.example.dataipclient.bean;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.dataipclient.entity.ClientEntity;
import org.example.dataipclient.mapper.ClientEntityToClientFunc;
import org.example.dataipclient.models.Client;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Stateless
public class SelectBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private DbManager dbManager;

    private Function<ClientEntity, Client> clientEntityListFunction = new ClientEntityToClientFunc();

    public List<Client> findAll() {
        //2 этап
        // List<ClientEntity> clientEntities =  em.createNativeQuery( "select * from client", ClientEntity.class).getResultList();
        //return clientEntities.stream().map(clientEntityListFunction).collect(Collectors.toList());
        //3 этап
        return dbManager.findAll();
    }

    public List<Client> findClientsWithFilter(String filterType, String searchText) {
//2 этап
//        StringBuilder reqToBase = new StringBuilder();
//
//        reqToBase.append( "SELECT * FROM client WHERE 1=1");
//
//        if (filterType != null && !filterType.isEmpty() && !"All".equals(filterType)) {
//            reqToBase.append(" AND client.type = " + "'" + filterType + "'");
//        }
//
//        if (searchText != null && !searchText.isEmpty()) {//
//              reqToBase.append(" AND (LOWER (client.client_name) LIKE " + "'%" + searchText + "%'");
//              reqToBase.append(" OR LOWER (address.address) LIKE " + "'%" + searchText + "%')");
//        }

//        List<ClientEntity> clientEntities =  em.createNativeQuery( reqToBase.toString(), ClientEntity.class).getResultList();
//        return clientEntities.stream().map(clientEntityListFunction).collect(Collectors.toList());
        //3 этап
        return dbManager.findClientsWithFilter(filterType, searchText);
    }
}
