package com.gmail.a93ak.andrei19.finance30.control.loaders;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.gmail.a93ak.andrei19.finance30.model.models.Income;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;
import com.gmail.a93ak.andrei19.finance30.model.reportDbHelpers.PieChartItemHelper;
import com.gmail.a93ak.andrei19.finance30.model.reportModels.PieChartItem;

import java.util.ArrayList;

public class PieReportLoader extends Loader<ArrayList<PieChartItem>> {

    private GetValuesTask task;
    private final long purseId;
    private final boolean type;
    private final long categoryId;

    public PieReportLoader(final Context context, final Bundle args) {

        super(context);
        purseId = args.getLong(Income.PURSE_ID);
        type = args.getBoolean(PieChartItem.TYPE);
        categoryId = args.getLong(Income.CATEGORY_ID);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        if (task != null) {
            task.cancel(true);
        }
        task = new GetValuesTask();
        task.execute(purseId, categoryId);
    }

    private void getResultFromTask(final ArrayList<PieChartItem> result) {
        deliverResult(result);
    }

    private class GetValuesTask extends AsyncTask<Long, Void, ArrayList<PieChartItem>> {

        @Override
        protected ArrayList<PieChartItem> doInBackground(final Long... params) {
            if (type) {
                return PieChartItemHelper.getInstance().gRepInfoIncome(params[0], params[1]);
            } else {
                return PieChartItemHelper.getInstance().gRepInfoCost(params[0], params[1]);
            }
        }

        @Override
        protected void onPostExecute(final ArrayList<PieChartItem> list) {
            super.onPostExecute(list);
            getResultFromTask(list);
        }

    }
}
