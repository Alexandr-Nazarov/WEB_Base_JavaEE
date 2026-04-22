package org.example.dataipclient.repository;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.dataipclient.entity.AddressEntity;
import org.example.dataipclient.entity.ClientEntity;
import org.example.dataipclient.models.Addresses;
import org.example.dataipclient.models.Client;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Singleton
public class ClientRepository {

    @PersistenceContext
    private EntityManager em;

    public ClientEntity findClientById(Integer id){
        if(id==null) Objects.requireNonNull(id, "Идентификатор клиента не может быть null");
        ClientEntity client = em.find(ClientEntity.class, id);
        return client;
    }

    public Set<ClientEntity> findAll(){
        return new HashSet<>(em.createNativeQuery( "select * from client"
                //"SELECT client.*, address.* FROM client LEFT JOIN address ON client.clientid = address.clientid;"
                , ClientEntity.class).getResultList());
    }

    public void create(ClientEntity entity) {
        em.persist(entity);
        em.flush();
    }

    public void create(AddressEntity entity) {
        em.persist(entity);
        em.flush();
    }

    public void update(ClientEntity clientEntity) {
        em.merge(clientEntity);
        em.flush();
    }

    public void update(AddressEntity entity) {
        em.merge(entity);
        em.flush();
    }

    public void delete(ClientEntity clientEntity) {
        em.remove(clientEntity);
    }
}
