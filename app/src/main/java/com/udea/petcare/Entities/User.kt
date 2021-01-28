package com.udea.petcare.Entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val lastName: String,
    val email: String,
    val password:String,
    val city:String,
    val description:String
):Parcelable