package isthisstuff.practice.marvellisimohdd.entities

import java.io.Serializable

data class MarvelDataWrapper(
    val code: Int,
    val status: String,
    val data: MarvelDataContainer
)

data class MarvelDataContainer(
    val total: Int,
    val count: Int,
    val results: List<MarvelObject>
)

data class MarvelObject(
    val id: Int,
    val name: String?,
    val title: String?,
    val description: String?,
    val thumbnail: Thumbnail,
    val urls: List<Urls>
) : Serializable

data class Thumbnail(val path: String, val extension: String) : Serializable

data class Urls(val type: String, val url: String) : Serializable



