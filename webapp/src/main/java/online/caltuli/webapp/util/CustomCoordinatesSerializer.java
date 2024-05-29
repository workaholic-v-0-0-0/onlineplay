package online.caltuli.webapp.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import online.caltuli.model.CellState;
import online.caltuli.model.Coordinates;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomCoordinatesSerializer extends StdSerializer<Coordinates> {

    public CustomCoordinatesSerializer() {
        super(Coordinates.class);
    }

    @Override
    public void serialize(Coordinates value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("x", value.getX());
        gen.writeNumberField("y", value.getY());
        gen.writeEndObject();
    }
}