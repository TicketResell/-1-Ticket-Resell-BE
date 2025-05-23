package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.converter.ChatMessageConverter;
import com.teamseven.ticketresell.dto.ChatMessageDTO;
import com.teamseven.ticketresell.dto.ConversationDTO;
import com.teamseven.ticketresell.entity.ChatMessageEntity;
import com.teamseven.ticketresell.entity.UserEntity;
import com.teamseven.ticketresell.repository.ChatMessageRepository;
import com.teamseven.ticketresell.repository.UserRepository;
import com.teamseven.ticketresell.service.impl.ChatService;
import com.teamseven.ticketresell.service.impl.UserService;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private ChatMessageConverter chatMessageConverter;
    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        if (message == null || message.getUser1_id() == null || message.getUser2_id() == null || message.getMessageContent() == null) {
            throw new IllegalArgumentException("Invalid message data");
        }

        // Log thông điệp
        System.out.println("Received message: {}" + message);

        try {
            return chatService.sendMessage(message);
        } catch (Exception e) {
            System.out.println("Error sending message");
            throw new RuntimeException("Failed to send message");
        }
    }


    // Lấy lịch sử chat qua WebSocket
    @MessageMapping("/chat/history")
    @SendTo("/topic/history")
    public List<ChatMessageDTO> getChatHistory(Long userId) {
        return chatService.getChatHistory(userId);
    }

    @GetMapping("chat-history/{id1}/{id2}")
    public ResponseEntity<List<ChatMessageDTO>> getChatttHistory(@PathVariable Long id1, @PathVariable Long id2) {
        List<ChatMessageEntity> en = chatMessageRepository.findByUser1AndUser2(id1, id2);
        List<ChatMessageDTO> dtos = new ArrayList<>();
        for (ChatMessageEntity chatMessageEntity : en) {
            dtos.add(chatMessageConverter.toDTO(chatMessageEntity));
        }
        return ResponseEntity.ok(dtos);
    }

    //ONLY FOR STAFF!!!
    @PostMapping("/get-chat-history")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistoryByPairUserId(Long userId, Long user2Id) {
        return ResponseEntity.ok(chatService.getChatHistory(userId, user2Id));
    }
    //ĐÂY LÀ API CHÍNH ĐỂ CALL CONVERSATION!!!!
    //MUST READ DTO -> CONVERSATION DTO DATA TYPE
    @MessageMapping("/chat/conversations")
    @SendTo("/topic/conversations")
    public List<ConversationDTO> getConversations(@RequestParam Long userId) {
        return chatService.getConversationsByUserId(userId);
    }

    @MessageMapping("/chat/set-online-status")
    @SendTo("/topic/set-online-status")
    public ResponseEntity<String> setOnlineStatus(@RequestParam Long userId) {
        UserEntity  user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        user.setStatus("online");
        user.setLastSeen(LocalDateTime.now());
        return ResponseEntity.ok("");
    }

    @MessageMapping("/chat/get-online-status")
    @SendTo("/topic/get-online-status")
    public String getOnlineStatus(@RequestParam Long userId) {
        UserEntity  user = userRepository.findById(userId).orElse(null);
        boolean isOnline = userService.isOnline(userId);
        if (user == null) return ("User not found");
        LocalDateTime lastSeen  = user.getLastSeen();
        // Trả về trạng thái của người dùng
        return isOnline ? "User " + userId + " is online." : "User " + userId + " is offline." + "last seen: " + lastSeen;
    }


    @MessageMapping("/chat/check-conversation-exist-ws")
    @SendTo("/topic/check-conversation-ws")
    public ConversationDTO checkConversationExistWebSocketVersion(Long userId, Long user2Id) {

        List<ChatMessageEntity> entities = chatMessageRepository.findByUser1AndUser2(userId,user2Id);
        ConversationDTO conversationDTO = new ConversationDTO();
        if (entities.isEmpty()) {

            conversationDTO.setUser1(userId);
            conversationDTO.setUser2(user2Id);
            conversationDTO.setUser1FullName(userService.getFullNameByID(userId));
            conversationDTO.setUser2FullName(userService.getFullNameByID(user2Id));
            return conversationDTO;
        } else {
            return null;
        }
    }

    @PostMapping("/check-conversation/{userId}/{user2Id}")
    public ResponseEntity<?> checkExistingConversation(
            @PathVariable Long userId,
            @PathVariable Long user2Id
    ) {

        List<ChatMessageEntity> entities = chatMessageRepository.findByUser1AndUser2(userId, user2Id);

        ChatMessageEntity entity = null;
        if (entities.isEmpty()) {
            //thay vì tạo 1 conversation, ta sẽ tạo 1 chat message welcome để tự sinh conversation
            entity = new ChatMessageEntity();
            entity.setUser1(userRepository.findById(user2Id).orElse(null));
            entity.setUser2(userRepository.findById(userId).orElse(null)); //ngược lại vì người bán welcome
            entity.setMessageContent("Xin chào, tôi có thể hỗ trợ gì cho bạn?");
            entity.setMessageType("text");
            entity.setRead(false);
            entity.setCreatedDate(LocalDateTime.now());

            chatMessageRepository.save(entity);
            chatMessageRepository.flush();
            return ResponseEntity.ok(entity);
        } else {
            return ResponseEntity.ok(entities);
        }
    }

    @PostMapping("/chat/set-hasRead-status/{userId}/{user2Id}")
    public ResponseEntity<Boolean> getRead(@PathVariable  Long userId,@PathVariable Long user2Id) {
        List<ChatMessageEntity> entities = chatMessageRepository.findByUser1AndUser2(userId, user2Id); //loì ra mọi đoạn chat
        for(ChatMessageEntity chatMessageEntity : entities) {
            chatMessageEntity.setRead(true);
        }
        chatMessageRepository.saveAll(entities);
        chatMessageRepository.flush();
        return ResponseEntity.ok(true);
    }
}
