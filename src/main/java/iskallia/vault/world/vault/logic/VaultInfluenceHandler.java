package iskallia.vault.world.vault.logic;

import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.*;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;

import java.util.Map;
import java.util.UUID;

public class VaultInfluenceHandler {
    private static final float influenceChance = 0.66F;
    private static final UUID BENEVOLENT_HP_REDUCTION = UUID.fromString("bb3be804-44c2-474a-af69-b300f5d01bc7");
    private static final UUID OMNISCIENT_SPEED_REDUCTION = UUID.fromString("3d0402b6-4edc-49fc-ada6-23700a9737ac");
    private static final UUID MALEVOLENCE_DAMAGE_REDUCTION = UUID.fromString("5d54dcbf-cb04-4716-85b7-e262080049c0");

    private static final UUID BENEVOLENT_HP_INCREASE = UUID.fromString("9093f3ee-64d8-4d64-b410-7052872f4b94");
    private static final UUID OMNISCIENT_ARMOR_INCREASE = UUID.fromString("15f0faaa-c014-4063-a3e5-ae801a95e721");
    private static final UUID MALEVOLENCE_DAMAGE_INCREASE = UUID.fromString("0011379d-97e7-44b1-860e-9d355746e886");

    private static final Random rand = new Random();
    private static final Map<PlayerFavourData.VaultGodType, InfluenceMessages> messages = new HashMap<>();

    public static void initializeInfluences(VaultRaid vault, ServerWorld world) {
        int vaultLvl = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        if (vaultLvl < 50) {
            return;
        }
        CrystalData data = vault.getProperties().getBase(VaultRaid.CRYSTAL_DATA).orElse(null);
        if (data == null ||
                !data.canTriggerInfluences() ||
                !data.getType().canTriggerInfluences() || vault
                .getPlayers().size() > 1) {
            return;
        }
        if (vault.getAllObjectives().stream().anyMatch(VaultObjective::preventsInfluences)) {
            return;
        }

        VaultInfluences influences = vault.getInfluences();
        PlayerFavourData favourData = PlayerFavourData.get(world);

        Map<PlayerFavourData.VaultGodType, Integer> positives = new HashMap<>();
        Map<PlayerFavourData.VaultGodType, Integer> negatives = new HashMap<>();

        for (PlayerFavourData.VaultGodType type : PlayerFavourData.VaultGodType.values()) {
            for (VaultPlayer vPlayer : vault.getPlayers()) {
                int favour = favourData.getFavour(vPlayer.getPlayerId(), type);
                if (Math.abs(favour) < 4) {
                    continue;
                }
                if (rand.nextFloat() >= 0.66F) {
                    continue;
                }

                if (favour < 0) {
                    negatives.put(type, Integer.valueOf(favour));
                    break;
                }
                positives.put(type, Integer.valueOf(favour));
            }
        }


        positives.forEach((type, favour) -> {
            Tuple<VaultInfluence, String> influenceResult = getPositiveInfluence(type, Math.abs(favour.intValue()));


            influences.addInfluence((VaultInfluence) influenceResult.getA(), vault, world);


            String message = ((InfluenceMessages) messages.get(type)).getPositiveMessage();


            IFormattableTextComponent vgName = (new StringTextComponent(type.getName())).withStyle(type.getChatColor());


            vgName.withStyle(());

            StringTextComponent stringTextComponent = new StringTextComponent("");

            stringTextComponent.append((ITextComponent) (new StringTextComponent("[VG] ")).withStyle(TextFormatting.DARK_PURPLE)).append((ITextComponent) vgName).append((ITextComponent) (new StringTextComponent(": ")).withStyle(TextFormatting.WHITE)).append((ITextComponent) new StringTextComponent(message));

            IFormattableTextComponent info = (new StringTextComponent((String) influenceResult.getB())).withStyle(TextFormatting.DARK_GRAY);

            vault.getPlayers().forEach(());
        });

        negatives.forEach((type, favour) -> {
            Tuple<VaultInfluence, String> influenceResult = getNegativeInfluence(type, Math.abs(favour.intValue()));
            influences.addInfluence((VaultInfluence) influenceResult.getA(), vault, world);
            String message = ((InfluenceMessages) messages.get(type)).getNegativeMessage();
            IFormattableTextComponent vgName = (new StringTextComponent(type.getName())).withStyle(type.getChatColor());
            vgName.withStyle(());
            StringTextComponent stringTextComponent = new StringTextComponent("");
            stringTextComponent.append((ITextComponent) (new StringTextComponent("[VG] ")).withStyle(TextFormatting.DARK_PURPLE)).append((ITextComponent) vgName).append((ITextComponent) (new StringTextComponent(": ")).withStyle(TextFormatting.WHITE)).append((ITextComponent) new StringTextComponent(message));
            IFormattableTextComponent info = (new StringTextComponent((String) influenceResult.getB())).withStyle(TextFormatting.DARK_GRAY);
            vault.getPlayers().forEach(());
        });
    }

