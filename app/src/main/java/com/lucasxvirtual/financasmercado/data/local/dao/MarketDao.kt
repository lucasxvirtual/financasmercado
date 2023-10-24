package com.lucasxvirtual.financasmercado.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lucasxvirtual.financasmercado.data.local.entities.MarketEntity

@Dao
interface MarketDao {
    @Upsert
    suspend fun insert(list: List<MarketEntity>)

    @Query("SELECT * FROM ${MarketEntity.TABLE_NAME} WHERE cnpj = :cnpj")
    suspend fun getByCnpj(cnpj: String): List<MarketEntity>

    @Query("DELETE FROM ${MarketEntity.TABLE_NAME}")
    suspend fun deleteAll()
}