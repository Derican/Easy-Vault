package iskallia.vault.item.crystal;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class FrameData implements INBTSerializable<CompoundNBT> {
    public List<Tile> tiles;

    public FrameData() {
        this.tiles = new ArrayList<Tile>();
    }

    public static FrameData fromNBT(final CompoundNBT nbt) {
        final FrameData frame = new FrameData();
        frame.deserializeNBT(nbt);
        return frame;
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        final ListNBT tilesList = new ListNBT();
        this.tiles.forEach(tile -> tilesList.add(tile.serializeNBT()));
        nbt.put("Tiles", (INBT) tilesList);
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        final ListNBT tilesList = nbt.getList("Tiles", 10);
        this.tiles.clear();
        for (int i = 0; i < tilesList.size(); ++i) {
            final Tile tile = new Tile();
            tile.deserializeNBT(tilesList.getCompound(i));
            this.tiles.add(tile);
        }
    }

    public static class Tile implements INBTSerializable<CompoundNBT> {
        public Block block;
        public CompoundNBT data;
        public BlockPos pos;

        public Tile() {
        }

        public Tile(final Block block, final CompoundNBT data, final BlockPos pos) {
            this.block = block;
            this.data = data;
            this.pos = pos;
        }

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Block", this.block.getRegistryName().toString());
            nbt.put("Data", (INBT) this.data.copy());
            nbt.putInt("PosX", this.pos.getX());
            nbt.putInt("PosY", this.pos.getY());
            nbt.putInt("PosZ", this.pos.getZ());
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.block = (Block) ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("Block")));
            this.data = nbt.getCompound("Data").copy();
            this.pos = new BlockPos(nbt.getInt("PosX"), nbt.getInt("PosY"), nbt.getInt("PosZ"));
        }
    }
}
