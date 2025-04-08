package art.ameliah.hsr.registry;

import art.ameliah.hsr.characters.AbstractCharacter;
import art.ameliah.hsr.characters.abundance.gallagher.Gallagher;
import art.ameliah.hsr.characters.abundance.huohuo.Huohuo;
import art.ameliah.hsr.characters.abundance.lingsha.Lingsha;
import art.ameliah.hsr.characters.destruction.yunli.Yunli;
import art.ameliah.hsr.characters.erudition.herta.Herta;
import art.ameliah.hsr.characters.erudition.jade.Jade;
import art.ameliah.hsr.characters.erudition.theherta.TheHerta;
import art.ameliah.hsr.characters.harmony.asta.Asta;
import art.ameliah.hsr.characters.harmony.bronya.Bronya;
import art.ameliah.hsr.characters.harmony.hanya.Hanya;
import art.ameliah.hsr.characters.harmony.robin.Robin;
import art.ameliah.hsr.characters.harmony.ruanmei.RuanMei;
import art.ameliah.hsr.characters.harmony.sparkle.Sparkle;
import art.ameliah.hsr.characters.harmony.sunday.Sunday;
import art.ameliah.hsr.characters.harmony.tingyun.Tingyun;
import art.ameliah.hsr.characters.harmony.tribbie.Tribbie;
import art.ameliah.hsr.characters.hunt.drRatio.DrRatio;
import art.ameliah.hsr.characters.hunt.feixiao.Feixiao;
import art.ameliah.hsr.characters.hunt.march.SwordMarch;
import art.ameliah.hsr.characters.hunt.moze.Moze;
import art.ameliah.hsr.characters.hunt.topaz.Topaz;
import art.ameliah.hsr.characters.nihility.cipher.Cipher;
import art.ameliah.hsr.characters.nihility.pela.Pela;
import art.ameliah.hsr.characters.preservation.aventurine.Aventurine;
import art.ameliah.hsr.characters.preservation.fuxuan.FuXuan;
import art.ameliah.hsr.characters.remembrance.aglaea.Aglaea;
import art.ameliah.hsr.characters.remembrance.castorice.Castorice;
import art.ameliah.hsr.characters.remembrance.hyacine.Hyacine;
import art.ameliah.hsr.characters.remembrance.trailblazer.Trailblazer;

public class PlayerRegistry extends AbstractRegistry<AbstractCharacter<?>> {

    public static final PlayerRegistry INSTANCE = new PlayerRegistry();

    public PlayerRegistry() {
        register(1009, Asta.class);
        register(1013, Herta.class);
        register(1101, Bronya.class);
        register(1106, Pela.class);
        register(1112, Topaz.class);
        register(1202, Tingyun.class);
        register(1208, FuXuan.class);
        register(1215, Hanya.class);
        register(1217, Huohuo.class);
        register(1220, Feixiao.class);
        register(1221, Yunli.class);
        register(1222, Lingsha.class);
        register(1223, Moze.class);
        register(1224, SwordMarch.class);
        register(1301, Gallagher.class);
        register(1303, RuanMei.class);
        register(1304, Aventurine.class);
        register(1305, DrRatio.class);
        register(1306, Sparkle.class);
        register(1309, Robin.class);
        register(1313, Sunday.class);
        register(1314, Jade.class);
        register(1401, TheHerta.class);
        register(1402, Aglaea.class);
        register(1403, Tribbie.class);
        register(1407, Castorice.class);
        register(8007, Trailblazer.class);
        register(8008, Trailblazer.class);
        register(1406, Cipher.class);
        register(1409, Hyacine.class);
    }

}
