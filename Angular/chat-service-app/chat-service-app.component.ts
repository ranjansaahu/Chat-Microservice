import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChatMessage } from '../model/chat/chat-message.model';
import { ChatService } from '../service/chat/chat.service';
import { BuyersChat } from '../model/chat/buyers-chat.model';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Message } from '../model/chat/message.model';
import { BuyerChatWithProductId } from '../model/chat/buyer-chat-with-product-id.model';


@Component({
  selector: 'app-chat-service-app',
  templateUrl: './chat-service-app.component.html',
  styleUrls: ['./chat-service-app.component.scss']
})
export class ChatServiceAppComponent implements OnInit {

  constructor(private chatService: ChatService) {
    this.buyersList = [];
    this.chatMessageList = [];
  }


  ngOnInit(): void {
    if (localStorage.getItem("ROLE") == null) {
      localStorage.setItem("ROLE", "Seller")
    }
    //console.log("getting not existing one from localstorage gices "+localStorage.getItem("ARAVIND"))
    //localStorage.setItem("ROLE", "Buyer");
    //localStorage.setItem("buyerEmail", "swapper@gmail.com"); //login user's email
    //localStorage.setItem("PRODUCT_ID_FOR_BUYER_UI", "11111");
    //localStorage.setItem("SELLER_EMAIL_FOR_BUYER_CHAT_UI","swap-on@gmail.com");
    //console.log("role is " + localStorage.getItem("ROLE") + "  and email is " + localStorage.getItem("buyerEmail"));

    this.displaySellerForBuyerScreen = localStorage.getItem("SELLER_EMAIL_FOR_BUYER_CHAT_UI");
    this.role=localStorage.getItem("ROLE")
    this.inputMessage = new FormGroup({ "message": new FormControl('', [Validators.required]), });

    if (localStorage.getItem("ROLE") == "Seller") {
      this.getBuyersList();
    }
    if (localStorage.getItem("ROLE") == "Buyer") {
      this.getSellerChat();
    }
  }
  displaySellerForBuyerScreen: string;
  Users = ["pavan", "srinivas", "debash", "priya", "akash", "aravind", "sahoo", "Karthik", "mudit", "1234"];
  userName: any;
  message: string;
  sellerEmail = "pavan@gmail.com";
  buyerEmail = "saaho@gmail.com";
  email: string;
  productId = "102";
  buyersList: BuyerChatWithProductId[];
  chatMessageList: any[];
  chatFromBackend: ChatMessage;
  buyers: any;
  chatMessages: ChatMessage;
  buyersChat: BuyersChat[];
  buyerEmailId: any;
  isSeller: boolean;
  isBuyer: boolean;
  role: string;
  productsList: ChatMessage[];
  inputMessage: FormGroup;
  MessageObj: Message = new Message();

 // buyer varibles

 test() {
  console.log("ngmodel is "+this.message)
 }

  getChat(userName: string) {
    this.buyerEmailId = userName + '@gmail.com';
    this.chatService.getChatApi(this.productId, this.buyerEmailId).subscribe((tmp: ChatMessage) => {
      this.chatFromBackend = tmp;
      this.buyers = this.chatFromBackend.buyersChat;
      console.log("no of buyers: " + this.buyers.length);
      console.log("Backend data is : " + this.chatFromBackend.buyersChat[0].buyersEmailId);
    }
    )
    console.log("DEBUG:  username is " + userName)
  }

  buyerProductId: string;
  msgSent = false
  getBuyersList() {
    console.log("Came into getBuyersList function !!!!!!!!!!!!!!!!!!!!!!")
    this.role = localStorage.getItem("ROLE")
    this.email = localStorage.getItem("buyerEmail")
    if (this.role == "Seller") {
      this.isSeller = true;
      this.isBuyer = false;
      this.chatService.getBuyers(this.email).subscribe((response: BuyerChatWithProductId[]) => {
        this.buyersList = response;
        for (let i = 0; i < response.length; i++) {
          console.log("buyer email: " + this.buyersList[i].buyersChat.buyersEmailId)
        }
        if (this.msgSent) {
          this.msgSent = false
          this.getBuyerChat(this.displayReceiver)
        }
      })
    }
  }

