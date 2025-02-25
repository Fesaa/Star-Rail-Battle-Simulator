package art.ameliah.hsr.characters.remembrance.castorice;

import art.ameliah.hsr.battleLogic.combat.MultiplierStat;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.DeathEvent;
import art.ameliah.hsr.powers.PermPower;

public class Pollux extends Memosprite<Pollux, Castorice> {

    public final static String NAME = "Pollux";

    public Pollux(Castorice master) {
        super(master, NAME, (int) Castorice.MAX_STAMEN_NOVA, 140, 80, ElementType.QUANTUM,
                0, 100, Path.REMEMBRANCE);

        this.usesEnergy = false;
    }

    @Override
    protected void memoSkill() {

    }

    private void RendTheRealmBeneath() {
        this.doAttack(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            dl.logic(getBattle().getEnemies(), (e, al) -> {
                al.hit(this.getMaster(), e, 0.3f, MultiplierStat.HP, TOUGHNESS_DAMAGE_SINGLE_UNIT);
            });
        });
    }

    private void DimscorchBreath() {
        // TODO
    }

    @Subscribe
    public void onDeath(DeathEvent event) {
        // Where The West Wind Dwells
        getBattle().getPlayers().forEach(p -> {
            p.increaseHealth(this, 250 + 0.1 * this.getMaster().getFinalHP());
        });
        // Ebon Wings Over Scorched Ruins
        this.doAttack(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            for (int i = 0; i < 6; i++) {
                dl.logic(getBattle().getRandomEnemy(),
                        (e, al) -> al.hit(this.getMaster(), e,
                                0.5f, MultiplierStat.HP, 4));
            }
        });
    }

    public static class MoonShelledVessel extends PermPower {
        // TODO
    }
}
