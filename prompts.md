### Proyecto MiBodega
**Estudiante:** Meliton Carbajal

---
**Descripcion:** Este proyecto es una aplicación móvil enfocada en la implementación de patrones de diseño de navegación modernos e interfaces reactivas.
#### Uso de Prompts:

Rol: Actúa como un desarrollador experto en Android y Jetpack Compose.
Contexto: Estoy desarrollando la Pantalla de Inicio de una app de tienda. Ya tengo una lista de productos gestionada con estado local (mutableStateOf) y un filtro de categorías funcionando.
Tarea: Implementa un campo de búsqueda que filtre la lista de productos en tiempo real a medida que escribo. Este filtro de texto debe combinarse 
lógicamente con el filtro de categoría existente, garantizando que ambos funcionen juntos de forma simultánea sin reemplazarse el uno al otro.


#### Resultado sin IA:

<img width="300" height="700" alt="Captura desde 2026-10-02 23-34-57" src="https://github.com/user-attachments/assets/834a9805-d990-45af-8386-fd9a8ebcb15d" />



#### Resultado con IA:

<img width="300" height="700" alt="imagen" src="https://github.com/user-attachments/assets/9be87a95-4e41-43e8-9be8-e5293f04de5a" />



#### Requerimiento funcionales
 **1. Arquitectura y Gestión de Estado**
* El proyecto se construye bajo una Arquitectura de Actividad Única (Single-Activity Architecture).
* Una sola Actividad alberga toda la interfaz de usuario, la cual es gestionada mediante Compose, NavHost y NavController.
* Se utiliza un enfoque estricto sin ViewModel / MVVM.
* Todo el estado se maneja localmente mediante las funciones remember y mutableStateOf.

**2. Estructura de Interfaz de Usuario**
* La estructura visual utiliza componentes basados en Material Design.
* Se integran listas de contenido mediante los componentes LazyColumn y LazyRow.
* Se incluye una sección de selección de opción única con un comportamiento de tipo radio (single-selection).
* El manejo de esta selección única se controla mediante un estado booleano o una clave seleccionada.

**3. Navegación Secuencial Principal**
* Se implementa un flujo de navegación secuencial: Inicio -> Detalle -> Acción (Agregar y Comprar) -> Confirmación.
* Esta transición entre pantallas contempla el paso de argumentos o parámetros.
* El envío de estos parámetros se realiza a través de las rutas del NavController para mantener la trazabilidad en la pila de navegación (backstack).

**4. Navegación Secundaria**
* Existe una navegación secundaria integrada con un mínimo de 3 destinos principales.
* Si se opta por navegación inferior, se utiliza un bottomBar (NavigationBar) fijado en la parte inferior, el cual resalta el ícono de la pantalla actualmente activa.

