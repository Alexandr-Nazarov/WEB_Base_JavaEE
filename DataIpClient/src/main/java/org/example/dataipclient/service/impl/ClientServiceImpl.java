package org.example.dataipclient.service.impl;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import org.example.dataipclient.entity.AddressEntity;
import org.example.dataipclient.entity.ClientEntity;
import org.example.dataipclient.mapper.ClientEntityToClientFunc;
import org.example.dataipclient.models.Addresses;
import org.example.dataipclient.models.Client;
import org.example.dataipclient.repository.ClientRepository;
import org.example.dataipclient.service.ClientService;

import javax.xml.crypto.Data;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class ClientServiceImpl implements ClientService {

    @EJB
    private ClientRepository clientRepository;

    private Function<ClientEntity, Client> clientEntityListFunction = new ClientEntityToClientFunc();

    @Override
    public List<Client> findAll() {
//        Client cl1 = new Client(1, "AAA", "Corporate", LocalDate.of(2023, 5, 15), Arrays.asList(
//                new Addresses(101, "192.168.1.10", "00:1A:2B:3C:4D:5E", "Dell XPS 13", "Москва, ул. Тверская, 15"),
//                new Addresses(102, "10.0.0.25", "AA:BB:CC:DD:EE:FF", "MacBook Pro", "Москва, пр. Вернадского, 42"),
//                new Addresses(103, "172.16.0.50", "11:22:33:44:55:66", "HP EliteBook", "Санкт‑Петербург, Невский пр., 105")
//        ));
//        Client client2 = new Client(
//                2,
//                "Иванов Алексей Сергеевич",
//                "Individual",
//                LocalDate.of(2023, 6, 20),
//                Arrays.asList(
//                        new Addresses(201, "192.168.2.20", "A1:B2:C3:D4:E5:F6", "Lenovo ThinkPad", "Москва, ул. Ленина, 34, кв. 12")
//                ));

//        return List.of(cl1, client2);

        return clientRepository.findAll().stream()
                .map(clientEntityListFunction)
                //.flatMap(Collection::stream)
                .toList();
    }

    @Override
    public Client create(Client client) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClientId(client.getClientId());
        clientEntity.setClientName(client.getClientName());
        clientEntity.setAdded(client.getAdded());
        clientEntity.setType(client.getType());

        Set<AddressEntity> addressEntities = convertAddressesToEntities(client.getAddresses(), clientEntity);
        clientEntity.setAddressEntities(addressEntities);

        clientRepository.create(clientEntity);

        if (addressEntities != null && !addressEntities.isEmpty()) {
            for (AddressEntity addressEntity : addressEntities) {
                clientRepository.create(addressEntity);
            }
        }

        return  clientEntityListFunction.apply(clientEntity);

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

    @Override
    public void delete(Integer clientId) {
        Optional.ofNullable(clientRepository.findClientById(clientId))
                .ifPresentOrElse(
                        entity -> {
                            clientRepository.delete(entity);
                        },
                        () -> {
                            throw new RuntimeException("Персона для удаления не найдена");
                        }
                );
    }

    @Override
    public Client findById(Integer clientId) {
      return Optional.ofNullable(clientRepository.findClientById(clientId))
                .map(clientEntityListFunction).orElseThrow(() -> {
                    throw new RuntimeException("Персона для модтфикации не найдена");
                });
    }

    @Override
    public Client update(Client client) {
        ClientEntity clientEntity = clientRepository.findClientById(client.getClientId());
        if (clientEntity != null) {
            clientEntity.setClientName(client.getClientName());
            clientEntity.setType(client.getType());

            clientRepository.update(clientEntity);
            Set<AddressEntity> addressEntities = convertAddressesToEntities(client.getAddresses(), clientEntity);

            if (client.getAddresses() != null && !client.getAddresses().isEmpty()) {
                for (AddressEntity addressEntity : addressEntities) {
                    clientRepository.update(addressEntity);
                }
            }
        }
        return client;
    }

}
