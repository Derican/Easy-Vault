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
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.ArtisanTalent;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.calc.CooldownHelper;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.MendingEnchantment;
import net.minecraft.enchantment.ThornsEnchantment;
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
import net.minecraft.world.server.ServerWorld;
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
    public static final UUID[] ARMOR_MODIFIERS = {UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    public static final int ROLL_TIME = 120;
    public static final int ENTRIES_PER_ROLL = 50;
    public static final DyeColor[] BASE_COLORS = {DyeColor.BLUE, DyeColor.BROWN, DyeColor.CYAN, DyeColor.GREEN, DyeColor.LIGHT_BLUE, DyeColor.LIME, DyeColor.MAGENTA, DyeColor.ORANGE, DyeColor.PINK, DyeColor.PURPLE, DyeColor.RED, DyeColor.WHITE, DyeColor.YELLOW};

    int getModelsFor(final Rarity p0);

    @Nullable
    EquipmentSlotType getIntendedSlot();

    default boolean isIntendedForSlot(final EquipmentSlotType slotType) {
        return this.getIntendedSlot() == slotType;
    }

    default boolean isDamageable(final T item, final ItemStack stack) {
        return ModAttributes.DURABILITY.exists(stack);
    }

    default int getMaxDamage(final T item, final ItemStack stack, final int maxDamage) {
        return ModAttributes.DURABILITY.getOrDefault(stack, maxDamage).getValue(stack);
    }

    default ITextComponent getDisplayName(final T item, final ItemStack stack, final ITextComponent name) {
        final String customName = ModAttributes.GEAR_NAME.getOrDefault(stack, "").getValue(stack);
        if (!customName.isEmpty()) {
            final Rarity rarity = ModAttributes.GEAR_RARITY.getOrDefault(stack, Rarity.COMMON).getValue(stack);
            final Style style = name.getStyle().withColor(rarity.getColor());
            return (ITextComponent) new StringTextComponent(customName).setStyle(style);
        }
        if (ModAttributes.GEAR_STATE.getOrDefault(stack, State.UNIDENTIFIED).getValue(stack) != State.IDENTIFIED) {
            final TextComponent prefix = (TextComponent) new StringTextComponent("Unidentified ");
            return (ITextComponent) prefix.setStyle(name.getStyle()).append(name);
        }
        if (item == ModItems.ETCHING) {
            return name;
        }
        final Rarity rarity = ModAttributes.GEAR_RARITY.getOrDefault(stack, Rarity.COMMON).getValue(stack);
        final Style style = name.getStyle().withColor(rarity.getColor());
        return (ITextComponent) ((IFormattableTextComponent) name).setStyle(style);
    }

    default boolean canApply(final ItemStack stack, final Enchantment enchantment) {
        return !(enchantment instanceof MendingEnchantment) && !(enchantment instanceof ThornsEnchantment);
    }

    default ActionResult<ItemStack> onItemRightClick(final T item, final World world, final PlayerEntity player, final Hand hand, final ActionResult<ItemStack> result) {
        final ItemStack stack = player.getItemInHand(hand);
        if (world.isClientSide()) {
            return result;
        }
        if (world.dimension() != Vault.VAULT_KEY) {
            final Optional<EnumAttribute<State>> attribute = ModAttributes.GEAR_STATE.get(stack);
            if (attribute.isPresent() && attribute.get().getValue(stack) == State.UNIDENTIFIED) {
                attribute.get().setBaseValue(State.ROLLING);
                return (ActionResult<ItemStack>) ActionResult.fail(stack);
            }
        }
        return result;
    }

    default void fillItemGroup(final NonNullList<ItemStack> items) {
        for (int tier = 0; tier < 3; ++tier) {
            final ItemStack stack = new ItemStack((IItemProvider) this);
            ModAttributes.GEAR_TIER.create(stack, tier);
            items.add(stack);
        }
    }

    default void splitStack(final T item, final ItemStack stack, final World world, final Entity entity) {
        if (world instanceof ServerWorld && entity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);
                    final ItemStack gearPiece = stack.copy();
                    gearPiece.setCount(1);
                    MiscUtils.giveItem(player, gearPiece);
                }
            }
        }
    }

    default void inventoryTick(final T item, final ItemStack stack, final World world, final ServerPlayerEntity player, final int itemSlot, final boolean isSelected) {
        if (!ModAttributes.GEAR_RANDOM_SEED.exists(stack)) {
            ModAttributes.GEAR_RANDOM_SEED.create(stack, world.random.nextLong());
        }
        if (ModAttributes.GEAR_STATE.getOrCreate(stack, State.UNIDENTIFIED).getValue(stack) == State.ROLLING) {
            this.tickRoll(item, stack, world, player, itemSlot, isSelected);
        }
        if (!ModAttributes.GEAR_ROLL_TYPE.exists(stack) && ModAttributes.GEAR_ROLL_POOL.exists(stack)) {
            ModAttributes.GEAR_ROLL_POOL.getBase(stack).ifPresent(pool -> {
                final int playerLevel = PlayerVaultStatsData.get(player.getLevel()).getVaultStats(player.getUUID()).getVaultLevel();
                final VaultGearScalingConfig.GearRarityOutcome outcome = ModConfigs.VAULT_GEAR_SCALING.getGearRollType(pool, playerLevel);
                if (outcome != null) {
                    ModAttributes.GEAR_TIER.create(stack, outcome.getTier());
                    ModAttributes.GEAR_ROLL_TYPE.create(stack, outcome.getRarity());
                }
                return;
            });
        }
        if (!ModAttributes.GEAR_ROLL_TYPE.exists(stack)) {
            ModAttributes.GEAR_ROLL_TYPE.create(stack, this.getDefaultRoll(player).getName());
        }
        if (!ModAttributes.GEAR_TIER.exists(stack)) {
            ModAttributes.GEAR_TIER.create(stack, this.getDefaultGearTier(player));
        }
        update(stack, world.getRandom());
        FlawedRubyItem.handleOutcome(player, stack);
    }

    default void tickRoll(final T item, final ItemStack stack, final World world, final ServerPlayerEntity player, final int itemSlot, final boolean isSelected) {
        final int rollTicks = stack.getOrCreateTag().getInt("RollTicks");
        final int lastModelHit = stack.getOrCreateTag().getInt("LastModelHit");
        final double displacement = getDisplacement(rollTicks);
        if (player.getItemInHand(Hand.OFF_HAND).getItem() == ModItems.IDENTIFICATION_TOME) {
            final String roll = ModAttributes.GEAR_ROLL_TYPE.getOrCreate(stack, this.getDefaultRoll(player).getName()).getValue(stack);
            final Rarity rarity = ModConfigs.VAULT_GEAR.getRoll(roll).orElse(this.getDefaultRoll(player)).getRandom(world.getRandom());
            ModAttributes.GEAR_RARITY.create(stack, rarity);
            ModAttributes.GEAR_MODEL.create(stack, world.random.nextInt(this.getModelsFor(rarity)));
            ModAttributes.GEAR_COLOR.create(stack, (item instanceof VaultArmorItem) ? -1 : randomBaseColor(world.random));
            if (item == ModItems.ETCHING) {
                final Set set = Set.values()[world.random.nextInt(Set.values().length)];
                ModAttributes.GEAR_SET.create(stack, set);
            }
            initialize(stack, world.getRandom());
            ModAttributes.GEAR_STATE.create(stack, State.IDENTIFIED);
            stack.getOrCreateTag().remove("RollTicks");
            stack.getOrCreateTag().remove("LastModelHit");
            world.playSound((PlayerEntity) null, player.blockPosition(), ModSounds.CONFETTI_SFX, SoundCategory.PLAYERS, 0.3f, 1.0f);
            return;
        }
        if (rollTicks >= 120) {
            initialize(stack, world.getRandom());
            ModAttributes.GEAR_STATE.create(stack, State.IDENTIFIED);
            stack.getOrCreateTag().remove("RollTicks");
            stack.getOrCreateTag().remove("LastModelHit");
            world.playSound((PlayerEntity) null, player.blockPosition(), ModSounds.CONFETTI_SFX, SoundCategory.PLAYERS, 0.5f, 1.0f);
            return;
        }
        if ((int) displacement != lastModelHit) {
            final String roll = ModAttributes.GEAR_ROLL_TYPE.getOrCreate(stack, this.getDefaultRoll(player).getName()).getValue(stack);
            final Rarity rarity = ModConfigs.VAULT_GEAR.getRoll(roll).orElse(this.getDefaultRoll(player)).getRandom(world.getRandom());
            ModAttributes.GEAR_RARITY.create(stack, rarity);
            ModAttributes.GEAR_MODEL.create(stack, world.random.nextInt(this.getModelsFor(rarity)));
            ModAttributes.GEAR_COLOR.create(stack, (item instanceof VaultArmorItem) ? -1 : randomBaseColor(world.random));
            if (item == ModItems.ETCHING) {
                final Set set = Set.values()[world.random.nextInt(Set.values().length)];
                ModAttributes.GEAR_SET.create(stack, set);
            }
            stack.getOrCreateTag().putInt("LastModelHit", (int) displacement);
            world.playSound((PlayerEntity) null, player.blockPosition(), ModSounds.RAFFLE_SFX, SoundCategory.PLAYERS, 0.4f, 1.0f);
        }
        stack.getOrCreateTag().putInt("RollTicks", rollTicks + 1);
    }

    default VaultGearConfig.General.Roll getDefaultRoll(final ServerPlayerEntity player) {
        final TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        final TalentNode<?> artisanNode = talents.getNodeOf((TalentGroup<?>) ModConfigs.TALENTS.ARTISAN);
        VaultGearConfig.General.Roll defaultRoll = ModConfigs.VAULT_GEAR.getDefaultRoll();
        if (artisanNode.isLearned() && artisanNode.getTalent() instanceof ArtisanTalent) {
            defaultRoll = ModConfigs.VAULT_GEAR.getRoll(((ArtisanTalent) artisanNode.getTalent()).getDefaultRoll()).orElse(defaultRoll);
        }
        return defaultRoll;
    }

    default int getDefaultGearTier(final ServerPlayerEntity sPlayer) {
        final int vaultLevel = PlayerVaultStatsData.get(sPlayer.getLevel()).getVaultStats((PlayerEntity) sPlayer).getVaultLevel();
        return ModConfigs.VAULT_GEAR_CRAFTING_SCALING.getRandomTier(vaultLevel);
    }

    static double getDisplacement(final int tick) {
        final double c = 7200.0;
        return (-tick * tick * tick / 6.0 + c * tick) * 50.0 / (-288000.0 + c * 120.0);
    }

    @OnlyIn(Dist.CLIENT)
    default void addInformation(final T item, final ItemStack stack, final List<ITextComponent> tooltip, final boolean displayDetails) {
        ModAttributes.GEAR_CRAFTED_BY.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(crafter -> {
            if (!crafter.isEmpty()) {
                tooltip.add(new StringTextComponent("Crafted by: ").append((ITextComponent) new StringTextComponent(crafter).setStyle(Style.EMPTY.withColor(Color.fromRgb(16770048)))));
            }
            return;
        });
        ModAttributes.GEAR_STATE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(state -> {
            if (state == State.IDENTIFIED) {
                return;
            } else {
                ModAttributes.GEAR_ROLL_TYPE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(rollName -> {
                    final Optional<VaultGearConfig.General.Roll> roll = ModConfigs.VAULT_GEAR.getRoll(rollName);
                    if (!(!roll.isPresent())) {
                        tooltip.add(new StringTextComponent("Roll: ").append((ITextComponent) new StringTextComponent(roll.get().getName()).setStyle(Style.EMPTY.withColor(Color.fromRgb(roll.get().getColor())))));
                    }
                });
                return;
            }
        });
        ModAttributes.GEAR_STATE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(state -> ModAttributes.GEAR_TIER.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(tierId -> {
            if (ModConfigs.VAULT_GEAR != null) {
                final ITextComponent displayTxt = ModConfigs.VAULT_GEAR.getTierConfig(tierId).getDisplay();
                if (!displayTxt.getString().isEmpty()) {
                    tooltip.add(new StringTextComponent("Tier: ").append(displayTxt));
                }
            }
        }));
        ModAttributes.IDOL_TYPE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            if (item instanceof IdolItem) {
                tooltip.add(((IdolItem) item).getType().getIdolDescription());
            }
            return;
        });
        ModAttributes.GEAR_RARITY.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(rarity -> {
            final IFormattableTextComponent rarityText = new StringTextComponent("Rarity: ").append(rarity.getName());
            if (displayDetails) {
                ModAttributes.GEAR_MODEL.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(model -> {
                    rarityText.append((ITextComponent) new StringTextComponent(" | ").withStyle(TextFormatting.BLACK));

                    final StringTextComponent stringTextComponent = new StringTextComponent("" + model);
                    rarityText.append((ITextComponent) stringTextComponent.withStyle(TextFormatting.GRAY));
                    return;
                });
            }
            tooltip.add(rarityText);
            return;
        });
        Rarity rarity = null;
        ModModels.SpecialGearModel model = null;
        if (item instanceof VaultArmorItem) {
            final EquipmentSlotType equipmentSlot = ((VaultArmorItem) item).getEquipmentSlot(stack);
            rarity = ModAttributes.GEAR_RARITY.getOrDefault(stack, Rarity.SCRAPPY).getValue(stack);
            final Integer gearModel = ModAttributes.GEAR_MODEL.getOrDefault(stack, -1).getValue(stack);
            final Integer gearSpecialModel = ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(stack, -1).getValue(stack);
            if (gearSpecialModel != -1 && gearSpecialModel == ModModels.SpecialGearModel.FAIRY_SET.modelForSlot(equipmentSlot).getId()) {
                tooltip.add((ITextComponent) new StringTextComponent(""));
                tooltip.add((ITextComponent) new StringTextComponent("Required in \"Grasshopper Ninja\" advancement").withStyle(TextFormatting.GREEN));
            }
            if (rarity == Rarity.UNIQUE && gearSpecialModel != -1 && item.getIntendedSlot() != null) {
                model = ModModels.SpecialGearModel.getModel(item.getIntendedSlot(), gearSpecialModel);
                if (model != null) {
                    tooltip.add((ITextComponent) new StringTextComponent(model.getDisplayName() + " Armor Set"));
                }
            }
        }
        ModAttributes.GEAR_SET.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            if (value == Set.NONE) {
                return;
            } else {
                tooltip.add(StringTextComponent.EMPTY);
                tooltip.add(new StringTextComponent("Etching: ").append((ITextComponent) new StringTextComponent(value.name()).withStyle(TextFormatting.RED)));
                return;
            }
        });
        ModAttributes.MAX_REPAIRS.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            if (value <= 0) {
                return;
            } else {
                final int current = ModAttributes.CURRENT_REPAIRS.getOrDefault(stack, 0).getValue(stack);
                final int unfilled = value - current;
                tooltip.add(new StringTextComponent("Repairs: ").append(tooltipDots(current, TextFormatting.YELLOW)).append(tooltipDots(unfilled, TextFormatting.GRAY)));
                return;
            }
        });
        ModAttributes.GEAR_MAX_LEVEL.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            if (value <= 0) {
                return;
            } else {
                final int current2 = ModAttributes.GEAR_LEVEL.getOrDefault(stack, 0.0f).getValue(stack).intValue();
                final int unfilled2 = value - current2;
                tooltip.add(new StringTextComponent("Levels: ").append(tooltipDots(current2, TextFormatting.YELLOW)).append(tooltipDots(unfilled2, TextFormatting.GRAY)));
                return;
            }
        });
        if (ModAttributes.GEAR_STATE.getOrDefault(stack, State.UNIDENTIFIED).getValue(stack) == State.IDENTIFIED) {
            this.addModifierInformation(stack, tooltip, displayDetails);
        }
        ModAttributes.REFORGED.get(stack).map(attribute -> attribute.getValue(stack)).filter(b -> b).ifPresent(value -> {
            tooltip.add(new StringTextComponent("Reforged").setStyle(Style.EMPTY.withColor(Color.fromRgb(14833698))));
            if (displayDetails) {
                tooltip.add(new StringTextComponent(" Has been reforged with Artisan Scroll").withStyle(TextFormatting.DARK_GRAY));
            }
            return;
        });
        ModAttributes.IMBUED.get(stack).map(attribute -> attribute.getValue(stack)).filter(b -> b).ifPresent(value -> {
            tooltip.add(new StringTextComponent("Imbued").setStyle(Style.EMPTY.withColor(Color.fromRgb(16772263))));
            if (displayDetails) {
                tooltip.add(new StringTextComponent(" Has been imbued with a Flawed Ruby").withStyle(TextFormatting.DARK_GRAY));
            }
            return;
        });
        ModAttributes.IDOL_AUGMENTED.get(stack).map(attribute -> attribute.getValue(stack)).filter(b -> b).ifPresent(value -> {
            tooltip.add(new StringTextComponent("Hallowed").withStyle(style -> style.withColor(Color.fromRgb(16746496))));
            if (displayDetails) {
                tooltip.add(new StringTextComponent(" Adds +3000 Durability").withStyle(TextFormatting.DARK_GRAY));
            }
            return;
        });
        final Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if (enchantments.size() > 0) {
            tooltip.add((ITextComponent) new StringTextComponent(""));
        }
        ModAttributes.MIN_VAULT_LEVEL.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent2 = new StringTextComponent("Requires level: ");

            final StringTextComponent stringTextComponent3 = new StringTextComponent(value + "");
            tooltip.add(stringTextComponent2.append((ITextComponent) stringTextComponent3.setStyle(Style.EMPTY.withColor(Color.fromRgb(16770048)))));
            return;
        });
        if (FlawedRubyItem.shouldHandleOutcome(stack)) {
            tooltip.add((ITextComponent) new StringTextComponent("Flawed Ruby Applied").setStyle(Style.EMPTY.withColor(Color.fromRgb(16772263))));
            if (displayDetails) {
                tooltip.add((ITextComponent) new StringTextComponent(" A Flawed Ruby has been applied").withStyle(TextFormatting.DARK_GRAY));
                tooltip.add((ITextComponent) new StringTextComponent(" and is unstable. This may break").withStyle(TextFormatting.DARK_GRAY));
                tooltip.add((ITextComponent) new StringTextComponent(" or become imbued and gain an").withStyle(TextFormatting.DARK_GRAY));
                tooltip.add((ITextComponent) new StringTextComponent(" additional modifier slot.").withStyle(TextFormatting.DARK_GRAY));
                tooltip.add((ITextComponent) new StringTextComponent(" Also a small chance nothing will happen.").withStyle(TextFormatting.DARK_GRAY));
            }
        }
    }

    default void addModifierInformation(final ItemStack stack, final List<ITextComponent> tooltip, final boolean displayDetails) {
        ModAttributes.ADD_ARMOR.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent = new StringTextComponent("+" + format(value, 5) + " Armor");
            tooltip.add(stringTextComponent.setStyle(Style.EMPTY.withColor(Color.fromRgb(4766456))));
            return;
        });
        ModAttributes.ADD_ARMOR_2.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent2 = new StringTextComponent("+" + format(value, 5) + " Armor");
            tooltip.add(stringTextComponent2.setStyle(Style.EMPTY.withColor(Color.fromRgb(4766456))));
            return;
        });
        ModAttributes.ADD_ARMOR_TOUGHNESS.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent3 = new StringTextComponent("+" + format(value, 5) + " Armor Toughness");
            tooltip.add(stringTextComponent3.setStyle(Style.EMPTY.withColor(Color.fromRgb(13302672))));
            return;
        });
        ModAttributes.ADD_ARMOR_TOUGHNESS_2.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent4 = new StringTextComponent("+" + format(value, 5) + " Armor Toughness");
            tooltip.add(stringTextComponent4.setStyle(Style.EMPTY.withColor(Color.fromRgb(13302672))));
            return;
        });
        ModAttributes.THORNS_CHANCE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent5 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Thorns Chance");
            tooltip.add(stringTextComponent5.setStyle(Style.EMPTY.withColor(Color.fromRgb(7195648))));
            return;
        });
        ModAttributes.THORNS_DAMAGE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent6 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Thorns Damage");
            tooltip.add(stringTextComponent6.setStyle(Style.EMPTY.withColor(Color.fromRgb(3646976))));
            return;
        });
        ModAttributes.ADD_KNOCKBACK_RESISTANCE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent7 = new StringTextComponent("+" + formatPercent(value * 100.0) + "% Knockback Resistance");
            tooltip.add(stringTextComponent7.setStyle(Style.EMPTY.withColor(Color.fromRgb(16756751))));
            return;
        });
        ModAttributes.ADD_KNOCKBACK_RESISTANCE_2.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent8 = new StringTextComponent("+" + formatPercent(value * 100.0) + "% Knockback Resistance");
            tooltip.add(stringTextComponent8.setStyle(Style.EMPTY.withColor(Color.fromRgb(16756751))));
            return;
        });
        ModAttributes.ADD_ATTACK_DAMAGE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent9 = new StringTextComponent("+" + format(value, 5) + " Attack Damage");
            tooltip.add(stringTextComponent9.setStyle(Style.EMPTY.withColor(Color.fromRgb(13116966))));
            return;
        });
        ModAttributes.ADD_ATTACK_DAMAGE_2.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent10 = new StringTextComponent("+" + format(value, 5) + " Attack Damage");
            tooltip.add(stringTextComponent10.setStyle(Style.EMPTY.withColor(Color.fromRgb(13116966))));
            return;
        });
        ModAttributes.ADD_ATTACK_SPEED.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent11 = new StringTextComponent("+" + format(value, 5) + " Attack Speed");
            tooltip.add(stringTextComponent11.setStyle(Style.EMPTY.withColor(Color.fromRgb(16767592))));
            return;
        });
        ModAttributes.ADD_ATTACK_SPEED_2.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent12 = new StringTextComponent("+" + format(value, 5) + " Attack Speed");
            tooltip.add(stringTextComponent12.setStyle(Style.EMPTY.withColor(Color.fromRgb(16767592))));
            return;
        });
        ModAttributes.DAMAGE_INCREASE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent13 = new StringTextComponent("+" + format(value * 100.0f, 5) + "% increased Damage");
            tooltip.add(stringTextComponent13.setStyle(Style.EMPTY.withColor(Color.fromRgb(16739072))));
            return;
        });
        ModAttributes.DAMAGE_INCREASE_2.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent14 = new StringTextComponent("+" + format(value * 100.0f, 5) + "% increased Damage");
            tooltip.add(stringTextComponent14.setStyle(Style.EMPTY.withColor(Color.fromRgb(16739072))));
            return;
        });
        ModAttributes.ON_HIT_CHAIN.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent15 = new StringTextComponent("+" + value + " Chaining Attacks");
            tooltip.add(stringTextComponent15.setStyle(Style.EMPTY.withColor(Color.fromRgb(6119096))));
            return;
        });
        ModAttributes.ON_HIT_AOE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent16 = new StringTextComponent("+" + value + " Attack AoE");
            tooltip.add(stringTextComponent16.setStyle(Style.EMPTY.withColor(Color.fromRgb(12085504))));
            return;
        });
        ModAttributes.ON_HIT_STUN.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent17 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Stun Attack Chance");
            tooltip.add(stringTextComponent17.setStyle(Style.EMPTY.withColor(Color.fromRgb(1681124))));
            return;
        });
        ModAttributes.DAMAGE_ILLAGERS.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent18 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Spiteful");
            tooltip.add(stringTextComponent18.setStyle(Style.EMPTY.withColor(Color.fromRgb(40882))));
            return;
        });
        ModAttributes.DAMAGE_SPIDERS.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent19 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Baneful");
            tooltip.add(stringTextComponent19.setStyle(Style.EMPTY.withColor(Color.fromRgb(8281694))));
            return;
        });
        ModAttributes.DAMAGE_UNDEAD.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent20 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Holy");
            tooltip.add(stringTextComponent20.setStyle(Style.EMPTY.withColor(Color.fromRgb(16382128))));
            return;
        });
        ModAttributes.ADD_DURABILITY.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent21 = new StringTextComponent("+" + value + " Durability");
            tooltip.add(stringTextComponent21.setStyle(Style.EMPTY.withColor(Color.fromRgb(14668030))));
            return;
        });
        ModAttributes.ADD_DURABILITY_2.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent22 = new StringTextComponent("+" + value + " Durability");
            tooltip.add(stringTextComponent22.setStyle(Style.EMPTY.withColor(Color.fromRgb(14668030))));
            return;
        });
        ModAttributes.ADD_PLATING.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent23 = new StringTextComponent("+" + value + " Plating");
            tooltip.add(stringTextComponent23.setStyle(Style.EMPTY.withColor(Color.fromRgb(14668030))));
            return;
        });
        ModAttributes.ADD_REACH.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent24 = new StringTextComponent("+" + value + " Reach");
            tooltip.add(stringTextComponent24.setStyle(Style.EMPTY.withColor(Color.fromRgb(8706047))));
            return;
        });
        ModAttributes.ADD_REACH_2.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent25 = new StringTextComponent("+" + value + " Reach");
            tooltip.add(stringTextComponent25.setStyle(Style.EMPTY.withColor(Color.fromRgb(8706047))));
            return;
        });
        ModAttributes.ADD_FEATHER_FEET.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent26 = new StringTextComponent("+" + format(value * 100.0f, 5) + "% Feather Feet");
            tooltip.add(stringTextComponent26.setStyle(Style.EMPTY.withColor(Color.fromRgb(13499899))));
            return;
        });
        ModAttributes.ADD_MIN_VAULT_LEVEL.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent27 = new StringTextComponent(((value < 0) ? "-" : "+") + Math.abs(value) + " Min Vault Level");
            tooltip.add(stringTextComponent27.setStyle(Style.EMPTY.withColor(Color.fromRgb(15523772))));
            return;
        });
        ModAttributes.ADD_COOLDOWN_REDUCTION.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent28 = new StringTextComponent(((value < 0.0f) ? "-" : "+") + format(Math.abs(value) * 100.0f, 5) + "% Cooldown Reduction");
            tooltip.add(stringTextComponent28.setStyle(Style.EMPTY.withColor(Color.fromRgb(63668))));
            return;
        });
        ModAttributes.ADD_COOLDOWN_REDUCTION_2.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {

            final StringTextComponent stringTextComponent29 = new StringTextComponent(((value < 0.0f) ? "-" : "+") + format(Math.abs(value) * 100.0f, 5) + "% Cooldown Reduction");
            tooltip.add(stringTextComponent29.setStyle(Style.EMPTY.withColor(Color.fromRgb(63668))));
            return;
        });
        ModAttributes.EXTRA_LEECH_RATIO.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent30 = new StringTextComponent("+" + format(value * 100.0f, 5) + "% Leech");

            tooltip.add(stringTextComponent30.setStyle(Style.EMPTY.withColor(Color.fromRgb(16716820))));
            return;
        });
        ModAttributes.ADD_EXTRA_LEECH_RATIO.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent31 = new StringTextComponent("+" + format(value * 100.0f, 5) + "% Leech");

            tooltip.add(stringTextComponent31.setStyle(Style.EMPTY.withColor(Color.fromRgb(16716820))));
            return;
        });
        ModAttributes.FATAL_STRIKE_CHANCE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent32 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Fatal Strike Chance");

            tooltip.add(stringTextComponent32.setStyle(Style.EMPTY.withColor(Color.fromRgb(16523264))));
            return;
        });
        ModAttributes.FATAL_STRIKE_DAMAGE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent33 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Fatal Strike Damage");

            tooltip.add(stringTextComponent33.setStyle(Style.EMPTY.withColor(Color.fromRgb(12520704))));
            return;
        });
        ModAttributes.EXTRA_HEALTH.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent34 = new StringTextComponent("+" + format(value, 5) + " Health");

            tooltip.add(stringTextComponent34.setStyle(Style.EMPTY.withColor(Color.fromRgb(2293541))));
            return;
        });
        ModAttributes.ADD_EXTRA_HEALTH.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent35 = new StringTextComponent("+" + format(value, 5) + " Health");

            tooltip.add(stringTextComponent35.setStyle(Style.EMPTY.withColor(Color.fromRgb(2293541))));
            return;
        });
        ModAttributes.EXTRA_PARRY_CHANCE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent36 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Parry");

            tooltip.add(stringTextComponent36.setStyle(Style.EMPTY.withColor(Color.fromRgb(11534098))));
            return;
        });
        ModAttributes.ADD_EXTRA_PARRY_CHANCE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent37 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Parry");

            tooltip.add(stringTextComponent37.setStyle(Style.EMPTY.withColor(Color.fromRgb(11534098))));
            return;
        });
        ModAttributes.EXTRA_RESISTANCE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent38 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Resistance");

            tooltip.add(stringTextComponent38.setStyle(Style.EMPTY.withColor(Color.fromRgb(16702720))));
            return;
        });
        ModAttributes.ADD_EXTRA_RESISTANCE.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent39 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Resistance");

            tooltip.add(stringTextComponent39.setStyle(Style.EMPTY.withColor(Color.fromRgb(16702720))));
            return;
        });
        ModAttributes.CHEST_RARITY.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            final StringTextComponent stringTextComponent40 = new StringTextComponent("+" + formatPercent(value * 100.0f) + "% Chest Rarity");

            tooltip.add(stringTextComponent40.setStyle(Style.EMPTY.withColor(Color.fromRgb(11073085))));
            return;
        });
        ModAttributes.EFFECT_IMMUNITY.get(stack).map(attribute -> (attribute).getValue(stack)).ifPresent(value -> value.forEach(effect -> tooltip.add(new StringTextComponent("+").append((ITextComponent) new TranslationTextComponent(effect.toEffect().getDescriptionId())).append((ITextComponent) new StringTextComponent(" Immunity")).setStyle(Style.EMPTY.withColor(Color.fromRgb(10801083))))));
        ModAttributes.EFFECT_CLOUD.get(stack).map(attribute -> (attribute).getValue(stack)).ifPresent(value -> value.forEach(effect -> {

            final StringTextComponent stringTextComponent41 = new StringTextComponent("+" + effect.getName() + " Cloud");
            tooltip.add(stringTextComponent41.setStyle(Style.EMPTY.withColor(Color.fromRgb(15007916))));
        }));
        ModAttributes.EXTRA_EFFECTS.get(stack).map(attribute -> (attribute).getValue(stack)).ifPresent(value -> value.forEach(effect -> {

            final StringTextComponent stringTextComponent42 = new StringTextComponent("+" + effect.getAmplifier() + " ");
            tooltip.add(stringTextComponent42.append((ITextComponent) new TranslationTextComponent(effect.getEffect().getDescriptionId())).setStyle(Style.EMPTY.withColor(Color.fromRgb(14111487))));
        }));
        ModAttributes.SOULBOUND.get(stack).map(attribute -> attribute.getValue(stack)).filter(b -> b).ifPresent(value -> {
            tooltip.add(new StringTextComponent("Soulbound").setStyle(Style.EMPTY.withColor(Color.fromRgb(9856253))));
            if (displayDetails) {
                tooltip.add(new StringTextComponent(" Keep item on death in vault").withStyle(TextFormatting.DARK_GRAY));
            }
        });
    }

    default String formatPercent(final double value) {
        return VaultGear.PERCENT_FORMAT.format(value);
    }

    default String format(final double value, final int scale) {
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    default boolean canElytraFly(final T item, final ItemStack stack, final LivingEntity entity) {
        return entity instanceof PlayerEntity && PlayerSet.isActive(Set.DRAGON, entity);
    }

    default boolean elytraFlightTick(final T item, final ItemStack stack, final LivingEntity entity, final int flightTicks) {
        return this.canElytraFly(item, stack, entity);
    }

    default int getColor(final T item, final ItemStack stack) {
        final EnumAttribute<State> stateAttribute = ModAttributes.GEAR_STATE.get(stack).orElse(null);
        if (stateAttribute == null || stateAttribute.getValue(stack) == State.UNIDENTIFIED) {
            return -1;
        }
        final Rarity rarity = ModAttributes.GEAR_RARITY.getOrDefault(stack, Rarity.SCRAPPY).getValue(stack);
        final Integer dyeColor = getDyeColor(stack);
        if (rarity == Rarity.SCRAPPY && dyeColor == null) {
            return -1;
        }
        final IntegerAttribute colorAttribute = ModAttributes.GEAR_COLOR.get(stack).orElse(null);
        final int baseColor = (colorAttribute == null) ? -1 : colorAttribute.getValue(stack);
        return (dyeColor != null) ? dyeColor : baseColor;
    }

    @OnlyIn(Dist.CLIENT)
    default <A extends BipedModel<?>> A getArmorModel(final T item, final LivingEntity entityLiving, final ItemStack itemStack, final EquipmentSlotType armorSlot, final A _default) {
        final Integer specialModelId = ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(itemStack, -1).getValue(itemStack);
        if (specialModelId != -1) {
            final ModModels.SpecialGearModel specialGearModel = ModModels.SpecialGearModel.getRegistryForSlot(armorSlot).get(specialModelId);
            if (specialGearModel != null) {
                return (A) specialGearModel.getModel();
            }
        }
        final Integer modelId = ModAttributes.GEAR_MODEL.getOrDefault(itemStack, -1).getValue(itemStack);
        final Rarity rarity = ModAttributes.GEAR_RARITY.getOrDefault(itemStack, Rarity.SCRAPPY).getValue(itemStack);
        final ModModels.GearModel gearModel = (rarity == Rarity.SCRAPPY) ? ModModels.GearModel.SCRAPPY_REGISTRY.get(modelId) : ModModels.GearModel.REGISTRY.get(modelId);
        if (gearModel == null) {
            return null;
        }
        return (A) gearModel.forSlotType(armorSlot);
    }

    @OnlyIn(Dist.CLIENT)
    default String getArmorTexture(final T item, final ItemStack itemStack, final Entity entity, final EquipmentSlotType slot, final String type) {
        final Integer specialModelId = ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(itemStack, -1).getValue(itemStack);
        if (specialModelId != -1) {
            final ModModels.SpecialGearModel specialGearModel = ModModels.SpecialGearModel.getRegistryForSlot(slot).get(specialModelId);
            if (specialGearModel != null) {
                return specialGearModel.getTextureName(slot, type);
            }
        }
        final Integer modelId = ModAttributes.GEAR_MODEL.getOrDefault(itemStack, -1).getValue(itemStack);
        final Rarity rarity = ModAttributes.GEAR_RARITY.getOrDefault(itemStack, Rarity.SCRAPPY).getValue(itemStack);
        final ModModels.GearModel gearModel = (rarity == Rarity.SCRAPPY) ? ModModels.GearModel.SCRAPPY_REGISTRY.get(modelId) : ModModels.GearModel.REGISTRY.get(modelId);
        if (gearModel == null) {
            return null;
        }
        return gearModel.getTextureName(slot, type);
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(final T item, final EquipmentSlotType slot, final ItemStack stack, final Multimap<Attribute, AttributeModifier> parent) {
        if (!item.isIntendedForSlot(slot)) {
            return parent;
        }
        final ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        final Optional<DoubleAttribute> attackDamage = ModAttributes.ATTACK_DAMAGE.get(stack);
        final Optional<DoubleAttribute> attackSpeed = ModAttributes.ATTACK_SPEED.get(stack);
        final Optional<DoubleAttribute> armor = ModAttributes.ARMOR.get(stack);
        final Optional<DoubleAttribute> armorToughness = ModAttributes.ARMOR_TOUGHNESS.get(stack);
        final Optional<DoubleAttribute> knockbackResistance = ModAttributes.KNOCKBACK_RESISTANCE.get(stack);
        final Optional<FloatAttribute> extraHealth = ModAttributes.EXTRA_HEALTH.get(stack);
        final Optional<FloatAttribute> extraHealth2 = ModAttributes.ADD_EXTRA_HEALTH.get(stack);
        final Optional<DoubleAttribute> reach = ModAttributes.REACH.get(stack);
        parent.forEach((attribute, modifier) -> {
            if (attribute == Attributes.ATTACK_DAMAGE && attackDamage.isPresent()) {
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(modifier.getId(), "Weapon modifier", (double) attackDamage.get().getValue(stack), AttributeModifier.Operation.ADDITION));
            } else if (attribute == Attributes.ATTACK_SPEED && attackSpeed.isPresent()) {
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(modifier.getId(), "Weapon modifier", (double) attackSpeed.get().getValue(stack), AttributeModifier.Operation.ADDITION));
            } else if (attribute == Attributes.ARMOR && armor.isPresent()) {
                builder.put(Attributes.ARMOR, new AttributeModifier(VaultGear.ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", (double) armor.get().getValue(stack), AttributeModifier.Operation.ADDITION));
            } else if (attribute == Attributes.ARMOR_TOUGHNESS && armorToughness.isPresent()) {
                builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(VaultGear.ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", (double) armorToughness.get().getValue(stack), AttributeModifier.Operation.ADDITION));
            } else if (attribute == Attributes.KNOCKBACK_RESISTANCE && knockbackResistance.isPresent()) {
                builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(VaultGear.ARMOR_MODIFIERS[slot.getIndex()], "Armor knockback resistance", (double) knockbackResistance.get().getValue(stack), AttributeModifier.Operation.ADDITION));
            } else {
                builder.put(attribute, modifier);
            }
            return;
        });
        final float health = extraHealth.map(attribute -> attribute.getValue(stack)).orElse(0.0f) + extraHealth2.map(attribute -> attribute.getValue(stack)).orElse(0.0f);
        if (health != 0.0f) {
            builder.put(Attributes.MAX_HEALTH, new AttributeModifier(itemHash(item, 0L), "Extra Health", (double) health, AttributeModifier.Operation.ADDITION));
        }
        reach.ifPresent(attribute -> builder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(itemHash(item, 1L), "Reach", (double) attribute.getValue(stack), AttributeModifier.Operation.ADDITION)));
        return (Multimap<Attribute, AttributeModifier>) builder.build();
    }

    static int getCooldownReduction(final ServerPlayerEntity player, final AbilityGroup<?, ?> abilityGroup, int cooldown) {
        final float totalCooldown = MathHelper.clamp(CooldownHelper.getCooldownMultiplier(player, abilityGroup), 0.0f, 1.0f);
        if (PlayerSet.isActive(Set.RIFT, (LivingEntity) player)) {
            cooldown /= 2;
        }
        return Math.round(cooldown * (1.0f - totalCooldown));
    }

    default UUID itemHash(final Item item, final long salt) {
        return new UUID(salt, item.hashCode());
    }

    static void addLevel(final ItemStack stack, final float amount) {
        if (!(stack.getItem() instanceof VaultGear)) {
            return;
        }
        final int maxLevel = ModAttributes.GEAR_MAX_LEVEL.getOrDefault(stack, 0).getValue(stack);
        final float current = ModAttributes.GEAR_LEVEL.getOrDefault(stack, 0.0f).getValue(stack);
        if ((int) current >= maxLevel) {
            return;
        }
        final float newLevel = current + amount;
        final int difference = (int) newLevel - (int) current;
        ModAttributes.GEAR_LEVEL.create(stack, newLevel);
        final int toRoll = ModAttributes.GEAR_MODIFIERS_TO_ROLL.getOrDefault(stack, 0).getValue(stack) + difference;
        if (toRoll != 0) {
            ModAttributes.GEAR_MODIFIERS_TO_ROLL.create(stack, toRoll);
        }
    }

    static void decrementLevel(final ItemStack stack, final int removed) {
        final float currentLevel = ModAttributes.GEAR_LEVEL.getOrDefault(stack, 0.0f).getValue(stack);
        ModAttributes.GEAR_LEVEL.create(stack, Math.max(currentLevel - removed, 0.0f));
    }

    static void incrementRepairs(final ItemStack stack) {
        final int curRepairs = ModAttributes.CURRENT_REPAIRS.getOrDefault(stack, 0).getValue(stack);
        ModAttributes.CURRENT_REPAIRS.create(stack, curRepairs + 1);
    }

    default void initialize(final ItemStack stack, final Random random) {
        ModAttributes.GEAR_RARITY.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(rarity -> {
            final int tier = ModAttributes.GEAR_TIER.getOrDefault(stack, 0).getValue(stack);
            final VaultGearConfig.Tier tierConfig = VaultGearConfig.get(rarity).TIERS.get(tier);
            tierConfig.getAttributes(stack).ifPresent(modifiers -> modifiers.initialize(stack, random));
        });
    }

    default void update(final ItemStack stack, final Random random) {
        ModAttributes.GEAR_RARITY.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(rarity -> {
            final int tier = ModAttributes.GEAR_TIER.getOrDefault(stack, 0).getValue(stack);
            final VaultGearConfig.Tier tierConfig = VaultGearConfig.get(rarity).TIERS.get(tier);
            tierConfig.getModifiers(stack).ifPresent(modifiers -> {
                if (ModAttributes.GEAR_STATE.getOrCreate(stack, State.UNIDENTIFIED).getValue(stack) == State.IDENTIFIED) {
                    ModAttributes.GUARANTEED_MODIFIER.getBase(stack).ifPresent(modifierKey -> {
                        final VAttribute<?, ?> modifier = ModAttributes.REGISTRY.get(new ResourceLocation(modifierKey));
                        if (modifier != null) {
                            VaultGearHelper.applyGearModifier(stack, tierConfig, modifier);
                        }
                        VaultGearHelper.removeAttribute(stack, ModAttributes.GUARANTEED_MODIFIER);
                        return;
                    });
                }
                modifiers.initialize(stack, random);
            });
        });
    }

    default int randomBaseColor(final Random rand) {
        return VaultGear.BASE_COLORS[rand.nextInt(VaultGear.BASE_COLORS.length)].getColorValue();
    }

    default Integer getDyeColor(final ItemStack stack) {
        final CompoundNBT compoundnbt = stack.getTagElement("display");
        if (compoundnbt != null && compoundnbt.contains("color", 3)) {
            return compoundnbt.getInt("color");
        }
        return null;
    }

    default ITextComponent tooltipDots(final int amount, final TextFormatting formatting) {
        final StringBuilder text = new StringBuilder();
        for (int i = 0; i < amount; ++i) {
            text.append("\u2b22 ");
        }
        return (ITextComponent) new StringTextComponent(text.toString()).withStyle(formatting);
    }

    public enum Type {
        SWORD, AXE, ARMOR;
    }

    public enum State {
        UNIDENTIFIED, ROLLING, IDENTIFIED;
    }

    public static class Material implements IArmorMaterial {
        public static final Material INSTANCE;

        private Material() {
        }

        public int getDurabilityForSlot(final EquipmentSlotType slot) {
            return 0;
        }

        public int getDefenseForSlot(final EquipmentSlotType slot) {
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
            return 0.0f;
        }

        public float getKnockbackResistance() {
            return 1.0E-4f;
        }

        static {
            INSTANCE = new Material();
        }
    }

    public static class Tier implements IItemTier {
        public static final Tier INSTANCE;

        private Tier() {
        }

        public int getUses() {
            return 0;
        }

        public float getSpeed() {
            return 0.0f;
        }

        public float getAttackDamageBonus() {
            return 0.0f;
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

        static {
            INSTANCE = new Tier();
        }
    }

    public enum Set {
        NONE, DRAGON, ZOD, NINJA, GOLEM, RIFT, CARAPACE, DIVINITY, DRYAD, BLOOD, VAMPIRE, TREASURE, ASSASSIN, PHOENIX, DREAM, PORCUPINE;
    }

    public enum Rarity {
        COMMON(TextFormatting.AQUA), RARE(TextFormatting.YELLOW), EPIC(TextFormatting.LIGHT_PURPLE), OMEGA(TextFormatting.GREEN), UNIQUE(Color.fromRgb(-1213660)), SCRAPPY(TextFormatting.GRAY);

        private final Color color;

        private Rarity(final TextFormatting color) {
            this(Color.fromLegacyFormat(color));
        }

        private Rarity(final Color color) {
            this.color = color;
        }

        public Color getColor() {
            return this.color;
        }

        public ITextComponent getName() {
            final Style style = Style.EMPTY.withColor(this.getColor());
            return (ITextComponent) new StringTextComponent(this.name()).withStyle(style);
        }
    }
}
