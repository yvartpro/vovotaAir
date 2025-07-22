package bi.vovota.vovota

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Contact::class], version = 1)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}