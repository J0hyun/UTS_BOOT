package com.mbc.repository;

import com.mbc.dto.ChatRoomDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChatRoomRepository {

    private Map<String, ChatRoomDTO> chatRoomDTOMap; // 채팅방 맵
    private Map<Long, Map<Long, String>> itemToRoomMap; // itemId와 memberId에 따른 roomId 매핑

    @PostConstruct
    private void init(){
        chatRoomDTOMap = new LinkedHashMap<>();
        itemToRoomMap = new HashMap<>();
    }

    // 상품과 연결된 채팅방 조회 (itemId와 memberId로)
    public ChatRoomDTO findRoomByItemIdAndMemberId(Long itemId, Long memberId) {
        Map<Long, String> memberMap = itemToRoomMap.get(itemId);
        String roomId = memberMap != null ? memberMap.get(memberId) : null;
        return roomId != null ? chatRoomDTOMap.get(roomId) : null;
    }

    // 상품에 대한 1:1 채팅방 생성 (itemId와 memberId 기반)
    public ChatRoomDTO createChatRoomForItemAndMember(Long itemId, Long memberId) {
        // 새로운 채팅방 생성
        String roomName = "상품" + itemId + " - 사용자" + memberId;
        ChatRoomDTO room = ChatRoomDTO.create(roomName);

        // 채팅방과 상품, 사용자 연결
        chatRoomDTOMap.put(room.getRoomId(), room);
        itemToRoomMap
                .computeIfAbsent(itemId, k -> new HashMap<>())
                .put(memberId, room.getRoomId()); // 상품과 사용자에 대해 채팅방 연결

        return room;
    }

    public List<ChatRoomDTO> findAllRooms(){
        //채팅방 생성 순서 최근 순으로 반환
        List<ChatRoomDTO> result = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(result);

        return result;
    }

    public ChatRoomDTO findRoomById(String id){
        return chatRoomDTOMap.get(id);
    }

    public ChatRoomDTO createChatRoomDTO(String name){
        ChatRoomDTO room = ChatRoomDTO.create(name);
        chatRoomDTOMap.put(room.getRoomId(), room);

        return room;
    }
}
