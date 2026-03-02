package com.springai.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatIdMapper {

    /**
     * 插入聊天ID
     * 如果 (type, chat_id) 已存在，则忽略插入 (不报错也不更新)
     *
     * @param type 类型
     * @param chatId 聊天会话ID
     */
    @Insert("INSERT INTO chat_id (type, chat_id) " +
            "VALUES (#{type}, #{chatId}) " +
            "ON DUPLICATE KEY UPDATE chat_id = VALUES(chat_id)")
    void save(@Param("type") String type, @Param("chatId") String chatId);

    /**
     * 根据type查找所有chatId
     * @param type
     * @return
     */
    @Select("SELECT chat_id from chat_id where type=#{type}")
    List<String> getChatIds(@Param("type") String type);

    /**
     * 根据类型删除记录
     * 修正点: DELETE 语句不需要 '*', 且使用 '='
     */
    @Delete("DELETE FROM chat_id WHERE type = #{type}")
    void delete(@Param("type") String type);

    /**
     * (可选) 根据 type 和 chatId 删除单条记录
     */
    @Delete("DELETE FROM chat_id WHERE type = #{type} AND chat_id = #{chatId}")
    void deleteById(@Param("type") String type, @Param("chatId") String chatId);

}
