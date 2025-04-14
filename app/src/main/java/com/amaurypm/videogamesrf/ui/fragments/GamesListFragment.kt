package com.amaurypm.videogamesrf.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.application.VideogamesRFApp
import com.amaurypm.videogamesrf.data.GameRepository
import com.amaurypm.videogamesrf.databinding.FragmentGamesListBinding
import com.amaurypm.videogamesrf.ui.adapters.GamesAdapter
import com.amaurypm.videogamesrf.utils.Constants
import kotlinx.coroutines.launch


class GamesListFragment : Fragment() {

    private var _binding: FragmentGamesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: GameRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentGamesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as VideogamesRFApp).repository

        lifecycleScope.launch {

           try {
               //val games = repository.getGames()
                val games = repository.getGamesApiary()
               //val games = repository.getGames("games/secure/games_list.php")


             //val response = repository.getGamesSecure("games/secure/games_list.php")

               binding.rvGames.apply {
                   layoutManager = LinearLayoutManager(requireContext())
                   adapter = GamesAdapter(games){ selectedGame ->
                      // Log.d(Constants.LOGTAG, "Click en ${selectedGame.title}")
                       selectedGame.user?.let {  id ->
                           requireActivity().supportFragmentManager.beginTransaction()
                               .replace(
                                   R.id.fragment_container,
                                   GameDetailFragment.newInstance(id)
                               )
                               .addToBackStack(null)
                               .commit()
                       }

                   }
               }

                } catch(e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "Error en la conexión",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } finally {
                    binding.pbLoading.visibility = View.GONE
                }

            }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}