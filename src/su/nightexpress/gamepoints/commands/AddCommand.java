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

import java.util.Arrays;
import java.util.List;

public class AddCommand extends ISubCommand<GamePoints> {

    public AddCommand(@NotNull GamePoints plugin) {
        super(plugin, new String[]{"add"}, Perms.CMD_ADD);
    }

    @Override
    @NotNull
    public String description() {
        return plugin.lang().Command_Add_Desc.getMsg();
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    @NotNull
    public String usage() {
        return plugin.lang().Command_Add_Usage.getMsg();
    }

    @Override
    @NotNull
    public List<@NotNull String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
        if (i == 1) {
            return PlayerUT.getPlayerNames();
        }
        if (i == 2) {
            return Arrays.asList("<amount>", "1", "10", "50", "100");
        }
        return super.getTab(player, i, args);
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 3) {
            this.printUsage(sender);
            return;
        }

        String userName = args[1];
        int amount = this.getNumI(sender, args[2], 0);
        if (amount <= 0) {
            return;
        }

        PointUser user = plugin.getUserManager().getOrLoadUser(userName, false);
        if (user == null) {
            this.errPlayer(sender);
            return;
        }

        user.addPoints(amount);

        plugin.lang().Command_Add_Done_Sender
                .replace(Config.replacePlaceholders())
                .replace("%amount%", amount).replace(user.replacePlaceholders()).send(sender);

        Player player = user.getPlayer();
        if (player != null) {
            plugin.lang().Command_Add_Done_User.replace(Config.replacePlaceholders())
                    .replace("%amount%", amount).send(player);
        }
    }
}
