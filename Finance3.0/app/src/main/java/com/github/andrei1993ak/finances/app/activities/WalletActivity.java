package com.github.andrei1993ak.finances.app.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.WalletExecutor;
import com.github.andrei1993ak.finances.control.loaders.WalletCursorLoader;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.Constants;

public class WalletActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {

    private WalletCursorAdapter walletCursorAdapter;
    private RequestAdapter<Wallet> requestAdapter;
    private MenuInflater menuInflater;
    private long selectedItemId;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standart_activity);
        setTitle(R.string.wallets);
        initFields();
        getSupportLoaderManager().restartLoader(Constants.MAIN_LOADER_ID, null, this);
    }

    private void initFields() {
        this.selectedItemId = Constants.NOT_SELECTED;
        this.menuInflater = getMenuInflater();
        this.requestAdapter = new RequestAdapter<>();
        this.walletCursorAdapter = new WalletCursorAdapter(this, null);
        final ListView lvWallets = (ListView) findViewById(R.id.standardListView);
        lvWallets.setAdapter(walletCursorAdapter);
        lvWallets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                selectedItemId = id;
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(WalletActivity.this, WalletAddActivity.class);
                startActivityForResult(intent, Constants.ADD_REQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (selectedItemId != Constants.NOT_SELECTED) {
            if (id == R.id.action_edit) {
                final Intent intent = new Intent(this, WalletEditActivity.class);
                intent.putExtra(Wallet.ID, selectedItemId);
                startActivityForResult(intent, Constants.EDIT_REQUEST);
                return true;
            } else {
                new WalletExecutor(this).execute(requestAdapter.delete(selectedItemId));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.ADD_REQUEST:
                    final Wallet newWallet = data.getParcelableExtra(TableQueryGenerator.getTableName(Wallet.class));
                    new WalletExecutor(this).execute(requestAdapter.add(newWallet));
                    break;
                case Constants.EDIT_REQUEST:
                    final Wallet editWallet = data.getParcelableExtra(TableQueryGenerator.getTableName(Wallet.class));
                    new WalletExecutor(this).execute(requestAdapter.edit(editWallet));
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
        final Loader<Wallet> loader = getSupportLoaderManager().getLoader(Constants.MAIN_LOADER_ID);
        switch (id) {
            case WalletExecutor.KEY_RESULT_ADD:
                if (loader != null) {
                    loader.forceLoad();
                }
                break;
            case WalletExecutor.KEY_RESULT_EDIT:
                if (loader != null) {
                    loader.forceLoad();
                }
                break;
            case WalletExecutor.KEY_RESULT_DELETE:
                if ((Integer) result.getObject() == -1) {
                    Toast.makeText(this, R.string.unpossibleToDeleteWallet, Toast.LENGTH_LONG).show();
                } else {
                    if (loader != null) {
                        loader.forceLoad();
                    }
                }
                break;
            default:
                break;
        }
    }
}
