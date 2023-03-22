package com.example.beerapp.presentation.beerlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.databinding.BeerListItemBinding

class BeerPagingAdapter : PagingDataAdapter<BeerDTO, BeerPagingAdapter.MyViewHolder>(
    COMPARATOR
) {

    private var listener: ((BeerDTO) -> Unit)? = null

    class MyViewHolder(val viewHolder: BeerListItemBinding) :
        RecyclerView.ViewHolder(viewHolder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            BeerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.viewHolder.beer = getItem(position)

        holder.viewHolder.root.setOnClickListener {
            listener?.let {
                getItem(position)?.let { it1 -> it(it1) }
            }
        }
    }

    fun itemClickListener(l: (BeerDTO) -> Unit) {
        listener = l
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