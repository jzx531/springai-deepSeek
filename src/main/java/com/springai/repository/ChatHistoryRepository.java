package com.springai.repository;

import java.util.List;

public interface ChatHistoryRepository {
    /**
     * 保存会话记录
     * @param type
     * @param chatId
     */
    void save(String type,String chatId);

    /**
     * 删除会话记录
     * @param type
     * @param chatId
     */
    void delete(String type,String chatId);

    /**
     * 获取会话id列表
     * @param type
     * @return
     */
    List<String> getChatIds(String type);
}
