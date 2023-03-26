package com.example.beerapp.data.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Beers")
data class BeerDTO(
    @PrimaryKey
    val id: Long,
    val name: String? = "",
    val tagline: String? = "",
    val first_brewed: String? = "",
    val description: String? = "",
    val image_url: String? = null,
    val abv: Double? = null,
    val ibu: Double? = null,
    val target_fg: Double? = null,
    val target_og: Double? = null,
    val ebc: Double? = null,
    val srm: Double? = null,
    val ph: Double? = null,
    val attenuation_level: Double? = null,
    @Embedded(prefix = "volume_")
    val volume: BoilVolume? = null,
    @Embedded(prefix = "boilVolume_")
    val boil_volume: BoilVolume? = null,
    @Embedded
    val method: Method? = null,
    @Embedded
    val ingredients: Ingredients? = null,
    val food_pairing: List<String>? = null,
    val brewers_tips: String? = "",
    val contributed_by: String? = ""
) : Parcelable

@Parcelize
data class BoilVolume(
    val value: Double?,
    val unit: String?
) : Parcelable


@Parcelize
data class Ingredients(
    val malt: List<Malt>?,
    val hops: List<Hop>?,
    val yeast: String?
) : Parcelable

@Parcelize
data class Hop(
    val name: String?,
    @Embedded(prefix = "hop_")
    val amount: BoilVolume?,
    val add: String?,
    val attribute: String?
) : Parcelable

@Parcelize
data class Malt(
    val name: String?,
    @Embedded(prefix = "malt_")
    val amount: BoilVolume?
) : Parcelable

@Parcelize
data class Method(
    val mash_temp: List<MashTemp>?,
    @Embedded
    val fermentation: Fermentation?,
    val twist: String?
) : Parcelable

@Parcelize
data class Fermentation(
    @Embedded(prefix = "fermentation_")
    val temp: BoilVolume?
) : Parcelable

@Parcelize
data class MashTemp(
    @Embedded(prefix = "mash_temp_")
    val temp: BoilVolume?,
    val duration: Long?
) : Parcelable
