package com.amaurypm.videogamesrf.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.data.remote.PokemonApi
import com.amaurypm.videogamesrf.databinding.ActivityPokemomBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class PokemomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemomBinding

    companion object{
        const val BASE_URL = "https://pokeapi.co"
        const val LOGTAG = "LOGSPOKEMON"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemomBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val pokemonApi = retrofit.create(PokemonApi::class.java)

        lifecycleScope.launch {
            try {

                val pokemonDetail = pokemonApi.getPokemonDetail("23")

                binding.tvPokemon.text =pokemonDetail.name.replaceFirstChar { char ->
                    char.uppercase()
                }

                Glide.with(this@PokemomActivity)
                    .load(pokemonDetail.sprites.other.officialArtwork.frontDefault)
                    .into(binding.ivPokemon)


            }catch (e:IOException){
                Toast.makeText(
                    this@PokemomActivity,
                    "No conexión",
                    Toast.LENGTH_SHORT
                ).show()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

    }
}