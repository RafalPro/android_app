package edu.upenn.cis350.mapui;

public class Address {

    private String addressLine;
    private String city;
    private String state;
    private String zip;

    public Address(String addressLine, String city, String state, String zip) {
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getCity() {
        return this.city;
    }

    public String getAddressLine() {
        return this.addressLine;
    }

    public String getState() {
        return this.state;
    }

    public String getZip() {
        return this.zip;
    }
}
