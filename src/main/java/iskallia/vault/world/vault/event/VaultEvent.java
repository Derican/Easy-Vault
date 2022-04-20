package iskallia.vault.world.vault.event;

import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.eventbus.api.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class VaultEvent<T extends Event> implements INBTSerializable<CompoundNBT> {
    public static final Map<ResourceLocation, VaultEvent<?>> REGISTRY;
    private ResourceLocation id;
    private Class<T> type;
    private BiConsumer<VaultRaid, T> onEvent;

    protected VaultEvent() {
    }

    protected VaultEvent(final ResourceLocation id, final Class<T> type, final BiConsumer<VaultRaid, T> onEvent) {
        this.id = id;
        this.type = type;
        this.onEvent = onEvent;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Class<T> getType() {
        return this.type;
    }

    protected void accept(final VaultRaid vault, final Event event) {
        this.onEvent.accept(vault, (T) event);
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", this.getId().toString());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.id = new ResourceLocation(nbt.getString("Id"));
    }

    public static <T extends Event> VaultEvent<T> register(final ResourceLocation id, final Class<T> type, final BiConsumer<VaultRaid, T> onEvent) {
        final VaultEvent<T> listener = new VaultEvent<T>(id, type, onEvent);
        VaultEvent.REGISTRY.put(id, listener);
        return listener;
    }

    public static VaultEvent<?> fromNBT(final CompoundNBT nbt) {
        return VaultEvent.REGISTRY.get(new ResourceLocation(nbt.getString("Id")));
    }

    static {
        REGISTRY = new HashMap<ResourceLocation, VaultEvent<?>>();
    }
}
