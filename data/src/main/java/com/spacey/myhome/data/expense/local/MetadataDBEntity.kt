package com.spacey.myhome.data.expense.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spacey.myhome.data.expense.FieldDBKeyConstants
import com.spacey.myhome.data.expense.FieldEntity

@Entity(tableName = MetadataLocalDataSource.TABLE_NAME)
data class FieldDBEntity(
    @PrimaryKey @ColumnInfo(FieldDBKeyConstants.ID) override val id: String,
    @ColumnInfo(FieldDBKeyConstants.NAME) override val name: String,
    @ColumnInfo(FieldDBKeyConstants.TYPE) override val type: String,
    @ColumnInfo(FieldDBKeyConstants.INPUT) override val input: String
) : FieldEntity(id, name, type, input)
