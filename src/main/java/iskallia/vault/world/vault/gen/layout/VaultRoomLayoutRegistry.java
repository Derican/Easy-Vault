package iskallia.vault.world.vault.gen.layout;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class VaultRoomLayoutRegistry {
    private static final Map<ResourceLocation, Supplier<VaultRoomLayoutGenerator>> layoutRegistry;

    public static void init() {
        VaultRoomLayoutRegistry.layoutRegistry.put(SingularVaultRoomLayout.ID, SingularVaultRoomLayout::new);
        VaultRoomLayoutRegistry.layoutRegistry.put(LineRoomLayout.ID, LineRoomLayout::new);
        VaultRoomLayoutRegistry.layoutRegistry.put(DiamondRoomLayout.ID, DiamondRoomLayout::new);
        VaultRoomLayoutRegistry.layoutRegistry.put(SquareRoomLayout.ID, SquareRoomLayout::new);
        VaultRoomLayoutRegistry.layoutRegistry.put(CircleRoomLayout.ID, CircleRoomLayout::new);
        VaultRoomLayoutRegistry.layoutRegistry.put(TriangleRoomLayout.ID, TriangleRoomLayout::new);
        VaultRoomLayoutRegistry.layoutRegistry.put(SpiralRoomLayout.ID, SpiralRoomLayout::new);
        VaultRoomLayoutRegistry.layoutRegistry.put(DebugVaultLayout.ID, DebugVaultLayout::new);
        VaultRoomLayoutRegistry.layoutRegistry.put(DenseDiamondRoomLayout.ID, DenseDiamondRoomLayout::new);
        VaultRoomLayoutRegistry.layoutRegistry.put(DenseSquareRoomLayout.ID, DenseSquareRoomLayout::new);
        VaultRoomLayoutRegistry.layoutRegistry.put(EmptyVaultLayout.ID, EmptyVaultLayout::new);

    }

    @Nullable
    public static VaultRoomLayoutGenerator getLayoutGenerator(final ResourceLocation id) {
        return VaultRoomLayoutRegistry.layoutRegistry.containsKey(id) ? VaultRoomLayoutRegistry.layoutRegistry.get(id).get() : null;
    }

    @Nullable
    public static VaultRoomLayoutGenerator deserialize(final CompoundNBT tag) {
        if (!tag.contains("Id", 8)) {
            return null;
        }
        final VaultRoomLayoutGenerator layout = getLayoutGenerator(new ResourceLocation(tag.getString("Id")));
        if (layout == null) {
            return null;
        }
        layout.deserialize(tag.getCompound("Data"));
        layout.generateLayout();
        return layout;
    }

    public static CompoundNBT serialize(final VaultRoomLayoutGenerator roomLayout) {
        final CompoundNBT layoutTag = new CompoundNBT();
        layoutTag.putString("Id", roomLayout.getId().toString());
        layoutTag.put("Data", roomLayout.serialize());
        return layoutTag;
    }

    static {
        layoutRegistry = new HashMap<ResourceLocation, Supplier<VaultRoomLayoutGenerator>>();
    }
}
