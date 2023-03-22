package com.example.beerapp.presentation.beerlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.example.beerapp.R
import com.example.beerapp.databinding.FragmentBeerListBinding
import com.example.beerapp.utils.Constants.PER_PAGE
import com.example.beerapp.utils.Constants.hideView
import com.example.beerapp.utils.Constants.showView
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController

@AndroidEntryPoint
class BeerListFragment : Fragment() {

    //    private val beerAdapter = BeerListAdapter()
    private val beerAdapter = BeerPagingAdapter()

    private val viewModel: BeerListViewModel by viewModels()

    private lateinit var binding: FragmentBeerListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBeerListBinding.inflate(inflater, container, false)
        activity?.title = getString(R.string.txt_beer_list)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.beerList.adapter = beerAdapter

        getData()

        lifecycle.coroutineScope.launchWhenCreated {
            viewModel.beerList.collect {

                binding.beerList.hideView()
                binding.errorView.message.hideView()
                binding.errorView.reload.hideView()
                binding.loaderView.hideView()

                if (it.isLoading) {
                    binding.loaderView.showView()
                }

                if (it.error.isNotBlank()) {
                    binding.errorView.message.showView()
                    binding.errorView.reload.showView()
                    binding.errorView.message.text = it.error
                }

                it.data?.let { list ->
//                    if (list.isEmpty()) {
//                        binding.errorView.message.showView()
//                        binding.errorView.message.text = getString(R.string.txt_no_data)
//                        binding.errorView.reload.showView()
//                    }
                    binding.beerList.showView()
//                    beerAdapter.setContentList(list.toMutableList())
                    beerAdapter.submitData(list)
                }
            }
        }

        actionEvents()

    }

    /**
     * Get Data from Api
     */
    private fun getData() {
        viewModel.getBeerList(1, PER_PAGE)
    }

    private fun actionEvents() {

        binding.errorView.reload.setOnClickListener { getData() }

//        beerAdapter.itemClickListener {
//            findNavController().navigate(
//                BeerListFragmentDirections.actionListFragmentToDetailsFragment(it)
//            )
//        }

    }


}