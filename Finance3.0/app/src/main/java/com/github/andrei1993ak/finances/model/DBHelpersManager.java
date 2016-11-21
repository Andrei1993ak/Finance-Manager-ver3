package com.github.andrei1993ak.finances.model;

import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryCost;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryIncome;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCost;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrency;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrencyOfficial;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperIncome;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperTransfer;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.dbHelpers.IDBHelperForModel;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.CostCategory;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.CurrencyOfficial;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.models.IncomeCategory;
import com.github.andrei1993ak.finances.model.models.TableClass;
import com.github.andrei1993ak.finances.model.models.Transfer;
import com.github.andrei1993ak.finances.model.models.Wallet;

public class DBHelpersManager {

    private DBHelperTransfer dbHelperTransfer = null;
    private DBHelperCost dbHelperCost = null;
    private DBHelperIncome dbHelperIncome = null;
    private DBHelperWallet dbHelperWallet = null;
    private DBHelperCategoryCost dbHelperCategoryCost = null;
    private DBHelperCategoryIncome dbHelperCategoryIncome = null;
    private DBHelperCurrency dbHelperCurrency = null;
    private DBHelperCurrencyOfficial dbHelperCurrencyOfficial = null;

    public IDBHelperForModel getDBHelper(final Class<? extends TableClass> clazz){
        if (Transfer.class.isAssignableFrom(clazz)) {
            if (dbHelperTransfer == null) {
                dbHelperTransfer = new DBHelperTransfer();
            }
            return dbHelperTransfer;
        } else if (clazz.isAssignableFrom(Cost.class)) {
            if (dbHelperCost == null) {
                dbHelperCost = new DBHelperCost();
            }
            return dbHelperCost;
        } else if (clazz.isAssignableFrom(Income.class)) {
            if (dbHelperIncome == null) {
                dbHelperIncome = new DBHelperIncome();
            }
            return dbHelperIncome;
        } else if (clazz.isAssignableFrom(Wallet.class)){
            if (dbHelperWallet == null){
                dbHelperWallet = new DBHelperWallet();
            }
            return dbHelperWallet;
        } else if (clazz.isAssignableFrom(CostCategory.class)){
            if (dbHelperCategoryCost == null){
                dbHelperCategoryCost = new DBHelperCategoryCost();
            }
            return dbHelperCategoryCost;
        } else if (clazz.isAssignableFrom(IncomeCategory.class)){
            if (dbHelperCategoryIncome == null){
                dbHelperCategoryIncome = new DBHelperCategoryIncome();
            }
            return dbHelperCategoryIncome;
        } else if (clazz.isAssignableFrom(Currency.class)){
            if (dbHelperCurrency == null){
                dbHelperCurrency = new DBHelperCurrency();
            }
            return dbHelperCurrency;
        } else if (clazz.isAssignableFrom(CurrencyOfficial.class)){
            if (dbHelperCurrencyOfficial == null){
                dbHelperCurrencyOfficial = new DBHelperCurrencyOfficial();
            }
            return dbHelperCurrencyOfficial;
        }
        return null;
    }
}
