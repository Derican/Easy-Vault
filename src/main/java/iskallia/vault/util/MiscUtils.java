// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import iskallia.vault.client.ClientTalentData;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.PlayerTalent;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MiscUtils
{
    private static final Random rand;
    
    public static <T> T eitherOf(final Random r, final T... selection) {
        if (selection.length == 0) {
            return null;
        }
        return selection[r.nextInt(selection.length)];
    }
    
    public static <T> List<T> concat(final List<T> list1, final T... elements) {
        return Stream.concat(list1.stream(), Arrays.stream(elements)).collect(Collectors.toList());
    }
    
    public static <T> List<T> concat(final List<T> list1, final List<T> list2) {
        return Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
    }
    
    public static Point2D.Float getMidpoint(final Rectangle r) {
        return new Point2D.Float(r.x + r.width / 2.0f, r.y + r.height / 2.0f);
    }
    
    public static <T extends PlayerTalent> Optional<TalentNode<T>> getTalent(final PlayerEntity player, final TalentGroup<T> talentGroup) {
        if (player instanceof ServerPlayerEntity) {
            final TalentTree talents = PlayerTalentsData.get(((ServerPlayerEntity)player).getLevel()).getTalents(player);
            return Optional.of(talents.getNodeOf(talentGroup));
        }
        return Optional.ofNullable(ClientTalentData.getLearnedTalentNode(talentGroup));
    }
    
    public static boolean hasEmptySlot(final IInventory inventory) {
        return getRandomEmptySlot(inventory) != -1;
    }
    
    public static boolean hasEmptySlot(final IItemHandler inventory) {
        return getRandomEmptySlot(inventory) != -1;
    }
    
    public static int getRandomEmptySlot(final IInventory inventory) {
        return getRandomEmptySlot((IItemHandler)new InvWrapper(inventory));
    }
    
    public static int getRandomEmptySlot(final IItemHandler handler) {
        final List<Integer> slots = new ArrayList<Integer>();
        for (int slot = 0; slot < handler.getSlots(); ++slot) {
            if (handler.getStackInSlot(slot).isEmpty()) {
                slots.add(slot);
            }
        }
        if (slots.isEmpty()) {
            return -1;
        }
        return getRandomEntry(slots, MiscUtils.rand);
    }
    
    public static int getRandomSlot(final IItemHandler handler) {
        final List<Integer> slots = new ArrayList<Integer>();
        for (int slot = 0; slot < handler.getSlots(); ++slot) {
            slots.add(slot);
        }
        if (slots.isEmpty()) {
            return -1;
        }
        return getRandomEntry(slots, MiscUtils.rand);
    }
    
    public static List<Integer> getEmptySlots(final IInventory inventory) {
        final List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            if (inventory.getItem(i).isEmpty()) {
                list.add(i);
            }
        }
        Collections.shuffle(list, MiscUtils.rand);
        return list;
    }
    
    public static boolean inventoryContains(final IInventory inventory, final Predicate<ItemStack> filter) {
        for (int slot = 0; slot < inventory.getContainerSize(); ++slot) {
            if (filter.test(inventory.getItem(slot))) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean inventoryContains(final IItemHandler handler, final Predicate<ItemStack> filter) {
        for (int slot = 0; slot < handler.getSlots(); ++slot) {
            if (filter.test(handler.getStackInSlot(slot))) {
                return true;
            }
        }
        return false;
    }
    
    public static List<ItemStack> mergeItemStacks(final List<ItemStack> stacks) {
        final List<ItemStack> out = new ArrayList<ItemStack>();
    Label_0015:
        for (final ItemStack stack : stacks) {
            if (stack.isEmpty()) {
                continue;
            }
            for (final ItemStack existing : out) {
                if (canMerge(existing, stack)) {
                    existing.setCount(existing.getCount() + stack.getCount());
                    continue Label_0015;
                }
            }
            out.add(stack);
        }
        return out;
    }
    
    public static List<ItemStack> splitAndLimitStackSize(final List<ItemStack> stacks) {
        final List<ItemStack> out = new ArrayList<ItemStack>();
        for (final ItemStack stack : stacks) {
            if (stack.isEmpty()) {
                continue;
            }
            int i = stack.getCount();
            while (i > 0) {
                final int newCount = Math.min(i, stack.getMaxStackSize());
                i -= newCount;
                final ItemStack copy = stack.copy();
                copy.setCount(newCount);
                out.add(copy);
            }
        }
        return out;
    }
    
    public static boolean canMerge(final ItemStack stack, final ItemStack other) {
        return stack.getItem() == other.getItem() && ItemStack.tagMatches(stack, other);
    }
    
    public static boolean addItemStack(final IInventory inventory, final ItemStack stack) {
        for (int slot = 0; slot < inventory.getContainerSize(); ++slot) {
            final ItemStack contained = inventory.getItem(slot);
            if (contained.isEmpty()) {
                inventory.setItem(slot, stack);
                return true;
            }
        }
        return false;
    }
    
    public static <T extends Enum<T>> T getEnumEntry(final Class<T> enumClass, final int index) {
        final T[] constants = enumClass.getEnumConstants();
        return constants[MathHelper.clamp(index, 0, constants.length - 1)];
    }
    
    public static Optional<BlockPos> getEmptyNearby(final IWorldReader world, final BlockPos pos) {
        return BlockPos.findClosestMatch(pos, 8, 8, world::isEmptyBlock);
    }
    
    public static BlockPos getRandomPos(final MutableBoundingBox box, final Random r) {
        return getRandomPos(AxisAlignedBB.of(box), r);
    }
    
    public static BlockPos getRandomPos(final AxisAlignedBB box, final Random r) {
        final int sizeX = Math.max(1, MathHelper.floor(box.getXsize()));
        final int sizeY = Math.max(1, MathHelper.floor(box.getYsize()));
        final int sizeZ = Math.max(1, MathHelper.floor(box.getZsize()));
        return new BlockPos(box.minX + r.nextInt(sizeX), box.minY + r.nextInt(sizeY), box.minZ + r.nextInt(sizeZ));
    }
    
    public static Vector3d getRandomOffset(final AxisAlignedBB box, final Random r) {
        return new Vector3d(box.minX + r.nextFloat() * (box.maxX - box.minX), box.minY + r.nextFloat() * (box.maxY - box.minY), box.minZ + r.nextFloat() * (box.maxZ - box.minZ));
    }
    
    public static Vector3d getRandomOffset(final BlockPos pos, final Random r) {
        return new Vector3d((double)(pos.getX() + r.nextFloat()), (double)(pos.getY() + r.nextFloat()), (double)(pos.getZ() + r.nextFloat()));
    }
    
    public static Vector3d getRandomOffset(final BlockPos pos, final Random r, final float scale) {
        final float x = pos.getX() + 0.5f - scale / 2.0f + r.nextFloat() * scale;
        final float y = pos.getY() + 0.5f - scale / 2.0f + r.nextFloat() * scale;
        final float z = pos.getZ() + 0.5f - scale / 2.0f + r.nextFloat() * scale;
        return new Vector3d((double)x, (double)y, (double)z);
    }
    
    public static Collection<ChunkPos> getChunksContaining(final AxisAlignedBB box) {
        return getChunksContaining(new Vector3i(box.minX, box.minY, box.minZ), new Vector3i(box.maxX, box.maxY, box.maxZ));
    }
    
    public static Collection<ChunkPos> getChunksContaining(final Vector3i min, final Vector3i max) {
        final List<ChunkPos> affected = Lists.newArrayList();
        final int maxX = max.getX() >> 4;
        final int maxZ = max.getZ() >> 4;
        for (int chX = min.getX() >> 4; chX <= maxX; ++chX) {
            for (int chZ = min.getZ() >> 4; chZ <= maxZ; ++chZ) {
                affected.add(new ChunkPos(chX, chZ));
            }
        }
        return affected;
    }
    
    @Nullable
    public static <T> T getRandomEntry(final Collection<T> collection, final Random rand) {
        if (collection.isEmpty()) {
            return null;
        }
        final int randomPick = rand.nextInt(collection.size());
        return (T)Iterables.get((Iterable)collection, randomPick, null);
    }
    
    public static void broadcast(final ITextComponent message) {
        final MinecraftServer srv = (MinecraftServer)LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        if (srv != null) {
            srv.getPlayerList().broadcastMessage(message, ChatType.CHAT, Util.NIL_UUID);
        }
    }
    
    public static Color blendColors(final Color color1, final Color color2, final float color1Ratio) {
        return new Color(blendColors(color1.getRGB(), color2.getRGB(), color1Ratio), true);
    }
    
    public static int blendColors(final int color1, final int color2, final float color1Ratio) {
        final float ratio1 = MathHelper.clamp(color1Ratio, 0.0f, 1.0f);
        final float ratio2 = 1.0f - ratio1;
        final int a1 = (color1 & 0xFF000000) >> 24;
        final int r1 = (color1 & 0xFF0000) >> 16;
        final int g1 = (color1 & 0xFF00) >> 8;
        final int b1 = color1 & 0xFF;
        final int a2 = (color2 & 0xFF000000) >> 24;
        final int r2 = (color2 & 0xFF0000) >> 16;
        final int g2 = (color2 & 0xFF00) >> 8;
        final int b2 = color2 & 0xFF;
        final int a3 = MathHelper.clamp(Math.round(a1 * ratio1 + a2 * ratio2), 0, 255);
        final int r3 = MathHelper.clamp(Math.round(r1 * ratio1 + r2 * ratio2), 0, 255);
        final int g3 = MathHelper.clamp(Math.round(g1 * ratio1 + g2 * ratio2), 0, 255);
        final int b3 = MathHelper.clamp(Math.round(b1 * ratio1 + b2 * ratio2), 0, 255);
        return a3 << 24 | r3 << 16 | g3 << 8 | b3;
    }
    
    public static Color overlayColor(final Color base, final Color overlay) {
        return new Color(overlayColor(base.getRGB(), overlay.getRGB()), true);
    }
    
    public static int overlayColor(final int base, final int overlay) {
        final int alpha = (base & 0xFF000000) >> 24;
        final int baseR = (base & 0xFF0000) >> 16;
        final int baseG = (base & 0xFF00) >> 8;
        final int baseB = base & 0xFF;
        final int overlayR = (overlay & 0xFF0000) >> 16;
        final int overlayG = (overlay & 0xFF00) >> 8;
        final int overlayB = overlay & 0xFF;
        final int r = Math.round(baseR * (overlayR / 255.0f)) & 0xFF;
        final int g = Math.round(baseG * (overlayG / 255.0f)) & 0xFF;
        final int b = Math.round(baseB * (overlayB / 255.0f)) & 0xFF;
        return alpha << 24 | r << 16 | g << 8 | b;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static int getOverlayColor(final ItemStack stack) {
        if (stack.isEmpty()) {
            return -1;
        }
        if (!(stack.getItem() instanceof BlockItem)) {
            return Minecraft.getInstance().getItemColors().getColor(stack, 0);
        }
        final Block b = Block.byItem(stack.getItem());
        if (b == Blocks.AIR) {
            return -1;
        }
        final BlockState state = b.defaultBlockState();
        return Minecraft.getInstance().getBlockColors().getColor(state, (IBlockDisplayReader)null, (BlockPos)null, 0);
    }
    
    @Nullable
    public static PlayerEntity findPlayerUsingAnvil(final ItemStack left, final ItemStack right) {
        for (final PlayerEntity player : SidedHelper.getSidedPlayers()) {
            if (player.containerMenu instanceof RepairContainer) {
                final NonNullList<ItemStack> contents = (NonNullList<ItemStack>)player.containerMenu.getItems();
                if (contents.get(0) == left && contents.get(1) == right) {
                    return player;
                }
                continue;
            }
        }
        return null;
    }
    
    public static void fillContainer(final Container ct, final NonNullList<ItemStack> items) {
        for (int slot = 0; slot < items.size(); ++slot) {
            ct.setItem(slot, (ItemStack)items.get(slot));
        }
    }
    
    public static void giveItem(final ServerPlayerEntity player, ItemStack stack) {
        stack = stack.copy();
        if (player.inventory.add(stack) && stack.isEmpty()) {
            stack.setCount(1);
            final ItemEntity dropped = player.drop(stack, false);
            if (dropped != null) {
                dropped.makeFakeItem();
            }
            player.level.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
            player.inventoryMenu.broadcastChanges();
        }
        else {
            final ItemEntity dropped = player.drop(stack, false);
            if (dropped != null) {
                dropped.setNoPickUpDelay();
                dropped.setOwner(player.getUUID());
            }
        }
    }
    
    public static Vector3f getRandomCirclePosition(final Vector3f centerOffset, final Vector3f axis, final float radius) {
        return getCirclePosition(centerOffset, axis, radius, (float)(Math.random() * 360.0));
    }
    
    public static Vector3f getCirclePosition(final Vector3f centerOffset, final Vector3f axis, final float radius, final float degree) {
        Vector3f circleVec = normalize(perpendicular(axis));
        circleVec = new Vector3f(circleVec.x() * radius, circleVec.y() * radius, circleVec.z() * radius);
        final Quaternion rotQuat = new Quaternion(axis, degree, true);
        circleVec.transform(rotQuat);
        return new Vector3f(circleVec.x() + centerOffset.x(), circleVec.y() + centerOffset.y(), circleVec.z() + centerOffset.z());
    }
    
    public static Vector3f normalize(final Vector3f vec) {
        final float lengthSq = vec.x() * vec.x() + vec.y() * vec.y() + vec.z() * vec.z();
        final float length = (float)Math.sqrt(lengthSq);
        return new Vector3f(vec.x() / length, vec.y() / length, vec.z() / length);
    }
    
    public static Vector3f perpendicular(final Vector3f vec) {
        if (vec.z() == 0.0) {
            return new Vector3f(vec.y(), -vec.x(), 0.0f);
        }
        return new Vector3f(0.0f, vec.z(), -vec.y());
    }
    
    public static boolean isPlayerFakeMP(final ServerPlayerEntity player) {
        if (player instanceof FakePlayer) {
            return true;
        }
        if (player.connection == null) {
            return true;
        }
        try {
            player.getIpAddress().length();
            player.connection.connection.getRemoteAddress().toString();
            if (!player.connection.connection.channel().isOpen()) {
                return true;
            }
        }
        catch (final Exception exc) {
            return true;
        }
        return false;
    }
    
    static {
        rand = new Random();
    }
}
