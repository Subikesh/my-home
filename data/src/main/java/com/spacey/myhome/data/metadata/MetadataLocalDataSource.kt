package com.spacey.myhome.data.metadata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MetadataLocalDataSource {

    @Query("SELECT * FROM ${FieldConstants.DB.TABLE}")
    fun getFieldsMetadata(): List<FieldDBEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFieldsMetadata(fieldsList: List<FieldDBEntity>)

    @Query("DELETE FROM ${FieldConstants.DB.TABLE}")
    fun deleteFieldsMetadata()
}