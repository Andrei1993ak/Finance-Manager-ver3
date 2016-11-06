package com.gmail.a93ak.andrei19.finance30.control.ItemsTouchHeplers;

import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.gmail.a93ak.andrei19.finance30.control.Executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.ViewHolders.PursesHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;

public class RecViewPursesSwissHelper extends ItemTouchHelper.SimpleCallback implements OnTaskCompleted {
    private int loaderId;
    private AppCompatActivity targetActivity;

    public RecViewPursesSwissHelper(AppCompatActivity targetActivity,int loaderId) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.loaderId = loaderId;
        this.targetActivity = targetActivity;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        long id = ((PursesHolder) viewHolder).getPurseId();
        RequestHolder<Purse> requestHolder = new RequestHolder<>();
        new PurseExecutor(this).execute(requestHolder.delete(id));
    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()) {
            case PurseExecutor.KEY_RESULT_DELETE:
                final Loader<Object> loader = targetActivity.getSupportLoaderManager().getLoader(loaderId);
                if (loader != null) {
                    loader.forceLoad();
                }
                break;
        }
    }
}
