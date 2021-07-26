package com.example.eknowledgelibrary.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class CourseDetails (
    val _id : String? = null,
    val courseTitle : String? = null,
    val courseDesc : String? = null,
    val courseCategory : String? = null,
    val courseFile : String? = null,
    val postDate: String? = null,
    val fileTitle: String? = null
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var courseID: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),

    ) {
        courseID = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(courseTitle)
        parcel.writeString(courseDesc)
        parcel.writeString(courseCategory)
        parcel.writeString(courseFile)
        parcel.writeString(postDate)
        parcel.writeString(fileTitle)
        parcel.writeInt(courseID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CourseDetails> {
        override fun createFromParcel(parcel: Parcel): CourseDetails {
            return CourseDetails(parcel)
        }

        override fun newArray(size: Int): Array<CourseDetails?> {
            return arrayOfNulls(size)
        }
    }


}
