package com.tecsup.mibodega.ui.cliente

import androidx.compose.runtime.Composable
import com.tecsup.mibodega.navegacion.AppNavegacion

/**
 * Composable principal del módulo cliente que delega la orquestación a [AppNavegacion].
 */
@Composable
fun ClienteApp() {
    AppNavegacion()
}
