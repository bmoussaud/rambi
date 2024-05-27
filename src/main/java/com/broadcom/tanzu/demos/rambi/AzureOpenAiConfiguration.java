package com.broadcom.tanzu.demos.rambi;

import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty(prefix = "rambi", name = "chatProvider", havingValue = "azure")
public class AzureOpenAiConfiguration {

    @Bean
    @Primary
    ChatClient.Builder myChatClientProvider(AzureOpenAiChatModel model) {
        return ChatClient.builder(model);
    }
}
