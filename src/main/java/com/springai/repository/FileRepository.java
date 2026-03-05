package com.springai.repository;

import org.springframework.core.io.Resource;

public interface FileRepository {
    /**
     * 保存文件,记录chatId和文件的关系
     * @param chatId
     * @param resource
     * @return
     */
    boolean save(String chatId, Resource resource);

    /**
     * 根据chatId获取文件
     * @param chatId
     * @return
     */
    Resource getFile(String chatId);
}