    private static Tuple<VaultInfluence, String> getPositiveInfluence(PlayerFavourData.VaultGodType type, int favour) {
        EffectInfluence effectInfluence2;
        VaultAttributeInfluence vaultAttributeInfluence4;
        EffectInfluence effectInfluence1;
        VaultAttributeInfluence vaultAttributeInfluence3;
        TimeInfluence timeInfluence;
        VaultAttributeInfluence vaultAttributeInfluence2;
        DamageInfluence damageInfluence;
        VaultAttributeInfluence vaultAttributeInfluence1;
        String text;
        int ampl, heal, increased, time, more;
        float healPerc;
        int cdReduction;
        float perc;
        int incDrops;
        DecimalFormat percentFormat = new DecimalFormat("0.##");


        switch (type) {
            case BENEVOLENT:
                switch (rand.nextInt(2)) {
                    case 1:
                        ampl = MathHelper.clamp((favour - 4) / 8 + 1, 1, 2);

                        effectInfluence2 = new EffectInfluence(Effects.REGENERATION, ampl);
                        text = "Grants +" + ampl + " Regeneration";


                        return new Tuple(effectInfluence2, text);
                }
                heal = 50 + MathHelper.ceil((favour - 4) * 4.166666F);
                healPerc = heal / 100.0F;
                vaultAttributeInfluence4 = new VaultAttributeInfluence(VaultAttributeInfluence.Type.HEALING_EFFECTIVENESS, 1.0F + healPerc, true);
                text = "Effectiveness of Healing is increased by " + heal + "%";
                return new Tuple(vaultAttributeInfluence4, text);
            case OMNISCIENT:
                switch (rand.nextInt(2)) {
                    case 1:
                        ampl = MathHelper.clamp(favour / 5, 1, 3);

                        effectInfluence1 = new EffectInfluence(Effects.LUCK, ampl);
                        text = "Grants +" + ampl + " Luck";


                        return new Tuple(effectInfluence1, text);
                }
                increased = 25 + Math.min(Math.round((favour - 4) * 6.25F), 75);
                vaultAttributeInfluence3 = new VaultAttributeInfluence(VaultAttributeInfluence.Type.CHEST_RARITY, increased / 100.0F, false);
                text = "Grants " + increased + "% Chest Rarity";
                return new Tuple(vaultAttributeInfluence3, text);
            case TIMEKEEPER:
                switch (rand.nextInt(2)) {
                    case 1:
                        time = favour / 4;

                        timeInfluence = new TimeInfluence(time * 60 * 20);
                        text = "Grants " + time + " additional minutes";


                        return new Tuple(timeInfluence, text);
                }
                cdReduction = 10 + Math.round((favour - 4) * 2.5F);
                vaultAttributeInfluence2 = new VaultAttributeInfluence(VaultAttributeInfluence.Type.COOLDOWN_REDUCTION, 1.0F + cdReduction / 100.0F, true);
                text = "Grants +" + cdReduction + "% Cooldown Reduction";
                return new Tuple(vaultAttributeInfluence2, text);
            case MALEVOLENCE:
                switch (rand.nextInt(2)) {
                    case 1:
                        more = 25 + Math.round((favour - 4) * 14.58F);
                        perc = 1.0F + more / 100.0F;

                        damageInfluence = new DamageInfluence(perc);
                        text = "You deal " + more + "% more damage";


                        return new Tuple(damageInfluence, text);
                }
                incDrops = 100 + (favour - 4) * 25;
                vaultAttributeInfluence1 = new VaultAttributeInfluence(VaultAttributeInfluence.Type.SOUL_SHARD_DROPS, 1.0F + incDrops / 100.0F, true);
                text = "Monsters drop " + incDrops + "% more Soul Shards.";
                return new Tuple(vaultAttributeInfluence1, text);
        }
        throw new IllegalArgumentException("Unknown type: " + type.name());
    }

