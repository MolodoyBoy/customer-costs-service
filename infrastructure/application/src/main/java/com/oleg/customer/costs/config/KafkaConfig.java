package com.oleg.customer.costs.config;

import com.oleg.customer.costs.data.CostCategoryData;
import com.oleg.customer.costs.data.CustomerCostsData;
import com.oleg.customer.costs.listener.deserializer.CostCategoryDeserializer;
import com.oleg.customer.costs.listener.deserializer.CustomerCostsDeserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.BATCH;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<Integer, CostCategoryData> costCategoryKafkaTemplate(
        ProducerFactory<Integer, CostCategoryData> costCategoryProducerFactory
    ) {
        return new KafkaTemplate<>(costCategoryProducerFactory);
    }

    @Bean
    public DefaultKafkaProducerFactory<Integer, CostCategoryData> costCategoryProducerFactory(
        KafkaProperties kafkaProperties,
        @Value("${spring.application.name}") String applicationName
    ) {
        Map<String, Object> configProps = kafkaProperties.buildProducerProperties(null);

        return factory(applicationName, configProps);
    }

    @Bean
    public KafkaTemplate<Integer, CustomerCostsData> customerCostsKafkaTemplate(
        ProducerFactory<Integer, CustomerCostsData> customerCostsProducerFactory
    ) {
        return new KafkaTemplate<>(customerCostsProducerFactory);
    }

    @Bean
    public DefaultKafkaProducerFactory<Integer, CustomerCostsData> customerCostsProducerFactory(
        KafkaProperties kafkaProperties,
        @Value("${spring.application.name}") String applicationName
    ) {
        Map<String, Object> configProps = kafkaProperties.buildProducerProperties(null);

        return factory(applicationName, configProps);
    }

    private <K, V> DefaultKafkaProducerFactory<K, V> factory(String applicationName,
                                                             Map<String, Object> configProps) {
        configProps.put(LINGER_MS_CONFIG, 10);
        configProps.put(REQUEST_TIMEOUT_MS_CONFIG, 8000);
        configProps.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        configProps.put(CLIENT_ID_CONFIG, "%s-customer-costs".formatted(applicationName));

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaListenerErrorHandler kafkaListenerErrorHandler() {
        final Logger logger = LoggerFactory.getLogger(KafkaListenerErrorHandler.class);
        return (message, exception) -> {
            logger.error("Error occurred during processing message: {}", message, exception);
            return "Error occurred: " + exception.getMessage();
        };
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, CustomerCostsData> customerCostsListenerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties(null);
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, CustomerCostsDeserializer.class);
        properties.put(TRUSTED_PACKAGES, "com.oleg.customer.costs.data");

        return containerFactory(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, CostCategoryData> costCategoryListenerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties(null);
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, CostCategoryDeserializer.class);
        properties.put(TRUSTED_PACKAGES, "com.oleg.customer.costs.data");

        return containerFactory(properties);
    }

    private <K, V> ConcurrentKafkaListenerContainerFactory<K, V> containerFactory(Map<String, Object> properties) {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setBatchListener(true);
        factory.getContainerProperties().setAckMode(BATCH);
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(properties));
        factory.getContainerProperties().setListenerTaskExecutor(asyncTaskExecutor());

        return factory;
    }

    private AsyncTaskExecutor asyncTaskExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.setConcurrencyLimit(1);
        executor.setVirtualThreads(true);

        return executor;
    }
}