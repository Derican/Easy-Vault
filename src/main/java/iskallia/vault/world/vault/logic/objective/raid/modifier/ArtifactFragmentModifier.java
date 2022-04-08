package iskallia.vault.world.vault.logic.objective.raid.modifier;

import iskallia.vault.entity.FloatingItemEntity;
import iskallia.vault.init.ModItems;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.raid.ActiveRaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ArtifactFragmentModifier extends RaidModifier {
    public ArtifactFragmentModifier(String name) {
        super(true, true, name);
    }


    public void affectRaidMob(MobEntity mob, float value) {
    }


    public void onVaultRaidFinish(VaultRaid vault, ServerWorld world, BlockPos controller, ActiveRaid raid, float value) {
        if (rand.nextFloat() >= value) {
            return;
        }
        BlockPos at = controller.relative(Direction.UP, 3);
        FloatingItemEntity itemEntity = FloatingItemEntity.create((World) world, at, new ItemStack((IItemProvider) ModItems.ARTIFACT_FRAGMENT));
        world.addFreshEntity((Entity) itemEntity);
    }


    public ITextComponent getDisplay(float value) {
        int percDisplay = Math.round(value * 100.0F);
        return (ITextComponent) (new StringTextComponent("+" + percDisplay + "% Artifact Fragment chance"))
                .withStyle(TextFormatting.GOLD);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\raid\modifier\ArtifactFragmentModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */