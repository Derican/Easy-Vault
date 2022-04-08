package iskallia.vault.util;

import com.google.common.collect.Lists;
import iskallia.vault.client.ClientTalentData;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
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
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MiscUtils {
    private static final Random rand = new Random();

    public static <T> T eitherOf(Random r, T... selection) {
        if (selection.length == 0) {
            return null;
        }
        return selection[r.nextInt(selection.length)];
    }

    public static <T> List<T> concat(List<T> list1, T... elements) {
        return (List<T>) Stream.concat(list1.stream(), Arrays.stream((Object[]) elements)).collect(Collectors.toList());
    }

    public static <T> List<T> concat(List<T> list1, List<T> list2) {
        return (List<T>) Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
    }

    public static Point2D.Float getMidpoint(Rectangle r) {
        return new Point2D.Float(r.x + r.width / 2.0F, r.y + r.height / 2.0F);
    }

    public static <T extends iskallia.vault.skill.talent.type.PlayerTalent> Optional<TalentNode<T>> getTalent(PlayerEntity player, TalentGroup<T> talentGroup) {
        if (player instanceof ServerPlayerEntity) {
            TalentTree talents = PlayerTalentsData.get(((ServerPlayerEntity) player).getLevel()).getTalents(player);
            return Optional.of(talents.getNodeOf(talentGroup));
        }
        return Optional.ofNullable(ClientTalentData.getLearnedTalentNode(talentGroup));
    }


    public static boolean hasEmptySlot(IInventory inventory) {
        return (getRandomEmptySlot(inventory) != -1);
    }

    public static boolean hasEmptySlot(IItemHandler inventory) {
        return (getRandomEmptySlot(inventory) != -1);
    }

    public static int getRandomEmptySlot(IInventory inventory) {
        return getRandomEmptySlot((IItemHandler) new InvWrapper(inventory));
    }

    public static int getRandomEmptySlot(IItemHandler handler) {
        List<Integer> slots = new ArrayList<>();
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            if (handler.getStackInSlot(slot).isEmpty()) {
                slots.add(Integer.valueOf(slot));
            }
        }
        if (slots.isEmpty()) {
            return -1;
        }
        return ((Integer) getRandomEntry(slots, rand)).intValue();
    }

    public static int getRandomSlot(IItemHandler handler) {
        List<Integer> slots = new ArrayList<>();
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            slots.add(Integer.valueOf(slot));
        }
        if (slots.isEmpty()) {
            return -1;
        }
        return ((Integer) getRandomEntry(slots, rand)).intValue();
    }

    public static List<Integer> getEmptySlots(IInventory inventory) {
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).isEmpty()) {
                list.add(Integer.valueOf(i));
            }
        }
        Collections.shuffle(list, rand);
        return list;
    }

    public static boolean inventoryContains(IInventory inventory, Predicate<ItemStack> filter) {
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (filter.test(inventory.getItem(slot))) {
                return true;
            }
        }
        return false;
    }

    public static boolean inventoryContains(IItemHandler handler, Predicate<ItemStack> filter) {
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            if (filter.test(handler.getStackInSlot(slot))) {
                return true;
            }
        }
        return false;
    }

    public static List<ItemStack> mergeItemStacks(List<ItemStack> stacks) {
        List<ItemStack> out = new ArrayList<>();

        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) {
                continue;
            }
            for (ItemStack existing : out) {
                if (canMerge(existing, stack)) {
                    existing.setCount(existing.getCount() + stack.getCount());
                }
            }

            out.add(stack);
        }
        return out;
    }

    public static List<ItemStack> splitAndLimitStackSize(List<ItemStack> stacks) {
        List<ItemStack> out = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) {
                continue;
            }
            for (int i = stack.getCount(); i > 0; ) {
                int newCount = Math.min(i, stack.getMaxStackSize());
                i -= newCount;

                ItemStack copy = stack.copy();
                copy.setCount(newCount);
                out.add(copy);
            }
        }
        return out;
    }

    public static boolean canMerge(ItemStack stack, ItemStack other) {
        return (stack.getItem() == other.getItem() && ItemStack.tagMatches(stack, other));
    }

    public static boolean addItemStack(IInventory inventory, ItemStack stack) {
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack contained = inventory.getItem(slot);
            if (contained.isEmpty()) {
                inventory.setItem(slot, stack);
                return true;
            }
        }
        return false;
    }

    public static <T extends Enum<T>> T getEnumEntry(Class<T> enumClass, int index) {
        Enum[] arrayOfEnum = (Enum[]) enumClass.getEnumConstants();
        return (T) arrayOfEnum[MathHelper.clamp(index, 0, arrayOfEnum.length - 1)];
    }

    public static Optional<BlockPos> getEmptyNearby(IWorldReader world, BlockPos pos) {
        return BlockPos.findClosestMatch(pos, 8, 8, world::isEmptyBlock);
    }

    public static BlockPos getRandomPos(MutableBoundingBox box, Random r) {
        return getRandomPos(AxisAlignedBB.of(box), r);
    }

    public static BlockPos getRandomPos(AxisAlignedBB box, Random r) {
        int sizeX = Math.max(1, MathHelper.floor(box.getXsize()));
        int sizeY = Math.max(1, MathHelper.floor(box.getYsize()));
        int sizeZ = Math.max(1, MathHelper.floor(box.getZsize()));
        return new BlockPos(box.minX + r
                .nextInt(sizeX), box.minY + r
                .nextInt(sizeY), box.minZ + r
                .nextInt(sizeZ));
    }

    public static Vector3d getRandomOffset(AxisAlignedBB box, Random r) {
        return new Vector3d(box.minX + r
                .nextFloat() * (box.maxX - box.minX), box.minY + r
                .nextFloat() * (box.maxY - box.minY), box.minZ + r
                .nextFloat() * (box.maxZ - box.minZ));
    }


    public static Vector3d getRandomOffset(BlockPos pos, Random r) {
        return new Vector3d((pos.getX() + r.nextFloat()), (pos.getY() + r.nextFloat()), (pos.getZ() + r.nextFloat()));
    }

    public static Vector3d getRandomOffset(BlockPos pos, Random r, float scale) {
        float x = pos.getX() + 0.5F - scale / 2.0F + r.nextFloat() * scale;
        float y = pos.getY() + 0.5F - scale / 2.0F + r.nextFloat() * scale;
        float z = pos.getZ() + 0.5F - scale / 2.0F + r.nextFloat() * scale;
        return new Vector3d(x, y, z);
    }

    public static Collection<ChunkPos> getChunksContaining(AxisAlignedBB box) {
        return getChunksContaining(new Vector3i(box.minX, box.minY, box.minZ), new Vector3i(box.maxX, box.maxY, box.maxZ));
    }

    public static Collection<ChunkPos> getChunksContaining(Vector3i min, Vector3i max) {
        List<ChunkPos> affected = Lists.newArrayList();
        int maxX = max.getX() >> 4;
        int maxZ = max.getZ() >> 4;
        for (int chX = min.getX() >> 4; chX <= maxX; chX++) {
            for (int chZ = min.getZ() >> 4; chZ <= maxZ; chZ++) {
                affected.add(new ChunkPos(chX, chZ));
            }
        }
        return affected;
    }

    @Nullable
    public static <T> T getRandomEntry(Collection<T> collection, Random rand) {
        if (collection.isEmpty()) {
            return null;
        }
        int randomPick = rand.nextInt(collection.size());
        return (T) Iterables.get(collection, randomPick, null);
    }

    public static void broadcast(ITextComponent message) {
        MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        if (srv != null) {
            srv.getPlayerList().broadcastMessage(message, ChatType.CHAT, Util.NIL_UUID);
        }
    }

    public static Color blendColors(Color color1, Color color2, float color1Ratio) {
        return new Color(blendColors(color1.getRGB(), color2.getRGB(), color1Ratio), true);
    }

    public static int blendColors(int color1, int color2, float color1Ratio) {
        float ratio1 = MathHelper.clamp(color1Ratio, 0.0F, 1.0F);
        float ratio2 = 1.0F - ratio1;

        int a1 = (color1 & 0xFF000000) >> 24;
        int r1 = (color1 & 0xFF0000) >> 16;
        int g1 = (color1 & 0xFF00) >> 8;
        int b1 = color1 & 0xFF;

        int a2 = (color2 & 0xFF000000) >> 24;
        int r2 = (color2 & 0xFF0000) >> 16;
        int g2 = (color2 & 0xFF00) >> 8;
        int b2 = color2 & 0xFF;

        int a = MathHelper.clamp(Math.round(a1 * ratio1 + a2 * ratio2), 0, 255);
        int r = MathHelper.clamp(Math.round(r1 * ratio1 + r2 * ratio2), 0, 255);
        int g = MathHelper.clamp(Math.round(g1 * ratio1 + g2 * ratio2), 0, 255);
        int b = MathHelper.clamp(Math.round(b1 * ratio1 + b2 * ratio2), 0, 255);

        return a << 24 | r << 16 | g << 8 | b;
    }

    public static Color overlayColor(Color base, Color overlay) {
        return new Color(overlayColor(base.getRGB(), overlay.getRGB()), true);
    }

    public static int overlayColor(int base, int overlay) {
        int alpha = (base & 0xFF000000) >> 24;

        int baseR = (base & 0xFF0000) >> 16;
        int baseG = (base & 0xFF00) >> 8;
        int baseB = base & 0xFF;

        int overlayR = (overlay & 0xFF0000) >> 16;
        int overlayG = (overlay & 0xFF00) >> 8;
        int overlayB = overlay & 0xFF;

        int r = Math.round(baseR * overlayR / 255.0F) & 0xFF;
        int g = Math.round(baseG * overlayG / 255.0F) & 0xFF;
        int b = Math.round(baseB * overlayB / 255.0F) & 0xFF;

        return alpha << 24 | r << 16 | g << 8 | b;
    }

    @OnlyIn(Dist.CLIENT)
    public static int getOverlayColor(ItemStack stack) {
        if (stack.isEmpty()) {
            return -1;
        }
        if (stack.getItem() instanceof net.minecraft.item.BlockItem) {
            Block b = Block.byItem(stack.getItem());
            if (b == Blocks.AIR) {
                return -1;
            }
            BlockState state = b.defaultBlockState();
            return Minecraft.getInstance().getBlockColors().getColor(state, null, null, 0);
        }
        return Minecraft.getInstance().getItemColors().getColor(stack, 0);
    }


    @Nullable
    public static PlayerEntity findPlayerUsingAnvil(ItemStack left, ItemStack right) {
        for (PlayerEntity player : SidedHelper.getSidedPlayers()) {
            if (player.containerMenu instanceof net.minecraft.inventory.container.RepairContainer) {
                NonNullList<ItemStack> contents = player.containerMenu.getItems();
                if (contents.get(0) == left && contents.get(1) == right) {
                    return player;
                }
            }
        }
        return null;
    }

    public static void fillContainer(Container ct, NonNullList<ItemStack> items) {
        for (int slot = 0; slot < items.size(); slot++) {
            ct.setItem(slot, (ItemStack) items.get(slot));
        }
    }

    public static void giveItem(ServerPlayerEntity player, ItemStack stack) {
        stack = stack.copy();
        if (player.inventory.add(stack) && stack.isEmpty()) {
            stack.setCount(1);
            ItemEntity dropped = player.drop(stack, false);
            if (dropped != null) {
                dropped.makeFakeItem();
            }

            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player

                    .getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryMenu.broadcastChanges();
        } else {
            ItemEntity dropped = player.drop(stack, false);
            if (dropped != null) {
                dropped.setNoPickUpDelay();
                dropped.setOwner(player.getUUID());
            }
        }
    }

    public static Vector3f getRandomCirclePosition(Vector3f centerOffset, Vector3f axis, float radius) {
        return getCirclePosition(centerOffset, axis, radius, (float) (Math.random() * 360.0D));
    }

    public static Vector3f getCirclePosition(Vector3f centerOffset, Vector3f axis, float radius, float degree) {
        Vector3f circleVec = normalize(perpendicular(axis));
        circleVec = new Vector3f(circleVec.x() * radius, circleVec.y() * radius, circleVec.z() * radius);
        Quaternion rotQuat = new Quaternion(axis, degree, true);
        circleVec.transform(rotQuat);
        return new Vector3f(circleVec.x() + centerOffset.x(), circleVec.y() + centerOffset.y(), circleVec.z() + centerOffset.z());
    }

    public static Vector3f normalize(Vector3f vec) {
        float lengthSq = vec.x() * vec.x() + vec.y() * vec.y() + vec.z() * vec.z();
        float length = (float) Math.sqrt(lengthSq);
        return new Vector3f(vec.x() / length, vec.y() / length, vec.z() / length);
    }

    public static Vector3f perpendicular(Vector3f vec) {
        if (vec.z() == 0.0D) {
            return new Vector3f(vec.y(), -vec.x(), 0.0F);
        }
        return new Vector3f(0.0F, vec.z(), -vec.y());
    }

    public static boolean isPlayerFakeMP(ServerPlayerEntity player) {
        if (player instanceof net.minecraftforge.common.util.FakePlayer) {
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
        } catch (Exception exc) {
            return true;
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\MiscUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */