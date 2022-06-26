package com.app.cheerthemup.models;

public class Message {

String sender;
String reciever;


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }


    String messsage;


    public Message() {
    }


    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }

    public Message(String messsage) {
        this.messsage = messsage;
    }
}
