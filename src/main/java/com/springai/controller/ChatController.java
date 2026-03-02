package com.springai.controller;


//import com.springai.repository.ChatHistoryRepository;
import com.springai.repository.ChatHistoryRepository;
import com.springai.service.ChatIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.Media;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    private final ChatHistoryRepository chatHistoryRepository;

    private final ChatIdService  chatIdService;

    /* //阻塞式调用
    @RequestMapping("/chat")
    public String chat(@RequestParam("prompt") String  prompt){
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }*/

    @RequestMapping(value = "/chat",produces = "text/html;charset=utf-8")
    public Flux<String> chat(@RequestParam("prompt") String  prompt,@RequestParam("chatId") String  chatId){
        //保存会话id
        chatHistoryRepository.save("chat",chatId);
        chatIdService.save("chat",chatId);

        return chatClient.prompt()
                .user(prompt)
                .advisors(a->a.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId))
                .stream()
                .content();
    }

}
