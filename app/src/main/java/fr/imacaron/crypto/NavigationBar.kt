package fr.imacaron.crypto

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.HMobiledata
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController

enum class Page(val route: String) {
    Euler("euler"),
    Matrix("Matrix"),
    Affine("Affine"),
    Hill("Hill");
}

@Composable
fun NavigationBar(navHost: NavHostController) {
    BottomAppBar {
        NavigationBarItem(
            selected = false,
            onClick = { navHost.navigate(Page.Euler.route) },
            label = { Text("Euclide") },
            icon = { Icon(Icons.Default.Functions, "Bouton pour naviguer vers la page Euclide") })
        NavigationBarItem(
            selected = false,
            onClick = { navHost.navigate(Page.Matrix.route) },
            label = { Text("Matrix") },
            icon = { Icon(Icons.Default.DataArray, "Bouton pour naviguer vers la page Hill") })
        NavigationBarItem(
            selected = false,
            onClick = { navHost.navigate(Page.Affine.route) },
            label = { Text("Affine") },
            icon = { Icon(painterResource(id = R.drawable.function), "Bouton pour naviguer vers la page Affine") })
        NavigationBarItem(
            selected = false,
            onClick = { navHost.navigate(Page.Hill.route) },
            label = { Text("Hill") },
            icon = { Icon(Icons.Default.HMobiledata, "Bouton pour naviguer vers la page Affine") })
    }
}