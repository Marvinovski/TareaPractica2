package com.amaurypm.videogamesrf.data.remote.model

import com.google.gson.annotations.SerializedName

data class GameDetailDto(

    /*@SerializedName("title")
    var title: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("long_desc")
    var longDesc: String? = null*/
    @SerializedName("name")
    var name: String? = null,

    @SerializedName("email")
    var email: String? = null,
    @SerializedName("telephone")
    var telephone: String? = null,
    @SerializedName("date_reservation")
    var dateReservation: String? = null,
    @SerializedName("time_reservation")
    var timeReservation: String? = null,
    @SerializedName("share_court")
    var shareCourt: String? = null,
    @SerializedName("image")
    var url_video: String? = null

)
