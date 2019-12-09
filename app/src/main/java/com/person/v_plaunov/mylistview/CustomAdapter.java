package com.person.v_plaunov.mylistview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Coin> implements Filterable {
    private List<Coin> myArrayList;
    private List<Coin> originalArrayList;
    private MyFilter filter;
    LayoutInflater lInflater;
    Context mContext;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<Coin> data) {
        super(context, textViewResourceId, data);
        this.myArrayList = data;
        this.originalArrayList = new ArrayList<Coin>();
        this.originalArrayList.addAll(data);
        mContext = context;
    }
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = super.getView(position, convertView, parent);
//        TextView tv = (TextView)row.findViewById(R.id.coin);
//
////        this.mContext=context;
//        return (row);
//    }

    private class ViewHolder {
        TextView coinNominal;
        TextView coinState;
        TextView coinYear;
        TextView coinDescription;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        this.mContext = context;
        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
//            LayoutInflater vi = ((Activity)mContext).getLayoutInflater();
            convertView = vi.inflate(R.layout.coin_item, null);

            holder = new ViewHolder();
            holder.coinNominal = (TextView) convertView.findViewById(R.id.coin_nominal);
            holder.coinState = (TextView) convertView.findViewById(R.id.coin_state);
            holder.coinYear = (TextView) convertView.findViewById(R.id.coin_year);
            holder.coinDescription = (TextView) convertView.findViewById(R.id.coin_description);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Coin coin = myArrayList.get(position);
        holder.coinNominal.setText(coin.getCoinNominal());
        holder.coinState.setText(coin.getCoinState());
        holder.coinYear.setText(coin.getCoinYear());
        holder.coinDescription.setText(coin.getCoinDescription());

        return convertView;
    }

    public Filter getFilter() {
        if (filter == null){
            filter  = new MyFilter();
        }
        return filter;
    }

    private class MyFilter extends Filter
    {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults result = new FilterResults();
                constraint = constraint.toString().toLowerCase();
                if (constraint == null || constraint.length() == 0) {
                    synchronized (this)
                    {
                        result.values = originalArrayList;
                        result.count = originalArrayList.size();
                    }
                }
                else {
                    ArrayList<Coin> filteredList = new ArrayList<>();
//                    for (String s : originalArrayList) {
//                    if (s.toLowerCase().contains(constraint))
//                        filteredList.add(s);
//                }
                     for (int i = 0,  l = originalArrayList.size(); i < l; i ++) {
                         Coin coin = originalArrayList.get(i);
                         if (coin.toString().toLowerCase().contains(constraint))
                            filteredList.add(coin);
                    }
                    result.values = filteredList;
                    result.count = filteredList.size();
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                myArrayList = (ArrayList<Coin>)results.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = myArrayList.size(); i < l; i++)
                    add(myArrayList.get(i));
                notifyDataSetInvalidated();
            }
        }

}

