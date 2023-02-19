package com.exxeta.investmentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Security {

    @Id
    @Column(length = 55)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Security security = (Security) o;
        return Objects.equals(isin, security.isin) && Objects.equals(securityName, security.securityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isin, securityName);
    }
}
