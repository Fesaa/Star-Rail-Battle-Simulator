package art.ameliah.hsr.enemies.game;

import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.dot.EnemyShock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class GuardianShadow extends AbstractEnemy {

    private Random banTypeRng;
    private final List<Function<GuardianShadow, TempPower>> bans = new ArrayList<>();

    public GuardianShadow() {
        super("Guardian Shadow", EnemyType.Elite, 411239, 718, 1000, 158.40f, 100, 92);
        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.WIND);
        this.addWeakness(ElementType.QUANTUM);

        this.bans.add(TranquilBan::new);
        this.bans.add(DisarmBan::new);
        this.bans.add(SilentBan::new);

        this.sequence.addAction(this::LightningRecollection, this::Ban);
        this.sequence.addAction(this::LightningRecollection, this::LightningCondemnation);
        this.sequence.addAction(this::LightningRecollection, this::ThunderstormCondemnation);
    }

    @Override
    public void onCombatStart() {
        super.onCombatStart();
        this.banTypeRng = new Random(getBattle().getSeed());
    }

    @Override
    protected void act() {
        this.sequence.runNext();
    }

    private void LightningRecollection() {
        AbstractCharacter<?> target = this.getRandomTarget();
        getBattle().getHelper().attackCharacter(this, target, 10, 976);
        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Lightning Recollection"));
    }

    private void LightningCondemnation() {
        int idx = this.getRandomTargetPosition();
        AbstractCharacter<?> target = getBattle().getPlayers().get(idx);

        getBattle().getHelper().attackCharacter(this, target, 10, 651);
        getBattle().characterCallback(idx-1, c -> {
            getBattle().getHelper().attackCharacter(this, c, 5, 488);
        });
        getBattle().characterCallback(idx+1, c -> {
            getBattle().getHelper().attackCharacter(this, c, 5, 488);
        });

        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.BLAST, "Lightning Condemnation"));
    }

    private void Ban() {
        Function<GuardianShadow, TempPower> powerFunction = this.bans.get(this.banTypeRng.nextInt(this.bans.size()));
        getBattle().getPlayers().forEach(p -> p.addPower(powerFunction.apply(this)));
        getBattle().addToLog(new EnemyAction(this, "bans: " + powerFunction.apply(this).name));
    }

    private void InevitablePunishment(AbstractCharacter<?> target) {
        getBattle().getHelper().attackCharacter(this, target, 10, 1139);
        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Inevitable Punishment"));
    }

    private void ThunderstormCondemnation() {
        for (int i = 0; i < 8; i++) {
            AbstractCharacter<?> target = this.getRandomTarget();
            getBattle().getHelper().attackCharacter(this, target, 5, 325);
            getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE));

            if (this.successfulHit(target, 50)) {
                target.addPower(new EnemyShock(this, 162, 2, 1));
            }
        }
    }

    public static class TranquilBan extends TempPower {
        public static String NAME = "Guardian Shadow Tranquil Ban";

        private final GuardianShadow guardian;

        public TranquilBan(GuardianShadow guardian) {
            super(1, NAME);

            this.type = PowerType.DEBUFF;
            this.guardian = guardian;
        }

        @Override
        public void onAttack(AbstractCharacter<?> character, Set<AbstractEnemy> enemiesHit, ArrayList<DamageType> types) {
            if (enemiesHit.stream().anyMatch(e -> e == this.guardian)) {
                this.guardian.InevitablePunishment(character);
            }
        }
    }

    public static class DisarmBan extends TempPower {
        public static String NAME = "Guardian Shadow Disarm Ban";
        private final GuardianShadow guardian;
        public DisarmBan(GuardianShadow guardian) {
            super(1, NAME);
            this.type = PowerType.DEBUFF;
            this.guardian = guardian;
        }

        @Override
        public void afterUseBasic() {
            this.guardian.InevitablePunishment((AbstractCharacter<?>) this.owner);
        }
    }

    public static class SilentBan extends TempPower {
        public static String NAME = "Guardian Shadow Silent Ban";
        private final GuardianShadow guardian;
        public SilentBan(GuardianShadow guardian) {
            super(1, NAME);
            this.type = PowerType.DEBUFF;
            this.guardian = guardian;
        }

        @Override
        public void afterUseSkill() {
            this.guardian.InevitablePunishment((AbstractCharacter<?>) this.owner);
        }
    }
}
