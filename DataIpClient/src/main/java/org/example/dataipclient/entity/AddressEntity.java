package org.example.dataipclient.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressid", nullable = false)
    private Integer addressId;

    @Column(name = "ip", length = 25)
    private String ip;

    @Column(name = "mac", length = 20)
    private String mac;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "address", length = 200)
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clientid")
    private ClientEntity client;

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

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }
}
