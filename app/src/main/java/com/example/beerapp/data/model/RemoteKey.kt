package com.example.beerapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val next: Int?,
    val prev: Int?
)