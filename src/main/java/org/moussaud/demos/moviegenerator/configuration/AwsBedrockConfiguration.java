package org.moussaud.demos.moviegenerator.configuration;

import org.springframework.ai.bedrock.anthropic3.Anthropic3ChatOptions;
import org.springframework.ai.bedrock.anthropic3.BedrockAnthropic3ChatModel;
import org.springframework.ai.bedrock.anthropic3.api.Anthropic3ChatBedrockApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

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

    @Bean
    Map<String, String> pitchServiceLLMConfiguration(Anthropic3ChatBedrockApi chatApi, BedrockAnthropic3ChatModel model) {
        Anthropic3ChatOptions options = (Anthropic3ChatOptions) model.getDefaultOptions();
        return Map.of("model", "AWS",
                "provider", "Anthropic3",
                "modelId", chatApi.getModelId(),
                "region", chatApi.getRegion().toString(),
                "version", options.getAnthropicVersion());
    }

}
