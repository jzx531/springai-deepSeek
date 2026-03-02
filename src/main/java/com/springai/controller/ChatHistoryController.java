package com.springai.controller;

//import com.springai.config.InSqlChatMemory;
import com.springai.entity.vo.MessageVO;
//import com.springai.enums.ChatType;
import com.springai.entity.vo.MessageVO;
import com.springai.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 历史会话id和会话内容记录
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {
    private final ChatHistoryRepository chatHistoryRepository;

    private final ChatMemory chatMemory;

    @GetMapping("/{type}")
    public Object getChatIds(@PathVariable("type") String type){
        return chatHistoryRepository.getChatIds(type);
    }

    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getMessages(@PathVariable("type") String type, @PathVariable("chatId") String chatId){
        List<Message> OriginMessages = chatMemory.get(chatId,Integer.MAX_VALUE);
        if(OriginMessages==null){
            return List.of();
        }
        return OriginMessages.stream().map(MessageVO::new).toList();
    }
}
