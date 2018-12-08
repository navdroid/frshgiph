package com.navdroid.frshgiph.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.orm.SugarRecord
import com.orm.dsl.Unique

@JsonIgnoreProperties(ignoreUnknown = true)
data class GiphyResponse(
        @JsonProperty("data") var data: ArrayList<Data>,
        @JsonProperty("pagination") var pagination: Pagination?,
        @JsonProperty("meta") var meta: Meta?
)

@JsonIgnoreProperties(ignoreUnknown = true)
class Data() : SugarRecord() {

    var uid: String? = null

    var imageUrl: String? = null

    @JsonProperty("images")
    fun parse(images: Images?) {
        imageUrl = images?.original?.url
    }

    @JsonProperty("id")
    fun parse(id: String?) {
        this.uid = id
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other?.javaClass != javaClass -> false
            (other is Data) && id == other.id -> true
            else -> false
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Images(
        @JsonProperty("fixed_height_small") var original: Original?
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