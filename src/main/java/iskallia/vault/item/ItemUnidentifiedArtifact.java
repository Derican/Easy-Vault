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

public class ItemUnidentifiedArtifact extends Item {
    public static int artifactOverride;

    public ItemUnidentifiedArtifact(final ItemGroup group, final ResourceLocation id) {
        super(new Item.Properties().tab(group).stacksTo(64));
        this.setRegistryName(id);
    }

    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        if (!world.isClientSide) {
            final ItemStack heldStack = player.getItemInHand(hand);
            final Vector3d position = player.position();
            world.playSound(null, position.x, position.y, position.z, ModSounds.BOOSTER_PACK_SUCCESS_SFX, SoundCategory.PLAYERS, 1.0f, 1.0f);
            ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.DRAGON_BREATH, position.x, position.y, position.z, 500, 1.0, 1.0, 1.0, 0.5);
            ItemStack artifactStack;
            if (ItemUnidentifiedArtifact.artifactOverride != -1) {
                artifactStack = VaultArtifactBlock.createArtifact(ItemUnidentifiedArtifact.artifactOverride);
                ItemUnidentifiedArtifact.artifactOverride = -1;
            } else {
                artifactStack = VaultArtifactBlock.createRandomArtifact();
            }
            player.drop(artifactStack, false, false);
            heldStack.shrink(1);
        }
        return super.use(world, player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        final StringTextComponent text = new StringTextComponent("Right click to identify.");
        text.setStyle(Style.EMPTY.withColor(Color.fromRgb(-9472)));
        tooltip.add(text);
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public boolean isFoil(final ItemStack stack) {
        return true;
    }

    static {
        ItemUnidentifiedArtifact.artifactOverride = -1;
    }
}
