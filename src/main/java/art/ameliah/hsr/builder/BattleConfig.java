package art.ameliah.hsr.builder;

import art.ameliah.hsr.characters.AbstractCharacter;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BattleConfig {

    public String key;

    public BattleType battleType;

    public List<Integer> team;

    @Expose
    public List<AbstractCharacter<?>> characters = new ArrayList<>();

    public enum BattleType {
        MOC,
        PURE_FICTION,
        ENDLESS,
    }

}
