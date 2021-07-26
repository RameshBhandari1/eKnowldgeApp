package com.example.finalassignment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eknowledgelibrary.entities.CourseDetails
import com.example.finalassignment.dao.CourseDAO

@Database(
    entities = [(CourseDetails::class)],
    version = 1,
    exportSchema = false
)
abstract class eKnowledgeDB: RoomDatabase() {
    abstract  fun addCourseDAO(): CourseDAO
    companion object{
        @Volatile
        private var instance : eKnowledgeDB? = null

        fun getInstance(context : Context): eKnowledgeDB{
            if (instance == null){
                synchronized(eKnowledgeDB :: class){
                    instance = buildDatabase(context)
                }
            }
            return  instance!!
        }

        private fun buildDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                eKnowledgeDB::class.java,
                "eKnowledgeDB"
            ).build()
    }
}