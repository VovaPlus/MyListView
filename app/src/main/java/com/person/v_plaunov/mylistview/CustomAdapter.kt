package com.person.v_plaunov.mylistview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import java.util.*

class CustomAdapter(context: Context, textViewResourceId: Int, data: ArrayList<Coin>) :
    ArrayAdapter<Coin?>(context, textViewResourceId, data), Filterable {
    private var myArrayList: List<Coin>
    private val originalArrayList: MutableList<Coin>
    private var filter: MyFilter? = null
    var lInflater: LayoutInflater? = null
    var mContext: Context

    init {
        myArrayList = data
        originalArrayList = ArrayList()
        originalArrayList.addAll(data)
        mContext = context
    }

    //    public View getView(int position, View convertView, ViewGroup parent) {
    //        View row = super.getView(position, convertView, parent);
    //        TextView tv = (TextView)row.findViewById(R.id.coin);
    //
    ////        this.mContext=context;
    //        return (row);
    //    }
    private inner class ViewHolder {
        var coinNominal: TextView? = null
        var coinState: TextView? = null
        var coinYear: TextView? = null
        var coinDescription: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        this.mContext = context;
        var convertView = convertView
        var holder: ViewHolder? = null
        Log.v("ConvertView", position.toString())
        if (convertView == null) {
            val vi = mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
            ) as LayoutInflater
            //            LayoutInflater vi = ((Activity)mContext).getLayoutInflater();
            convertView = vi.inflate(R.layout.coin_item, null)
            holder = ViewHolder()
            holder!!.coinNominal = convertView.findViewById<View>(R.id.coin_nominal) as TextView
            holder.coinState = convertView.findViewById<View>(R.id.coin_state) as TextView
            holder.coinYear = convertView.findViewById<View>(R.id.coin_year) as TextView
            holder.coinDescription =
                convertView.findViewById<View>(R.id.coin_description) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val coin = myArrayList[position]
        holder!!.coinNominal!!.text = coin.coinNominal
        holder.coinState!!.text = coin.coinState
        holder.coinYear!!.text = coin.coinYear
        holder.coinDescription!!.text = coin.coinDescription
        return convertView!!
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = MyFilter()
        }
        return filter!!
    }

    private inner class MyFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            var constraint: CharSequence? = constraint
            val result = FilterResults()
            constraint = constraint.toString().lowercase(Locale.getDefault())
            if (constraint == null || constraint.length == 0) {
                synchronized(this) {
                    result.values = originalArrayList
                    result.count = originalArrayList.size
                }
            } else {
                val filteredList = ArrayList<Coin>()
                //                    for (String s : originalArrayList) {
//                    if (s.toLowerCase().contains(constraint))
//                        filteredList.add(s);
//                }
                var i = 0
                val l = originalArrayList.size
                while (i < l) {
                    val coin = originalArrayList[i]
                    if (coin.toString().lowercase(Locale.getDefault())
                            .contains(constraint)
                    ) filteredList.add(coin)
                    i++
                }
                result.values = filteredList
                result.count = filteredList.size
            }
            return result
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            myArrayList = results.values as ArrayList<Coin>
            notifyDataSetChanged()
            clear()
            var i = 0
            val l = myArrayList.size
            while (i < l) {
                add(myArrayList[i])
                i++
            }
            notifyDataSetInvalidated()
        }
    }
}