package iskallia.vault.item.crystal;

import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.builder.*;
import iskallia.vault.world.vault.gen.VaultRoomNames;
import iskallia.vault.world.vault.logic.VaultLogic;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.modifier.VaultModifier;
import iskallia.vault.world.vault.modifier.VaultModifiers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class CrystalData implements INBTSerializable<CompoundNBT> {
    public static final CrystalData EMPTY;
    private CompoundNBT delegate;
    protected Type type;
    protected String playerBossName;
    protected List<Modifier> modifiers;
    protected boolean preventsRandomModifiers;
    protected ResourceLocation selectedObjective;
    protected int targetObjectiveCount;
    protected boolean canBeModified;
    protected boolean canTriggerInfluences;
    protected boolean canGenerateTreasureRooms;
    protected List<String> guaranteedRoomFilters;
    protected EchoData echoData;
    protected FrameData frameData;
    protected boolean challenge;

    public CrystalData() {
        this.delegate = new CompoundNBT();
        this.type = Type.CLASSIC;
        this.playerBossName = "";
        this.modifiers = new ArrayList<Modifier>();
        this.preventsRandomModifiers = false;
        this.selectedObjective = null;
        this.targetObjectiveCount = -1;
        this.canBeModified = true;
        this.canTriggerInfluences = true;
        this.canGenerateTreasureRooms = true;
        this.guaranteedRoomFilters = new ArrayList<String>();
    }

    public CrystalData(final ItemStack delegate) {
        this.delegate = new CompoundNBT();
        this.type = Type.CLASSIC;
        this.playerBossName = "";
        this.modifiers = new ArrayList<Modifier>();
        this.preventsRandomModifiers = false;
        this.selectedObjective = null;
        this.targetObjectiveCount = -1;
        this.canBeModified = true;
        this.canTriggerInfluences = true;
        this.canGenerateTreasureRooms = true;
        this.guaranteedRoomFilters = new ArrayList<String>();
        if (delegate != null) {
            this.delegate = delegate.getOrCreateTag();
            this.deserializeNBT(this.delegate.getCompound("CrystalData"));
        }
    }

    public CompoundNBT getDelegate() {
        return this.delegate;
    }

    public void updateDelegate() {
        if (this.delegate != null) {
            this.delegate.put("CrystalData", (INBT) this.serializeNBT());
        }
    }

    public Type getType() {
        return this.type;
    }

    public void setType(final Type type) {
        if (this.type != type) {
            this.type = type;
            this.updateDelegate();
        }
    }

    public String getPlayerBossName() {
        return this.playerBossName;
    }

    public void setPlayerBossName(final String playerBossName) {
        final boolean nameChanged = !StringUtils.equalsIgnoreCase((CharSequence) this.playerBossName, (CharSequence) playerBossName);
        this.playerBossName = playerBossName;
        if (nameChanged) {
            this.updateDelegate();
        }
        if (!playerBossName.isEmpty()) {
            this.setType(Type.RAFFLE);
            this.setSelectedObjective(VaultRaid.SUMMON_AND_KILL_BOSS.get().getId());
        } else if (this.getType() == Type.RAFFLE) {
            this.setType(Type.CLASSIC);
        }
    }

    public boolean canModifyWithCrafting() {
        if (!this.canBeModified()) {
            return false;
        }
        final List<String> modifierNames = this.getModifiers().stream().map(Modifier::getModifierName).collect(Collectors.toList());
        return !modifierNames.contains("Afterlife") && !Vault.id("raid_challenge").equals(this.getSelectedObjective()) && !Vault.id("cake_hunt").equals(this.getSelectedObjective()) && this.getType().canCraft();
    }

    public boolean canAddModifier(final String name, final Modifier.Operation operation) {
        return this.canBeModified() && this.getModifiers().stream().noneMatch(mod -> name.equals(mod.name));
    }

    public boolean addCatalystModifier(final String name, final boolean preventsRandomModifiers, final Modifier.Operation operation) {
        if (!this.canAddModifier(name, operation)) {
            return false;
        }
        this.addModifier(name, operation);
        if (preventsRandomModifiers) {
            this.setPreventsRandomModifiers(true);
        }
        return true;
    }

    public void addModifier(final String name) {
        this.addModifier(name, Modifier.Operation.ADD);
    }

    public void addModifier(final String name, final Modifier.Operation operation) {
        this.modifiers.add(new Modifier(name, operation));
        this.updateDelegate();
    }

    public boolean canRemoveModifier(final String name, final Modifier.Operation operation) {
        return this.canBeModified() && this.getModifiers().stream().anyMatch(mod -> name.equals(mod.name));
    }

    public boolean removeCatalystModifier(final String name, final boolean preventsRandomModifiers, final Modifier.Operation operation) {
        if (!this.canRemoveModifier(name, operation)) {
            return false;
        }
        this.removeModifier(name, operation);
        if (preventsRandomModifiers) {
            this.setPreventsRandomModifiers(true);
        }
        return true;
    }

    public void removeModifier(final String name, final Modifier.Operation operation) {
        final Modifier modifier = this.modifiers.stream().filter(mod -> mod.name.equals(name)).findFirst().orElse(null);
        if (modifier == null) {
            return;
        }
        this.modifiers.remove(modifier);
        this.updateDelegate();
    }

    public List<Modifier> getModifiers() {
        return Collections.unmodifiableList((List<? extends Modifier>) this.modifiers);
    }

    public void clearModifiers() {
        this.modifiers.clear();
        this.updateDelegate();
    }

    public boolean canAddRoom(final String roomKey) {
        return !VaultRaid.ARCHITECT_EVENT.get().getId().equals(this.getSelectedObjective());
    }

    public void addGuaranteedRoom(final String roomKey) {
        this.addGuaranteedRoom(roomKey, 1);
    }

    public void addGuaranteedRoom(final String roomKey, final int amount) {
        for (int i = 0; i < amount; ++i) {
            this.guaranteedRoomFilters.add(roomKey);
        }
        this.updateDelegate();
    }

    public List<String> getGuaranteedRoomFilters() {
        return Collections.unmodifiableList((List<? extends String>) this.guaranteedRoomFilters);
    }

    public boolean preventsRandomModifiers() {
        return !this.canBeModified() || this.preventsRandomModifiers || !this.getType().canGenerateRandomModifiers();
    }

    public void setPreventsRandomModifiers(final boolean preventsRandomModifiers) {
        this.preventsRandomModifiers = preventsRandomModifiers;
        this.updateDelegate();
    }

    public boolean canTriggerInfluences() {
        return this.canTriggerInfluences;
    }

    public void setCanTriggerInfluences(final boolean canTriggerInfluences) {
        this.canTriggerInfluences = canTriggerInfluences;
        this.updateDelegate();
    }

    public boolean canGenerateTreasureRooms() {
        return this.canGenerateTreasureRooms;
    }

    public void setCanGenerateTreasureRooms(final boolean canGenerateTreasureRooms) {
        this.canGenerateTreasureRooms = canGenerateTreasureRooms;
        this.updateDelegate();
    }
    public boolean isChallenge() {
        return this.challenge;
    }

    public void setChallenge(final boolean challenge) {
        this.challenge = challenge;
        this.updateDelegate();
    }

    public boolean canBeModified() {
        return this.canBeModified;
    }

    public void setModifiable(final boolean modifiable) {
        this.canBeModified = modifiable;
        this.updateDelegate();
    }

    public void setSelectedObjective(final ResourceLocation selectedObjective) {
        if (!Objects.equals(this.selectedObjective, selectedObjective)) {
            this.selectedObjective = selectedObjective;
            this.updateDelegate();
        }
    }

    @Nullable
    public ResourceLocation getSelectedObjective() {
        return this.selectedObjective;
    }

    public void setTargetObjectiveCount(final int targetObjectiveCount) {
        this.targetObjectiveCount = targetObjectiveCount;
        this.updateDelegate();
    }

    public int getTargetObjectiveCount() {
        return this.targetObjectiveCount;
    }

    public void apply(final VaultRaid vault, final Random random) {
        this.modifiers.forEach(modifier -> modifier.apply(vault.getModifiers(), random));
    }

    public VaultRaid.Builder createVault(final ServerWorld world, final ServerPlayerEntity player) {
        return this.getType().getVaultBuilder().initializeBuilder(world, player, this);
    }

    public static boolean shouldForceCowVault(final CrystalData data) {
        final List<String> requiredModifiers = Arrays.asList("hoard", "hunger", "raging");
        final List<Modifier> existingModifiers = data.getModifiers();
        return existingModifiers.size() == 3 && existingModifiers.stream().allMatch(modifier -> requiredModifiers.contains(modifier.getModifierName().toLowerCase()));
    }

    public EchoData getEchoData() {
        if (this.echoData == null) {
            this.echoData = new EchoData(0);
        }
        return this.echoData;
    }
    public FrameData getFrameData() {
        return this.frameData;
    }

    public int addEchoGems(final int amount) {
        final int remainder = this.getEchoData().addEchoGems(amount);
        this.updateDelegate();
        return remainder;
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        final Type crystalType = this.getType();
        if (crystalType.showTypePrefix()) {
            tooltip.add((ITextComponent) new StringTextComponent("Type: ").append(this.getType().getDisplayName()));
        } else {
            tooltip.add(this.getType().getDisplayName());
        }
        if (crystalType.showObjective()) {
            final ResourceLocation objectiveKey = this.getSelectedObjective();
            ITextComponent objectiveCountDescription = null;
            ITextComponent objective;
            if (objectiveKey == null) {
                objective = (ITextComponent) new StringTextComponent("???").withStyle(TextFormatting.GRAY);
            } else {
                VaultRaid.ARCHITECT_EVENT.get();
                final VaultObjective vObjective = VaultObjective.getObjective(objectiveKey);
                if (vObjective == null) {
                    objective = (ITextComponent) new StringTextComponent("???").withStyle(TextFormatting.GRAY);
                } else {
                    objective = vObjective.getObjectiveDisplayName();
                    if (this.targetObjectiveCount >= 0) {
                        objectiveCountDescription = vObjective.getObjectiveTargetDescription(this.targetObjectiveCount);
                    }
                }
            }
            tooltip.add((ITextComponent) new StringTextComponent("Objective: ").append(objective));
            if (objectiveCountDescription != null) {
                tooltip.add(objectiveCountDescription);
            }
        }
        if (!this.getPlayerBossName().isEmpty()) {
            tooltip.add((ITextComponent) new StringTextComponent("Player Boss: ").append((ITextComponent) new StringTextComponent(this.getPlayerBossName()).withStyle(TextFormatting.GREEN)));
        }
        final Map<String, Integer> collapsedFilters = new HashMap<String, Integer>();
        String roomFilter = null;
        int count = 0;
        for (String guaranteedRoomFilter : this.guaranteedRoomFilters) {
            roomFilter = guaranteedRoomFilter;
            count = collapsedFilters.getOrDefault(roomFilter, 0);
            collapsedFilters.put(roomFilter, ++count);
        }
        collapsedFilters.forEach((roomFilter1, count1) -> {
            final ITextComponent roomName = VaultRoomNames.getName(roomFilter1);
            if (roomName == null) {
                return;
            } else {
                final String roomStr = (count1 > 1) ? "Rooms" : "Room";

                final StringTextComponent stringTextComponent = new StringTextComponent(" " + roomStr);
                final IFormattableTextComponent formattableTextComponent = new StringTextComponent("- Has ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(String.valueOf(count1)).withStyle(TextFormatting.GOLD)).append(" ").append(roomName);

                final ITextComponent txt3 = formattableTextComponent.append(stringTextComponent.withStyle(TextFormatting.GRAY));
                tooltip.add(txt3);
                return;
            }
        });
        for (final Modifier modifier : this.modifiers) {
            final StringTextComponent modifierName = new StringTextComponent(modifier.name);
            final VaultModifier vModifier = ModConfigs.VAULT_MODIFIERS.getByName(modifier.name);
            if (vModifier != null) {
                modifierName.setStyle(Style.EMPTY.withColor(Color.fromRgb(vModifier.getColor())));
            }
            final ITextComponent type = (ITextComponent) new StringTextComponent(modifier.operation.title).withStyle(modifier.operation.color);
            tooltip.add((ITextComponent) new StringTextComponent("- ").append(type).append(" ").append((ITextComponent) modifierName));
            if (Screen.hasShiftDown() && vModifier != null) {
                final ITextComponent description = (ITextComponent) new StringTextComponent("   " + vModifier.getDescription()).withStyle(TextFormatting.DARK_GRAY);
                tooltip.add(description);
            }
        }
        if (this.getEchoData().getEchoCount() > 0) {
            final int count2 = this.getEchoData().getEchoCount();
            final StringTextComponent txt = new StringTextComponent("Echoed");
            txt.setStyle(Style.EMPTY.withColor(Color.fromRgb(2491465)));
            tooltip.add((ITextComponent) new StringTextComponent("- ").append((ITextComponent) txt));
            if (Screen.hasShiftDown()) {
                final ITextComponent description2 = (ITextComponent) new StringTextComponent("   " + count2 + "% Echo Rate").withStyle(TextFormatting.DARK_GRAY);
                tooltip.add(description2);
            }
        }
        if (!this.canBeModified()) {
            final StringTextComponent txt2 = new StringTextComponent("Exhausted");
            txt2.setStyle(Style.EMPTY.withColor(Color.fromRgb(3084959)));
            tooltip.add((ITextComponent) new StringTextComponent("- ").append((ITextComponent) txt2));
            if (Screen.hasShiftDown()) {
                final ITextComponent description3 = (ITextComponent) new StringTextComponent("   Crystal can not be modified.").withStyle(TextFormatting.DARK_GRAY);
                tooltip.add(description3);
            }
        }
        if (this.delegate.getBoolean("Cloned")) {
            final StringTextComponent txt2 = new StringTextComponent("Cloned");
            txt2.setStyle(Style.EMPTY.withColor(Color.fromRgb(2491465)));
            tooltip.add((ITextComponent) new StringTextComponent("- ").append((ITextComponent) txt2));
            if (Screen.hasShiftDown()) {
                final ITextComponent description3 = (ITextComponent) new StringTextComponent("   Crystal has been cloned with an Echoed Crystal.").withStyle(TextFormatting.DARK_GRAY);
                tooltip.add(description3);
            }
        }
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Type", this.type.name());
        nbt.putString("PlayerBossName", this.playerBossName);
        final ListNBT modifiersList = new ListNBT();
        this.modifiers.forEach(modifier -> modifiersList.add(modifier.toNBT()));
        nbt.put("Modifiers", (INBT) modifiersList);
        nbt.putBoolean("preventsRandomModifiers", this.preventsRandomModifiers);
        nbt.putBoolean("canBeModified", this.canBeModified);
        nbt.putBoolean("canTriggerInfluences", this.canTriggerInfluences);
        nbt.putBoolean("canGenerateTreasureRooms", this.canGenerateTreasureRooms);
        if (this.selectedObjective != null) {
            nbt.putString("Objective", this.selectedObjective.toString());
        }
        nbt.putInt("targetObjectiveCount", this.targetObjectiveCount);
        nbt.put("echoData", (INBT) this.getEchoData().toNBT());
        final ListNBT roomList = new ListNBT();
        this.guaranteedRoomFilters.forEach(roomKey -> roomList.add(StringNBT.valueOf(roomKey)));
        if (this.frameData != null) {
            nbt.put("Frame", (INBT)this.frameData.serializeNBT());
        }
        nbt.putBoolean("Challenge", this.challenge);
        nbt.put("rooms", (INBT) roomList);
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.type = (nbt.contains("Type", 8) ? Enum.valueOf(Type.class, nbt.getString("Type")) : Type.CLASSIC);
        if (this.type == Type.COOP) {
            this.type = Type.CLASSIC;
        }
        this.playerBossName = nbt.getString("PlayerBossName");
        final ListNBT modifiersList = nbt.getList("Modifiers", 10);
        modifiersList.forEach(inbt -> this.modifiers.add(Modifier.fromNBT((CompoundNBT) inbt)));
        this.migrateModifiers(this.modifiers);
        this.preventsRandomModifiers = (nbt.contains("preventsRandomModifiers", 1) ? nbt.getBoolean("preventsRandomModifiers") : (!this.modifiers.isEmpty()));
        this.canBeModified = (!nbt.contains("canBeModified", 1) || nbt.getBoolean("canBeModified"));
        this.canTriggerInfluences = (!nbt.contains("canTriggerInfluences", 1) || nbt.getBoolean("canTriggerInfluences"));
        this.canGenerateTreasureRooms = (!nbt.contains("canGenerateTreasureRooms", 1) || nbt.getBoolean("canGenerateTreasureRooms"));
        this.selectedObjective = null;
        if (nbt.contains("Objective", 8)) {
            this.selectedObjective = new ResourceLocation(nbt.getString("Objective"));
        }
        this.targetObjectiveCount = (nbt.contains("targetObjectiveCount", 3) ? nbt.getInt("targetObjectiveCount") : -1);
        if (nbt.contains("echoData")) {
            this.echoData = EchoData.fromNBT(nbt.getCompound("echoData"));
        }
        final ListNBT roomList = nbt.getList("rooms", 8);
        roomList.forEach(inbt -> this.guaranteedRoomFilters.add(this.migrateRoomName(inbt.getAsString())));
        this.frameData = FrameData.fromNBT(nbt.getCompound("Frame"));
        this.challenge = nbt.getBoolean("Challenge");
    }

    private void migrateModifiers(final List<Modifier> modifiers) {
        modifiers.forEach(modifier -> modifier.name = VaultModifier.migrateModifierName(modifier.name));
    }

    private String migrateRoomName(String roomName) {
        if (roomName.equalsIgnoreCase("contest")) {
            roomName = "contest_tree";
        }
        return roomName;
    }

    static {
        EMPTY = new EmptyCrystalData();
    }

    public enum Type {
        CLASSIC(VaultLogic.COOP, (VaultRaidBuilder)CoopVaultBuilder.getInstance(), "Normal"),
        RAFFLE(VaultLogic.RAFFLE, (VaultRaidBuilder)RaffleVaultBuilder.getInstance(), "Raffle"),
        COOP(VaultLogic.COOP, (VaultRaidBuilder)CoopVaultBuilder.getInstance(), "Cooperative"),
        TROVE(VaultLogic.CLASSIC, (VaultRaidBuilder)TroveVaultBuilder.getInstance(), "Vault Trove", TextFormatting.GOLD),
        BOSS_BENEVOLENT_PREP("Velara's Sacrifice", PlayerFavourData.VaultGodType.BENEVOLENT.getChatColor()),
        BOSS_BENEVOLENT("Velara's Demand", PlayerFavourData.VaultGodType.BENEVOLENT.getChatColor()),
        BOSS_OMNISCIENT("Tenos' Oblivion", PlayerFavourData.VaultGodType.OMNISCIENT.getChatColor()),
        BOSS_TIMEKEEPER("Wendarr's Transience", PlayerFavourData.VaultGodType.TIMEKEEPER.getChatColor()),
        BOSS_MALEVOLENCE("Idona's Wrath", PlayerFavourData.VaultGodType.MALEVOLENCE.getChatColor()),
        FINAL_LOBBY(VaultLogic.FINAL_LOBBY, (VaultRaidBuilder)FinalLobbyBuilder.getInstance(), "Final Vault", TextFormatting.DARK_PURPLE),
        FINAL_BOSS(VaultLogic.FINAL_BOSS, (VaultRaidBuilder)FinalBossBuilder.getInstance(), "Final Vault - Boss", TextFormatting.DARK_PURPLE),
        FINAL_VELARA(VaultLogic.COOP, (VaultRaidBuilder)CoopVaultBuilder.getInstance(), "Final Velara Challenge", TextFormatting.GREEN),
        FINAL_TENOS(VaultLogic.COOP, (VaultRaidBuilder)CoopVaultBuilder.getInstance(), "Final Tenos Challenge", TextFormatting.AQUA),
        FINAL_WENDARR(VaultLogic.COOP, (VaultRaidBuilder)CoopVaultBuilder.getInstance(), "Final Wendarr Challenge", TextFormatting.GOLD),
        FINAL_IDONA(VaultLogic.COOP, (VaultRaidBuilder)CoopVaultBuilder.getInstance(), "Final Idona Challenge", TextFormatting.RED);


        private final VaultLogic logic;
        private final VaultRaidBuilder vaultBuilder;
        private final String name;
        private final TextFormatting color;

        private Type(final String name) {
            this(VaultLogic.CLASSIC, ClassicVaultBuilder.getInstance(), name, TextFormatting.GOLD);
        }

        private Type(final String name, final TextFormatting color) {
            this(VaultLogic.CLASSIC, ClassicVaultBuilder.getInstance(), name, color);
        }

        private Type(final VaultLogic logic, final VaultRaidBuilder vaultBuilder, final String name) {
            this(logic, vaultBuilder, name, TextFormatting.GOLD);
        }

        private Type(final VaultLogic logic, final VaultRaidBuilder vaultBuilder, final String name, final TextFormatting color) {
            this.logic = logic;
            this.vaultBuilder = vaultBuilder;
            this.name = name;
            this.color = color;
        }

        public boolean canCraft() {
            return this == Type.CLASSIC;
        }

        public boolean showTypePrefix() {
            return this == Type.CLASSIC || this == Type.RAFFLE;
        }

        public boolean showObjective() {
            return this == Type.CLASSIC || this == Type.RAFFLE;
        }

        public boolean visibleInCreative() {
            return this == Type.CLASSIC || this == Type.RAFFLE || this == Type.TROVE || this == Type.FINAL_LOBBY;
        }

        public boolean canBeCowVault() {
            return this == Type.CLASSIC || this == Type.RAFFLE;
        }

        public boolean canGenerateRandomModifiers() {
            return this == Type.CLASSIC || this == Type.RAFFLE || this == Type.COOP || this == Type.FINAL_VELARA || this == Type.FINAL_IDONA || this == Type.FINAL_WENDARR || this == Type.FINAL_TENOS;
        }

        public boolean isFinalType() {
            return this == Type.FINAL_BOSS || this == Type.FINAL_IDONA || this == Type.FINAL_TENOS || this == Type.FINAL_WENDARR || this == Type.FINAL_VELARA;
        }

        public boolean canTriggerInfluences() {
            return this == Type.CLASSIC;
        }

        public VaultLogic getLogic() {
            return this.logic;
        }

        public VaultRaidBuilder getVaultBuilder() {
            return this.vaultBuilder;
        }

        public ITextComponent getDisplayName() {
            return (ITextComponent) new StringTextComponent(this.name).withStyle(this.color);
        }
    }

    public static class Modifier {
        public String name;
        public final Operation operation;

        public Modifier(final String name, final Operation operation) {
            this.name = name;
            this.operation = operation;
        }

        public void apply(final VaultModifiers modifiers, final Random random) {
            if (this.operation == Operation.ADD) {
                modifiers.addPermanentModifier(this.name);
            }
        }

        public CompoundNBT toNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Name", this.name);
            nbt.putInt("Operation", this.operation.ordinal());
            return nbt;
        }

        public static Modifier fromNBT(final CompoundNBT nbt) {
            return new Modifier(nbt.getString("Name"), Operation.values()[nbt.getInt("Operation")]);
        }

        public String getModifierName() {
            return this.name;
        }

        public Operation getOperation() {
            return this.operation;
        }

        public enum Operation {
            ADD("Has", TextFormatting.GRAY);

            private final String title;
            private final TextFormatting color;

            private Operation(final String title, final TextFormatting color) {
                this.title = title;
                this.color = color;
            }
        }
    }

    private static class EmptyCrystalData extends CrystalData {
        @Override
        public boolean addCatalystModifier(final String name, final boolean preventsRandomModifiers, final Modifier.Operation operation) {
            return false;
        }

        @Override
        public boolean canAddModifier(final String name, final Modifier.Operation operation) {
            return false;
        }

        @Override
        public boolean preventsRandomModifiers() {
            return false;
        }

        @Override
        public void setType(final Type type) {
        }

        @Override
        public void setPlayerBossName(final String playerBossName) {
        }

        @Override
        public void setSelectedObjective(final ResourceLocation selectedObjective) {
        }

        @Override
        public VaultRaid.Builder createVault(final ServerWorld world, final ServerPlayerEntity player) {
            return null;
        }
    }

    public static class EchoData {
        int echoCount;

        public EchoData(final int echoCount) {
            this.echoCount = echoCount;
        }

        public int getEchoCount() {
            return this.echoCount;
        }

        public int addEchoGems(final int amount) {
            if (this.echoCount >= 100) {
                return amount;
            }
            for (int i = amount; i > 0; --i) {
                if (this.echoCount >= 100) {
                    return i;
                }
                if (MathUtilities.randomFloat(0.0f, 1.0f) < this.getEchoSuccessRate()) {
                    ++this.echoCount;
                }
            }
            return 0;
        }

        public float getCloneSuccessRate() {
            return this.echoCount / 100.0f;
        }

        public float getEchoSuccessRate() {
            return (100 - this.echoCount) / 100.0f;
        }

        public CompoundNBT toNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("echoCount", this.echoCount);
            return nbt;
        }

        public static EchoData fromNBT(final CompoundNBT nbt) {
            return new EchoData(nbt.getInt("echoCount"));
        }
    }
}
