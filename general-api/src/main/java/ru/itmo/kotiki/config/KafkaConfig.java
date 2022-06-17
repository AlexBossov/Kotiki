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
import ru.itmo.kotiki.dto.OwnerDTO;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
//    @Value(value = "${kafka.bootstrapAddress}")
//    private String bootstrapAddress;
//
//    @Bean
//    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        factory.setBatchListener(false);
//        return factory;
//    }
//
//    @Bean
//    public ConsumerFactory<String, Object> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
//    }
//
//    @Bean
//    public Map<String, Object> consumerConfigs() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        configProps.put(
//                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                bootstrapAddress);
//        configProps.put(
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class);
//        configProps.put(
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//                JsonDeserializer.class);
//        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
//
//        return configProps;
//    }
//
//    @Bean
//    public Map<String, Object> producerConfigs() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        configProps.put(
//                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                bootstrapAddress);
//        configProps.put(
//                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//                StringSerializer.class);
//        configProps.put(
//                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//                JsonSerializer.class);
//        return configProps;
//    }
//
//    @Bean
//    public ProducerFactory<String, Object> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfigs());
//    }
//
//    @Bean
//    public KafkaTemplate<String, Object> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
@Value(value = "${kafka.bootstrapAddress}")
private String bootstrapAddress;

    @Bean
    public ReplyingKafkaTemplate< String, Long, OwnerDTO> replyKafkaTemplate(
            ProducerFactory < String, Long > pf,
            KafkaMessageListenerContainer< String, OwnerDTO > lc) {
        return new ReplyingKafkaTemplate < > (pf, lc);
    }

    @Bean
    public KafkaTemplate< String, OwnerDTO > replyTemplate() {
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
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "");
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

    @Bean
    public ProducerFactory < String, Long > requestProducerFactory() {
        return new DefaultKafkaProducerFactory < > (producerConfigs());
    }

    @Bean
    public ProducerFactory < String, OwnerDTO > replyProducerFactory() {
        return new DefaultKafkaProducerFactory < > (producerConfigs());
    }

    @Bean
    public ConsumerFactory < String, Long > requestConsumerFactory() {
        return new DefaultKafkaConsumerFactory < > (consumerConfigs(), new StringDeserializer(),
                new JsonDeserializer<> ());
    }

    @Bean
    public ConsumerFactory< String, OwnerDTO > replyConsumerFactory() {
        return new DefaultKafkaConsumerFactory < > (
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<> ());
    }

    @Bean
    public KafkaMessageListenerContainer < String, OwnerDTO > replyListenerContainer() {
        ContainerProperties containerProperties = new ContainerProperties("reply_message");
        return new KafkaMessageListenerContainer < > (replyConsumerFactory(), containerProperties);
    }

    @Bean
    public KafkaListenerContainerFactory < ConcurrentMessageListenerContainer < String, Long >> requestListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory < String, Long > factory =
                new ConcurrentKafkaListenerContainerFactory < > ();
        factory.setConsumerFactory(requestConsumerFactory());
        factory.setReplyTemplate(replyTemplate());
        return factory;
    }
}
