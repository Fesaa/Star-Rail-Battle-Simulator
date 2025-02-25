package art.ameliah.hsr.characters.remembrance.aglaea;

import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.goal.shared.target.enemy.HighestEnemyTargetGoal;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostAllyAttack;
import art.ameliah.hsr.events.combat.DeathEvent;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

public class Garmentmaker extends Memosprite<Garmentmaker, Aglaea> {

    public static final String NAME = "Garmentmaker";

    private static boolean hadStacks = false; // Last Thread of Fate

    public Garmentmaker(Aglaea aglaea) {
        super(aglaea, NAME,
                (int) (720 + 0.66 * aglaea.getFinalHP()),
                (int) (0.35 * aglaea.getFinalSpeed() * 1.06),
                90,
                ElementType.LIGHTNING,
                0,
                100,
                Path.REMEMBRANCE);

        this.registerGoal(10, new GarmentmakerTargetGoal(this));
        this.registerGoal(0, new HighestEnemyTargetGoal<>(this));

        this.addPower(new ABodyBrewedByTears());
    }

    @Override
    protected void memoSkill() {
        this.doAttack(DamageType.MEMOSPRITE_DAMAGE, dl -> {
            int idx = this.getTargetIdx(MoveType.MEMOSPRITE_SKILL);

            dl.logic(idx-1, (e, al) -> al.hit(e, 0.66f, 5));
            dl.logic(idx, (e, al) -> al.hit(e, 1.1f, 10));
            dl.logic(idx+1, (e, al) -> al.hit(e, 0.66f, 5));
        });
    }

    @Subscribe
    public void onDeath(DeathEvent event) {
        this.getMaster().increaseEnergy(20, "Bloom of Drying Grass");
    }

    public class ABodyBrewedByTears extends PermPower {

        public static final String NAME = "A Body Brewed by Tears";

        public ABodyBrewedByTears() {
            super(NAME);
            this.maxStacks = 6;
            this.stacks = Garmentmaker.hadStacks ? 1 : 0;

            this.setConditionalStat(PowerStat.FLAT_SPEED, _ -> 55f * this.stacks);
        }

        @Subscribe
        public void afterAttack(PostAllyAttack event) {
            boolean shouldTrigger = event.getAttack().getTargets()
                    .stream().anyMatch(e -> e.hasPower(Aglaea.SeamStitch.NAME));

            if (!shouldTrigger) {
                return;
            }

            Garmentmaker.hadStacks = true;
            getBattle().IncreaseSpeed(this.getOwner(), this);

            if (Garmentmaker.this.getMaster().hasPower(Aglaea.DanceDestinedWeaveress.NAME)) {
                getBattle().IncreaseSpeed(Garmentmaker.this.getMaster(), new Aglaea.DanceDestinedWeaveress());
            }
        }
    }
}
