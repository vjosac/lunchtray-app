package com.example.lunchtray

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.ui.OrderViewModel
import com.example.lunchtray.ui.StartOrderScreen

enum class LunchTrayScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Entree(title = R.string.choose_entree),
    SideDish(title = R.string.choose_side_dish),
    Accompaniment(title = R.string.choose_accompaniment),
    Checkout(title = R.string.order_checkout)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    @StringRes currentScreenTitle: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreenTitle)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun LunchTrayApp() {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = LunchTrayScreen.valueOf(
        backStackEntry?.destination?.route ?: LunchTrayScreen.Start.name
    )

    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
            LunchTrayAppBar(
                currentScreenTitle = currentScreen.title,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        
        NavHost(
            navController = navController,
            startDestination = LunchTrayScreen.Start.name
        ) {
            composable(route = LunchTrayScreen.Start.name) {
                StartOrderScreen(
                    onStartOrderButtonClicked = {
                        navController.navigate(LunchTrayScreen.Entree.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}