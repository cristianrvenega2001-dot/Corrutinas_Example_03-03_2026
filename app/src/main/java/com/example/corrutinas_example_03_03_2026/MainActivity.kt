package com.example.corrutinas_example_03_03_2026

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.corrutinas_example_03_03_2026.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var progressBarStatus = 0
    private var isDownloading = false // Variable para bloquear el botón

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Evento del botón de iniciar descarga
        binding.btnStart.setOnClickListener {
            // Solo iniciamos si no hay una descarga ya en proceso
            if (!isDownloading) {
                startDownload()
            }
        }
    }

    private fun startDownload() {
        // 1. Configuraciones iniciales (Bloqueo de UI)
        isDownloading = true
        binding.btnStart.isEnabled = false // Se bloquea el botón según los requerimientos
        progressBarStatus = 0
        binding.progressBar.progress = 0
        binding.txtComplete.text = ""
        binding.txtProgress.text = getString(R.string.downloading_progress, 0)

        // 2. Creamos el Handler asociado al hilo principal (MainLooper)
        val handler = Handler(Looper.getMainLooper())

        // 3. Simulamos la descarga en un Hilo separado con "Thread"
        Thread {
            while (progressBarStatus < 100) {
                // Avanzamos de 10% en 10%
                progressBarStatus += 10

                // Aseguramos que no se pase del 100%
                if (progressBarStatus > 100) {
                    progressBarStatus = 100
                }

                // Pausa obligatoria del ejercicio: 1 segundo (1000ms)
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                // 4. Actualizamos la Interfaz usando el Handler (Hilo principal)
                handler.post {
                    binding.progressBar.progress = progressBarStatus
                    binding.txtProgress.text = getString(R.string.downloading_progress, progressBarStatus)
                }
            }

            // 5. Al finalizar (llega al 100%) mostramos el mensaje de éxito
            handler.post {
                binding.txtComplete.text = getString(R.string.download_completed)
                binding.btnStart.isEnabled = true // Reactivamos el botón
                isDownloading = false
            }
        }.start()
    }
}
