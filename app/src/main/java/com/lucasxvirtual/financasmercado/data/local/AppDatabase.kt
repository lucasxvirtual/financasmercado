package com.lucasxvirtual.financasmercado.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lucasxvirtual.financasmercado.data.local.dao.InvoiceDao
import com.lucasxvirtual.financasmercado.data.local.dao.MarketDao
import com.lucasxvirtual.financasmercado.data.local.dao.ProductDao
import com.lucasxvirtual.financasmercado.data.local.dao.ProductInvoiceDao
import com.lucasxvirtual.financasmercado.data.local.entities.InvoiceEntity
import com.lucasxvirtual.financasmercado.data.local.entities.MarketEntity
import com.lucasxvirtual.financasmercado.data.local.entities.ProductEntity
import com.lucasxvirtual.financasmercado.data.local.entities.ProductInvoiceEntity

@Database(
    entities = [
        InvoiceEntity::class,
        MarketEntity::class,
        ProductEntity::class,
        ProductInvoiceEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun invoiceDao(): InvoiceDao
    abstract fun productDao(): ProductDao
    abstract fun marketDao(): MarketDao
    abstract fun productInvoiceDao(): ProductInvoiceDao
    companion object {
        const val DATA_BASE_NAME = "financas-mercado-db"
    }
}
