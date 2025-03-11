package art.ameliah.hsr.enemies.game.amphoreus;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.EnemyType;
import art.ameliah.hsr.events.Subscribe;
import art.ameliah.hsr.events.combat.CombatStartEvent;
import art.ameliah.hsr.events.combat.DeathEvent;
import art.ameliah.hsr.powers.AbstractPower;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;

import java.util.HashSet;
import java.util.Set;

// DMG is guessed; https://homdgcat.wiki/sr/monster?lang=EN&id=402301001&lv=95&hlg=3&eg=271&def=1000#_402301001
// does not include it
public class NoontideGryphon extends AbstractEnemy {

    private final Set<AbstractCharacter<?>> lockedOn = new HashSet<>();
    private final Set<AquilasMark> marks = new HashSet<>();

    public NoontideGryphon(int baseHP) {
        super("Noontide Gryphon", EnemyType.Minion, baseHP, 738, 1150, 158.40f, 160, 95);

        this.addWeakness(ElementType.PHYSICAL);
        this.addWeakness(ElementType.QUANTUM);
        this.addWeakness(ElementType.IMAGINARY);

        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 36.0f, "Noontide Gryphon EHR Boost"));
        this.addPower(PermPower.create(PowerStat.EFFECT_HIT, 30.0f, "Noontide Gryphon ER Boost"));

        this.sequence.addAction(this::ThroughTheDarkness, this::HeraldingTheDawn);
        this.sequence.addAction(this::LightTheFuture);
        this.sequence.addAction(this::PraiseAquila);
    }

    @Override
    protected void act() {
        this.sequence.runNext();
    }

    @Subscribe
    public void onCombatStartAquilasMarker(CombatStartEvent event) {
        getBattle().registerForEnemy(e -> e.addPower(new AquilasMarkListener()));
    }

    private void ThroughTheDarkness() {
        this.doAttack(dl -> {
            dl.logic(getRandomTarget(), (t, al) -> al.hit(t, 5, 900));
        });
    }

    private void HeraldingTheDawn() {
        this.doAttack(dl -> {
            int size = getBattle().playerSize();
            for (int i = 0; i < 3; i++) {
                getBattle().characterCallback(size - i, c -> {
                    dl.logic(c, al -> al.hit(c, 10, 700));
                    c.addPower(this.newMark());
                });
            }
        });
    }

    private void LightTheFuture() {
        this.lockedOn.add(this.getRandomTarget());
    }

    private void PraiseAquila() {
        this.startAttack().handle(dl -> {
            for (var target : this.lockedOn) {
                int idx = getBattle().getPlayers().indexOf(target);
                dl.logic(idx - 1, (e, al) -> {
                    al.hit(e, 20, 1300);
                    e.addPower(this.newMark());
                });
            }
        }).afterAttackHook(this.lockedOn::clear).execute();
    }

    private AquilasMark newMark() {
        var mark = new AquilasMark();
        this.marks.add(mark);
        return mark;
    }

    public class AquilasMarkListener extends PermPower {

        @Subscribe
        public void onDeath(DeathEvent e) {
            NoontideGryphon.this.marks.forEach(mark -> mark.lastsForever = false);
        }
    }

    public class AquilasMark extends PermPower {

        public AquilasMark() {
            super("Aquilas Mark");

            this.type = PowerType.DEBUFF;
        }


        // TODO: When the target possesses both "Aquila's Mark" and "Oronyx's Mark" simultaneously, dispel all marks
        //  and the target takes True DMG based on their Max HP, loses Energy, and has a high chance to be afflicted
        //  with Entanglement.

        @Override
        public void merge(AbstractPower other) {
            super.merge(other);
            if (this.stacks < 2) {
                return;
            }

            AbstractCharacter<?> character = (AbstractCharacter<?>) this.owner;

            character.removePower(this);
            NoontideGryphon.this.doAttack(dl -> {
                dl.logic(character, (al) -> al.hit(character, 700));
            });
            // No idea about the amount :/
            character.decreaseEnergy(character.maxEnergy * 0.1f, "Aquilas Mark Energy Reduction");

        }
    }
}
