package com.spacey.myhome.data.metadata

import android.util.Log
import com.spacey.myhome.data.ConnectivityHelper
import com.spacey.myhome.data.base.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MetadataRepository(
    private val metadataRemoteDataSource: MetadataRemoteDataSource,
    private val metadataLocalDataSource: MetadataLocalDataSource,
    private val connectivityHelper: ConnectivityHelper
) {
    suspend fun getMetadata(offline: Boolean = false): Data<MetadataEntity> = withContext(Dispatchers.IO) {
        try {
            if (!offline && connectivityHelper.isInternetConnected()) {
                val metadata = metadataRemoteDataSource.getMetadata()
                metadataLocalDataSource.insertFieldsMetadata(metadata.fields.map { it.toFieldDBEntity() })
            }
            Log.d("Meta", "Meta refreshed")
            Data.Success(MetadataEntity(metadataLocalDataSource.getFieldsMetadata()))
        } catch (e: Exception) {
            e.printStackTrace()
            Data.Error(exception = e)
        }
    }

    private fun FieldEntity.toFieldDBEntity(): FieldDBEntity = FieldDBEntity(id, name, routine, type)
}