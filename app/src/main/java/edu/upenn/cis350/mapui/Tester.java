package edu.upenn.cis350.mapui;

import java.util.*;
import android.util.Log;

//An example of how to call the Crawler

public class Tester {




    public static void main(String[] args) {
        Crawler c = new Crawler("philadelphia", "pa", "camping", "2020-07-01");
       // Log.d("hi, "hey");

        List<Event> list = new LinkedList<Event>();
        list = c.crawl();

        for (Event e : list) {
            System.out.println(e.getDescription());
        }
    }




}
