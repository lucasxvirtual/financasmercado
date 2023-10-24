package com.lucasxvirtual.financasmercado.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lucasxvirtual.financasmercado.data.local.ProductInvoiceAndProduct
import com.lucasxvirtual.financasmercado.data.local.ProductInvoiceCompleteInformationLocalData
import com.lucasxvirtual.financasmercado.data.local.entities.InvoiceEntity
import com.lucasxvirtual.financasmercado.data.local.entities.ProductEntity
import com.lucasxvirtual.financasmercado.data.local.entities.ProductInvoiceEntity
import com.lucasxvirtual.financasmercado.data.model.SpentOnProductReport
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductInvoiceDao {
    @Upsert
    suspend fun insert(list: List<ProductInvoiceEntity>)

    @Query(
        "SELECT ${ProductInvoiceEntity.TABLE_NAME}.id, ${ProductInvoiceEntity.TABLE_NAME}.productId, ${ProductInvoiceEntity.TABLE_NAME}.price, ${ProductInvoiceEntity.TABLE_NAME}.quantity, ${ProductInvoiceEntity.TABLE_NAME}.discount, ${ProductInvoiceEntity.TABLE_NAME}.invoiceId, ${ProductEntity.TABLE_NAME}.id as pid, ${ProductEntity.TABLE_NAME}.eanCode as peanCode, ${ProductEntity.TABLE_NAME}.name as pname, ${ProductEntity.TABLE_NAME}.unitType as punitType FROM ${ProductInvoiceEntity.TABLE_NAME} " +
            "INNER JOIN ${ProductEntity.TABLE_NAME} ON ${ProductEntity.TABLE_NAME}.id = ${ProductInvoiceEntity.TABLE_NAME}.productId " +
            "WHERE ${ProductInvoiceEntity.TABLE_NAME}.invoiceId = :id")
    suspend fun getByInvoiceId(id: String): List<ProductInvoiceAndProduct>

    @Query("SELECT ${ProductEntity.TABLE_NAME}.id, SUM((${ProductInvoiceEntity.TABLE_NAME}.price * ${ProductInvoiceEntity.TABLE_NAME}.quantity) - ${ProductInvoiceEntity.TABLE_NAME}.discount) as spent, ${ProductEntity.TABLE_NAME}.name " +
            "FROM ${ProductInvoiceEntity.TABLE_NAME} INNER JOIN ${ProductEntity.TABLE_NAME} ON ${ProductEntity.TABLE_NAME}.id = ${ProductInvoiceEntity.TABLE_NAME}.productId " +
            "INNER JOIN ${InvoiceEntity.TABLE_NAME} ON ${InvoiceEntity.TABLE_NAME}.id = ${ProductInvoiceEntity.TABLE_NAME}.invoiceId " +
            "WHERE date(date) > datetime('now', '-3 month') " +
            "GROUP BY ${ProductEntity.TABLE_NAME}.id " +
            "ORDER BY spent DESC " +
            "LIMIT 30")
    fun getByMostSpent(): Flow<List<SpentOnProductReport>>

    @Query("DELETE FROM ${ProductInvoiceEntity.TABLE_NAME}")
    suspend fun deleteAll()

    @Query(
        "SELECT ${ProductInvoiceEntity.TABLE_NAME}.id, ${ProductInvoiceEntity.TABLE_NAME}.invoiceId, ${ProductInvoiceEntity.TABLE_NAME}.productId, ${ProductInvoiceEntity.TABLE_NAME}.price, ${ProductInvoiceEntity.TABLE_NAME}.quantity, ${ProductInvoiceEntity.TABLE_NAME}.discount, ${ProductEntity.TABLE_NAME}.name as pname, ${ProductEntity.TABLE_NAME}.unitType as punitType, ${ProductEntity.TABLE_NAME}.id as pid, ${ProductEntity.TABLE_NAME}.eanCode as peanCode, ${InvoiceEntity.TABLE_NAME}.date " +
            "FROM ${ProductInvoiceEntity.TABLE_NAME} " +
            "INNER JOIN ${ProductEntity.TABLE_NAME} ON ${ProductEntity.TABLE_NAME}.id = ${ProductInvoiceEntity.TABLE_NAME}.productId " +
            "INNER JOIN ${InvoiceEntity.TABLE_NAME} ON ${ProductInvoiceEntity.TABLE_NAME}.invoiceId = ${InvoiceEntity.TABLE_NAME}.id " +
            "WHERE ${ProductEntity.TABLE_NAME}.id = :productId AND date(date) > datetime('now', '-3 month') " +
            "ORDER BY date DESC")
    fun getByProductId(productId: Int): Flow<List<ProductInvoiceCompleteInformationLocalData>>
}