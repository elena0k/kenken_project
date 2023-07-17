package com.project.kenken.strategySalvataggio;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.project.kenken.componenti.Coordinate;

import java.io.IOException;
import java.util.List;

class CelleDeserializer extends StdDeserializer<List<Coordinate>> {

    public CelleDeserializer() {
        super(new TypeReference<List<Coordinate>>() {}.getClass());
    }

    @Override
    public List<Coordinate> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        if (p.getCurrentName().equals("listaCelle")) {
            JsonNode node = p.getCodec().readTree(p);
            return new ObjectMapper().readValue(node.asText(), new TypeReference<>() {});
        }
        return null;
    }
}