package iskallia.vault.world.vault.gen.layout;

import iskallia.vault.world.gen.structure.JigsawPatternFilter;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public abstract class VaultRoomLayoutGenerator implements JigsawPoolProvider {
    protected VaultRoomLayoutGenerator(ResourceLocation id) {
        this.id = id;
    }

    private final ResourceLocation id;

    public final ResourceLocation getId() {
        return this.id;
    }

    public abstract void setSize(int paramInt);

    public abstract Layout generateLayout();

    protected CompoundNBT serialize() {
        return new CompoundNBT();
    }

    protected void deserialize(CompoundNBT tag) {
    }

    public static class Layout {
        private final Map<Vector3i, VaultRoomLayoutGenerator.Room> rooms = new HashMap<>();
        private final Set<VaultRoomLayoutGenerator.Tunnel> tunnels = new HashSet<>();

        protected void putRoom(Vector3i roomPosition) {
            putRoom(new VaultRoomLayoutGenerator.Room(roomPosition));
        }

        protected void putRoom(VaultRoomLayoutGenerator.Room room) {
            this.rooms.put(room.getRoomPosition(), room);
        }

        @Nullable
        public VaultRoomLayoutGenerator.Room getRoom(Vector3i v) {
            return this.rooms.get(v);
        }

        public Collection<VaultRoomLayoutGenerator.Room> getRooms() {
            return this.rooms.values();
        }

        protected void addTunnel(VaultRoomLayoutGenerator.Room from, VaultRoomLayoutGenerator.Room to) {
            addTunnel(new VaultRoomLayoutGenerator.Tunnel(from, to));
        }

        protected void addTunnel(VaultRoomLayoutGenerator.Tunnel tunnel) {
            this.tunnels.add(tunnel);
        }

        public Collection<VaultRoomLayoutGenerator.Tunnel> getTunnels() {
            return this.tunnels;
        }
    }

    public static class Room {
        protected final Vector3i roomPosition;
        private final JigsawPatternFilter jigsawFilter = new JigsawPatternFilter();

        public Room(Vector3i roomPosition) {
            this.roomPosition = roomPosition;
        }

        public Room andFilter(Predicate<ResourceLocation> roomPieceFilter) {
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
            return new BlockPos(
                    getRoomPosition().getX() * 47 + getRoomPosition().getX() * 48, 0,

                    getRoomPosition().getZ() * 47 + getRoomPosition().getZ() * 48);
        }

        public BlockPos getAbsoluteOffset(Rotation vaultRotation, Rotation roomRotation) {
            return getRoomOffset().rotate(vaultRotation)
                    .offset((Vector3i) (new BlockPos(-23, -13, -23)).rotate(roomRotation));
        }

        public JigsawPiece getRandomPiece(JigsawPattern pattern, Random random) {
            return this.jigsawFilter.getRandomPiece(pattern, random);
        }


        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Room room = (Room) o;
            return Objects.equals(this.roomPosition, room.roomPosition);
        }


        public int hashCode() {
            return Objects.hash(new Object[]{this.roomPosition});
        }
    }

    public static class Tunnel {
        private final VaultRoomLayoutGenerator.Room from;
        private final VaultRoomLayoutGenerator.Room to;
        private final JigsawPatternFilter jigsawFilter = new JigsawPatternFilter();

        public Tunnel(VaultRoomLayoutGenerator.Room from, VaultRoomLayoutGenerator.Room to) {
            this.from = from;
            this.to = to;
        }

        public Tunnel(VaultRoomLayoutGenerator.Room from, VaultRoomLayoutGenerator.Room to, Predicate<ResourceLocation> tunnelPieceFilter) {
            this(from, to);
            this.jigsawFilter.andMatches(tunnelPieceFilter);
        }

        public VaultRoomLayoutGenerator.Room getFrom() {
            return this.from;
        }

        public VaultRoomLayoutGenerator.Room getTo() {
            return this.to;
        }

        public Rotation getRandomConnectingRotation(Random random) {
            if (getFrom().getRoomPosition().getX() - getTo().getRoomPosition().getX() == 0) {
                return Rotation.CLOCKWISE_180;
            }
            return Rotation.CLOCKWISE_90;
        }


        public BlockPos getAbsoluteOffset(Rotation vaultRotation, Rotation tunnelRotation) {
            Vector3i from = getFrom().getRoomPosition();
            Vector3i to = getTo().getRoomPosition();
            Vector3i dir = new Vector3i(to.getX() - from.getX(), 0, to.getZ() - from.getZ());
            BlockPos relativeOffset = getFrom().getRoomOffset().offset(dir
                    .getX() * 47, 0, dir

                    .getZ() * 47);
            if (dir.getX() < 0) {
                relativeOffset = relativeOffset.offset(-1, 0, 0);
            }
            if (dir.getZ() < 0) {
                relativeOffset = relativeOffset.offset(0, 0, -1);
            }
            return relativeOffset.rotate(vaultRotation)

                    .offset((Vector3i) (new BlockPos(-5, 6, -24)).rotate(tunnelRotation));
        }

        public JigsawPiece getRandomPiece(JigsawPattern pattern, Random random) {
            return this.jigsawFilter.getRandomPiece(pattern, random);
        }


        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tunnel tunnel = (Tunnel) o;
            return ((Objects.equals(this.from, tunnel.from) && Objects.equals(this.to, tunnel.to)) || (
                    Objects.equals(this.from, tunnel.to) && Objects.equals(this.to, tunnel.from)));
        }


        public int hashCode() {
            return this.from.hashCode() ^ this.to.hashCode();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\layout\VaultRoomLayoutGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */