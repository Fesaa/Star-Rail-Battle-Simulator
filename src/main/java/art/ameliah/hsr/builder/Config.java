package art.ameliah.hsr.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Config {

    public Map<Integer, CharacterConfig> characters;

    public List<List<Integer>> teams;

}
