package com.proficiency.assingment.model


import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FactsResponse(
    @SerializedName("rows")
    val rows: ArrayList<Row>,
    @SerializedName("title")
    val title: String
) {
    @Entity(
        tableName = "facts",indices = arrayOf(Index(value = ["imageHref"], unique = true))
    )
    data class Row(
        @PrimaryKey(
            autoGenerate = true
        )
        val id: Int? = null,
        @SerializedName("description")
        val description: String?,
        @SerializedName("imageHref")
        val imageHref: String?,
        @SerializedName("title")
        val title: String?
    ) : Serializable

    data class Source(
        val id: Any,
        val name: String
    )
}