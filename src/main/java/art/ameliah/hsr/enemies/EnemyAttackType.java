package art.ameliah.hsr.enemies;

public enum EnemyAttackType {
    AOE(25), BLAST(20), SINGLE(55);

    public final int weight;

    EnemyAttackType(int weight) {
        this.weight = weight;
    }
}
