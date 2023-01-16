package com.microservice.chatservice.controller;


import com.microservice.chatservice.model.BuyerChatWithProductId;
import com.microservice.chatservice.model.BuyersChat;
import com.microservice.chatservice.model.ChatMessage;
import com.microservice.chatservice.model.Message;
import com.microservice.chatservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/chats")
public class ChatController {
    @Autowired
    public ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/seller/{sellerEmail}")
    public  ResponseEntity<List<BuyerChatWithProductId>> getBuyersList(@PathVariable String sellerEmail) {
        List<BuyerChatWithProductId> buyerList = chatService.getBuyersList(sellerEmail);
        return new ResponseEntity<>(buyerList, HttpStatus.OK);
    }

    @GetMapping("/buyer/{buyerEmailId}")
    public  ResponseEntity<List<ChatMessage>> getProductsOfBuyer(@PathVariable String buyerEmailId) {
        List<ChatMessage> productsList = chatService.getProductsOfBuyer(buyerEmailId);
        return new ResponseEntity<>(productsList, HttpStatus.OK);
    }

    @PostMapping("/message")
    public ResponseEntity<?> saveChat(@RequestBody ChatMessage chatMessage) {
        ChatMessage existingChat = null;
        try {
            existingChat = chatService.getChatById(chatMessage.getProductId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (existingChat == null) {
            try {
                chatService.saveMessage(chatMessage);
                return new ResponseEntity<ChatMessage>(chatMessage, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<String>("Exception", HttpStatus.CONFLICT);
            }
        } else {
            List<BuyersChat> buyersChat = existingChat.getBuyersChat();
            String buyerEmailId = chatMessage.getBuyersChat().get(0).getBuyersEmailId();
            boolean buyerExisting = false;
            for (BuyersChat b : buyersChat) {
                if (Objects.equals(b.getBuyersEmailId(), buyerEmailId)) {

                    buyerExisting = true;
                }
            }
            if (!buyerExisting) {
                buyersChat.add(chatMessage.getBuyersChat().get(0));
                existingChat.setBuyersChat(buyersChat);
                try {
                    chatService.saveMessage(existingChat);
                    return new ResponseEntity<ChatMessage>(chatMessage, HttpStatus.OK);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<String>("Exception", HttpStatus.CONFLICT);
                }
            } else {
                List<BuyersChat> newChat = new ArrayList<>();
                for (BuyersChat b : buyersChat) {
                    if (Objects.equals(b.getBuyersEmailId(), buyerEmailId)) {
                        List<Message> existingMsg =  b.getMessage();
                        Message newMsg = chatMessage.getBuyersChat().get(0).getMessage().get(0);
                        existingMsg.add(newMsg);
                        b.setMessage(existingMsg);
                        b.setBuyersEmailId(buyerEmailId);
                        newChat.add(b);
                    } else {
                        newChat.add(b);
                    }
                }
                existingChat.setBuyersChat(newChat);
                try {
                    chatService.saveMessage(existingChat);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<String>("Exception", HttpStatus.CONFLICT);
                }
                return new ResponseEntity<List<BuyersChat>>(chatMessage.getBuyersChat(),HttpStatus.CREATED);
            }
        }
    }

    @PostMapping("/reply")
    public ResponseEntity<?> messageReply(@RequestBody ChatMessage chatMessage)
    {
        ChatMessage existingChat = null;
        try {
            existingChat = chatService.getChatById(chatMessage.getProductId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (existingChat == null) {
            try {
                chatService.saveMessage(chatMessage);
                return new ResponseEntity<ChatMessage>(chatMessage, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<String>("Exception", HttpStatus.CONFLICT);
            }
        } else {
            List<BuyersChat> buyersChat = existingChat.getBuyersChat();
            String buyerEmailId = chatMessage.getBuyersChat().get(0).getBuyersEmailId();
            List<BuyersChat> newChat = new ArrayList<>();
            for (BuyersChat b : buyersChat) {
                if (Objects.equals(b.getBuyersEmailId(), buyerEmailId)) {
                    List<Message> existingMsg =  b.getMessage();
                    Message msg = chatMessage.getBuyersChat().get(0).getMessage().get(0);
                    existingMsg.add(msg);
                    b.setMessage(existingMsg);
                    b.setBuyersEmailId(buyerEmailId);
                    newChat.add(b);
                } else {
                    newChat.add(b);
                }
            }
            existingChat.setBuyersChat(newChat);
            try {
                chatService.saveMessage(existingChat);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<String>("Exception", HttpStatus.CONFLICT);
            }
            return new ResponseEntity<List<BuyersChat>>(chatMessage.getBuyersChat(), HttpStatus.CREATED);
        }
    }



    @GetMapping("/message/{productId}/{sellerEmail}")
    public ResponseEntity<?> getChatByProductId(@PathVariable String productId, @PathVariable String sellerEmail) {
        try {
            ChatMessage chat = chatService.getAllMessage(productId);
            if (chat == null) {
                return new ResponseEntity<String>("No chat available", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<ChatMessage>(chat, HttpStatus.OK);
        } catch (Exception e ) {
            e.printStackTrace();
//            ChatMessage newProduct = new ChatMessage();
//
//            List<BuyersChat> neBuCh = new ArrayList<BuyersChat>();
//            List<Message> newMessage = new ArrayList<Message>();
//            BuyersChat nB = new BuyersChat();
//            newProduct.setProductId(productId);
//            newProduct.setSellerEmail(sellerEmail);
//            nB.setMessage(newMessage);
//            neBuCh.add(nB);
//            newProduct.setBuyersChat(neBuCh);
//            try {
//                chatService.saveMessage(newProduct);
//                return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
//            } catch (Exception exc) {
//                exc.printStackTrace();
//                return new ResponseEntity<String>("Exception", HttpStatus.CONFLICT);
//            }
            return new ResponseEntity<>("No product and chat available...", HttpStatus.NOT_IMPLEMENTED);
        }
    }


    @GetMapping("/{buyerEmailId}/{productId}")
    public ResponseEntity<?> getBuyersChatByProductId(@PathVariable String buyerEmailId,@PathVariable String productId)
    {
        try{
            ChatMessage buyer=chatService.getBuyerChat(buyerEmailId,productId);
            if(buyer==null)
            {
                return new ResponseEntity<String>("No chat available",HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<ChatMessage>(buyer,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Exception with product id"+productId,HttpStatus.CONFLICT);
        }
    }
}