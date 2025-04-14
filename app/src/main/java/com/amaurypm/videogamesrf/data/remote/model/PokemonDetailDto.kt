package com.amaurypm.videogamesrf.data.remote.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailDto(
    @SerializedName("name")
    var name: String,
    @SerializedName("sprites")
    var sprites: Sprites
)

data class Sprites(
    @SerializedName("other")
    var other: Other
)

data class Other(
    @SerializedName("official-artwork")
    var officialArtwork: OfficialArtwork
)

data class OfficialArtwork(
    @SerializedName("front_default")
    var frontDefault: String,
    @SerializedName("front_shiny")
    var frontShiny: String
)