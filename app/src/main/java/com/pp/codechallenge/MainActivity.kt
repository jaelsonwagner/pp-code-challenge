package com.pp.codechallenge

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pp.codechallenge.ui.LoginViewModel
import com.pp.codechallenge.ui.UiState
import com.pp.codechallenge.ui.theme.PPCodeChallengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }

        setContent {
            val uiState = viewModel.uiState.collectAsState(UiState())
            PPCodeChallengeTheme {
                LoginScreen(
                    uiState.value, modifier = Modifier.padding(16.dp)
                ) { userName, password ->
                    viewModel.authenticate(userName, password)
                }
            }
        }
    }

    @Composable
    fun LoginScreen(
        uiState: UiState, modifier: Modifier = Modifier, callback: (String, String) -> Unit
    ) {
        var userName by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        if (uiState.success) {
            LocalFocusManager.current.clearFocus(true)
            userName = ""
            password = ""
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
        }

        if (uiState.error != null) {
            Toast.makeText(this, uiState.error, Toast.LENGTH_SHORT).show()
        }

        val keyboardController = LocalSoftwareKeyboardController.current

        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp)
                )
            } else {
                Column(modifier = modifier.fillMaxSize()) {
                    TextField(value = userName,
                        label = { Text("User Name") },
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { userName = it })

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = password,
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { password = it },
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Button(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp), onClick = {
                        keyboardController?.hide()
                        callback(userName, password)
                    }) {
                        Text(text = "Login")
                    }
                }
            }
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun LoginScreenPreview() {
        PPCodeChallengeTheme {
            LoginScreen(
                uiState = UiState(), modifier = Modifier.padding(16.dp)
            ) { userName, password ->
            }
        }
    }
}