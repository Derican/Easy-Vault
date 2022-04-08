package iskallia.vault.world.vault.modifier;

import iskallia.vault.config.VaultModifiersConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.objective.raid.RaidChallengeObjective;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VaultModifiers implements INBTSerializable<CompoundNBT>, Iterable<VaultModifier> {
    private final List<ActiveModifier> modifiers = new ArrayList<>();

    protected boolean initialized;


    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized() {
        this.initialized = true;
    }

    public void generateGlobal(VaultRaid vault, ServerWorld world, Random random) {
        int level = ((Integer) vault.getProperties().getValue(VaultRaid.LEVEL)).intValue();
        VaultModifiersConfig.ModifierPoolType type = VaultModifiersConfig.ModifierPoolType.DEFAULT;
        if (((Boolean) vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(Boolean.valueOf(false))).booleanValue()) {
            type = VaultModifiersConfig.ModifierPoolType.RAFFLE;
        } else if (vault.getActiveObjective(RaidChallengeObjective.class).isPresent()) {
            type = VaultModifiersConfig.ModifierPoolType.RAID;
        }
        ResourceLocation objectiveKey = vault.getAllObjectives().stream().findFirst().map(VaultObjective::getId).orElse(null);
        ModConfigs.VAULT_MODIFIERS.getRandom(random, level, type, objectiveKey).forEach(this::addPermanentModifier);
    }


    @Deprecated
    public void generatePlayer(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        int level = ((Integer) player.getProperties().getValue(VaultRaid.LEVEL)).intValue();
        VaultModifiersConfig.ModifierPoolType type = VaultModifiersConfig.ModifierPoolType.DEFAULT;
        if (((Boolean) vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(Boolean.valueOf(false))).booleanValue()) {
            type = VaultModifiersConfig.ModifierPoolType.RAFFLE;
        } else if (vault.getActiveObjective(RaidChallengeObjective.class).isPresent()) {
            type = VaultModifiersConfig.ModifierPoolType.RAID;
        }
        ResourceLocation objectiveKey = vault.getAllObjectives().stream().findFirst().map(VaultObjective::getId).orElse(null);
        ModConfigs.VAULT_MODIFIERS.getRandom(random, level, type, objectiveKey).forEach(this::addPermanentModifier);
        setInitialized();
    }

    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world, Random random) {
        this.modifiers.forEach(modifier -> modifier.getModifier().apply(vault, player, world, random));
    }

    public void tick(VaultRaid vault, ServerWorld world, PlayerFilter applyFilter) {
        this.modifiers.removeIf(activeModifier -> {
            VaultModifier modifier = activeModifier.getModifier();
            vault.getPlayers().forEach(());
            if (activeModifier.tick()) {
                IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("Modifier ")).withStyle(TextFormatting.GRAY).append(modifier.getNameComponent()).append((ITextComponent) (new StringTextComponent(" expired.")).withStyle(TextFormatting.GRAY));
                vault.getPlayers().forEach(());
                return true;
            }
            return false;
        });
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT modifiersList = new ListNBT();
        this.modifiers.forEach(modifier -> modifiersList.add(modifier.serialize()));
        nbt.put("modifiers", (INBT) modifiersList);
        nbt.putBoolean("Initialized", isInitialized());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.modifiers.clear();

        ListNBT modifierList = nbt.getList("modifiers", 10);
        for (int i = 0; i < modifierList.size(); i++) {
            CompoundNBT tag = modifierList.getCompound(i);
            ActiveModifier mod = ActiveModifier.deserialize(tag);
            if (mod != null) {
                this.modifiers.add(mod);
            }
        }


        ListNBT legacyModifierList = nbt.getList("List", 8);
        for (int j = 0; j < legacyModifierList.size(); j++) {
            VaultModifier modifier = ModConfigs.VAULT_MODIFIERS.getByName(legacyModifierList.getString(j));
            if (modifier != null) {
                this.modifiers.add(new ActiveModifier(modifier, -1));
            }
        }

        this.initialized = nbt.getBoolean("Initialized");
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.modifiers.size());
        this.modifiers.forEach(modifier -> modifier.encode(buffer));
    }

    public static VaultModifiers decode(PacketBuffer buffer) {
        VaultModifiers result = new VaultModifiers();

        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            ActiveModifier modifier = ActiveModifier.decode(buffer);
            if (modifier != null) {
                result.modifiers.add(modifier);
            }
        }

        return result;
    }

    public Stream<VaultModifier> stream() {
        return this.modifiers.stream().map(rec$ -> ((ActiveModifier) rec$).getModifier());
    }


    @Nonnull
    public Iterator<VaultModifier> iterator() {
        List<VaultModifier> modifiers = stream().collect((Collector) Collectors.toList());
        return modifiers.iterator();
    }

    public void forEach(BiConsumer<Integer, VaultModifier> consumer) {
        int index = 0;
        for (ActiveModifier modifier : this.modifiers) {
            consumer.accept(Integer.valueOf(index), modifier.getModifier());
            index++;
        }
    }

    public int size() {
        return this.modifiers.size();
    }

    public boolean isEmpty() {
        return (size() <= 0);
    }

    public void addPermanentModifier(String name) {
        addPermanentModifier(ModConfigs.VAULT_MODIFIERS.getByName(name));
    }

    public void addPermanentModifier(VaultModifier modifier) {
        putModifier(modifier, -1);
    }

    public void addTemporaryModifier(VaultModifier modifier, int timeout) {
        putModifier(modifier, Math.max(0, timeout));
    }

    private void putModifier(VaultModifier modifier, int timeout) {
        this.modifiers.add(new ActiveModifier(modifier, timeout));
    }

    public boolean removePermanentModifier(String name) {
        for (ActiveModifier activeModifier : this.modifiers) {
            if (activeModifier.getModifier().getName().equals(name) && activeModifier.tick == -1) {
                this.modifiers.remove(activeModifier);
                return true;
            }
        }
        return false;
    }

    private static class ActiveModifier {
        private final VaultModifier modifier;
        private int tick;

        private ActiveModifier(VaultModifier modifier, int tick) {
            this.modifier = modifier;
            this.tick = tick;
        }

        @Nullable
        private static ActiveModifier deserialize(CompoundNBT tag) {
            VaultModifier modifier = ModConfigs.VAULT_MODIFIERS.getByName(tag.getString("key"));
            int timeout = tag.getInt("timeout");
            if (modifier == null) {
                return null;
            }
            return new ActiveModifier(modifier, timeout);
        }

        @Nullable
        private static ActiveModifier decode(PacketBuffer buffer) {
            String modifierName = buffer.readUtf(32767);
            int timeout = buffer.readInt();
            VaultModifier modifier = ModConfigs.VAULT_MODIFIERS.getByName(modifierName);
            if (modifier == null) {
                return null;
            }
            return new ActiveModifier(modifier, timeout);
        }

        private VaultModifier getModifier() {
            return this.modifier;
        }

        private boolean tick() {
            if (this.tick == -1) {
                return false;
            }
            this.tick--;
            return (this.tick == 0);
        }

        private void encode(PacketBuffer buffer) {
            buffer.writeUtf(this.modifier.getName());
            buffer.writeInt(this.tick);
        }

        private CompoundNBT serialize() {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("key", this.modifier.getName());
            tag.putInt("timeout", this.tick);
            return tag;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\modifier\VaultModifiers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */