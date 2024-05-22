package online.caltuli.webapp.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import online.caltuli.model.Coordinates;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class CustomHashMapDeserializer extends StdDeserializer<HashMap<CoordinateKey, Integer>> {

    public CustomHashMapDeserializer() {
        super((Class<HashMap<CoordinateKey, Integer>>)(Class<?>) HashMap.class);
    }

    @Override
    public HashMap<CoordinateKey, Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        HashMap<CoordinateKey, Integer> map = new HashMap<>();
        JsonNode node = p.getCodec().readTree(p);
        for (JsonNode entry : node) {
            List<Coordinates> coordsList = new ArrayList<>();
            JsonNode keyNode = entry.get("key");
            keyNode.forEach(coord -> coordsList.add(new Coordinates(coord.get("x").asInt(), coord.get("y").asInt())));
            CoordinateKey key = new CoordinateKey(coordsList);
            Integer value = entry.get("value").asInt();
            map.put(key, value);
        }
        return map;
    }
}
