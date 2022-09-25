package com.imdmp.converter.features.mainscreen.currencypicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imdmp.converter.databinding.CurrencyPickerItemBinding

class CurrencyPickerAdapter(private val onItemClicked: (currencyModel: CurrencyModel) -> Unit):
    RecyclerView.Adapter<CurrencyPickerAdapter.ViewHolder>() {

    var currencyList = listOf<CurrencyModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CurrencyPickerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = currencyList[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    class ViewHolder(
        private val binding: CurrencyPickerItemBinding,
        private val onItemClicked: (currencyModel: CurrencyModel) -> Unit
    ):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currency: CurrencyModel) {
            binding.tvCurrencyAbbrev.text = currency.abbrev
            binding.root.setOnClickListener {
                onItemClicked(currency)
            }
        }

    }

}