package iskallia.vault.util;

import com.google.common.collect.Lists;
import net.minecraftforge.common.UsernameCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NameProviderPublic {
    private static final Random rand;
    private static final List<String> DEV_NAMES;
    private static final List<String> SMP_S2;

    public static String getRandomName() {
        return getRandomName(NameProviderPublic.rand);
    }

    public static String getRandomName(final Random random) {
        return MiscUtils.getRandomEntry(getAllAvailableNames(), random);
    }

    public static List<String> getAllAvailableNames() {
        final List<String> names = new ArrayList<String>();
        names.addAll(NameProviderPublic.DEV_NAMES);
        names.addAll(NameProviderPublic.SMP_S2);
        names.addAll(getKnownUsernames());
        return names;
    }

    public static List<String> getVHSMPAssociates() {
        final List<String> names = new ArrayList<String>();
        names.addAll(NameProviderPublic.DEV_NAMES);
        names.addAll(NameProviderPublic.SMP_S2);
        return names;
    }

    private static List<String> getKnownUsernames() {
        return new ArrayList<String>(UsernameCache.getMap().values());
    }

    static {
        rand = new Random();
        DEV_NAMES = Lists.newArrayList(new String[]{"KaptainWutax", "iGoodie", "jmilthedude", "Scalda", "Kumara22", "Goktwo", "Aolsen96", "Winter_Grave", "kimandjax", "Monni_21", "Starmute", "MukiTanuki", "RowanArtifex", "HellFirePvP", "Pau1_", "Douwsky", "pomodoko", "Damnsecci", "Derican"});
        SMP_S2 = Lists.newArrayList(new String[]{"CaptainSparklez", "Stressmonster101", "CaptainPuffy", "AntonioAsh", "ItsFundy", "iskall85", "Tubbo_", "HBomb94", "5uppps", "X33N", "PeteZahHutt", "Seapeekay"});
    }
}
