package iskallia.vault.mixin;

import net.minecraft.server.dedicated.ServerHangWatchdog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.management.ThreadInfo;
import java.util.Arrays;
import java.util.StringJoiner;


@Mixin({ServerHangWatchdog.class})
public class MixinServerHangWatchdog {
    @Redirect(method = {"run"}, at = @At(value = "INVOKE", target = "Ljava/lang/StringBuilder;append(Ljava/lang/Object;)Ljava/lang/StringBuilder;"))
    public StringBuilder appendThreadInfo(StringBuilder sb, Object obj) {
        if (obj instanceof ThreadInfo) {
            StackTraceElement[] trace = ((ThreadInfo) obj).getStackTrace();
            StringJoiner joiner = new StringJoiner("\n");
            Arrays.<StackTraceElement>stream(trace).map(StackTraceElement::toString).forEach(joiner::add);
            sb.append(obj).append("\n");
            sb.append("Full Trace:\n");
            sb.append(joiner.toString());
            return sb;
        }
        return sb.append(obj);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinServerHangWatchdog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */