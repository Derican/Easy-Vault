package iskallia.vault.item;

import iskallia.vault.util.EntityHelper;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.time.extension.TimeExtension;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemVaultFruit extends Item {
    public static Food VAULT_FRUIT_FOOD = (new Food.Builder())
            .saturationMod(0.0F).nutrition(0)
            .fast().alwaysEat().build();

    protected int extraVaultTicks;

    public ItemVaultFruit(ItemGroup group, ResourceLocation id, int extraVaultTicks) {
        super((new Item.Properties())
                .tab(group)
                .food(VAULT_FRUIT_FOOD)
                .stacksTo(64));

        setRegistryName(id);

        this.extraVaultTicks = extraVaultTicks;
    }

    public boolean addTime(World world, PlayerEntity player) {
        if (world.isClientSide() || !(world instanceof ServerWorld)) {
            return false;
        }
        VaultRaid vault = VaultRaidData.get((ServerWorld) world).getActiveFor(player.getUUID());
        if (vault == null) {
            return false;
        }
        for (VaultObjective objective : vault.getAllObjectives()) {
            if (objective.preventsEatingExtensionFruit(world.getServer(), vault)) {
                return false;
            }
        }
        if (!vault.getActiveModifiersFor(PlayerFilter.of(new PlayerEntity[]{player}, ), VaultFruitPreventionModifier.class).isEmpty()) {
            return false;
        }
        vault.getPlayers().forEach(vPlayer -> vPlayer.getTimer().addTime((TimeExtension) new FruitExtension(this), 0));
        return true;
    }

    public int getExtraVaultTicks() {
        return this.extraVaultTicks;
    }


    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStack = playerIn.getItemInHand(handIn);
        if (playerIn.level.dimension() != Vault.VAULT_KEY)
            return ActionResult.fail(itemStack);
        return super.use(worldIn, playerIn, handIn);
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(""));
        StringTextComponent comp = new StringTextComponent("[!] Only edible inside a Vault");
        comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)).withItalic(Boolean.valueOf(true)));
        tooltip.add(comp);

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }


    public ITextComponent getName(ItemStack stack) {
        IFormattableTextComponent displayName = (IFormattableTextComponent) super.getName(stack);
        return (ITextComponent) displayName.setStyle(Style.EMPTY.withColor(Color.fromRgb(16563456)));
    }

    public static class BitterLemon extends ItemVaultFruit {
        protected DamageSource damageSource = (new DamageSource("bitter_lemon")).bypassArmor();

        public BitterLemon(ItemGroup group, ResourceLocation id, int extraVaultTicks) {
            super(group, id, extraVaultTicks);
        }


        public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
            if (!worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
                if (addTime(worldIn, (PlayerEntity) player)) {
                    EntityHelper.changeHealth((LivingEntity) player, -6);

                    worldIn.playSound(null, player
                            .getX(), player
                            .getY(), player
                            .getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundCategory.MASTER, 1.0F, 1.0F);

                } else {

                    return stack;
                }
            }

            return super.finishUsingItem(stack, worldIn, entityLiving);
        }


        @OnlyIn(Dist.CLIENT)
        public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
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
        protected DamageSource damageSource = (new DamageSource("sour_orange")).bypassArmor();

        public SourOrange(ItemGroup group, ResourceLocation id, int extraVaultTicks) {
            super(group, id, extraVaultTicks);
        }


        public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
            if (!worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
                if (addTime(worldIn, (PlayerEntity) player)) {
                    EntityHelper.changeHealth((LivingEntity) player, -10);

                    worldIn.playSound(null, player
                            .getX(), player
                            .getY(), player
                            .getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundCategory.MASTER, 1.0F, 1.0F);

                } else {

                    return stack;
                }
            }

            return super.finishUsingItem(stack, worldIn, entityLiving);
        }


        @OnlyIn(Dist.CLIENT)
        public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
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
        protected DamageSource damageSource = (new DamageSource("mystic_pear")).bypassArmor();

        public MysticPear(ItemGroup group, ResourceLocation id, int extraVaultTicks) {
            super(group, id, extraVaultTicks);
        }


        public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
            if (!worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
                if (addTime(worldIn, (PlayerEntity) player)) {
                    EntityHelper.changeHealth((LivingEntity) player, -MathUtilities.getRandomInt(10, 20));

                    if (MathUtilities.randomFloat(0.0F, 100.0F) <= 50.0F) {
                        player.addEffect(new EffectInstance(Effects.POISON, 600));
                    } else {
                        player.addEffect(new EffectInstance(Effects.WITHER, 600));
                    }

                    worldIn.playSound(null, player
                            .getX(), player
                            .getY(), player
                            .getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundCategory.MASTER, 1.0F, 1.0F);

                } else {

                    return stack;
                }
            }
            return super.finishUsingItem(stack, worldIn, entityLiving);
        }


        @OnlyIn(Dist.CLIENT)
        public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
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
        public SweetKiwi(ItemGroup group, ResourceLocation id, int extraVaultTicks) {
            super(group, id, extraVaultTicks);
        }


        public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
            if (!worldIn.isClientSide && entityLiving instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
                if (addTime(worldIn, (PlayerEntity) player)) {
                    worldIn.playSound(null, player
                            .getX(), player
                            .getY(), player
                            .getZ(), SoundEvents.CONDUIT_ACTIVATE, SoundCategory.MASTER, 1.0F, 1.0F);

                } else {

                    return stack;
                }
            }

            return super.finishUsingItem(stack, worldIn, entityLiving);
        }


        @OnlyIn(Dist.CLIENT)
        public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            tooltip.add(new StringTextComponent(""));
            StringTextComponent comp = new StringTextComponent("- Adds 5 seconds to the Vault Timer");
            comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(65280)));
            tooltip.add(comp);
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemVaultFruit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */