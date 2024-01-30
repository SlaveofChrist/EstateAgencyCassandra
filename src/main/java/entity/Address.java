package entity;

import com.opencsv.bean.CsvBindByName;

public class Address {
    @CsvBindByName(column = "address_street_number", required = true)
    private int streetNumber;
    @CsvBindByName(column = "address_street", required = true)
    private String street;
    @CsvBindByName(column = "address_city", required = true)
    private String city;
    @CsvBindByName(column = "address_postal_code")
    private String postalCode;
    @CsvBindByName(column = "address_country", required = true)
    private String country;

    public Address() {
    }

    public Address(int streetNumber, String street, String city, String postalCode, String country) {
        this.streetNumber = streetNumber;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
