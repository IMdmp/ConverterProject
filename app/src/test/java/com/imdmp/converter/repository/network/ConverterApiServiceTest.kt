package com.imdmp.converter.repository.network

import com.imdmp.converter.repository.network.ConverterService
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
        .build()
        .create(ConverterService::class.java)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `pullLatestRates correctly gives 200 response`() {
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

}

internal fun MockWebServer.enqueueResponse(
    fileName: String,
    code: Int,
) {
    val inputStream =
        javaClass.classLoader?.getResourceAsStream("$fileName")

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
