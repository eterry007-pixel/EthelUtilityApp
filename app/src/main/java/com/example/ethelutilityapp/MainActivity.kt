package com.example.ethelutilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ethelutilityapp.ui.theme.EthelUtilityAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

interface QuoteApi {
    @GET("api/1.0/?method=getQuote&lang=en&format=text")
    ////suspend fun: asynchronous code is called within a coroutine or another suspend function.
    suspend fun getQuote(): String
}

//creates a single, static instance (a singleton)
object RetrofitInstance {
    val api: QuoteApi by lazy {//defers the initialization of the variable until it is first accessed.
        Retrofit.Builder()
            .baseUrl("http://api.forismatic.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(QuoteApi::class.java)
    }
}

//ViewModel: store and manage UI-related data that survives configuration changes, such as screen rotations
class QuoteViewModel : ViewModel() {
    //MutableStateFlow: manage state changes in a reactive way.
    private val _quote = MutableStateFlow("Click button to load quote")
    val quote: StateFlow<String> = _quote //StateFlow: read-only version of MutableStateFlow.
    fun loadQuote() {
        //viewModelScope.launch is to start a Kotlin coroutine within a ViewModel
        // that is automatically canceled when the ViewModel is destroyed (when its onCleared() method is called).
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getQuote()
                _quote.value = result
            } catch (e: Exception) {
                _quote.value = "Error: ${e.message}"
            }
        }
    }
}

class CounterViewModel : ViewModel() {
     private val _count = MutableStateFlow(0)
     val count: StateFlow<Int> = _count
     fun increment() {
         _count.value++
         }
    }
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EthelUtilityAppTheme {
                EthelUtilityApp()
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun EthelUtilityAppPreview() {
    EthelUtilityAppTheme {
        EthelUtilityApp()
    }
}
@Composable
fun EthelUtilityApp() {
    var selectedTab by remember { mutableStateOf("Utility") }
    //var counter = 0

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Utility") },
                    label = { Text("Utility") },
                    selected = selectedTab == "Utility",
                    onClick = { selectedTab = "Utility" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == "Settings",
                    onClick = { selectedTab = "Settings" }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "Utility" -> UtilityScreen()
                "Settings" -> SettingsScreen()
            }
        }
    }
}
@Composable
fun UtilityScreen() {
    //var counter by remember { mutableIntStateOf(0) }
    //val vm: CounterViewModel = viewModel()
    //val counter by vm.count.collectAsState()
    val vm: QuoteViewModel = viewModel()
    val quote by vm.quote.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Utility Screen", style = MaterialTheme.typography.headlineMedium)
        //Text("Counter: $counter", style = MaterialTheme.typography.bodyLarge)

       // Button(onClick = { counter++ }) {
            Button(onClick = { vm.loadQuote()}) {
            //Button(onClick = { vm.increment() }) {
               // Text("Increment")
                Text("Get Quote")
            }
        Text(quote, style = MaterialTheme.typography.bodyLarge)
    }
}
        //}
    //}
//}
@Composable
fun SettingsScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp), Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings Screen", style = MaterialTheme.typography.headlineMedium)
        Text("This is where you can add toggles or preferences.")
    }
}