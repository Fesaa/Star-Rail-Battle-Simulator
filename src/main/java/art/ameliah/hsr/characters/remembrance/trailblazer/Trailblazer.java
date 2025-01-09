package art.ameliah.hsr.characters.remembrance.trailblazer;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.DamageType;
import art.ameliah.hsr.characters.ElementType;
import art.ameliah.hsr.characters.MoveType;
import art.ameliah.hsr.characters.Path;
import art.ameliah.hsr.characters.remembrance.Memomaster;
import art.ameliah.hsr.characters.remembrance.Memosprite;
import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.powers.PermPower;
import art.ameliah.hsr.powers.PowerStat;
import art.ameliah.hsr.powers.TracePower;

import java.util.List;

public class Trailblazer extends Memomaster<Trailblazer> {
    public static final String NAME = "Trailblazer(Remembrance)";

    private Mem mem;
    private boolean firstSummon = true;
    private boolean e2Cooldown = false;

    public Trailblazer() {
        super(NAME, 1048, 543, 631, 103, 80, ElementType.ICE, 160, 100, Path.REMEMBRANCE);

        this.addPower(new TracePower()
                .setStat(PowerStat.CRIT_DAMAGE, 37.3f)
                .setStat(PowerStat.ATK_PERCENT, 14)
                .setStat(PowerStat.HP_PERCENT, 14)
        );
        this.addPower(new E6());
    }

    @Override
    public Memosprite<Mem> getMemo() {
        return this.mem;
    }

    @Override
    public void onCombatStart() {
        getBattle().AdvanceEntity(this, 30);
        getBattle().registerForPlayers(p -> p.addPower(new EidolonsListener()));
    }

    @Override
    public void onTurnStart() {
        this.e2Cooldown = true;
    }

    @Override
    public void useTechnique() {
        getBattle().getEnemies().forEach(e -> {
            getBattle().DelayEntity(e, 50);
        });

        this.doAttack(al -> {
            al.logic(getBattle().getEnemies(), (e, dl) -> {
                dl.hit(e, 100);
            });
        });
    }

    private void summonMem() {
        this.mem = new Mem(this);
        int idx = getBattle().getPlayers().indexOf(this.mem);
        getBattle().addPlayerAt(this.mem, idx+1);

        if (this.firstSummon) {
            this.mem.increaseCharge(40);
            this.firstSummon = false;
        }
    }

    @Override
    protected void useSkill() {
        if (this.mem == null) {
            this.summonMem();
            return;
        }

        this.mem.increaseCharge(10);
        this.mem.getCurrentHp().increase(0.6f*this.mem.getCurrentHp().get());
    }

    @Override
    protected void useBasic() {
        this.doAttack(al -> {
            al.logic(getTarget(MoveType.BASIC), (e, dl) -> {
                dl.hit(e, 1, 10);
            });
        });
    }

    @Override
    protected void useUltimate() {
        if (this.mem == null) {
            this.summonMem();
        }

        this.mem.increaseCharge(40);
        this.doAttack(al -> {
            al.logic(getBattle().getEnemies(), (e, dl) -> {
                dl.hit(this.mem, e, 2.4f, 20);
            });
        });
    }

    public class EidolonsListener extends PermPower {

        public EidolonsListener() {
            super(NAME+"(EidolonsListener)");
        }

        private void E4() {
            if (((AbstractCharacter<?>)this.getOwner()).usesEnergy) {
                return;
            }

            if (Trailblazer.this.mem == null) {
                return;
            }

            Trailblazer.this.mem.increaseCharge(3);
        }

        private void E2() {
            if (Trailblazer.this.e2Cooldown) {
                return;
            }


            if (this.getOwner() instanceof Memosprite<?> memosprite) {
                if (Trailblazer.this.mem != null && Trailblazer.this.mem != memosprite) {
                 Trailblazer.this.increaseEnergy(9, "E4");
                }
            }
        }

        @Override
        public void onTurnStart() {
            this.E2();
        }

        @Override
        public void onEndTurn() {
            this.E4();
        }

        @Override
        public void afterUseUltimate() {
            this.E4();
        }
    }

    private static class E6 extends PermPower {
        public E6() {
            super(NAME + "(E6)");
        }

        @Override
        public float setFixedCritRate(AbstractCharacter<?> character, AbstractEnemy enemy, List<DamageType> damageTypes, float currentCrit) {
            if (damageTypes.contains(DamageType.ULTIMATE)) {
                return 100;
            }
            return currentCrit;
        }
    }
}
