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

public class VaultStewItem extends SoupItem {
    public static Food FOOD = (new Food.Builder()).saturationMod(0.0F).nutrition(0).fast().alwaysEat().build();
    private final Rarity rarity;

    public VaultStewItem(ResourceLocation id, Rarity rarity, Item.Properties builder) {
        super(builder);
        setRegistryName(id);
        this.rarity = rarity;
    }

    public Rarity getRarity() {
        return this.rarity;
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide && getRarity() == Rarity.MYSTERY) {
            ItemStack heldStack = player.getItemInHand(hand);
            String randomPart = (String) ModConfigs.VAULT_STEW.STEW_POOL.getRandom(world.random);
            ItemStack stackToDrop = new ItemStack((IItemProvider) Registry.ITEM.getOptional(new ResourceLocation(randomPart)).orElse(Items.AIR));
            ItemRelicBoosterPack.successEffects(world, player.position());

            player.drop(stackToDrop, false, false);
            heldStack.shrink(1);
        }

        return super.use(world, player, hand);
    }


    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (getRarity() != Rarity.MYSTERY && entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            PlayerVaultStatsData statsData = PlayerVaultStatsData.get((ServerWorld) world);
            PlayerVaultStats stats = statsData.getVaultStats((PlayerEntity) player);
            statsData.addVaultExp(player, (int) (stats.getTnl() * (getRarity()).tnlProgress));
        }

        return super.finishUsingItem(stack, world, entity);
    }

    public enum Rarity {
        MYSTERY(0.0F),
        NORMAL(0.2F),
        RARE(0.4F),
        EPIC(0.65F),
        OMEGA(0.99F);

        public final float tnlProgress;

        Rarity(float tnlProgress) {
            this.tnlProgress = tnlProgress;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\VaultStewItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */