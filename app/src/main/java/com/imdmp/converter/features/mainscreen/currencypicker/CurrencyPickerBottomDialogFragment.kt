package com.imdmp.converter.features.mainscreen.currencypicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.imdmp.converter.R
import com.imdmp.converter.databinding.FragmentCurrencyPickerBottomDialogBinding
import com.imdmp.converter.usecase.GetAvailableCurrenciesUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyPickerBottomDialogFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentCurrencyPickerBottomDialogBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var getAvailableCurrenciesUseCase: GetAvailableCurrenciesUseCase

    var listener: CurrencyPickerListener? = null
    private val adapter: CurrencyPickerAdapter = CurrencyPickerAdapter {
        listener?.currencySelected(it)
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyPickerBottomDialogBinding.inflate(inflater, container, false)
        binding.rvCurrencies.adapter = adapter
        binding.rvCurrencies.layoutManager = LinearLayoutManager(binding.root.context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val dataList = getAvailableCurrenciesUseCase(true)
                adapter.currencyList = dataList.map { CurrencyModel(it.currencyAbbrev) }
            } catch (e: Exception) {
                Snackbar.make(
                    view.findViewById(android.R.id.content),
                    getString(R.string.error_occurred_please_try_again),
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}