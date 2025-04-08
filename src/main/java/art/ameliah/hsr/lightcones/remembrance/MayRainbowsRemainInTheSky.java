package art.ameliah.hsr.lightcones.remembrance;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.battleLogic.combat.hit.AllyHit;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostBasic;
import art.ameliah.hsr.events.character.PostMemoSkill;
import art.ameliah.hsr.events.character.PostMemospriteAttack;
import art.ameliah.hsr.events.character.PostSkill;
import art.ameliah.hsr.events.character.PostUltimate;
import art.ameliah.hsr.events.character.PreMemospriteAttack;
import art.ameliah.hsr.lightcones.AbstractLightcone;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TempPower;

import java.util.List;


public class MayRainbowsRemainInTheSky extends AbstractLightcone {

    private float tally;

    public MayRainbowsRemainInTheSky(AbstractCharacter<?> owner) {
        super(1164, 476, 529, owner);
    }

    @Override
    public void onEquip() {
        this.owner.addPower(PermPower.create(PowerStat.SPEED_PERCENT, 18, "May Rainbows Remain in the Sky"));
    }

    @Subscribe
    public void afterMemoSkill(PostMemoSkill event) {
        getBattle().getEnemies().forEach(e -> {
            e.addPower(TempPower.create(PowerStat.DAMAGE_TAKEN, 18, 2, "May Rainbows Remain in the Sky"));
        });
    }

    @Subscribe
    public void onMemoAttack(PostMemospriteAttack event) {
        event.getAttack().getTargets().forEach(e -> {
            event.getAttack().hit(AllyHit.fixedHit(event.getAttack(), event.getMemo(), e, 2.5f * this.tally, 0, ElementType.WIND,  List.of(DamageType.ADDITIONAL_DAMAGE)));
        });
    }

    @Subscribe
    public void afterBasic(PostBasic e) {
        this.consumeHp();
    }

    @Subscribe
    public void afterSkill(PostSkill e) {
        this.consumeHp();
    }

    @Subscribe
    public void afterUlt(PostUltimate e) {
        this.consumeHp();
    }

    private void consumeHp() {
        getBattle().getPlayers().forEach(p -> {
            var lost = p.reduceHealth(this.owner, 0.01f * p.getFinalHP(), false);
            this.tally += lost;
        });
    }
}
