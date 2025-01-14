package com.mbc.controller;

import com.mbc.dto.ChatMessageDTO;
import com.mbc.entity.ChatMessage;
import com.mbc.entity.ChatRoom;
import com.mbc.repository.ChatMessageRepository;
import com.mbc.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 입장 메시지 처리
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO messageDTO) {
        Long roomId = messageDTO.getRoomId();
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid roomId"));

        messageDTO.setMessage(messageDTO.getWriter() + "님이 채팅방에 참여하였습니다.");
        saveMessage(messageDTO, chatRoom);
        template.convertAndSend("/sub/chat/room/" + roomId, messageDTO);
    }

    // 일반 메시지 처리
    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO messageDTO) {
        Long roomId = messageDTO.getRoomId();
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid roomId"));

        saveMessage(messageDTO, chatRoom);
        template.convertAndSend("/sub/chat/room/" + roomId, messageDTO);
    }

    // 메시지 저장
    private void saveMessage(ChatMessageDTO messageDTO, ChatRoom chatRoom) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId(messageDTO.getRoomId());
        chatMessage.setWriter(messageDTO.getWriter());
        chatMessage.setMessage(messageDTO.getMessage());
        chatMessage.setChatRoom(chatRoom);

        chatMessageRepository.save(chatMessage);
    }
}
