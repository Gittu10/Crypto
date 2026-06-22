package com.example.cryptoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cryptoapp.databinding.FragmentDetailsBinding
import com.example.cryptoapp.models.CryptoCurrency


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val item: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        val data: CryptoCurrency = item.data!!

        setUpDetails(data)
        loadChart(data, "D") // Load initial chart with daily interval
        setButtonClick(data)

        return binding.root
    }

    private fun setButtonClick(item: CryptoCurrency) {
        val buttons = listOf(
            binding.button, binding.button1, binding.button2,
            binding.button3, binding.button4, binding.button5
        )
        val intervals = listOf("M", "W", "D", "4H", "1H", "15")

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                loadChart(item, intervals[index])
                // Optionally, update button appearances to indicate the selected interval
                // For example, you could reset backgrounds for all buttons and set the
                // background for the clicked button to indicate it's active.
            }
        }
    }

    private fun loadChart(item: CryptoCurrency, interval: String) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        val url = "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" +
                "${item.symbol}USD&interval=$interval&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1" +
                "&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1" +
                "&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]" +
                "&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget" +
                "&utm_campaign=chart&utm_term=BTCUSDT"

        binding.detaillChartWebView.loadUrl(url)
    }

    private fun setUpDetails(data: CryptoCurrency) {
        binding.detailSymbolTextView.text = data.symbol

        Glide.with(requireContext())
            .load("https://s2.coinmarketcap.com/static/img/coins/64x64/${data.id}.png")
            .thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        binding.detailPriceTextView.text = String.format("$%.04f", data.quotes[0].price)

        val change = data.quotes[0].percentChange24h
        binding.detailChangeTextView.text = String.format("%.02f", change) + " %"
        if (change > 0) {
            binding.detailChangeTextView.setTextColor(requireContext().getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
        } else {
            binding.detailChangeTextView.setTextColor(requireContext().getColor(R.color.crimson))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
        }
    }
}