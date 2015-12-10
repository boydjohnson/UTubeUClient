package com.example.boydjohnson.androidutubeuclient.bus;

import com.squareup.otto.Bus;

/**
 * Created by boydjohnson on 12/10/15.
 * The otto.Bus instance must be a singleton and this class makes sure of it
 *
 *
 */
public class MessageBus {

    private static Bus instance = null;

    private MessageBus(){
        instance = new Bus();
    }

    public static Bus getInstance(){
        if(instance==null){
            new MessageBus();
        }
        return instance;
    }
}
