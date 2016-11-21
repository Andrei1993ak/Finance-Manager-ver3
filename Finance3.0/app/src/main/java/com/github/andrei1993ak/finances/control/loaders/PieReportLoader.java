package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.reportDbHelpers.PieChartItemHelper;
import com.github.andrei1993ak.finances.model.reportModels.PieChartItem;
import com.github.andrei1993ak.finances.util.Constants;

import java.util.ArrayList;

public class PieReportLoader extends Loader<ArrayList<PieChartItem>> {

    private GetValuesTask task;
    private final long walletId;
    private final boolean type;
    private final long categoryId;

    public PieReportLoader(final Context context, final Bundle args) {

        super(context);
        walletId = args.getLong(Income.WALLET_ID);
        type = args.getBoolean(Constants.PIE_CHART_TYPE);
        categoryId = args.getLong(Income.CATEGORY_ID);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        if (task != null) {
            task.cancel(true);
        }
        task = new GetValuesTask();
        task.execute(walletId, categoryId);
    }

    private void getResultFromTask(final ArrayList<PieChartItem> result) {
        deliverResult(result);
    }

    private class GetValuesTask extends AsyncTask<Long, Void, ArrayList<PieChartItem>> {

        @Override
        protected ArrayList<PieChartItem> doInBackground(final Long... params) {
            if (type) {
                return new PieChartItemHelper().gRepInfoIncome(params[0], params[1]);
            } else {
                return new PieChartItemHelper().gRepInfoCost(params[0], params[1]);
            }
        }

        @Override
        protected void onPostExecute(final ArrayList<PieChartItem> list) {
            super.onPostExecute(list);
            getResultFromTask(list);
        }

    }
}
