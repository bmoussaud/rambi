package org.moussaud.demos.moviegenerator.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "rambi", name = "chatProvider", havingValue = "openai")
public class OpenAiConfiguration {

    @Bean
    @Primary
    ChatClient.Builder myChatClientProvider(OpenAiChatModel model) {
        return ChatClient.builder(model);
    }

    @Bean
    boolean pitchServiceLoadImages() {
        return false;
    }

    @Bean
    Map<String, String> pitchServiceLLMConfiguration(OpenAiChatModel model) {
        return Map.of("model", "OpenAi",
                "provider", "OpenAI",
                "temperature", model.getDefaultOptions().getTemperature().toString()
        );
    }
}
