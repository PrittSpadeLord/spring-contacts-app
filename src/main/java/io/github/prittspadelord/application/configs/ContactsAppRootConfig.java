package io.github.prittspadelord.application.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import tools.jackson.core.StreamReadConstraints;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.util.JsonRecyclerPools;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

@ComponentScan(basePackages = {
    "io.github.prittspadelord.application.components",
    "io.github.prittspadelord.application.rest.interceptors",
    "io.github.prittspadelord.application.services",
})
@Configuration
public class ContactsAppRootConfig {

    @Bean
    JsonMapper jsonMapper() {

        StreamReadConstraints constraints = StreamReadConstraints.builder()
            .maxDocumentLength(10_000)
            .maxNameLength(50)
            .maxNestingDepth(5)
            .maxNumberLength(100)
            .maxStringLength(100)
            .maxTokenCount(50)
            .build();

        JsonFactory factory = JsonFactory.builder()
            .recyclerPool(JsonRecyclerPools.nonRecyclingPool())
            .streamReadConstraints(constraints)
            .build();

        return JsonMapper.builder(factory)
            .enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

            .disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)
            .disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .build();
    }
}