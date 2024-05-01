package ru.taf;

//public class RabbitQueue {
////    public static final String DOC_MESSAGE_UPDATE = "doc_message_update";
////    public static final String PHOTO_MESSAGE_UPDATE = "photo_message_update";
////    public static final String TEXT_MESSAGE_UPDATE = "text_message_update";
////    public static final String ANSWER_MESSAGE = "answer_message";
////}

import lombok.Getter;

@Getter
public enum RabbitQueue {

    DOC_MESSAGE_UPDATE("doc_message_update"),
    PHOTO_MESSAGE_UPDATE("photo_message_update"),
    TEXT_MESSAGE_UPDATE("text_message_update"),
    ANSWER_MESSAGE("answer_message");

    private final String val;

    private RabbitQueue(String val){
        this.val = val;
    }
}
