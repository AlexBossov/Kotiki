package ru.itmo.kotiki.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public ReplyingKafkaTemplate< String, Object, Object> replyKafkaTemplate(
            ProducerFactory < String, Object > pf,
            KafkaMessageListenerContainer< String, Object > lc) {
        return new ReplyingKafkaTemplate < > (pf, lc);
    }

    @Bean
    public KafkaTemplate< String, Object > replyTemplate() {
        return new KafkaTemplate < > (replyProducerFactory());
    }

    @Bean
    public Map < String, Object > consumerConfigs() {
        Map < String, Object > props = new HashMap < > ();
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "message-group");
        return props;
    }

    @Bean
    public Map < String, Object > producerConfigs() {
        Map < String, Object > props = new HashMap < > ();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

//    @Bean
//    public ProducerFactory < String, Object > requestProducerFactory() {
//        return new DefaultKafkaProducerFactory < > (producerConfigs());
//    }

    @Bean
    public ProducerFactory < String, Object > replyProducerFactory() {
        return new DefaultKafkaProducerFactory < > (producerConfigs());
    }

    @Bean
    public ConsumerFactory < String, Object > requestConsumerFactory() {
        return new DefaultKafkaConsumerFactory < > (consumerConfigs(), new StringDeserializer(),
                new JsonDeserializer<> ());
    }

    @Bean
    public ConsumerFactory< String, Object > replyConsumerFactory() {
        return new DefaultKafkaConsumerFactory < > (
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<> ());
    }

    @Bean
    public KafkaMessageListenerContainer < String, Object > replyListenerContainer() {
        ContainerProperties containerProperties = new ContainerProperties("reply_message");
        return new KafkaMessageListenerContainer < > (replyConsumerFactory(), containerProperties);
    }

    @Bean
    public KafkaListenerContainerFactory < ConcurrentMessageListenerContainer < String, Object >> requestListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory < String, Object > factory =
                new ConcurrentKafkaListenerContainerFactory < > ();
        factory.setConsumerFactory(requestConsumerFactory());
        factory.setReplyTemplate(replyTemplate());
        return factory;
    }
}