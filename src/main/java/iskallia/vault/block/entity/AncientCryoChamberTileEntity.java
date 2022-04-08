package iskallia.vault.block.entity;

import com.google.common.collect.Iterables;
import iskallia.vault.client.ClientEternalData;
import iskallia.vault.entity.eternal.EternalDataSnapshot;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.data.EternalsData;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;


public class AncientCryoChamberTileEntity
        extends CryoChamberTileEntity {
    public AncientCryoChamberTileEntity() {
        super(ModBlocks.ANCIENT_CRYO_CHAMBER_TILE_ENTITY);
        setMaxCores(1);
    }

    public void setEternalName(String coreName) {
        this.coreNames.clear();
        this.coreNames.add(coreName);
        setChanged();
    }

    @Nonnull
    public String getEternalName() {
        return (String) Iterables.getFirst(this.coreNames, "Unknown");
    }


    public void tick() {
        if (this.level == null || this.level.isClientSide || getOwner() == null)
            return;
        if (getEternalId() != null && this.level.getGameTime() % 40L == 0L) {
            this.level.playSound(null, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), SoundEvents.CONDUIT_AMBIENT, SoundCategory.PLAYERS, 0.25F, 1.0F);
        }

        if (getEternalId() == null && !this.coreNames.isEmpty()) {
            createAncient();
            sendUpdates();
        }
    }


    @OnlyIn(Dist.CLIENT)
    public void updateSkin() {
        if (this.eternalId == null && !this.coreNames.isEmpty()) {
            this.skin.updateSkin(getEternalName());
            return;
        }
        EternalDataSnapshot snapshot = ClientEternalData.getSnapshot(getEternalId());
        if (snapshot == null || snapshot.getName() == null) {
            return;
        }
        this.skin.updateSkin(snapshot.getName());
    }

    private void createAncient() {
        String name = (String) Iterables.getFirst(this.coreNames, "Unknown");
        this.eternalId = EternalsData.get((ServerWorld) getLevel()).add(getOwner(), name, true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\AncientCryoChamberTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */