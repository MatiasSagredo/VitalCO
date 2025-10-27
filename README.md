# VitalCO - Sistema de Control de Inventario

Aplicación móvil desarrollada en **Kotlin** con **Jetpack Compose** para gestionar el inventario de productos de primera necesidad (agua embotellada, carbón y huevos) de la empresa distribuidora **Vitalco**.

## Características

### Funcionalidades Implementadas

- **Gestión de Productos**
  - Listar todos los productos con búsqueda en tiempo real
  - Agregar nuevos productos
  - Editar información del producto (nombre, descripción, precio)
  - Eliminar productos
  - Ver detalles completos del producto

- **Control de Stock**
  - Visualizar stock actual y mínimo
  - Ajustar stock manualmente
  - Registrar ventas (resta automática de inventario)
  - Registrar compras a proveedores (suma automática de inventario)
  - Alertas visuales de stock bajo

- **Persistencia de Datos**
  - Base de datos local con **Room**
  - Almacenamiento en caché para acceso rápido
  - Sincronización con backend

- **Interfaz de Usuario**
  - Diseño moderno con **Material Design 3**
  - Navegación intuitiva
  - Pantalla de detalle de producto con actualización en vivo
  - Soporte para tema claro/oscuro

## Arquitectura

### Capas

```
├── ui/
│   ├── screens/          # Pantallas principales
│   ├── components/       # Componentes reutilizables
│   ├── theme/            # Temas y estilos
│   └── viewmodel/        # ViewModels
├── data/
│   ├── remote/           # Base de datos local (Room)
│   │   ├── dao/          # Data Access Objects
│   │   ├── model/        # Entidades
│   │   └── AppDatabase.kt
│   └── repository/       # Repositorios
├── navigation/           # Rutas de navegación
└── MainActivity.kt       # Actividad principal
```

### Patrones

- **MVVM** - Model-View-ViewModel para separación de responsabilidades
- **Repository Pattern** - Acceso a datos centralizado
- **Compose** - UI declarativa y reactiva
- **StateFlow** - Manejo reactivo del estado

## Pantallas

### 1. Login
- Autenticación de usuario
- Validación de credenciales
- Opción de registro

### 2. Home (Inventario)
- Lista de todos los productos
- Búsqueda y filtros
- Botón flotante para agregar productos
- Vista de productos en tiempo real

### 3. Detalle de Producto
- Información completa del producto
- **Modo Edición**: Editar nombre, descripción y precio
- **Modo Lectura**: Ver detalles con botones de acción
- Botones para:
  - Ajustar Stock (cambiar cantidad manualmente)
  - Vender (restar cantidad)
  - Comprar (sumar cantidad)
  - Eliminar producto

### 4. Perfil
- Información del usuario
- Opción de logout

## Estructura de Datos

### Product (Producto)
```kotlin
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val description: String,
    val sku: String,
    val currentStock: Int,
    val minStock: Int,
    val priceClp: Double
)
```

### User (Usuario)
```kotlin
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val username: String,
    val email: String,
    val password: String
)
```

## Flujo de Datos

```
UI (Compose) 
  ↓
ViewModel (StateFlow)
  ↓
Repository (Abstracción de datos)
  ↓
Room DAO (Acceso a BD)
  ↓
SQLite (Base de datos local)
```

## Personalización

### Temas
Los temas se definen en `ui/theme/`:
- `Color.kt` - Paleta de colores
- `Type.kt` - Tipografía
- `Theme.kt` - Tema principal

---

**Última actualización**: Octubre 2025
**Versión**: 1.0
**Estado**: En desarrollo
