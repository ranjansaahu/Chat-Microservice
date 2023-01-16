package com.microservice.chatservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BuyerChatWithProductId {

    private String productId;
    private BuyersChat buyersChat;
}
