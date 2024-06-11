package com.broadcom.tanzu.demos.rambi.configuration;

import org.springframework.ai.bedrock.anthropic3.BedrockAnthropic3ChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty(prefix = "rambi", name = "chatProvider", havingValue = "aws")
public class AwsBedrockConfiguration {

    @Bean
    @Primary
    ChatClient.Builder myChatClientProvider(BedrockAnthropic3ChatModel model) {
        return ChatClient.builder(model);
    }

    @Bean
    boolean pitchServiceLoadImages() {
        return true;
    }
}
