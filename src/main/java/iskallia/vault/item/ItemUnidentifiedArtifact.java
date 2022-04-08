package iskallia.vault.item;

import iskallia.vault.block.VaultArtifactBlock;
import iskallia.vault.init.ModSounds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemUnidentifiedArtifact
        extends Item {
    public static int artifactOverride = -1;

    public ItemUnidentifiedArtifact(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(64));

        setRegistryName(id);
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            ItemStack artifactStack, heldStack = player.getItemInHand(hand);

            Vector3d position = player.position();

            ((ServerWorld) world).playSound(null, position.x, position.y, position.z, ModSounds.BOOSTER_PACK_SUCCESS_SFX, SoundCategory.PLAYERS, 1.0F, 1.0F);


            ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.DRAGON_BREATH, position.x, position.y, position.z, 500, 1.0D, 1.0D, 1.0D, 0.5D);


            if (artifactOverride != -1) {
                artifactStack = VaultArtifactBlock.createArtifact(artifactOverride);
                artifactOverride = -1;
            } else {
                artifactStack = VaultArtifactBlock.createRandomArtifact();
            }
            player.drop(artifactStack, false, false);

            heldStack.shrink(1);
        }

        return super.use(world, player, hand);
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        StringTextComponent text = new StringTextComponent("Right click to identify.");
        text.setStyle(Style.EMPTY.withColor(Color.fromRgb(-9472)));
        tooltip.add(text);

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }


    public boolean isFoil(ItemStack stack) {
        return true;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemUnidentifiedArtifact.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */