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
    public static GiveLootCommand GIVE_LOOT;
    public static VaultGodSayCommand VAULTGOD_SAY;
    public static CrystalCommand CRYSTAL;
    public static EternalCommand ETERNAL;
    public static GearCommand GEAR;
    public static PartyCommand PARTY;

    public static void registerCommands(final CommandDispatcher<CommandSource> dispatcher, final Commands.EnvironmentType env) {
        ModCommands.RELOAD_CONFIGS = registerCommand(ReloadConfigsCommand::new, dispatcher, env);
        ModCommands.VAULT_LEVEL = registerCommand(VaultLevelCommand::new, dispatcher, env);
        ModCommands.INTERNAL = registerCommand(InternalCommand::new, dispatcher, env);
        ModCommands.DEBUG = registerCommand(DebugCommand::new, dispatcher, env);
        ModCommands.INV_RESTORE = registerCommand(InvRestoreCommand::new, dispatcher, env);
        ModCommands.GIVE_LOOT = registerCommand(GiveLootCommand::new, dispatcher, env);
        ModCommands.VAULTGOD_SAY = registerCommand(VaultGodSayCommand::new, dispatcher, env);
        ModCommands.CRYSTAL = registerCommand(CrystalCommand::new, dispatcher, env);
        ModCommands.ETERNAL = registerCommand(EternalCommand::new, dispatcher, env);
        ModCommands.GEAR = registerCommand(GearCommand::new, dispatcher, env);
        ModCommands.PARTY = registerCommand(PartyCommand::new, dispatcher, env);
        ArchitectDirectionCommands.register(dispatcher);
    }

    public static void registerArgumentTypes() {
        ArgumentTypes.register(Vault.id("backup_list_player").toString(), (Class) BackupListArgument.Player.class, (IArgumentSerializer) new ArgumentSerializer((Supplier) BackupListArgument.Player::new));
        ArgumentTypes.register(Vault.id("backup_list_uuid").toString(), (Class) BackupListArgument.UUIDRef.class, (IArgumentSerializer) new ArgumentSerializer((Supplier) BackupListArgument.UUIDRef::new));
    }

    public static <T extends Command> T registerCommand(final Supplier<T> supplier, final CommandDispatcher<CommandSource> dispatcher, final Commands.EnvironmentType env) {
        final T command = supplier.get();
        if (!command.isDedicatedServerOnly() || env == Commands.EnvironmentType.DEDICATED || env == Commands.EnvironmentType.ALL) {
            command.registerCommand(dispatcher);
        }
        return command;
    }
}
