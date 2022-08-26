package iskallia.vault.network.message;

import iskallia.vault.entity.eternal.EternalDataAccess;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EternalInteractionMessage {
    private final Action action;
    private CompoundNBT extraData;

    private EternalInteractionMessage(final Action action) {
        this.extraData = new CompoundNBT();
        this.action = action;
    }

    public static EternalInteractionMessage feedItem(final ItemStack stack) {
        final EternalInteractionMessage pkt = new EternalInteractionMessage(Action.FEED_SELECTED);
        pkt.extraData.put("stack", stack.serializeNBT());
        return pkt;
    }

    public static EternalInteractionMessage levelUp(final String attribute) {
        final EternalInteractionMessage pkt = new EternalInteractionMessage(Action.LEVEL_UP);
        pkt.extraData.putString("attribute", attribute);
        return pkt;
    }

    public static EternalInteractionMessage selectEffect(final String effectName) {
        final EternalInteractionMessage pkt = new EternalInteractionMessage(Action.SELECT_EFFECT);
        pkt.extraData.putString("effectName", effectName);
        return pkt;
    }

    public static void encode(final EternalInteractionMessage pkt, final PacketBuffer buffer) {
        buffer.writeEnum(pkt.action);
        buffer.writeNbt(pkt.extraData);
    }

    public static EternalInteractionMessage decode(final PacketBuffer buffer) {
        final EternalInteractionMessage pkt = new EternalInteractionMessage((Action) buffer.readEnum((Class) Action.class));
        pkt.extraData = buffer.readNbt();
        return pkt;
    }

    public static void handle(final EternalInteractionMessage pkt, final Supplier<NetworkEvent.Context> contextSupplier) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokeinterface java/util/function/Supplier.get:()Ljava/lang/Object;
        //     6: checkcast       Lnet/minecraftforge/fml/network/NetworkEvent$Context;
        //     9: astore_2        /* context */
        //    10: aload_2         /* context */
        //    11: aload_1         /* contextSupplier */
        //    12: aload_0         /* pkt */
        //    13: invokedynamic   BootstrapMethod #0, run:(Ljava/util/function/Supplier;Liskallia/vault/network/message/EternalInteractionMessage;)Ljava/lang/Runnable;
        //    18: invokevirtual   net/minecraftforge/fml/network/NetworkEvent$Context.enqueueWork:(Ljava/lang/Runnable;)Ljava/util/concurrent/CompletableFuture;
        //    21: pop            
        //    22: aload_2         /* context */
        //    23: iconst_1       
        //    24: invokevirtual   net/minecraftforge/fml/network/NetworkEvent$Context.setPacketHandled:(Z)V
        //    27: return         
        //    Signature:
        //  (Liskallia/vault/network/message/EternalInteractionMessage;Ljava/util/function/Supplier<Lnet/minecraftforge/fml/network/NetworkEvent$Context;>;)V
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:252)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:185)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.nameVariables(AstMethodBodyBuilder.java:1482)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.populateVariables(AstMethodBodyBuilder.java:1411)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }

    public static boolean canBeFed(final EternalDataAccess eternal, final ItemStack stack) {
        return !stack.isEmpty() && ((!eternal.isAlive() && stack.getItem().equals(ModItems.LIFE_SCROLL)) || stack.getItem().equals(ModItems.AURA_SCROLL) || (eternal.getLevel() < eternal.getMaxLevel() && ModConfigs.ETERNAL.getFoodExp(stack.getItem()).isPresent()));
    }

    public enum Action {
        FEED_SELECTED,
        LEVEL_UP,
        SELECT_EFFECT
    }
}
