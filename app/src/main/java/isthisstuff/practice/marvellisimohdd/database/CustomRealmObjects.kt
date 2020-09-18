package isthisstuff.practice.marvellisimohdd.database

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import isthisstuff.practice.marvellisimohdd.entities.Thumbnail
import isthisstuff.practice.marvellisimohdd.entities.Urls

open class User : RealmObject() {
    @PrimaryKey
    var email : String? = null
    var name: String? = null
    var favorites: RealmList<MarvelRealmObject> = RealmList()

}

open class MarvelRealmObject : RealmObject() {
    @PrimaryKey
    var id: Int? = null
    var name: String? = null
    val title: String? = null
    val description: String? = null
    val thumbnail: Thumbnail? = null
    val urls: List<Urls>? = null
}







/*
User

1 till många

Marvel



SPARA NER  (när favoritstjärnan trycks!?)

 realm.beginTransaction()
blablablablabla
realm.copyToRealmOrUpdate(person)
realm.commitTransaction()





HÄMTA  (i början? i main?)
val result = realm.where<User>()
.blablablaharNåttIFAVORITER
.findAll()



DELETE
gör en sökning och appenda :deleteAllFromRealm() på resultatet




/*
Det finns tex:
equalTo
between
lessThan och greaterThan samt lessThanOrEqualTo samt greaterThanOrEqualTo
anyOf
Strängar har också:
beginsWith
endsWith
contains

Dessutom kan man man använda:
or()
not()
samt skapa start-parentes och slut-parentes via beginGroup() och endGroup()

 */
 */
