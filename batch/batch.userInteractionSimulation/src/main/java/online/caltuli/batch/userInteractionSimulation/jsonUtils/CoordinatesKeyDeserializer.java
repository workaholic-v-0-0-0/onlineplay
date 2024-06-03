package online.caltuli.batch.userInteractionSimulation.jsonUtils;

import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import online.caltuli.model.Coordinates;

import java.io.IOException;

public class CoordinatesKeyDeserializer extends KeyDeserializer {

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (key == null || key.isEmpty()) {
            return null;
        }
        String[] parts = key.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("The key must be in the format 'x,y'");
        }
        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        return new Coordinates(x, y);
    }
}
