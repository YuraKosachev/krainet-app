package com.krainet.auth.configurations;

import com.krainet.auth.core.constants.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {
        @Bean
        public NewTopic notificationTopic() {
            return TopicBuilder.name(KafkaConstants.NOTIFICATION_TOPIC).build();
        }
}
