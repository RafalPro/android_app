package edu.upenn.cis350.mapui;//PLEASE MAKE SURE YOU HAVE THE JSON SIMPLE JAR ADDED TO THE LIBRARY IN ANDROID STUDIO
//https://stackoverflow.com/questions/25660166/how-to-add-a-jar-in-external-libraries-in-android-studio
import java.util.*;


import java.io.*;
import java.net.*;
import org.json.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Crawler {
    private String city;
    private String stateAbbreviation;
    private String topic;
    private String date;     //please input date as yr-month-date  ex 2019-05-09 for May 9th, 2019
    private String children;
    String apiKey = "gextgbv38mgf2y4zye5ez995";


    public Crawler(String city, String stateAbbreviation, String topic, String date) {
        this.city = city;
        this.stateAbbreviation = stateAbbreviation;
        this.topic = topic;
        this.date = date;
    }

    public List<Event> crawl() {
        LinkedList<Event> events = new LinkedList<Event>();
        //String apiKey = "gextgbv38mgf2y4zye5ez995";
        try {
            // create an object to represent the connection to a
            URL url = new URL("http://api.amp.active.com/v2/search/?city=" + this.city +
                    "&state=" + this.stateAbbreviation + "&current_page=1&per_page=10&sort=distance&" +
                    "topic=" + this.topic + "&start_date=" + date + "..&exclude_children=true&api_key=" + this.apiKey);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");

            // open connection and send HTTP request
            conn.connect();

            // now the response comes back
            int responsecode = conn.getResponseCode();
            // make sure the response has "200 OK" as the status
            if (responsecode != 200) {
                System.out.println("Unexpected status code: " + responsecode);
            } else {
                // made it here, we got a good response, let's read it
                Scanner in = new Scanner(url.openStream());
                while (in.hasNext()) {
                    // read the next line of the body of the response
                    String line = in.nextLine();
                    //System.out.println(line);
                    // the rest of this code assumes that the body contains JSON
                    //JSONTokener parser = new JSONTokener(line);
                    JSONParser parser = new JSONParser();
                    JSONObject data = (JSONObject) parser.parse(line);

                    //these are all the results
                    JSONArray results = (JSONArray) data.get("results");
                    int i = 0;
                    for (Object j : results) {
                        //get the actual event
                        JSONObject event = (JSONObject) results.get(i);
                        //get the registrationURLAddress
                     //   System.out.println(event.get("registrationUrlAdr"));
                        String urlRegistrationAddress = (String) event.get("registrationUrlAdr");
                        //get the event description
                        JSONArray descriptionArray = (JSONArray) event.get("assetDescriptions");
                        JSONObject description = (JSONObject) descriptionArray.get(0);
                        //System.out.println(description.get("description"));

                    //    System.out.println(description.get("description"));
                        String descriptionString = (String) description.get("description");
                        //get start time
                     //   System.out.println(event.get("activityStartDate"));

                        String startTime = (String) event.get("activityStartDate");
                        //get the time zone and the place
                        JSONObject place = (JSONObject) event.get("place");
                        //time zone
                     //   System.out.println(place.get("timezone"));
                        //address
                      //  System.out.println(place.get("addressLine1Txt"));
                        String addressLine = (String) place.get("addressLine1Txt");
                      //  System.out.println(place.get("cityName"));
                        String city = (String) place.get("cityName");
                      //  System.out.println(place.get("stateProvinceCode"));
                        String state = (String) place.get("stateProvinceCode");
                      //  System.out.println(place.get("postalCode"));
                        String zip = (String) place.get("postalCode");
                        Address address = new Address(addressLine, city, state, zip);
                       // System.out.println(i);

                        events.add(new Event(topic, descriptionString, startTime, address,
                                urlRegistrationAddress));
                        i++;
                        //System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return events;
    }

}
