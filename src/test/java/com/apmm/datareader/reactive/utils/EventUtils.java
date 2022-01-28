package com.apmm.datareader.reactive.utils;


import com.apmm.datareader.reactive.entity.Event;

public class EventUtils {

    private static Event event;

    public static Event getEvent(){


        event=new Event();
        event.setId("12345");
        event.setEventId("123");
        event.setEventMessage("eventXml");
        event.setEventJson("eventJson");
        return event;
    }


}

