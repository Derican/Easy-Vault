package iskallia.vault.item.gear;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import iskallia.vault.Vault;
import iskallia.vault.attribute.*;
import iskallia.vault.config.VaultGearConfig;
import iskallia.vault.config.VaultGearScalingConfig;
import iskallia.vault.init.*;
import iskallia.vault.item.FlawedRubyItem;
import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.set.PlayerSet;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.ArtisanTalent;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.calc.CooldownHelper;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.extensions.IForgeItem;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public interface VaultGear<T extends Item & VaultGear<? extends Item>> extends IForgeItem {
    public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("0.##");

    public static final int MAX_EXPECTED_TIER = 3;
    public static final UUID[] ARMOR_MODIFIERS = new UUID[]{
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"),
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"),
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"),
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")
    };


    public static final int ROLL_TIME = 120;


    public static final int ENTRIES_PER_ROLL = 50;


    default boolean isIntendedForSlot(EquipmentSlotType slotType) {
        return (getIntendedSlot() == slotType);
    }

    default boolean isDamageable(T item, ItemStack stack) {
        return ModAttributes.DURABILITY.exists(stack);
    }

    default int getMaxDamage(T item, ItemStack stack, int maxDamage) {
        return ((Integer) ((IntegerAttribute) ModAttributes.DURABILITY.getOrDefault(stack, Integer.valueOf(maxDamage))).getValue(stack)).intValue();
    }

    default ITextComponent getDisplayName(T item, ItemStack stack, ITextComponent name) {
        String customName = (String) ((StringAttribute) ModAttributes.GEAR_NAME.getOrDefault(stack, "")).getValue(stack);
        if (!customName.isEmpty()) {
            Rarity rarity = (Rarity) ((EnumAttribute) ModAttributes.GEAR_RARITY.getOrDefault(stack, Rarity.COMMON)).getValue(stack);
            Style style = name.getStyle().withColor(rarity.getColor());
            return (ITextComponent) (new StringTextComponent(customName)).setStyle(style);
        }
        if (((EnumAttribute) ModAttributes.GEAR_STATE.getOrDefault(stack, State.UNIDENTIFIED)).getValue(stack) == State.IDENTIFIED) {
            if (item == ModItems.ETCHING) {
                return name;
            }
            Rarity rarity = (Rarity) ((EnumAttribute) ModAttributes.GEAR_RARITY.getOrDefault(stack, Rarity.COMMON)).getValue(stack);
            Style style = name.getStyle().withColor(rarity.getColor());
            return (ITextComponent) ((IFormattableTextComponent) name).setStyle(style);
        }


        StringTextComponent stringTextComponent = new StringTextComponent("Unidentified ");
        return (ITextComponent) stringTextComponent.setStyle(name.getStyle()).append(name);
    }

    default boolean canApply(ItemStack stack, Enchantment enchantment) {
        return (!(enchantment instanceof net.minecraft.enchantment.MendingEnchantment) && !(enchantment instanceof net.minecraft.enchantment.ThornsEnchantment));
    }

    default ActionResult<ItemStack> onItemRightClick(T item, World world, PlayerEntity player, Hand hand, ActionResult<ItemStack> result) {
        ItemStack stack = player.getItemInHand(hand);

        if (world.isClientSide()) {
            return result;
        }

        if (world.dimension() != Vault.VAULT_KEY) {
            Optional<EnumAttribute<State>> attribute = ModAttributes.GEAR_STATE.get(stack);

            if (attribute.isPresent() && ((EnumAttribute) attribute.get()).getValue(stack) == State.UNIDENTIFIED) {
                ((EnumAttribute) attribute.get()).setBaseValue(State.ROLLING);
                return ActionResult.fail(stack);
            }
        }

        return result;
    }

    default void fillItemGroup(NonNullList<ItemStack> items) {
        for (int tier = 0; tier < 3; tier++) {
            ItemStack stack = new ItemStack((IItemProvider) this);
            ModAttributes.GEAR_TIER.create(stack, Integer.valueOf(tier));
            items.add(stack);
        }
    }

    default void splitStack(T item, ItemStack stack, World world, Entity entity) {
        if (world instanceof net.minecraft.world.server.ServerWorld && entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);

                    ItemStack gearPiece = stack.copy();
                    gearPiece.setCount(1);
                    MiscUtils.giveItem(player, gearPiece);
                }
            }
        }
    }

    default void inventoryTick(T item, ItemStack stack, World world, ServerPlayerEntity player, int itemSlot, boolean isSelected) {
        if (!ModAttributes.GEAR_RANDOM_SEED.exists(stack)) {
            ModAttributes.GEAR_RANDOM_SEED.create(stack, Long.valueOf(world.random.nextLong()));
        }
        if (((EnumAttribute) ModAttributes.GEAR_STATE.getOrCreate(stack, State.UNIDENTIFIED)).getValue(stack) == State.ROLLING) {
            tickRoll(item, stack, world, player, itemSlot, isSelected);
        }

        if (!ModAttributes.GEAR_ROLL_TYPE.exists(stack) && ModAttributes.GEAR_ROLL_POOL.exists(stack)) {
            ModAttributes.GEAR_ROLL_POOL.getBase(stack).ifPresent(pool -> {
                int playerLevel = PlayerVaultStatsData.get(player.getLevel()).getVaultStats(player.getUUID()).getVaultLevel();

                VaultGearScalingConfig.GearRarityOutcome outcome = ModConfigs.VAULT_GEAR_SCALING.getGearRollType(pool, playerLevel);
                if (outcome != null) {
                    ModAttributes.GEAR_TIER.create(stack, Integer.valueOf(outcome.getTier()));
                    ModAttributes.GEAR_ROLL_TYPE.create(stack, outcome.getRarity());
                }
            });
        }
        if (!ModAttributes.GEAR_ROLL_TYPE.exists(stack)) {
            ModAttributes.GEAR_ROLL_TYPE.create(stack, getDefaultRoll(player).getName());
        }
        if (!ModAttributes.GEAR_TIER.exists(stack)) {
            ModAttributes.GEAR_TIER.create(stack, Integer.valueOf(getDefaultGearTier(player)));
        }

        update(stack, world.getRandom());

        FlawedRubyItem.handleOutcome(player, stack);
    }

    default void tickRoll(T item, ItemStack stack, World world, ServerPlayerEntity player, int itemSlot, boolean isSelected) {
        int rollTicks = stack.getOrCreateTag().getInt("RollTicks");
        int lastModelHit = stack.getOrCreateTag().getInt("LastModelHit");
        double displacement = getDisplacement(rollTicks);

        if (player.getItemInHand(Hand.OFF_HAND).getItem() == ModItems.IDENTIFICATION_TOME) {
            String roll = (String) ((StringAttribute) ModAttributes.GEAR_ROLL_TYPE.getOrCreate(stack, getDefaultRoll(player).getName())).getValue(stack);
            Rarity rarity = ((VaultGearConfig.General.Roll) ModConfigs.VAULT_GEAR.getRoll(roll).orElse(getDefaultRoll(player))).getRandom(world.getRandom());

            ModAttributes.GEAR_RARITY.create(stack, rarity);
            ModAttributes.GEAR_MODEL.create(stack, Integer.valueOf(world.random.nextInt(getModelsFor(rarity))));
            ModAttributes.GEAR_COLOR.create(stack, Integer.valueOf((item instanceof VaultArmorItem) ? -1 : randomBaseColor(world.random)));

            if (item == ModItems.ETCHING) {
                Set set = Set.values()[world.random.nextInt((Set.values()).length)];
                ModAttributes.GEAR_SET.create(stack, set);
            }

            initialize(stack, world.getRandom());
            ModAttributes.GEAR_STATE.create(stack, State.IDENTIFIED);
            stack.getOrCreateTag().remove("RollTicks");
            stack.getOrCreateTag().remove("LastModelHit");
            world.playSound(null, player.blockPosition(), ModSounds.CONFETTI_SFX, SoundCategory.PLAYERS, 0.3F, 1.0F);

            return;
        }
        if (rollTicks >= 120) {
            initialize(stack, world.getRandom());
            ModAttributes.GEAR_STATE.create(stack, State.IDENTIFIED);
            stack.getOrCreateTag().remove("RollTicks");
            stack.getOrCreateTag().remove("LastModelHit");
            world.playSound(null, player.blockPosition(), ModSounds.CONFETTI_SFX, SoundCategory.PLAYERS, 0.5F, 1.0F);

            return;
        }
        if ((int) displacement != lastModelHit) {
            String roll = (String) ((StringAttribute) ModAttributes.GEAR_ROLL_TYPE.getOrCreate(stack, getDefaultRoll(player).getName())).getValue(stack);
            Rarity rarity = ((VaultGearConfig.General.Roll) ModConfigs.VAULT_GEAR.getRoll(roll).orElse(getDefaultRoll(player))).getRandom(world.getRandom());

            ModAttributes.GEAR_RARITY.create(stack, rarity);
            ModAttributes.GEAR_MODEL.create(stack, Integer.valueOf(world.random.nextInt(getModelsFor(rarity))));
            ModAttributes.GEAR_COLOR.create(stack, Integer.valueOf((item instanceof VaultArmorItem) ? -1 : randomBaseColor(world.random)));
            if (item == ModItems.ETCHING) {
                Set set = Set.values()[world.random.nextInt((Set.values()).length)];
                ModAttributes.GEAR_SET.create(stack, set);
            }

            stack.getOrCreateTag().putInt("LastModelHit", (int) displacement);
            world.playSound(null, player.blockPosition(), ModSounds.RAFFLE_SFX, SoundCategory.PLAYERS, 0.4F, 1.0F);
        }

        stack.getOrCreateTag().putInt("RollTicks", rollTicks + 1);
    }

    default VaultGearConfig.General.Roll getDefaultRoll(ServerPlayerEntity player) {
        TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        TalentNode<?> artisanNode = talents.getNodeOf(ModConfigs.TALENTS.ARTISAN);
        VaultGearConfig.General.Roll defaultRoll = ModConfigs.VAULT_GEAR.getDefaultRoll();

        if (artisanNode.isLearned() && artisanNode.getTalent() instanceof ArtisanTalent) {
            defaultRoll = ModConfigs.VAULT_GEAR.getRoll(((ArtisanTalent) artisanNode.getTalent()).getDefaultRoll()).orElse(defaultRoll);
        }
        return defaultRoll;
    }

    default int getDefaultGearTier(ServerPlayerEntity sPlayer) {
        int vaultLevel = PlayerVaultStatsData.get(sPlayer.getLevel()).getVaultStats((PlayerEntity) sPlayer).getVaultLevel();
        return ModConfigs.VAULT_GEAR_CRAFTING_SCALING.getRandomTier(vaultLevel);
    }

    static double getDisplacement(int tick) {
        double c = 7200.0D;

        return ((-tick * tick * tick) / 6.0D + c * tick) * 50.0D / (-288000.0D + c * 120.0D);
    }


    @OnlyIn(Dist.CLIENT)
    default void addInformation(T item, ItemStack stack, List<ITextComponent> tooltip, boolean displayDetails) {
        ModAttributes.GEAR_CRAFTED_BY.get(stack).map(attribute -> (String) attribute.getValue(stack)).ifPresent(crafter -> {
            if (!crafter.isEmpty()) {
                tooltip.add((new StringTextComponent("Crafted by: ")).append((ITextComponent) (new StringTextComponent(crafter)).setStyle(Style.EMPTY.withColor(Color.fromRgb(16770048)))));
            }
        });

        ModAttributes.GEAR_STATE.get(stack).map(attribute -> (State) attribute.getValue(stack)).ifPresent(state -> {
            if (state == State.IDENTIFIED) {
                return;
            }


//            ModAttributes.GEAR_ROLL_TYPE.get(stack).map(()).ifPresent(());
        });


//        ModAttributes.GEAR_STATE.get(stack).map(attribute -> (State) attribute.getValue(stack)).ifPresent(state -> ModAttributes.GEAR_TIER.get(stack).map(()).ifPresent(()));


        ModAttributes.IDOL_TYPE.get(stack).map(attribute -> (PlayerFavourData.VaultGodType) attribute.getValue(stack)).ifPresent(value -> {
            if (item instanceof IdolItem) {
                tooltip.add(((IdolItem) item).getType().getIdolDescription());
            }
        });
        ModAttributes.GEAR_RARITY.get(stack).map(attribute -> (Rarity) attribute.getValue(stack)).ifPresent(rarity -> {
            IFormattableTextComponent rarityText = (new StringTextComponent("Rarity: ")).append(rarity.getName());


            if (displayDetails) {
//                ModAttributes.GEAR_MODEL.get(stack).map(()).ifPresent(());
            }

            tooltip.add(rarityText);
        });

        if (item instanceof VaultArmorItem) {
            EquipmentSlotType equipmentSlot = ((VaultArmorItem) item).getEquipmentSlot(stack);
            Rarity rarity = (Rarity) ((EnumAttribute) ModAttributes.GEAR_RARITY.getOrDefault(stack, Rarity.SCRAPPY)).getValue(stack);
            Integer gearModel = (Integer) ((IntegerAttribute) ModAttributes.GEAR_MODEL.getOrDefault(stack, Integer.valueOf(-1))).getValue(stack);
            Integer gearSpecialModel = (Integer) ((IntegerAttribute) ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(stack, Integer.valueOf(-1))).getValue(stack);

            if (gearSpecialModel.intValue() != -1 && gearSpecialModel.intValue() == ModModels.SpecialGearModel.FAIRY_SET.modelForSlot(equipmentSlot).getId()) {
                tooltip.add(new StringTextComponent(""));
                tooltip.add((new StringTextComponent("Required in \"Grasshopper Ninja\" advancement")).withStyle(TextFormatting.GREEN));
            }

            if (rarity == Rarity.UNIQUE && gearSpecialModel.intValue() != -1 && ((VaultGear) item).getIntendedSlot() != null) {
                ModModels.SpecialGearModel model = ModModels.SpecialGearModel.getModel(((VaultGear) item).getIntendedSlot(), gearSpecialModel.intValue());
                if (model != null) {
                    tooltip.add(new StringTextComponent(model.getDisplayName() + " Armor Set"));
                }
            }
        }

        ModAttributes.GEAR_SET.get(stack).map(attribute -> (Set) attribute.getValue(stack)).ifPresent(value -> {
            if (value == Set.NONE)
                return;
            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add((new StringTextComponent("Etching: ")).append((ITextComponent) (new StringTextComponent(value.name())).withStyle(TextFormatting.RED)));
        });
        ModAttributes.MAX_REPAIRS.get(stack).map(attribute -> (Integer) attribute.getValue(stack)).ifPresent(value -> {
            if (value.intValue() <= 0) {
                return;
            }
            int current = ((Integer) ((IntegerAttribute) ModAttributes.CURRENT_REPAIRS.getOrDefault(stack, Integer.valueOf(0))).getValue(stack)).intValue();
            int unfilled = value.intValue() - current;
            tooltip.add((new StringTextComponent("Repairs: ")).append(tooltipDots(current, TextFormatting.YELLOW)).append(tooltipDots(unfilled, TextFormatting.GRAY)));
        });
        ModAttributes.GEAR_MAX_LEVEL.get(stack).map(attribute -> (Integer) attribute.getValue(stack)).ifPresent(value -> {
            if (value.intValue() <= 0) {
                return;
            }

            int current = ((Float) ((FloatAttribute) ModAttributes.GEAR_LEVEL.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).intValue();

            int unfilled = value.intValue() - current;
            tooltip.add((new StringTextComponent("Levels: ")).append(tooltipDots(current, TextFormatting.YELLOW)).append(tooltipDots(unfilled, TextFormatting.GRAY)));
        });
        if (((EnumAttribute) ModAttributes.GEAR_STATE.getOrDefault(stack, State.UNIDENTIFIED)).getValue(stack) == State.IDENTIFIED) {
            addModifierInformation(stack, tooltip, displayDetails);
        }

        ModAttributes.REFORGED.get(stack).map(attribute -> (Boolean) attribute.getValue(stack)).filter(b -> b.booleanValue()).ifPresent(value -> {
            tooltip.add((new StringTextComponent("Reforged")).setStyle(Style.EMPTY.withColor(Color.fromRgb(14833698))));

            if (displayDetails) {
                tooltip.add((new StringTextComponent(" Has been reforged with Artisan Scroll")).withStyle(TextFormatting.DARK_GRAY));
            }
        });
        ModAttributes.IMBUED.get(stack).map(attribute -> (Boolean) attribute.getValue(stack)).filter(b -> b.booleanValue()).ifPresent(value -> {
            tooltip.add((new StringTextComponent("Imbued")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16772263))));

            if (displayDetails) {
                tooltip.add((new StringTextComponent(" Has been imbued with a Flawed Ruby")).withStyle(TextFormatting.DARK_GRAY));
            }
        });
        ModAttributes.IDOL_AUGMENTED.get(stack).map(attribute -> (Boolean) attribute.getValue(stack)).filter(b -> b.booleanValue()).ifPresent(value -> {
//            tooltip.add((new StringTextComponent("Hallowed")).withStyle(()));

            if (displayDetails) {
                tooltip.add((new StringTextComponent(" Adds +3000 Durability")).withStyle(TextFormatting.DARK_GRAY));
            }
        });
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

        if (enchantments.size() > 0) {
            tooltip.add(new StringTextComponent(""));
        }

        ModAttributes.MIN_VAULT_LEVEL.get(stack).map(attribute -> (Integer) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("Requires level: ")).append((ITextComponent) (new StringTextComponent(value + "")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16770048))))));


        if (FlawedRubyItem.shouldHandleOutcome(stack)) {
            tooltip.add((new StringTextComponent("Flawed Ruby Applied")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16772263))));
            if (displayDetails) {
                tooltip.add((new StringTextComponent(" A Flawed Ruby has been applied")).withStyle(TextFormatting.DARK_GRAY));
                tooltip.add((new StringTextComponent(" and is unstable. This may break")).withStyle(TextFormatting.DARK_GRAY));
                tooltip.add((new StringTextComponent(" or become imbued and gain an")).withStyle(TextFormatting.DARK_GRAY));
                tooltip.add((new StringTextComponent(" additional modifier slot.")).withStyle(TextFormatting.DARK_GRAY));
                tooltip.add((new StringTextComponent(" Also a small chance nothing will happen.")).withStyle(TextFormatting.DARK_GRAY));
            }
        }
    }

    default void addModifierInformation(ItemStack stack, List<ITextComponent> tooltip, boolean displayDetails) {
        ModAttributes.ADD_ARMOR.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format(value.doubleValue(), 5) + " Armor")).setStyle(Style.EMPTY.withColor(Color.fromRgb(4766456)))));


        ModAttributes.ADD_ARMOR_2.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format(value.doubleValue(), 5) + " Armor")).setStyle(Style.EMPTY.withColor(Color.fromRgb(4766456)))));


        ModAttributes.ADD_ARMOR_TOUGHNESS.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format(value.doubleValue(), 5) + " Armor Toughness")).setStyle(Style.EMPTY.withColor(Color.fromRgb(13302672)))));


        ModAttributes.ADD_ARMOR_TOUGHNESS_2.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format(value.doubleValue(), 5) + " Armor Toughness")).setStyle(Style.EMPTY.withColor(Color.fromRgb(13302672)))));


        ModAttributes.THORNS_CHANCE.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Thorns Chance")).setStyle(Style.EMPTY.withColor(Color.fromRgb(7195648)))));


        ModAttributes.THORNS_DAMAGE.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Thorns Damage")).setStyle(Style.EMPTY.withColor(Color.fromRgb(3646976)))));


        ModAttributes.ADD_KNOCKBACK_RESISTANCE.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent(value.doubleValue() * 100.0D) + "% Knockback Resistance")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16756751)))));


        ModAttributes.ADD_KNOCKBACK_RESISTANCE_2.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent(value.doubleValue() * 100.0D) + "% Knockback Resistance")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16756751)))));


        ModAttributes.ADD_ATTACK_DAMAGE.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format(value.doubleValue(), 5) + " Attack Damage")).setStyle(Style.EMPTY.withColor(Color.fromRgb(13116966)))));


        ModAttributes.ADD_ATTACK_DAMAGE_2.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format(value.doubleValue(), 5) + " Attack Damage")).setStyle(Style.EMPTY.withColor(Color.fromRgb(13116966)))));


        ModAttributes.ADD_ATTACK_SPEED.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format(value.doubleValue(), 5) + " Attack Speed")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16767592)))));


        ModAttributes.ADD_ATTACK_SPEED_2.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format(value.doubleValue(), 5) + " Attack Speed")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16767592)))));


        ModAttributes.DAMAGE_INCREASE.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format((value.floatValue() * 100.0F), 5) + "% increased Damage")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16739072)))));


        ModAttributes.DAMAGE_INCREASE_2.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format((value.floatValue() * 100.0F), 5) + "% increased Damage")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16739072)))));


        ModAttributes.ON_HIT_CHAIN.get(stack).map(attribute -> (Integer) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + value + " Chaining Attacks")).setStyle(Style.EMPTY.withColor(Color.fromRgb(6119096)))));


        ModAttributes.ON_HIT_AOE.get(stack).map(attribute -> (Integer) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + value + " Attack AoE")).setStyle(Style.EMPTY.withColor(Color.fromRgb(12085504)))));


        ModAttributes.ON_HIT_STUN.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Stun Attack Chance")).setStyle(Style.EMPTY.withColor(Color.fromRgb(1681124)))));


        ModAttributes.DAMAGE_ILLAGERS.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Spiteful")).setStyle(Style.EMPTY.withColor(Color.fromRgb(40882)))));


        ModAttributes.DAMAGE_SPIDERS.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Baneful")).setStyle(Style.EMPTY.withColor(Color.fromRgb(8281694)))));


        ModAttributes.DAMAGE_UNDEAD.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Holy")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16382128)))));


        ModAttributes.ADD_DURABILITY.get(stack).map(attribute -> (Integer) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + value + " Durability")).setStyle(Style.EMPTY.withColor(Color.fromRgb(14668030)))));


        ModAttributes.ADD_DURABILITY_2.get(stack).map(attribute -> (Integer) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + value + " Durability")).setStyle(Style.EMPTY.withColor(Color.fromRgb(14668030)))));


        ModAttributes.ADD_PLATING.get(stack).map(attribute -> (Integer) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + value + " Plating")).setStyle(Style.EMPTY.withColor(Color.fromRgb(14668030)))));


        ModAttributes.ADD_REACH.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + value + " Reach")).setStyle(Style.EMPTY.withColor(Color.fromRgb(8706047)))));


        ModAttributes.ADD_REACH_2.get(stack).map(attribute -> (Double) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + value + " Reach")).setStyle(Style.EMPTY.withColor(Color.fromRgb(8706047)))));


        ModAttributes.ADD_FEATHER_FEET.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format((value.floatValue() * 100.0F), 5) + "% Feather Feet")).setStyle(Style.EMPTY.withColor(Color.fromRgb(13499899)))));


        ModAttributes.ADD_MIN_VAULT_LEVEL.get(stack).map(attribute -> (Integer) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent(((value.intValue() < 0) ? "-" : "+") + Math.abs(value.intValue()) + " Min Vault Level")).setStyle(Style.EMPTY.withColor(Color.fromRgb(15523772)))));


        ModAttributes.ADD_COOLDOWN_REDUCTION.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent(((value.floatValue() < 0.0F) ? "-" : "+") + format((Math.abs(value.floatValue()) * 100.0F), 5) + "% Cooldown Reduction")).setStyle(Style.EMPTY.withColor(Color.fromRgb(63668)))));


        ModAttributes.ADD_COOLDOWN_REDUCTION_2.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent(((value.floatValue() < 0.0F) ? "-" : "+") + format((Math.abs(value.floatValue()) * 100.0F), 5) + "% Cooldown Reduction")).setStyle(Style.EMPTY.withColor(Color.fromRgb(63668)))));


        ModAttributes.EXTRA_LEECH_RATIO.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format((value.floatValue() * 100.0F), 5) + "% Leech")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16716820)))));


        ModAttributes.ADD_EXTRA_LEECH_RATIO.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format((value.floatValue() * 100.0F), 5) + "% Leech")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16716820)))));


        ModAttributes.FATAL_STRIKE_CHANCE.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Fatal Strike Chance")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16523264)))));


        ModAttributes.FATAL_STRIKE_DAMAGE.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Fatal Strike Damage")).setStyle(Style.EMPTY.withColor(Color.fromRgb(12520704)))));


        ModAttributes.EXTRA_HEALTH.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format(value.floatValue(), 5) + " Health")).setStyle(Style.EMPTY.withColor(Color.fromRgb(2293541)))));


        ModAttributes.ADD_EXTRA_HEALTH.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + format(value.floatValue(), 5) + " Health")).setStyle(Style.EMPTY.withColor(Color.fromRgb(2293541)))));


        ModAttributes.EXTRA_PARRY_CHANCE.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Parry")).setStyle(Style.EMPTY.withColor(Color.fromRgb(11534098)))));


        ModAttributes.ADD_EXTRA_PARRY_CHANCE.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Parry")).setStyle(Style.EMPTY.withColor(Color.fromRgb(11534098)))));


        ModAttributes.EXTRA_RESISTANCE.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Resistance")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16702720)))));


        ModAttributes.ADD_EXTRA_RESISTANCE.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Resistance")).setStyle(Style.EMPTY.withColor(Color.fromRgb(16702720)))));


        ModAttributes.CHEST_RARITY.get(stack).map(attribute -> (Float) attribute.getValue(stack)).ifPresent(value -> tooltip.add((new StringTextComponent("+" + formatPercent((value.floatValue() * 100.0F)) + "% Chest Rarity")).setStyle(Style.EMPTY.withColor(Color.fromRgb(11073085)))));


