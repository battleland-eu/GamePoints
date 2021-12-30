package su.nightexpress.gamepoints.api.event;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.manager.api.event.ICancellableEvent;
import su.nightexpress.gamepoints.api.store.IPointProduct;
import su.nightexpress.gamepoints.api.store.IPointStore;
import su.nightexpress.gamepoints.data.PointUser;

public class PointProductPurchaseEvent extends ICancellableEvent {

    private final Player        player;
    private final PointUser     user;
    private final IPointProduct product;
    private       int           price;

    public PointProductPurchaseEvent(@NotNull Player player, @NotNull PointUser user, @NotNull IPointProduct product, int price) {
        this.player = player;
        this.user = user;
        this.product = product;
        this.setPrice(price);
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public PointUser getUser() {
        return user;
    }

    @NotNull
    public IPointProduct getProduct() {
        return product;
    }

    @NotNull
    public final IPointStore getStore() {
        return this.getProduct().getStore();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
