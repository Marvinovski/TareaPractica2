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
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.amaurypm.videogamesrf.R
import com.amaurypm.videogamesrf.application.VideogamesRFApp
import com.amaurypm.videogamesrf.data.GameRepository
import com.amaurypm.videogamesrf.databinding.FragmentGameDetailBinding
import com.amaurypm.videogamesrf.utils.Constants
import com.amaurypm.videogamesrf.utils.isAtLeastAndroid
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch


private const val ARG_GAMEID = "user"

private var latitud: String? = null
private var longitud: String? = null
private var title: String? = null
private var snippet: String? = null


class GameDetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

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

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        repository = (requireActivity().application as VideogamesRFApp).repository

        //Log.d(Constants.LOGTAG, "Id: $gameId")
        lifecycleScope.launch {

            try {
                 //val gameDetail = repository.getGameDetail(gameId)
               val gameDetail = repository.getGamesDetailApiary(gameId)


                binding.apply {

                    tvTitle.text = gameDetail.name

                    //Glide.with(requireActivity())
                     //   .load(gameDetail.image)
                        //.into(ivImage)

                    ytVideo.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            super.onReady(youTubePlayer)

                            youTubePlayer.loadVideo(gameDetail.url_video.toString(), 0f )
                        }

                        override fun onError(
                            youTubePlayer: YouTubePlayer,
                            error: PlayerConstants.PlayerError
                        ) {
                            super.onError(youTubePlayer, error)
                            youTubePlayer.loadVideo(gameDetail.url_video.toString(), 0f )
                        }
                    })


                    tvEmail.text = (getString(R.string.email, gameDetail.email))
                    tvTelephone.text = (getString(R.string.telephone, gameDetail.telephone))
                    tvDateReservation.text = (getString(R.string.date_reservation, gameDetail.dateReservation))
                    tvTimeReservation.text = (getString(R.string.time_reservation, gameDetail.timeReservation))


                    latitud = gameDetail.lat
                    longitud = gameDetail.lon
                    title = gameDetail.title
                    snippet = gameDetail.snippet


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

    private fun createMarker(lat: Double, lon: Double, title: String, snippet: String){
        val coordinates = LatLng(lat, lon)
        val  marker = MarkerOptions()
            .position(coordinates)
            .title(title)
            .snippet(snippet)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ball))

        googleMap.addMarker(marker)
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 15f),
            4_000,
            null
        )

     }

    override fun onMapReady(map: GoogleMap) {

        googleMap = map

        createMarker(
            latitud?.toDouble() ?: 0.0,
            longitud?.toDouble() ?: 0.0,
            title.toString(),
            snippet.toString()
        )

    }


}