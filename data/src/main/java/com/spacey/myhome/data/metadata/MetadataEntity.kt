package com.spacey.myhome.data.metadata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MetadataEntity(
    @SerializedName(MetadataConstants.Network.FIELDS) val fields: List<FieldEntity>
)

open class FieldEntity(
    @SerializedName(FieldConstants.Network.ID) open val id: String,
    @SerializedName(FieldConstants.Network.NAME) open val name: String,
    @SerializedName(FieldConstants.Network.ROUTINE) open val routine: String,
    @SerializedName(FieldConstants.Network.TYPE) open val type: String,
)
/** Field roomDB entity */
@Entity(tableName = FieldConstants.DB.TABLE)
data class FieldDBEntity(
    @PrimaryKey @ColumnInfo(FieldConstants.DB.ID) override val id: String,
    override val name: String,
    override val routine: String,
    override val type: String
) : FieldEntity(id, name, routine, type)
