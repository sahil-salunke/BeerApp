package com.example.beerapp.presentation.beerdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.beerapp.R
import com.example.beerapp.databinding.FragmentBeerDetailsBinding
import com.example.beerapp.presentation.beerlist.BeerListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeerDetailsFragment : Fragment() {

    private val viewModel: BeerListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentBeerDetailsBinding.inflate(inflater, container, false)
        activity?.title = getString(R.string.txt_details)

        // Receive data as an argument from previous fragment
        val beer = arguments?.let { BeerDetailsFragmentArgs.fromBundle(it).beer }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.selectedBeer.value = beer

        return binding.root
    }
}