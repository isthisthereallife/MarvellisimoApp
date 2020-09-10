package isthisstuff.practice.marvellisimohdd.entities

class User (_username: String){
    val id = (1..999999999).random().toString()
    var username = _username
    var favouriteSeries = mutableListOf<String>()
    var favouriteCharacters = mutableListOf<String>()
    private lateinit var viewModel: UserViewModel



}