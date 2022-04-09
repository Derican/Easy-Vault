// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.entity;

import iskallia.vault.block.SoulAltarBlock;
import iskallia.vault.block.base.FillableAltarBlock;
import iskallia.vault.block.base.FillableAltarTileEntity;
import iskallia.vault.entity.EternalEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.util.CodecUtils;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulAltarTileEntity extends FillableAltarTileEntity
{
    private static final Random rand;
    private static final float range = 8.0f;
    private static final AxisAlignedBB SEARCH_BOX;
    public static final String SOUL_ALTAR_TAG = "the_vault_SoulAltar";
    public static final String SOUL_ALTAR_REF = "the_vault_SoulAltarPos";
    private int ticksExisted;
    
    public SoulAltarTileEntity() {
        super(ModBlocks.SOUL_ALTAR_TILE_ENTITY);
        this.ticksExisted = 0;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (!this.getLevel().isClientSide()) {
            ++this.ticksExisted;
            if (this.ticksExisted % 10 != 0) {
                return;
            }
            this.getLevel().getLoadedEntitiesOfClass(LivingEntity.class, SoulAltarTileEntity.SEARCH_BOX.move(this.getBlockPos()), entity -> entity.isAlive() && !entity.isSpectator() && !entity.isInvulnerable() && entity.getType().getCategory() == EntityClassification.MONSTER).forEach(entity -> {
                if (entity.addTag("the_vault_SoulAltar")) {
                    CodecUtils.writeNBT((com.mojang.serialization.Codec<BlockPos>)BlockPos.CODEC, this.getBlockPos(), entity.getPersistentData(), "the_vault_SoulAltarPos");
                }
            });
        }
    }
    
    @SubscribeEvent
    public static void onLivingUpdate(final LivingEvent.LivingUpdateEvent event) {
        final LivingEntity entity = event.getEntityLiving();
        final World world = entity.getCommandSenderWorld();
        if (world.isClientSide() || !(world instanceof ServerWorld)) {
            return;
        }
        if (!entity.getTags().contains("the_vault_SoulAltar")) {
            return;
        }
        final CompoundNBT tag = entity.getPersistentData();
        if (!tag.contains("the_vault_SoulAltarPos")) {
            return;
        }
        final BlockPos altarRef = CodecUtils.readNBT((com.mojang.serialization.Codec<BlockPos>)BlockPos.CODEC, tag.get("the_vault_SoulAltarPos"), null);
        if (altarRef == null || !world.hasChunkAt(altarRef)) {
            return;
        }
        final BlockState state = world.getBlockState(altarRef);
        final TileEntity te = world.getBlockEntity(altarRef);
        if (!(te instanceof SoulAltarTileEntity) || !(state.getBlock() instanceof SoulAltarBlock)) {
            return;
        }
        final IParticleData particle = ((SoulAltarBlock)state.getBlock()).getFlameParticle();
        final Vector3d at = MiscUtils.getRandomOffset(entity.getBoundingBox().inflate(0.20000000298023224), SoulAltarTileEntity.rand);
        final ServerWorld sWorld = (ServerWorld)world;
        sWorld.sendParticles(particle, at.x, at.y, at.z, 1, 0.0, 0.0, 0.0, 0.0);
    }
    
    @SubscribeEvent
    public static void onEntityDead(final LivingDeathEvent event) {
        final LivingEntity deadEntity = event.getEntityLiving();
        final World world = deadEntity.getCommandSenderWorld();
        if (world.isClientSide()) {
            return;
        }
        final DamageSource src = event.getSource();
        Entity sourceEntity = src.getEntity();
        if (sourceEntity instanceof EternalEntity) {
            sourceEntity = ((EternalEntity)sourceEntity).getOwner().right().orElse(null);
        }
        if (!(sourceEntity instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity killer = (ServerPlayerEntity)sourceEntity;
        if (!deadEntity.getTags().contains("the_vault_SoulAltar")) {
            return;
        }
        final CompoundNBT tag = deadEntity.getPersistentData();
        if (!tag.contains("the_vault_SoulAltarPos")) {
            return;
        }
        final BlockPos altarRef = CodecUtils.readNBT((com.mojang.serialization.Codec<BlockPos>)BlockPos.CODEC, tag.get("the_vault_SoulAltarPos"), null);
        if (altarRef == null || !world.hasChunkAt(altarRef)) {
            return;
        }
        final TileEntity te = world.getBlockEntity(altarRef);
        if (!(te instanceof SoulAltarTileEntity) || !((SoulAltarTileEntity)te).initialized() || ((SoulAltarTileEntity)te).isMaxedOut()) {
            return;
        }
        ((SoulAltarTileEntity)te).makeProgress(killer, 1, sPlayer -> {
            final PlayerFavourData data = PlayerFavourData.get(sPlayer.getLevel());
            if (SoulAltarTileEntity.rand.nextFloat() < FillableAltarBlock.getFavourChance((PlayerEntity)sPlayer, PlayerFavourData.VaultGodType.MALEVOLENCE)) {
                final PlayerFavourData.VaultGodType vg = PlayerFavourData.VaultGodType.MALEVOLENCE;
                if (data.addFavour((PlayerEntity)sPlayer, vg, 1)) {
                    data.addFavour((PlayerEntity)sPlayer, vg.getOther(SoulAltarTileEntity.rand), -1);
                    FillableAltarBlock.playFavourInfo(sPlayer);
                }
            }
            return;
        });
        te.setChanged();
    }
    
    @Override
    public ITextComponent getRequirementName() {
        return (ITextComponent)new StringTextComponent("Monster Soul");
    }
    
    @Override
    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.MALEVOLENCE;
    }
    
    @Override
    public ITextComponent getRequirementUnit() {
        return (ITextComponent)new StringTextComponent("kills");
    }
    
    @Override
    public Color getFillColor() {
        return new Color(-2158319);
    }
    
    @Override
    protected Optional<Integer> calcMaxProgress(final VaultRaid vault) {
        return vault.getProperties().getBase(VaultRaid.LEVEL).map(vaultLevel -> {
            final float multiplier = vault.getProperties().getBase(VaultRaid.HOST).map(this::getMaxProgressMultiplier).orElse(1.0f);
            final int progress = 4 + vaultLevel / 7;
            return Integer.valueOf(Math.round(progress * multiplier));
        });
    }
    
    static {
        rand = new Random();
        SEARCH_BOX = new AxisAlignedBB(-8.0, -8.0, -8.0, 8.0, 8.0, 8.0);
    }
}
