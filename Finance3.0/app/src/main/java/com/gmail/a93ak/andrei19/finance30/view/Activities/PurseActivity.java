package com.gmail.a93ak.andrei19.finance30.view.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gmail.a93ak.andrei19.finance30.App;
import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.adapters.PurseCursorAdapter;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.control.executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.loaders.PurseCursorLoader;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.PurseAddActivity;
import com.gmail.a93ak.andrei19.finance30.view.addEditActivities.PurseEditActivity;

public class PurseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnTaskCompleted {


    private static final int ADD_PURSE_REQUEST = 1;
    private static final int EDIT_PURSE_REQUEST = 2;

    public static final int MAIN_LOADER_ID = 0;

    private PurseCursorAdapter purseCursorAdapter;
    private RequestHolder<Purse> requestHolder;
    private ListView lvPurses;
    private long itemId = -1;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setTitle(R.string.purses);
        setContentView(R.layout.standart_activity);
        requestHolder = new RequestHolder<>();
        purseCursorAdapter = new PurseCursorAdapter(this, null);
        lvPurses = (ListView) findViewById(R.id.standartListView);
        lvPurses.setAdapter(purseCursorAdapter);
        lvPurses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                itemId = id;
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(PurseActivity.this, PurseAddActivity.class);
                startActivityForResult(intent, ADD_PURSE_REQUEST);
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
                final Intent intent = new Intent(this, PurseEditActivity.class);
                intent.putExtra(Purse.ID, itemId);
                startActivityForResult(intent, EDIT_PURSE_REQUEST);
                return true;
            } else {
                new PurseExecutor(this).execute(requestHolder.delete(itemId));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_PURSE_REQUEST:
                    final Purse newPurse = data.getParcelableExtra(TableQueryGenerator.getTableName(Purse.class));
                    new PurseExecutor(this).execute(requestHolder.add(newPurse));
                    break;
                case EDIT_PURSE_REQUEST:
                    final Purse editPurse = data.getParcelableExtra(TableQueryGenerator.getTableName(Purse.class));
                    new PurseExecutor(this).execute(requestHolder.edit(editPurse));
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new PurseCursorLoader(this);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        purseCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        purseCursorAdapter.swapCursor(null);
    }

    @Override
    public void onTaskCompleted(final Result result) {
        final int id = result.getId();
        switch (id) {
            case PurseExecutor.KEY_RESULT_ADD:
                if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                    getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
                }
                break;
            case PurseExecutor.KEY_RESULT_EDIT:
                if (getSupportLoaderManager().getLoader(MAIN_LOADER_ID) != null) {
                    getSupportLoaderManager().getLoader(MAIN_LOADER_ID).forceLoad();
                }
                break;
            case PurseExecutor.KEY_RESULT_DELETE:
                if ((Integer) result.getObject() == -1) {
                    Toast.makeText(this, R.string.unpossibleToDeletePurse, Toast.LENGTH_LONG).show();
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
