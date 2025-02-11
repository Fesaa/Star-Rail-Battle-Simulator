package art.ameliah.hsr.builder;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.registry.LightconeRegistry;
import art.ameliah.hsr.registry.PlayerRegistry;
import art.ameliah.hsr.registry.RelicSetRegistry;
import art.ameliah.hsr.relics.RelicStats;
import art.ameliah.hsr.relics.Stats;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.List;

@Getter
@Setter
public class CharacterConfig {

    public int id;

    public int lcId;

    public List<RelicConfig> relics;

    public List<Stats> mainStats;

    public List<SubStat> subStats;


    public record RelicConfig(int id, boolean fullSet) {
    }

    public record SubStat(PowerStat stat, float amount) {
    }

    @SneakyThrows
    public AbstractCharacter<?> toCharacter() {
        AbstractCharacter<?> character = PlayerRegistry.INSTANCE.get(id);

        var lc = LightconeRegistry.INSTANCE.getLightCone(this.lcId, character);
        character.EquipLightcone(lc);

        for (var relic : relics) {
            var rs = RelicSetRegistry.INSTANCE.get(relic.id, character, relic.fullSet);
            character.EquipRelicSet(rs);
        }

        var stats = new RelicStats();
        for (var ms : mainStats) {
            stats.addMainStat(ms);
        }
        stats.equipTo(character);

        var statsPower = new PermPower("Stats Power, auto generated");
        for (var ss : subStats) {
            statsPower.setStat(ss.stat, ss.amount);
        }

        character.addPower(statsPower);
        return character;
    }

}
