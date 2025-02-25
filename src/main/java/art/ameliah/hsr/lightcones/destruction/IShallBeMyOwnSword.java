package art.ameliah.hsr.lightcones.destruction;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttacked;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.List;


public class IShallBeMyOwnSword extends AbstractLightcone {

    private int eclipse = 0;

    public IShallBeMyOwnSword(AbstractCharacter<?> owner) {
        super(1164, 582, 397, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.CRIT_DAMAGE, 20, "I Shall Be My Own Sword CD Boost"));
    }

    @Subscribe
    public void onCombatStart(CombatStartEvent e) {
        getBattle().registerForPlayers(c -> c.addPower(new IShallBeMyOwnSwordEffect(this)));
    }

    @Subscribe
    public void beforeAttack(PreAllyAttack e) {
        eclipse = 0;
    }

    public class IShallBeMyOwnSwordEffect extends PermPower {

        private final IShallBeMyOwnSword lightcone;

        public IShallBeMyOwnSwordEffect(IShallBeMyOwnSword lightcone) {
            this.setName(this.getClass().getSimpleName());
            this.lightcone = lightcone;
        }

        @Override
        public float getConditionalDamageBonus(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (character != lightcone.owner) return 0;

            return eclipse * 14;
        }

        @Override
        public float getConditionDefenseIgnore(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes) {
            if (character != lightcone.owner) return 0;

            return eclipse == 3 ? 12 : 0;
        }

        @Subscribe
        public void afterAttacked(PostAllyAttacked e) {
            eclipse = Math.min(3, eclipse + 1);
        }
    }
}
