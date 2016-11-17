package com.github.andrei1993ak.finances.app.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.WalletAddActivity;
import com.github.andrei1993ak.finances.app.addEditActivities.WalletEditActivity;
import com.github.andrei1993ak.finances.control.adapters.WalletCursorAdapter;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestHolder;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.WalletExecutor;
import com.github.andrei1993ak.finances.control.loaders.WalletCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Wallet;

public class WalletActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {


    private static final int ADD_WALLET_REQUEST = 1;
    private static final int EDIT_WALLET_REQUEST = 2;

    public static final int MAIN_LOADER_ID = 0;

    private WalletCursorAdapter walletCursorAdapter;
    private RequestHolder<Wallet> requestHolder;
    private ListView lvWallets;
    private long itemId = -1;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.wallets);
        setContentView(R.layout.standart_activity);
        requestHolder = new RequestHolder<>();
        walletCursorAdapter = new WalletCursorAdapter(this, null);
        lvWallets = (ListView) findViewById(R.id.standardListView);
        lvWallets.setAdapter(walletCursorAdapter);
        lvWallets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                itemId = id;
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(WalletActivity.this, WalletAddActivity.class);
                startActivityForResult(intent, ADD_WALLET_REQUEST);
            }
        });
        getSupportLoaderManager().restartLoader(MAIN_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (itemId != -1) {
            if (id == R.id.action_edit) {
                final Intent intent = new Intent(this, WalletEditActivity.class);
                intent.putExtra(Wallet.ID, itemId);
                startActivityForResult(intent, EDIT_WALLET_REQUEST);
                return true;
            } else {
                new WalletExecutor(this).execute(requestHolder.delete(itemId));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_WALLET_REQUEST:
                    final Wallet newWallet = data.getParcelableExtra(TableQueryGenerator.getTableName(Wallet.class));
                    new WalletExecutor(this).execute(requestHolder.add(newWallet));
                    break;
                case EDIT_WALLET_REQUEST:
                    final Wallet editWallet = data.getParcelableExtra(TableQueryGenerator.getTableName(Wallet.class));
                    new WalletExecutor(this).execute(requestHolder.edit(editWallet));
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new WalletCursorLoader(this);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        walletCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        walletCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(final Result result) {
        final int id = result.getId();
        switch (id) {
            case WalletExecutor.KEY_RESULT_ADD:
                if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                    getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
                }
                break;
            case WalletExecutor.KEY_RESULT_EDIT:
                if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                    getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
                }
                break;
            case WalletExecutor.KEY_RESULT_DELETE:
                if ((Integer) result.getObject() == -1) {
                    Toast.makeText(this, R.string.unpossibleToDeleteWallet, Toast.LENGTH_LONG).show();
                } else {
                    if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                        getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
                    }
                }
                break;
            default:
                break;
        }
    }
}
