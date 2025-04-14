package com.amaurypm.videogamesrf.data

import com.amaurypm.videogamesrf.data.remote.GamesApi
import com.amaurypm.videogamesrf.data.remote.model.GameDetailDto
import com.amaurypm.videogamesrf.data.remote.model.GameDto
import com.amaurypm.videogamesrf.data.remote.model.GamesDto
import retrofit2.Retrofit

class GameRepository(
   private val retrofit: Retrofit
)
{
    //Creamos nuestra instancia al api para acceder a los endpoints
    private val gameApi = retrofit.create(GamesApi::class.java)

    suspend fun getGames(url: String?): List<GameDto> = gameApi.getGames(url)

    suspend fun getGames(): List<GameDto> = gameApi.getGames()

    suspend fun getGameDetail(id: String?): GameDetailDto = gameApi.getGameDetail(id)

    suspend fun getGamesApiary(): List<GameDto> = gameApi.getGamesApiary()

    suspend fun getGamesDetailApiary(user: String?): GameDetailDto = gameApi.getGameDetailApiary(user)

    suspend fun getGamesSecure(url: String?): GamesDto = gameApi.getGamesSecure(url)

}