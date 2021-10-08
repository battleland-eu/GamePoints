package su.nightexpress.gamepoints.store;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.config.api.JYML;
import su.nexmedia.engine.manager.api.task.ITask;
import su.nexmedia.engine.utils.FileUT;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.api.store.IPointStore;
import su.nightexpress.gamepoints.config.Config;
import su.nightexpress.gamepoints.legacy.LegacyStore;
import su.nightexpress.gamepoints.store.listener.StoreListener;
import su.nightexpress.gamepoints.store.object.PointStore;

import java.io.File;
import java.util.*;

public class StoreManager extends AbstractManager<GamePoints> {

    private Map<String, IPointStore>         stores;
    private List<Map.Entry<String, Integer>> balanceTop;

    private StoreMainMenu     storeMainMenu;
    private StoreConfirmation storeConfirmation;

    private TopTask topTask;

    public static final String DIR_STORES = "/stores/";

    public StoreManager(@NotNull GamePoints plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.plugin.getConfigManager().extract(DIR_STORES);
        this.stores = new HashMap<>();
        this.balanceTop = new ArrayList<>();

        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + DIR_STORES, false)) {
            LegacyStore.update(plugin, cfg);
        }
        for (File dir : FileUT.getFolders(plugin.getDataFolder() + DIR_STORES)) {
            JYML cfg = new JYML(dir.getAbsolutePath(), dir.getName() + ".yml");
            IPointStore store = new PointStore(plugin, cfg);
            this.stores.put(store.getId(), store);
        }
        this.plugin.info("Stores Loaded: " + stores.size());

        this.storeMainMenu = new StoreMainMenu(this.plugin);
        this.storeConfirmation = new StoreConfirmation(this.plugin);
        this.addListener(new StoreListener(this));

        if (Config.GENERAL_TOP_UPDATE_MIN > 0) {
            this.topTask = new TopTask();
            this.topTask.start();
        }
    }

    @Override
    protected void onShutdown() {
        if (this.topTask != null) {
            this.topTask.stop();
            this.topTask = null;
        }
        if (this.stores != null) {
            this.stores.values().forEach(IPointStore::clear);
            this.stores.clear();
            this.stores = null;
        }
        if (this.storeMainMenu != null) {
            this.storeMainMenu.clear();
            this.storeMainMenu = null;
        }
    }

    @NotNull
    public StoreMainMenu getStoreMainMenu() {
        return storeMainMenu;
    }

    @NotNull
    public StoreConfirmation getStoreConfirmation() {
        return storeConfirmation;
    }

    @NotNull
    public List<String> getStoreIds() {
        return new ArrayList<>(stores.keySet());
    }

    @Nullable
    public IPointStore getStore(@NotNull String id) {
        return this.stores.get(id.toLowerCase());
    }

    @NotNull
    public Collection<IPointStore> getStores() {
        return this.stores.values();
    }

    @NotNull
    public List<Map.Entry<String, Integer>> getBalanceTop() {
        return this.balanceTop;
    }

    class TopTask extends ITask<GamePoints> {

        TopTask() {
            super(StoreManager.this.plugin, Config.GENERAL_TOP_UPDATE_MIN * 60, true);
        }

        @Override
        public void action() {
            plugin.info("Updating balance top...");
            long took = System.currentTimeMillis();

            balanceTop.clear();

            Map<String, Integer> map = plugin.getData().getUserBalance();
            map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach((entry) -> {
                balanceTop.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            });

            took = System.currentTimeMillis() - took;
            plugin.info("Balance top updated in " + took + " ms!");
        }
    }
}
