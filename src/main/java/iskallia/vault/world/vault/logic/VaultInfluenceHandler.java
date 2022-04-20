package iskallia.vault.world.vault.logic;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.*;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effects;
import net.minecraft.util.Tuple;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.server.ServerWorld;

import java.text.DecimalFormat;
import java.util.*;

public class VaultInfluenceHandler {
    private static final float influenceChance = 0.66f;
    private static final UUID BENEVOLENT_HP_REDUCTION;
    private static final UUID OMNISCIENT_SPEED_REDUCTION;
    private static final UUID MALEVOLENCE_DAMAGE_REDUCTION;
    private static final UUID BENEVOLENT_HP_INCREASE;
    private static final UUID OMNISCIENT_ARMOR_INCREASE;
    private static final UUID MALEVOLENCE_DAMAGE_INCREASE;
    private static final Random rand;
    private static final Map<PlayerFavourData.VaultGodType, InfluenceMessages> messages;

    public static void initializeInfluences(final VaultRaid vault, final ServerWorld world) {
        final int vaultLvl = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        if (vaultLvl < 50) {
            return;
        }
        final CrystalData data = vault.getProperties().getBase(VaultRaid.CRYSTAL_DATA).orElse(null);
        if (data == null || !data.canTriggerInfluences() || !data.getType().canTriggerInfluences() || vault.getPlayers().size() > 1) {
            return;
        }
        if (vault.getAllObjectives().stream().anyMatch(VaultObjective::preventsInfluences)) {
            return;
        }
        final VaultInfluences influences = vault.getInfluences();
        final PlayerFavourData favourData = PlayerFavourData.get(world);
        final Map<PlayerFavourData.VaultGodType, Integer> positives = new HashMap<PlayerFavourData.VaultGodType, Integer>();
        final Map<PlayerFavourData.VaultGodType, Integer> negatives = new HashMap<PlayerFavourData.VaultGodType, Integer>();
        final PlayerFavourData.VaultGodType[] values = PlayerFavourData.VaultGodType.values();
        PlayerFavourData.VaultGodType type = null;
        VaultPlayer vPlayer = null;
        int favour = 0;
        for (int length = values.length, i = 0; i < length; ++i) {
            type = values[i];
            final Iterator<VaultPlayer> iterator = vault.getPlayers().iterator();
            while (iterator.hasNext()) {
                vPlayer = iterator.next();
                favour = favourData.getFavour(vPlayer.getPlayerId(), type);
                if (Math.abs(favour) < 4) {
                    continue;
                }
                if (VaultInfluenceHandler.rand.nextFloat() >= 0.66f) {
                    continue;
                }
                if (favour < 0) {
                    negatives.put(type, favour);
                    break;
                }
                positives.put(type, favour);
                break;
            }
        }
        positives.forEach((type1, favour1) -> {
            final Tuple<VaultInfluence, String> influenceResult = getPositiveInfluence(type1, Math.abs(favour1));
            influences.addInfluence((VaultInfluence) influenceResult.getA(), vault, world);
            final String message = VaultInfluenceHandler.messages.get(type1).getPositiveMessage();
            final IFormattableTextComponent vgName = new StringTextComponent(type1.getName()).withStyle(type1.getChatColor());
            vgName.withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, type1.getHoverChatComponent())));
            final IFormattableTextComponent txt = (IFormattableTextComponent) new StringTextComponent("");
            txt.append((ITextComponent) new StringTextComponent("[VG] ").withStyle(TextFormatting.DARK_PURPLE)).append((ITextComponent) vgName).append((ITextComponent) new StringTextComponent(": ").withStyle(TextFormatting.WHITE)).append((ITextComponent) new StringTextComponent(message));
            final IFormattableTextComponent info = new StringTextComponent((String) influenceResult.getB()).withStyle(TextFormatting.DARK_GRAY);
            vault.getPlayers().forEach(vPlayer1 -> vPlayer1.runIfPresent(world.getServer(), sPlayer -> {
                sPlayer.sendMessage((ITextComponent) txt, Util.NIL_UUID);
                sPlayer.sendMessage((ITextComponent) info, Util.NIL_UUID);
            }));
            return;
        });
        negatives.forEach((type2, favour2) -> {
            final Tuple<VaultInfluence, String> influenceResult2 = getNegativeInfluence(type2, Math.abs(favour2));
            influences.addInfluence((VaultInfluence) influenceResult2.getA(), vault, world);
            final String message2 = VaultInfluenceHandler.messages.get(type2).getNegativeMessage();
            final IFormattableTextComponent vgName2 = new StringTextComponent(type2.getName()).withStyle(type2.getChatColor());
            vgName2.withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, type2.getHoverChatComponent())));
            final IFormattableTextComponent txt2 = (IFormattableTextComponent) new StringTextComponent("");
            txt2.append((ITextComponent) new StringTextComponent("[VG] ").withStyle(TextFormatting.DARK_PURPLE)).append((ITextComponent) vgName2).append((ITextComponent) new StringTextComponent(": ").withStyle(TextFormatting.WHITE)).append((ITextComponent) new StringTextComponent(message2));
            final IFormattableTextComponent info2 = new StringTextComponent((String) influenceResult2.getB()).withStyle(TextFormatting.DARK_GRAY);
            vault.getPlayers().forEach(vPlayer2 -> vPlayer2.runIfPresent(world.getServer(), sPlayer -> {
                sPlayer.sendMessage((ITextComponent) txt2, Util.NIL_UUID);
                sPlayer.sendMessage((ITextComponent) info2, Util.NIL_UUID);
            }));
        });
    }

    private static Tuple<VaultInfluence, String> getPositiveInfluence(final PlayerFavourData.VaultGodType type, final int favour) {
        final DecimalFormat percentFormat = new DecimalFormat("0.##");
        switch (type) {
            case BENEVOLENT: {
                VaultInfluence influence = null;
                String text = null;
                switch (VaultInfluenceHandler.rand.nextInt(2)) {
                    case 1: {
                        final int ampl = MathHelper.clamp((favour - 4) / 8 + 1, 1, 2);
                        influence = new EffectInfluence(Effects.REGENERATION, ampl);
                        text = "Grants +" + ampl + " Regeneration";
                        break;
                    }
                    default: {
                        final int heal = 50 + MathHelper.ceil((favour - 4) * 4.166666f);
                        final float healPerc = heal / 100.0f;
                        influence = new VaultAttributeInfluence(VaultAttributeInfluence.Type.HEALING_EFFECTIVENESS, 1.0f + healPerc, true);
                        text = "Effectiveness of Healing is increased by " + heal + "%";
                        break;
                    }
                }
                return (Tuple<VaultInfluence, String>) new Tuple(influence, text);
            }
            case OMNISCIENT: {
                VaultInfluence influence = null;
                String text = null;
                switch (VaultInfluenceHandler.rand.nextInt(2)) {
                    case 1: {
                        final int ampl = MathHelper.clamp(favour / 5, 1, 3);
                        influence = new EffectInfluence(Effects.LUCK, ampl);
                        text = "Grants +" + ampl + " Luck";
                        break;
                    }
                    default: {
                        final int increased = 25 + Math.min(Math.round((favour - 4) * 6.25f), 75);
                        influence = new VaultAttributeInfluence(VaultAttributeInfluence.Type.CHEST_RARITY, increased / 100.0f, false);
                        text = "Grants " + increased + "% Chest Rarity";
                        break;
                    }
                }
                return (Tuple<VaultInfluence, String>) new Tuple(influence, text);
            }
            case TIMEKEEPER: {
                VaultInfluence influence = null;
                String text = null;
                switch (VaultInfluenceHandler.rand.nextInt(2)) {
                    case 1: {
                        final int time = favour / 4;
                        influence = new TimeInfluence(time * 60 * 20);
                        text = "Grants " + time + " additional minutes";
                        break;
                    }
                    default: {
                        final int cdReduction = 10 + Math.round((favour - 4) * 2.5f);
                        influence = new VaultAttributeInfluence(VaultAttributeInfluence.Type.COOLDOWN_REDUCTION, 1.0f + cdReduction / 100.0f, true);
                        text = "Grants +" + cdReduction + "% Cooldown Reduction";
                        break;
                    }
                }
                return (Tuple<VaultInfluence, String>) new Tuple(influence, text);
            }
            case MALEVOLENCE: {
                VaultInfluence influence = null;
                String text = null;
                switch (VaultInfluenceHandler.rand.nextInt(2)) {
                    case 1: {
                        final int more = 25 + Math.round((favour - 4) * 14.58f);
                        final float perc = 1.0f + more / 100.0f;
                        influence = new DamageInfluence(perc);
                        text = "You deal " + more + "% more damage";
                        break;
                    }
                    default: {
                        final int incDrops = 100 + (favour - 4) * 25;
                        influence = new VaultAttributeInfluence(VaultAttributeInfluence.Type.SOUL_SHARD_DROPS, 1.0f + incDrops / 100.0f, true);
                        text = "Monsters drop " + incDrops + "% more Soul Shards.";
                        break;
                    }
                }
                return (Tuple<VaultInfluence, String>) new Tuple(influence, text);
            }
            default: {
                throw new IllegalArgumentException("Unknown type: " + type.name());
            }
        }
    }

    private static Tuple<VaultInfluence, String> getNegativeInfluence(final PlayerFavourData.VaultGodType type, final int favour) {
        switch (type) {
            case BENEVOLENT: {
                VaultInfluence influence = null;
                String text = null;
                switch (VaultInfluenceHandler.rand.nextInt(2)) {
                    case 1: {
                        final int more = 4 + Math.round((favour - 4) * 0.5f);
                        influence = new MobsInfluence(more);
                        text = more + " additional monsters spawn around you";
                        break;
                    }
                    default: {
                        final int reduced = 10 + Math.round((favour - 4) * 3.3333f);
                        influence = new VaultAttributeInfluence(VaultAttributeInfluence.Type.HEALING_EFFECTIVENESS, 1.0f - reduced / 100.0f, true);
                        text = "Effectiveness of Healing is reduced by " + reduced + "%";
                        break;
                    }
                }
                return (Tuple<VaultInfluence, String>) new Tuple(influence, text);
            }
            case OMNISCIENT: {
                VaultInfluence influence = null;
                String text = null;
                switch (VaultInfluenceHandler.rand.nextInt(2)) {
                    case 1: {
                        final int decreased = 25 + Math.min(Math.round((favour - 4) * 6.25f), 75);
                        influence = new VaultAttributeInfluence(VaultAttributeInfluence.Type.CHEST_RARITY, -decreased / 100.0f, false);
                        text = "Reduces Chest Rarity by " + decreased + "%";
                        break;
                    }
                    default: {
                        final int ampl = MathHelper.clamp(favour / 5, 1, 3);
                        influence = new EffectInfluence(Effects.UNLUCK, ampl);
                        text = "Applies -" + ampl + " Luck";
                        break;
                    }
                }
                return (Tuple<VaultInfluence, String>) new Tuple(influence, text);
            }
            case TIMEKEEPER: {
                VaultInfluence influence = null;
                String text = null;
                switch (VaultInfluenceHandler.rand.nextInt(2)) {
                    case 1: {
                        final int time = 1 + (favour - 4) / 6;
                        influence = new TimeInfluence(-time * 60 * 20);
                        text = "Removes " + time + " minutes";
                        break;
                    }
                    default: {
                        final int more2 = 10 + Math.round((favour - 4) * 3.333f);
                        final float perc = more2 / 100.0f;
                        influence = new MobAttributeInfluence(Attributes.MOVEMENT_SPEED, new AttributeModifier(VaultInfluenceHandler.OMNISCIENT_SPEED_REDUCTION, "Favours", (double) perc, AttributeModifier.Operation.MULTIPLY_TOTAL));
                        text = "Monsters move " + more2 + "% faster";
                        break;
                    }
                }
                return (Tuple<VaultInfluence, String>) new Tuple(influence, text);
            }
            case MALEVOLENCE: {
                VaultInfluence influence = null;
                String text = null;
                switch (VaultInfluenceHandler.rand.nextInt(2)) {
                    case 1: {
                        final int more = 20 + (favour - 4) * 15;
                        influence = new MobAttributeInfluence(Attributes.MAX_HEALTH, new AttributeModifier(VaultInfluenceHandler.BENEVOLENT_HP_REDUCTION, "Favours", (double) (more / 100.0f), AttributeModifier.Operation.MULTIPLY_TOTAL));
                        text = "Monsters have " + more + "% more Health";
                        break;
                    }
                    default: {
                        final int less = 10 + Math.round((favour - 4) * 5.416666f);
                        influence = new DamageInfluence(1.0f - less / 100.0f);
                        text = "You deal " + less + "% less damage";
                        break;
                    }
                }
                return (Tuple<VaultInfluence, String>) new Tuple(influence, text);
            }
            default: {
                throw new IllegalArgumentException("Unknown type: " + type.name());
            }
        }
    }

    static {
        BENEVOLENT_HP_REDUCTION = UUID.fromString("bb3be804-44c2-474a-af69-b300f5d01bc7");
        OMNISCIENT_SPEED_REDUCTION = UUID.fromString("3d0402b6-4edc-49fc-ada6-23700a9737ac");
        MALEVOLENCE_DAMAGE_REDUCTION = UUID.fromString("5d54dcbf-cb04-4716-85b7-e262080049c0");
        BENEVOLENT_HP_INCREASE = UUID.fromString("9093f3ee-64d8-4d64-b410-7052872f4b94");
        OMNISCIENT_ARMOR_INCREASE = UUID.fromString("15f0faaa-c014-4063-a3e5-ae801a95e721");
        MALEVOLENCE_DAMAGE_INCREASE = UUID.fromString("0011379d-97e7-44b1-860e-9d355746e886");
        rand = new Random();
        messages = new HashMap<PlayerFavourData.VaultGodType, InfluenceMessages>();
        final InfluenceMessages benevolent = new InfluenceMessages();
        benevolent.positiveMessages.add("Our domain's ground will carve a path.");
        benevolent.positiveMessages.add("Tread upon our domain with care and it will respond in kind.");
        benevolent.positiveMessages.add("May your desire blossom into a wildfire.");
        benevolent.positiveMessages.add("Creation bends to our will.");
        benevolent.negativeMessages.add("Nature rises against you.");
        benevolent.negativeMessages.add("Prosperity withers at your touch.");
        benevolent.negativeMessages.add("Defile, rot, decay and fester.");
        benevolent.negativeMessages.add("The flower of your aspirations will waste away.");
        VaultInfluenceHandler.messages.put(PlayerFavourData.VaultGodType.BENEVOLENT, benevolent);
        final InfluenceMessages omniscient = new InfluenceMessages();
        omniscient.positiveMessages.add("May foresight guide your step.");
        omniscient.positiveMessages.add("Careful planning and strategy may lead you.");
        omniscient.positiveMessages.add("A set choice; followed through and flawlessly executed.");
        omniscient.positiveMessages.add("Chance's hand may favour your goals.");
        omniscient.negativeMessages.add("A choice; leading one to disfavour.");
        omniscient.negativeMessages.add("Riches, Wealth, Prosperity. An illusion.");
        omniscient.negativeMessages.add("Cascading eventuality. Solidified in ruin.");
        omniscient.negativeMessages.add("Diminishing reality.");
        VaultInfluenceHandler.messages.put(PlayerFavourData.VaultGodType.OMNISCIENT, omniscient);
        final InfluenceMessages timekeeper = new InfluenceMessages();
        timekeeper.positiveMessages.add("Seize the opportunity.");
        timekeeper.positiveMessages.add("A single instant, stretched to infinity.");
        timekeeper.positiveMessages.add("Your future glows golden with possibility.");
        timekeeper.positiveMessages.add("Hasten and value every passing moment.");
        timekeeper.negativeMessages.add("Eternity in the moment of standstill.");
        timekeeper.negativeMessages.add("Drown in the flow of time.");
        timekeeper.negativeMessages.add("Transience manifested.");
        timekeeper.negativeMessages.add("Immutable emptiness.");
        VaultInfluenceHandler.messages.put(PlayerFavourData.VaultGodType.TIMEKEEPER, timekeeper);
        final InfluenceMessages malevolence = new InfluenceMessages();
        malevolence.positiveMessages.add("Enforce your path through obstacles.");
        malevolence.positiveMessages.add("Our vigor may aid your conquest.");
        malevolence.positiveMessages.add("Cherish this mote of my might.");
        malevolence.positiveMessages.add("A tempest incarnate.");
        malevolence.negativeMessages.add("Feel our domain's wrath.");
        malevolence.negativeMessages.add("Malice and spite given form.");
        malevolence.negativeMessages.add("Flee before the growing horde.");
        malevolence.negativeMessages.add("Perish from your own ambition.");
        VaultInfluenceHandler.messages.put(PlayerFavourData.VaultGodType.MALEVOLENCE, malevolence);
    }

    private static class InfluenceMessages {
        private final List<String> positiveMessages;
        private final List<String> negativeMessages;

        private InfluenceMessages() {
            this.positiveMessages = new ArrayList<String>();
            this.negativeMessages = new ArrayList<String>();
        }

        private String getNegativeMessage() {
            return this.negativeMessages.get(VaultInfluenceHandler.rand.nextInt(this.negativeMessages.size()));
        }

        private String getPositiveMessage() {
            return this.positiveMessages.get(VaultInfluenceHandler.rand.nextInt(this.positiveMessages.size()));
        }
    }
}
