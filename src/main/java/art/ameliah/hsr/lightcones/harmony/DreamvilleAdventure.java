package art.ameliah.hsr.lightcones.harmony;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PreBasic;
import art.ameliah.hsr.events.character.PreSkill;
import art.ameliah.hsr.events.character.PreUltimate;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;

import java.util.List;

public class DreamvilleAdventure extends AbstractLightcone {

    final AbstractPower childishness = new Childishness();
    private DamageType currBenefit;

    public DreamvilleAdventure(AbstractCharacter<?> owner) {
        super(953, 423, 397, owner);
    }

    @Subscribe
    public void onUseSkill(PreSkill e) {
        currBenefit = DamageType.SKILL;
    }

    @Subscribe
    public void onUseBasic(PreBasic e) {
        currBenefit = DamageType.BASIC;
    }

    @Subscribe
    public void onUseUltimate(PreUltimate e) {
        currBenefit = DamageType.ULTIMATE;
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent event) {
        getBattle().registerForPlayers(c -> c.addPower(childishness));
    }

    public class Childishness extends PermPower {
        public Childishness() {
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (currBenefit == null) {
                return 0;
            }
            if (damageTypes.contains(currBenefit)) {
                return 20;
            }
            return 0;
        }
    }
}
