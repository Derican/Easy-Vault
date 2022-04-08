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
public abstract class MixinCommandBlockTileEntity
        extends TileEntity {
    @Shadow
    private boolean auto;

    @Shadow
    public abstract void setAutomatic(boolean paramBoolean);

    public MixinCommandBlockTileEntity(TileEntityType<?> type) {
        super(type);
    }

    @Shadow
    public abstract CommandBlockLogic getCommandBlock();

    @Shadow
    public abstract void setPowered(boolean paramBoolean);

    @Inject(method = {"validate"}, at = {@At("RETURN")})
    public void validate(CallbackInfo ci) {
        if (!this.level.isClientSide && this.level.dimension() == Vault.VAULT_KEY)
            this.level.getServer().tell((TickDelayedTask) new TickDelayedTask(this.level
                    .getServer().getTickCount() + 10, () -> {
                if (!this.level.getBlockTicks().hasScheduledTick(getBlockPos(), Blocks.COMMAND_BLOCK) && this.auto) {
                    this.auto = false;
                    setAutomatic(true);
                }
                setPowered(false);
                BlockState state = this.level.getBlockState(getBlockPos());
                this.level.getBlockState(getBlockPos()).neighborChanged(this.level, getBlockPos(), state.getBlock(), getBlockPos(), false);
            }));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinCommandBlockTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */