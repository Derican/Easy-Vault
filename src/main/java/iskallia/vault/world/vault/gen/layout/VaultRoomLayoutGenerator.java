package iskallia.vault.world.vault.gen.layout;

import java.util.Objects;

import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;

import java.util.Random;

import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

import iskallia.vault.world.gen.structure.JigsawPatternFilter;

import java.util.Collection;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;

import net.minecraft.util.math.vector.Vector3i;

import java.util.Map;

import net.minecraft.nbt.CompoundNBT;
import iskallia.vault.Vault;
import net.minecraft.util.ResourceLocation;

public abstract class VaultRoomLayoutGenerator implements JigsawPoolProvider {
    private final ResourceLocation id;
    private ResourceLocation startRoomId;
    private ResourceLocation roomId;
    private ResourceLocation tunnelId;

    protected VaultRoomLayoutGenerator(final ResourceLocation id) {
        this.startRoomId = Vault.id("vault/starts");
        this.roomId = Vault.id("vault/rooms");
        this.tunnelId = Vault.id("vault/tunnels");
        this.id = id;
    }

    public final ResourceLocation getId() {
        return this.id;
    }

    @Override
    public ResourceLocation getStartRoomId() {
        return this.startRoomId;
    }

    @Override
    public ResourceLocation getRoomId() {
        return this.roomId;
    }

    @Override
    public ResourceLocation getTunnelId() {
        return this.tunnelId;
    }

    public void setStartRoomId(final ResourceLocation startRoomId) {
        this.startRoomId = startRoomId;
    }

    public void setRoomId(final ResourceLocation roomId) {
        this.roomId = roomId;
    }

    public void setTunnelId(final ResourceLocation tunnelId) {
        this.tunnelId = tunnelId;
    }

    public abstract void setSize(final int p0);

    public abstract Layout generateLayout();

    protected CompoundNBT serialize() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("startRoomId", this.startRoomId.toString());
        nbt.putString("roomId", this.roomId.toString());
        nbt.putString("tunnelId", this.tunnelId.toString());
        return nbt;
    }

    protected void deserialize(final CompoundNBT tag) {
        if (tag.contains("startRoomId", 8)) {
            this.startRoomId = new ResourceLocation(tag.getString("startRoomId"));
        }
        if (tag.contains("roomId", 8)) {
            this.roomId = new ResourceLocation(tag.getString("roomId"));
        }
        if (tag.contains("tunnelId", 8)) {
            this.tunnelId = new ResourceLocation(tag.getString("tunnelId"));
        }
    }

    public static class Layout {
        private final Map<Vector3i, Room> rooms;
        private final Set<Tunnel> tunnels;

        public Layout() {
            this.rooms = new HashMap<Vector3i, Room>();
            this.tunnels = new HashSet<Tunnel>();
        }

        protected void putRoom(final Vector3i roomPosition) {
            this.putRoom(new Room(roomPosition));
        }

        protected void putRoom(final Room room) {
            this.rooms.put(room.getRoomPosition(), room);
        }

        @Nullable
        public Room getRoom(final Vector3i v) {
            return this.rooms.get(v);
        }

        public Collection<Room> getRooms() {
            return this.rooms.values();
        }

        protected void addTunnel(final Room from, final Room to) {
            this.addTunnel(new Tunnel(from, to));
        }

        protected void addTunnel(final Tunnel tunnel) {
            this.tunnels.add(tunnel);
        }

        public Collection<Tunnel> getTunnels() {
            return this.tunnels;
        }
    }

    public static class Room {
        protected final Vector3i roomPosition;
        private final JigsawPatternFilter jigsawFilter;

        public Room(final Vector3i roomPosition) {
            this.jigsawFilter = new JigsawPatternFilter();
            this.roomPosition = roomPosition;
        }

        public Room andFilter(final Predicate<ResourceLocation> roomPieceFilter) {
            this.jigsawFilter.andMatches(roomPieceFilter);
            return this;
        }

        public Vector3i getRoomPosition() {
            return this.roomPosition;
        }

        public boolean canGenerateTreasureRooms() {
            return true;
        }

        public BlockPos getRoomOffset() {
            return new BlockPos(this.getRoomPosition().getX() * 47 + this.getRoomPosition().getX() * 48, 0, this.getRoomPosition().getZ() * 47 + this.getRoomPosition().getZ() * 48);
        }

        public BlockPos getAbsoluteOffset(final Rotation vaultRotation, final Rotation roomRotation) {
            return this.getRoomOffset().rotate(vaultRotation).offset((Vector3i) new BlockPos(-23, -13, -23).rotate(roomRotation));
        }

        public JigsawPiece getRandomPiece(final JigsawPattern pattern, final Random random) {
            return this.jigsawFilter.getRandomPiece(pattern, random);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Room room = (Room) o;
            return Objects.equals(this.roomPosition, room.roomPosition);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.roomPosition);
        }
    }

    public static class Tunnel {
        private final Room from;
        private final Room to;
        private final JigsawPatternFilter jigsawFilter;

        public Tunnel(final Room from, final Room to) {
            this.jigsawFilter = new JigsawPatternFilter();
            this.from = from;
            this.to = to;
        }

        public Tunnel(final Room from, final Room to, final Predicate<ResourceLocation> tunnelPieceFilter) {
            this(from, to);
            this.jigsawFilter.andMatches(tunnelPieceFilter);
        }

        public Room getFrom() {
            return this.from;
        }

        public Room getTo() {
            return this.to;
        }

        public Rotation getRandomConnectingRotation(final Random random) {
            if (this.getFrom().getRoomPosition().getX() - this.getTo().getRoomPosition().getX() == 0) {
                return Rotation.CLOCKWISE_180;
            }
            return Rotation.CLOCKWISE_90;
        }

        public BlockPos getAbsoluteOffset(final Rotation vaultRotation, final Rotation tunnelRotation) {
            final Vector3i from = this.getFrom().getRoomPosition();
            final Vector3i to = this.getTo().getRoomPosition();
            final Vector3i dir = new Vector3i(to.getX() - from.getX(), 0, to.getZ() - from.getZ());
            BlockPos relativeOffset = this.getFrom().getRoomOffset().offset(dir.getX() * 47, 0, dir.getZ() * 47);
            if (dir.getX() < 0) {
                relativeOffset = relativeOffset.offset(-1, 0, 0);
            }
            if (dir.getZ() < 0) {
                relativeOffset = relativeOffset.offset(0, 0, -1);
            }
            return relativeOffset.rotate(vaultRotation).offset((Vector3i) new BlockPos(-5, 6, -24).rotate(tunnelRotation));
        }

        public JigsawPiece getRandomPiece(final JigsawPattern pattern, final Random random) {
            return this.jigsawFilter.getRandomPiece(pattern, random);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Tunnel tunnel = (Tunnel) o;
            return (Objects.equals(this.from, tunnel.from) && Objects.equals(this.to, tunnel.to)) || (Objects.equals(this.from, tunnel.to) && Objects.equals(this.to, tunnel.from));
        }

        @Override
        public int hashCode() {
            return this.from.hashCode() ^ this.to.hashCode();
        }
    }
}
