package com.amaurypm.videogamesrf.data.remote.model

import com.google.gson.annotations.SerializedName

data class GamesDto(
    @SerializedName("page")
    var page: Int,
    @SerializedName("limit")
    var limit: Int,
    @SerializedName("data")
    var games: List<GameDto>
)
