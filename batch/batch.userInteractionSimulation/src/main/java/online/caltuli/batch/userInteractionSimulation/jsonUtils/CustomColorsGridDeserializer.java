package online.caltuli.batch.userInteractionSimulation.jsonUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import online.caltuli.model.CellState;
import online.caltuli.model.Coordinates;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomColorsGridDeserializer extends JsonDeserializer<Map<Coordinates, CellState>> {

    @Override
    public Map<Coordinates, CellState> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode root = p.getCodec().readTree(p);
        Map<Coordinates, CellState> result = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = root.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String[] coords = entry.getKey().split(",");
            Coordinates key = new Coordinates(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
            CellState value = CellState.valueOf(entry.getValue().asText());
            result.put(key, value);
        }
        return result;
    }
}
