package su.nightexpress.gamepoints.api.store;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.IPlaceholder;
import su.nexmedia.engine.utils.PlayerUT;
import su.nexmedia.engine.utils.TimeUT;
import su.nightexpress.gamepoints.GamePoints;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;

public interface IPointProduct extends IPlaceholder {

    String PLACEHOLDER_ID                = "%product_id%";
    String PLACEHOLDER_NAME              = "%product_name%";
    String PLACEHOLDER_DESCRIPTION       = "%product_description%";
    String PLACEHOLDER_PRICE             = "%product_price%";
    String PLACEHOLDER_PRICE_INHERITED   = "%product_price_inherited%";
    String PLACEHOLDER_COOLDOWN          = "%product_cooldown%";
    String PLACEHOLDER_ONE_TIME_PURCHASE = "%product_one_time_purchase%";
    String PLACEHOLDER_PURCHASE_COOLDOWN = "%product_purchase_cooldown%";

    @Override
    @NotNull
    default UnaryOperator<String> replacePlaceholders() {
        GamePoints plugin = this.getStore().plugin();
        return str -> str
                .replace(PLACEHOLDER_DESCRIPTION, String.join("\n", this.getDescription()))
                .replace(PLACEHOLDER_ID, this.getId())
                .replace(PLACEHOLDER_NAME, this.getName())
                .replace(PLACEHOLDER_ONE_TIME_PURCHASE, plugin.lang().getBool(this.isOneTimedPurchase()))
                .replace(PLACEHOLDER_PURCHASE_COOLDOWN, TimeUT.formatTime(this.getPurchaseCooldown()))
                .replace(PLACEHOLDER_PRICE, String.valueOf(this.getPrice()))
                ;
    }

    default void giveRewards(@NotNull Player player) {
        IPointStore store = this.getStore();

        List<String> commands = new ArrayList<>(this.getRewardCommands());
        this.getInheritedRewards().stream().map(store::getProduct).filter(Objects::nonNull).forEach(inherited -> {
            commands.addAll(inherited.getRewardCommands());
        });
        commands.forEach(command -> PlayerUT.execCmd(player, command));
    }

    @NotNull IPointStore getStore();

    @NotNull String getId();

    @NotNull String getName();

    void setName(@NotNull String name);

    @NotNull List<String> getDescription();

    void setDescription(@NotNull List<String> description);

    int getPrice();

    void setPrice(int price);

    long getPurchaseCooldown();

    void setPurchaseCooldown(long purchaseCooldown);

    default boolean isOneTimedPurchase() {
        return this.getPurchaseCooldown() < 0;
    }

    int getPriority();

    void setPriority(int priority);

    @NotNull Set<String> getInheritedRewards();

    void setInheritedRewards(@NotNull Set<String> inheritedRewards);

    @NotNull Set<String> getInheritedPrice();

    void setInheritedPrice(@NotNull Set<String> inheritedPrice);

    @NotNull ItemStack getPreview();

    void setPreview(@NotNull ItemStack preview);

    @NotNull List<String> getRewardCommands();

    void setRewardCommands(@NotNull List<String> rewardCommands);

    int getStoreSlot();

    void setStoreSlot(int storeSlot);
}
