package com.zeroboss.scoring500.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.zeroboss.scoring500.presentation.screens.NavGraphs
import com.zeroboss.scoring500.ui.theme.Blue50
import com.zeroboss.scoring500.ui.theme.Scoring500Theme

class MainActivity : ComponentActivity() {

//    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        FirebaseApp.initializeApp(LocalContext.current)
//
//        mAuth = FirebaseAuth.getInstance()

        setContent {
            Scoring500Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Blue50
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }
}
