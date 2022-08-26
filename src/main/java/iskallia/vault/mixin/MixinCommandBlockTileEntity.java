package iskallia.vault.mixin;

import iskallia.vault.Vault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.CommandBlockLogic;
import net.minecraft.tileentity.CommandBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.concurrent.TickDelayedTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({CommandBlockTileEntity.class})
public abstract class MixinCommandBlockTileEntity extends TileEntity {
    @Shadow
    private boolean auto;

    @Shadow
    public abstract void setAutomatic(final boolean p0);

    @Shadow
    public abstract CommandBlockLogic getCommandBlock();

    @Shadow
    public abstract void setPowered(final boolean p0);

    public MixinCommandBlockTileEntity(final TileEntityType<?> type) {
        super(type);
    }

    @Inject(method = {"clearRemoved"}, at = {@At("RETURN")})
    public void validate(final CallbackInfo ci) {
        if (!this.level.isClientSide && this.level.dimension() == Vault.VAULT_KEY) {
            this.level.getServer().tell(new TickDelayedTask(this.level.getServer().getTickCount() + 10, () -> {
                if (!this.level.getBlockTicks().hasScheduledTick(this.getBlockPos(), Blocks.COMMAND_BLOCK) && this.auto) {
                    this.auto = false;
                    this.setAutomatic(true);
                }
                this.setPowered(false);
                final BlockState state = this.level.getBlockState(this.getBlockPos());
                this.level.getBlockState(this.getBlockPos()).neighborChanged(this.level, this.getBlockPos(), state.getBlock(), this.getBlockPos(), false);
            }));
        }
    }
}
