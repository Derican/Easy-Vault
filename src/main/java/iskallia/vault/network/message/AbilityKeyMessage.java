// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.network.message;

import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AbilityKeyMessage
{
    public boolean keyUp;
    public boolean keyDown;
    public boolean scrollUp;
    public boolean scrollDown;
    public boolean shouldCancelDown;
    public String selectedAbility;
    
    public AbilityKeyMessage() {
        this.selectedAbility = "";
    }
    
    public AbilityKeyMessage(final boolean keyUp, final boolean keyDown, final boolean scrollUp, final boolean scrollDown) {
        this.selectedAbility = "";
        this.keyUp = keyUp;
        this.keyDown = keyDown;
        this.scrollUp = scrollUp;
        this.scrollDown = scrollDown;
    }
    
    public AbilityKeyMessage(final boolean shouldCancelDown) {
        this.selectedAbility = "";
        this.shouldCancelDown = shouldCancelDown;
    }
    
    public AbilityKeyMessage(final AbilityGroup<?, ?> selectAbility) {
        this.selectedAbility = "";
        this.selectedAbility = selectAbility.getParentName();
    }
    
    public static void encode(final AbilityKeyMessage message, final PacketBuffer buffer) {
        buffer.writeBoolean(message.keyUp);
        buffer.writeBoolean(message.keyDown);
        buffer.writeBoolean(message.scrollUp);
        buffer.writeBoolean(message.scrollDown);
        buffer.writeBoolean(message.shouldCancelDown);
        buffer.writeUtf(message.selectedAbility);
    }
    
    public static AbilityKeyMessage decode(final PacketBuffer buffer) {
        final AbilityKeyMessage message = new AbilityKeyMessage();
        message.keyUp = buffer.readBoolean();
        message.keyDown = buffer.readBoolean();
        message.scrollUp = buffer.readBoolean();
        message.scrollDown = buffer.readBoolean();
        message.shouldCancelDown = buffer.readBoolean();
        message.selectedAbility = buffer.readUtf(32767);
        return message;
    }
    
    public static void handle(final AbilityKeyMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = context.getSender();
            if (sender == null) {
                return;
            }
            else {
                final PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get((ServerWorld)sender.level);
                final AbilityTree abilityTree = abilitiesData.getAbilities((PlayerEntity)sender);
                if (message.scrollUp) {
                    abilityTree.scrollUp(sender.server);
                }
                else if (message.scrollDown) {
                    abilityTree.scrollDown(sender.server);
                }
                else if (message.keyUp) {
                    abilityTree.keyUp(sender.server);
                }
                else if (message.keyDown) {
                    abilityTree.keyDown(sender.server);
                }
                else if (message.shouldCancelDown) {
                    abilityTree.cancelKeyDown(sender.server);
                }
                else if (!message.selectedAbility.isEmpty()) {
                    abilityTree.quickSelectAbility(sender.server, message.selectedAbility);
                }
                return;
            }
        });
        context.setPacketHandled(true);
    }
}
