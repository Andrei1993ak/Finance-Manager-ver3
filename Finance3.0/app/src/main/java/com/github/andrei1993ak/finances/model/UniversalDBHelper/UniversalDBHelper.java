package com.github.andrei1993ak.finances.model.UniversalDBHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.TableClass;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.util.List;

public class UniversalDBHelper<Model extends TableClass> implements IUniversalDBHelper<Model> {

    private final DBHelper dbHelper;
    private final ModelsFromCursorGenerator<Model> cursorGenerator;
    private final ContentValuesFromModelGenerator<Model> cvGenerator;

    public UniversalDBHelper() {
        this.dbHelper = DBHelper.getInstance(ContextHolder.getInstance().getContext());
        this.cursorGenerator = new ModelsFromCursorGenerator<>();
        this.cvGenerator = new ContentValuesFromModelGenerator<>();
    }

    @Override
    public long add(final Model model) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = cvGenerator.generateCVFromModel(model);
        if (values != null) {
            final long id;
            try {
                db.beginTransaction();
                id = db.insert(TableQueryGenerator.getTableName(Wallet.class), null, values);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            return id;
        } else {
            return 0;
        }
    }

    @Override
    public Model get(final long id, final Class<Model> clazz) {
        Cursor cursor = null;
        try {
            final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(clazz) + " WHERE _id = " + id;
            final SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursorGenerator.generateModelFromCursor(cursor, clazz);
            } else {
                return null;
            }
        } catch (final Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public Cursor getAll(final Class<Model> clazz) {
        final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(clazz);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public List<Model> getAllToList(final Class<Model> clazz) {
        Cursor cursor = null;
        try {
            final String selectQuery = "SELECT * FROM " + TableQueryGenerator.getTableName(clazz);
            final SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            return cursorGenerator.generateListOfModelsFromCursor(cursor, clazz);
        } catch (final Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public int update(final Model model, final long id) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final ContentValues values = cvGenerator.generateCVFromModel(model);
        if (values != null) {
            final int count;
            try {
                db.beginTransaction();
                count = db.update(TableQueryGenerator.getTableName(model.getClass()), values, Model.ID + "=?", new String[]{String.valueOf(id)});
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            return count;
        } else {
            return 0;
        }
    }

    @Override
    public int delete(final long id, final Class<Model> clazz) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int count;
        try {
            db.beginTransaction();
            count = db.delete(TableQueryGenerator.getTableName(clazz), Model.ID + " = " + id, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int deleteAll(final Class<Model> clazz) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(TableQueryGenerator.getTableName(clazz), null, null);
    }
}
