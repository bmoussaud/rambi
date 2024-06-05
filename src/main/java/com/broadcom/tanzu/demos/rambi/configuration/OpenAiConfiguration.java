package com.broadcom.tanzu.demos.rambi.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty(prefix = "rambi", name = "chatProvider", havingValue = "openai")
public class OpenAiConfiguration {

    @Bean
    @Primary
    ChatClient.Builder myChatClientProvider(OpenAiChatModel model) {
        return ChatClient.builder(model);
    }
}
