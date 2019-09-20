package com.example.varun.vesica.models;

/**
 * Created by varun on 23/8/16.
 */
public class Message {

    private long burnTime;

    public void setBurnable(int burnable) {
        this.burnable = burnable;
    }

    private int burnable;
    private int isExpired;
    private int read;
    private String cipherText;
    private String senderName;
    private String recipientName;
    private long messageId;
    private long deliveryTime;
    private long receiveTime;
    private String sendToSocketId;
    private String messageType;

    public void setRead(int read) {
        this.read = read;
    }

    public void setExpired(int expired) {
        isExpired = expired;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }
    public void setBurnTime(long num){this.burnTime =num;}

    public long getReceiveTime() {
        return receiveTime;
    }

    public long getBurnTime() {
        return burnTime;
    }

    public int getBurnable() {
        return burnable;
    }

    public int getExpired() {
        return isExpired;
    }

    public int getRead() {
        return read;
    }

    public String getCipherText() {
        return cipherText;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getDeliveryTime() {
        return deliveryTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getSendToSocketId() {
        return sendToSocketId;
    }

    private Message(MessageBuilder builder){
        this.burnTime = builder.burnTime;
        this.burnable = builder.burnable;
        this.cipherText = builder.cipherText;
        this.senderName = builder.senderName;
        this.recipientName = builder.recipientName;
        this.messageId = builder.messageId;
        this.deliveryTime = builder.deliveryTime;
        this.isExpired = builder.isExpired;

    }

    //Builder for making a chat message
    public static class MessageBuilder{
        private long burnTime;
        private int burnable;
        private String cipherText;
        private String senderName;
        private String recipientName;
        private long messageId;
        private long deliveryTime;

        private int isExpired;
        private long receiveTime;
        private Boolean read;

        public MessageBuilder burnTime(long burnTime){
            this.burnTime= burnTime;
            return this;
        }

        public MessageBuilder burnable(int burnable){
            this.burnable= burnable;
            return this;
        }

        public MessageBuilder cipherText(String cipherText){
            this.cipherText = cipherText;
            return this;
        }

        public MessageBuilder senderName(String senderName){
            this.senderName= senderName;
            return this;
        }

        public MessageBuilder recipientName(String recipientName){
            this.recipientName= recipientName;
            return this;
        }

        public MessageBuilder messageId(long messageId){
            this.messageId= messageId;
            return this;
        }

        public MessageBuilder deliveryTime(long deliveryTime){
            this.deliveryTime= deliveryTime;
            return this;
        }

        public MessageBuilder isExpired(int isExpired){
            this.isExpired = isExpired;
            return this;
        }

        public Message build(){
            return new Message(this);
        }
    }
}
