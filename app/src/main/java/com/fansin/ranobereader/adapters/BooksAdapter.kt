package com.fansin.ranobereader.adapters

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.fansin.ranobereader.R
import com.fansin.ranobereader.data.Book
import com.squareup.picasso.Picasso

/* Created by artem on 10.08.2018.
*/

@Suppress("UNCHECKED_CAST")
class BooksAdapter(private var items: ArrayList<Book>, private var onItemClick: ((Book) -> Unit)?) : RecyclerView.Adapter<BooksAdapter.ViewHolder>(), Filterable {

    private var filteredItems : ArrayList<Book> = arrayListOf()

    init {
        filteredItems.addAll(items)
    }

    fun updateItems(booksList: ArrayList<Book>) {
        filteredItems.clear()
        filteredItems.addAll(booksList)
        this.notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                filteredItems = filterResults!!.values as ArrayList<Book>
                notifyDataSetChanged()
            }

            override fun performFiltering(filterCharSequence: CharSequence?): FilterResults {
                val filterString = filterCharSequence.toString()
                filteredItems = if (filterString.isEmpty()) {
                    items
                } else {
                    val resultItems = ArrayList<Book>()
                    items.filterTo(resultItems) { it.title.toLowerCase().contains(filterString.toLowerCase()) }
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
        private var tvName : TextView = view.findViewById(R.id.tv_name)
        private var tvDesc : TextView = view.findViewById(R.id.tv_description)
        private var thumbnail : ImageView = view.findViewById(R.id.thumbnail)

        @SuppressWarnings("deprecation")
        fun bind(name:String, imageUrl: String, description: String) {
            tvName.text = name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvDesc.text = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
            } else {
                tvDesc.text = Html.fromHtml(description)
            }
            Picasso.get().load(imageUrl).into(thumbnail)
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(filteredItems[adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredItems[position].title, filteredItems[position].imageUrl, filteredItems[position].description)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false))

}