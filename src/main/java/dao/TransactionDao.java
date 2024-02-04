package dao;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import entity.AccommodationGoal;

import java.time.Instant;

public class TransactionDao {

    @CsvBindByName(column = "code", required = true)
    private String code;
    @CsvBindByName(column = "date", required = true)
    @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ssX")
    private Instant date;
    @CsvBindByName(column = "commission", required = true)
    private float commission;
    @CsvBindByName(column = "price", required = true)
    private float price;
    @CsvBindByName(column = "type", required = true)
    private AccommodationGoal type;
    @CsvBindByName(column = "customer_code", required = true)
    private String customerCode;
    @CsvBindByName(column = "estate_agent_code", required = true)
    private String estateAgentCode;
    @CsvBindByName(column = "owner_code", required = true)
    private String ownerCode;
    @CsvBindByName(column = "accommodation_code", required = true)
    private String accommodationCode;

    public TransactionDao() {}

    public TransactionDao(String code, Instant date, float commission, float price, AccommodationGoal type, String customerCode, String estateAgentCode, String ownerCode, String accommodationCode) {
        this.code = code;
        this.date = date;
        this.commission = commission;
        this.price = price;
        this.type = type;
        this.customerCode = customerCode;
        this.estateAgentCode = estateAgentCode;
        this.ownerCode = ownerCode;
        this.accommodationCode = accommodationCode;
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

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getEstateAgentCode() {
        return estateAgentCode;
    }

    public void setEstateAgentCode(String estateAgentCode) {
        this.estateAgentCode = estateAgentCode;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getAccommodationCode() {
        return accommodationCode;
    }

    public void setAccommodationCode(String accommodationCode) {
        this.accommodationCode = accommodationCode;
    }
}
