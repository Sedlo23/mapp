package com.sedlo.mapp1.UserConnection;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.sedlo.mapp1.Activity.LoginActivity;
import com.sedlo.mapp1.Activity.PlayersListActivity;
import com.sedlo.mapp1.Activity.TrainingsListActivity;

public class PrefManager {
    private static final String SHARED_PREF_NAME = "prodevsblogpref";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_ID = "user_id";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String DB_NAME_PLAYERS= "players";
    private static final String DB_NAME_TRAININGS= "trainings";
    private static final String DB_NAME_ABS= "abs";

    private SQLiteDatabase mydatabase;

    private PlayersListActivity playersListActivity;

    private TrainingsListActivity trainingsListActivity;

    private static PrefManager mInstance;

    private static Context mCtx;

    private PrefManager (Context context) {
        mCtx = context;
    }

    public static synchronized PrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrefManager(context);
        }
        return mInstance;
    }

    public void setUserLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putBoolean(KEY_IS_LOGGED_IN,true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    //this method will give the logged in user
    public static User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null)
        );
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }

    public static String getBDName()
    {
        return  getUser().getUsername().replace(" ","")+".db";

    }

    public static String getDbNamePlayers() {
        return DB_NAME_PLAYERS;
    }

    public SQLiteDatabase getMydatabase() {
        return mydatabase;
    }

    public PrefManager setMydatabase(SQLiteDatabase mydatabase) {
        this.mydatabase = mydatabase;
        return this;
    }

    public PlayersListActivity getPlayersListActivity() {
        return playersListActivity;
    }

    public PrefManager setPlayersListActivity(PlayersListActivity playersListActivity) {
        this.playersListActivity = playersListActivity;
        return this;
    }

    public TrainingsListActivity getTrainingsListActivity() {
        return trainingsListActivity;
    }

    public PrefManager setTrainingsListActivity(TrainingsListActivity trainingsListActivity) {
        this.trainingsListActivity = trainingsListActivity;
        return this;
    }

    public static String getDbNameTrainings() {
        return DB_NAME_TRAININGS;
    }


    public static String getDbNameAbs() {
        return DB_NAME_ABS;
    }
}
