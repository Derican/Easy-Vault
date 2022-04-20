package iskallia.vault.block.entity;

import com.google.common.collect.Iterables;
import iskallia.vault.client.ClientEternalData;
import iskallia.vault.entity.eternal.EternalDataSnapshot;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.data.EternalsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class AncientCryoChamberTileEntity extends CryoChamberTileEntity {
    public AncientCryoChamberTileEntity() {
        super(ModBlocks.ANCIENT_CRYO_CHAMBER_TILE_ENTITY);
        this.setMaxCores(1);
    }

    public void setEternalName(final String coreName) {
        this.coreNames.clear();
        this.coreNames.add(coreName);
        this.setChanged();
    }

    @Nonnull
    public String getEternalName() {
        return (String) Iterables.getFirst((Iterable) this.coreNames, "Unknown");
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide || this.getOwner() == null) {
            return;
        }
        if (this.getEternalId() != null && this.level.getGameTime() % 40L == 0L) {
            this.level.playSound((PlayerEntity) null, (double) this.worldPosition.getX(), (double) this.worldPosition.getY(), (double) this.worldPosition.getZ(), SoundEvents.CONDUIT_AMBIENT, SoundCategory.PLAYERS, 0.25f, 1.0f);
        }
        if (this.getEternalId() == null && !this.coreNames.isEmpty()) {
            this.createAncient();
            this.sendUpdates();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void updateSkin() {
        if (this.eternalId == null && !this.coreNames.isEmpty()) {
            this.skin.updateSkin(this.getEternalName());
            return;
        }
        final EternalDataSnapshot snapshot = ClientEternalData.getSnapshot(this.getEternalId());
        if (snapshot == null || snapshot.getName() == null) {
            return;
        }
        this.skin.updateSkin(snapshot.getName());
    }

    private void createAncient() {
        final String name = (String) Iterables.getFirst((Iterable) this.coreNames, "Unknown");
        this.eternalId = EternalsData.get((ServerWorld) this.getLevel()).add(this.getOwner(), name, true);
    }
}