    private static Tuple<VaultInfluence, String> getNegativeInfluence(PlayerFavourData.VaultGodType type, int favour) {
        MobsInfluence mobsInfluence;
        VaultAttributeInfluence vaultAttributeInfluence2;
        VaultAttributeInfluence vaultAttributeInfluence1;
        EffectInfluence effectInfluence;
        TimeInfluence timeInfluence;
        MobAttributeInfluence mobAttributeInfluence2;
        MobAttributeInfluence mobAttributeInfluence1;
        DamageInfluence damageInfluence;
        String text;
        int ampl;
        int i;
        int decreased;
        int time;
        int more;
        int reduced;
        int j;
        int less;
        float perc;
        switch (type) {
            case BENEVOLENT:
                switch (rand.nextInt(2)) {
                    case 1:
                        i = 4 + Math.round((favour - 4) * 0.5F);

                        mobsInfluence = new MobsInfluence(i);
                        text = i + " additional monsters spawn around you";


                        return new Tuple(mobsInfluence, text);
                }
                reduced = 10 + Math.round((favour - 4) * 3.3333F);
                vaultAttributeInfluence2 = new VaultAttributeInfluence(VaultAttributeInfluence.Type.HEALING_EFFECTIVENESS, 1.0F - reduced / 100.0F, true);
                text = "Effectiveness of Healing is reduced by " + reduced + "%";
                return new Tuple(vaultAttributeInfluence2, text);
            case OMNISCIENT:
                switch (rand.nextInt(2)) {
                    case 1:
                        decreased = 25 + Math.min(Math.round((favour - 4) * 6.25F), 75);

                        vaultAttributeInfluence1 = new VaultAttributeInfluence(VaultAttributeInfluence.Type.CHEST_RARITY, -decreased / 100.0F, false);
                        text = "Reduces Chest Rarity by " + decreased + "%";


                        return new Tuple(vaultAttributeInfluence1, text);
                }
                ampl = MathHelper.clamp(favour / 5, 1, 3);
                effectInfluence = new EffectInfluence(Effects.UNLUCK, ampl);
                text = "Applies -" + ampl + " Luck";
                return new Tuple(effectInfluence, text);
            case TIMEKEEPER:
                switch (rand.nextInt(2)) {
                    case 1:
                        time = 1 + (favour - 4) / 6;

                        timeInfluence = new TimeInfluence(-time * 60 * 20);
                        text = "Removes " + time + " minutes";


                        return new Tuple(timeInfluence, text);
                }
                j = 10 + Math.round((favour - 4) * 3.333F);
                perc = j / 100.0F;
                mobAttributeInfluence2 = new MobAttributeInfluence(Attributes.MOVEMENT_SPEED, new AttributeModifier(OMNISCIENT_SPEED_REDUCTION, "Favours", perc, AttributeModifier.Operation.MULTIPLY_TOTAL));
                text = "Monsters move " + j + "% faster";
                return new Tuple(mobAttributeInfluence2, text);
            case MALEVOLENCE:
                switch (rand.nextInt(2)) {
                    case 1:
                        more = 20 + (favour - 4) * 15;

                        mobAttributeInfluence1 = new MobAttributeInfluence(Attributes.MAX_HEALTH, new AttributeModifier(BENEVOLENT_HP_REDUCTION, "Favours", (more / 100.0F), AttributeModifier.Operation.MULTIPLY_TOTAL));

                        text = "Monsters have " + more + "% more Health";


                        return new Tuple(mobAttributeInfluence1, text);
                }
                less = 10 + Math.round((favour - 4) * 5.416666F);
                damageInfluence = new DamageInfluence(1.0F - less / 100.0F);
                text = "You deal " + less + "% less damage";
                return new Tuple(damageInfluence, text);
        }
        throw new IllegalArgumentException("Unknown type: " + type.name());
    }


