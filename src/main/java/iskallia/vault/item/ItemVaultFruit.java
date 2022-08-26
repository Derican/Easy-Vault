package iskallia.vault.item;

import iskallia.vault.Vault;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.modifier.VaultFruitPreventionModifier;
import iskallia.vault.world.vault.time.extension.FruitExtension;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemVaultFruit extends Item {
    public static Food VAULT_FRUIT_FOOD;
    protected int extraVaultTicks;

    public ItemVaultFruit(final ItemGroup group, final ResourceLocation id, final int extraVaultTicks) {
        super(new Item.Properties().tab(group).food(ItemVaultFruit.VAULT_FRUIT_FOOD).stacksTo(64));
        this.setRegistryName(id);
        this.extraVaultTicks = extraVaultTicks;
    }

    public boolean addTime(final World world, final PlayerEntity player) {
        if (world.isClientSide() || !(world instanceof ServerWorld)) {
            return false;
        }
        final VaultRaid vault = VaultRaidData.get((ServerWorld) world).getActiveFor(player.getUUID());
        if (vault == null) {
            return false;
        }
        for (final VaultObjective objective : vault.getAllObjectives()) {
            if (objective.preventsEatingExtensionFruit(world.getServer(), vault)) {
                return false;
            }
        }
        if (!vault.getActiveModifiersFor(PlayerFilter.of(player), VaultFruitPreventionModifier.class).isEmpty()) {
            return false;
        }
        vault.getPlayers().forEach(vPlayer -> vPlayer.getTimer().addTime(new FruitExtension(this), 0));
        return true;
    }

    public int getExtraVaultTicks() {
        return this.extraVaultTicks;
    }

    public ActionResult<ItemStack> use(final World worldIn, final PlayerEntity playerIn, final Hand handIn) {
        final ItemStack itemStack = playerIn.getItemInHand(handIn);
        if (playerIn.level.dimension() != Vault.VAULT_KEY) {
            return ActionResult.fail(itemStack);
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(""));
        final StringTextComponent comp = new StringTextComponent("[!] Only edible inside a Vault");
        comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)).withItalic(Boolean.valueOf(true)));
        tooltip.add(comp);
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public ITextComponent getName(final ItemStack stack) {
        final IFormattableTextComponent displayName = (IFormattableTextComponent) super.getName(stack);
        return displayName.setStyle(Style.EMPTY.withColor(Color.fromRgb(16563456)));
    }

    static {
        ItemVaultFruit.VAULT_FRUIT_FOOD = new Food.Builder().saturationMod(0.0f).nutrition(0).fast().alwaysEat().build();
    }

    public static class BitterLemon extends ItemVaultFruit {
        protected DamageSource damageSource;

        public BitterLemon(final ItemGroup group, final ResourceLocation id, final int extraVaultTicks) {
            super(group, id, extraVaultTicks);
            this.damageSource = new DamageSource("bitter_lemon").bypassArmor();
        }

        public ItemStack finishUsingItem(final ItemStack stack, final World worldIn, final LivingEntity entityLiving) {
            if (!worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                final ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
                if (!this.addTime(worldIn, player)) {
                    return stack;
                }
                EntityHelper.changeHealth(player, -6);
                worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundCategory.MASTER, 1.0f, 1.0f);
            }
            return super.finishUsingItem(stack, worldIn, entityLiving);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
            tooltip.add(new StringTextComponent(""));
            StringTextComponent comp = new StringTextComponent("A magical lemon with a bitter taste");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(12512238)).withItalic(Boolean.valueOf(true)));
            tooltip.add(comp);
            comp = new StringTextComponent("It is grown on the gorgeous trees of Iskallia.");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(12512238)).withItalic(Boolean.valueOf(true)));
            tooltip.add(comp);
            tooltip.add(new StringTextComponent(""));
            comp = new StringTextComponent("- Wipes away 3 hearts");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)));
            tooltip.add(comp);
            comp = new StringTextComponent("- Adds 30 seconds to the Vault Timer");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(65280)));
            tooltip.add(comp);
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
        }
    }

    public static class SourOrange extends ItemVaultFruit {
        protected DamageSource damageSource;

        public SourOrange(final ItemGroup group, final ResourceLocation id, final int extraVaultTicks) {
            super(group, id, extraVaultTicks);
            this.damageSource = new DamageSource("sour_orange").bypassArmor();
        }

        public ItemStack finishUsingItem(final ItemStack stack, final World worldIn, final LivingEntity entityLiving) {
            if (!worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                final ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
                if (!this.addTime(worldIn, player)) {
                    return stack;
                }
                EntityHelper.changeHealth(player, -10);
                worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundCategory.MASTER, 1.0f, 1.0f);
            }
            return super.finishUsingItem(stack, worldIn, entityLiving);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
            tooltip.add(new StringTextComponent(""));
            StringTextComponent comp = new StringTextComponent("A magical orange with a sour taste");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(12512238)).withItalic(Boolean.valueOf(true)));
            tooltip.add(comp);
            comp = new StringTextComponent("It is grown on the gorgeous trees of Iskallia.");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(12512238)).withItalic(Boolean.valueOf(true)));
            tooltip.add(comp);
            tooltip.add(new StringTextComponent(""));
            comp = new StringTextComponent("- Wipes away 5 hearts");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)));
            tooltip.add(comp);
            comp = new StringTextComponent("- Adds 60 seconds to the Vault Timer");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(65280)));
            tooltip.add(comp);
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
        }
    }

    public static class MysticPear extends ItemVaultFruit {
        protected DamageSource damageSource;

        public MysticPear(final ItemGroup group, final ResourceLocation id, final int extraVaultTicks) {
            super(group, id, extraVaultTicks);
            this.damageSource = new DamageSource("mystic_pear").bypassArmor();
        }

        public ItemStack finishUsingItem(final ItemStack stack, final World worldIn, final LivingEntity entityLiving) {
            if (!worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                final ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
                if (!this.addTime(worldIn, player)) {
                    return stack;
                }
                EntityHelper.changeHealth(player, -MathUtilities.getRandomInt(10, 20));
                if (MathUtilities.randomFloat(0.0f, 100.0f) <= 50.0f) {
                    player.addEffect(new EffectInstance(Effects.POISON, 600));
                } else {
                    player.addEffect(new EffectInstance(Effects.WITHER, 600));
                }
                worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundCategory.MASTER, 1.0f, 1.0f);
            }
            return super.finishUsingItem(stack, worldIn, entityLiving);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
            tooltip.add(new StringTextComponent(""));
            StringTextComponent comp = new StringTextComponent("A magical pear with a strange taste");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(12512238)).withItalic(Boolean.valueOf(true)));
            tooltip.add(comp);
            comp = new StringTextComponent("It is grown on the gorgeous trees of Iskallia.");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(12512238)).withItalic(Boolean.valueOf(true)));
            tooltip.add(comp);
            tooltip.add(new StringTextComponent(""));
            comp = new StringTextComponent("- Wipes away 5 to 10 hearts");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)));
            tooltip.add(comp);
            comp = new StringTextComponent("- Inflicts with either Wither or Poison effect");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)));
            tooltip.add(comp);
            comp = new StringTextComponent("- Adds 5 minutes to the Vault Timer");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(65280)));
            tooltip.add(comp);
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
        }
    }

    public static class SweetKiwi extends ItemVaultFruit {
        public SweetKiwi(final ItemGroup group, final ResourceLocation id, final int extraVaultTicks) {
            super(group, id, extraVaultTicks);
        }

        public ItemStack finishUsingItem(final ItemStack stack, final World worldIn, final LivingEntity entityLiving) {
            if (!worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                final ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
                if (!this.addTime(worldIn, player)) {
                    return stack;
                }
                worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundCategory.MASTER, 1.0f, 1.0f);
            }
            return super.finishUsingItem(stack, worldIn, entityLiving);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
            tooltip.add(new StringTextComponent(""));
            final StringTextComponent comp = new StringTextComponent("- Adds 5 seconds to the Vault Timer");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(65280)));
            tooltip.add(comp);
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
        }
    }
}
