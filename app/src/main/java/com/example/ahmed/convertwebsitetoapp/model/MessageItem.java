package com.example.ahmed.convertwebsitetoapp.model;

public class MessageItem{
        
        String messageId = "";
        String messageContent = "";
        String rightOrLeft = "";


    public MessageItem() {
    }

    public MessageItem(String messageId, String messageContent, String rightOrLeft) {
        this.messageId = messageId;
        this.messageContent = messageContent;
        this.rightOrLeft = rightOrLeft;
    }


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getRightOrLeft() {
        return rightOrLeft;
    }

    public void setRightOrLeft(String rightOrLeft) {
        this.rightOrLeft = rightOrLeft;
    }
}