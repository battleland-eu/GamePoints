package su.nightexpress.gamepoints.config;

import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.config.api.ILangMsg;
import su.nexmedia.engine.core.config.CoreLang;
import su.nightexpress.gamepoints.GamePoints;

public class Lang extends CoreLang {

    public Lang(@NotNull GamePoints plugin) {
        super(plugin);
    }

    @Override
    protected void setupEnums() {

    }

    public ILangMsg Command_Add_Usage       = new ILangMsg(this, "<player> <amount>");
    public ILangMsg Command_Add_Desc        = new ILangMsg(this, "Add points to player account.");
    public ILangMsg Command_Add_Done_Sender = new ILangMsg(this, "&7Added &a%amount% %points_name% &7to &a%user_name%&7. New balance: &a%user_balance% %points_name%&7.");
    public ILangMsg Command_Add_Done_User   = new ILangMsg(this, "&7You received &a%amount% %points_name%&7!");

    public ILangMsg Command_Take_Usage       = new ILangMsg(this, "<player> <amount>");
    public ILangMsg Command_Take_Desc        = new ILangMsg(this, "Take points from player account.");
    public ILangMsg Command_Take_Done_Sender = new ILangMsg(this, "&7Taken &a%amount% %points_name% &7from &a%user_name%&7. New balance: &a%user_balance% %points_name%&7.");
    public ILangMsg Command_Take_Done_User   = new ILangMsg(this, "&7You lost &a%amount% %points_name%&7!");

    public ILangMsg Command_Set_Usage       = new ILangMsg(this, "<player> <amount>");
    public ILangMsg Command_Set_Desc        = new ILangMsg(this, "Set balance of player account.");
    public ILangMsg Command_Set_Done_Sender = new ILangMsg(this, "&7Set &a%amount% %points_name% &7for &a%user_name%&7. New balance: &a%user_balance% %points_name%&7.");
    public ILangMsg Command_Set_Done_User   = new ILangMsg(this, "&7Your balance has been set to &a%amount% %points_name%&7!");

    public ILangMsg Command_Pay_Usage         = new ILangMsg(this, "<player> <amount>");
    public ILangMsg Command_Pay_Desc          = new ILangMsg(this, "Transfer points to player.");
    public ILangMsg Command_Pay_Error_NoMoney = new ILangMsg(this, "You don't have enought points!");
    public ILangMsg Command_Pay_Done_Sender   = new ILangMsg(this, "&7You sent &a%amount% %points_name% &7to &a%player%&7!");
    public ILangMsg Command_Pay_Done_User     = new ILangMsg(this, "&7You received &a%amount% %points_name%&7 from &a%player%&7!");

    public ILangMsg Command_RemovePurchase_Usage     = new ILangMsg(this, "<player> <store> <product>");
    public ILangMsg Command_RemovePurchase_Desc      = new ILangMsg(this, "Removes specified pruchase from user data.");
    public ILangMsg Command_RemovePurchase_Done_User = new ILangMsg(this, "&7Purchase removed!");

    public ILangMsg Command_Balance_Usage = new ILangMsg(this, "[player]");
    public ILangMsg Command_Balance_Desc  = new ILangMsg(this, "Displays player balance.");
    public ILangMsg Command_Balance_Done  = new ILangMsg(this, "&a%user_name%&7's balance: &a%user_balance% %points_name%");

    public ILangMsg Command_BalanceTop_Usage = new ILangMsg(this, "[page]");
    public ILangMsg Command_BalanceTop_Desc  = new ILangMsg(this, "Displays top balances.");
    public ILangMsg Command_BalanceTop_List  = new ILangMsg(
            this, """
            &6&m             &6&l[ &e&lGame Points &7- &e&lTop &f%page_min%&7/&f%page_max% &6&l]&6&m             &7
            &6%pos%. &e%user_name%: &a%user_balance% %points_name%
            &6&m             &6&l[ &e&lEnd Game Points Top &6&l]&6&m              &7""");

    public ILangMsg Command_Store_Usage = new ILangMsg(this, "[store] [player]");
    public ILangMsg Command_Store_Desc  = new ILangMsg(this, "Opens specified store.");

    public ILangMsg Store_Open_Error_Invalid    = new ILangMsg(this, "&cInvalid store!");
    public ILangMsg Store_Open_Error_Permission = new ILangMsg(this, "&cWhoops! &7You don't access to %store_name%&7 store!");

    public ILangMsg Store_Buy_Error_NoMoney        = new ILangMsg(this, "&cWhoops! &7You don't have enough %points_name%&7!");
    public ILangMsg Store_Buy_Error_Inherited      = new ILangMsg(this, "&cWhoops! &7You already have purchased a higher version of this product!");
    public ILangMsg Store_Buy_Error_SinglePurchase = new ILangMsg(this, "&cWhoops! &7This product can be purchased only once! You already have purchased this product.");
    public ILangMsg Store_Buy_Error_Cooldown       = new ILangMsg(this, "&cWhoops! &7You can purchase this product again in: &c%product_cooldown%");

    public ILangMsg Store_Buy_Success = new ILangMsg(this, "&7You successfully bought &a%product_name%&7 for &a%product_price_inherited% %points_name%&7!");
}
