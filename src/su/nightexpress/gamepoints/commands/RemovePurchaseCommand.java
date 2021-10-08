package su.nightexpress.gamepoints.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.commands.api.ISubCommand;
import su.nexmedia.engine.utils.PlayerUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.Perms;
import su.nightexpress.gamepoints.api.store.IPointProduct;
import su.nightexpress.gamepoints.api.store.IPointStore;
import su.nightexpress.gamepoints.data.objects.PointUser;

import java.util.List;

public class RemovePurchaseCommand extends ISubCommand<GamePoints> {

    public RemovePurchaseCommand(@NotNull GamePoints plugin) {
        super(plugin, new String[]{"removepurchase"}, Perms.CMD_REMOVEPURCHASE);
    }

    @Override
    @NotNull
    public String usage() {
        return plugin.lang().Command_RemovePurchase_Usage.getMsg();
    }

    @Override
    @NotNull
    public String description() {
        return plugin.lang().Command_RemovePurchase_Desc.getMsg();
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
        if (i == 1) {
            return PlayerUT.getPlayerNames();
        }
        if (i == 2) {
            return plugin.getStoreManager().getStoreIds();
        }
        if (i == 3) {
            IPointStore store = plugin.getStoreManager().getStore(args[2]);
            if (store != null) return store.getProducts().stream().map(IPointProduct::getId).toList();
        }
        return super.getTab(player, i, args);
    }

    @Override
    protected void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 4) {
            this.printUsage(sender);
            return;
        }

        PointUser user = plugin.getUserManager().getOrLoadUser(args[1], false);
        if (user == null) {
            this.errPlayer(sender);
            return;
        }

        String storeId = args[2];
        String productId = args[3];
        user.getPurchases(storeId).remove(productId);
        plugin.lang().Command_RemovePurchase_Done_User.send(sender);
    }
}
