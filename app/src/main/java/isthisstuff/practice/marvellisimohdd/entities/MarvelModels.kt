package isthisstuff.practice.marvellisimohdd.entities

data class CharacterDataWrapper(
    val code: Int,
    val status: String,
    val data: CharacterDataContainer
)

data class CharacterDataContainer(
    val total: Int,
    val count: Int,
    val results: Array<MarvelCharacter>
)

//TODO add thumbnail
data class MarvelCharacter(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail
)

data class Thumbnail(val path: String, val extension: String)



