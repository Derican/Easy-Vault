package iskallia.vault.block.entity;

import com.google.common.base.Enums;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.config.VaultChestConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import iskallia.vault.nbt.VMapNBT;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.util.calc.ChestRarityHelper;
import iskallia.vault.util.data.RandomListAccess;
import iskallia.vault.util.data.WeightedDoubleList;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.chest.VaultChestEffect;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.gen.piece.VaultTreasure;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.world.vault.modifier.CatalystChanceModifier;
import iskallia.vault.world.vault.modifier.ChestTrapModifier;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VaultChestTileEntity
        extends ChestTileEntity {
    private VaultRarity rarity;
    private boolean generated;

    protected VaultChestTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
        this.rarityPool = new VMapNBT<>((nbt, rarity1) -> nbt.putInt("Rarity", rarity1.ordinal()), (nbt, weight) -> nbt.putInt("Weight", weight.intValue()), nbt -> VaultRarity.values()[nbt.getInt("Rarity")], nbt -> Integer.valueOf(nbt.getInt("Weight")));
    }

    private BlockState renderState;
    private final VMapNBT<VaultRarity, Integer> rarityPool;
    private int ticksSinceSync;

    public VaultChestTileEntity() {
        this(ModBlocks.VAULT_CHEST_TILE_ENTITY);
    }

    public Map<VaultRarity, Integer> getRarityPool() {
        return (Map<VaultRarity, Integer>) this.rarityPool;
    }

    @Nullable
    public VaultRarity getRarity() {
        return this.rarity;
    }

    @OnlyIn(Dist.CLIENT)
    public void setRenderState(BlockState renderState) {
        this.renderState = renderState;
    }


    public void tick() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        this.ticksSinceSync++;
        this.openCount = getOpenCount(this.level, (LockableTileEntity) this, this.ticksSinceSync, i, j, k, this.openCount);
        this.oOpenness = this.openness;
        float f = 0.1F;
        if (this.openCount > 0 && this.openness == 0.0F) {
            playVaultChestSound(true);
        }

        if ((this.openCount == 0 && this.openness > 0.0F) || (this.openCount > 0 && this.openness < 1.0F)) {
            float f1 = this.openness;
            if (this.openCount > 0) {
                this.openness += 0.1F;
            } else {
                this.openness -= 0.1F;
            }

            if (this.openness > 1.0F) {
                this.openness = 1.0F;
            }
            if (this.openness < 0.5F && f1 >= 0.5F) {
                playVaultChestSound(false);
            }

            if (this.openness < 0.0F) {
                this.openness = 0.0F;
            }
        }


        if (this.level.isClientSide) addParticles();
    }

    private void playVaultChestSound(boolean open) {
        if (this.level == null)
            return;
        double x = this.worldPosition.getX() + 0.5D;
        double y = this.worldPosition.getY() + 0.5D;
        double z = this.worldPosition.getZ() + 0.5D;

        if (open) {
            this.level.playSound(null, x, y, z, SoundEvents.CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            if (this.rarity != null) {
                switch (this.rarity) {
                    case RARE:
                        this.level.playSound(null, x, y, z, ModSounds.VAULT_CHEST_RARE_OPEN, SoundCategory.BLOCKS, 0.2F, this.level.random.nextFloat() * 0.1F + 0.9F);
                        break;
                    case EPIC:
                        this.level.playSound(null, x, y, z, ModSounds.VAULT_CHEST_EPIC_OPEN, SoundCategory.BLOCKS, 0.2F, this.level.random.nextFloat() * 0.1F + 0.9F);
                        break;
                    case OMEGA:
                        this.level.playSound(null, x, y, z, ModSounds.VAULT_CHEST_OMEGA_OPEN, SoundCategory.BLOCKS, 0.2F, this.level.random.nextFloat() * 0.1F + 0.9F);
                        break;
                }
            }
        } else {
            this.level.playSound(null, x, y, z, SoundEvents.CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
        }
    }


    private void addParticles() {
        if (this.level == null)
            return;
        if (this.rarity == null || this.rarity == VaultRarity.COMMON || this.rarity == VaultRarity.RARE) {
            return;
        }
        float xx = this.level.random.nextFloat() * 2.0F - 1.0F;
        float zz = this.level.random.nextFloat() * 2.0F - 1.0F;
        double x = this.worldPosition.getX() + 0.5D + 0.7D * xx;
        double y = (this.worldPosition.getY() + this.level.random.nextFloat());
        double z = this.worldPosition.getZ() + 0.5D + 0.7D * zz;
        double xSpeed = (this.level.random.nextFloat() * xx);
        double ySpeed = (this.level.random.nextFloat() - 0.5D) * 0.25D;
        double zSpeed = (this.level.random.nextFloat() * zz);

        float red = (this.rarity == VaultRarity.EPIC) ? 1.0F : 0.0F;
        float green = (this.rarity == VaultRarity.OMEGA) ? 1.0F : 0.0F;
        float blue = (this.rarity == VaultRarity.EPIC) ? 1.0F : 0.0F;
        this.level.addParticle((IParticleData) new RedstoneParticleData(red, green, blue, 1.0F), x, y, z, xSpeed, ySpeed, zSpeed);
    }


    public void unpackLootTable(PlayerEntity player) {
        generateChestLoot(player, false);
    }

    public void generateChestLoot(PlayerEntity player, boolean compressLoot) {
        if (getLevel() == null || getLevel().isClientSide() || !(player instanceof ServerPlayerEntity) || this.generated) {
            return;
        }

        ServerWorld world = (ServerWorld) getLevel();
        ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;
        if (MiscUtils.isPlayerFakeMP(sPlayer) || player.isSpectator()) {
            this.generated = true;
            setChanged();

            return;
        }
        VaultRaid vault = VaultRaidData.get(world).getAt(world, getBlockPos());
        if (vault == null) {
            this.generated = true;
            this.lootTable = null;
            setChanged();

            return;
        }
        BlockState state = getBlockState();
        if (!sPlayer.isCreative() &&
                shouldPreventCheatyAccess(vault, world, state)) {
            this.generated = true;
            this.lootTable = null;
            setChanged();

            return;
        }
        if (shouldDoChestTrapEffect(vault, world, sPlayer, state)) {
            this.generated = true;
            this.lootTable = null;

            return;
        }
        if (this.lootTable == null) {
            WeightedDoubleList<String> chestRarityList = new WeightedDoubleList();
            float incChestRarity = ChestRarityHelper.getIncreasedChestRarity(sPlayer);

            if (this.rarityPool.isEmpty()) {
                ModConfigs.VAULT_CHEST.RARITY_POOL.forEach((rarity, weight) -> {
                    if (!rarity.equalsIgnoreCase(VaultRarity.COMMON.name())) {
                        chestRarityList.add(rarity, (weight.floatValue() * (1.0F + incChestRarity)));
                    } else {
                        chestRarityList.add(rarity, weight.floatValue());
                    }
                });
            } else {
                this.rarityPool.forEach((rarity, weight) -> {
                    if (!rarity.equals(VaultRarity.COMMON)) {
                        chestRarityList.add(rarity.name(), (weight.floatValue() * (1.0F + incChestRarity)));
                    } else {
                        chestRarityList.add(rarity.name(), weight.floatValue());
                    }
                });
            }
            this


                    .rarity = vault.getPlayer(player).map(VaultPlayer::getProperties).flatMap(properties -> properties.getBase(VaultRaid.CHEST_PITY)).map(pity -> pity.getRandomChestRarity(chestRarityList, player, world.getRandom())).flatMap(key -> Enums.getIfPresent(VaultRarity.class, key).toJavaUtil()).orElse(VaultRarity.COMMON);

            int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
            LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);

            if (config != null) {
                if (state.getBlock() == ModBlocks.VAULT_CHEST) {
                    this.lootTable = config.getChest(this.rarity);
                } else if (state.getBlock() == ModBlocks.VAULT_TREASURE_CHEST) {
                    this.lootTable = config.getTreasureChest(this.rarity);
                } else if (state.getBlock() == ModBlocks.VAULT_ALTAR_CHEST) {
                    this.lootTable = config.getAltarChest(this.rarity);
                } else if (state.getBlock() == ModBlocks.VAULT_COOP_CHEST) {
                    this.lootTable = config.getCoopChest(this.rarity);
                } else if (state.getBlock() == ModBlocks.VAULT_BONUS_CHEST) {
                    this.lootTable = config.getBonusChest(this.rarity);
                }
            }
        }

        List<ItemStack> loot = generateSpecialLoot(vault, world, sPlayer, state);
        fillFromLootTable(player, loot, compressLoot);
        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        this.generated = true;
    }

    private List<ItemStack> generateSpecialLoot(VaultRaid vault, ServerWorld sWorld, ServerPlayerEntity player, BlockState thisState) {
        List<ItemStack> loot = new ArrayList<>();


        if (vault.getActiveObjectives().stream().noneMatch(VaultObjective::preventsCatalystFragments)) {
            vault.getProperties().getBase(VaultRaid.CRYSTAL_DATA).ifPresent(crystalData -> {
                if (!crystalData.preventsRandomModifiers()) {
                    float chance = ModConfigs.VAULT_CHEST_META.getCatalystChance(thisState.getBlock().getRegistryName(), this.rarity);

                    float incModifier = 0.0F;

                    for (CatalystChanceModifier modifier : vault.getActiveModifiersFor(PlayerFilter.any(), CatalystChanceModifier.class)) {
                        incModifier += modifier.getCatalystChanceIncrease();
                    }

                    chance *= 1.0F + incModifier;

                    if (sWorld.getRandom().nextFloat() < chance) {
                        loot.add(new ItemStack((IItemProvider) ModItems.VAULT_CATALYST_FRAGMENT));
                    }
                }

                if (crystalData.getGuaranteedRoomFilters().isEmpty()) {
                    float chance = ModConfigs.VAULT_CHEST_META.getRuneChance(thisState.getBlock().getRegistryName(), this.rarity);

                    if (sWorld.getRandom().nextFloat() < chance) {
                        Item rune = ModConfigs.VAULT_RUNE.getRandomRune();
                        int vaultLevel = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
                        int minLevel = ((Integer) ModConfigs.VAULT_RUNE.getMinimumLevel(rune).orElse(Integer.valueOf(0))).intValue();
                        if (vaultLevel >= minLevel) {
                            loot.add(new ItemStack((IItemProvider) rune));
                        }
                    }
                }
            });
        }
        vault.getProperties().getBase(VaultRaid.LEVEL).ifPresent(level -> {
            int traders = ModConfigs.SCALING_CHEST_REWARDS.traderCount(thisState.getBlock().getRegistryName(), this.rarity, level.intValue());

            for (int i = 0; i < traders; i++) {
                int slot = MiscUtils.getRandomEmptySlot((IInventory) this);
                if (slot != -1) {
                    setItem(slot, new ItemStack((IItemProvider) ModItems.TRADER_CORE));
                }
            }
            int statues = ModConfigs.SCALING_CHEST_REWARDS.statueCount(thisState.getBlock().getRegistryName(), this.rarity, level.intValue());
            for (int j = 0; j < statues; j++) {
                int slot = MiscUtils.getRandomEmptySlot((IInventory) this);
                if (slot != -1) {
                    ItemStack statue = new ItemStack((IItemProvider) ModBlocks.GIFT_NORMAL_STATUE);
                    if (ModConfigs.SCALING_CHEST_REWARDS.isMegaStatue()) {
                        statue = new ItemStack((IItemProvider) ModBlocks.GIFT_MEGA_STATUE);
                    }
                    setItem(slot, statue);
                }
            }
        });
        vault.getActiveObjective(ScavengerHuntObjective.class).ifPresent(objective -> vault.getProperties().getBase(VaultRaid.IDENTIFIER).ifPresent((UUID)->{}));


        return loot;
    }

    private boolean shouldDoChestTrapEffect(VaultRaid vault, ServerWorld sWorld, ServerPlayerEntity player, BlockState thisState) {
        if (vault.getAllObjectives().stream().anyMatch(VaultObjective::preventsTrappedChests)) {
            return false;
        }
        return ((Boolean) vault.getPlayer(player.getUUID()).map(vPlayer -> {
            int level = ((Integer) vPlayer.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();

            boolean raffle = ((Boolean) vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(Boolean.valueOf(false))).booleanValue();

            VaultChestConfig config = null;

            if (thisState.getBlock() == ModBlocks.VAULT_CHEST) {
                config = ModConfigs.VAULT_CHEST;
            } else if (thisState.getBlock() == ModBlocks.VAULT_TREASURE_CHEST) {
                config = ModConfigs.VAULT_TREASURE_CHEST;
            } else if (thisState.getBlock() == ModBlocks.VAULT_ALTAR_CHEST) {
                config = ModConfigs.VAULT_ALTAR_CHEST;
            } else if (thisState.getBlock() == ModBlocks.VAULT_COOP_CHEST) {
                config = ModConfigs.VAULT_COOP_CHEST;
            } else if (thisState.getBlock() == ModBlocks.VAULT_BONUS_CHEST) {
                config = ModConfigs.VAULT_BONUS_CHEST;
            }
            if (config != null) {
                WeightedList weightedList = config.getEffectPool(level, raffle);
                if (weightedList != null) {
                    RandomListAccess randomListAccess = null;
                    for (ChestTrapModifier modifier : vault.getActiveModifiersFor(PlayerFilter.of(new VaultPlayer[]{vPlayer} ), ChestTrapModifier.class)) {
                        randomListAccess = modifier.modifyWeightedList(config, (RandomListAccess) weightedList);
                    }
                    VaultChestEffect effect = config.getEffectByName((String) randomListAccess.getRandom(this.level.getRandom()));
                    if (effect != null) {
                        effect.apply(vault, vPlayer, sWorld);
                        this.level.setBlockAndUpdate(getBlockPos(), ModBlocks.VAULT_BEDROCK.defaultBlockState());
                        return Boolean.valueOf(true);
                    }
                }
            }
            return Boolean.valueOf(false);
        }).orElse(Boolean.valueOf(false))).booleanValue();
    }

    private boolean shouldPreventCheatyAccess(VaultRaid vault, ServerWorld sWorld, BlockState thisState) {
        if (vault.getActiveObjective(ArchitectObjective.class).isPresent()) {

            return false;
        }
        if (thisState.getBlock() == ModBlocks.VAULT_TREASURE_CHEST) {
            boolean isValidPosition = false;
            for (VaultPiece piece : vault.getGenerator().getPiecesAt(getBlockPos())) {
                if (piece instanceof VaultTreasure) {
                    VaultTreasure treasurePiece = (VaultTreasure) piece;
                    if (treasurePiece.isDoorOpen((World) sWorld)) {
                        isValidPosition = true;
                    }
                }
            }
            if (!isValidPosition) {
                vault.getPlayers().stream()
                        .filter(vPlayer -> vPlayer instanceof iskallia.vault.world.vault.player.VaultRunner)
                        .findAny()
                        .ifPresent(vRunner -> vRunner.runIfPresent(sWorld.getServer(), (ServerPlayerEntity entity)->{}));


                return true;
            }
        }
        return false;
    }

    private void fillFromLootTable(@Nullable PlayerEntity player, List<ItemStack> customLoot, boolean compressLoot) {
        if (this.lootTable != null && this.level.getServer() != null) {
            LootTable loottable = this.level.getServer().getLootTables().get(this.lootTable);
            if (player instanceof ServerPlayerEntity) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayerEntity) player, this.lootTable);
            }

            this.lootTable = null;


            LootContext.Builder ctxBuilder = (new LootContext.Builder((ServerWorld) this.level)).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf((Vector3i) this.worldPosition)).withOptionalRandomSeed(this.lootTableSeed);
            if (player != null) {
                ctxBuilder.withLuck(player.getLuck())
                        .withParameter(LootParameters.THIS_ENTITY, player);
            }

            fillFromLootTable(loottable, ctxBuilder.create(LootParameterSets.CHEST), customLoot, compressLoot);
        }
    }

    private void fillFromLootTable(LootTable lootTable, LootContext context, List<ItemStack> customLoot, boolean compressLoot) {
        if (!compressLoot) {
            customLoot.forEach(stack -> {
                int slot = MiscUtils.getRandomEmptySlot((IInventory) this);
                if (slot != -1) {
                    setItem(slot, stack);
                }
            });
            lootTable.fill((IInventory) this, context);
            return;
        }
        List<ItemStack> mergedLoot = MiscUtils.splitAndLimitStackSize(MiscUtils.mergeItemStacks(lootTable.getRandomItems(context)));
        mergedLoot.addAll(customLoot);
        mergedLoot.forEach(stack -> MiscUtils.addItemStack((IInventory) this, stack));
    }


    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }


    public ItemStack removeItem(int index, int count) {
        ItemStack stack = super.removeItem(index, count);
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        return stack;
    }


    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = super.removeItemNoUpdate(index);
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        return stack;
    }


    public BlockState getBlockState() {
        if (this.renderState != null) {
            return this.renderState;
        }
        return super.getBlockState();
    }


    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);

        if (nbt.contains("Rarity", 3)) {
            this.rarity = VaultRarity.values()[nbt.getInt("Rarity")];
        }

        this.rarityPool.deserializeNBT(nbt.getList("RarityPool", 10));
        this.generated = nbt.getBoolean("Generated");
    }


    public CompoundNBT save(CompoundNBT compound) {
        CompoundNBT nbt = super.save(compound);

        if (this.rarity != null) {
            nbt.putInt("Rarity", this.rarity.ordinal());
        }

        nbt.put("RarityPool", (INBT) this.rarityPool.serializeNBT());
        nbt.putBoolean("Generated", this.generated);
        return nbt;
    }


    public ITextComponent getDisplayName() {
        if (this.rarity != null) {
            String rarity = StringUtils.capitalize(this.rarity.name().toLowerCase());

            BlockState state = getBlockState();
            if (state.getBlock() == ModBlocks.VAULT_CHEST || state
                    .getBlock() == ModBlocks.VAULT_COOP_CHEST || state
                    .getBlock() == ModBlocks.VAULT_BONUS_CHEST)
                return (ITextComponent) new StringTextComponent(rarity + " Chest");
            if (state.getBlock() == ModBlocks.VAULT_TREASURE_CHEST)
                return (ITextComponent) new StringTextComponent(rarity + " Treasure Chest");
            if (state.getBlock() == ModBlocks.VAULT_ALTAR_CHEST) {
                return (ITextComponent) new StringTextComponent(rarity + " Altar Chest");
            }
        }

        return super.getDisplayName();
    }


    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        save(nbt);
        return nbt;
    }


    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        load(state, tag);
    }


    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, getUpdateTag());
    }


    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getTag();
        handleUpdateTag(getBlockState(), tag);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\VaultChestTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */