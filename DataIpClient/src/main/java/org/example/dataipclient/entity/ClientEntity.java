package org.example.dataipclient.entity;

import jakarta.persistence.*;
import org.example.dataipclient.models.Addresses;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clientid", nullable = false)
    private Integer clientId;

    @Column(name = "client_name", length = 100)
    private String clientName;

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "added")
    private LocalDate added;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER/*, cascade = CascadeType.ALL,  orphanRemoval = true*/)
    private Set<AddressEntity> addressEntities = new LinkedHashSet<>();

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getAdded() {
        return added;
    }

    public void setAdded(LocalDate added) {
        this.added = added;
    }

    public Set<AddressEntity> getAddressEntities() {
        return addressEntities;
    }

    public void setAddressEntities(Set<AddressEntity> addressEntities) {
        this.addressEntities = addressEntities;
    }

    @Override
    public String toString() {
        return "ClientEntity{" +
                "clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                ", type='" + type + '\'' +
                ", added=" + added +
                '}';
    }

    public void addAddress(AddressEntity address) {
        addressEntities.add(address);
        address.setClient(this);
    }
}
