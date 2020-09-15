package isthisstuff.practice.marvellisimohdd.entities

import java.io.Serializable

data class CharacterDataWrapper(
    val code: Int,
    val status: String,
    val data: CharacterDataContainer
)

data class CharacterDataContainer(
    val total: Int,
    val count: Int,
    val results: List<MarvelObject>
)

//TODO add thumbnail
data class MarvelObject(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail
):Serializable

data class Thumbnail(val path: String, val extension: String):Serializable



