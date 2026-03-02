package com.springai.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

@NoArgsConstructor
@Data
public class MessageVO {
    private String role;
    private String content;

    public MessageVO(Message message){
        MessageType messageType = message.getMessageType();
        switch (messageType){
            case USER:
                this.role = "user";break;

            case ASSISTANT:
                this.role = "assistant";break;
            case SYSTEM:
                this.role = "system"   ;break;
            case TOOL:
                this.role = "function";break;
            default:
                this.role = "unknown";break;
        }
        this.content= message.getText();
    }
}
