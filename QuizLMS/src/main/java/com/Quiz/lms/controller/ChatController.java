//package com.Quiz.lms.controller;
//
//import com.Quiz.lms.service.ChatService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/chat")
//public class ChatController {
//
//    @Autowired
//    private ChatService chatService;
//
//    @PostMapping
//    public String chat(@RequestBody String message) {
//        return chatService.processMessage(message);
//    }
//}