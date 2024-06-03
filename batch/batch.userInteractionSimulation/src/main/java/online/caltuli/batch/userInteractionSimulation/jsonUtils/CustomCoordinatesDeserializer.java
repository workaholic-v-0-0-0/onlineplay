package online.caltuli.batch.userInteractionSimulation.jsonUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import online.caltuli.model.Coordinates;

import java.io.IOException;

public class CustomCoordinatesDeserializer extends JsonDeserializer<Coordinates> {

    @Override
    public Coordinates deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        int x = node.get("x").asInt();
        int y = node.get("y").asInt();
        return new Coordinates(x, y);
    }
}
