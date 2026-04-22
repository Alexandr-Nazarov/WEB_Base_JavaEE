package org.example.dataipclient.models;

//import lombok.Builder;
//import lombok.Data;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//@Data
//@Builder
public class Client {
    private Integer clientId;
    private String clientName;
    private String type;
    private LocalDate added;
    private List<Addresses> addresses  = new ArrayList<>();;

    public Client(Integer clientId, String clientName, String type, LocalDate added, List<Addresses> addresses) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.type = type;
        this.added = added;
        this.addresses = addresses;
    }

    public Client() {
    }

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

    public List<Addresses> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Addresses> addresses) {
        this.addresses = addresses;
    }
}
