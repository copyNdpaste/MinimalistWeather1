package com.baronzhang.android.weather.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.baronzhang.android.weather.R;
import com.baronzhang.android.weather.WeatherApplication;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 *         16/3/13
 */
public final class CityDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "CityDatabaseHelper";

    private static final String DATABASE_NAME = "city.db";
    private static final int DATABASE_VERSION = 1;

    private static volatile CityDatabaseHelper instance;

    public CityDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        //도시 데이터베이스는 외부적으로 가져오기 때문에 테이블을 만들 필요가 없습니다.
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    /**
     * 单例获取OpenHelper实例
     *
     * @param context application context
     * @return instance
     */
    public static CityDatabaseHelper getInstance(Context context) {

        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (CityDatabaseHelper.class) {
                if (instance == null) {
                    instance = new CityDatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void close() {
        super.close();
        DaoManager.clearCache();
    }

    public <D extends Dao<T, ?>, T> D getCityDao(Class<T> clazz) {
        try {
            return getDao(clazz);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 도시 DB 가져오기
     */
    public static void importCityDB() {

        // 도시의 DB 파일을 유지할지 결정
        File file = new File(WeatherApplication.getInstance().getDatabasePath(DATABASE_NAME).getAbsolutePath());
        if (!file.exists()) {// 존재하지 않으면 DB 파일 가져옴
            // DB 파일
            File dbFile = WeatherApplication.getInstance().getDatabasePath(DATABASE_NAME);
            try {
                if (!dbFile.getParentFile().exists()) {
                    dbFile.getParentFile().mkdir();
                }
                if (!dbFile.exists()) {
                    dbFile.createNewFile();
                }
                //가져올 DB 로드
                InputStream is = WeatherApplication.getInstance().getResources().openRawResource(R.raw.city);
                FileOutputStream fos = new FileOutputStream(dbFile);
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                fos.write(buffer);
                is.close();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
