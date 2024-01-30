package entity;

import com.opencsv.bean.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class Accommodation {

    @CsvBindByName(column = "code", required = true)
    private String code;
    @CsvBindByName(column = "number", required = true)
    private int number;
    @CsvBindByName(column = "type", required = true)
    private AccommodationType type;
    @CsvBindByName(column = "goal", required = true)
    private AccommodationGoal goal;
    @CsvBindByName(column = "room_number", required = true)
    private int roomNumber;
    @CsvBindByName(column = "floor_space", required = true)
    private int floorSpace;
    @CsvBindByName(column = "state", required = true)
    private AccommodationState state;
    @CsvBindByName(column = "price", capture = "\\$(.+)", required = true)
    private double price;
//    @CsvBindByName(column = "availability_date", required = true)
//    @CsvDate(
//            value = "dd-MM-yyyy",
//            writeFormat = "dd-MM-yyyy",
//            writeFormatEqualsReadFormat = false
//            )
    private Instant availabilityDate;
    @CsvRecurse
    private Address address;

    public Accommodation() {
    }

    public Accommodation(String code, int number, AccommodationType type, AccommodationGoal goal, int roomNumber, int floorSpace, AccommodationState state, double price, Instant availabilityDate, int streetNumber, String street, String city, String postalCode, String country) {
        this.code = code;
        this.number = number;
        this.type = type;
        this.goal = goal;
        this.roomNumber = roomNumber;
        this.floorSpace = floorSpace;
        this.state = state;
        this.price = price;
        this.availabilityDate = availabilityDate;
        this.address = new Address(streetNumber, street, city, postalCode, country);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public AccommodationType getType() {
        return type;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public AccommodationGoal getGoal() {
        return goal;
    }

    public void setGoal(AccommodationGoal goal) {
        this.goal = goal;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getFloorSpace() {
        return floorSpace;
    }

    public void setFloorSpace(int floorSpace) {
        this.floorSpace = floorSpace;
    }

    public AccommodationState getState() {
        return state;
    }

    public void setState(AccommodationState state) {
        this.state = state;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Instant getAvailabilityDate() {
        return availabilityDate;
    }

    public void setAvailabilityDate(Instant availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
