package iskallia.vault.util;

import com.google.common.collect.ImmutableSet;
import iskallia.vault.Vault;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.RelicPartItem;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class RelicSet {
    public static final Map<ResourceLocation, RelicSet> REGISTRY;
    public static RelicSet DRAGON;
    public static RelicSet MINER;
    public static RelicSet WARRIOR;
    public static RelicSet RICHITY;
    public static RelicSet TWITCH;
    public static RelicSet CUPCAKE;
    public static RelicSet ELEMENT;
    public static RelicSet TWOLF999;
    public static RelicSet SHIELDMANH;
    private final String name;
    private final ResourceLocation id;
    private final Set<RelicPartItem> itemSet;

    private RelicSet(final String name, final String id, final RelicPartItem... items) {
        this.name = name;
        this.id = Vault.id(id);
        this.itemSet = Arrays.stream(items).peek(relicItem -> relicItem.setRelicSet(this)).collect(ImmutableSet.toImmutableSet());
    }

    public String getName() {
        return this.name;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Set<RelicPartItem> getItemSet() {
        return this.itemSet;
    }

    public static List<RelicSet> getAll() {
        return new ArrayList<RelicSet>(RelicSet.REGISTRY.values());
    }

    public static void register() {
        RelicSet.DRAGON = register(new RelicSet("Dragon Set", "dragon", ModItems.DRAGON_BREATH_RELIC, ModItems.DRAGON_CHEST_RELIC, ModItems.DRAGON_FOOT_RELIC, ModItems.DRAGON_HEAD_RELIC, ModItems.DRAGON_TAIL_RELIC));
        RelicSet.MINER = register(new RelicSet("Miner Set", "miner", ModItems.MINERS_DELIGHT_RELIC, ModItems.MINERS_LIGHT_RELIC, ModItems.PICKAXE_HANDLE_RELIC, ModItems.PICKAXE_HEAD_RELIC, ModItems.PICKAXE_TOOL_RELIC));
        RelicSet.WARRIOR = register(new RelicSet("Warrior Set", "warrior", ModItems.WARRIORS_ARMOUR_RELIC, ModItems.WARRIORS_CHARM_RELIC, ModItems.SWORD_BLADE_RELIC, ModItems.SWORD_HANDLE_RELIC, ModItems.SWORD_STICK_RELIC));
        RelicSet.RICHITY = register(new RelicSet("Richity Set", "richity", ModItems.DIAMOND_ESSENCE_RELIC, ModItems.GOLD_ESSENCE_RELIC, ModItems.MYSTIC_GEM_ESSENCE_RELIC, ModItems.NETHERITE_ESSENCE_RELIC, ModItems.PLATINUM_ESSENCE_RELIC));
        RelicSet.TWITCH = register(new RelicSet("Twitch Set", "twitch", ModItems.TWITCH_EMOTE1_RELIC, ModItems.TWITCH_EMOTE2_RELIC, ModItems.TWITCH_EMOTE3_RELIC, ModItems.TWITCH_EMOTE4_RELIC, ModItems.TWITCH_EMOTE5_RELIC));
        RelicSet.CUPCAKE = register(new RelicSet("Cupcake Set", "cupcake", ModItems.CUPCAKE_BLUE_RELIC, ModItems.CUPCAKE_LIME_RELIC, ModItems.CUPCAKE_PINK_RELIC, ModItems.CUPCAKE_PURPLE_RELIC, ModItems.CUPCAKE_RED_RELIC));
        RelicSet.ELEMENT = register(new RelicSet("Element Set", "element", ModItems.AIR_RELIC, ModItems.SPIRIT_RELIC, ModItems.FIRE_RELIC, ModItems.EARTH_RELIC, ModItems.WATER_RELIC));
        RelicSet.TWOLF999 = register(new RelicSet("Twolf999 Set", "twolf999", ModItems.TWOLF999_HEAD_RELIC, ModItems.TWOLF999_COMBAT_VEST_RELIC, ModItems.TWOLF999_COMBAT_LEGGINGS_RELIC, ModItems.TWOLF999_COMBAT_GLOVES_RELIC, ModItems.TWOLF999_COMBAT_BOOTS_RELIC));
        RelicSet.SHIELDMANH = register(new RelicSet("ShieldManH Set", "shieldmanh", ModItems.ARMOR_OF_FORBEARANCE_RELIC, ModItems.HEART_OF_THE_VOID_RELIC, ModItems.NEMESIS_THWARTER_RELIC, ModItems.REVERENCE_EDGE_RELIC, ModItems.WINGS_OF_EQUITY_RELIC));
    }

    public static RelicSet register(final RelicSet relicSet) {
        RelicSet.REGISTRY.put(relicSet.id, relicSet);
        return relicSet;
    }

    static {
        REGISTRY = new HashMap<ResourceLocation, RelicSet>();
    }
}
