package com.springai.controller;

import com.springai.entity.vo.Result;
import com.springai.repository.ChatHistoryRepository;
import com.springai.repository.FileRepository;
import com.springai.service.ChatIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor.FILTER_EXPRESSION;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/pdf")
public class PdfController {

    private final FileRepository fileRepository;

    private final VectorStore vectorStore;

    private final ChatClient pdfChatClient;

    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatIdService chatIdService;

    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt,String chatId){
//        1.找到会话文件
        Resource file = fileRepository.getFile(chatId);
        if(!file.exists()){
            log.info("不存在对应的file");
            throw new RuntimeException("文件不存在");
        }
        //2. 保存会话id
        chatHistoryRepository.save("pdf",chatId);
        chatIdService.save("pdf",chatId);

// 3.请求模型
        return pdfChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .advisors(a -> a.param(FILTER_EXPRESSION, "file_name == '" + file.getFilename() + "'"))
                .stream()
                .content();
    }

    /**
     * 文件上传
     * @param chatId
     * @param file
     * @return
     */
    @RequestMapping("/upload/{chatId}")
    public Result uploadPdf(@PathVariable String chatId, @RequestParam("file") MultipartFile file) {
        try{
            //检查是否是pdf格式
            if(file.isEmpty()){
                return Result.fail("不能为空文件");
            }
            if (!file.getContentType().startsWith("application/pdf")) {
                return Result.fail("请上传pdf");
            }
//            2.保存文件
            boolean success = fileRepository.save(chatId,file.getResource());
            if(!success){
                return Result.fail("文件保存失败");
            }
//           3.写入向量库
            this.writeToVectorStore(file.getResource());
            return Result.ok();
        } catch (Exception e) {
            log.error("Failed to upload PDF.", e);
            return Result.fail("上传文件失败！");
        }
    }

    /**
     * 文件下载
     * @param chatId
     * @return
     * @throws IOException
     */
    @GetMapping("/file/{chatId}")
    public ResponseEntity<Resource> download(@PathVariable("chatId") String chatId) throws IOException {
//        1.读取文件
        Resource file = fileRepository.getFile(chatId);
        if(!file.exists()){
            return ResponseEntity.notFound().build();
        }

//        2.文件名编码写入响应头
        String fileName = URLEncoder.encode(Objects.requireNonNull(file.getFilename()), StandardCharsets.UTF_8);
//        3.返回文件
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file);
    }

    private void writeToVectorStore(Resource resource){
        //        1.创建pdf读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource,//文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1)
                        .build()
        );
//        2.读取pdf文档,拆分为Document
        List<Document> documents = reader.read();
//        3.写入向量库
        vectorStore.add(documents);
    }

}
