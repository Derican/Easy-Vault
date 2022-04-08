package iskallia.vault.world.vault.logic.objective.raid;

import iskallia.vault.block.entity.VaultRaidControllerTileEntity;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.gen.decorator.BreadcrumbFeature;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.VaultGenerator;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.gen.piece.VaultRaidRoom;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.objective.raid.modifier.BlockPlacementModifier;
import iskallia.vault.world.vault.logic.objective.raid.modifier.FloatingItemModifier;
import iskallia.vault.world.vault.logic.objective.raid.modifier.RaidModifier;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.modifier.CatalystChanceModifier;
import iskallia.vault.world.vault.modifier.InventoryRestoreModifier;
import iskallia.vault.world.vault.modifier.LootableModifier;
import iskallia.vault.world.vault.modifier.VaultModifier;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RaidChallengeObjective extends VaultObjective {
    private final LinkedHashMap<RaidModifier, Float> modifierValues = new LinkedHashMap<>();
    private int completedRaids = 0;
    private boolean started = false;

    public RaidChallengeObjective(ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
    }


    @Nonnull
    public BlockState getObjectiveRelevantBlock() {
        return Blocks.AIR.defaultBlockState();
    }


    @Nonnull
    public Supplier<? extends VaultGenerator> getVaultGenerator() {
        return VaultRaid.RAID_CHALLENGE_GENERATOR;
    }


    @Nullable
    public LootTable getRewardLootTable(VaultRaid vault, Function<ResourceLocation, LootTable> tblResolver) {
        return null;
    }


    public boolean shouldPauseTimer(MinecraftServer srv, VaultRaid vault) {
        return !this.started;
    }


    public ITextComponent getObjectiveDisplayName() {
        return (ITextComponent) (new StringTextComponent("Raid")).withStyle(TextFormatting.RED);
    }


    public ITextComponent getVaultName() {
        return (ITextComponent) new StringTextComponent("Vault Raid");
    }

    public int getCompletedRaids() {
        return this.completedRaids;
    }

    public void addModifier(RaidModifier modifier, float value) {
        if (modifier instanceof iskallia.vault.world.vault.logic.objective.raid.modifier.ModifierDoublingModifier) {
            this.modifierValues.forEach(this::addModifier);
            return;
        }
        for (RaidModifier existing : this.modifierValues.keySet()) {
            if (existing.getName().equals(modifier.getName())) {
                float existingValue = ((Float) this.modifierValues.get(existing)).floatValue();
                this.modifierValues.put(modifier, Float.valueOf(existingValue + value));

                return;
            }
        }
        this.modifierValues.put(modifier, Float.valueOf(value));
    }

    public Map<RaidModifier, Float> getAllModifiers() {
        return Collections.unmodifiableMap(this.modifierValues);
    }

    public <T extends RaidModifier> Map<T, Float> getModifiersOfType(Class<T> modifierClass) {
        return (Map<T, Float>) this.modifierValues.entrySet().stream()
                .filter(modifierTpl -> modifierClass.isAssignableFrom(((RaidModifier) modifierTpl.getKey()).getClass()))
                .map(tpl -> tpl)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public LinkedHashMap<RaidModifier, Float> getModifiers(boolean positive) {
        LinkedHashMap<RaidModifier, Float> modifiers = new LinkedHashMap<>();
        this.modifierValues.forEach((modifier, value) -> {
            if ((positive && modifier.isPositive()) || (!positive && !modifier.isPositive())) {
                modifiers.put(modifier, value);
            }
        });

        return modifiers;
    }


    public void tick(VaultRaid vault, PlayerFilter filter, ServerWorld world) {
        super.tick(vault, filter, world);
        MinecraftServer srv = world.getServer();
        ActiveRaid raid = vault.getActiveRaid();
        if (raid != null) {
            this.started = true;
        }

        sendRaidMessage(vault, filter, srv, raid);
    }

    private void sendRaidMessage(VaultRaid vault, PlayerFilter filter, MinecraftServer srv, @Nullable ActiveRaid raid) {
        int wave = (raid == null) ? 0 : raid.getWave();
        int totalWaves = (raid == null) ? 0 : raid.getTotalWaves();
        int mobs = (raid == null) ? 0 : raid.getAliveEntities();
        int totalMobs = (raid == null) ? 0 : raid.getTotalWaveEntities();
        int startDelay = (raid == null) ? 0 : raid.getStartDelay();
        List<ITextComponent> positives = new ArrayList<>();
        List<ITextComponent> negatives = new ArrayList<>();
        this.modifierValues.forEach((modifier, value) -> {
            ITextComponent display = modifier.getDisplay(value.floatValue());

            if (modifier.isPositive()) {
                positives.add(display);
            } else {
                negatives.add(display);
            }
        });
        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, ()));
    }


    public void onRaidStart(VaultRaid vault, ServerWorld world, ActiveRaid raid, BlockPos controller) {
        TileEntity te = world.getBlockEntity(controller);
        if (!(te instanceof VaultRaidControllerTileEntity)) {
            return;
        }
        VaultRaidControllerTileEntity raidController = (VaultRaidControllerTileEntity) te;
        raidController.getRaidModifiers().forEach((modifierName, value) -> {
            RaidModifier mod = ModConfigs.RAID_MODIFIER_CONFIG.getByName(modifierName);

            if (mod != null) {
                addModifier(mod, value.floatValue());
            }
        });
        if (ModConfigs.RAID_EVENT_CONFIG.isEnabled() && rand
                .nextFloat() < ModConfigs.RAID_EVENT_CONFIG.getViewerVoteChance()) {
            addRandomModifier(vault, world);
        }
    }


    private void addRandomModifier(VaultRaid vault, ServerWorld world) {
        while (true) {
            String modifierName = ModConfigs.RAID_EVENT_CONFIG.getRandomModifier().getName();
            VaultModifier vModifier = ModConfigs.VAULT_MODIFIERS.getByName(modifierName);
            if (vModifier != null) {
                VaultModifier fModifier = vModifier;

                int minutes = ModConfigs.RAID_EVENT_CONFIG.getTemporaryModifierMinutes();


                IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("Added ")).withStyle(TextFormatting.GRAY).append(vModifier.getNameComponent()).append((ITextComponent) (new StringTextComponent(" for ")).withStyle(TextFormatting.GRAY)).append((ITextComponent) (new StringTextComponent(minutes + " minutes!")).withStyle(TextFormatting.GOLD));

                vault.getModifiers().addTemporaryModifier(vModifier, minutes * 60 * 20);
                vault.getPlayers().forEach(vPlayer -> {
                    fModifier.apply(vault, vPlayer, world, world.getRandom());
                    vPlayer.runIfPresent(world.getServer(), ());
                });
                return;
            }
        }
    }

    public void onRaidFinish(VaultRaid vault, ServerWorld world, ActiveRaid raid, BlockPos controller) {
        this.completedRaids++;
        if (this.completedRaids % 10 == 0) {
            RaidModifier modifier = ModConfigs.RAID_MODIFIER_CONFIG.getByName("artifactFragment");
            if (modifier != null) {

                boolean canGetArtifact = vault.getActiveModifiersFor(PlayerFilter.any(), InventoryRestoreModifier.class).stream().noneMatch(InventoryRestoreModifier::preventsArtifact);
                if (canGetArtifact) {
                    addModifier(modifier, MathUtilities.randomFloat(0.02F, 0.05F));
                }
            }
        }


        VaultRaidRoom raidRoom = vault.getGenerator().getPiecesAt(controller, VaultRaidRoom.class).stream().findFirst().orElse(null);
        if (raidRoom == null) {
            return;
        }
        for (Direction direction : Direction.values()) {
            if (direction.getAxis() != Direction.Axis.Y) {


                if (VaultJigsawHelper.canExpand(vault, (VaultRoom) raidRoom, direction)) {


                    VaultJigsawHelper.expandVault(vault, world, (VaultRoom) raidRoom, direction, VaultJigsawHelper.getRaidChallengeRoom());
                }
            }
        }
        raidRoom.setRaidFinished();

        getAllModifiers().forEach((modifier, value) -> modifier.onVaultRaidFinish(vault, world, controller, raid, value.floatValue()));


        AxisAlignedBB raidBoundingBox = raid.getRaidBoundingBox();

        FloatingItemModifier catalystPlacement = new FloatingItemModifier("", 4, (new WeightedList()).add(new SingleItemEntry((IItemProvider) ModItems.VAULT_CATALYST_FRAGMENT), 1), "");
        vault.getActiveModifiersFor(PlayerFilter.any(), CatalystChanceModifier.class).forEach(modifier -> catalystPlacement.onVaultRaidFinish(vault, world, controller, raid, 1.0F));


        BlockPlacementModifier orePlacement = new BlockPlacementModifier("", ModBlocks.UNKNOWN_ORE, 12, "");
        vault.getActiveModifiersFor(PlayerFilter.any(), LootableModifier.class).forEach(modifier -> orePlacement.onVaultRaidFinish(vault, world, controller, raid, modifier.getAverageMultiplier()));


        BreadcrumbFeature.generateVaultBreadcrumb(vault, world, Lists.newArrayList((Object[]) new VaultPiece[]{(VaultPiece) raidRoom}));
    }


    public boolean preventsMobSpawning() {
        return true;
    }


    public boolean preventsInfluences() {
        return true;
    }


    public boolean preventsNormalMonsterDrops() {
        return true;
    }


    public boolean preventsCatalystFragments() {
        return true;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();

        ListNBT modifiers = new ListNBT();
        this.modifierValues.forEach((modifier, value) -> {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("name", modifier.getName());
            nbt.putFloat("value", value.floatValue());
            modifiers.add(nbt);
        });
        tag.put("raidModifiers", (INBT) modifiers);

        tag.putInt("completedRaids", this.completedRaids);
        tag.putBoolean("started", this.started);
        return tag;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);

        ListNBT modifiers = nbt.getList("raidModifiers", 10);
        for (int i = 0; i < modifiers.size(); i++) {
            CompoundNBT modifierTag = modifiers.getCompound(i);
            RaidModifier modifier = ModConfigs.RAID_MODIFIER_CONFIG.getByName(modifierTag.getString("name"));
            if (modifier != null) {
                float val = modifierTag.getFloat("value");
                this.modifierValues.put(modifier, Float.valueOf(val));
            }
        }

        this.completedRaids = nbt.getInt("completedRaids");
        this.started = nbt.getBoolean("started");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\raid\RaidChallengeObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */