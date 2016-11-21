package com.github.andrei1993ak.finances.model.reportDbHelpers;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCost;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperIncome;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperTransfer;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperWallet;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.models.Transfer;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.ContextHolder;

import org.achartengine.model.TimeSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BalanceChartHelper {

    private final DBHelperIncome dbHelperIncome;
    private final DBHelperWallet dbHelperWallet;
    private final DBHelperCost dbHelperCost;
    private final DBHelperTransfer dbHelperTransfer;


    public BalanceChartHelper() {
        this.dbHelperCost = (DBHelperCost) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Cost.class);
        this.dbHelperIncome = (DBHelperIncome) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Income.class);
        this.dbHelperTransfer = (DBHelperTransfer) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Transfer.class);
        this.dbHelperWallet = (DBHelperWallet) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Wallet.class);
    }

    public TimeSeries getSeries(final long walletId) {

        final List<Income> incomesList = dbHelperIncome.getAllToListByWalletId(walletId);
        final List<Cost> costsList = dbHelperCost.getAllToListByWalletId(walletId);
        final List<Transfer> transferList = dbHelperTransfer.getAllToListByWalletId(walletId);

        final HashMap<Date, Double> operations = new HashMap<>();

        for (final Income income : incomesList) {
            final Date date = new Date(income.getDate());
            if (operations.containsKey(date)) {
                operations.put(date, operations.get(date) + income.getAmount());
            } else {
                operations.put(date, income.getAmount());
            }
        }
        for (final Cost cost : costsList) {
            final Date date = new Date(cost.getDate());
            if (operations.containsKey(date)) {
                operations.put(date, operations.get(date) - cost.getAmount());
            } else {
                operations.put(date, -cost.getAmount());
            }
        }
        for (final Transfer transfer : transferList) {
            final Date date = new Date(transfer.getDate());
            if (operations.containsKey(date)) {
                if (transfer.getFromWalletId() == walletId) {
                    operations.put(date, operations.get(date) - transfer.getFromAmount());
                } else {
                    operations.put(date, operations.get(date) + transfer.getToAmount());
                }
            } else {
                if (transfer.getFromWalletId() == walletId) {
                    operations.put(date, -transfer.getFromAmount());
                } else {
                    operations.put(date, transfer.getToAmount());
                }
            }
        }

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final TimeSeries series = new TimeSeries("Balance");
        Double amount = dbHelperWallet.get(walletId).getAmount();
        int count = 0;
        for (final Date date = calendar.getTime(); count < operations.size(); date.setTime(date.getTime() - 86400000)) {
            if (operations.containsKey(date)) {
                amount -= operations.get(date);
                count++;
            }
            series.add(date, amount);
        }
        return series;
    }
}
