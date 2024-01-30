package dao;

import entity.Accommodation;
import entity.AccommodationGoal;
import entity.Individual;

import java.time.Instant;

public class TransactionDao {

    private String code;
    private Instant date;
    private float commission;
    private float price;
    private AccommodationGoal type;
    private String customer_code;
    private String estateAgent_code;
    private String owner_code;
    private String accommodation_code;

    public TransactionDao(String code, Instant date, float commission, float price, AccommodationGoal type, String customer_code, String estateAgent_code, String owner_code, String accommodation_code) {
        this.code = code;
        this.date = date;
        this.commission = commission;
        this.price = price;
        this.type = type;
        this.customer_code = customer_code;
        this.estateAgent_code = estateAgent_code;
        this.owner_code = owner_code;
        this.accommodation_code = accommodation_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public AccommodationGoal getType() {
        return type;
    }

    public void setType(AccommodationGoal type) {
        this.type = type;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getEstateAgent_code() {
        return estateAgent_code;
    }

    public void setEstateAgent_code(String estateAgent_code) {
        this.estateAgent_code = estateAgent_code;
    }

    public String getOwner_code() {
        return owner_code;
    }

    public void setOwner_code(String owner_code) {
        this.owner_code = owner_code;
    }

    public String getAccommodation_code() {
        return accommodation_code;
    }

    public void setAccommodation_code(String accommodation_code) {
        this.accommodation_code = accommodation_code;
    }
}
