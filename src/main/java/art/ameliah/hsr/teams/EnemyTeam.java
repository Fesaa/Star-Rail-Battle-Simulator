package art.ameliah.hsr.teams;

import art.ameliah.hsr.enemies.AbstractEnemy;
import art.ameliah.hsr.enemies.AllWeakEnemy;
import art.ameliah.hsr.enemies.FireWindImgLightningWeakEnemy;
import art.ameliah.hsr.enemies.PhysFireWeakEnemy;
import art.ameliah.hsr.enemies.PhysWeakEnemy;
import art.ameliah.hsr.enemies.WindWeakEnemy;

import java.util.ArrayList;

public class EnemyTeam {
    protected String description;

    public ArrayList<AbstractEnemy> getTeam() {
        return null;
    }

    public String toString() {
        return description;
    }

    public static class AllWeakEnemies2 extends EnemyTeam {
        public AllWeakEnemies2() {
            description = "2 Enemies weak to everything with 150 Speed (Each enemy performs 1.33 attacks per turn)";
        }

        @Override
        public ArrayList<AbstractEnemy> getTeam() {
            ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
            enemyTeam.add(new AllWeakEnemy(0, 2));
            enemyTeam.add(new AllWeakEnemy(1, 2));
            return enemyTeam;
        }
    }

    public static class FireWindImgLightningWeakTarget1 extends EnemyTeam {
        public FireWindImgLightningWeakTarget1() {
            description = "1 Fire and Wind and Imaginary and Lightning Weak Enemy with 150 Speed (Performs 2 attacks per turn)";
        }

        @Override
        public ArrayList<AbstractEnemy> getTeam() {
            ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
            enemyTeam.add(new FireWindImgLightningWeakEnemy(0, 0));
            return enemyTeam;
        }
    }

    public static class WindWeakTarget1 extends EnemyTeam {
        public WindWeakTarget1() {
            description = "1 Wind Weak Enemy with 150 Speed (Performs 2 attacks per turn)";
        }

        @Override
        public ArrayList<AbstractEnemy> getTeam() {
            ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
            enemyTeam.add(new WindWeakEnemy(0, 0));
            return enemyTeam;
        }
    }

    public static class PhysFireWeakTargets3 extends EnemyTeam {
        public PhysFireWeakTargets3() {
            description = "3 Physical and Fire Weak Enemies with 150 Speed (Each enemy performs 1.33 attacks per turn)";
        }

        @Override
        public ArrayList<AbstractEnemy> getTeam() {
            ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
            enemyTeam.add(new PhysFireWeakEnemy(0, 2));
            enemyTeam.add(new PhysFireWeakEnemy(1, 2));
            enemyTeam.add(new PhysFireWeakEnemy(2, 2));
            return enemyTeam;
        }
    }

    public static class PhysFireWeakTargets2 extends EnemyTeam {
        public PhysFireWeakTargets2() {
            description = "2 Physical and Fire Weak Enemies with 150 Speed (Each enemy performs 1.33 attacks per turn)";
        }

        @Override
        public ArrayList<AbstractEnemy> getTeam() {
            ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
            enemyTeam.add(new PhysFireWeakEnemy(0, 2));
            enemyTeam.add(new PhysFireWeakEnemy(1, 2));
            return enemyTeam;
        }
    }

    public static class PhysFireWeakTargets1 extends EnemyTeam {
        public PhysFireWeakTargets1() {
            description = "1 Physical and Fire Weak Enemy with 150 Speed (Performs 2 attacks per turn)";
        }

        @Override
        public ArrayList<AbstractEnemy> getTeam() {
            ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
            enemyTeam.add(new PhysFireWeakEnemy(0, 0));
            return enemyTeam;
        }
    }

    public static class PhysWeakTargets3 extends EnemyTeam {
        public PhysWeakTargets3() {
            description = "3 Physical Weak Enemies with 150 Speed (Each enemy performs 1.33 attacks per turn)";
        }

        @Override
        public ArrayList<AbstractEnemy> getTeam() {
            ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
            enemyTeam.add(new PhysWeakEnemy(0, 2));
            enemyTeam.add(new PhysWeakEnemy(1, 2));
            enemyTeam.add(new PhysWeakEnemy(2, 2));
            return enemyTeam;
        }
    }

    public static class PhysWeakTargets2 extends EnemyTeam {
        public PhysWeakTargets2() {
            description = "2 Physical Weak Enemies with 150 Speed (Each enemy performs 1.33 attacks per turn)";
        }

        @Override
        public ArrayList<AbstractEnemy> getTeam() {
            ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
            enemyTeam.add(new PhysWeakEnemy(0, 2));
            enemyTeam.add(new PhysWeakEnemy(1, 2));
            return enemyTeam;
        }
    }

    public static class PhysWeakTargets1 extends EnemyTeam {
        public PhysWeakTargets1() {
            description = "1 Physical Weak Enemy with 150 Speed (Performs 2 attacks per turn)";
        }

        @Override
        public ArrayList<AbstractEnemy> getTeam() {
            ArrayList<AbstractEnemy> enemyTeam = new ArrayList<>();
            enemyTeam.add(new PhysWeakEnemy(0, 0));
            return enemyTeam;
        }
    }
}
