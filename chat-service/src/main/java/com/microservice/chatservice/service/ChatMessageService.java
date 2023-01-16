package com.microservice.chatservice.service;

import com.microservice.chatservice.model.BuyerChatWithProductId;
import com.microservice.chatservice.model.BuyersChat;
import com.microservice.chatservice.repository.ChatMessageRepository;
import com.microservice.chatservice.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ChatMessageService implements ChatService {

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public List<BuyerChatWithProductId> getBuyersList(String sellerEmail) {
        List<BuyerChatWithProductId> buyerList = new ArrayList<BuyerChatWithProductId>();
        List<ChatMessage> allProducts = chatMessageRepository.findAll();
        for( ChatMessage product: allProducts) {
            if (Objects.equals(product.getSellerEmail(), sellerEmail)) {
                for(BuyersChat buyersChat: product.getBuyersChat()){
                    BuyerChatWithProductId temp = new BuyerChatWithProductId();
                    temp.setProductId(product.getProductId());
                    temp.setBuyersChat(buyersChat);
                    buyerList.add(temp);
                }
            }
        }
        return buyerList;
    }

    @Override
    public List<ChatMessage> getProductsOfBuyer(String buyerEmail) {
        List<ChatMessage> productList = new ArrayList<ChatMessage>();
       // System.out.println("Inside function!!!!!!!!!!!!!!!!!!!");
        List<ChatMessage> allProducts = chatMessageRepository.findAll();
        for( ChatMessage product: allProducts) {
            //System.out.println("Inside first for loop!!!!!!!!!!!!!!!!!!!");
            for( BuyersChat eachBuyer: product.getBuyersChat()) {
                System.out.println("Inside second for loop!!!!!!!!!!!!!!!!!!!");
                //System.out.println("eachBuyer.getBuyeremailid is "+eachBuyer.getBuyersEmailId()+"and buyerEmail is "+buyerEmail);
                if (Objects.equals(eachBuyer.getBuyersEmailId(), buyerEmail)) {
                   // System.out.println("HELLOOOOOOOOOOOOOOOOOOOOOOO");
                    ChatMessage temp = new ChatMessage() ;
                    List<BuyersChat> tempBuyersChat = new ArrayList<>();
                    temp.setProductId(product.getProductId());
                    temp.setSellerEmail(product.getSellerEmail());
                    tempBuyersChat.add(eachBuyer);
                    temp.setBuyersChat(tempBuyersChat);
                    productList.add(temp);
                    break;
                }
            }
        }
        return productList;
    }

    @Override
    public ChatMessage saveMessage(ChatMessage chatMessage) throws Exception {
        ChatMessage chatMessage1 = chatMessageRepository.save(chatMessage);
        return chatMessage1;
    }

    @Override
    public ChatMessage getAllMessage(String productId) throws Exception {
        ChatMessage chatMessage2 = chatMessageRepository.findById(productId).get();
        return chatMessage2;
    }



    @Override
    public ChatMessage getBuyerChat(String buyerEmailId, String productId) throws Exception {
        try {
            System.out.println("inside service funciton before findBYid......................");
            ChatMessage existingChat = chatMessageRepository.findById(productId).get();
            System.out.println("AFTER findBYid.................");
            ChatMessage retChatMsg = new ChatMessage();

            List<BuyersChat> buyers = existingChat.getBuyersChat();
            BuyersChat c = new BuyersChat();
            for (BuyersChat b : buyers) {
                if (Objects.equals(b.getBuyersEmailId(), buyerEmailId)) {
                    c.setBuyersEmailId(b.getBuyersEmailId());
                    c.setMessage(b.getMessage());
                }
            }
            List<BuyersChat> tmp = new ArrayList<>();
            tmp.add(c);
            retChatMsg.setBuyersChat(tmp);
            retChatMsg.setSellerEmail(existingChat.getSellerEmail());
            retChatMsg.setProductId(existingChat.getProductId());
            return retChatMsg;
        } catch (Exception e) {
            throw new Exception("buyer chat not available");
        }
    }

    @Override
    public ChatMessage getChatById(String productId) throws  Exception {
        try {
            return chatMessageRepository.findById(productId).get();
        } catch (Exception e ) {
            throw new Exception("no chat available");
        }

    }
}

