// 
// Decompiled by Procyon v0.6.0
// 

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

public abstract class VaultXPFoodItem extends Item
{
    public static Food FOOD;
    private final int levelLimit;
    
    public VaultXPFoodItem(final ResourceLocation id, final Item.Properties properties) {
        this(id, properties, -1);
    }
    
    public VaultXPFoodItem(final ResourceLocation id, final Item.Properties properties, final int levelLimit) {
        super(properties.food(VaultXPFoodItem.FOOD));
        this.setRegistryName(id);
        this.levelLimit = levelLimit;
    }
    
    public UseAction getUseAnimation(final ItemStack stack) {
        return super.getUseAnimation(stack);
    }
    
    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        if (this.levelLimit > 0) {
            int vaultLevel;
            if (player instanceof ServerPlayerEntity) {
                vaultLevel = PlayerVaultStatsData.get(((ServerPlayerEntity)player).getLevel()).getVaultStats(player).getVaultLevel();
            }
            else {
                vaultLevel = this.getVaultLevel();
            }
            if (vaultLevel >= this.levelLimit) {
                return (ActionResult<ItemStack>)ActionResult.pass(player.getItemInHand(hand));
            }
        }
        return (ActionResult<ItemStack>)super.use(world, player, hand);
    }
    
    @OnlyIn(Dist.CLIENT)
    private int getVaultLevel() {
        return VaultBarOverlay.vaultLevel;
    }
    
    public ItemStack finishUsingItem(final ItemStack stack, final World world, final LivingEntity entityLiving) {
        if (!world.isClientSide && entityLiving instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity)entityLiving;
            this.grantExp(player);
        }
        return super.finishUsingItem(stack, world, entityLiving);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        if (this.levelLimit > 0) {
            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add((ITextComponent)new StringTextComponent("Can't be consumed after Level: ").withStyle(TextFormatting.GRAY).append((ITextComponent)new StringTextComponent(String.valueOf(this.levelLimit)).withStyle(TextFormatting.AQUA)));
        }
    }
    
    public abstract void grantExp(final ServerPlayerEntity p0);
    
    static {
        VaultXPFoodItem.FOOD = new Food.Builder().saturationMod(0.0f).nutrition(0).fast().alwaysEat().build();
    }
    
    public static class Percent extends VaultXPFoodItem
    {
        private final Supplier<Float> min;
        private final Supplier<Float> max;
        
        public Percent(final ResourceLocation id, final Supplier<Float> min, final Supplier<Float> max, final Item.Properties properties) {
            this(id, min, max, properties, -1);
        }
        
        public Percent(final ResourceLocation id, final Supplier<Float> min, final Supplier<Float> max, final Item.Properties properties, final int levelRequirement) {
            super(id, properties, levelRequirement);
            this.min = min;
            this.max = max;
        }
        
        @Override
        public void grantExp(final ServerPlayerEntity sPlayer) {
            final PlayerVaultStatsData statsData = PlayerVaultStatsData.get(sPlayer.getLevel());
            final PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity)sPlayer);
            final float randomPercentage = MathUtilities.randomFloat(this.min.get(), this.max.get());
            statsData.addVaultExp(sPlayer, (int)(stats.getTnl() * randomPercentage));
        }
    }
    
    public static class Flat extends VaultXPFoodItem
    {
        private final Supplier<Integer> min;
        private final Supplier<Integer> max;
        
        public Flat(final ResourceLocation id, final Supplier<Integer> min, final Supplier<Integer> max, final Item.Properties properties) {
            this(id, min, max, properties, -1);
        }
        
        public Flat(final ResourceLocation id, final Supplier<Integer> min, final Supplier<Integer> max, final Item.Properties properties, final int levelRequirement) {
            super(id, properties, levelRequirement);
            this.min = min;
            this.max = max;
        }
        
        @Override
        public void grantExp(final ServerPlayerEntity sPlayer) {
            final PlayerVaultStatsData statsData = PlayerVaultStatsData.get(sPlayer.getLevel());
            statsData.addVaultExp(sPlayer, MathUtilities.getRandomInt(this.min.get(), this.max.get()));
        }
    }
}
