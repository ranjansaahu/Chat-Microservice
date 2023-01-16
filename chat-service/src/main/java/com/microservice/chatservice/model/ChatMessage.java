package com.microservice.chatservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ChatMessage {

    @Id
    private String productId;
    private String sellerEmail;
    private List<BuyersChat> buyersChat;

}
