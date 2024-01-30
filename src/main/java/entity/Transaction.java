package entity;

import dao.TransactionDao;

import java.time.Instant;

public class Transaction {

    private String code;
    private Instant date;
    private float commission;
    private float price;
    private AccommodationGoal type;
    private Individual customer;
    private Individual estateAgent;
    private Individual owner;
    private Accommodation accommodation;

    public Transaction(String code, Instant date, float commission, float price, AccommodationGoal type, Individual customer, Individual estateAgent, Individual owner, Accommodation accommodation) {
        this.code = code;
        this.date = date;
        this.commission = commission;
        this.price = price;
        this.type = type;
        this.customer = customer;
        this.estateAgent = estateAgent;
        this.owner = owner;
        this.accommodation = accommodation;
    }

    public TransactionDao to_dao(){
        return new TransactionDao(code, date, commission, price, type, customer.getCode(), estateAgent.getCode(), owner.getCode(), accommodation.getCode());
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

    public Individual getCustomer() {
        return customer;
    }

    public void setCustomer(Individual customer) {
        this.customer = customer;
    }

    public Individual getEstateAgent() {
        return estateAgent;
    }

    public void setEstateAgent(Individual estateAgent) {
        this.estateAgent = estateAgent;
    }

    public Individual getOwner() {
        return owner;
    }

    public void setOwner(Individual owner) {
        this.owner = owner;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }
}
