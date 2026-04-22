package org.example.dataipclient.models;

import java.util.Objects;

public class Addresses {
    private Integer addressId;
    private String ip;
    private String mac;
    private String model;
    private String address;
    private Client client;

    public Addresses(Integer addressId, String ip, String mac, String model, String address, Client client) {
        this.addressId = addressId;
        this.ip = ip;
        this.mac = mac;
        this.model = model;
        this.address = address;
        this.client = client;
        client.getAddresses().add(this); // добавляем адрес в коллекцию клиента
    }

    public Addresses() {
    }

    public Addresses(Integer addressId, String ip, String mac, String model, String address) {
        this.addressId = addressId;
        this.ip = ip;
        this.mac = mac;
        this.model = model;
        this.address = address;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Addresses addresses = (Addresses) o;
        return Objects.equals(addressId, addresses.addressId) && Objects.equals(ip, addresses.ip) && Objects.equals(mac, addresses.mac) && Objects.equals(model, addresses.model) && Objects.equals(address, addresses.address) && Objects.equals(client, addresses.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId, ip, mac, model, address, client);
    }
}
