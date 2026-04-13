package com.example.controldegastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.controldegastos.ui.theme.ControlDeGastosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControlDeGastosTheme {
                MainAppContainer()
            }
        }
    }
}

@Composable
fun MainAppContainer() {
    var currentScreen by rememberSaveable { mutableStateOf("registro") }
    val expenseList = remember { mutableStateListOf<Pair<String, Double>>() }
    val totalAmount = expenseList.sumOf { it.second }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (currentScreen == "registro") {
            ExpenseTrackerScreen(
                modifier = Modifier.padding(innerPadding),
                total = totalAmount,
                onAddExpense = { name, value -> expenseList.add(name to value) },
                onNavigateToHistory = { currentScreen = "historial" }
            )
        } else {
            HistoryScreen(
                modifier = Modifier.padding(innerPadding),
                expenses = expenseList,
                onBack = { currentScreen = "registro" }
            )
        }
    }
}

@Composable
fun ExpenseTrackerScreen(
    modifier: Modifier,
    total: Double,
    onAddExpense: (String, Double) -> Unit,
    onNavigateToHistory: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var valueStr by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Wallet Monitor", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(20.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Balance Total", color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                Text("$${String.format("%.2f", total)}", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("¿En qué gastaste?") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = valueStr, onValueChange = { valueStr = it }, label = { Text("Monto ($)") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val v = valueStr.toDoubleOrNull() ?: 0.0
                if (name.isNotBlank() && v > 0) {
                    onAddExpense(name, v)
                    name = ""; valueStr = ""
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) { Text("GUARDAR GASTO") }

        TextButton(onClick = onNavigateToHistory, modifier = Modifier.padding(top = 16.dp)) {
            Icon(Icons.Default.List, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("VER HISTORIAL DE GASTOS")
        }
    }
}

@Composable
fun HistoryScreen(modifier: Modifier, expenses: List<Pair<String, Double>>, onBack: () -> Unit) {
    Column(modifier = modifier.fillMaxSize().padding(24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
            Text("Historial", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (expenses.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay gastos registrados aún.", color = Color.Gray)
            }
        } else {
            LazyColumn {
                items(expenses) { (name, amount) ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(name, fontWeight = FontWeight.Medium)
                            Text("-$${String.format("%.2f", amount)}", color = Color.Red, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}