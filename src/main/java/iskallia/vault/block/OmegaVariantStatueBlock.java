package iskallia.vault.block;

import iskallia.vault.block.entity.LootStatueTileEntity;
import iskallia.vault.container.OmegaStatueContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.StatueType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;


public class OmegaVariantStatueBlock
        extends LootStatueBlock {
    public OmegaVariantStatueBlock() {
        super(StatueType.OMEGA_VARIANT, AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                .strength(1.0F, 3600000.0F)
                .noOcclusion());
        registerDefaultState((BlockState) ((BlockState) getStateDefinition().any()).setValue((Property) FACING, (Comparable) Direction.SOUTH));
    }


    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (worldIn.isClientSide || !(placer instanceof PlayerEntity))
            return;
        PlayerEntity player = (PlayerEntity) placer;
        TileEntity tileEntity = worldIn.getBlockEntity(pos);

        if (tileEntity instanceof LootStatueTileEntity) {
            LootStatueTileEntity lootStatue = (LootStatueTileEntity) tileEntity;
            if (stack.hasTag()) {
                CompoundNBT nbt = stack.getTag();
                CompoundNBT blockEntityTag = nbt.getCompound("BlockEntityTag");
                setStatueTileData(lootStatue, blockEntityTag);
                if (nbt.contains("LootItem")) {
                    lootStatue.setLootItem(ItemStack.of(blockEntityTag.getCompound("LootItem")));
                }

                lootStatue.setChanged();
                lootStatue.sendUpdates();

                if (lootStatue.getLootItem() == null || lootStatue.getLootItem().isEmpty()) {
                    final CompoundNBT data = new CompoundNBT();
                    ListNBT itemList = new ListNBT();
                    List<ItemStack> options = ModConfigs.STATUE_LOOT.getOmegaOptions();
                    for (ItemStack option : options) {
                        itemList.add(option.serializeNBT());
                    }
                    data.put("Items", (INBT) itemList);
                    data.put("Position", (INBT) NBTUtil.writeBlockPos(pos));


                    NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {

                        public ITextComponent getDisplayName() {
                            return (ITextComponent) new StringTextComponent("Omega Statue Options");
                        }


                        @Nullable
                        public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                            return (Container) new OmegaStatueContainer(windowId, data);
                        }
                    },buffer -> buffer.writeNbt(data));
                }
            }
        }
    }


    protected void setStatueTileData(LootStatueTileEntity lootStatue, CompoundNBT blockEntityTag) {
        StatueType statueType = StatueType.values()[blockEntityTag.getInt("StatueType")];
        String playerNickname = blockEntityTag.getString("PlayerNickname");

        lootStatue.setStatueType(StatueType.OMEGA_VARIANT);
        lootStatue.setCurrentTick(blockEntityTag.getInt("CurrentTick"));
        lootStatue.getSkin().updateSkin(playerNickname);
        lootStatue.setLootItem(ItemStack.of(blockEntityTag.getCompound("LootItem")));
    }


    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{(Property) FACING});
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\OmegaVariantStatueBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */