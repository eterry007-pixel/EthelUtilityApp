package com.example.ethelutilityapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ethelutilityapp.model.CurrencyViewModel
import com.example.ethelutilityapp.model.SettingsViewModel

@Composable
fun EthelUtilityApp(
    settingsViewModel: SettingsViewModel = viewModel(),
    currencyViewModel: CurrencyViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf("Currency") }
    val backgroundColor by settingsViewModel.backgroundColor.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Sync, contentDescription = "Currency") },
                    label = { Text("Currency") },
                    selected = selectedTab == "Currency",
                    onClick = { selectedTab = "Currency" }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == "Settings",
                    onClick = { selectedTab = "Settings" }
                )
            }
        },
        containerColor = Color(backgroundColor)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(backgroundColor))
        ) {
            when (selectedTab) {
                "Currency" -> CurrencyConverterScreen(currencyViewModel)
                "Settings" -> SettingsScreen(settingsViewModel)
            }
        }
    }
}

@Composable
fun CurrencyConverterScreen(viewModel: CurrencyViewModel) {
    val rates by viewModel.rates.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var amount by remember { mutableStateOf("1") }
    val currencies = listOf("USD", "EUR", "GBP", "AUD", "JPY", "CNY")
    var baseCurrency by remember { mutableStateOf("USD") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Currency Converter", style = MaterialTheme.typography.headlineMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) amount = it },
                label = { Text("Amount") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            Box {
                Button(onClick = { expanded = true }) {
                    Text(baseCurrency)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(currency) },
                            onClick = {
                                baseCurrency = currency
                                viewModel.fetchRates(currency)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator()
        }

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(rates.toList()) { (currency, rate) ->
                val convertedAmount = (amount.toDoubleOrNull() ?: 0.0) * rate
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(currency, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            String.format("%.2f", convertedAmount),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val backgroundColor by viewModel.backgroundColor.collectAsState()
    val isMusicEnabled by viewModel.isMusicEnabled.collectAsState()
    val language by viewModel.language.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        Text("Background Color", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ColorOption(Color.White, backgroundColor, viewModel)
            ColorOption(Color(0xFFFFEBEE), backgroundColor, viewModel) // Light Red
            ColorOption(Color(0xFFE3F2FD), backgroundColor, viewModel) // Light Blue
            ColorOption(Color(0xFFE8F5E9), backgroundColor, viewModel) // Light Green
        }

        Divider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Background Music", style = MaterialTheme.typography.titleMedium)
                Text("Toggle sound on/off", style = MaterialTheme.typography.bodySmall)
            }
            Switch(
                checked = isMusicEnabled,
                onCheckedChange = { viewModel.toggleMusic(it) }
            )
        }

        Divider()

        Text("Language", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val languages = listOf("English", "Spanish", "French")
            languages.forEach { lang ->
                FilterChip(
                    selected = language == lang,
                    onClick = { viewModel.setLanguage(lang) },
                    label = { Text(lang) }
                )
            }
        }
    }
}

@Composable
fun ColorOption(color: Color, selectedColor: Long, viewModel: SettingsViewModel) {
    val isSelected = selectedColor == color.value.toLong()
    Button(
        onClick = { viewModel.setBackgroundColor(color.value.toLong()) },
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .size(48.dp)
            .then(if (isSelected) Modifier.background(Color.Black.copy(alpha = 0.1f)) else Modifier),
        border = if (isSelected) ButtonDefaults.outlinedButtonBorder else null,
        contentPadding = PaddingValues(0.dp)
    ) {
        if (isSelected) {
            Icon(Icons.Default.Sync, contentDescription = null, tint = Color.Gray)
        }
    }
}
