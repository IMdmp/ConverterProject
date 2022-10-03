package com.imdmp.converter.repository.network

import com.imdmp.converter.repository.network.ConverterService
import com.imdmp.converter.repository.network.response.ConvertCurrencyResponse
import com.imdmp.converter.repository.network.response.GetSupportedCurrenciesResponse
import com.imdmp.converter.repository.network.response.Info
import com.imdmp.converter.schema.PullLatestRatesSchema
import com.imdmp.converter.schema.convertToPullLatestRatesSchema
import com.imdmp.converter.schema.toMap
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit


class ConverterApiServiceTest {
    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ConverterService::class.java)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `pullLatestRates correctly converts response data`() {
        // testing if api call correctly converted to response class. error handling done in different test.
        mockWebServer.enqueueResponse("pull_latest_rates.json", 200)

        val sampleBase = "EUR"

        runBlocking {
            val call = api.pullLatestRates(sampleBase)

            val jsonObject = JSONObject(call)
            val result = jsonObject.convertToPullLatestRatesSchema()
            val expected = PullLatestRatesSchema(
                base = "EUR",
                date = "2022-09-24",
                timestamp = 1663990563,
                rates = (jsonObject.get("rates") as JSONObject).toMap() as Map<String, BigDecimal>
            )
            assertEquals(result, expected)
        }
    }

    @Test
    fun `getSupported correctly converts response data`() {
        mockWebServer.enqueueResponse("get_supported_rates.json", 200)
        val expected = GetSupportedCurrenciesResponse(
            getSupportedCurrencyMap()
        )
        runBlocking {
            val result = api.getSupportedCurrencies()
            assertEquals(expected, result)
        }
    }

    @Test
    fun `convertCurrency correctly converts response data`() {
        mockWebServer.enqueueResponse("convert_currency.json", 200)

        val expected = ConvertCurrencyResponse(
            info = Info(timestamp = 1664710084, rate = 0.980478),
            date = "2022-10-02",
            result = 98.0478
        )

        runBlocking {
            val result = api.convertCurrency(from = "", to = "", amount = "")

            assertEquals(expected, result)
        }
    }

    private fun getSupportedCurrencyMap(): Map<String, String> {
        return mapOf(
            Pair("AED", "United Arab Emirates Dirham"),
            Pair("AFN", "Afghan Afghani"),
            Pair("ALL", "Albanian Lek"),
            Pair("AMD", "Armenian Dram"),
            Pair("ANG", "Netherlands Antillean Guilder"),
            Pair("AOA", "Angolan Kwanza"),
            Pair("ARS", "Argentine Peso"),
            Pair("AUD", "Australian Dollar"),
            Pair("AWG", "Aruban Florin"),
            Pair("AZN", "Azerbaijani Manat"),
            Pair("BAM", "Bosnia-Herzegovina Convertible Mark"),
            Pair("BBD", "Barbadian Dollar"),
            Pair("BDT", "Bangladeshi Taka"),
            Pair("BGN", "Bulgarian Lev"),
            Pair("BHD", "Bahraini Dinar"),
            Pair("BIF", "Burundian Franc"),
            Pair("BMD", "Bermudan Dollar"),
            Pair("BND", "Brunei Dollar"),
            Pair("BOB", "Bolivian Boliviano"),
            Pair("BRL", "Brazilian Real"),
            Pair("BSD", "Bahamian Dollar"),
            Pair("BTC", "Bitcoin"),
            Pair("BTN", "Bhutanese Ngultrum"),
            Pair("BWP", "Botswanan Pula"),
            Pair("BYN", "New Belarusian Ruble"),
            Pair("BYR", "Belarusian Ruble"),
            Pair("BZD", "Belize Dollar"),
            Pair("CAD", "Canadian Dollar"),
            Pair("CDF", "Congolese Franc"),
            Pair("CHF", "Swiss Franc"),
            Pair("CLF", "Chilean Unit of Account (UF)"),
            Pair("CLP", "Chilean Peso"),
            Pair("CNY", "Chinese Yuan"),
            Pair("COP", "Colombian Peso"),
            Pair("CRC", "Costa Rican Colón"),
            Pair("CUC", "Cuban Convertible Peso"),
            Pair("CUP", "Cuban Peso"),
            Pair("CVE", "Cape Verdean Escudo"),
            Pair("CZK", "Czech Republic Koruna"),
            Pair("DJF", "Djiboutian Franc"),
            Pair("DKK", "Danish Krone"),
            Pair("DOP", "Dominican Peso"),
            Pair("DZD", "Algerian Dinar"),
            Pair("EGP", "Egyptian Pound"),
            Pair("ERN", "Eritrean Nakfa"),
            Pair("ETB", "Ethiopian Birr"),
            Pair("EUR", "Euro"),
            Pair("FJD", "Fijian Dollar"),
            Pair("FKP", "Falkland Islands Pound"),
            Pair("GBP", "British Pound Sterling"),
            Pair("GEL", "Georgian Lari"),
            Pair("GGP", "Guernsey Pound"),
            Pair("GHS", "Ghanaian Cedi"),
            Pair("GIP", "Gibraltar Pound"),
            Pair("GMD", "Gambian Dalasi"),
            Pair("GNF", "Guinean Franc"),
            Pair("GTQ", "Guatemalan Quetzal"),
            Pair("GYD", "Guyanaese Dollar"),
            Pair("HKD", "Hong Kong Dollar"),
            Pair("HNL", "Honduran Lempira"),
            Pair("HRK", "Croatian Kuna"),
            Pair("HTG", "Haitian Gourde"),
            Pair("HUF", "Hungarian Forint"),
            Pair("IDR", "Indonesian Rupiah"),
            Pair("ILS", "Israeli New Sheqel"),
            Pair("IMP", "Manx pound"),
            Pair("INR", "Indian Rupee"),
            Pair("IQD", "Iraqi Dinar"),
            Pair("IRR", "Iranian Rial"),
            Pair("ISK", "Icelandic Króna"),
            Pair("JEP", "Jersey Pound"),
            Pair("JMD", "Jamaican Dollar"),
            Pair("JOD", "Jordanian Dinar"),
            Pair("JPY", "Japanese Yen"),
            Pair("KES", "Kenyan Shilling"),
            Pair("KGS", "Kyrgystani Som"),
            Pair("KHR", "Cambodian Riel"),
            Pair("KMF", "Comorian Franc"),
            Pair("KPW", "North Korean Won"),
            Pair("KRW", "South Korean Won"),
            Pair("KWD", "Kuwaiti Dinar"),
            Pair("KYD", "Cayman Islands Dollar"),
            Pair("KZT", "Kazakhstani Tenge"),
            Pair("LAK", "Laotian Kip"),
            Pair("LBP", "Lebanese Pound"),
            Pair("LKR", "Sri Lankan Rupee"),
            Pair("LRD", "Liberian Dollar"),
            Pair("LSL", "Lesotho Loti"),
            Pair("LTL", "Lithuanian Litas"),
            Pair("LVL", "Latvian Lats"),
            Pair("LYD", "Libyan Dinar"),
            Pair("MAD", "Moroccan Dirham"),
            Pair("MDL", "Moldovan Leu"),
            Pair("MGA", "Malagasy Ariary"),
            Pair("MKD", "Macedonian Denar"),
            Pair("MMK", "Myanma Kyat"),
            Pair("MNT", "Mongolian Tugrik"),
            Pair("MOP", "Macanese Pataca"),
            Pair("MRO", "Mauritanian Ouguiya"),
            Pair("MUR", "Mauritian Rupee"),
            Pair("MVR", "Maldivian Rufiyaa"),
            Pair("MWK", "Malawian Kwacha"),
            Pair("MXN", "Mexican Peso"),
            Pair("MYR", "Malaysian Ringgit"),
            Pair("MZN", "Mozambican Metical"),
            Pair("NAD", "Namibian Dollar"),
            Pair("NGN", "Nigerian Naira"),
            Pair("NIO", "Nicaraguan Córdoba"),
            Pair("NOK", "Norwegian Krone"),
            Pair("NPR", "Nepalese Rupee"),
            Pair("NZD", "New Zealand Dollar"),
            Pair("OMR", "Omani Rial"),
            Pair("PAB", "Panamanian Balboa"),
            Pair("PEN", "Peruvian Nuevo Sol"),
            Pair("PGK", "Papua New Guinean Kina"),
            Pair("PHP", "Philippine Peso"),
            Pair("PKR", "Pakistani Rupee"),
            Pair("PLN", "Polish Zloty"),
            Pair("PYG", "Paraguayan Guarani"),
            Pair("QAR", "Qatari Rial"),
            Pair("RON", "Romanian Leu"),
            Pair("RSD", "Serbian Dinar"),
            Pair("RUB", "Russian Ruble"),
            Pair("RWF", "Rwandan Franc"),
            Pair("SAR", "Saudi Riyal"),
            Pair("SBD", "Solomon Islands Dollar"),
            Pair("SCR", "Seychellois Rupee"),
            Pair("SDG", "Sudanese Pound"),
            Pair("SEK", "Swedish Krona"),
            Pair("SGD", "Singapore Dollar"),
            Pair("SHP", "Saint Helena Pound"),
            Pair("SLL", "Sierra Leonean Leone"),
            Pair("SOS", "Somali Shilling"),
            Pair("SRD", "Surinamese Dollar"),
            Pair("STD", "São Tomé and Príncipe Dobra"),
            Pair("SVC", "Salvadoran Colón"),
            Pair("SYP", "Syrian Pound"),
            Pair("SZL", "Swazi Lilangeni"),
            Pair("THB", "Thai Baht"),
            Pair("TJS", "Tajikistani Somoni"),
            Pair("TMT", "Turkmenistani Manat"),
            Pair("TND", "Tunisian Dinar"),
            Pair("TOP", "Tongan Paʻanga"),
            Pair("TRY", "Turkish Lira"),
            Pair("TTD", "Trinidad and Tobago Dollar"),
            Pair("TWD", "New Taiwan Dollar"),
            Pair("TZS", "Tanzanian Shilling"),
            Pair("UAH", "Ukrainian Hryvnia"),
            Pair("UGX", "Ugandan Shilling"),
            Pair("USD", "United States Dollar"),
            Pair("UYU", "Uruguayan Peso"),
            Pair("UZS", "Uzbekistan Som"),
            Pair("VEF", "Venezuelan Bolívar Fuerte"),
            Pair("VND", "Vietnamese Dong"),
            Pair("VUV", "Vanuatu Vatu"),
            Pair("WST", "Samoan Tala"),
            Pair("XAF", "CFA Franc BEAC"),
            Pair("XAG", "Silver (troy ounce)"),
            Pair("XAU", "Gold (troy ounce)"),
            Pair("XCD", "East Caribbean Dollar"),
            Pair("XDR", "Special Drawing Rights"),
            Pair("XOF", "CFA Franc BCEAO"),
            Pair("XPF", "CFP Franc"),
            Pair("YER", "Yemeni Rial"),
            Pair("ZAR", "South African Rand"),
            Pair("ZMK", "Zambian Kwacha (pre-2013)"),
            Pair("ZMW", "Zambian Kwacha"),
            Pair("ZWL", "Zimbabwean Dollar"),
        )
    }

}

internal fun MockWebServer.enqueueResponse(
    fileName: String,
    code: Int,
) {
    val inputStream =
        javaClass.classLoader?.getResourceAsStream(fileName)

    inputStream?.use {
        val source = inputStream.source().buffer()
        source.use {
            enqueue(
                MockResponse()
                    .setResponseCode(code)
                    .setBody(source.readString(StandardCharsets.UTF_8))
            )
        }
    }
}
