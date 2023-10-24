package com.lucasxvirtual.financasmercado.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lucasxvirtual.financasmercado.data.local.entities.ProductEntity

@Dao
interface ProductDao {
    @Upsert
    suspend fun insert(list: List<ProductEntity>)

    @Query("DELETE FROM ${ProductEntity.TABLE_NAME}")
    suspend fun deleteAll()
}