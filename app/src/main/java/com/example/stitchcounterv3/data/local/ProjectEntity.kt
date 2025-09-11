package com.example.stitchcounterv3.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entry")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int = 0,

    @ColumnInfo(name = "type")
    val type: String, // "single" or "double"

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "stitch_counter_number")
    val stitchCounterNumber: Int = 0,

    @ColumnInfo(name = "stitch_adjustment")
    val stitchAdjustment: Int = 1,

    @ColumnInfo(name = "row_counter_number")
    val rowCounterNumber: Int = 0,

    @ColumnInfo(name = "row_adjustment")
    val rowAdjustment: Int = 1,

    @ColumnInfo(name = "total_rows")
    val totalRows: Int = 0,
)

