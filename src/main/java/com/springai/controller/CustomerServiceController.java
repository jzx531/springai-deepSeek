package com.springai.controller;

import com.springai.repository.ChatHistoryRepository;
import com.springai.service.ChatIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class CustomerServiceController {

    private final ChatClient serviceChatClient;

    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatIdService chatIdService;
    /*
    🚨 核心错误
    错误类型: java.lang.IllegalStateException
    错误信息: Currently only one tool call is supported per message!
    含义: Spring AI (版本 1.0.0-M6) 的流式处理组件 (OpenAiStreamFunctionCallingHelper) 检测到模型在单条消息中返回了多个工具调用 (Tool Calls)，但当前版本不支持这种并行调用，只允许一次调用一个工具。
    */

/*
    @RequestMapping(value = "/service", produces = "text/html;charset=utf-8")
    public Flux<String> service(@RequestParam("prompt") String  prompt, @RequestParam("chatId") String  chatId) {
        // 1.保存会话id
        chatHistoryRepository.save("service", chatId);
        chatIdService.save("service", chatId);
        // 2.请求模型
        return serviceChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }*/


    @RequestMapping(value = "/service", produces = "text/html;charset=utf-8")
    public String service(@RequestParam("prompt") String  prompt,@RequestParam("chatId") String  chatId) {
        // 1.保存会话id
        chatHistoryRepository.save("service", chatId);
        chatIdService.save("service", chatId);
        // 2.请求模型
        return serviceChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .call()
                .content();
    }

}