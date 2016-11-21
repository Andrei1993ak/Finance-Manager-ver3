package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.Loader;

import com.github.andrei1993ak.finances.model.reportDbHelpers.BalanceChartHelper;

import org.achartengine.model.TimeSeries;

public class BalanceChartLoader extends Loader<TimeSeries> {

    private final long walletID;
    private GetTimeSeriesTask task;

    public BalanceChartLoader(final Context context, final long walletID) {
        super(context);
        this.walletID = walletID;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        if (task != null) {
            task.cancel(true);
        }
        task = new GetTimeSeriesTask();
        task.execute(walletID, walletID);
    }

    private class GetTimeSeriesTask extends AsyncTask<Long, Void, TimeSeries> {

        @Override
        protected TimeSeries doInBackground(final Long... params) {
            return new BalanceChartHelper().getSeries(params[0]);
        }

        private void getResultFromTask(final TimeSeries result) {
            deliverResult(result);
        }

        @Override
        protected void onPostExecute(final TimeSeries series) {
            super.onPostExecute(series);
            getResultFromTask(series);
        }

    }
}
