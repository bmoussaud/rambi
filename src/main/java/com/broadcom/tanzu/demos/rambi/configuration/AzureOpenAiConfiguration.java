package com.broadcom.tanzu.demos.rambi.configuration;

import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.azure.openai.AzureOpenAiImageModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "rambi", name = "chatProvider", havingValue = "azure")
public class AzureOpenAiConfiguration {

    @Bean
    @Primary
    ChatClient.Builder myChatClientProvider(AzureOpenAiChatModel model) {
        return ChatClient.builder(model);
    }

    @Bean
    boolean pitchServiceLoadImages() {
        return false;
    }

    @Bean
    Map<String, String> pitchServiceLLMConfiguration(AzureOpenAiChatModel chatModel, AzureOpenAiImageModel imageModel) {
        return Map.of("model", "Azure",
                "provider", "OpenAI",
                "chatDeployment", chatModel.getDefaultOptions().getDeploymentName(),
                "imageDeployment", imageModel.getDefaultOptions().getDeploymentName()
        );
    }

}
