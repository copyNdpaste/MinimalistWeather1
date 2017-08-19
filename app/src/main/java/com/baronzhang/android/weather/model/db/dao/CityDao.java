package com.baronzhang.android.weather.model.db.dao;

import android.content.Context;

import com.baronzhang.android.weather.model.db.CityDatabaseHelper;
import com.baronzhang.android.weather.model.db.entities.City;
import com.baronzhang.android.weather.model.db.entities.HotCity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

/**
 * City 테이블 연산 클래스스 *
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 *         16/3/13
 */
public class CityDao {

    private Dao<City, Integer> cityDaoOperation;
    private Dao<HotCity, Integer> hotCityDaoOperation;

    @Inject
    CityDao(Context context) {

        this.cityDaoOperation = CityDatabaseHelper.getInstance(context).getCityDao(City.class);
        this.hotCityDaoOperation = CityDatabaseHelper.getInstance(context).getCityDao(HotCity.class);
    }

    /**
     * 테이블에 있는 모든 도시 찾기
     *
     * @return 도시 목록 데이터
     */
    public List<City> queryCityList() {

        try {
            return cityDaoOperation.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 도시 정보에 따라 쿼리
     *
     * @param cityId 城市ID
     * @return city
     * @throws SQLException
     */
    public City queryCityById(String cityId) throws SQLException {

        QueryBuilder<City, Integer> queryBuilder = cityDaoOperation.queryBuilder();
        queryBuilder.where().eq(City.CITY_ID_FIELD_NAME, cityId);

        return queryBuilder.queryForFirst();
    }

    /**
     * 모든 인기 도시 찾기
     *
     * @return 인기 도시 목록
     */
    public List<HotCity> queryAllHotCity() {
        try {
            return hotCityDaoOperation.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
