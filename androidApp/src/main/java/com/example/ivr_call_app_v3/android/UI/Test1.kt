package com.example.ivr_call_app_v3.android.UI

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.ivr_call_app_v3.android.FunctionalComponents.NetworkCalls.API_ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.kotlinx.serializer.KotlinxSerializer
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import androidx.lifecycle.viewmodel.compose.viewModel

@Serializable
data class TestMessage(
    val message: String
)

interface Services {
    suspend fun testconnection(): TestMessage?

    companion object {
        fun create() : Services {
            return APIInterfaceImpl(
                client = HttpClient(Android){
                    install(Logging){
                        level = LogLevel.ALL
                        logger = Logger.DEFAULT
                    }
                    install(ContentNegotiation) {
                        json(Json { ignoreUnknownKeys = true }) // Configure the JSON serializer
                    }
                }
            )
        }
    }
}

//           "http://172.31.20.220:5000/api/testconnection"
class APIInterfaceImpl(private val client: HttpClient) : Services {
    override suspend fun testconnection(): TestMessage? {
        return try {

            client.get(urlString = "http://172.31.20.220:5000/api/testconnection").body<TestMessage>()

        } catch (e: RedirectResponseException) { // 3xx responses
            Log.e("TAG1", e.message ?: "Redirect error")
            null
        } catch (e: ClientRequestException) { // 4xx responses
            Log.e("TAG2", e.message ?: "Client request error")
            null
        } catch (e: ServerResponseException) { // 5xx responses
            Log.e("TAG3", e.message ?: "Server error")
            null
        } catch (e: Exception) { // Handle any other exceptions
            Log.e("TAG4", "Unexpected error: ${e.message}")
            null
        }
    }
}

@Composable
fun Test1() {

    var ApiViewmodel : API_ViewModel = viewModel()

    LaunchedEffect(Unit) {
        ApiViewmodel.initialize("172.31.20.220-")
    }

    val retromessage by ApiViewmodel.testconnection.collectAsState()
    val error by ApiViewmodel.errorMessage.collectAsState()
    // State variables
    var mymessage by remember { mutableStateOf("No message") }
    var isLoading by remember { mutableStateOf(false) }
    var triggerFetch by remember { mutableStateOf(false) }
//
//    // Create an instance of the service
//    val service = Services.create()
//
//    // This LaunchedEffect runs when triggerFetch changes
//    LaunchedEffect(triggerFetch) {
//        if (triggerFetch) {
//            isLoading = true
//            val result = service.testconnection()
//            mymessage = result?.message ?: "Failed to fetch message"
//            isLoading = false
//        }
//    }
//
    Column {
        Button(onClick = {
//            triggerFetch = true
            ApiViewmodel.TestConnectionWithBackend()
        }) {
            Text(text = "Click Me")
        }

//        if (isLoading) {
//            Text(text = "Loading...")
//        } else {
//            Text(text = mymessage)
//        }

        retromessage?.let {
            Text(text = "Message: ${it.message}", color = Color.Blue)
        }

        error?.let {
            Text(text = "Error: $it", color = Color.Red)
        }
    }
}

