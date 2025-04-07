package art.ameliah.hsr.builder;

public class TeamCharacter {

    public TeamCharacterType type;

    public int referenceId;

    public boolean isDps;

    public CharacterConfig character;

    public Integer lcOverwriteId;

    public enum TeamCharacterType {
        REFERENCE,
        NEW
    }

}
