package com.lucasxvirtual.financasmercado.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lucasxvirtual.financasmercado.data.local.entities.InvoiceEntity
import com.lucasxvirtual.financasmercado.data.model.InvoiceSimpleInformation
import com.lucasxvirtual.financasmercado.data.model.MonthlyReport
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {
    @Upsert
    suspend fun insert(list: List<InvoiceEntity>)

    @Query("SELECT * FROM ${InvoiceEntity.TABLE_NAME}")
    suspend fun getAll(): List<InvoiceEntity>

    @Query("SELECT id, totalPrice, date FROM ${InvoiceEntity.TABLE_NAME} ORDER BY date(date) DESC LIMIT 30")
    fun getInvoiceSimpleInformation(): Flow<List<InvoiceSimpleInformation>>

    @Query("SELECT id, totalPrice, date FROM ${InvoiceEntity.TABLE_NAME} WHERE strftime('%Y-%m', date) = :date ORDER BY date(date)")
    fun getInvoiceSimpleInformationByDate(date: String): Flow<List<InvoiceSimpleInformation>>

    @Query("SELECT * FROM ${InvoiceEntity.TABLE_NAME} ORDER BY date(date) DESC LIMIT 1")
    fun getLastInvoice(): Flow<InvoiceEntity?>

    @Query("SELECT * FROM ${InvoiceEntity.TABLE_NAME} WHERE id = :id LIMIT 1")
    fun getInvoice(id: String): Flow<InvoiceEntity?>

    @Query("SELECT SUM(${InvoiceEntity.TABLE_NAME}.totalPrice) as spent, ${InvoiceEntity.TABLE_NAME}.date " +
            "FROM ${InvoiceEntity.TABLE_NAME} " +
            "GROUP BY strftime('%Y-%m', date) " +
            "ORDER BY date(date) DESC " +
            "LIMIT 6")
    fun getMonthlyReport(): Flow<List<MonthlyReport>>

    @Query("DELETE FROM ${InvoiceEntity.TABLE_NAME}")
    suspend fun deleteAll()
}