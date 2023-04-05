package com.example.beerapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.errorprone.annotations.Keep

@Entity
@Keep
data class RemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val next: Int?,
    val prev: Int?
)