package isthisstuff.practice.marvellisimohdd.entities

data class LoggedInUser(
    val userId: Int,
    var userName: String,
    var favourites: MutableList<String>
)

/*(1..999999999).random().toString()*/