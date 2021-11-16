package su.nightexpress.gamepoints;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.NexPlugin;
import su.nexmedia.engine.api.data.UserDataHolder;
import su.nexmedia.engine.commands.api.IGeneralCommand;
import su.nexmedia.engine.hooks.Hooks;
import su.nightexpress.gamepoints.commands.*;
import su.nightexpress.gamepoints.config.Config;
import su.nightexpress.gamepoints.config.Lang;
import su.nightexpress.gamepoints.data.GamePointsData;
import su.nightexpress.gamepoints.data.UserManager;
import su.nightexpress.gamepoints.data.PointUser;
import su.nightexpress.gamepoints.hooks.PlaceholderAPIHook;
import su.nightexpress.gamepoints.store.StoreManager;

import java.sql.SQLException;

public class GamePoints extends NexPlugin<GamePoints> implements UserDataHolder<GamePoints, PointUser> {

    private static GamePoints instance;

    private Config config;
    private Lang   lang;

    private StoreManager   storeManager;

    private GamePointsData dataHandler;
    private UserManager userManager;

    public static GamePoints getInstance() {
        return instance;
    }

    public GamePoints() {
        instance = this;
    }

    @Override
    public void enable() {
        this.storeManager = new StoreManager(this);
        this.storeManager.setup();
    }

    @Override
    public void disable() {
        if (this.storeManager != null) {
            this.storeManager.shutdown();
            this.storeManager = null;
        }
    }

    @Override
    public boolean setupDataHandlers() {
        try {
            this.dataHandler = GamePointsData.getInstance(this);
            this.dataHandler.setup();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        this.userManager = new UserManager(this);
        this.userManager.setup();

        return true;
    }

    @Override
    public void setConfig() {
        this.config = new Config(this);
        this.config.setup();

        this.lang = new Lang(this);
        this.lang.setup();
    }

    @Override
    @NotNull
    public Config cfg() {
        return this.config;
    }

    @Override
    @NotNull
    public Lang lang() {
        return this.lang;
    }

    @Override
    public void registerCmds(@NotNull IGeneralCommand<GamePoints> mainCommand) {
        mainCommand.addSubCommand(new AddCommand(this));
        mainCommand.addSubCommand(new BalanceCommand(this));
        mainCommand.addSubCommand(new BalanceTopCommand(this));
        mainCommand.addSubCommand(new PayCommand(this));
        mainCommand.addSubCommand(new SetCommand(this));
        mainCommand.addSubCommand(new StoreCommand(this));
        mainCommand.addSubCommand(new TakeCommand(this));
        mainCommand.addSubCommand(new RemovePurchaseCommand(this));
    }

    @Override
    public void registerHooks() {
        if (Hooks.hasPlaceholderAPI()) {
            this.registerHook(Hooks.PLACEHOLDER_API, PlaceholderAPIHook.class);
        }
    }

    @NotNull
    public StoreManager getStoreManager() {
        return this.storeManager;
    }

    @Override
    @NotNull
    public GamePointsData getData() {
        return this.dataHandler;
    }

    @NotNull
    @Override
    public UserManager getUserManager() {
        return userManager;
    }
}
