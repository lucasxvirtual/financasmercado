package com.lucasxvirtual.financasmercado.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lucasxvirtual.financasmercado.data.model.Invoice
import com.lucasxvirtual.financasmercado.data.model.ProductInvoiceCompleteInfo
import com.lucasxvirtual.financasmercado.data.toInvoiceEntity
import com.lucasxvirtual.financasmercado.data.toMarket
import com.lucasxvirtual.financasmercado.data.toMarketEntity
import com.lucasxvirtual.financasmercado.data.toProduct
import com.lucasxvirtual.financasmercado.data.toProductEntity
import com.lucasxvirtual.financasmercado.data.toProductInvoice
import com.lucasxvirtual.financasmercado.data.toProductInvoiceCompleteInfo
import com.lucasxvirtual.financasmercado.data.toProductInvoiceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val database: AppDatabase,
    private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    suspend fun saveInvoice(invoice: Invoice) {
        database.invoiceDao().insert(listOf(invoice.toInvoiceEntity()))
        database.marketDao().insert(listOf(invoice.market.toMarketEntity()))
        database.productDao().insert(invoice.productInvoiceList.map { it.product.toProductEntity() })
        database.productInvoiceDao().insert(
            invoice.productInvoiceList.map {
                it.toProductInvoiceEntity(invoice.id)
            }
        )
    }

    fun getInvoiceSimpleInformation() = database.invoiceDao().getInvoiceSimpleInformation()

    fun getInvoiceSimpleInformationByDate(date: String) =
        database.invoiceDao().getInvoiceSimpleInformationByDate(date)

    fun getLastInvoice(): Flow<Invoice?> {
        return database.invoiceDao().getLastInvoice().map {
            if (it == null) {
                return@map null
            }
            val marketEntity = database.marketDao().getByCnpj(it.marketCnpj)
            val productInvoiceAndProductList = database.productInvoiceDao().getByInvoiceId(it.id)
            val productInvoiceList = productInvoiceAndProductList.map { value ->
                value.productInvoice.toProductInvoice(value.product.toProduct())
            }
            Invoice(
                id = it.id,
                market = marketEntity.firstOrNull().toMarket(),
                date = it.date,
                totalPrice = it.totalPrice,
                productInvoiceList = productInvoiceList
            )
        }
    }

    fun getInvoice(invoiceId: String): Flow<Invoice?> {
        return database.invoiceDao().getInvoice(invoiceId).map {
            if (it == null) {
                return@map null
            }
            val marketEntity = database.marketDao().getByCnpj(it.marketCnpj)
            val productInvoiceAndProductList =
                database.productInvoiceDao().getByInvoiceId(it.id)
            val productInvoiceList = productInvoiceAndProductList.map { value ->
                value.productInvoice.toProductInvoice(value.product.toProduct())
            }
            Invoice(
                id = it.id,
                market = marketEntity.firstOrNull().toMarket(),
                date = it.date,
                totalPrice = it.totalPrice,
                productInvoiceList = productInvoiceList
            )
        }
    }

    fun getMostSpentProducts() = database.productInvoiceDao().getByMostSpent()

    fun getMonthlyReport() = database.invoiceDao().getMonthlyReport()

    fun getProductCompleteInfo(productId: Int): Flow<List<ProductInvoiceCompleteInfo>> {
        return database.productInvoiceDao().getByProductId(productId).map {
            it.map { list ->
                list.toProductInvoiceCompleteInfo()
            }
        }
    }

    suspend fun saveHasSeenTutorial() {
        context.dataStore.edit {
            it[booleanPreferencesKey(TUTORIAL)] = true
        }
    }

    fun hasSeenTutorial(): Flow<Boolean> {
        return context.dataStore.data.map { it[booleanPreferencesKey(TUTORIAL)] ?: false }
    }

    suspend fun saveNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey(NOTIFICATIONS)] = enabled
        }
    }

    fun isNotificationEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { it[booleanPreferencesKey(NOTIFICATIONS)] ?: true }
    }

    suspend fun saveNonSentNotificationToken(token: String) {
        context.dataStore.edit {
            it[stringPreferencesKey(NOTIFICATION_TOKEN)] = token
        }
    }

    suspend fun clearNonSentNotificationToken() {
        context.dataStore.edit {
            it.remove(stringPreferencesKey(NOTIFICATION_TOKEN))
        }
    }

    fun getNonSentNotificationToken(): Flow<String?> {
        return context.dataStore.data.map { it[stringPreferencesKey(NOTIFICATION_TOKEN)] }
    }

    suspend fun clear() {
        database.invoiceDao().deleteAll()
        database.marketDao().deleteAll()
        database.productDao().deleteAll()
        database.productInvoiceDao().deleteAll()
        context.dataStore.edit { it.clear() }
    }

    companion object {
        const val TUTORIAL = "tutorial"
        const val NOTIFICATIONS = "notifications"
        const val NOTIFICATION_TOKEN = "notifications"
    }
}