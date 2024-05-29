package online.caltuli.webapp.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import online.caltuli.model.CellState;
import online.caltuli.model.Coordinates;

import java.io.IOException;
import java.util.Map;

public class CustomColorsGridSerializer extends StdSerializer<Map<Coordinates, CellState>> {

    public CustomColorsGridSerializer() {
        super((Class<Map<Coordinates, CellState>>) (Class<?>) Map.class);
    }

    @Override
    public void serialize(Map<Coordinates, CellState> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for (Map.Entry<Coordinates, CellState> entry : value.entrySet()) {
            gen.writeFieldName(entry.getKey().getX() + "," + entry.getKey().getY());
            gen.writeString(entry.getValue().name());
        }
        gen.writeEndObject();
    }
}
