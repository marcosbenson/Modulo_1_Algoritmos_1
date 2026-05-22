# Cómo ejecutar 1982

## Requisitos previos

- **Java 8 o superior** instalado (`java` y `javac` disponibles en la terminal)

Para verificar que Java está instalado:

```bash
java -version
javac -version
```

Si no aparece la versión, instalá Java:
- **Ubuntu/Debian**: `sudo apt install default-jdk`
- **macOS**: `brew install openjdk`
- **Windows**: Descargar desde [adoptium.net](https://adoptium.net/)

---

## Controles del juego

| Tecla | Acción |
|-------|--------|
| **ENTER** | Seleccionar / Confirmar |
| **ESC** | Volver / Salir del juego |
| **W** / **S** | Navegar menús (arriba/abajo) |
| **W** / **A** / **S** / **D** | Mover avión (en juego) |
| **ESPACIO** | Disparar (en juego) |

También se puede usar el **mouse** para hacer clic en los botones de los menús.

---


## Ejecutar desde Processing IDE

### Paso 1: Abrir Processing IDE

Abrir la aplicación Processing. Si no la tenés:
- **Linux**: `sudo snap install processing` o descargar de [processing.org](https://processing.org/download)
- **Windows/macOS**: Descargar de [processing.org](https://processing.org/download)

### Paso 2: Cambiar a Modo Java

1. En la esquina superior derecha del IDE, donde dice **"Java"**, verificar que esté seleccionado.
2. Si dice otra cosa (p5.js, Python), hacer clic y seleccionar **Java**.

### Paso 3: Crear un nuevo sketch

1. `Archivo` → `Nuevo`
2. `Archivo` → `Guardar como...` → Guardar con el nombre **`Game1982`**
3. Processing va a crear una carpeta `Game1982/` con un archivo `Game1982.pde`

### Paso 4: Copiar los archivos del proyecto

Copiar todo el contenido del branch `processing-ide` dentro de la carpeta del sketch


### Paso 5: Ejecutar

Hacer clic en el botón **▶ Play** del IDE de Processing, o usar `Sketch` → `Run`.



---

## Problemas comunes

| Problema | Solución |
|----------|----------|
| `javac: command not found` | Instalar Java JDK (ver arriba) |
| `Error: Could not find or load main class` | Ejecutar el script desde la raíz del proyecto |
| Pantalla negra sin nada | Verificar que `src/main/Resources/fonts/` contiene la fuente TTF |
| El juego no cierra | Cerrar la ventana con la X |
| ESC no funciona | Hacer clic primero en la ventana del juego para que tenga foco |

---

## Estructura rápida

```
run.sh / run.bat     ← ejecutar esto
lib/core.jar         ← librería de Processing (incluida)
src/main/java/       ← código fuente
src/main/Resources/  ← imágenes y fuentes
out/                 ← se genera al compilar (ignorado por git)
```
