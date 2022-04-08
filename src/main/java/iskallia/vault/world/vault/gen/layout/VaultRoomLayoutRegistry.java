package iskallia.vault.world.vault.gen.layout;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


public class VaultRoomLayoutRegistry {
    private static final Map<ResourceLocation, Supplier<VaultRoomLayoutGenerator>> layoutRegistry = new HashMap<>();

    public static void init() {
        layoutRegistry.put(SingularVaultRoomLayout.ID, SingularVaultRoomLayout::new);
        layoutRegistry.put(LineRoomLayout.ID, LineRoomLayout::new);
        layoutRegistry.put(DiamondRoomLayout.ID, DiamondRoomLayout::new);
        layoutRegistry.put(SquareRoomLayout.ID, SquareRoomLayout::new);
        layoutRegistry.put(CircleRoomLayout.ID, CircleRoomLayout::new);
        layoutRegistry.put(TriangleRoomLayout.ID, TriangleRoomLayout::new);
        layoutRegistry.put(SpiralRoomLayout.ID, SpiralRoomLayout::new);
        layoutRegistry.put(DebugVaultLayout.ID, DebugVaultLayout::new);
        layoutRegistry.put(DenseDiamondRoomLayout.ID, DenseDiamondRoomLayout::new);
        layoutRegistry.put(DenseSquareRoomLayout.ID, DenseSquareRoomLayout::new);
    }

    @Nullable
    public static VaultRoomLayoutGenerator getLayoutGenerator(ResourceLocation id) {
        return layoutRegistry.containsKey(id) ? ((Supplier<VaultRoomLayoutGenerator>) layoutRegistry.get(id)).get() : null;
    }

    @Nullable
    public static VaultRoomLayoutGenerator deserialize(CompoundNBT tag) {
        if (!tag.contains("Id", 8)) {
            return null;
        }
        VaultRoomLayoutGenerator layout = getLayoutGenerator(new ResourceLocation(tag.getString("Id")));
        if (layout == null) {
            return null;
        }
        layout.deserialize(tag.getCompound("Data"));
        layout.generateLayout();
        return layout;
    }

    public static CompoundNBT serialize(VaultRoomLayoutGenerator roomLayout) {
        CompoundNBT layoutTag = new CompoundNBT();
        layoutTag.putString("Id", roomLayout.getId().toString());
        layoutTag.put("Data", (INBT) roomLayout.serialize());
        return layoutTag;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\layout\VaultRoomLayoutRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */