package online.caltuli.webapp.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import online.caltuli.model.Coordinates;
import online.caltuli.model.Game;

import java.io.IOException;

public class CustomGameSerializer extends JsonSerializer<Game> {

    @Override
    public void serialize(Game value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());

        // Serialize the colorsGrid
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Coordinates.class, new CustomCoordinatesSerializer());
        module.addSerializer(new CustomColorsGridSerializer());
        mapper.registerModule(module);
        String colorsGridJson = mapper.writeValueAsString(value.getColorsGrid());
        gen.writeFieldName("colorsGrid");
        gen.writeRawValue(colorsGridJson);  // Inject the raw JSON string

        // Serialize the players using default serialization
        gen.writeObjectField("firstPlayer", value.getFirstPlayer());
        gen.writeObjectField("secondPlayer", value.getSecondPlayer());

        gen.writeStringField("gameState", value.getGameState().name());
        gen.writeEndObject();
    }
}

