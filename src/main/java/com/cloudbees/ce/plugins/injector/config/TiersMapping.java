package com.cloudbees.ce.plugins.injector.config;

import com.cloudbees.ce.plugins.injector.model.Tier;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class TiersMapping {
    @JsonComponent
    public static class TiersDeserializer extends JsonDeserializer<Tier> {
        private static final Logger LOGGER = LoggerFactory.getLogger(TiersDeserializer.class);
        @Override
        public Tier deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            try {
                return Tier.valueOf(p.getText().toLowerCase());
            } catch (IllegalArgumentException ex) {
                LOGGER.error("Couldn't parse `{}` as Tier", p.getText());
                return Tier.none;
            }
        }
    }
}
