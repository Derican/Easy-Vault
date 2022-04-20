package iskallia.vault.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class SkinProfile {
    public static final ExecutorService SERVICE;
    private String latestNickname;
    public AtomicReference<GameProfile> gameProfile;
    public AtomicReference<NetworkPlayerInfo> playerInfo;

    public SkinProfile() {
        this.gameProfile = new AtomicReference<GameProfile>();
        this.playerInfo = new AtomicReference<NetworkPlayerInfo>();
    }

    public String getLatestNickname() {
        return this.latestNickname;
    }

    public void updateSkin(final String name) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: getfield        iskallia/vault/util/SkinProfile.latestNickname:Ljava/lang/String;
        //     5: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //     8: ifeq            12
        //    11: return         
        //    12: aload_0         /* this */
        //    13: aload_1         /* name */
        //    14: putfield        iskallia/vault/util/SkinProfile.latestNickname:Ljava/lang/String;
        //    17: getstatic       net/minecraftforge/fml/loading/FMLEnvironment.dist:Lnet/minecraftforge/api/distmarker/Dist;
        //    20: invokevirtual   net/minecraftforge/api/distmarker/Dist.isClient:()Z
        //    23: ifeq            42
        //    26: getstatic       iskallia/vault/util/SkinProfile.SERVICE:Ljava/util/concurrent/ExecutorService;
        //    29: aload_0         /* this */
        //    30: aload_1         /* name */
        //    31: invokedynamic   BootstrapMethod #0, run:(Liskallia/vault/util/SkinProfile;Ljava/lang/String;)Ljava/lang/Runnable;
        //    36: invokeinterface java/util/concurrent/ExecutorService.submit:(Ljava/lang/Runnable;)Ljava/util/concurrent/Future;
        //    41: pop            
        //    42: return         
        //    StackMapTable: 00 02 0C 1D
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:382)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:95)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:206)
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

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getLocationSkin() {
        if (this.playerInfo == null || this.playerInfo.get() == null) {
            return DefaultPlayerSkin.getDefaultSkin();
        }
        try {
            return this.playerInfo.get().getSkinLocation();
        } catch (final Exception e) {
            e.printStackTrace();
            return DefaultPlayerSkin.getDefaultSkin();
        }
    }

    static {
        SERVICE = Executors.newFixedThreadPool(4);
    }
}
