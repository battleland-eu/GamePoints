package su.nightexpress.gamepoints.api.event;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.manager.api.event.ICancellableEvent;
import su.nightexpress.gamepoints.data.PointUser;

public class PointUserChangeBalanceEvent extends ICancellableEvent {

    private final PointUser user;
    private final int balanceOld;
    private final int balanceNew;

    public PointUserChangeBalanceEvent(@NotNull PointUser user, int balanceOld, int balanceNew) {
        this.user = user;
        this.balanceOld = balanceOld;
        this.balanceNew = balanceNew;
    }

    @NotNull
    public PointUser getUser() {
        return user;
    }

    public int getBalanceOld() {
        return balanceOld;
    }

    public int getBalanceNew() {
        return balanceNew;
    }
}
