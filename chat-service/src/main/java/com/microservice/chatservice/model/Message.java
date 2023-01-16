package com.microservice.chatservice.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {
    private  String message;
    private String messageSendBy;
    private long messageSendOn;
}
