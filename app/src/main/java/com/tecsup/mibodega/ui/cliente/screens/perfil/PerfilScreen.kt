package com.tecsup.mibodega.ui.cliente.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.theme.BodegaTheme
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun PerfilScreen(
    nombreUsuario: String = "Cliente Mi Bodega",
    telefonoUsuario: String = "987654321",
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onVerFavoritos: () -> Unit,
    onVerPedidos: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Mi Perfil",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .background(VerdeBodega.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = VerdeBodega,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = nombreUsuario,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = telefonoUsuario,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(32.dp))

        // Opciones de configuración y navegación
        OpcionPerfilCard(
            titulo = "Modo Oscuro",
            icono = Icons.Default.DarkMode,
            esSwitch = true,
            estadoSwitch = isDarkMode,
            onSwitchChanged = onToggleDarkMode,
            onClick = {}
        )

        Spacer(Modifier.height(12.dp))

        OpcionPerfilCard(
            titulo = "Mis Favoritos",
            icono = Icons.Default.Favorite,
            onClick = onVerFavoritos
        )

        Spacer(Modifier.height(12.dp))

        OpcionPerfilCard(
            titulo = "Mis Pedidos",
            icono = Icons.Default.Receipt,
            onClick = onVerPedidos
        )

        Spacer(Modifier.height(12.dp))

        OpcionPerfilCard(
            titulo = "Cerrar Sesión",
            icono = Icons.AutoMirrored.Filled.Logout,
            onClick = onCerrarSesion,
            esPeligro = true
        )
    }
}

@Composable
private fun OpcionPerfilCard(
    titulo: String,
    icono: ImageVector,
    esSwitch: Boolean = false,
    estadoSwitch: Boolean = false,
    onSwitchChanged: (Boolean) -> Unit = {},
    onClick: () -> Unit,
    esPeligro: Boolean = false
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (esPeligro) MaterialTheme.colorScheme.error else VerdeBodega
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyLarge,
                color = if (esPeligro) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            if (esSwitch) {
                Switch(
                    checked = estadoSwitch,
                    onCheckedChange = onSwitchChanged
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PerfilPreview() {
    BodegaTheme {
        PerfilScreen(
            isDarkMode = false,
            onToggleDarkMode = {},
            onVerFavoritos = {},
            onVerPedidos = {},
            onCerrarSesion = {}
        )
    }
}
