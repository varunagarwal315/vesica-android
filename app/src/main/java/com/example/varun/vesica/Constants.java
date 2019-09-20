package com.example.varun.vesica;

/**
 * Created by varun on 23/8/16.
 */
public class Constants {
    protected Constants(){}
    //office address
    public static final String baseIpAddress="http://192.168.0.101:";
    //public static final String baseIpAddress="http://vesica5642.cloudapp.net:";

    public static Integer portNumber=null;
    public static String username=null;

    public static Integer getPortNumber(){
        return portNumber;
    }
    public static void setPortNumber(Integer number){
        portNumber=number;
    }

    public static String getUsername(){
        return username;
    }
    public static void setUsername(String name){
        username= name;
    }


    //Socket io event constants
    public static String SEND_MESSAGE="new_message";
}
