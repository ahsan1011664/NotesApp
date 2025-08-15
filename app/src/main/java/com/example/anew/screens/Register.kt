package com.example.anew.screens
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.anew.data.AppDatabase
import com.example.anew.Repository.Repository
import com.example.anew.viewmodel.AuthViewModel
import com.example.anew.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.launch


fun isValidemail(email: String): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun validatepassword(password: String) : Pair<Boolean, List<String>> {
val errors = mutableListOf<String>()
    if (password.length < 8) {
        errors.add("Password must be at least 8 characters long")
    }
    if (!password.any { it.isUpperCase() }) {
        errors.add("Password must contain at least one uppercase letter")
    }
    if (!password.any { it.isLowerCase() }) {
        errors.add("Password must contain at least one lowercase letter")
    }
    if (!password.any { it.isDigit() }) {
        errors.add("Password must contain at least one digit")
    }
return Pair(errors.isEmpty(), errors)
}
@Composable
fun RegisterScreen(navHostController: NavHostController) {
    val context = LocalContext.current

    // Set up ViewModel manually with factory
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDao()
    val repository = Repository(userDao)
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repository))

    // UI states
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isemailvalid = remember(email) { isValidemail(email) }
    val (isPasswordvalid, errors) = remember(password) { validatepassword(password) }

    val message = viewModel.registration.value
    val scope = rememberCoroutineScope()
    LaunchedEffect(message) {
        if (message == "Registration successful") {
            navHostController.navigate( "login?email=${Uri.encode(email.trim())}&password=${Uri.encode(password)}") {
                popUpTo("register") { inclusive = true } // remove register from backstack
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Register", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = email.isNotBlank()&& !isemailvalid,
            modifier = Modifier.fillMaxWidth()
        )
        if (email.isNotBlank()&& !isemailvalid){
            Text(
                text = "Invalid email",
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            isError = password.isNotBlank() && !isPasswordvalid, // correct prop
            modifier = Modifier.fillMaxWidth()
        )
          if(password.isNotBlank() && !isPasswordvalid){
              Text(
                  text = "Invalid password",
                  color = MaterialTheme.colorScheme.error
              )
          }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    viewModel.registerUser(username, password, email)
                }
            },
            enabled = isemailvalid && username.isNotBlank() && isPasswordvalid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navHostController.navigate("login")
            },
        ){
            Text("Login")
        }

        message?.let {
            Text(
                text = it,
                color = if (it == "Registration successful") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}