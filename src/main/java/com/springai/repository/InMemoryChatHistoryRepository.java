package com.springai.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryChatHistoryRepository implements ChatHistoryRepository {
    private final Map<String,List<String>> chatHistory = new HashMap<>();//使用内存进行存储

    @Override
    public void save(String type,String chatId){
        /*
        if(!chatHistory.containsKey(type)){
            chatHistory.put(type,new ArrayList<>());
        }
        List<String> chatIds = chatHistory.get(type);*/
        //上面代码的简化写法
        List<String> chatIds = chatHistory.computeIfAbsent(type,key->new ArrayList<>());
        if(!chatIds.contains(chatId)){
            chatIds.add(chatId);
        }
    }

    @Override
    public void delete(String type,String chatId){
        List<String> chatIds = chatHistory.get(type);
        if(chatIds.contains(chatId)){
            chatIds.remove(chatId);
        }
    }

    @Override
    public List<String> getChatIds(String type){
        /*
        List<String> chatIds = chatHistory.get(type);
        return chatIds!=null?chatIds:new ArrayList<>();*/
//        上面的代码简化为

        return chatHistory.getOrDefault(type,new ArrayList<>());
    }

}
