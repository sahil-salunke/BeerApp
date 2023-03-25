package com.example.beerapp.presentation.beerlist

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.beerapp.R
import com.example.beerapp.databinding.FragmentBeerListBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager

@AndroidEntryPoint
class BeerListFragment : Fragment() {

    private lateinit var beerAdapter: BeerPagingAdapter

    private val viewModel: BeerListViewModel by viewModels()

    private lateinit var binding: FragmentBeerListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBeerListBinding.inflate(inflater, container, false)
        activity?.title = getString(R.string.txt_beer_list)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        beerAdapter = BeerPagingAdapter()

        viewModel.isDataAvailable.set(true)

        binding.beerList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = beerAdapter
        }

        viewModel.result.observe(viewLifecycleOwner) {
            beerAdapter.submitData(lifecycle, it)
        }

        adapterEvents()

        viewModel.reloadClick.observe(viewLifecycleOwner) {
            beerAdapter.refresh()
        }

    }

    private fun adapterEvents() {

        beerAdapter.itemClickListener {
            findNavController().navigate(
                BeerListFragmentDirections.actionListFragmentToDetailsFragment(it)
            )
        }

        beerAdapter.addLoadStateListener { loadState ->

            viewModel.isError.set(false)
            if (loadState.refresh is LoadState.Loading) {
                viewModel.isLoading.set(true)
            } else {
                // getting the error
                viewModel.isLoading.set(false)
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }

                if (beerAdapter.itemCount == 0) {
                    viewModel.isError.set(true)
                    viewModel.errorMessage.value = getString(R.string.txt_no_data)
                } else {
                    Log.d("Error ", "${error?.error?.message}")
                }
            }
        }
    }

}