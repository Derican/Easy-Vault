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


public class PlayerFavourData
        extends WorldSavedData {
    protected static final String DATA_NAME = "the_vault_PlayerFavour";
    protected Map<UUID, Map<VaultGodType, Integer>> favourStats = new HashMap<>();

    public PlayerFavourData() {
        this("the_vault_PlayerFavour");
    }

    public PlayerFavourData(String name) {
        super(name);
    }

    public boolean addFavour(PlayerEntity player, VaultGodType type, int count) {
        UUID playerUUID = player.getUUID();

        int favour = ((Integer) ((Map) this.favourStats.computeIfAbsent(playerUUID, key -> new HashMap<>())).getOrDefault(type, Integer.valueOf(0))).intValue();
        if (Math.abs(favour + count) > 16) {
            return false;
        }

        favour += count;
        ((Map<VaultGodType, Integer>) this.favourStats.computeIfAbsent(playerUUID, key -> new HashMap<>()))
                .put(type, Integer.valueOf(favour));
        setDirty();
        return true;
    }

    public int getFavour(UUID playerUUID, VaultGodType type) {
        return ((Integer) ((Map) this.favourStats.getOrDefault(playerUUID, Collections.emptyMap()))
                .getOrDefault(type, Integer.valueOf(0))).intValue();
    }


    public void load(CompoundNBT nbt) {
        this.favourStats.clear();

        for (String key : nbt.getAllKeys()) {
            UUID playerUUID;
            try {
                playerUUID = UUID.fromString(key);
            } catch (IllegalArgumentException exc) {
                continue;
            }


            Map<VaultGodType, Integer> playerFavour = new HashMap<>();
            CompoundNBT favourTag = nbt.getCompound(key);
            for (String godKey : favourTag.getAllKeys()) {
                try {
                    playerFavour.put(VaultGodType.valueOf(godKey), Integer.valueOf(favourTag.getInt(godKey)));
                } catch (IllegalArgumentException illegalArgumentException) {
                }
            }

            this.favourStats.put(playerUUID, playerFavour);
        }
    }


    public CompoundNBT save(CompoundNBT compound) {
        this.favourStats.forEach((uuid, playerFavour) -> {
            CompoundNBT favourTag = new CompoundNBT();
            playerFavour.forEach(());
            compound.put(uuid.toString(), (INBT) favourTag);
        });
        return compound;
    }

    public static PlayerFavourData get(ServerWorld world) {
        return (PlayerFavourData) world.getServer().overworld().getDataStorage().computeIfAbsent(PlayerFavourData::new, "the_vault_PlayerFavour");
    }

    public enum VaultGodType {
        BENEVOLENT("Velara", "The Benevolent", (String) TextFormatting.GREEN),
        OMNISCIENT("Tenos", "The Omniscient", (String) TextFormatting.AQUA),
        TIMEKEEPER("Wendarr", "The Timekeeper", (String) TextFormatting.GOLD),
        MALEVOLENCE("Idona", "The Malevolence", (String) TextFormatting.RED);

        private final String name;
        private final String title;
        private final TextFormatting color;

        VaultGodType(String name, String title, TextFormatting color) {
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
            return (ITextComponent) (new StringTextComponent("[Vault God] ")).withStyle(TextFormatting.WHITE)
                    .append((ITextComponent) (new StringTextComponent(this.name + ", " + this.title)).withStyle(this.color));
        }

        public ITextComponent getIdolDescription() {
            String s = getName().endsWith("s") ? "" : "s";
            return (ITextComponent) (new StringTextComponent(String.format("%s'%s Idol", new Object[]{getName(), s}))).withStyle(getChatColor());
        }

        public IFormattableTextComponent getChosenPrefix() {
            String prefix = "[" + getName().charAt(0) + "C] ";
            IFormattableTextComponent cmp = (new StringTextComponent(prefix)).withStyle(this.color);

            String s = getName().endsWith("s") ? "" : "s";

            IFormattableTextComponent hover = (new StringTextComponent(String.format("%s'%s Chosen", new Object[]{getName(), s}))).withStyle(getChatColor());
            cmp.withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover)));
            return cmp;
        }


        public VaultGodType getOther(Random rand) {
            while (true) {
                int i = rand.nextInt((values()).length);
                if (i != ordinal())
                    return values()[i];
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\data\PlayerFavourData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */