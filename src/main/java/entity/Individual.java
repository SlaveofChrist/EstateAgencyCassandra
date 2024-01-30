package entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvRecurse;

import java.time.Instant;
import java.util.Date;

public class Individual {
    @CsvBindByName(column = "code", required = true)
    private String code;
    @CsvBindByName(column = "name", required = true)
    private String name;
    @CsvBindByName(column = "surname", required = true)
    private String surname;
    @CsvBindByName(column = "birthday", required = true)
    @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ssX")
    private Instant birthday;
    @CsvRecurse
    private Address address;

    public Individual() {}

    public Individual(String code, String name, String surname, Instant birthday, int streetNumber, String street, String city, String postalCode, String country) {
        this.code = code;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.address = new Address(streetNumber, street, city, postalCode, country);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Instant getBirthday() {
        return birthday;
    }

    public void setBirthday(Instant birthday) {
        this.birthday = birthday;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