  displayChat: Message[];
  displayReceiver: string;
  displaySender: string;
  isChatClicked = false;
  getBuyerChat(buyerEmailFromUI: string) 
  {
    this.isChatClicked = false;
    console.log("inside getBuyerChat() functino" + buyerEmailFromUI);
    this.displaySender = localStorage.getItem("buyerEmail");
    for (let i = 0; i < this.buyersList.length; i++) {
      if (this.buyersList[i].buyersChat.buyersEmailId  == buyerEmailFromUI) {
        console.log("email " + this.buyersList[i].buyersChat.buyersEmailId);
        this.displayChat = this.buyersList[i].buyersChat.message;
        this.displayReceiver = this.buyersList[i].buyersChat.buyersEmailId;
        this.productId = this.buyersList[i].productId
      }
    }
  }

  buyer: string;
  seller: string;
  sellerChat: ChatMessage;
  getSellerChat() {
    console.log("Inside getSellerChat Function");
    this.buyer = localStorage.getItem("buyerEmail"); 
    this.buyerProductId = localStorage.getItem("PRODUCT_ID_FOR_BUYER_UI")
    console.log("productId is "+this.buyerProductId+" buyer is "+ this.buyer);
    this.chatService.getSpecificChat( this.buyer,this.buyerProductId).subscribe((buyerChat:BuyerChatWithProductId) => {
      console.log("getSelerChat");
      this.sellerChat = buyerChat.buyersChat[0].message;
     this.seller = buyerChat.buyersChat[0].sellerEmailId;
    })
  }

  getProductsForBuyer() {
    console.log("Came into Buyer function !!!!!!!!!!!!!!!!!!!!!!")
    this.role = localStorage.getItem("ROLE")
    this.email = localStorage.getItem("buyerEmail")
    if (this.role == "Buyer") {
      this.isSeller = false;
      this.isBuyer = true;
      this.chatService.getProductsOfBuyer(this.email).subscribe((response: ChatMessage[]) => {
        this.productsList = response;
        for (let i = 0; i < response.length; i++) {
          console.log("buyer email: " + this.productsList[i].productId);
        }
      });
    }
  }

  sendChatMessage: ChatMessage = new ChatMessage();
  sendBuyersChat: BuyersChat = new BuyersChat();
  sendMsg: Message = new Message();
  
   
  sendMessage() {

    const currentDate = new Date();
    const timestamp = currentDate.getTime();
    console.log("before setting message " + this.inputMessage.value.message);
    this.sendMsg.message = this.inputMessage.value.message;
    this.inputMessage.reset();
    console.log("inside sendmessage after setting message")
    this.sendMsg.messageSendBy = this.displaySender.split("@")[0];
    this.sendMsg.messageSendOn =  timestamp;
    this.sendBuyersChat.buyersEmailId = this.displayReceiver;
    this.sendBuyersChat.message = [this.sendMsg];
    this.sendChatMessage.buyersChat = [this.sendBuyersChat];
    this.sendChatMessage.sellerEmail = this.displaySender;
    this.sendChatMessage.productId = this.productId;
    this.chatService.sendMessage(this.sendChatMessage).subscribe((tmp)=>{
      console.log("SendMessage function - "+tmp[0].buyersEmailId)
      //this.getBuyerChat(this.displayReceiver)
      this.msgSent = true;
      this.getBuyersList();
      
      
    })
    location.reload();
  }

  sendMessageBuyer() {
    const currentDate = new Date();
    const timestamp = currentDate.getTime();
    console.log("before setting message " + this.inputMessage.value.message);
    this.sendMsg.message = this.inputMessage.value.message;
    this.inputMessage.reset();
    console.log("inside sendmessage after setting message")
    this.sendMsg.messageSendBy = this.buyer.split("@")[0];
    this.sendMsg.messageSendOn =  timestamp;
    this.sendBuyersChat.buyersEmailId = this.buyer;
    this.sendBuyersChat.message = [this.sendMsg];
    this.sendChatMessage.buyersChat = [this.sendBuyersChat];
    this.sendChatMessage.sellerEmail = this.displaySellerForBuyerScreen;
    this.sendChatMessage.productId = this.buyerProductId;
    this.chatService.sendMessage(this.sendChatMessage).subscribe((tmp)=>{
      //console.log("SendMessage function - "+tmp[0].buyersEmailId)
      //this.getBuyerChat(this.displayReceiver)
      this.getSellerChat();
    })
    location.reload();
  }

//   onStart():void {
//     this.chatService.videoApi().subscribe(
//       Response=>{
//         console.log("video call started")
//       }
//     )

// }

}