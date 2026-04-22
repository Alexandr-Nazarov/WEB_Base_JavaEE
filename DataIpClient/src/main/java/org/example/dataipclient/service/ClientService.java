package org.example.dataipclient.service;

import org.example.dataipclient.models.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAll();
    Client create(Client client);
    Client update(Client client);
    void delete(Integer clientId);
    Client findById(Integer clientId);
}
