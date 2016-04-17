package org.codeselect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import org.codeselect.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CodeMyMobile on 14-04-2016.
 */
public class DataHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
            MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MovieEntry.MOVIE_ID + " INTEGER UNIQUE NOT NULL," +
            MovieEntry.ORIGINAL_TITLE + " TEXT," +
            MovieEntry.TITLE + " TEXT," +
            MovieEntry.POSTER_IMAGE + " TEXT," +
            MovieEntry.OVERVIEW + " TEXT," +
            MovieEntry.RELEASE_DATE + " TEXT," +
            MovieEntry.RATING + " FLOAT" + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;


    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie_entry";
        public static final String MOVIE_ID = "movie_id";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String TITLE = "title";
        public static final String POSTER_IMAGE = "poster_path";
        public static final String OVERVIEW = "overview";
        public static final String RATING = "vote_average";
        public static final String RELEASE_DATE = "release_date";
    }


    public static DataHelper mInstance;

    private DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DataHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public long addToFavourite(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.MOVIE_ID, movie.getId());
        values.put(MovieEntry.ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieEntry.TITLE, movie.getTitle());
        values.put(MovieEntry.POSTER_IMAGE, movie.getImage());
        values.put(MovieEntry.RATING, movie.getRating());
        values.put(MovieEntry.RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieEntry.OVERVIEW, movie.getOverview());

        return getWritableDatabase().insert(MovieEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Movie> readAllFavourite() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        ArrayList<Movie> movieList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(MovieEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieEntry.MOVIE_ID)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.ORIGINAL_TITLE)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.TITLE)));
                movie.setImage(cursor.getString(cursor.getColumnIndex(MovieEntry.POSTER_IMAGE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieEntry.RELEASE_DATE)));
                movie.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(MovieEntry.RATING)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieEntry.OVERVIEW)));

                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return movieList;
    }
}
