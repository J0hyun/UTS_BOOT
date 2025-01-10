package com.mbc.controller;


import com.mbc.dto.ChatRoomDTO;
import com.mbc.entity.Item;
import com.mbc.repository.ChatRoomRepository;
import com.mbc.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class ChatRoomController {

    private final ChatRoomRepository repository;
    private final ItemRepository itemRepository; // 상품 관련 repository 추가

    // 상품 페이지에서 채팅방으로 이동 (1:1 채팅을 위해 memberId 추가)
    @GetMapping("/item/{itemId}/{memberId}")
    public String getItemChat(@PathVariable Long itemId, @PathVariable Long memberId, Model model) {
        // 상품 정보 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid itemId: "));

        // 상품에 연결된 채팅방 조회 (itemId와 memberId로 조회)
        ChatRoomDTO chatRoom = repository.findRoomByItemIdAndMemberId(itemId, memberId);
        if (chatRoom == null) {
            // 채팅방이 없으면 새로 생성
            chatRoom = repository.createChatRoomForItemAndMember(itemId, memberId);
        }

        model.addAttribute("item", item);
        model.addAttribute("room", chatRoom);

        return "chat/room";
    }


    //채팅방 목록 조회
    @GetMapping(value = "/rooms")
    public ModelAndView rooms(){

        log.info("# All Chat Rooms");
        ModelAndView mv = new ModelAndView("chat/rooms");

        mv.addObject("list", repository.findAllRooms());

        return mv;
    }

    //채팅방 개설
    @PostMapping(value = "/room")
    public String create(@RequestParam String name, RedirectAttributes rttr){

        log.info("# Create Chat Room , name: " + name);
        rttr.addFlashAttribute("roomName", repository.createChatRoomDTO(name));
        return "redirect:/chat/rooms";
    }

    //채팅방 조회
    @GetMapping("/room")
    public void getRoom(String roomId, Model model){

        log.info("# get Chat Room, roomID : " + roomId);

        model.addAttribute("room", repository.findRoomById(roomId));
    }
}
