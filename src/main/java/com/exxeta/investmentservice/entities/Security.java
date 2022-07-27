package com.exxeta.investmentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Security {

    @Id
    @Column(length = 13)
    private String isin;

    @JsonIgnore
    private long userId;

    private String securityName;

    @OneToMany(cascade = CascadeType.REMOVE,
            fetch = FetchType.EAGER,
            mappedBy = "security")
    @JsonIgnore
    private Collection<Transaction> transactions = new ArrayList<>();

    public Security() {
    }

    public Security(String isin, String securityName) {
        this.isin = isin;
        this.securityName = securityName;
    }

    public String getIsin() {
        return isin;
    }

    public String getSecurityName() {
        return securityName;
    }
}
