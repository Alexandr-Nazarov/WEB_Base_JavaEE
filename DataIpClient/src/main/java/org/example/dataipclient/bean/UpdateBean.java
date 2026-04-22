package org.example.dataipclient.bean;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.dataipclient.entity.AddressEntity;
import org.example.dataipclient.entity.ClientEntity;
import org.example.dataipclient.models.Addresses;
import org.example.dataipclient.models.Client;
import org.example.dataipclient.service.ClientService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class UpdateBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private DbManager dbManager;

//    @EJB
//    private ClientService clientService;


    public Client createClient(Client client) {

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClientId(client.getClientId());
        clientEntity.setClientName(client.getClientName());
        clientEntity.setAdded(client.getAdded());
        clientEntity.setType(client.getType());

        Set<AddressEntity> addressEntities = convertAddressesToEntities(client.getAddresses(), clientEntity);
        clientEntity.setAddressEntities(addressEntities);

        //2 этап
        // em.persist(clientEntity);
        //3 этап
        dbManager.persist(clientEntity);

        if (addressEntities != null && !addressEntities.isEmpty()) {
            for (AddressEntity addressEntity : addressEntities) {
                //2 этап
                //em.persist(addressEntity);
                //3 этап
                dbManager.persist(addressEntity);
            }
        }
     //   em.flush();

        return client;
    }

    private Set<AddressEntity> convertAddressesToEntities(List<Addresses> addresses, ClientEntity clientEntity) {
        if (addresses == null || addresses.isEmpty()) {
            return new HashSet<>();
        }
        return addresses.stream()
                .map(address -> convertToAddressEntity(address, clientEntity))
                .collect(Collectors.toSet());
    }

    private AddressEntity convertToAddressEntity(Addresses address, ClientEntity clientEntity) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddressId(address.getAddressId());
        addressEntity.setIp(address.getIp());
        addressEntity.setMac(address.getMac());
        addressEntity.setModel(address.getModel());
        addressEntity.setAddress(address.getAddress());
        addressEntity.setClient(clientEntity);

        clientEntity.getAddressEntities().add(addressEntity);

        return addressEntity;
    }

    public void delete(Integer clientId) {
        //2 этап
        //ClientEntity clientEntity = findClientById(clientId);
        //3 этап
        ClientEntity clientEntity = dbManager.find(clientId);

        if (clientEntity != null) {
        Set<AddressEntity> addresses = clientEntity.getAddressEntities();
        if (addresses != null && !addresses.isEmpty()) {
            for (AddressEntity addressEntity : addresses) {
                //2 этап
                // em.remove(addressEntity);
                //3 этап
                dbManager.remove(addressEntity);
            }
        //    em.flush();
        }
              //2 этап
              //em.remove(clientEntity);
              //em.flush();
              //3 этап
            dbManager.remove(clientEntity);

        }
    }

    public ClientEntity findClientById(Integer id){
        if(id==null) Objects.requireNonNull(id, "Идентификатор клиента не может быть null");
        ClientEntity clientEntity = em.find(ClientEntity.class, id);
        return clientEntity;
    }

    public Client update(Client client) {
        ClientEntity clientEntity = findClientById(client.getClientId());
        if (clientEntity != null) {
            clientEntity.setClientName(client.getClientName());
            clientEntity.setType(client.getType());
            //2 этап
            //em.merge(clientEntity);
            //em.flush();
            dbManager.merge(clientEntity);
            Set<AddressEntity> addressEntities = convertAddressesToEntities(client.getAddresses(), clientEntity);
            if (client.getAddresses() != null && !client.getAddresses().isEmpty()) {
                for (AddressEntity addressEntity : addressEntities) {
                    //2 этап
                    //em.merge(addressEntity);
                    //em.flush();
                    //3 этап
                    dbManager.merge(addressEntity);
                }
            }
        }
        return client;
    }
}
