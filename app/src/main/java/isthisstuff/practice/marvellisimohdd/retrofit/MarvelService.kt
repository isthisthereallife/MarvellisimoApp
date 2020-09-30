package isthisstuff.practice.marvellisimohdd.retrofit

import retrofit2.http.GET
import retrofit2.http.Url
import io.reactivex.Single
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import isthisstuff.practice.marvellisimohdd.entities.MarvelDataWrapper

const val ts = "1"
const val apiKey = "b86c9b29de66b43245e33fa2c4be3784"
const val privateKey = "a517e2142e39da03e49f617637f61a8eda5bddcc"

const val MARVEL_API_BASE_URL = "https://gateway.marvel.com/v1/public/"
val HASH = (ts + privateKey + apiKey).md5()

interface MarvelService {
    @GET
    fun builtRequestResult(@Url url:String?): Single<MarvelDataWrapper?>?
}

fun String.md5(): String {
    val MD5 = "MD5"
    try {
        val digest = MessageDigest.getInstance(MD5)
        digest.update(this.toByteArray())
        val messageDigest = digest.digest()
        val hexString = StringBuilder()
        for (aMessageDigest in messageDigest) {
            var h = Integer.toHexString(0XFF and aMessageDigest.toInt())

            while (h.length < 2) {
                h = "0$h"
            }
            hexString.append(h)

        }
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}

