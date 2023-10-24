package com.lucasxvirtual.financasmercado.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lucasxvirtual.financasmercado.ui.home.HomeScreen
import com.lucasxvirtual.financasmercado.ui.importinvoice.ImportInvoiceScreen
import com.lucasxvirtual.financasmercado.ui.invoice.InvoiceScreen
import com.lucasxvirtual.financasmercado.ui.month.MonthScreen
import com.lucasxvirtual.financasmercado.ui.product.ProductScreen

@Composable
fun FinancasMercadoApp() {
    val navController = rememberNavController()
    FinancasMercadoNavHost(
        navController = navController
    )
}

@Composable
fun FinancasMercadoNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onInvoiceClicked = {
                    navController.navigate("invoice/$it")
                },
                onInvoiceItemClicked = { id, name ->
                    val encodedName = Uri.encode(name)
                    navController.navigate("product/$id/$encodedName")
                },
                onImportClicked = {
                    navController.navigate("importinvoice")
                },
                onMonthClicked = {
                    navController.navigate("month/$it")
                }
            )
        }
        composable("importinvoice") {
            ImportInvoiceScreen {
                if (navController.currentDestination?.route == "importinvoice") {
                    navController.popBackStack()
                }
            }
        }
        composable(
            "invoice/{invoiceId}",
            arguments = listOf(navArgument("invoiceId") {type = NavType.StringType})
        ) {
            InvoiceScreen(
                onInvoiceItemClicked = { id, name ->
                    val encodedName = Uri.encode(name)
                    navController.navigate("product/$id/$encodedName")
                }
            ) {
                navController.popBackStack()
            }
        }
        composable(
            "product/{productId}/{productName}",
            arguments = listOf(
                navArgument("productId") {type = NavType.IntType},
                navArgument("productName") {type = NavType.StringType}
            )
        ) {
            ProductScreen(
                onInvoiceClicked = {
                    navController.navigate("invoice/$it")
                }
            ) {
                navController.popBackStack()
            }
        }
        composable(
            "month/{date}",
            arguments = listOf(
                navArgument("date") {type = NavType.StringType}
            )
        ) {
            MonthScreen(
                onInvoiceClicked = {
                    navController.navigate("invoice/$it")
                }
            ) {
                navController.popBackStack()
            }
        }
    }
}