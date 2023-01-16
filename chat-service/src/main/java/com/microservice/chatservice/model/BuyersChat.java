package com.microservice.chatservice.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BuyersChat {
    private String buyersEmailId;
    private List<Message> message;
}
