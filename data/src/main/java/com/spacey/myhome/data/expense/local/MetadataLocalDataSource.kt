package com.spacey.myhome.data.expense.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MetadataLocalDataSource {

    @Query("SELECT * FROM $TABLE_NAME")
    fun getFieldsMetadata(): List<FieldDBEntity>

    @Insert
    fun insertFieldsMetadata(fieldsList: List<FieldDBEntity>)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteFieldsMetadata()

    companion object {
        const val TABLE_NAME = "FIELD_METADATA"
    }
}