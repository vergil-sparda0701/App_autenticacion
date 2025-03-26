package com.example.autenticacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.autenticacion.ui.theme.AutenticacionTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AutenticacionTheme {

                Auth()
            }

        }
        //Setup
        setupAuth()

    }

    //metodos privados
    private var canAuthenticate = false
    private lateinit var promptInfo : BiometricPrompt.PromptInfo

    private fun setupAuth(){
        if (BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK or
        BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS){
            canAuthenticate = true

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticacion biometrica")
                .setSubtitle("Autenticate utilizando el sensor biometrico")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build()
        }
    }

    private fun authenticate(auth: (auth: Boolean) -> Unit){
        if (canAuthenticate){
            BiometricPrompt(this, ContextCompat.getMainExecutor(this),
                object : BiometricPrompt.AuthenticationCallback(){
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        auth(true)
                    }
                }).authenticate(promptInfo)
        }else{
            auth(true)
        }
    }

    //Composables
    @Composable
    fun Auth() {

        var auth by remember { mutableStateOf(false) }

        Column (modifier = Modifier.background(if (auth) Color.Green else Color.Red)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(if (auth) "Autenticado" else "Necesitas autenticarte", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Nixon Gutierrez 2019-1306", fontSize = 16.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {if (auth){auth = false} else {authenticate { auth= it }} }) {
                Text(if(auth) "Cerrar" else "Autenticar", fontSize = 15.sp, fontWeight = FontWeight.Bold )
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun PreviewSystem(){
        Auth()
    }
}

