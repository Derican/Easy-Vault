package iskallia.vault.mixin;

import iskallia.vault.block.render.ScavengerChestRenderer;
import iskallia.vault.block.render.VaultChestRenderer;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;


@Mixin({Atlases.class})
public class MixinAtlases {
    @Inject(method = {"collectAllMaterials"}, at = {@At("RETURN")})
    private static void collectAllMaterials(Consumer<RenderMaterial> materialConsumer, CallbackInfo ci) {
        materialConsumer.accept(VaultChestRenderer.NORMAL);
        materialConsumer.accept(VaultChestRenderer.TREASURE);
        materialConsumer.accept(VaultChestRenderer.ALTAR);
        materialConsumer.accept(VaultChestRenderer.COOP);
        materialConsumer.accept(VaultChestRenderer.BONUS);
        materialConsumer.accept(ScavengerChestRenderer.MATERIAL);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinAtlases.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */