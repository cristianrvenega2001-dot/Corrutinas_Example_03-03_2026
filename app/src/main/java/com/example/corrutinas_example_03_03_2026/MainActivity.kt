package com.example.corrutinas_example_03_03_2026

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.corrutinas_example_03_03_2026.databinding.ActivityMainBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private lateinit var  binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStart.setOnClickListener {
        starDownload()
      }

        }


    // Variable que guarda la referencia de la coroutine activa
    // Nos permite cancelarla si es necesario

    private var downloadJob : Job? = null


    private fun starDownload() {

        // Si ya hay una descarga ejecutándose, la cancelamos
         downloadJob?.cancel()

        downloadJob = lifecycleScope.launch (Dispatchers.Main){

            try {
                // Deshabilitamos el botón para evitar múltiples descargas

                   binding.btnStart.isEnabled= false
                // Limpiamos mensaje previo
                    binding.txtComplete.text =""
                    binding.txtProgress.text = "Preparando descargar"


                // Simulamos trabajo pesado (como red o base de datos)
                // Cambiamos al Dispatcher.IO para no usar el hilo principal

                withContext(Dispatchers.IO){

                    for(i in 1 ..100) {

                        // delay() NO bloquea el hilo
                        // Suspende la coroutine y libera el hilo

                        delay(300)

                        // Volvemos al hilo principal para actualizar la UI
                        withContext(Dispatchers.Main) {
                            binding.progressBar.progress = i
                            binding.txtProgress.text = "Descargando $i%"
                        }
                    }
                }

                // Cuando termina correctamente
                binding.txtComplete.text ="¡Descarga Completada!"

            }catch ( e: CancellationException){

                // Se ejecuta si la coroutine fue cancelada manualmente
                binding.txtComplete.text ="Error en la descargar"

            }finally {

                // Siempre se ejecuta, haya éxito o error
                // Reactivamos el botón
                binding.btnStart.isEnabled = true
            }



        }




    }














    }
