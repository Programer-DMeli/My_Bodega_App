## Mejora con IA - MiBodega
**Estudainte**: Meliton Carbajal

---
### Evidencias de uso de prompts 
1.- Rol: Actúa como un desarrollador experto en Android y Jetpack Compose.
Contexto: Estoy desarrollando la Pantalla de Inicio de una app de tienda. Ya tengo una lista de productos gestionada con estado 
local (mutableStateOf) y un filtro de categorías funcionando.
Tarea: Implementa un campo de búsqueda que filtre la lista de productos en tiempo real a medida que escribo. Este filtro de texto debe combinarse 
lógicamente con el filtro de categoría existente, garantizando que ambos funcionen juntos de forma simultánea sin reemplazarse el uno al otro.

**Resultado IA:**
<img width="379" height="832" alt="imagen" src="https://github.com/user-attachments/assets/43fdf4f8-5a05-4b88-9ce3-84b333c524bc" />

  

# Requerimientos Funcionales del Proyecto

## 1. Alcance Técnico y Arquitectura
*   **Arquitectura:** Se debe aplicar una Arquitectura de Actividad Única (*Single-Activity Architecture*), donde una sola Activity alberga toda la interfaz de usuario gestionada por Compose[cite: 13, 14].
*   **Manejo de Estado Local:** El proyecto exige un enfoque estricto sin el uso de ViewModel o arquitectura MVVM[cite: 13]. Todo el estado de la aplicación debe manejarse de forma local utilizando `remember` y `mutableStateOf`[cite: 13, 14].
*   **Listas de Datos:** Se requiere la integración de listas para mostrar elementos utilizando los componentes `LazyColumn` y `LazyRow`[cite: 13].
*   **Selección Única:** Se debe implementar la selección de opción única (comportamiento tipo *single-selection* o radio) en componentes como botones o *chips*, controlada mediante un estado booleano o una clave seleccionada[cite: 13].

## 2. Patrones de Navegación Exigidos
El sistema de navegación debe controlarse a través de un `NavController` y un `NavHost`, cumpliendo con dos modalidades[cite: 13, 14]:

### A. Navegación Secuencial
*   **Flujo obligatorio:** El usuario debe poder navegar en el siguiente orden: Inicio $\rightarrow$ Detalle $\rightarrow$ Acción (Agendar/Reservar) $\rightarrow$ Confirmación[cite: 13].
*   **Paso de Parámetros:** El flujo secuencial debe contemplar el paso de argumentos o parámetros entre pantallas a través de las rutas del `NavController`[cite: 13, 14].

### B. Navegación Secundaria (Mínimo 3 destinos)
El proyecto debe incluir una navegación secundaria que conmute entre al menos 3 destinos principales, eligiendo una de las siguientes opciones[cite: 13]:
*   **Opción A (Clínica Salud+):** Uso de un menú lateral desplegable mediante el componente `ModalNavigationDrawer` que se acciona con un ícono de hamburguesa (☰)[cite: 13].
*   **Opción B (TECSUP Fit):** Uso de un menú de navegación fijado en la parte inferior de la pantalla utilizando el componente `bottomBar` (`NavigationBar`), el cual debe resaltar visualmente el ícono de la pantalla activa[cite: 13].

## 3. Estructuración Visual
*   **Uso de Scaffold:** La plantilla visual de la pantalla debe construirse utilizando el componente `Scaffold` para asegurar que los espacios (como la `topBar` o la `bottomBar`) no se superpongan con el área de contenido principal[cite: 13, 14].
*   **Regla del Drawer:** Si se opta por la implementación del menú lateral (Opción A), el componente `ModalNavigationDrawer` no debe ir colocado dentro del `Scaffold`[cite: 13, 14]. Su correcta implementación exige que el `ModalNavigationDrawer` envuelva al `Scaffold` para que el panel pueda deslizarse y cubrir la barra superior de forma nativa[cite: 13, 14].
