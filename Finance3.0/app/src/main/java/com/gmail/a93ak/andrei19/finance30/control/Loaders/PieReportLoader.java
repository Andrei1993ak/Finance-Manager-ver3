package com.gmail.a93ak.andrei19.finance30.control.Loaders;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.Loader;

import com.gmail.a93ak.andrei19.finance30.model.reportDbHelpers.IncomePieReportHelper;
import com.gmail.a93ak.andrei19.finance30.model.reportModels.IncomePieCategory;

import java.util.ArrayList;

public class PieReportLoader extends Loader<ArrayList<IncomePieCategory>> {

    private GetValuesTask task;
    private final long id;

    public PieReportLoader(final Context context, final long id) {

        super(context);
        this.id = id;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        if (task != null) {
            task.cancel(true);
        }
        task = new GetValuesTask();
        task.execute(id);
    }

    private void getResultFromTask(final ArrayList<IncomePieCategory> result) {
        deliverResult(result);
    }

    private class GetValuesTask extends AsyncTask<Long, Void, ArrayList<IncomePieCategory>> {

        @Override
        protected ArrayList<IncomePieCategory> doInBackground(final Long... params) {
            return IncomePieReportHelper.getInstance().gRepInfo(params[0]);
        }

        @Override
        protected void onPostExecute(final ArrayList<IncomePieCategory> list) {
            super.onPostExecute(list);
            getResultFromTask(list);
        }

    }
}
