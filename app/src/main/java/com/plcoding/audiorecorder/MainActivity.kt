package com.plcoding.audiorecorder

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.plcoding.audiorecorder.playback.AndroidAudioPlayer
import com.plcoding.audiorecorder.record.AndroidAudioRecorder
import com.plcoding.audiorecorder.ui.theme.AudioRecorderTheme
import java.io.File

class MainActivity : ComponentActivity() {

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }
    private var isInitializationComplete = false
    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { !isInitializationComplete }

        // Simulate app initialization or load resources here
        // For example, using a Handler to simulate a delay for initialization
        Handler(Looper.getMainLooper()).postDelayed({
            // Once initialization is complete, update the flag to allow the splash screen to hide
            isInitializationComplete = true

            // Now set the content for your main activity
            setContent {
                AudioRecorderTheme {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            File(cacheDir, "audio.mp3").also {
                                recorder.start(it)
                                audioFile = it
                            }
                        }) {
                            Text(text = "Start recording")
                        }
                        Button(onClick = {
                            recorder.stop()
                        }) {
                            Text(text = "Stop recording")
                        }
                        Button(onClick = {
                            player.playFile(audioFile ?: return@Button)
                        }) {
                            Text(text = "Play")
                        }
                        Button(onClick = {
                            player.stop()
                        }) {
                            Text(text = "Stop playing")
                        }
                    }
                }
            }
        }, 1700)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )

    }
}