//        ModAttributes.EFFECT_IMMUNITY.get(stack).map(attribute -> (List) attribute.getValue(stack)).ifPresent(value -> value.forEach(()));


//        ModAttributes.EFFECT_CLOUD.get(stack).map(attribute -> (List) attribute.getValue(stack)).ifPresent(value -> value.forEach(()));


//        ModAttributes.EXTRA_EFFECTS.get(stack).map(attribute -> (List) attribute.getValue(stack)).ifPresent(value -> value.forEach(()));


        ModAttributes.SOULBOUND.get(stack).map(attribute -> (Boolean) attribute.getValue(stack)).filter(b -> b.booleanValue()).ifPresent(value -> {
            tooltip.add((new StringTextComponent("Soulbound")).setStyle(Style.EMPTY.withColor(Color.fromRgb(9856253))));
            if (displayDetails) {
                tooltip.add((new StringTextComponent(" Keep item on death in vault")).withStyle(TextFormatting.DARK_GRAY));
            }
        });
    }

    static String formatPercent(double value) {
        return PERCENT_FORMAT.format(value);
    }

    static String format(double value, int scale) {
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    default boolean canElytraFly(T item, ItemStack stack, LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            return PlayerSet.isActive(Set.DRAGON, entity);
        }
        return false;
    }

    default boolean elytraFlightTick(T item, ItemStack stack, LivingEntity entity, int flightTicks) {
        return canElytraFly(item, stack, entity);
    }

    default int getColor(T item, ItemStack stack) {
        EnumAttribute<State> stateAttribute = ModAttributes.GEAR_STATE.get(stack).orElse(null);
        if (stateAttribute == null || stateAttribute.getValue(stack) == State.UNIDENTIFIED) {
            return -1;
        }

        Rarity rarity = (Rarity) ((EnumAttribute) ModAttributes.GEAR_RARITY.getOrDefault(stack, Rarity.SCRAPPY)).getValue(stack);
        Integer dyeColor = getDyeColor(stack);

        if (rarity == Rarity.SCRAPPY && dyeColor == null) {
            return -1;
        }

        IntegerAttribute colorAttribute = ModAttributes.GEAR_COLOR.get(stack).orElse(null);
        int baseColor = (colorAttribute == null) ? -1 : ((Integer) colorAttribute.getValue(stack)).intValue();

        return (dyeColor != null) ? dyeColor.intValue() : baseColor;
    }


    @OnlyIn(Dist.CLIENT)
    default <A extends net.minecraft.client.renderer.entity.model.BipedModel<?>> A getArmorModel(T item, LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        Integer specialModelId = (Integer) ((IntegerAttribute) ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(itemStack, Integer.valueOf(-1))).getValue(itemStack);

        if (specialModelId.intValue() != -1) {

            ModModels.SpecialGearModel specialGearModel = (ModModels.SpecialGearModel) ModModels.SpecialGearModel.getRegistryForSlot(armorSlot).get(specialModelId);

            if (specialGearModel != null) {
                return (A) specialGearModel.getModel();
            }
        }

        Integer modelId = (Integer) ((IntegerAttribute) ModAttributes.GEAR_MODEL.getOrDefault(itemStack, Integer.valueOf(-1))).getValue(itemStack);
        Rarity rarity = (Rarity) ((EnumAttribute) ModAttributes.GEAR_RARITY.getOrDefault(itemStack, Rarity.SCRAPPY)).getValue(itemStack);


        ModModels.GearModel gearModel = (rarity == Rarity.SCRAPPY) ? (ModModels.GearModel) ModModels.GearModel.SCRAPPY_REGISTRY.get(modelId) : (ModModels.GearModel) ModModels.GearModel.REGISTRY.get(modelId);

        if (gearModel == null) {
            return null;
        }

        return (A) gearModel.forSlotType(armorSlot);
    }

    @OnlyIn(Dist.CLIENT)
    default String getArmorTexture(T item, ItemStack itemStack, Entity entity, EquipmentSlotType slot, String type) {
        Integer specialModelId = (Integer) ((IntegerAttribute) ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(itemStack, Integer.valueOf(-1))).getValue(itemStack);

        if (specialModelId.intValue() != -1) {

            ModModels.SpecialGearModel specialGearModel = (ModModels.SpecialGearModel) ModModels.SpecialGearModel.getRegistryForSlot(slot).get(specialModelId);

            if (specialGearModel != null) {
                return specialGearModel.getTextureName(slot, type);
            }
        }

        Integer modelId = (Integer) ((IntegerAttribute) ModAttributes.GEAR_MODEL.getOrDefault(itemStack, Integer.valueOf(-1))).getValue(itemStack);
        Rarity rarity = (Rarity) ((EnumAttribute) ModAttributes.GEAR_RARITY.getOrDefault(itemStack, Rarity.SCRAPPY)).getValue(itemStack);


        ModModels.GearModel gearModel = (rarity == Rarity.SCRAPPY) ? (ModModels.GearModel) ModModels.GearModel.SCRAPPY_REGISTRY.get(modelId) : (ModModels.GearModel) ModModels.GearModel.REGISTRY.get(modelId);

        if (gearModel == null) {
            return null;
        }

        return gearModel.getTextureName(slot, type);
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(T item, EquipmentSlotType slot, ItemStack stack, Multimap<Attribute, AttributeModifier> parent) {
        if (!((VaultGear) item).isIntendedForSlot(slot)) {
            return parent;
        }
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        Optional<DoubleAttribute> attackDamage = ModAttributes.ATTACK_DAMAGE.get(stack);
        Optional<DoubleAttribute> attackSpeed = ModAttributes.ATTACK_SPEED.get(stack);
        Optional<DoubleAttribute> armor = ModAttributes.ARMOR.get(stack);
        Optional<DoubleAttribute> armorToughness = ModAttributes.ARMOR_TOUGHNESS.get(stack);
        Optional<DoubleAttribute> knockbackResistance = ModAttributes.KNOCKBACK_RESISTANCE.get(stack);
        Optional<FloatAttribute> extraHealth = ModAttributes.EXTRA_HEALTH.get(stack);
        Optional<FloatAttribute> extraHealth2 = ModAttributes.ADD_EXTRA_HEALTH.get(stack);
        Optional<DoubleAttribute> reach = ModAttributes.REACH.get(stack);

        parent.forEach((attribute, modifier) -> {
            if (attribute == Attributes.ATTACK_DAMAGE && attackDamage.isPresent()) {
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(modifier.getId(), "Weapon modifier", ((Double) ((DoubleAttribute) attackDamage.get()).getValue(stack)).doubleValue(), AttributeModifier.Operation.ADDITION));
            } else if (attribute == Attributes.ATTACK_SPEED && attackSpeed.isPresent()) {
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(modifier.getId(), "Weapon modifier", ((Double) ((DoubleAttribute) attackSpeed.get()).getValue(stack)).doubleValue(), AttributeModifier.Operation.ADDITION));
            } else if (attribute == Attributes.ARMOR && armor.isPresent()) {
                builder.put(Attributes.ARMOR, new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", ((Double) ((DoubleAttribute) armor.get()).getValue(stack)).doubleValue(), AttributeModifier.Operation.ADDITION));
            } else if (attribute == Attributes.ARMOR_TOUGHNESS && armorToughness.isPresent()) {
                builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", ((Double) ((DoubleAttribute) armorToughness.get()).getValue(stack)).doubleValue(), AttributeModifier.Operation.ADDITION));
            } else if (attribute == Attributes.KNOCKBACK_RESISTANCE && knockbackResistance.isPresent()) {
                builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor knockback resistance", ((Double) ((DoubleAttribute) knockbackResistance.get()).getValue(stack)).doubleValue(), AttributeModifier.Operation.ADDITION));
            } else {
                builder.put(attribute, modifier);
            }
        });


        float health = ((Float) extraHealth.<Float>map(attribute -> (Float) attribute.getValue(stack)).orElse(Float.valueOf(0.0F))).floatValue() + ((Float) extraHealth2.<Float>map(attribute -> (Float) attribute.getValue(stack)).orElse(Float.valueOf(0.0F))).floatValue();

        if (health != 0.0F) {
            builder.put(Attributes.MAX_HEALTH, new AttributeModifier(itemHash((Item) item, 0L), "Extra Health", health, AttributeModifier.Operation.ADDITION));
        }

        reach.ifPresent(attribute -> builder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(itemHash(item, 1L), "Reach", ((Double) attribute.getValue(stack)).doubleValue(), AttributeModifier.Operation.ADDITION)));


        return (Multimap<Attribute, AttributeModifier>) builder.build();
    }

    static int getCooldownReduction(ServerPlayerEntity player, AbilityGroup<?, ?> abilityGroup, int cooldown) {
        float totalCooldown = MathHelper.clamp(CooldownHelper.getCooldownMultiplier(player, abilityGroup), 0.0F, 1.0F);

        if (PlayerSet.isActive(Set.RIFT, (LivingEntity) player)) {
            cooldown /= 2;
        }

        return Math.round(cooldown * (1.0F - totalCooldown));
    }

    static UUID itemHash(Item item, long salt) {
        return new UUID(salt, item.hashCode());
    }

    static void addLevel(ItemStack stack, float amount) {
        if (!(stack.getItem() instanceof VaultGear))
            return;
        int maxLevel = ((Integer) ((IntegerAttribute) ModAttributes.GEAR_MAX_LEVEL.getOrDefault(stack, Integer.valueOf(0))).getValue(stack)).intValue();
        float current = ((Float) ((FloatAttribute) ModAttributes.GEAR_LEVEL.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).floatValue();
        if ((int) current >= maxLevel)
            return;
        float newLevel = current + amount;
        int difference = (int) newLevel - (int) current;
        ModAttributes.GEAR_LEVEL.create(stack, Float.valueOf(newLevel));

        int toRoll = ((Integer) ((IntegerAttribute) ModAttributes.GEAR_MODIFIERS_TO_ROLL.getOrDefault(stack, Integer.valueOf(0))).getValue(stack)).intValue() + difference;
        if (toRoll != 0) ModAttributes.GEAR_MODIFIERS_TO_ROLL.create(stack, Integer.valueOf(toRoll));
    }

    static void decrementLevel(ItemStack stack, int removed) {
        float currentLevel = ((Float) ((FloatAttribute) ModAttributes.GEAR_LEVEL.getOrDefault(stack, Float.valueOf(0.0F))).getValue(stack)).floatValue();
        ModAttributes.GEAR_LEVEL.create(stack, Float.valueOf(Math.max(currentLevel - removed, 0.0F)));
    }

    static void incrementRepairs(ItemStack stack) {
        int curRepairs = ((Integer) ((IntegerAttribute) ModAttributes.CURRENT_REPAIRS.getOrDefault(stack, Integer.valueOf(0))).getValue(stack)).intValue();
        ModAttributes.CURRENT_REPAIRS.create(stack, Integer.valueOf(curRepairs + 1));
    }

    static void initialize(ItemStack stack, Random random) {
        ModAttributes.GEAR_RARITY.get(stack).map(attribute -> (Rarity) attribute.getValue(stack)).ifPresent(rarity -> {
            int tier = ((Integer) ((IntegerAttribute) ModAttributes.GEAR_TIER.getOrDefault(stack, Integer.valueOf(0))).getValue(stack)).intValue();
            VaultGearConfig.Tier tierConfig = (VaultGearConfig.get(rarity)).TIERS.get(tier);
//            tierConfig.getAttributes(stack).ifPresent(());
        });
    }


    static void update(ItemStack stack, Random random) {
        ModAttributes.GEAR_RARITY.get(stack).map(attribute -> (Rarity) attribute.getValue(stack)).ifPresent(rarity -> {
            int tier = ((Integer) ((IntegerAttribute) ModAttributes.GEAR_TIER.getOrDefault(stack, Integer.valueOf(0))).getValue(stack)).intValue();
            VaultGearConfig.Tier tierConfig = (VaultGearConfig.get(rarity)).TIERS.get(tier);
//            tierConfig.getModifiers(stack).ifPresent(());
        });
    }


    static int randomBaseColor(Random rand) {
        return BASE_COLORS[rand.nextInt(BASE_COLORS.length)].getColorValue();
    }

    static Integer getDyeColor(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTagElement("display");
        if (compoundnbt != null && compoundnbt.contains("color", 3)) {
            return Integer.valueOf(compoundnbt.getInt("color"));
        }
        return null;
    }

    static ITextComponent tooltipDots(int amount, TextFormatting formatting) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            text.append("⬢ ");
        }
        return (ITextComponent) (new StringTextComponent(text.toString()))
                .withStyle(formatting);
    }

    public static final DyeColor[] BASE_COLORS = new DyeColor[]{DyeColor.BLUE, DyeColor.BROWN, DyeColor.CYAN, DyeColor.GREEN, DyeColor.LIGHT_BLUE, DyeColor.LIME, DyeColor.MAGENTA, DyeColor.ORANGE, DyeColor.PINK, DyeColor.PURPLE, DyeColor.RED, DyeColor.WHITE, DyeColor.YELLOW};


    int getModelsFor(Rarity paramRarity);


    @Nullable
    EquipmentSlotType getIntendedSlot();


    public enum Type {
        SWORD, AXE, ARMOR;
    }

    public enum State {
        UNIDENTIFIED, ROLLING, IDENTIFIED;
    }

    public static class Material implements IArmorMaterial {
        public static final Material INSTANCE = new Material();


        public int getDurabilityForSlot(EquipmentSlotType slot) {
            return 0;
        }


        public int getDefenseForSlot(EquipmentSlotType slot) {
            return 0;
        }


        public int getEnchantmentValue() {
            return ArmorMaterial.DIAMOND.getEnchantmentValue();
        }


        public SoundEvent getEquipSound() {
            return ArmorMaterial.DIAMOND.getEquipSound();
        }


        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }


        public String getName() {
            return "vault_dummy";
        }


        public float getToughness() {
            return 0.0F;
        }


        public float getKnockbackResistance() {
            return 1.0E-4F;
        }
    }

    public static class Tier implements IItemTier {
        public static final Tier INSTANCE = new Tier();


        public int getUses() {
            return 0;
        }


        public float getSpeed() {
            return 0.0F;
        }


        public float getAttackDamageBonus() {
            return 0.0F;
        }


        public int getLevel() {
            return ItemTier.DIAMOND.getLevel();
        }


        public int getEnchantmentValue() {
            return ItemTier.DIAMOND.getEnchantmentValue();
        }


        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }
    }

    public enum Set {
        NONE,
        DRAGON,
        ZOD,
        NINJA,
        GOLEM,
        RIFT,
        CARAPACE,
        DIVINITY,
        DRYAD,
        BLOOD,
        VAMPIRE,
        TREASURE,
        ASSASSIN,
        PHOENIX,
        DREAM,
        PORCUPINE;
    }

    public enum Rarity {
        COMMON( Color.fromRgb(TextFormatting.AQUA.getColor())),
        RARE( Color.fromRgb(TextFormatting.YELLOW.getColor())),
        EPIC( Color.fromRgb(TextFormatting.LIGHT_PURPLE.getColor())),
        OMEGA( Color.fromRgb(TextFormatting.GREEN.getColor())),
        UNIQUE( Color.fromRgb(-1213660)),

        SCRAPPY( Color.fromRgb(TextFormatting.GRAY.getColor()));


        private final Color color;


        Rarity(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return this.color;
        }

        public ITextComponent getName() {
            Style style = Style.EMPTY.withColor(getColor());
            return (ITextComponent) (new StringTextComponent(name())).withStyle(style);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\VaultGear.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */