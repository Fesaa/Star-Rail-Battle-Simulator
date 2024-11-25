package art.ameliah.hsr.enemies.game;

import art.ameliah.hsr.battleLogic.combat.AttackLogic;
import art.ameliah.hsr.battleLogic.combat.EnemyAttack;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.powers.TempPower;
import art.ameliah.hsr.powers.dot.EnemyShock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class GuardianShadow extends AbstractEnemy {

    private final List<Function<GuardianShadow, TempPower>> bans = new ArrayList<>();
    private Random banTypeRng;

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
        this.startAttack().hit(target, 10, 976).execute();
        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Lightning Recollection"));
    }

    private void LightningCondemnation() {
        int idx = this.getRandomTargetPosition();
        AbstractCharacter<?> target = getBattle().getPlayers().get(idx);

        EnemyAttack attack = this.startAttack();

        attack.hit(target, 10, 976);
        getBattle().characterCallback(idx - 1, c -> attack.hit(c, 5, 488));
        getBattle().characterCallback(idx + 1, c -> attack.hit(c, 5, 488));

        attack.execute();

        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.BLAST, "Lightning Condemnation"));
    }

    private void Ban() {
        Function<GuardianShadow, TempPower> powerFunction = this.bans.get(this.banTypeRng.nextInt(this.bans.size()));
        getBattle().getPlayers().forEach(p -> p.addPower(powerFunction.apply(this)));
        getBattle().addToLog(new EnemyAction(this, "bans: " + powerFunction.apply(this).getName()));
    }

    private void InevitablePunishment(AbstractCharacter<?> target) {
        this.startAttack().hit(target, 10, 1139).execute();
        getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE, "Inevitable Punishment"));
    }

    private void ThunderstormCondemnation() {
        EnemyAttack attack = this.startAttack();
        for (int i = 0; i < 8; i++) {
            AbstractCharacter<?> target = this.getRandomTarget();
            attack.hit(target, 10, 325);
            getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.SINGLE)); // TODO: Decide if this is the correct type

            if (this.successfulHit(target, 50)) {
                target.addPower(new EnemyShock(this, 162, 2, 1));
            }
        }

        attack.execute();

    }

    public static class TranquilBan extends TempPower {
        public static final String NAME = "Guardian Shadow Tranquil Ban";

        private final GuardianShadow guardian;

        public TranquilBan(GuardianShadow guardian) {
            super(1, NAME);

            this.type = PowerType.DEBUFF;
            this.guardian = guardian;
        }

        @Override
        public void beforeAttack(AttackLogic attack) {
            if (attack.getTargets().stream().anyMatch(e -> e == this.guardian)) {
                this.guardian.InevitablePunishment(attack.getSource());
            }
        }
    }

    public static class DisarmBan extends TempPower {
        public static final String NAME = "Guardian Shadow Disarm Ban";
        private final GuardianShadow guardian;

        public DisarmBan(GuardianShadow guardian) {
            super(1, NAME);
            this.type = PowerType.DEBUFF;
            this.guardian = guardian;
        }

        @Override
        public void afterUseBasic() {
            this.guardian.InevitablePunishment((AbstractCharacter<?>) this.getOwner());
        }
    }

    public static class SilentBan extends TempPower {
        public static final String NAME = "Guardian Shadow Silent Ban";
        private final GuardianShadow guardian;

        public SilentBan(GuardianShadow guardian) {
            super(1, NAME);
            this.type = PowerType.DEBUFF;
            this.guardian = guardian;
        }

        @Override
        public void afterUseSkill() {
            this.guardian.InevitablePunishment((AbstractCharacter<?>) this.getOwner());
        }
    }
}
