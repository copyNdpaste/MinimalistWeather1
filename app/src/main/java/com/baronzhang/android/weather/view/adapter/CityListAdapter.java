package com.baronzhang.android.weather.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.baronzhang.android.library.adapter.BaseRecyclerViewAdapter;
import com.baronzhang.android.weather.R;
import com.baronzhang.android.weather.model.db.entities.City;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 *         16/3/16
 */
public class CityListAdapter extends BaseRecyclerViewAdapter<CityListAdapter.ViewHolder> implements Filterable {

    private List<City> cities;
    public List<City> mFilterData;//필터링된 데이터

    private RecyclerViewFilter filter;

    public CityListAdapter(List<City> cities) {
        this.cities = cities;
        mFilterData = cities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        City city = mFilterData.get(position);
        String cityName = city.getCityName();
        String parentName = city.getParent();
        if (!cityName.equals(parentName)) {
            cityName = parentName + "." + cityName;
        }
        holder.cityNameTextView.setText(cityName);
    }

    @Override
    public int getItemCount() {
        return mFilterData == null ? 0 : mFilterData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.city_name_text_view)
        TextView cityNameTextView;

        ViewHolder(View itemView, CityListAdapter cityListAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> cityListAdapter.onItemHolderClick(this));
        }
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new RecyclerViewFilter();
        }
        return filter;
    }

    private class RecyclerViewFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            //반환된 결과가 필터링됨 ArrayList<City>
            FilterResults results = new FilterResults();

            // 제약되지 않은 문자열이 반환됩니다. null
            if (charSequence == null || charSequence.length() == 0) {
                results.values = null;
                results.count = 0;
            } else {
                String prefixString = charSequence.toString().toLowerCase();
                //새 값은 필터링된 데이터를 저장함
                ArrayList<City> newValues = new ArrayList<>();
                Stream.of(cities)
                        .filter(city -> (city.getCityName().contains(prefixString)
                                || city.getCityNameEn().contains(prefixString) || city.getParent().contains(prefixString)
                                || city.getRoot().contains(prefixString)))
                        .forEach(newValues::add);
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mFilterData = (List<City>) filterResults.values;
            if (filterResults.count > 0) {
                notifyDataSetChanged();//현재 보이는 영역 다시 그리기
            } else {
                mFilterData = cities;
                notifyDataSetChanged();//컨트롤 다시 그리기 (초기 상태 복원)
            }
        }
    }
}
