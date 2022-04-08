package iskallia.vault.init;

import com.mojang.brigadier.CommandDispatcher;
import iskallia.vault.Vault;
import iskallia.vault.backup.BackupListArgument;
import iskallia.vault.command.*;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.IArgumentSerializer;

import java.util.function.Supplier;

public class ModCommands {
    public static ReloadConfigsCommand RELOAD_CONFIGS;
    public static VaultLevelCommand VAULT_LEVEL;
    public static InternalCommand INTERNAL;
    public static DebugCommand DEBUG;
    public static InvRestoreCommand INV_RESTORE;

    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher, Commands.EnvironmentType env) {
        RELOAD_CONFIGS = registerCommand(ReloadConfigsCommand::new, dispatcher, env);
        VAULT_LEVEL = registerCommand(VaultLevelCommand::new, dispatcher, env);
        INTERNAL = registerCommand(InternalCommand::new, dispatcher, env);
        DEBUG = registerCommand(DebugCommand::new, dispatcher, env);
        INV_RESTORE = registerCommand(InvRestoreCommand::new, dispatcher, env);
        GIVE_LOOT = registerCommand(GiveLootCommand::new, dispatcher, env);
        VAULTGOD_SAY = registerCommand(VaultGodSayCommand::new, dispatcher, env);
        CRYSTAL = registerCommand(CrystalCommand::new, dispatcher, env);
        ETERNAL = registerCommand(EternalCommand::new, dispatcher, env);
        GEAR = registerCommand(GearCommand::new, dispatcher, env);

        PARTY = registerCommand(PartyCommand::new, dispatcher, env);
        ArchitectDirectionCommands.register(dispatcher);
    }

    public static GiveLootCommand GIVE_LOOT;
    public static VaultGodSayCommand VAULTGOD_SAY;
    public static CrystalCommand CRYSTAL;
    public static EternalCommand ETERNAL;
    public static GearCommand GEAR;
    public static PartyCommand PARTY;

    public static void registerArgumentTypes() {
        ArgumentTypes.register(Vault.id("backup_list_player").toString(), BackupListArgument.Player.class, (IArgumentSerializer) new ArgumentSerializer(iskallia.vault.backup.BackupListArgument.Player::new));
        ArgumentTypes.register(Vault.id("backup_list_uuid").toString(), BackupListArgument.UUIDRef.class, (IArgumentSerializer) new ArgumentSerializer(iskallia.vault.backup.BackupListArgument.UUIDRef::new));
    }

    public static <T extends Command> T registerCommand(Supplier<T> supplier, CommandDispatcher<CommandSource> dispatcher, Commands.EnvironmentType env) {
        Command command = (Command) supplier.get();
        if (!command.isDedicatedServerOnly() || env == Commands.EnvironmentType.DEDICATED || env == Commands.EnvironmentType.ALL) {
            command.registerCommand(dispatcher);
        }
        return (T) command;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */