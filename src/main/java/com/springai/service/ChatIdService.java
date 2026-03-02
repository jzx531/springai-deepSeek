package com.springai.service;

import com.springai.mapper.ChatIdMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ChatIdService {

   void deleteById(String type,String chatId);

   void save(@Param("type") String type, @Param("chatId") String chatId);

    List<String> getChatIds(@Param("type") String type);

    void delete(@Param("type") String type);

}
