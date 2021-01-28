package com.udea.petcare.Entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "publication_table", foreignKeys = arrayOf(
    ForeignKey(entity = User::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("user"),
    onDelete = ForeignKey.NO_ACTION)
))


data  class Publication (
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var title: String,
    var description: String,
    var city: String,
    var type:String,
    var dateTime:String,
    var image:String,
    var user:Int

)

