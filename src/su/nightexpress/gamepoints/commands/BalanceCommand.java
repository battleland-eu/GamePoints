package su.nightexpress.gamepoints.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.commands.api.ISubCommand;
import su.nexmedia.engine.utils.PlayerUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.Perms;
import su.nightexpress.gamepoints.config.Config;
import su.nightexpress.gamepoints.data.PointUser;

import java.util.List;

public class BalanceCommand extends ISubCommand<GamePoints> {

    public BalanceCommand(@NotNull GamePoints plugin) {
        super(plugin, new String[]{"balance", "bal"}, Perms.CMD_BALANCE);
    }

    @Override
    @NotNull
    public String description() {
        return plugin.lang().Command_Balance_Desc.getMsg();
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    @NotNull
    public String usage() {
        return plugin.lang().Command_Balance_Usage.getMsg();
    }

    @Override
    @NotNull
    public List<@NotNull String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
        if (i == 1 && player.hasPermission(Perms.CMD_BALANCE_OTHERS)) {
            return PlayerUT.getPlayerNames();
        }
        return super.getTab(player, i, args);
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if ((args.length < 2 && !(sender instanceof Player)) || args.length > 2) {
            this.printUsage(sender);
            return;
        }

        if (args.length >= 2 && !sender.hasPermission(Perms.CMD_BALANCE_OTHERS)) {
            this.errPerm(sender);
            return;
        }

        String userName = args.length == 2 ? args[1] : sender.getName();
        PointUser user = plugin.getUserManager().getOrLoadUser(userName, false);
        if (user == null) {
            this.errPlayer(sender);
            return;
        }

        plugin.lang().Command_Balance_Done.replace(Config.replacePlaceholders()).replace(user.replacePlaceholders()).send(sender);
    }
}