    static {
        InfluenceMessages benevolent = new InfluenceMessages();
        benevolent.positiveMessages.add("Our domain's ground will carve a path.");
        benevolent.positiveMessages.add("Tread upon our domain with care and it will respond in kind.");
        benevolent.positiveMessages.add("May your desire blossom into a wildfire.");
        benevolent.positiveMessages.add("Creation bends to our will.");

        benevolent.negativeMessages.add("Nature rises against you.");
        benevolent.negativeMessages.add("Prosperity withers at your touch.");
        benevolent.negativeMessages.add("Defile, rot, decay and fester.");
        benevolent.negativeMessages.add("The flower of your aspirations will waste away.");
        messages.put(PlayerFavourData.VaultGodType.BENEVOLENT, benevolent);

        InfluenceMessages omniscient = new InfluenceMessages();
        omniscient.positiveMessages.add("May foresight guide your step.");
        omniscient.positiveMessages.add("Careful planning and strategy may lead you.");
        omniscient.positiveMessages.add("A set choice; followed through and flawlessly executed.");
        omniscient.positiveMessages.add("Chance's hand may favour your goals.");

        omniscient.negativeMessages.add("A choice; leading one to disfavour.");
        omniscient.negativeMessages.add("Riches, Wealth, Prosperity. An illusion.");
        omniscient.negativeMessages.add("Cascading eventuality. Solidified in ruin.");
        omniscient.negativeMessages.add("Diminishing reality.");
        messages.put(PlayerFavourData.VaultGodType.OMNISCIENT, omniscient);

        InfluenceMessages timekeeper = new InfluenceMessages();
        timekeeper.positiveMessages.add("Seize the opportunity.");
        timekeeper.positiveMessages.add("A single instant, stretched to infinity.");
        timekeeper.positiveMessages.add("Your future glows golden with possibility.");
        timekeeper.positiveMessages.add("Hasten and value every passing moment.");

        timekeeper.negativeMessages.add("Eternity in the moment of standstill.");
        timekeeper.negativeMessages.add("Drown in the flow of time.");
        timekeeper.negativeMessages.add("Transience manifested.");
        timekeeper.negativeMessages.add("Immutable emptiness.");
        messages.put(PlayerFavourData.VaultGodType.TIMEKEEPER, timekeeper);

        InfluenceMessages malevolence = new InfluenceMessages();
        malevolence.positiveMessages.add("Enforce your path through obstacles.");
        malevolence.positiveMessages.add("Our vigor may aid your conquest.");
        malevolence.positiveMessages.add("Cherish this mote of my might.");
        malevolence.positiveMessages.add("A tempest incarnate.");

        malevolence.negativeMessages.add("Feel our domain's wrath.");
        malevolence.negativeMessages.add("Malice and spite given form.");
        malevolence.negativeMessages.add("Flee before the growing horde.");
        malevolence.negativeMessages.add("Perish from your own ambition.");
        messages.put(PlayerFavourData.VaultGodType.MALEVOLENCE, malevolence);
    }

    private static class InfluenceMessages {
        private final List<String> positiveMessages = new ArrayList<>();
        private final List<String> negativeMessages = new ArrayList<>();

        private String getNegativeMessage() {
            return this.negativeMessages.get(VaultInfluenceHandler.rand.nextInt(this.negativeMessages.size()));
        }

        private String getPositiveMessage() {
            return this.positiveMessages.get(VaultInfluenceHandler.rand.nextInt(this.positiveMessages.size()));
        }

        private InfluenceMessages() {
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\VaultInfluenceHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */