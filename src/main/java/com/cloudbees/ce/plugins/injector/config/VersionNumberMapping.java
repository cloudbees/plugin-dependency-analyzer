package com.cloudbees.ce.plugins.injector.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import hudson.util.VersionNumber;
import org.neo4j.ogm.typeconversion.AttributeConverter;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class VersionNumberMapping {
    @JsonComponent
    public static class VersionNumberJsonSerializer extends JsonSerializer<VersionNumber> {
        @Override
        public void serialize(VersionNumber value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value == null ? null : value.toString());
        }
    }

    public static class VersionNumberConverter implements AttributeConverter<VersionNumber, String> {
        @Override
        public String toGraphProperty(VersionNumber value) {
            return value == null ? null : value.toString();
        }

        @Override
        public VersionNumber toEntityAttribute(String value) {
            return value == null ? null : new VersionNumber(value);
        }
    }
}
