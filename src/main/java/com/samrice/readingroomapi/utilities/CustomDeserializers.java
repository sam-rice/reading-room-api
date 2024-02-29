package com.samrice.readingroomapi.utilities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.samrice.readingroomapi.dtos.BookDescriptionDto;

import java.io.IOException;

public class CustomDeserializers {

    public Object deserializeBookDescription(JsonParser parser) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        if (node.isTextual()) {
            return node.asText();
        } else if (node.isObject()) {
            return new BookDescriptionDto(node.get("type").asText(), node.get("value").asText());
        }
        throw new RuntimeException("Unexpected JSON type for description");
    }

}
