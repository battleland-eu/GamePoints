package su.nightexpress.gamepoints.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.commands.api.ISubCommand;
import su.nexmedia.engine.utils.PlayerUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.Perms;
import su.nightexpress.gamepoints.api.store.IPointStore;

import java.util.List;

public class StoreCommand extends ISubCommand<GamePoints> {

    public StoreCommand(@NotNull GamePoints plugin) {
        super(plugin, new String[]{"store"}, Perms.CMD_STORE);
    }

    @Override
    @NotNull
    public String description() {
        return plugin.lang().Command_Store_Desc.getMsg();
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    @NotNull
    public String usage() {
        return plugin.lang().Command_Store_Usage.getMsg();
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
        if (i == 1) {
            return plugin.getStoreManager().getStoreIds();
        }
        if (i == 2) {
            return PlayerUT.getPlayerNames();
        }
        return super.getTab(player, i, args);
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if ((args.length < 3 && !(sender instanceof Player)) || args.length > 3) {
            this.printUsage(sender);
            return;
        }

        if (args.length < 2) {
            Player player = (Player) sender;
            plugin.getStoreManager().getStoreMainMenu().open(player, 1);
            return;
        }

        String storeName = args[1];
        IPointStore store = plugin.getStoreManager().getStore(storeName);
        if (store == null) {
            plugin.lang().Store_Open_Error_Invalid.send(sender);
            return;
        }

        if (args.length >= 3 && !sender.hasPermission(Perms.ADMIN)) {
            this.errPerm(sender);
            return;
        }

        String userName = args.length == 3 ? args[2] : sender.getName();
        Player player = plugin.getServer().getPlayer(userName);
        if (player == null) {
            this.errPlayer(sender);
            return;
        }

        store.open(player);
    }
}
