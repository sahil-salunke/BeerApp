package com.example.beerapp.presentation.beerlist

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.beerapp.R
import com.example.beerapp.databinding.FragmentBeerListBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.beerapp.presentation.MainActivity
import com.example.beerapp.utils.Constants.hideView
import com.example.beerapp.utils.Constants.showView
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class BeerListFragment : Fragment() {

    private lateinit var beerAdapter: BeerPagingAdapter

    private val viewModel: BeerListViewModel by viewModels()

    private lateinit var binding: FragmentBeerListBinding

    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = getString(R.string.txt_beer_list)
        mainActivity = (activity as MainActivity)
        viewModel.getBeersData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBeerListBinding.inflate(inflater, container, false)
        beerAdapter = BeerPagingAdapter()
        binding.viewModel = viewModel
        binding.adapter = beerAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.resultList.collectLatest {
                beerAdapter.submitData(lifecycle, it)
            }
        }

        adapterEvents()

        viewModel.reloadClick.observe(viewLifecycleOwner) { beerAdapter.refresh() }

    }

    private fun adapterEvents() {

        beerAdapter.itemClickListener {
            findNavController().navigate(
                BeerListFragmentDirections.actionListFragmentToDetailsFragment(it)
            )
        }

        beerAdapter.addLoadStateListener { loadState ->
            hideErrorView()
            if (loadState.refresh is LoadState.Loading) {
                mainActivity.showProgress()
            } else {
                mainActivity.hideProgress()
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }

                if (beerAdapter.itemCount == 0) {
                    showErrorView(getString(R.string.txt_no_data))
                } else {
                    Log.e("ERROR ", "${error?.error?.localizedMessage}")
                }
            }
        }
    }

    private fun showErrorView(error: String) {
        binding.errorView.message.text = error
        binding.errorView.message.showView()
        binding.errorView.reload.showView()
    }

    private fun hideErrorView() {
        binding.errorView.message.hideView()
        binding.errorView.reload.hideView()
    }
}