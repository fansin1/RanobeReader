package com.fansin.ranobereader.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.fansin.ranobereader.R
import com.fansin.ranobereader.data.Chapter

/**
 * Created by artem on 10.08.2018.
 */

@Suppress("UNCHECKED_CAST")
class ChaptersAdapter(internal var items: ArrayList<Chapter>, var onItemClick: ((Chapter, Int) -> Unit)?) : RecyclerView.Adapter<ChaptersAdapter.ViewHolder>(), Filterable {

    internal var filteredItems : ArrayList<Chapter>


    init {
        filteredItems = items
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                filteredItems = filterResults!!.values as ArrayList<Chapter>
                notifyDataSetChanged()
            }

            override fun performFiltering(filterCharSequence: CharSequence?): FilterResults {
                val filterString = filterCharSequence.toString()
                filteredItems = if (filterString.isEmpty()) {
                    items
                } else {
                    val resultItems = ArrayList<Chapter>()
                    items.filterTo(resultItems) { it.title.contains(filterString.toLowerCase()) }
                    resultItems
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = filteredItems
                return filterResults
            }
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private var view : View = itemView
        private var tv : TextView = view.findViewById(R.id.tv_name)

        fun bind(name:String) {
            tv.text = name
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(filteredItems[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredItems[position].title)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chapter_list_item, parent, false))

}