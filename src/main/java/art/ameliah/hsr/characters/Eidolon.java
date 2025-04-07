package art.ameliah.hsr.characters;

public enum Eidolon {
    E0(0),
    E1(1),
    E2(2),
    E3(3),
    E4(4),
    E5(5),
    E6(6);

    private final int value;

    Eidolon(int value) {
        this.value = value;
    }

    public boolean isActivated(Eidolon eidolon) {
        return eidolon.value <= this.value;
    }
}