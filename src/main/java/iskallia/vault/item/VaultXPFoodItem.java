package iskallia.vault.item;

import iskallia.vault.client.gui.overlay.VaultBarOverlay;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public abstract class VaultXPFoodItem extends Item {
    public static Food FOOD = (new Food.Builder()).saturationMod(0.0F).nutrition(0).fast().alwaysEat().build();

    private final int levelLimit;

    public VaultXPFoodItem(ResourceLocation id, Item.Properties properties) {
        this(id, properties, -1);
    }

    public VaultXPFoodItem(ResourceLocation id, Item.Properties properties, int levelLimit) {
        super(properties.food(FOOD));
        setRegistryName(id);
        this.levelLimit = levelLimit;
    }


    public UseAction getUseAnimation(ItemStack stack) {
        return super.getUseAnimation(stack);
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (this.levelLimit > 0) {
            int vaultLevel;
            if (player instanceof ServerPlayerEntity) {
                vaultLevel = PlayerVaultStatsData.get(((ServerPlayerEntity) player).getLevel()).getVaultStats(player).getVaultLevel();
            } else {
                vaultLevel = getVaultLevel();
            }
            if (vaultLevel >= this.levelLimit) {
                return ActionResult.pass(player.getItemInHand(hand));
            }
        }

        return super.use(world, player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    private int getVaultLevel() {
        return VaultBarOverlay.vaultLevel;
    }


    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entityLiving) {
        if (!world.isClientSide && entityLiving instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
            grantExp(player);
        }
        return super.finishUsingItem(stack, world, entityLiving);
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this.levelLimit > 0) {
            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add((new StringTextComponent("Can't be consumed after Level: ")).withStyle(TextFormatting.GRAY)
                    .append((ITextComponent) (new StringTextComponent(String.valueOf(this.levelLimit))).withStyle(TextFormatting.AQUA)));
        }
    }

    public abstract void grantExp(ServerPlayerEntity paramServerPlayerEntity);

    public static class Percent
            extends VaultXPFoodItem {
        private final Supplier<Float> min;
        private final Supplier<Float> max;

        public Percent(ResourceLocation id, Supplier<Float> min, Supplier<Float> max, Item.Properties properties) {
            this(id, min, max, properties, -1);
        }

        public Percent(ResourceLocation id, Supplier<Float> min, Supplier<Float> max, Item.Properties properties, int levelRequirement) {
            super(id, properties, levelRequirement);
            this.min = min;
            this.max = max;
        }


        public void grantExp(ServerPlayerEntity sPlayer) {
            PlayerVaultStatsData statsData = PlayerVaultStatsData.get(sPlayer.getLevel());
            PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity) sPlayer);
            float randomPercentage = MathUtilities.randomFloat(((Float) this.min.get()).floatValue(), ((Float) this.max.get()).floatValue());
            statsData.addVaultExp(sPlayer, (int) (stats.getTnl() * randomPercentage));
        }
    }

    public static class Flat
            extends VaultXPFoodItem {
        private final Supplier<Integer> min;
        private final Supplier<Integer> max;

        public Flat(ResourceLocation id, Supplier<Integer> min, Supplier<Integer> max, Item.Properties properties) {
            this(id, min, max, properties, -1);
        }

        public Flat(ResourceLocation id, Supplier<Integer> min, Supplier<Integer> max, Item.Properties properties, int levelRequirement) {
            super(id, properties, levelRequirement);
            this.min = min;
            this.max = max;
        }


        public void grantExp(ServerPlayerEntity sPlayer) {
            PlayerVaultStatsData statsData = PlayerVaultStatsData.get(sPlayer.getLevel());
            statsData.addVaultExp(sPlayer, MathUtilities.getRandomInt(((Integer) this.min.get()).intValue(), ((Integer) this.max.get()).intValue()));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\VaultXPFoodItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */