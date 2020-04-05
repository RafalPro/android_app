package edu.upenn.cis350.mapui;

/*
  This is a description of an event
 */
public class Event {

    String topic;
    String description;
    String time;
    //String date;
    Address address;
    String urlRegistrationAddress;

    public Event(String topic, String description, String time, Address address,
                 String urlRegistrationAddress) {
        this.topic = topic;
        this.description = description;
        //this.timeZone = timeZone;
        //this.date = date;
        this.address = address;
        this.urlRegistrationAddress = urlRegistrationAddress;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTime() {
        return this.time;
    }

//    public String getDate() {
//        return this.date;
//    }

    public Address getAddress() {
        return this.address;
    }

    public String getUrlRegistrationAddress() {
        return this.urlRegistrationAddress;
    }

}
