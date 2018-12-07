package com.navdroid.frshgiph.model

import com.google.gson.annotations.SerializedName


data class GiphyResponse(
        @SerializedName("data") var data: List<Data?>?,
        @SerializedName("pagination") var pagination: Pagination?,
        @SerializedName("meta") var meta: Meta?
)

data class Data(
        @SerializedName("id") var id: String?,
        @SerializedName("images") var images: Images?
)

data class Images(
        @SerializedName("original") var original: Original?
)


data class Original(
        @SerializedName("url") var url: String?,
        @SerializedName("width") var width: String?,
        @SerializedName("height") var height: String?,
        @SerializedName("size") var size: String?,
        @SerializedName("frames") var frames: String?
)

data class Meta(
        @SerializedName("status") var status: Int?,
        @SerializedName("msg") var msg: String?,
        @SerializedName("response_id") var responseId: String?
)

data class Pagination(
        @SerializedName("total_count") var totalCount: Int?,
        @SerializedName("count") var count: Int?,
        @SerializedName("offset") var offset: Int?
)