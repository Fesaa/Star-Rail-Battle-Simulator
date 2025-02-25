package art.ameliah.hsr.enemies.game.jarilovi.fragmentum;

import art.ameliah.hsr.battleLogic.combat.ally.AttackLogic;
import art.ameliah.hsr.battleLogic.log.lines.enemy.EnemyAction;
import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyAttackType;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.character.PostBasic;
import art.ameliah.hsr.events.character.PostSkill;
import art.ameliah.hsr.events.character.PreAllyAttack;
import art.ameliah.hsr.events.combat.CombatStartEvent;
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

    @Subscribe
    public void onCombatStartSetBanType(CombatStartEvent event) {
        this.banTypeRng = new Random(getBattle().getSeed());
    }

    @Override
    protected void act() {
        this.sequence.runNext();
    }

    private void LightningRecollection() {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.doAttack(da -> da.logic(this.getRandomTarget(), (c, al) -> {
            al.hit(c, 10, 976);
            getBattle().addToLog(new EnemyAction(this, c, EnemyAttackType.SINGLE, "Lightning Recollection"));
        }));
    }

    private void LightningCondemnation() {
        this.actionMetric.record(EnemyAttackType.BLAST);
        this.doAttack(da -> {
            int idx = this.getRandomTargetPosition();
            AbstractCharacter<?> target = getBattle().getPlayers().get(idx);

            da.logic(idx-1, (c, al) -> al.hit(c, 5, 488));
            da.logic(idx, (c, al) -> al.hit(c, 10, 976));
            da.logic(idx+1, (c, al) -> al.hit(c, 5, 488));

            getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.BLAST, "Lightning Condemnation"));
        });
    }

    private void Ban() {
        Function<GuardianShadow, TempPower> powerFunction = this.bans.get(this.banTypeRng.nextInt(this.bans.size()));
        getBattle().getPlayers().forEach(p -> p.addPower(powerFunction.apply(this)));
        getBattle().addToLog(new EnemyAction(this, "bans: " + powerFunction.apply(this).getName()));
    }

    private void InevitablePunishment(AbstractCharacter<?> target) {
        this.actionMetric.record(EnemyAttackType.SINGLE);
        this.doAttack(da -> da.logic(target, (c, al) -> {
            al.hit(c, 10, 1139);
            getBattle().addToLog(new EnemyAction(this, c, EnemyAttackType.SINGLE, "Inevitable Punishment"));
        }));
    }

    private void ThunderstormCondemnation() {
        this.actionMetric.record(EnemyAttackType.AOE);
        this.doAttack(da -> {
            for (int i = 0; i < 8; i++) {
                AbstractCharacter<?> target = this.getRandomTarget();
                da.logic(target, (c, al) -> {
                    al.hit(c, 10, 325);
                    if (this.successfulHit(c, 50)) {
                        c.addPower(new EnemyShock(this, 162, 2, 1));
                    }
                });
                getBattle().addToLog(new EnemyAction(this, target, EnemyAttackType.AOE));
            }
        });
    }

    public static class TranquilBan extends TempPower {
        public static final String NAME = "Guardian Shadow Tranquil Ban";

        private final GuardianShadow guardian;

        public TranquilBan(GuardianShadow guardian) {
            super(1, NAME);

            this.type = PowerType.DEBUFF;
            this.guardian = guardian;
        }

        @Subscribe
        public void beforeAttack(PreAllyAttack event) {
            if (event.getAttack().getTargets().stream().anyMatch(e -> e == this.guardian)) {
                this.guardian.InevitablePunishment(event.getAttack().getSource());
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

        @Subscribe
        public void afterUseBasic(PostBasic e) {
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

        @Subscribe
        public void afterUseSkill(PostSkill e) {
            this.guardian.InevitablePunishment((AbstractCharacter<?>) this.getOwner());
        }
    }
}
