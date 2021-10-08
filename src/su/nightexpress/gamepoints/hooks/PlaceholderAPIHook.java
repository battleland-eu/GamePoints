package su.nightexpress.gamepoints.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.hooks.HookState;
import su.nexmedia.engine.hooks.NHook;
import su.nexmedia.engine.utils.StringUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.api.store.IPointProduct;
import su.nightexpress.gamepoints.api.store.IPointStore;
import su.nightexpress.gamepoints.data.objects.PointUser;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlaceholderAPIHook extends NHook<GamePoints> {

    private PointsExpansion pointsExpansion;

    public PlaceholderAPIHook(@NotNull GamePoints plugin) {
        super(plugin);
    }

    @Override
    @NotNull
    protected HookState setup() {
        (this.pointsExpansion = new PointsExpansion()).register();
        return HookState.SUCCESS;
    }

    @Override
    protected void shutdown() {
        if (this.pointsExpansion != null) {
            this.pointsExpansion.unregister();
            this.pointsExpansion = null;
        }
    }

    class PointsExpansion extends PlaceholderExpansion {

        @Override
        @NotNull
        public String getAuthor() {
            return plugin.getAuthor();
        }

        @Override
        @NotNull
        public String getIdentifier() {
            return plugin.getNameRaw();
        }

        @Override
        @NotNull
        public String getVersion() {
            return plugin.getDescription().getVersion();
        }

        @Override
        public String onPlaceholderRequest(Player player, String holder) {
            // %gp_item_rawprice_ranks_rank-vip%
            if (holder.startsWith("item_rawprice")) {
                String[] ss = this.getProductStoreIds("item_rawprice_", holder);
                String storeId = ss[0];
                String itemId = ss[1];

                IPointStore store = plugin.getStoreManager().getStore(storeId);
                if (store == null) return null;

                IPointProduct item = store.getProduct(itemId);
                if (item == null) return null;

                return String.valueOf(item.getPrice());
            }

            PointUser user = plugin.getUserManager().getOrLoadUser(player);
            if (user == null) return "N/A";

            if (holder.startsWith("item_price")) {
                String[] ss = this.getProductStoreIds("item_price_", holder);
                String storeId = ss[0];
                String itemId = ss[1];

                IPointStore store = plugin.getStoreManager().getStore(storeId);
                if (store == null) return null;

                IPointProduct item = store.getProduct(itemId);
                if (item == null) return null;

                return String.valueOf(user.getInheritedPriceForItem(item));
            }

            if (holder.startsWith("top")) {
                // top_balance_1, top_player_1
                String[] split = holder.split("top_");
                if (split.length < 2) return "N/A";

                String[] splitTypePos = split[1].split("_");
                if (splitTypePos.length < 2) return "N/A";

                String type = splitTypePos[0];

                int pos = StringUT.getInteger(splitTypePos[1], 0);
                if (pos == 0) return "-";

                List<Map.Entry<String, Integer>> baltop = plugin.getStoreManager().getBalanceTop();
                if (pos > baltop.size()) return "-";

                Map.Entry<String, Integer> top = baltop.get(pos - 1);
                return type.equalsIgnoreCase("balance") ? String.valueOf(top.getValue()) : top.getKey();
            }

            if (holder.equalsIgnoreCase("balance")) {
                return String.valueOf(user.getBalance());
            }
            if (holder.equalsIgnoreCase("balance_formatted")) {
                NumberFormat format = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
                return format.format(user.getBalance());
            }

            return null;
        }

        @NotNull
        private String[] getProductStoreIds(@NotNull String prefix, @NotNull String holder) {
            // ranks_rank-vip
            String left = holder.replace(prefix, "");
            int index = left.indexOf('_');
            String storeId = left.substring(0, index);
            String itemId = left.substring(index + 1);

            //System.out.println("store: " + storeId);
            //System.out.println("item: " + itemId);

            return new String[]{storeId, itemId};
        }
    }
}
