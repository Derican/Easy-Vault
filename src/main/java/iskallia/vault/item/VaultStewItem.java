// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class VaultStewItem extends SoupItem
{
    public static Food FOOD;
    private final Rarity rarity;
    
    public VaultStewItem(final ResourceLocation id, final Rarity rarity, final Item.Properties builder) {
        super(builder);
        this.setRegistryName(id);
        this.rarity = rarity;
    }
    
    public Rarity getRarity() {
        return this.rarity;
    }
    
    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        if (!world.isClientSide && this.getRarity() == Rarity.MYSTERY) {
            final ItemStack heldStack = player.getItemInHand(hand);
            final String randomPart = ModConfigs.VAULT_STEW.STEW_POOL.getRandom(world.random);
            final ItemStack stackToDrop = new ItemStack((IItemProvider)Registry.ITEM.getOptional(new ResourceLocation(randomPart)).orElse(Items.AIR));
            ItemRelicBoosterPack.successEffects(world, player.position());
            player.drop(stackToDrop, false, false);
            heldStack.shrink(1);
        }
        return (ActionResult<ItemStack>)super.use(world, player, hand);
    }
    
    public ItemStack finishUsingItem(final ItemStack stack, final World world, final LivingEntity entity) {
        if (this.getRarity() != Rarity.MYSTERY && entity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity)entity;
            final PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld)world);
            final PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity)player);
            statsData.addVaultExp(player, (int)(stats.getTnl() * this.getRarity().tnlProgress));
        }
        return super.finishUsingItem(stack, world, entity);
    }
    
    static {
        VaultStewItem.FOOD = new Food.Builder().saturationMod(0.0f).nutrition(0).fast().alwaysEat().build();
    }
    
    public enum Rarity
    {
        MYSTERY(0.0f), 
        NORMAL(0.2f), 
        RARE(0.4f), 
        EPIC(0.65f), 
        OMEGA(0.99f);
        
        public final float tnlProgress;
        
        private Rarity(final float tnlProgress) {
            this.tnlProgress = tnlProgress;
        }
    }
}
