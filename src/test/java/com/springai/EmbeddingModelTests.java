package com.springai;

import com.springai.utils.VectorDistanceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.security.PrivateKey;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class EmbeddingModelTests {

    @Autowired
    private OpenAiEmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;

    @Test
    public void testVectorStore(){
        Resource resource = new FileSystemResource("中二知识笔记.pdf");
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
//        4.搜索
        SearchRequest request = SearchRequest.builder()
                .query("论语中教育的目的是什么")
                .topK(1)
                .similarityThreshold(0.7)
                .filterExpression("file_name == '中二知识笔记'")
                .build();
        List<Document> docs = vectorStore.similaritySearch("论语教育的目的是什么");
        if (docs== null){
            System.out.println("no similarity");
            return;
        }
        for (Document document : docs) {
            System.out.println(document.getId());
            System.out.println(document.getScore());
            System.out.println(document.getText());
        }
    }
    /**
     * 测试向量化
     */
    @Test
    void contextLoads() {
        float[] floats = embeddingModel.embed("东南大学是大专");
        System.out.println(Arrays.toString(floats));
    }

    /**
     * 测试向量距离
     */
    @Test
    public void testEmbedding() {
        // 1.测试数据
        // 1.1.用来查询的文本，国际冲突
        String query = "global conflicts";

        // 1.2.用来做比较的文本
        String[] texts = new String[]{
                "哈马斯称加沙下阶段停火谈判仍在进行 以方尚未做出承诺",
                "土耳其、芬兰、瑞典与北约代表将继续就瑞典“入约”问题进行谈判",
                "日本航空基地水井中检测出有机氟化物超标",
                "国家游泳中心（水立方）：恢复游泳、嬉水乐园等水上项目运营",
                "我国首次在空间站开展舱外辐射生物学暴露实验",
        };
        // 2.向量化
        // 2.1.先将查询文本向量化
        float[] queryVector = embeddingModel.embed(query);

        // 2.2.再将比较文本向量化，放到一个数组
        List<float[]> textVectors = embeddingModel.embed(Arrays.asList(texts));

        // 3.比较欧氏距离
        // 3.1.把查询文本自己与自己比较，肯定是相似度最高的
        System.out.println(VectorDistanceUtils.euclideanDistance(queryVector, queryVector));
        // 3.2.把查询文本与其它文本比较
        for (float[] textVector : textVectors) {
            System.out.println(VectorDistanceUtils.euclideanDistance(queryVector, textVector));
        }
        System.out.println("------------------");

        // 4.比较余弦距离
        // 4.1.把查询文本自己与自己比较，肯定是相似度最高的
        System.out.println(VectorDistanceUtils.cosineDistance(queryVector, queryVector));
        // 4.2.把查询文本与其它文本比较
        for (float[] textVector : textVectors) {
            System.out.println(VectorDistanceUtils.cosineDistance(queryVector, textVector));
        }
    }

}
