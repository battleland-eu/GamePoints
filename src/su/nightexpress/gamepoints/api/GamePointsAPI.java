package su.nightexpress.gamepoints.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.data.users.IUserManager;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.api.store.IPointStore;
import su.nightexpress.gamepoints.data.objects.PointUser;
import su.nightexpress.gamepoints.store.StoreManager;

import java.util.Collection;

public class GamePointsAPI {

    private static final GamePoints PLUGIN = GamePoints.getPlugin(GamePoints.class);

    @Nullable
    public static PointUser getUserData(@NotNull Player player) {
        return PLUGIN.getUserManager().getOrLoadUser(player);
    }

    @Nullable
    public static PointUser getUserData(@NotNull String name, boolean uuid) {
        return PLUGIN.getUserManager().getOrLoadUser(name, uuid);
    }

    @Nullable
    public static IPointStore getStore(@NotNull String id) {
        return PLUGIN.getStoreManager().getStore(id);
    }

    @NotNull
    public static Collection<IPointStore> getStores() {
        return PLUGIN.getStoreManager().getStores();
    }

    @NotNull
    public static IUserManager<GamePoints, PointUser> getUserManager() {
        return PLUGIN.getUserManager();
    }

    @NotNull
    public static StoreManager getStoreManager() {
        return PLUGIN.getStoreManager();
    }
}
