package art.ameliah.hsr.battleLogic.log;

import art.ameliah.hsr.battleLogic.AbstractEntity;
import art.ameliah.hsr.battleLogic.IBattle;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.enemies.AbstractEnemy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.Getter;

import java.io.PrintStream;
import java.lang.reflect.Type;

@Getter
public class JsonLogger extends Logger {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(AbstractCharacter.class, new AbstractCharacterJsonAdapter())
            .registerTypeAdapter(AbstractEnemy.class, new AbstractEnemyJsonAdapter())
            .registerTypeAdapter(AbstractEntity.class, new AbstractEntityJsonAdapter())
            .setPrettyPrinting()
            .create();

    public JsonLogger(IBattle battle, PrintStream out) {
        super(battle, out);
    }

    public JsonLogger(IBattle battle) {
        super(battle);
    }

    @Override
    protected void log(Loggable loggable) {
        try {
            this.out.println(gson.toJson(loggable));
            this.out.println();
        } catch (Exception e) {
            throw new RuntimeException("Failed to log " + loggable, e);
        }
    }

    public static class AbstractEnemyJsonAdapter implements JsonSerializer<AbstractEnemy> {
        @Override
        public JsonElement serialize(AbstractEnemy abstractEntity, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(abstractEntity.getName());
        }
    }

    public static class AbstractCharacterJsonAdapter implements JsonSerializer<AbstractCharacter<?>> {
        @Override
        public JsonElement serialize(AbstractCharacter<?> character, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", character.getName());
            jsonObject.addProperty("lightcone", character.lightcone.toString());
            return jsonObject;
        }
    }

    public static class AbstractEntityJsonAdapter implements JsonSerializer<AbstractEntity> {
        @Override
        public JsonElement serialize(AbstractEntity abstractEntity, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(abstractEntity.getName());
        }
    }

}
