package fr.imacaron.crypto

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

enum class Page(val route: String) {
    Euler("euler"),
    Hill("Hill");
}

@Composable
fun NavigationBar(navHost: NavHostController) {
    BottomAppBar {
        NavigationBarItem(
            selected = false,
            onClick = { navHost.navigate(Page.Euler.route) },
            label = { Text("Euler") },
            icon = { Icon(Icons.Default.Functions, "Bouton pour naviguer vers la page Euler") })
        NavigationBarItem(
            selected = false,
            onClick = { navHost.navigate(Page.Hill.route) },
            label = { Text("Hill") },
            icon = { Icon(Icons.Default.DataArray, "Bouton pour naviguer vers la page Hill") })
    }
}