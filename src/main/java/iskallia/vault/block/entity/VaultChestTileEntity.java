package iskallia.vault.block.entity;

import com.google.common.base.Enums;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.config.VaultChestConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.BasicScavengerItem;
import iskallia.vault.nbt.VMapNBT;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.util.VaultRarity;
import iskallia.vault.util.calc.ChestRarityHelper;
import iskallia.vault.util.data.RandomListAccess;
import iskallia.vault.util.data.WeightedDoubleList;
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
import iskallia.vault.world.vault.player.VaultRunner;
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
import net.minecraft.util.DamageSource;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VaultChestTileEntity extends ChestTileEntity {
    private VaultRarity rarity;
    private boolean generated;
    private BlockState renderState;
    private final VMapNBT<VaultRarity, Integer> rarityPool;
    private int ticksSinceSync;

    protected VaultChestTileEntity(final TileEntityType<?> typeIn) {
        super((TileEntityType) typeIn);
        this.rarityPool = new VMapNBT<VaultRarity, Integer>((nbt, rarity1) -> nbt.putInt("Rarity", rarity1.ordinal()), (nbt, weight) -> nbt.putInt("Weight", (int) weight), nbt -> VaultRarity.values()[nbt.getInt("Rarity")], nbt -> nbt.getInt("Weight"));
    }

    public VaultChestTileEntity() {
        this(ModBlocks.VAULT_CHEST_TILE_ENTITY);
    }

    public Map<VaultRarity, Integer> getRarityPool() {
        return this.rarityPool;
    }

    @Nullable
    public VaultRarity getRarity() {
        return this.rarity;
    }

    @OnlyIn(Dist.CLIENT)
    public void setRenderState(final BlockState renderState) {
        this.renderState = renderState;
    }

    public void tick() {
        final int i = this.worldPosition.getX();
        final int j = this.worldPosition.getY();
        final int k = this.worldPosition.getZ();
        ++this.ticksSinceSync;
        this.openCount = getOpenCount(this.level, (LockableTileEntity) this, this.ticksSinceSync, i, j, k, this.openCount);
        this.oOpenness = this.openness;
        final float f = 0.1f;
        if (this.openCount > 0 && this.openness == 0.0f) {
            this.playVaultChestSound(true);
        }
        if ((this.openCount == 0 && this.openness > 0.0f) || (this.openCount > 0 && this.openness < 1.0f)) {
            final float f2 = this.openness;
            if (this.openCount > 0) {
                this.openness += 0.1f;
            } else {
                this.openness -= 0.1f;
            }
            if (this.openness > 1.0f) {
                this.openness = 1.0f;
            }
            if (this.openness < 0.5f && f2 >= 0.5f) {
                this.playVaultChestSound(false);
            }
            if (this.openness < 0.0f) {
                this.openness = 0.0f;
            }
        }
        if (this.level.isClientSide) {
            this.addParticles();
        }
    }

    private void playVaultChestSound(final boolean open) {
        if (this.level == null) {
            return;
        }
        final double x = this.worldPosition.getX() + 0.5;
        final double y = this.worldPosition.getY() + 0.5;
        final double z = this.worldPosition.getZ() + 0.5;
        if (open) {
            this.level.playSound((PlayerEntity) null, x, y, z, SoundEvents.CHEST_OPEN, SoundCategory.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
            if (this.rarity != null) {
                switch (this.rarity) {
                    case RARE: {
                        this.level.playSound((PlayerEntity) null, x, y, z, ModSounds.VAULT_CHEST_RARE_OPEN, SoundCategory.BLOCKS, 0.2f, this.level.random.nextFloat() * 0.1f + 0.9f);
                        break;
                    }
                    case EPIC: {
                        this.level.playSound((PlayerEntity) null, x, y, z, ModSounds.VAULT_CHEST_EPIC_OPEN, SoundCategory.BLOCKS, 0.2f, this.level.random.nextFloat() * 0.1f + 0.9f);
                        break;
                    }
                    case OMEGA: {
                        this.level.playSound((PlayerEntity) null, x, y, z, ModSounds.VAULT_CHEST_OMEGA_OPEN, SoundCategory.BLOCKS, 0.2f, this.level.random.nextFloat() * 0.1f + 0.9f);
                        break;
                    }
                }
            }
        } else {
            this.level.playSound((PlayerEntity) null, x, y, z, SoundEvents.CHEST_CLOSE, SoundCategory.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
        }
    }

    private void addParticles() {
        if (this.level == null) {
            return;
        }
        if (this.rarity == null || this.rarity == VaultRarity.COMMON || this.rarity == VaultRarity.RARE) {
            return;
        }
        final float xx = this.level.random.nextFloat() * 2.0f - 1.0f;
        final float zz = this.level.random.nextFloat() * 2.0f - 1.0f;
        final double x = this.worldPosition.getX() + 0.5 + 0.7 * xx;
        final double y = this.worldPosition.getY() + this.level.random.nextFloat();
        final double z = this.worldPosition.getZ() + 0.5 + 0.7 * zz;
        final double xSpeed = this.level.random.nextFloat() * xx;
        final double ySpeed = (this.level.random.nextFloat() - 0.5) * 0.25;
        final double zSpeed = this.level.random.nextFloat() * zz;
        final float red = (this.rarity == VaultRarity.EPIC) ? 1.0f : 0.0f;
        final float green = (this.rarity == VaultRarity.OMEGA) ? 1.0f : 0.0f;
        final float blue = (this.rarity == VaultRarity.EPIC) ? 1.0f : 0.0f;
        this.level.addParticle((IParticleData) new RedstoneParticleData(red, green, blue, 1.0f), x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public void unpackLootTable(final PlayerEntity player) {
        this.generateChestLoot(player, false);
    }

    public void generateChestLoot(final PlayerEntity player, final boolean compressLoot) {
        if (this.getLevel() == null || this.getLevel().isClientSide() || !(player instanceof ServerPlayerEntity) || this.generated) {
            return;
        }
        final ServerWorld world = (ServerWorld) this.getLevel();
        final ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;
        if (MiscUtils.isPlayerFakeMP(sPlayer) || player.isSpectator()) {
            this.generated = true;
            this.setChanged();
            return;
        }
        final VaultRaid vault = VaultRaidData.get(world).getAt(world, this.getBlockPos());
        if (vault == null) {
            this.generated = true;
            this.lootTable = null;
            this.setChanged();
            return;
        }
        final BlockState state = this.getBlockState();
        if (!sPlayer.isCreative() && this.shouldPreventCheatyAccess(vault, world, state)) {
            this.generated = true;
            this.lootTable = null;
            this.setChanged();
            return;
        }
        if (this.shouldDoChestTrapEffect(vault, world, sPlayer, state)) {
            this.generated = true;
            this.lootTable = null;
            return;
        }
        if (this.lootTable == null) {
            final WeightedDoubleList<String> chestRarityList = new WeightedDoubleList<String>();
            final float incChestRarity = ChestRarityHelper.getIncreasedChestRarity(sPlayer);
            if (this.rarityPool.isEmpty()) {
                ModConfigs.VAULT_CHEST.RARITY_POOL.forEach((rarity, weight) -> {
                    if (!rarity.equalsIgnoreCase(VaultRarity.COMMON.name())) {
                        chestRarityList.add(rarity, weight.floatValue() * (1.0f + incChestRarity));
                    } else {
                        chestRarityList.add(rarity, weight.floatValue());
                    }
                    return;
                });
            } else {
                this.rarityPool.forEach((rarity, weight) -> {
                    if (!rarity.equals(VaultRarity.COMMON)) {
                        chestRarityList.add(rarity.name(), weight * (1.0f + incChestRarity));
                    } else {
                        chestRarityList.add(rarity.name(), (float) weight);
                    }
                    return;
                });
            }
            this.rarity = vault.getPlayer(player).map(VaultPlayer::getProperties).flatMap(properties -> properties.getBase(VaultRaid.CHEST_PITY)).map(pity -> pity.getRandomChestRarity(chestRarityList, player, world.getRandom())).flatMap(key -> Enums.getIfPresent(VaultRarity.class, key).toJavaUtil()).orElse(VaultRarity.COMMON);
            final int level = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
            final LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
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
        final List<ItemStack> loot = this.generateSpecialLoot(vault, world, sPlayer, state);
        this.fillFromLootTable(player, loot, compressLoot);
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        this.generated = true;
    }

    private List<ItemStack> generateSpecialLoot(final VaultRaid vault, final ServerWorld sWorld, final ServerPlayerEntity player, final BlockState thisState) {
        final List<ItemStack> loot = new ArrayList<ItemStack>();
        if (vault.getActiveObjectives().stream().noneMatch(VaultObjective::preventsCatalystFragments)) {
            vault.getProperties().getBase(VaultRaid.CRYSTAL_DATA).ifPresent(crystalData -> {
                if (crystalData.isChallenge() || !crystalData.preventsRandomModifiers()) {
                    final float chance = ModConfigs.VAULT_CHEST_META.getCatalystChance(thisState.getBlock().getRegistryName(), this.rarity);
                    float incModifier = 0.0f;
                    for (CatalystChanceModifier modifier : vault.getActiveModifiersFor(PlayerFilter.any(), CatalystChanceModifier.class)) {
                        incModifier += modifier.getCatalystChanceIncrease();
                    }
                    final float chance2 = chance * (1.0f + incModifier);
                    if (sWorld.getRandom().nextFloat() < chance2) {
                        loot.add(new ItemStack((IItemProvider) ModItems.VAULT_CATALYST_FRAGMENT));
                    }
                }
                if (crystalData.getGuaranteedRoomFilters().isEmpty()) {
                    final float chance3 = ModConfigs.VAULT_CHEST_META.getRuneChance(thisState.getBlock().getRegistryName(), this.rarity);
                    if (sWorld.getRandom().nextFloat() < chance3) {
                        final Item rune = ModConfigs.VAULT_RUNE.getRandomRune();
                        final int vaultLevel = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
                        final int minLevel = ModConfigs.VAULT_RUNE.getMinimumLevel(rune).orElse(0);
                        if (vaultLevel >= minLevel) {
                            loot.add(new ItemStack((IItemProvider) rune));
                        }
                    }
                }
                return;
            });
        }
        vault.getProperties().getBase(VaultRaid.LEVEL).ifPresent(level -> {
            for (int traders = ModConfigs.SCALING_CHEST_REWARDS.traderCount(thisState.getBlock().getRegistryName(), this.rarity, level), i = 0; i < traders; ++i) {
                final int slot = MiscUtils.getRandomEmptySlot((IInventory) this);
                if (slot != -1) {
                    this.setItem(slot, new ItemStack((IItemProvider) ModItems.TRADER_CORE));
                }
            }
            for (int statues = ModConfigs.SCALING_CHEST_REWARDS.statueCount(thisState.getBlock().getRegistryName(), this.rarity, level), j = 0; j < statues; ++j) {
                final int slot2 = MiscUtils.getRandomEmptySlot((IInventory) this);
                if (slot2 != -1) {
                    ItemStack statue = new ItemStack((IItemProvider) ModBlocks.GIFT_NORMAL_STATUE);
                    if (ModConfigs.SCALING_CHEST_REWARDS.isMegaStatue()) {
                        statue = new ItemStack((IItemProvider) ModBlocks.GIFT_MEGA_STATUE);
                    }
                    this.setItem(slot2, statue);
                }
            }
            return;
        });
        vault.getActiveObjective(ScavengerHuntObjective.class).ifPresent(objective -> vault.getProperties().getBase(VaultRaid.IDENTIFIER).ifPresent(identifier -> ModConfigs.SCAVENGER_HUNT.generateChestLoot(objective.getGenerationDropFilter()).forEach(itemEntry -> {
            final ItemStack stack = itemEntry.createItemStack();
            if (!stack.isEmpty()) {
                BasicScavengerItem.setVaultIdentifier(stack, identifier);
                loot.add(stack);
            }
        })));
        return loot;
    }

    private boolean shouldDoChestTrapEffect(final VaultRaid vault, final ServerWorld sWorld, final ServerPlayerEntity player, final BlockState thisState) {
        return !vault.getAllObjectives().stream().anyMatch(VaultObjective::preventsTrappedChests) && vault.getPlayer(player.getUUID()).map(vPlayer -> {
            final int level = vPlayer.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
            final boolean raffle = vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(false);
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
                RandomListAccess<String> effectPool = config.getEffectPool(level, raffle);
                if (effectPool != null) {
                    for (ChestTrapModifier modifier : vault.getActiveModifiersFor(PlayerFilter.of(vPlayer), ChestTrapModifier.class)) {
                        effectPool = modifier.modifyWeightedList(config, effectPool);
                    }
                    final VaultChestEffect effect = config.getEffectByName(effectPool.getRandom(this.level.getRandom()));
                    if (effect != null) {
                        effect.apply(vault, vPlayer, sWorld);
                        this.level.setBlockAndUpdate(this.getBlockPos(), ModBlocks.VAULT_BEDROCK.defaultBlockState());
                        return true;
                    }
                }
            }
            return false;
        }).orElse(false);
    }

    private boolean shouldPreventCheatyAccess(final VaultRaid vault, final ServerWorld sWorld, final BlockState thisState) {
        if (vault.getActiveObjective(ArchitectObjective.class).isPresent()) {
            return false;
        }
        if (thisState.getBlock() == ModBlocks.VAULT_TREASURE_CHEST) {
            boolean isValidPosition = false;
            for (final VaultPiece piece : vault.getGenerator().getPiecesAt(this.getBlockPos())) {
                if (piece instanceof VaultTreasure) {
                    final VaultTreasure treasurePiece = (VaultTreasure) piece;
                    if (!treasurePiece.isDoorOpen((World) sWorld)) {
                        continue;
                    }
                    isValidPosition = true;
                }
            }
            if (!isValidPosition) {
                vault.getPlayers().stream().filter(vPlayer -> vPlayer instanceof VaultRunner).findAny().ifPresent(vRunner -> vRunner.runIfPresent(sWorld.getServer(), sPlayer -> {
                    sPlayer.hurt(DamageSource.MAGIC, 1000000.0f);
                    sPlayer.setHealth(0.0f);
                }));
                return true;
            }
        }
        return false;
    }

    private void fillFromLootTable(@Nullable final PlayerEntity player, final List<ItemStack> customLoot, final boolean compressLoot) {
        if (this.lootTable != null && this.level.getServer() != null) {
            final LootTable loottable = this.level.getServer().getLootTables().get(this.lootTable);
            if (player instanceof ServerPlayerEntity) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayerEntity) player, this.lootTable);
            }
            this.lootTable = null;
            final LootContext.Builder ctxBuilder = new LootContext.Builder((ServerWorld) this.level).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf((Vector3i) this.worldPosition)).withOptionalRandomSeed(this.lootTableSeed);
            if (player != null) {
                ctxBuilder.withLuck(player.getLuck()).withParameter(LootParameters.THIS_ENTITY, player);
            }
            this.fillFromLootTable(loottable, ctxBuilder.create(LootParameterSets.CHEST), customLoot, compressLoot);
        }
    }

    private void fillFromLootTable(final LootTable lootTable, final LootContext context, final List<ItemStack> customLoot, final boolean compressLoot) {
        if (!compressLoot) {
            customLoot.forEach(stack -> {
                final int slot = MiscUtils.getRandomEmptySlot((IInventory) this);
                if (slot != -1) {
                    this.setItem(slot, stack);
                }
                return;
            });
            lootTable.fill((IInventory) this, context);
            return;
        }
        final List<ItemStack> mergedLoot = MiscUtils.splitAndLimitStackSize(MiscUtils.mergeItemStacks(lootTable.getRandomItems(context)));
        mergedLoot.addAll(customLoot);
        mergedLoot.forEach(stack -> MiscUtils.addItemStack((IInventory) this, stack));
    }

    public void setItem(final int index, final ItemStack stack) {
        super.setItem(index, stack);
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public ItemStack removeItem(final int index, final int count) {
        final ItemStack stack = super.removeItem(index, count);
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        return stack;
    }

    public ItemStack removeItemNoUpdate(final int index) {
        final ItemStack stack = super.removeItemNoUpdate(index);
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        return stack;
    }

    public BlockState getBlockState() {
        if (this.renderState != null) {
            return this.renderState;
        }
        return super.getBlockState();
    }

    public void load(final BlockState state, final CompoundNBT nbt) {
        super.load(state, nbt);
        if (nbt.contains("Rarity", 3)) {
            this.rarity = VaultRarity.values()[nbt.getInt("Rarity")];
        }
        this.rarityPool.deserializeNBT(nbt.getList("RarityPool", 10));
        this.generated = nbt.getBoolean("Generated");
    }

    public CompoundNBT save(final CompoundNBT compound) {
        final CompoundNBT nbt = super.save(compound);
        if (this.rarity != null) {
            nbt.putInt("Rarity", this.rarity.ordinal());
        }
        nbt.put("RarityPool", (INBT) this.rarityPool.serializeNBT());
        nbt.putBoolean("Generated", this.generated);
        return nbt;
    }

    public ITextComponent getDisplayName() {
        if (this.rarity != null) {
            final String rarity = StringUtils.capitalize(this.rarity.name().toLowerCase());
            final BlockState state = this.getBlockState();
            if (state.getBlock() == ModBlocks.VAULT_CHEST || state.getBlock() == ModBlocks.VAULT_COOP_CHEST || state.getBlock() == ModBlocks.VAULT_BONUS_CHEST) {
                return (ITextComponent) new StringTextComponent(rarity + " Chest");
            }
            if (state.getBlock() == ModBlocks.VAULT_TREASURE_CHEST) {
                return (ITextComponent) new StringTextComponent(rarity + " Treasure Chest");
            }
            if (state.getBlock() == ModBlocks.VAULT_ALTAR_CHEST) {
                return (ITextComponent) new StringTextComponent(rarity + " Altar Chest");
            }
        }
        return super.getDisplayName();
    }

    public CompoundNBT getUpdateTag() {
        final CompoundNBT nbt = new CompoundNBT();
        this.save(nbt);
        return nbt;
    }

    public void handleUpdateTag(final BlockState state, final CompoundNBT tag) {
        this.load(state, tag);
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, 1, this.getUpdateTag());
    }

    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
        final CompoundNBT tag = pkt.getTag();
        this.handleUpdateTag(this.getBlockState(), tag);
    }
}
