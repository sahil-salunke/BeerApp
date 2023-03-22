package com.example.beerapp.presentation.beerlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.databinding.BeerListItemBinding

class BeerPagingAdapter :
    PagingDataAdapter<BeerDTO, BeerPagingAdapter.MyViewHolder>(COMPARATOR) {

//    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val quote = itemView.findViewById<TextView>(R.id.beer_name)
//    }

    class MyViewHolder(val viewHolder: BeerListItemBinding) :
        RecyclerView.ViewHolder(viewHolder.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.viewHolder.beer = getItem(position)

//        holder.viewHolder.beerName.text = getItem(position)?.name

//        holder.viewHolder.root.setOnClickListener {
//            listener?.let {
//                it(this.list[position])
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            BeerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<BeerDTO>() {
            override fun areItemsTheSame(oldItem: BeerDTO, newItem: BeerDTO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BeerDTO, newItem: BeerDTO): Boolean {
                return oldItem == newItem
            }
        }
    }
}
