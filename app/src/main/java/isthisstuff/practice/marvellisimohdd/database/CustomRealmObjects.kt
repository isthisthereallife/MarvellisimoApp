package isthisstuff.practice.marvellisimohdd.database

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class User : RealmObject() {
    @PrimaryKey
    var email: String? = null
    var name: String? = null
    var favorites: RealmList<MarvelRealmObject> = RealmList()

}

open class MarvelRealmObject : RealmObject() {
    @PrimaryKey
    var id: Int? = null
    var name: String? = null
    var title: String? = null
    var description: String? = null
    var thumbnail: ThumbnailRealmObject? = null
    var urls: RealmList<UrlsRealmObject>? = RealmList()
}

//JAG VILL INTE SPARA THUMBNAIL MEN JAG MÅSTE TYDLIGEN


open class ThumbnailRealmObject : RealmObject() {
    @PrimaryKey
    var path: String? = null
    var extension: String? = null
}

open class UrlsRealmObject : RealmObject() {
    @PrimaryKey
    var url: String? = null
    var type: String? = null
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
