package su.nightexpress.gamepoints.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.data.users.IUserManager;
import su.nightexpress.gamepoints.GamePoints;
import su.nightexpress.gamepoints.data.objects.PointUser;

public class UserManager extends IUserManager<GamePoints, PointUser> {

    public UserManager(@NotNull GamePoints plugin) {
        super(plugin);
    }

    @Override
    @NotNull
    protected PointUser createData(@NotNull Player player) {
        return new PointUser(plugin, player);
    }
}
