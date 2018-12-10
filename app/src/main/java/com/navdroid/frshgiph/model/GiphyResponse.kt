package com.navdroid.frshgiph.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GiphyResponse(
        @JsonProperty("data") var data: ArrayList<Data>,
        @JsonProperty("pagination") var pagination: Pagination?,
        @JsonProperty("meta") var meta: Meta?
)

@Entity(tableName = "gif_table")
@JsonIgnoreProperties(ignoreUnknown = true)
class Data {

    @JsonProperty("id")
    @PrimaryKey
    lateinit var uid: String

    var imageUrl: String? = null

    var isFavorite: Boolean = false

    @JsonProperty("images")
    fun parse(images: Images?) {
        imageUrl = images?.original?.url
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other?.javaClass != javaClass -> false
            (other is Data) && uid == other.uid -> true
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + isFavorite.hashCode()
        return result
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Images(
        @JsonProperty("fixed_width_small") var original: Original?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Original(
        @JsonProperty("url") var url: String?,
        @JsonProperty("width") var width: String?,
        @JsonProperty("height") var height: String?,
        @JsonProperty("size") var size: String?
)

data class Meta(
        @JsonProperty("status") var status: Int?,
        @JsonProperty("msg") var msg: String?,
        @JsonProperty("response_id") var responseId: String?
)

data class Pagination(
        @JsonProperty("total_count") var totalCount: Int = 0,
        @JsonProperty("count") var count: Int?,
        @JsonProperty("offset") var offset: Int?
)