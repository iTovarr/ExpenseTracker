package com.example.controldegastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.controldegastos.ui.theme.ControlDeGastosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControlDeGastosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExpenseTrackerApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ExpenseTrackerApp(modifier: Modifier = Modifier) {
    var totalAmount by rememberSaveable { mutableStateOf(0.0) }
    var expenseName by rememberSaveable { mutableStateOf("") }
    var expenseValue by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Control de Gastos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Total Gastado", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = "$${String.format("%.2f", totalAmount)}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Inputs
        OutlinedTextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text("Concepto (ej. Almuerzo)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = expenseValue,
            onValueChange = { expenseValue = it },
            label = { Text("Monto ($)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val value = expenseValue.toDoubleOrNull() ?: 0.0
                if (expenseName.isNotBlank() && value > 0) {
                    totalAmount += value
                    expenseName = ""
                    expenseValue = ""
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Añadir Gasto")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseTrackerPreview() {
    ControlDeGastosTheme {
        ExpenseTrackerApp()
    }
}