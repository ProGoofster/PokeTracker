package com.example.poketracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PokedexContentProvider extends ContentProvider {
    public static final String TABLE_NAME = "PokedexTable";
    public static final String DB_NAME = "PokedexDB";
    public static final String COL_NATNUM = "NATIONALNUM";
    public static final String COL_NAME = "NAME";
    public static final String COL_SPECIES = "SPECIES";
    public static final String COL_GENDER = "GENDER";
    public static final String COL_HEIGHT = "HEIGHT_M";
    public static final String COL_WEIGHT = "WEIGHT_KG";
    public static final String COL_LEVEL = "LEVEL";
    public static final String COL_HP = "HP";
    public static final String COL_ATTACK = "ATTACK";
    public static final String COL_DEFENSE = "DEFENSE";

    public final static String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NATNUM + " INTEGER, " +
            COL_NAME + " TEXT, " +
            COL_SPECIES + " TEXT, " +
            COL_GENDER + " TEXT, " +
            COL_HEIGHT + " REAL, " +
            COL_WEIGHT + " REAL, " +
            COL_LEVEL + " INTEGER, " +
            COL_HP + " INTEGER, " +
            COL_ATTACK + " INTEGER, " +
            COL_DEFENSE + " INTEGER" +
            ")";

    public static final Uri CONTENT_URI = Uri.parse("content://com.example.poketracker.provider");

    MainDatabaseHelper mHelper;

    protected final class MainDatabaseHelper extends SQLiteOpenHelper {


        public MainDatabaseHelper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String natNum = values.getAsString(COL_NATNUM);
        String name = values.getAsString(COL_NAME);
        String species = values.getAsString(COL_SPECIES);
        String gender = values.getAsString(COL_GENDER);
        String height = values.getAsString(COL_HEIGHT);
        String weight = values.getAsString(COL_WEIGHT);
        String level = values.getAsString(COL_LEVEL);
        String hp = values.getAsString(COL_HP);
        String attack = values.getAsString(COL_ATTACK);
        String defense = values.getAsString(COL_DEFENSE);
        mHelper.getWritableDatabase().insert(TABLE_NAME, null, values);
        long id = mHelper.getWritableDatabase().insert(TABLE_NAME, null, values);
        return uri.withAppendedPath(uri, id + "");
    }

    @Override
    public boolean onCreate() {
        mHelper = new MainDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return mHelper.getReadableDatabase().query(TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
