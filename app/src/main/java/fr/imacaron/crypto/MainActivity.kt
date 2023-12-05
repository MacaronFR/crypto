package fr.imacaron.crypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.imacaron.crypto.ui.theme.CryptoTheme

@Composable
fun RowScope.TableCell(
    text: String
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, MaterialTheme.colorScheme.background)
            .weight(1f / 6)
            .padding(8.dp)
    )
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CryptoTheme {
                Scaffold(
                    bottomBar = { NavigationBar(navController) }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(navController = navController, startDestination = Page.Hill.route) {
                            composable(Page.Euler.route) { Euler() }
                            composable(Page.Hill.route) { Hill() }
                        }
                    }
                }
            }
        }
    }
}