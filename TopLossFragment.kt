package com.example.cryptoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoapp.adapter.MarketAdapter
import com.example.cryptoapp.apis.ApiUtitlities
import com.example.cryptoapp.apis.Apiinterface
import com.example.cryptoapp.databinding.FragmentTopLossBinding
import com.example.cryptoapp.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections

class TopLossFragment : Fragment() {

    lateinit var binding: FragmentTopLossBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTopLossBinding.inflate(layoutInflater, container, false) // Inflate only once

        // Initialize RecyclerView with LinearLayoutManager
        binding.topGainLoseRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        getMarketData()

        return binding.root // Return the root view from binding
    }

    private fun getMarketData() {
        val position = requireArguments().getInt("position")
        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtitlities.getInstance().create(Apiinterface::class.java).getMarketData()

            if (res.body() != null) {
                withContext(Dispatchers.Main) {
                    val dataItem = res.body()!!.data.cryptoCurrencyList

                    Collections.sort(dataItem) { o1, o2 ->
                        (o2.quotes[0].percentChange24h.toInt())
                            .compareTo(o1.quotes[0].percentChange24h.toInt())
                    }

                    binding.spinKitView.visibility = View.VISIBLE

                    val list = ArrayList<CryptoCurrency>()

                    if (position == 0) {
                        list.clear()
                        for (i in 0..9) {
                            list.add(dataItem[i])
                        }
                    } else {
                        list.clear()
                        for (i in 0..9) {
                            list.add(dataItem[dataItem.size - 1 - i])
                        }
                    }

                    binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                        requireContext(),
                        list,
                        "home"
                    )
                    binding.spinKitView.visibility = View.GONE // Hide the loading indicator after data is loaded
                }
            }
        }
    }
}