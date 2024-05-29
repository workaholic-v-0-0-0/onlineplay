package online.caltuli.webapp.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import online.caltuli.model.Coordinates;
import online.caltuli.model.json.CoordinatesKey;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class CustomHashMapDeserializer extends StdDeserializer<HashMap<CoordinatesKey, Integer>> {

    public CustomHashMapDeserializer() {
        super((Class<HashMap<CoordinatesKey, Integer>>)(Class<?>) HashMap.class);
    }

    @Override
    public HashMap<CoordinatesKey, Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        HashMap<CoordinatesKey, Integer> map = new HashMap<>();
        JsonNode node = p.getCodec().readTree(p);
        for (JsonNode entry : node) {
            List<Coordinates> coordsList = new ArrayList<>();
            JsonNode keyNode = entry.get("key");
            keyNode.forEach(coord -> coordsList.add(new Coordinates(coord.get("x").asInt(), coord.get("y").asInt())));
            CoordinatesKey key = new CoordinatesKey(coordsList);
            Integer value = entry.get("value").asInt();
            map.put(key, value);
        }
        return map;
    }
}
