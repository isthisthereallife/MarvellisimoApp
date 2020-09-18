package isthisstuff.practice.marvellisimohdd.database

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmApp : Application() {


    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val configuration =
            RealmConfiguration.Builder().schemaVersion(1).deleteRealmIfMigrationNeeded()
                .name("marveldatabase").build()
        Realm.setDefaultConfiguration(configuration)
    }
}