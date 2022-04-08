package iskallia.vault.world.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PlayerImmunityData extends WorldSavedData {
    protected Map<UUID, List<String>> effects = new HashMap<>();
    protected static final String DATA_NAME = "the_vault_PlayerImmunity";

    public PlayerImmunityData() {
        this("the_vault_PlayerImmunity");
    }

    public PlayerImmunityData(String name) {
        super(name);
    }


    public void addEffect(PlayerEntity player, EffectInstance effectInstance) {
        addEffect(player.getUUID(), effectInstance);
    }

    private void addEffect(UUID playerUUID, EffectInstance effectInstance) {
        this.effects.put(playerUUID, Collections.singletonList(effectInstance.getEffect().getRegistryName().toString()));
        setDirty();
    }

    public void addEffects(PlayerEntity player, List<EffectInstance> effects) {
        addEffects(player.getUUID(), effects);
    }

    public void addEffects(UUID playerUUID, List<EffectInstance> effects) {
        this.effects.put(playerUUID, (List<String>) effects
                .stream()
                .map(EffectInstance::getEffect)
                .map(ForgeRegistryEntry::getRegistryName)
                .filter(Objects::nonNull)
                .map(ResourceLocation::toString)
                .collect(Collectors.toList()));

        setDirty();
    }

    public List<Effect> getEffects(UUID playerUUID) {
        return (List<Effect>) Registry.MOB_EFFECT.stream().filter(effect -> ((List) this.effects.get(playerUUID)).contains(effect.getRegistryName().toString())).collect(Collectors.toList());
    }

    public void removeEffects(PlayerEntity player) {
        removeEffects(player.getUUID());
    }

    public void removeEffects(UUID playerUUID) {
        this.effects.remove(playerUUID);
        setDirty();
    }


    public void load(CompoundNBT nbt) {
        this.effects.clear();
        for (Iterator<String> iterator = nbt.getAllKeys().iterator(); iterator.hasNext(); ) {
            UUID uuid;
            String key = iterator.next();

            try {
                uuid = UUID.fromString(key);
            } catch (IllegalArgumentException ignored) {
                continue;
            }

            List<String> effects = new ArrayList<>();
            CompoundNBT effectTag = nbt.getCompound(key);
            effectTag.getAllKeys().forEach(effectKey -> effects.add(effectTag.getString(effectKey)));
            this.effects.put(uuid, effects);
        }

    }


    public CompoundNBT save(CompoundNBT compound) {
        this.effects.forEach((uuid, effects) -> {
            CompoundNBT effectTag = new CompoundNBT();
            AtomicInteger index = new AtomicInteger();
            effects.forEach(());
            compound.put(uuid.toString(), (INBT) effectTag);
        });
        return compound;
    }

    public static PlayerImmunityData get(ServerWorld world) {
        return (PlayerImmunityData) world.getServer().overworld().getDataStorage().computeIfAbsent(PlayerImmunityData::new, "the_vault_PlayerImmunity");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\PlayerImmunityData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */