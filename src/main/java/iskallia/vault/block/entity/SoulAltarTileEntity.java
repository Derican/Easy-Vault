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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.awt.*;
import java.util.Optional;
import java.util.Random;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulAltarTileEntity
        extends FillableAltarTileEntity {
    private static final Random rand = new Random();
    private static final float range = 8.0F;
    private static final AxisAlignedBB SEARCH_BOX = new AxisAlignedBB(-8.0D, -8.0D, -8.0D, 8.0D, 8.0D, 8.0D);

    public static final String SOUL_ALTAR_TAG = "the_vault_SoulAltar";

    public static final String SOUL_ALTAR_REF = "the_vault_SoulAltarPos";
    private int ticksExisted = 0;

    public SoulAltarTileEntity() {
        super(ModBlocks.SOUL_ALTAR_TILE_ENTITY);
    }


    public void tick() {
        super.tick();

        if (!getLevel().isClientSide()) {
            this.ticksExisted++;

            if (this.ticksExisted % 10 != 0) {
                return;
            }

            getLevel().getLoadedEntitiesOfClass(LivingEntity.class, SEARCH_BOX

                            .move(getBlockPos()), entity ->
                            (entity.isAlive() && !entity.isSpectator() && !entity.isInvulnerable() && entity.getType().getCategory() == EntityClassification.MONSTER))


                    .forEach(entity -> {
                        if (entity.addTag("the_vault_SoulAltar")) {
                            CodecUtils.writeNBT(BlockPos.CODEC, getBlockPos(), entity.getPersistentData(), "the_vault_SoulAltarPos");
                        }
                    });
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.getCommandSenderWorld();
        if (world.isClientSide() || !(world instanceof ServerWorld)) {
            return;
        }
        if (!entity.getTags().contains("the_vault_SoulAltar")) {
            return;
        }
        CompoundNBT tag = entity.getPersistentData();
        if (!tag.contains("the_vault_SoulAltarPos")) {
            return;
        }
        BlockPos altarRef = (BlockPos) CodecUtils.readNBT(BlockPos.CODEC, tag.get("the_vault_SoulAltarPos"), null);
        if (altarRef == null || !world.hasChunkAt(altarRef)) {
            return;
        }
        BlockState state = world.getBlockState(altarRef);
        TileEntity te = world.getBlockEntity(altarRef);
        if (!(te instanceof SoulAltarTileEntity) || !(state.getBlock() instanceof SoulAltarBlock)) {
            return;
        }
        IParticleData particle = ((SoulAltarBlock) state.getBlock()).getFlameParticle();
        Vector3d at = MiscUtils.getRandomOffset(entity.getBoundingBox().inflate(0.20000000298023224D), rand);
        ServerWorld sWorld = (ServerWorld) world;

        sWorld.sendParticles(particle, at.x, at.y, at.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    @SubscribeEvent
    public static void onEntityDead(LivingDeathEvent event) {
        LivingEntity deadEntity = event.getEntityLiving();
        World world = deadEntity.getCommandSenderWorld();
        if (world.isClientSide()) {
            return;
        }

        DamageSource src = event.getSource();
        Entity sourceEntity = src.getEntity();
        if (sourceEntity instanceof EternalEntity) {
            sourceEntity = ((EternalEntity) sourceEntity).getOwner().right().orElse(null);
        }
        if (!(sourceEntity instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity killer = (ServerPlayerEntity) sourceEntity;

        if (!deadEntity.getTags().contains("the_vault_SoulAltar")) {
            return;
        }
        CompoundNBT tag = deadEntity.getPersistentData();
        if (!tag.contains("the_vault_SoulAltarPos")) {
            return;
        }
        BlockPos altarRef = (BlockPos) CodecUtils.readNBT(BlockPos.CODEC, tag.get("the_vault_SoulAltarPos"), null);
        if (altarRef == null || !world.hasChunkAt(altarRef)) {
            return;
        }
        TileEntity te = world.getBlockEntity(altarRef);
        if (!(te instanceof SoulAltarTileEntity) || !((SoulAltarTileEntity) te).initialized() || ((SoulAltarTileEntity) te).isMaxedOut()) {
            return;
        }
        ((SoulAltarTileEntity) te).makeProgress(killer, 1, sPlayer -> {
            PlayerFavourData data = PlayerFavourData.get(sPlayer.getLevel());
            if (rand.nextFloat() < FillableAltarBlock.getFavourChance((PlayerEntity) sPlayer, PlayerFavourData.VaultGodType.MALEVOLENCE)) {
                PlayerFavourData.VaultGodType vg = PlayerFavourData.VaultGodType.MALEVOLENCE;
                if (data.addFavour((PlayerEntity) sPlayer, vg, 1)) {
                    data.addFavour((PlayerEntity) sPlayer, vg.getOther(rand), -1);
                    FillableAltarBlock.playFavourInfo(sPlayer);
                }
            }
        });
        te.setChanged();
    }


    public ITextComponent getRequirementName() {
        return (ITextComponent) new StringTextComponent("Monster Soul");
    }


    public PlayerFavourData.VaultGodType getAssociatedVaultGod() {
        return PlayerFavourData.VaultGodType.MALEVOLENCE;
    }


    public ITextComponent getRequirementUnit() {
        return (ITextComponent) new StringTextComponent("kills");
    }


    public net.minecraft.util.text.Color getFillColor() {
        Color color = new Color(2158319);
        return net.minecraft.util.text.Color.fromRgb(color.getRGB());
    }


    protected Optional<Integer> calcMaxProgress(VaultRaid vault) {
        return vault.getProperties().getBase(VaultRaid.LEVEL).map(vaultLevel -> {
            float multiplier = ((Float) vault.getProperties().getBase(VaultRaid.HOST).map(this::getMaxProgressMultiplier).orElse(Float.valueOf(1.0F))).floatValue();
            int progress = 4 + vaultLevel.intValue() / 7;
            return Integer.valueOf(Math.round(progress * multiplier));
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\entity\SoulAltarTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */