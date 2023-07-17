package com.spacey.myhome.data.expense

import com.google.gson.annotations.SerializedName
import com.spacey.myhome.data.expense.MetadataNetworkConstants.FieldNetworkConstants

data class MetadataEntity(
    @SerializedName(MetadataNetworkConstants.FIELDS) val fields: List<FieldEntity>
)

open class FieldEntity(
    @SerializedName(FieldNetworkConstants.ID) open val id: String,
    @SerializedName(FieldNetworkConstants.NAME) open val name: String,
    @SerializedName(FieldNetworkConstants.TYPE) open val type: String,
    @SerializedName(FieldNetworkConstants.INPUT) open val input: String,
)