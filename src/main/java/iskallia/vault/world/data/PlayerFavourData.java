package iskallia.vault.world.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;
import java.util.function.Supplier;

public class PlayerFavourData extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_PlayerFavour";
    protected Map<UUID, Map<VaultGodType, Integer>> favourStats;

    public PlayerFavourData() {
        this("the_vault_PlayerFavour");
    }

    public PlayerFavourData(final String name) {
        super(name);
        this.favourStats = new HashMap<UUID, Map<VaultGodType, Integer>>();
    }

    public boolean addFavour(final PlayerEntity player, final VaultGodType type, final int count) {
        final UUID playerUUID = player.getUUID();
        int favour = (int) this.favourStats.computeIfAbsent(playerUUID, key -> new HashMap()).getOrDefault(type, 0);
        if (Math.abs(favour + count) > 16) {
            return false;
        }
        favour += count;
        this.favourStats.computeIfAbsent(playerUUID, key -> new HashMap()).put(type, favour);
        this.setDirty();
        return true;
    }

    public int getFavour(final UUID playerUUID, final VaultGodType type) {
        return this.favourStats.getOrDefault(playerUUID, Collections.emptyMap()).getOrDefault(type, 0);
    }

    public void load(final CompoundNBT nbt) {
        this.favourStats.clear();
        for (final String key : nbt.getAllKeys()) {
            UUID playerUUID;
            try {
                playerUUID = UUID.fromString(key);
            } catch (final IllegalArgumentException exc) {
                continue;
            }
            final Map<VaultGodType, Integer> playerFavour = new HashMap<VaultGodType, Integer>();
            final CompoundNBT favourTag = nbt.getCompound(key);
            for (final String godKey : favourTag.getAllKeys()) {
                try {
                    playerFavour.put(VaultGodType.valueOf(godKey), favourTag.getInt(godKey));
                } catch (final IllegalArgumentException ex) {
                }
            }
            this.favourStats.put(playerUUID, playerFavour);
        }
    }

    public CompoundNBT save(final CompoundNBT compound) {
        this.favourStats.forEach((uuid, playerFavour) -> {
            final CompoundNBT favourTag = new CompoundNBT();
            playerFavour.forEach((type, count) -> favourTag.putInt(type.name(), (int) count));
            compound.put(uuid.toString(), (INBT) favourTag);
            return;
        });
        return compound;
    }

    public static PlayerFavourData get(final ServerWorld world) {
        return (PlayerFavourData) world.getServer().overworld().getDataStorage().computeIfAbsent((Supplier) PlayerFavourData::new, "the_vault_PlayerFavour");
    }

    public enum VaultGodType {
        BENEVOLENT("Velara", "The Benevolent", TextFormatting.GREEN),
        OMNISCIENT("Tenos", "The Omniscient", TextFormatting.AQUA),
        TIMEKEEPER("Wendarr", "The Timekeeper", TextFormatting.GOLD),
        MALEVOLENCE("Idona", "The Malevolence", TextFormatting.RED);

        private final String name;
        private final String title;
        private final TextFormatting color;

        private VaultGodType(final String name, final String title, final TextFormatting color) {
            this.name = name;
            this.title = title;
            this.color = color;
        }

        public String getName() {
            return this.name;
        }

        public String getTitle() {
            return this.title;
        }

        public TextFormatting getChatColor() {
            return this.color;
        }

        public ITextComponent getHoverChatComponent() {
            return (ITextComponent) new StringTextComponent("[Vault God] ").withStyle(TextFormatting.WHITE).append((ITextComponent) new StringTextComponent(this.name + ", " + this.title).withStyle(this.color));
        }

        public ITextComponent getIdolDescription() {
            final String s = this.getName().endsWith("s") ? "" : "s";
            return (ITextComponent) new StringTextComponent(String.format("%s'%s Idol", this.getName(), s)).withStyle(this.getChatColor());
        }

        public IFormattableTextComponent getChosenPrefix() {
            final String prefix = "[" + this.getName().charAt(0) + "C] ";
            final IFormattableTextComponent cmp = new StringTextComponent(prefix).withStyle(this.color);
            final String s = this.getName().endsWith("s") ? "" : "s";
            final IFormattableTextComponent hover = new StringTextComponent(String.format("%s'%s Chosen", this.getName(), s)).withStyle(this.getChatColor());
            cmp.withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)));
            return cmp;
        }

        public VaultGodType getOther(final Random rand) {
            int i;
            do {
                i = rand.nextInt(values().length);
            } while (i == this.ordinal());
            return values()[i];
        }
    }
}
