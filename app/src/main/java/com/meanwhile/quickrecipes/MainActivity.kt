package com.meanwhile.quickrecipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.meanwhile.quickrecipes.domain.model.Address
import com.meanwhile.quickrecipes.domain.model.Badge
import com.meanwhile.quickrecipes.ui.AltMainViewModel
import com.meanwhile.quickrecipes.ui.MainViewModel
import com.meanwhile.quickrecipes.ui.UiState
import com.meanwhile.quickrecipes.ui.theme.QuickRecipesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //private val viewModel by viewModels<MainViewModel>()
    private val viewModel by viewModels<AltMainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuickRecipesTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    QuickRecipesMainScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun QuickRecipesMainScreen(viewModel: AltMainViewModel) {
    val state = viewModel.uiState.collectAsState().value

    QuickRecipesMainScreenContent(
        uiState = state,
        onLoginClick = { viewModel.onLoginClicked() },
        onLogoutClick = { viewModel.onLogoutClicked() }
    )
}

@Composable
fun QuickRecipesMainScreenContent(
    uiState: UiState,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        uiState.userAddress?.let { address ->
            Text(text = address.street + address.zipCode)
        }

        uiState.userBadges?.let{
            it.forEach { badge ->
                Text(text = "Badge $badge")
            }
        }

        Button(onClick = onLoginClick) {
            Text(text = "Login")
        }

        Button(onClick = onLogoutClick) {
            Text(text = "Logout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultLoggedOutPreview() {
    QuickRecipesTheme {
        QuickRecipesMainScreenContent(UiState(), {},{})
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultLoggedInPreview() {
    QuickRecipesTheme {
        val state = UiState(
            userAddress = Address("street", "0001"),
            userBadges = listOf(Badge("gold"), Badge("silver"))
        )
        QuickRecipesMainScreenContent(state, {},{})
    }
}