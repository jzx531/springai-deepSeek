package com.springai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.autoconfigure.ollama.OllamaAutoConfiguration;
import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = {
//        DataSourceAutoConfiguration.class,
//        com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class,
//        OpenAiAutoConfiguration.class, // 【新增】排除 OpenAI 自动配置
//        OllamaAutoConfiguration.class  // 【新增】排除 Ollama 自动配置 (可选，防止类似报错)
//})
@MapperScan("com.springai.mapper")

@SpringBootApplication
public class SpringaiDeepSeekApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringaiDeepSeekApplication.class, args);
    }

}
