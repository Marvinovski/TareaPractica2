package com.amaurypm.videogamesrf.data.remote

import com.amaurypm.videogamesrf.data.remote.model.GameDetailDto
import com.amaurypm.videogamesrf.data.remote.model.GameDto
import com.amaurypm.videogamesrf.data.remote.model.GamesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

//Análogo al Dao en Room
interface GamesApi {

    //Aquí se definen los endpoints a consumir

    @GET
    suspend fun getGames(
        @Url
        url: String?
    ): List<GameDto>

    @GET("cm/games/games_list.php")
    suspend fun getGames(): List<GameDto>

    @GET("cm/games/game_detail.php")
    suspend fun getGameDetail(
        @Query("id")
        id: String?/*,
        @Query("name")
        name: String?*/
    ): GameDetailDto

    //Listado de juegos en apiary
    //@GET("games/games_list")
    @GET("users/users_list")
    suspend fun getGamesApiary(): List<GameDto>
    //Detalle de cada juego en apiary
    //@GET("games/game_detail/{id}")


    @GET("users/user_detail/{user}")
    suspend fun getGameDetailApiary(
        //@Path("id")
        @Path("user")
        user: String?
    ): GameDetailDto

    @GET
    suspend fun getGamesSecure(
        @Url
        url: String?
    ): GamesDto

}