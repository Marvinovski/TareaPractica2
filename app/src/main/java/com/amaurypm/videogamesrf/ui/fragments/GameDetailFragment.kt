package com.amaurypm.videogamesrf.ui.fragments

import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.application.VideogamesRFApp
import com.amaurypm.videogamesrf.data.GameRepository
import com.amaurypm.videogamesrf.databinding.FragmentGameDetailBinding
import com.amaurypm.videogamesrf.utils.Constants
import com.amaurypm.videogamesrf.utils.isAtLeastAndroid
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

private const val ARG_GAMEID = "user"

class GameDetailFragment : Fragment() {

    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: GameRepository

    private var gameId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            gameId = args.getString(ARG_GAMEID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as VideogamesRFApp).repository

        //Log.d(Constants.LOGTAG, "Id: $gameId")
        lifecycleScope.launch {

            try {
                 //val gameDetail = repository.getGameDetail(gameId)
               val gameDetail = repository.getGamesDetailApiary(gameId)


                binding.apply {

                    tvTitle.text = gameDetail.name

                    Glide.with(requireActivity())
                        .load(gameDetail.image)
                        .into(ivImage)


                    tvEmail.text = (getString(R.string.email, gameDetail.email))
                    tvTelephone.text = (getString(R.string.telephone, gameDetail.telephone))
                    tvDateReservation.text = (getString(R.string.date_reservation, gameDetail.dateReservation))
                    tvTimeReservation.text = (getString(R.string.time_reservation, gameDetail.timeReservation))

                    var comparte = gameDetail.shareCourt.toString()
                    if(comparte == getString(R.string.yes)){
                        comparte = getString(R.string.si)
                    }else{
                        comparte = getString(R.string.no)
                    }

                    tvShareCourt.text = (getString(R.string.share_court,"$comparte"))

                    //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    //tvLongDesc.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD

                    isAtLeastAndroid(Build.VERSION_CODES.Q){
                        tvEmail.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
                    }
                }

            }
            catch (e: Exception){
                //
            }finally {
                binding.pbLoading.visibility = View.GONE
            }



        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        //Instancia al fragment
        @JvmStatic
        fun newInstance(id: String) =
            GameDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_GAMEID, id)
                }
            }
    }
}