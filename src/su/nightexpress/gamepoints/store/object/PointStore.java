package su.nightexpress.gamepoints.store.object;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.AbstractLoadableItem;
import su.nexmedia.engine.config.api.JYML;
import su.nexmedia.engine.utils.ItemUT;
import su.nexmedia.engine.utils.StringUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.Perms;
import su.nightexpress.gamepoints.api.store.IPointProduct;
import su.nightexpress.gamepoints.api.store.IPointStore;
import su.nightexpress.gamepoints.store.StoreView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PointStore extends AbstractLoadableItem<GamePoints> implements IPointStore {

    private String  name;
    private boolean isPermissionRequired;

    private final Map<String, IPointProduct> productMap;

    private StoreView view;

    public PointStore(@NotNull GamePoints plugin, @NotNull JYML cfg) {
        super(plugin, cfg);

        this.setName(cfg.getString("Name", this.getId()));
        this.setPermissionRequired(cfg.getBoolean("Permission_Required"));

        JYML cfgProducts = new JYML(cfg.getFile().getParentFile().getAbsolutePath(), "products.yml");
        this.productMap = new HashMap<>();
        for (String sId : cfgProducts.getSection("")) {
            String path = sId + ".";

            String pName = cfgProducts.getString(path + "Name", sId);
            List<String> description = cfgProducts.getStringList(path + "Description");
            int pCost = cfgProducts.getInt(path + "Price");
            long pPurchaseCooldown = cfgProducts.getLong(path + "Purchase_Cooldown");
            int pPriority = cfgProducts.getInt(path + "Priority");
            Set<String> pInherRewards = cfgProducts.getStringSet(path + "Inheritance.Rewards");
            Set<String> pInherPrice = cfgProducts.getStringSet(path + "Inheritance.Price");
            ItemStack pPreview = cfgProducts.getItem(path + "Preview");
            if (ItemUT.isAir(pPreview)) {
                plugin.error("Invalid product icon for '" + sId + "' in '" + getId() + "' store!");
                continue;
            }
            List<String> pCommands = cfgProducts.getStringList(path + "Rewards.Commands");
            int pStoreSlot = cfgProducts.getInt(path + "Store.Slot");

            IPointProduct product = new PointProduct(this, sId, pName, description, pCost, pPurchaseCooldown,
                    pPriority, pInherRewards, pInherPrice, pPreview, pCommands, pStoreSlot);
            this.productMap.put(product.getId(), product);
        }

        this.getProducts().stream().filter(IPointProduct::isOneTimedPurchase).forEach(productParent -> {
            this.getProducts().stream().filter(IPointProduct::isOneTimedPurchase).forEach(productChild -> {
                if (productChild.getInheritedPrice().contains(productParent.getId()) || productChild.equals(productParent)) {
                    productParent.getInheritedPrice().add(productChild.getId());
                    //System.out.println("Added inherited price for parent: '" + productParent.getId() + "' children: '" + productChild.getId() + "'");
                }
            });
        });

        this.view = new StoreView(this, "View.");
    }

    @Override
    public void clear() {
        if (this.view != null) {
            this.view.clear();
            this.view = null;
        }
    }

    @Override
    public void onSave() {
        // TODO
    }

    @Override
    public boolean hasPermission(@NotNull Player player) {
        return !this.isPermissionRequired() || player.hasPermission(Perms.STORE + this.getId());
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = StringUT.color(name);
    }

    @Override
    public boolean isPermissionRequired() {
        return isPermissionRequired;
    }

    @Override
    public void setPermissionRequired(boolean permissionRequired) {
        isPermissionRequired = permissionRequired;
    }

    @Override
    @NotNull
    public Map<String, IPointProduct> getProductsMap() {
        return this.productMap;
    }

    @NotNull
    @Override
    public StoreView getView() {
        return view;
    }
}
