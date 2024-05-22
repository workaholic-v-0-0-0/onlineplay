package online.caltuli.webapp.util;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class CustomHashMapSerializer extends StdSerializer<HashMap<CoordinateKey, Integer>> {

    public CustomHashMapSerializer() {
        super((Class<HashMap<CoordinateKey, Integer>>)(Class<?>) HashMap.class);
    }

    @Override
    public void serialize(HashMap<CoordinateKey, Integer> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for (Map.Entry<CoordinateKey, Integer> entry : value.entrySet()) {
            gen.writeFieldName(entry.getKey().toString());
            gen.writeNumber(entry.getValue());
        }
        gen.writeEndObject();
    }
}
