package su.nightexpress.gamepoints.store;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.api.menu.MenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.config.api.JYML;
import su.nexmedia.engine.utils.ItemUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.api.store.IPointStore;

public class StoreMainMenu extends AbstractMenu<GamePoints> {

    public StoreMainMenu(@NotNull GamePoints plugin) {
        super(plugin, JYML.loadOrExtract(plugin, "store.main.yml"), "");

        for (String sId : cfg.getSection("Stores")) {
            IPointStore store = plugin.getStoreManager().getStore(sId);
            if (store == null) {
                plugin.error("Invalid store '" + sId + "' in main store!");
                continue;
            }

            MenuItem menuItem = cfg.getMenuItem("Stores." + sId);
            menuItem.setClick((p, type, e) -> store.open(p));

            this.addItem(menuItem);
        }

        for (String id : cfg.getSection("Content")) {
            MenuItem guiItem = cfg.getMenuItem("Content." + id, MenuItemType.class);

            this.addItem(guiItem);
        }
    }

    @Override
    public void onPrepare(@NotNull Player player, @NotNull Inventory inventory) {

    }

    @Override
    public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

    }

    @Override
    public void onItemPrepare(@NotNull Player player, @NotNull IMenuItem menuItem, @NotNull ItemStack item) {
        super.onItemPrepare(player, menuItem, item);

        IPointStore store = plugin.getStoreManager().getStore(menuItem.getId());
        if (store == null) return;

        ItemUT.replace(item, store.replacePlaceholders());
    }

    @Override
    public boolean cancelClick(@NotNull SlotType slotType, int slot) {
        return true;
    }
}
