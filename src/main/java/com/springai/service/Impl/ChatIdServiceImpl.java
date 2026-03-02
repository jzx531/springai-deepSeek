package com.springai.service.Impl;

import com.springai.mapper.ChatIdMapper;
import com.springai.service.ChatIdService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor // 【关键】Lombok 会自动生成一个包含所有 final 字段的构造函数
public class ChatIdServiceImpl implements ChatIdService {

    @Autowired
    private ChatIdMapper chatIdMapper;

    @Override
    public void deleteById(String type,String chatId)
    {
        chatIdMapper.deleteById(type,chatId);
    }

    @Override
    public void save(@Param("type") String type, @Param("chatId") String chatId)
    {
        chatIdMapper.save(type,chatId);
    }

    @Override
    public List<String> getChatIds(@Param("type") String type)
    {
        List<String> ChatIds = chatIdMapper.getChatIds(type);
        return ChatIds==null?new ArrayList<String>():ChatIds;
    }

    @Override
    public void delete(@Param("type") String type)
    {
        chatIdMapper.delete(type);
    }

}
