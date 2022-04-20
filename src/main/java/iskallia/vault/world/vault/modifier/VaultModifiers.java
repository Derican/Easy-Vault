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
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VaultModifiers implements INBTSerializable<CompoundNBT>, Iterable<VaultModifier> {
    private final List<ActiveModifier> modifiers;
    protected boolean initialized;

    public VaultModifiers() {
        this.modifiers = new ArrayList<ActiveModifier>();
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized() {
        this.initialized = true;
    }

    public void generateGlobal(final VaultRaid vault, final ServerWorld world, final Random random) {
        final int level = vault.getProperties().getValue(VaultRaid.LEVEL);
        VaultModifiersConfig.ModifierPoolType type = VaultModifiersConfig.ModifierPoolType.DEFAULT;
        if (vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(false)) {
            type = VaultModifiersConfig.ModifierPoolType.RAFFLE;
        } else if (vault.getActiveObjective(RaidChallengeObjective.class).isPresent()) {
            type = VaultModifiersConfig.ModifierPoolType.RAID;
        }
        final ResourceLocation objectiveKey = vault.getAllObjectives().stream().findFirst().map(VaultObjective::getId).orElse(null);
        ModConfigs.VAULT_MODIFIERS.getRandom(random, level, type, objectiveKey).forEach(this::addPermanentModifier);
    }

    @Deprecated
    public void generatePlayer(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
        final int level = player.getProperties().getValue(VaultRaid.LEVEL);
        VaultModifiersConfig.ModifierPoolType type = VaultModifiersConfig.ModifierPoolType.DEFAULT;
        if (vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(false)) {
            type = VaultModifiersConfig.ModifierPoolType.RAFFLE;
        } else if (vault.getActiveObjective(RaidChallengeObjective.class).isPresent()) {
            type = VaultModifiersConfig.ModifierPoolType.RAID;
        }
        final ResourceLocation objectiveKey = vault.getAllObjectives().stream().findFirst().map(VaultObjective::getId).orElse(null);
        ModConfigs.VAULT_MODIFIERS.getRandom(random, level, type, objectiveKey).forEach(this::addPermanentModifier);
        this.setInitialized();
    }

    public void apply(final VaultRaid vault, final VaultPlayer player, final ServerWorld world, final Random random) {
        this.modifiers.forEach(modifier -> modifier.getModifier().apply(vault, player, world, random));
    }

    public void tick(final VaultRaid vault, final ServerWorld world, final PlayerFilter applyFilter) {
        this.modifiers.removeIf(activeModifier -> {
            final VaultModifier modifier = activeModifier.getModifier();
            vault.getPlayers().forEach(vPlayer -> {
                if (applyFilter.test(vPlayer.getPlayerId())) {
                    modifier.tick(vault, null, world);
                }
                return;
            });
            if (activeModifier.tick()) {
                final ITextComponent removalMsg = (ITextComponent) new StringTextComponent("Modifier ").withStyle(TextFormatting.GRAY).append(modifier.getNameComponent()).append((ITextComponent) new StringTextComponent(" expired.").withStyle(TextFormatting.GRAY));
                vault.getPlayers().forEach(vPlayer -> {
                    if (applyFilter.test(vPlayer.getPlayerId())) {
                        modifier.remove(vault, vPlayer, world, world.getRandom());
                        vPlayer.runIfPresent(world.getServer(), sPlayer -> sPlayer.sendMessage(removalMsg, Util.NIL_UUID));
                    }
                    return;
                });
                return true;
            } else {
                return false;
            }
        });
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        final ListNBT modifiersList = new ListNBT();
        this.modifiers.forEach(modifier -> modifiersList.add(modifier.serialize()));
        nbt.put("modifiers", (INBT) modifiersList);
        nbt.putBoolean("Initialized", this.isInitialized());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.modifiers.clear();
        final ListNBT modifierList = nbt.getList("modifiers", 10);
        for (int i = 0; i < modifierList.size(); ++i) {
            final CompoundNBT tag = modifierList.getCompound(i);
            final ActiveModifier mod = ActiveModifier.deserialize(tag);
            if (mod != null) {
                this.modifiers.add(mod);
            }
        }
        final ListNBT legacyModifierList = nbt.getList("List", 8);
        for (int j = 0; j < legacyModifierList.size(); ++j) {
            final VaultModifier modifier = ModConfigs.VAULT_MODIFIERS.getByName(legacyModifierList.getString(j));
            if (modifier != null) {
                this.modifiers.add(new ActiveModifier(modifier, -1));
            }
        }
        this.initialized = nbt.getBoolean("Initialized");
    }

    public void encode(final PacketBuffer buffer) {
        buffer.writeInt(this.modifiers.size());
        this.modifiers.forEach(modifier -> modifier.encode(buffer));
    }

    public static VaultModifiers decode(final PacketBuffer buffer) {
        final VaultModifiers result = new VaultModifiers();
        for (int size = buffer.readInt(), i = 0; i < size; ++i) {
            final ActiveModifier modifier = ActiveModifier.decode(buffer);
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
        final List<VaultModifier> modifiers = this.stream().collect(Collectors.toList());
        return modifiers.iterator();
    }

    public void forEach(final BiConsumer<Integer, VaultModifier> consumer) {
        int index = 0;
        for (final ActiveModifier modifier : this.modifiers) {
            consumer.accept(index, modifier.getModifier());
            ++index;
        }
    }

    public int size() {
        return this.modifiers.size();
    }

    public boolean isEmpty() {
        return this.size() <= 0;
    }

    public void addPermanentModifier(final String name) {
        this.addPermanentModifier(ModConfigs.VAULT_MODIFIERS.getByName(name));
    }

    public void addPermanentModifier(final VaultModifier modifier) {
        this.putModifier(modifier, -1);
    }

    public void addTemporaryModifier(final VaultModifier modifier, final int timeout) {
        this.putModifier(modifier, Math.max(0, timeout));
    }

    private void putModifier(final VaultModifier modifier, final int timeout) {
        this.modifiers.add(new ActiveModifier(modifier, timeout));
    }

    public boolean removePermanentModifier(final String name) {
        for (final ActiveModifier activeModifier : this.modifiers) {
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

        private ActiveModifier(final VaultModifier modifier, final int tick) {
            this.modifier = modifier;
            this.tick = tick;
        }

        @Nullable
        private static ActiveModifier deserialize(final CompoundNBT tag) {
            final VaultModifier modifier = ModConfigs.VAULT_MODIFIERS.getByName(tag.getString("key"));
            final int timeout = tag.getInt("timeout");
            if (modifier == null) {
                return null;
            }
            return new ActiveModifier(modifier, timeout);
        }

        @Nullable
        private static ActiveModifier decode(final PacketBuffer buffer) {
            final String modifierName = buffer.readUtf(32767);
            final int timeout = buffer.readInt();
            final VaultModifier modifier = ModConfigs.VAULT_MODIFIERS.getByName(modifierName);
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
            --this.tick;
            return this.tick == 0;
        }

        private void encode(final PacketBuffer buffer) {
            buffer.writeUtf(this.modifier.getName());
            buffer.writeInt(this.tick);
        }

        private CompoundNBT serialize() {
            final CompoundNBT tag = new CompoundNBT();
            tag.putString("key", this.modifier.getName());
            tag.putInt("timeout", this.tick);
            return tag;
        }
    }
}
