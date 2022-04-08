package iskallia.vault.item.crystal;

import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.builder.*;
import iskallia.vault.world.vault.gen.VaultRoomNames;
import iskallia.vault.world.vault.logic.VaultLogic;
import iskallia.vault.world.vault.logic.objective.SummonAndKillBossObjective;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
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
    public static final CrystalData EMPTY = new EmptyCrystalData();

    private CompoundNBT delegate = new CompoundNBT();

    protected Type type = Type.CLASSIC;
    protected String playerBossName = "";
    protected List<Modifier> modifiers = new ArrayList<>();
    protected boolean preventsRandomModifiers = false;
    protected ResourceLocation selectedObjective = null;
    protected int targetObjectiveCount = -1;
    protected boolean canBeModified = true;
    protected boolean canTriggerInfluences = true;
    protected boolean canGenerateTreasureRooms = true;
    protected List<String> guaranteedRoomFilters = new ArrayList<>();


    protected EchoData echoData;


    public CrystalData(ItemStack delegate) {
        if (delegate != null) {
            this.delegate = delegate.getOrCreateTag();
            deserializeNBT(this.delegate.getCompound("CrystalData"));
        }
    }

    public CompoundNBT getDelegate() {
        return this.delegate;
    }

    public void updateDelegate() {
        if (this.delegate != null) {
            this.delegate.put("CrystalData", (INBT) serializeNBT());
        }
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        if (this.type != type) {
            this.type = type;
            updateDelegate();
        }
    }

    public String getPlayerBossName() {
        return this.playerBossName;
    }

    public void setPlayerBossName(String playerBossName) {
        boolean nameChanged = !StringUtils.equalsIgnoreCase(this.playerBossName, playerBossName);
        this.playerBossName = playerBossName;
        if (nameChanged) {
            updateDelegate();
        }
        if (!playerBossName.isEmpty()) {
            setType(Type.RAFFLE);
            setSelectedObjective(((SummonAndKillBossObjective) VaultRaid.SUMMON_AND_KILL_BOSS.get()).getId());
        } else if (getType() == Type.RAFFLE) {
            setType(Type.CLASSIC);
        }
    }

    public boolean canModifyWithCrafting() {
        if (!canBeModified()) {
            return false;
        }


        List<String> modifierNames = (List<String>) getModifiers().stream().map(Modifier::getModifierName).collect(Collectors.toList());
        if (modifierNames.contains("Afterlife")) {
            return false;
        }
        if (Vault.id("raid_challenge").equals(getSelectedObjective())) {
            return false;
        }
        if (Vault.id("cake_hunt").equals(getSelectedObjective())) {
            return false;
        }
        return getType().canCraft();
    }

    public boolean canAddModifier(String name, Modifier.Operation operation) {
        return (canBeModified() && getModifiers().stream().noneMatch(mod -> name.equals(mod.name)));
    }

    public boolean addCatalystModifier(String name, boolean preventsRandomModifiers, Modifier.Operation operation) {
        if (!canAddModifier(name, operation)) {
            return false;
        }

        addModifier(name, operation);
        if (preventsRandomModifiers) {
            setPreventsRandomModifiers(true);
        }
        return true;
    }

    public void addModifier(String name) {
        addModifier(name, Modifier.Operation.ADD);
    }

    public void addModifier(String name, Modifier.Operation operation) {
        this.modifiers.add(new Modifier(name, operation));
        updateDelegate();
    }

    public boolean canRemoveModifier(String name, Modifier.Operation operation) {
        return (canBeModified() && getModifiers().stream().anyMatch(mod -> name.equals(mod.name)));
    }

    public boolean removeCatalystModifier(String name, boolean preventsRandomModifiers, Modifier.Operation operation) {
        if (!canRemoveModifier(name, operation)) {
            return false;
        }

        removeModifier(name, operation);
        if (preventsRandomModifiers) {
            setPreventsRandomModifiers(true);
        }
        return true;
    }

    public void removeModifier(String name, Modifier.Operation operation) {
        Modifier modifier = this.modifiers.stream().filter(mod -> mod.name.equals(name)).findFirst().orElse(null);
        if (modifier == null)
            return;
        this.modifiers.remove(modifier);
        updateDelegate();
    }

    public List<Modifier> getModifiers() {
        return Collections.unmodifiableList(this.modifiers);
    }

    public void clearModifiers() {
        this.modifiers.clear();
        updateDelegate();
    }

    public boolean canAddRoom(String roomKey) {
        if (((ArchitectObjective) VaultRaid.ARCHITECT_EVENT.get()).getId().equals(getSelectedObjective())) {
            return false;
        }
        return true;
    }

    public void addGuaranteedRoom(String roomKey) {
        addGuaranteedRoom(roomKey, 1);
    }

    public void addGuaranteedRoom(String roomKey, int amount) {
        for (int i = 0; i < amount; i++) {
            this.guaranteedRoomFilters.add(roomKey);
        }
        updateDelegate();
    }

    public List<String> getGuaranteedRoomFilters() {
        return Collections.unmodifiableList(this.guaranteedRoomFilters);
    }

    public boolean preventsRandomModifiers() {
        if (!canBeModified()) {
            return true;
        }
        return (this.preventsRandomModifiers || !getType().canGenerateRandomModifiers());
    }

    public void setPreventsRandomModifiers(boolean preventsRandomModifiers) {
        this.preventsRandomModifiers = preventsRandomModifiers;
        updateDelegate();
    }

    public boolean canTriggerInfluences() {
        return this.canTriggerInfluences;
    }

    public void setCanTriggerInfluences(boolean canTriggerInfluences) {
        this.canTriggerInfluences = canTriggerInfluences;
        updateDelegate();
    }

    public boolean canGenerateTreasureRooms() {
        return this.canGenerateTreasureRooms;
    }

    public void setCanGenerateTreasureRooms(boolean canGenerateTreasureRooms) {
        this.canGenerateTreasureRooms = canGenerateTreasureRooms;
        updateDelegate();
    }

    public boolean canBeModified() {
        return this.canBeModified;
    }

    public void setModifiable(boolean modifiable) {
        this.canBeModified = modifiable;
        updateDelegate();
    }

    public void setSelectedObjective(ResourceLocation selectedObjective) {
        if (!Objects.equals(this.selectedObjective, selectedObjective)) {
            this.selectedObjective = selectedObjective;
            updateDelegate();
        }
    }

    @Nullable
    public ResourceLocation getSelectedObjective() {
        return this.selectedObjective;
    }

    public void setTargetObjectiveCount(int targetObjectiveCount) {
        this.targetObjectiveCount = targetObjectiveCount;
        updateDelegate();
    }

    public int getTargetObjectiveCount() {
        return this.targetObjectiveCount;
    }

    public void apply(VaultRaid vault, Random random) {
        this.modifiers.forEach(modifier -> modifier.apply(vault.getModifiers(), random));
    }

    public VaultRaid.Builder createVault(ServerWorld world, ServerPlayerEntity player) {
        return getType().getVaultBuilder().initializeBuilder(world, player, this);
    }

    public static boolean shouldForceCowVault(CrystalData data) {
        List<String> requiredModifiers = Arrays.asList(new String[]{"hoard", "hunger", "raging"});
        List<Modifier> existingModifiers = data.getModifiers();
        return (existingModifiers.size() == 3 && existingModifiers.stream().allMatch(modifier -> requiredModifiers.contains(modifier.getModifierName().toLowerCase())));
    }

    public EchoData getEchoData() {
        if (this.echoData == null) this.echoData = new EchoData(0);
        return this.echoData;
    }

    public int addEchoGems(int amount) {
        int remainder = getEchoData().addEchoGems(amount);
        updateDelegate();

        return remainder;
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        Type crystalType = getType();
        if (crystalType.showTypePrefix()) {
            tooltip.add((new StringTextComponent("Type: ")).append(getType().getDisplayName()));
        } else {
            tooltip.add(getType().getDisplayName());
        }

        if (crystalType.showObjective()) {
            ITextComponent objective = null;
            ResourceLocation objectiveKey = getSelectedObjective();
            ITextComponent objectiveCountDescription = null;

            if (objectiveKey == null) {
                IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("???")).withStyle(TextFormatting.GRAY);
            } else {

                VaultRaid.ARCHITECT_EVENT.get();
                VaultObjective vObjective = VaultObjective.getObjective(objectiveKey);
                if (vObjective == null) {
                    IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("???")).withStyle(TextFormatting.GRAY);
                } else {
                    objective = vObjective.getObjectiveDisplayName();
                    if (this.targetObjectiveCount >= 0) {
                        objectiveCountDescription = vObjective.getObjectiveTargetDescription(this.targetObjectiveCount);
                    }
                }
            }
            tooltip.add((new StringTextComponent("Objective: ")).append(objective));
            if (objectiveCountDescription != null) {
                tooltip.add(objectiveCountDescription);
            }
        }

        if (!getPlayerBossName().isEmpty()) {
            tooltip.add((new StringTextComponent("Player Boss: "))
                    .append((ITextComponent) (new StringTextComponent(getPlayerBossName())).withStyle(TextFormatting.GREEN)));
        }

        Map<String, Integer> collapsedFilters = new HashMap<>();
        for (String roomFilter : this.guaranteedRoomFilters) {
            int count = ((Integer) collapsedFilters.getOrDefault(roomFilter, Integer.valueOf(0))).intValue();
            collapsedFilters.put(roomFilter, Integer.valueOf(++count));
        }
        collapsedFilters.forEach((roomFilter, count) -> {
            ITextComponent roomName = VaultRoomNames.getName(roomFilter);

            if (roomName == null) {
                return;
            }

            String roomStr = (count.intValue() > 1) ? "Rooms" : "Room";

            IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("- Has ")).withStyle(TextFormatting.GRAY).append((ITextComponent) (new StringTextComponent(String.valueOf(count))).withStyle(TextFormatting.GOLD)).append(" ").append(roomName).append((ITextComponent) (new StringTextComponent(" " + roomStr)).withStyle(TextFormatting.GRAY));

            tooltip.add(iFormattableTextComponent);
        });

        for (Modifier modifier : this.modifiers) {
            StringTextComponent modifierName = new StringTextComponent(modifier.name);
            VaultModifier vModifier = ModConfigs.VAULT_MODIFIERS.getByName(modifier.name);
            if (vModifier != null) {
                modifierName.setStyle(Style.EMPTY.withColor(Color.fromRgb(vModifier.getColor())));
            }
            IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent(modifier.operation.title)).withStyle(modifier.operation.color);

            tooltip.add((new StringTextComponent("- ")).append((ITextComponent) iFormattableTextComponent).append(" ").append((ITextComponent) modifierName));
            if (Screen.hasShiftDown() && vModifier != null) {
                IFormattableTextComponent iFormattableTextComponent1 = (new StringTextComponent("   " + vModifier.getDescription())).withStyle(TextFormatting.DARK_GRAY);
                tooltip.add(iFormattableTextComponent1);
            }
        }

        if (getEchoData().getEchoCount() > 0) {
            int count = getEchoData().getEchoCount();
            StringTextComponent txt = new StringTextComponent("Echoed");
            txt.setStyle(Style.EMPTY.withColor(Color.fromRgb(2491465)));
            tooltip.add((new StringTextComponent("- ")).append((ITextComponent) txt));
            if (Screen.hasShiftDown()) {
                IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("   " + count + "% Echo Rate")).withStyle(TextFormatting.DARK_GRAY);
                tooltip.add(iFormattableTextComponent);
            }
        }

        if (!canBeModified()) {
            StringTextComponent txt = new StringTextComponent("Exhausted");
            txt.setStyle(Style.EMPTY.withColor(Color.fromRgb(3084959)));
            tooltip.add((new StringTextComponent("- ")).append((ITextComponent) txt));
            if (Screen.hasShiftDown()) {
                IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("   Crystal can not be modified.")).withStyle(TextFormatting.DARK_GRAY);
                tooltip.add(iFormattableTextComponent);
            }
        }

        if (this.delegate.getBoolean("Cloned")) {
            StringTextComponent txt = new StringTextComponent("Cloned");
            txt.setStyle(Style.EMPTY.withColor(Color.fromRgb(2491465)));
            tooltip.add((new StringTextComponent("- ")).append((ITextComponent) txt));
            if (Screen.hasShiftDown()) {
                IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("   Crystal has been cloned with an Echoed Crystal.")).withStyle(TextFormatting.DARK_GRAY);
                tooltip.add(iFormattableTextComponent);
            }
        }
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putString("Type", this.type.name());
        nbt.putString("PlayerBossName", this.playerBossName);

        ListNBT modifiersList = new ListNBT();
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

        nbt.put("echoData", (INBT) getEchoData().toNBT());

        ListNBT roomList = new ListNBT();
        this.guaranteedRoomFilters.forEach(roomKey -> roomList.add(StringNBT.valueOf(roomKey)));
        nbt.put("rooms", (INBT) roomList);

        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.type = nbt.contains("Type", 8) ? Enum.<Type>valueOf(Type.class, nbt.getString("Type")) : Type.CLASSIC;
        this.playerBossName = nbt.getString("PlayerBossName");

        ListNBT modifiersList = nbt.getList("Modifiers", 10);
        modifiersList.forEach(inbt -> this.modifiers.add(Modifier.fromNBT((CompoundNBT) inbt)));

        migrateModifiers(this.modifiers);

        this
                .preventsRandomModifiers = nbt.contains("preventsRandomModifiers", 1) ? nbt.getBoolean("preventsRandomModifiers") : (!this.modifiers.isEmpty());
        this.canBeModified = (!nbt.contains("canBeModified", 1) || nbt.getBoolean("canBeModified"));
        this.canTriggerInfluences = (!nbt.contains("canTriggerInfluences", 1) || nbt.getBoolean("canTriggerInfluences"));
        this.canGenerateTreasureRooms = (!nbt.contains("canGenerateTreasureRooms", 1) || nbt.getBoolean("canGenerateTreasureRooms"));

        this.selectedObjective = null;
        if (nbt.contains("Objective", 8)) {
            this.selectedObjective = new ResourceLocation(nbt.getString("Objective"));
        }
        this.targetObjectiveCount = nbt.contains("targetObjectiveCount", 3) ? nbt.getInt("targetObjectiveCount") : -1;

        if (nbt.contains("echoData")) {
            this.echoData = EchoData.fromNBT(nbt.getCompound("echoData"));
        }

        ListNBT roomList = nbt.getList("rooms", 8);
        roomList.forEach(inbt -> this.guaranteedRoomFilters.add(migrateRoomName(inbt.getAsString())));
    }

    private void migrateModifiers(List<Modifier> modifiers) {
        modifiers.forEach(modifier -> modifier.name = VaultModifier.migrateModifierName(modifier.name));
    }

    private String migrateRoomName(String roomName) {
        if (roomName.equalsIgnoreCase("contest")) {
            roomName = "contest_tree";
        }
        return roomName;
    }

    public CrystalData() {
    }

    public enum Type {
        CLASSIC(VaultLogic.CLASSIC, ClassicVaultBuilder.getInstance(),"Normal",TextFormatting.WHITE),
        RAFFLE( VaultLogic.RAFFLE, RaffleVaultBuilder.getInstance(),  "Raffle",TextFormatting.BLUE),
        COOP( VaultLogic.COOP, CoopVaultBuilder.getInstance(),  "Cooperative",TextFormatting.YELLOW),
        TROVE( VaultLogic.CLASSIC, TroveVaultBuilder.getInstance(),  "Vault Trove", TextFormatting.GOLD);


        private final VaultLogic logic;


        private final VaultRaidBuilder vaultBuilder;


        private final String name;


        private final TextFormatting color;


        Type(VaultLogic logic, VaultRaidBuilder vaultBuilder, String name, TextFormatting color) {
            this.logic = logic;
            this.vaultBuilder = vaultBuilder;
            this.name = name;
            this.color = color;
        }

        public boolean canCraft() {
            return (this == CLASSIC || this == COOP);
        }

        public boolean showTypePrefix() {
            return (this == CLASSIC || this == RAFFLE || this == COOP);
        }

        public boolean showObjective() {
            return (this == CLASSIC || this == RAFFLE || this == COOP);
        }

        public boolean visibleInCreative() {
            return (this == CLASSIC || this == RAFFLE || this == COOP || this == TROVE);
        }

        public boolean canBeCowVault() {
            return (this == CLASSIC || this == RAFFLE || this == COOP);
        }

        public boolean canGenerateRandomModifiers() {
            return (this == CLASSIC || this == RAFFLE || this == COOP);
        }

        public boolean canTriggerInfluences() {
            return (this == CLASSIC || this == COOP);
        }

        public VaultLogic getLogic() {
            return this.logic;
        }

        public VaultRaidBuilder getVaultBuilder() {
            return this.vaultBuilder;
        }

        public ITextComponent getDisplayName() {
            return (ITextComponent) (new StringTextComponent(this.name)).withStyle(this.color);
        }
    }


    public static class Modifier {
        public String name;
        public final Operation operation;

        public Modifier(String name, Operation operation) {
            this.name = name;
            this.operation = operation;
        }

        public void apply(VaultModifiers modifiers, Random random) {
            if (this.operation == Operation.ADD) {
                modifiers.addPermanentModifier(this.name);
            }
        }


        public CompoundNBT toNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Name", this.name);
            nbt.putInt("Operation", this.operation.ordinal());
            return nbt;
        }

        public static Modifier fromNBT(CompoundNBT nbt) {
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

            private final TextFormatting color;

            private final String title;

            Operation(String title, TextFormatting color) {
                this.title = title;
                this.color = color;
            }
        }
    }

    public enum Operation {
        ADD("Has", TextFormatting.GRAY);
        private final TextFormatting color;
        private final String title;

        Operation(String title, TextFormatting color) {
            this.title = title;
            this.color = color;
        }
    }


    private static class EmptyCrystalData
            extends CrystalData {
        private EmptyCrystalData() {
        }

        public boolean addCatalystModifier(String name, boolean preventsRandomModifiers, CrystalData.Modifier.Operation operation) {
            return false;
        }


        public boolean canAddModifier(String name, CrystalData.Modifier.Operation operation) {
            return false;
        }


        public boolean preventsRandomModifiers() {
            return false;
        }


        public void setType(CrystalData.Type type) {
        }


        public void setPlayerBossName(String playerBossName) {
        }


        public void setSelectedObjective(ResourceLocation selectedObjective) {
        }


        public VaultRaid.Builder createVault(ServerWorld world, ServerPlayerEntity player) {
            return null;
        }
    }

    public static class EchoData {
        int echoCount;

        public EchoData(int echoCount) {
            this.echoCount = echoCount;
        }

        public int getEchoCount() {
            return this.echoCount;
        }


        public int addEchoGems(int amount) {
            if (this.echoCount >= 100) return amount;
            for (int i = amount; i > 0; i--) {
                if (this.echoCount >= 100) return i;
                if (MathUtilities.randomFloat(0.0F, 1.0F) < getEchoSuccessRate()) {
                    this.echoCount++;
                }
            }
            return 0;
        }

        public float getCloneSuccessRate() {
            return this.echoCount / 100.0F;
        }

        public float getEchoSuccessRate() {
            return (100 - this.echoCount) / 100.0F;
        }

        public CompoundNBT toNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("echoCount", this.echoCount);
            return nbt;
        }

        public static EchoData fromNBT(CompoundNBT nbt) {
            return new EchoData(nbt.getInt("echoCount"));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\crystal\CrystalData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */