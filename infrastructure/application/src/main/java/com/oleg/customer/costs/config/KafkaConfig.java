package com.oleg.customer.costs.config;

import com.oleg.customer.costs.data.CostCategoryData;
import com.oleg.customer.costs.data.CustomerCostsData;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

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
}