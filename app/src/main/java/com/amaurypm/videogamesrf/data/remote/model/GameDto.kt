package com.amaurypm.videogamesrf.data.remote.model

import com.google.gson.annotations.SerializedName

data class GameDto(
  /*  @SerializedName("id")
    var id: String? = null,
    @SerializedName("thumbnail")
    var thumbnail: String? = null,
    @SerializedName("title")
    var title: String? = null*/

    @SerializedName("user")
    var user: String? = null,
    @SerializedName("thumbnail")
    var thumbnail: String? = null,
    @SerializedName("name")
    var name: String? = null

)
