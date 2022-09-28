package com.imdmp.converter.features.mainscreen

interface ConverterScreenCallbacks {
    fun onSellDataUpdated(data: Double)
    fun onBuyDataUpdated(data: Double)

    companion object {
        fun default(): ConverterScreenCallbacks {
            return object: ConverterScreenCallbacks {
                override fun onSellDataUpdated(data: Double) {
                    TODO("Not yet implemented")
                }

                override fun onBuyDataUpdated(data: Double) {
                    TODO("Not yet implemented")
                }

            }
        }
    }
}