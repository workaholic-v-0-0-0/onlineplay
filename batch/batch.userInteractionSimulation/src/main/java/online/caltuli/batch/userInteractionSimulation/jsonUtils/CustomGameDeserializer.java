package online.caltuli.batch.userInteractionSimulation.jsonUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import online.caltuli.model.*;

import java.io.IOException;
import java.util.HashMap;

public class CustomGameDeserializer extends JsonDeserializer<Game> {

    @Override
    public Game deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        int id = node.get("id").asInt();

        // Use ObjectMapper to deserialize complex types correctly
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        HashMap<Coordinates, CellState> colorsGrid = mapper.readValue(node.get("colorsGrid").traverse(), new TypeReference<HashMap<Coordinates, CellState>>() {});
        Player firstPlayer = mapper.readValue(node.get("firstPlayer").traverse(), Player.class);
        Player secondPlayer = mapper.readValue(node.get("secondPlayer").traverse(), Player.class);
        GameState gameState = GameState.valueOf(node.get("gameState").asText());

        Game game = new Game();
        game.setId(id);
        game.setColorsGrid(colorsGrid);
        game.setFirstPlayer(firstPlayer);
        game.setSecondPlayer(secondPlayer);
        game.setGameState(gameState);
        return game;
    }
}
