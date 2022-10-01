package com.imdmp.converter.features.mainscreen

import com.imdmp.converter.features.mainscreen.currencydisplay.CurrencyDisplayCallbacks
import com.imdmp.converter.features.mainscreen.numberscreen.NumberScreenCallbacks

interface ConverterScreenCallbacks: CurrencyDisplayCallbacks, NumberScreenCallbacks {

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

                override fun inputBoxSelected(selectedInputBox: SelectedInputBox) {
                    TODO("Not yet implemented")
                }

                override fun characterEmitted(c: Char) {
                    TODO("Not yet implemented")
                }

            }
        }
    }
}