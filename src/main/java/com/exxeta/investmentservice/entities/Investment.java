package com.exxeta.investmentservice.entities;

import com.exxeta.investmentservice.util.LocalDateDeserializer;
import com.exxeta.investmentservice.util.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Investment {

    @Id
    @GeneratedValue
    @JsonIgnore
    public long investmentId;

    @JsonIgnore
    public long userId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull
    public LocalDate date;

    @NotNull
    public double amount;

    public Investment() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Investment that = (Investment) o;
        return Double.compare(that.amount, amount) == 0 &&
            Objects.equals(date, that.date);
    }
}
