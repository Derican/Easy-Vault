package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRelicBoosterPack extends Item {
    public ItemRelicBoosterPack(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(64));

        setRegistryName(id);
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            int rand = world.random.nextInt(100);
            ItemStack heldStack = player.getItemInHand(hand);
            ItemStack stackToDrop = ItemStack.EMPTY;

            if (rand == 99) {
                RelicPartItem randomPart = ModConfigs.VAULT_RELICS.getRandomPart();
                stackToDrop = new ItemStack((IItemProvider) randomPart);
                successEffects(world, player.position());
            } else if (rand == 98) {
                stackToDrop = new ItemStack((IItemProvider) ModItems.MYSTERY_BOX);
                successEffects(world, player.position());
            } else if (rand == 97 && "architect_event".equals(getKey(heldStack))) {
                stackToDrop = VaultCrystalItem.getCrystalWithObjective(((ArchitectObjective) VaultRaid.ARCHITECT_EVENT.get()).getId());
                successEffects(world, player.position());
            } else {

                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                ServerWorld serverWorld = serverPlayer.getLevel();
                float coef = MathUtilities.randomFloat(0.1F, 0.25F);
                PlayerVaultStatsData.get(serverWorld).addVaultExp(serverPlayer, (int) (90.0F * coef));
                failureEffects(world, player.position());
            }

            if (!stackToDrop.isEmpty()) {
                player.drop(stackToDrop, false, false);
            }
            heldStack.shrink(1);
        }

        return super.use(world, player, hand);
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, world, tooltip, flagIn);

        if ("architect_event".equals(getKey(stack))) {
            tooltip.add((new StringTextComponent("Architect")).withStyle(TextFormatting.AQUA));
        }
    }

    public static ItemStack getArchitectBoosterPack() {
        ItemStack stack = new ItemStack((IItemProvider) ModItems.RELIC_BOOSTER_PACK);
        stack.getOrCreateTag().putString("eventKey", "architect_event");
        return stack;
    }

    @Nullable
    public static String getKey(ItemStack stack) {
        if (!stack.hasTag()) {
            return null;
        }
        return stack.getOrCreateTag().getString("eventKey");
    }

    public static void successEffects(World world, Vector3d pos) {
        world.playSound(null, pos.x, pos.y, pos.z, ModSounds.BOOSTER_PACK_SUCCESS_SFX, SoundCategory.PLAYERS, 1.0F, 1.0F);
        ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.DRAGON_BREATH, pos.x, pos.y, pos.z, 500, 1.0D, 1.0D, 1.0D, 0.5D);
    }


    public static void failureEffects(World world, Vector3d pos) {
        world.playSound(null, pos.x, pos.y, pos.z, ModSounds.BOOSTER_PACK_FAIL_SFX, SoundCategory.PLAYERS, 1.0F, 1.0F);
        ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 500, 1.0D, 1.0D, 1.0D, 0.5D);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemRelicBoosterPack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */