package com.example.beerapp.presentation.beerlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beerapp.data.model.BeerDTO
import com.example.beerapp.databinding.BeerListItemBinding

class BeerListAdapter : RecyclerView.Adapter<BeerListAdapter.MyViewHolder>() {

    private var listener: ((BeerDTO) -> Unit)? = null

    var list = mutableListOf<BeerDTO>()

    fun setContentList(list: MutableList<BeerDTO>) {
        this.list = list
//        notifyDataSetChanged()
    }


    class MyViewHolder(val viewHolder: BeerListItemBinding) :
        RecyclerView.ViewHolder(viewHolder.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            BeerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    fun itemClickListener(l: (BeerDTO) -> Unit) {
        listener = l
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.viewHolder.beer = this.list[position]

        holder.viewHolder.root.setOnClickListener {
            listener?.let {
                it(this.list[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return this.list.size
    }
}