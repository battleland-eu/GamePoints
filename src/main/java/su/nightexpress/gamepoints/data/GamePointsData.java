package su.nightexpress.gamepoints.data;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.data.AbstractUserDataHandler;
import su.nexmedia.engine.data.DataTypes;
import su.nightexpress.gamepoints.GamePoints;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class GamePointsData extends AbstractUserDataHandler<GamePoints, PointUser> {

    private static GamePointsData instance;

    private final Function<ResultSet, PointUser> FUNC_USER;

    GamePointsData(@NotNull GamePoints plugin) throws SQLException {
        super(plugin);

        this.FUNC_USER = (rs) -> {
            try {
                UUID uuid = UUID.fromString(rs.getString(COL_USER_UUID));
                String name = rs.getString(COL_USER_NAME);
                long lastOnline = rs.getLong(COL_USER_LAST_ONLINE);

                int balance = rs.getInt("balance");
                Map<String, Map<String, Long>> items = gson.fromJson(rs.getString("purchases"), new TypeToken<Map<String, Map<String, Long>>>() {
                }.getType());

                return new PointUser(plugin, uuid, name, lastOnline, balance, items);
            } catch (SQLException e) {
                return null;
            }
        };
    }

    @NotNull
    public static synchronized GamePointsData getInstance(@NotNull GamePoints plugin) throws SQLException {
        if (instance == null) {
            instance = new GamePointsData(plugin);
        }
        return instance;
    }

    @Override
    protected void onTableCreate() {
        super.onTableCreate();
        this.addColumn(this.tableUsers, "purchases", DataTypes.STRING.build(this.dataType));
    }

    @NotNull
    public Map<String, Integer> getUserBalance() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT `name`, `balance` FROM " + this.tableUsers;

        try (Statement statement = this.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int balance = resultSet.getInt("balance");

                map.put(name, balance);
            }
            return map;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return map;
        }
    }

    @Override
    @NotNull
    protected LinkedHashMap<String, String> getColumnsToCreate() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("purchases", DataTypes.STRING.build(this.dataType));
        map.put("balance", DataTypes.INTEGER.build(this.dataType));
        return map;
    }

    @Override
    @NotNull
    protected LinkedHashMap<String, String> getColumnsToSave(@NotNull PointUser user) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("purchases", this.gson.toJson(user.getPurchases()));
        map.put("balance", String.valueOf(user.getBalance()));
        if (this.hasColumn(this.tableUsers, "items")) {
            map.put("items", "{}");
        }
        return map;
    }

    @Override
    @NotNull
    protected Function<ResultSet, PointUser> getFunctionToUser() {
        return this.FUNC_USER;
    }
}
