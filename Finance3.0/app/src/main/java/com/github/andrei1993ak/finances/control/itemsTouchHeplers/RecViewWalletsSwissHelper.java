package com.github.andrei1993ak.finances.control.itemsTouchHeplers;

import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.github.andrei1993ak.finances.control.executors.WalletExecutor;
import com.github.andrei1993ak.finances.control.viewHolders.WalletsHolder;
import com.github.andrei1993ak.finances.control.base.IOnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.models.Wallet;

public class RecViewWalletsSwissHelper extends ItemTouchHelper.SimpleCallback implements IOnTaskCompleted {
    private final int loaderId;
    private final AppCompatActivity targetActivity;

    public RecViewWalletsSwissHelper(final AppCompatActivity targetActivity, final int loaderId) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.loaderId = loaderId;
        this.targetActivity = targetActivity;
    }

    @Override
    public boolean onMove(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
        final long id = ((WalletsHolder) viewHolder).getWalletId();
        final RequestAdapter<Wallet> requestAdapter = new RequestAdapter<>();
        new WalletExecutor(this).execute(requestAdapter.delete(id));
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case WalletExecutor.KEY_RESULT_DELETE:
                final Loader<Object> loader = targetActivity.getSupportLoaderManager().getLoader(loaderId);
                if (loader != null) {
                    loader.forceLoad();
                }
                break;
        }
    }
}
