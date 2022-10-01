package com.imdmp.converter.features.mainscreen

import com.imdmp.converter.features.mainscreen.currencydisplay.CurrencyDisplayCallbacks

interface ConverterScreenCallbacks: CurrencyDisplayCallbacks {

    companion object {
        fun default(): ConverterScreenCallbacks {
            return object: ConverterScreenCallbacks {
                override fun onSellDataUpdated(data: Double) {
                    TODO("Not yet implemented")
                }

                override fun onBuyDataUpdated(data: Double) {
                    TODO("Not yet implemented")
                }

                override fun switchCurrencyLabels() {
                    TODO("Not yet implemented")
                }

            }
        }
    }
}