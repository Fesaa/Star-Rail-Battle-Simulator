package art.ameliah.hsr.builder;

import art.ameliah.hsr.characters.AbstractCharacter;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {

    public static final Gson gson = new Gson();

    public static List<BattleConfig> loadTeams() {
        Config config;
        try (FileReader reader = new FileReader("configs/teams.json")) {
            config = gson.fromJson(reader, Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (var battle : config.getBattles()) {
            List<AbstractCharacter<?>> characters = new ArrayList<>();

            for (var id : battle.getTeam()) {
                var character = config.getCharacters().get(id);
                if (character == null) {
                    throw new RuntimeException("Character '" + id + "' not found. Did you configure it?");
                }

                characters.add(character.toCharacter());
            }

            battle.setCharacters(characters);
        }

        return config.getBattles();
    }


}
