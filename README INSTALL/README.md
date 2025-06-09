# Proyecto Nimesuki

Este proyecto incluye una base de datos en MySQL, un servicio REST, una aplicación de escritorio y una app móvil. Todos los archivos necesarios (código fuente, ejecutables `.apk`, `.jar`, `.exe`, setup y scripts de base de datos) están disponibles en el repositorio de GitHub.

---

## ✅ Requisitos Generales

- **Sistema operativo**: Windows, macOS o Linux  
- **Memoria RAM**: 8GB (16GB recomendado)  
- **Espacio disponible**: 2GB  
- **Virtualización**: VirtualBox

---

## 🗃️ Base de Datos (MySQL en Máquina Virtual)

1. Iniciar una máquina virtual en modo **"Host Only"** con la ip asignada.
2. Instalar **MySQL 8 o compatible**.
3. Editar el archivo `my.cnf` o `my.ini` y en la sección `[mysqld]` agregar:
   ```ini
   bind-address = 0.0.0.0
   ```
4. Crear un usuario en MySQL con **host `%`** y **todos los privilegios**.
5. Si hay errores al modificar el archivo, asignar permisos adecuados (cambiar propietario o dar control total sobre el archivo).

---

## 🌐 Servicio REST

- **Requisitos**:
  - JDK 17 o superior
  - Puerto libre: 8088
  - Acceso a red con la base de datos

- **Ejecución**:
  - Desde un IDE (NetBeans, IntelliJ, etc.)
  - O mediante el archivo `.jar` del repositorio

- **Nota**: Si se ejecuta en la máquina virtual, usar la misma IP que la base de datos en la app móvil (desde código).

---

## 💻 Aplicación de Escritorio

- **Requisitos**:
  - JDK 17 o superior

- **Ejecución**:
  - Desde un IDE compatible
  - O usando el ejecutable `.jar`, `.exe` o intalar el '.exe' con el setup (los report por ahora solo funcionan en código)

- **Configuración**:
  - La conexión se introduce desde la app y se guarda en un archivo `config.properties`.
  - Los valores deben ser reintroducidos o guardados manualmente en cada sesión.

---

## 📱 Aplicación Móvil

- **Requisitos**:
  - Android Studio o similar (IDE recomendado)
  - Android 13 o superior
  - 200MB de espacio libre

- **Ejecución**:
  - Instalar el `.apk` en dispositivo físico (vía USB con ADB reverse)
  - O ejecutarlo en un emulador Android

- **Notas**:
  - En emuladores, usar IP `10.0.2.2` para referirse al host.
  - Si el servicio REST está en la MV, usar la misma IP que la base de datos.
  - La app solicita los datos de conexión en el login y los guarda en preferencias.

---
