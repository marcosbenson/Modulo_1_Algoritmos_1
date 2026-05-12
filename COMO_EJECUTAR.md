# Cómo ejecutar 1982

## Requisitos previos

- **Java 8 o superior** instalado (`java` y `javac` disponibles en la terminal)
- **Processing core.jar** ya incluido en `lib/` (no hace falta instalar nada más)

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

## Opción 1: Ejecutar desde la terminal (recomendado)

### Linux / macOS

```bash
# Dar permisos de ejecución (solo la primera vez)
chmod +x run.sh

# Ejecutar
./run.sh
```

### Windows

```cmd
run.bat
```

### Qué hace el script

1. Limpia la carpeta `out/` (compilados anteriores)
2. Copia los recursos (imágenes, fuentes) a `out/`
3. Compila todos los `.java` contra `lib/core.jar`
4. Ejecuta `Home.GameApp` (el punto de entrada del juego)

---

## Opción 2: Ejecutar desde Processing IDE

El proyecto usa **paquetes Java** (`package Contrato`, `package Home`, etc.) para organizar el código
según los principios de POO. Por eso no se puede abrir directamente como un sketch `.pde`.

Para ejecutarlo desde Processing IDE, seguí estos pasos:

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

Copiar todo el contenido de `src/main/java/` dentro de la carpeta del sketch:

```bash
# Desde la raiz del proyecto
cp -r src/main/java/* ~/sketchbook/Game1982/
# o donde sea que Processing haya guardado el sketch
```

La carpeta del sketch debería quedar así:

```
Game1982/
├── Game1982.pde          ← archivo principal (lo editamos en el paso 5)
├── Contrato/             ← paquete del contrato
│   ├── ModuloJuego.java
│   ├── EstadoJuego.java
│   └── ...
├── Home/                 ← paquete del Home
│   ├── GameApp.java
│   ├── HomeJuego.java
│   └── ui/
│       └── ...
└── Aviones/              ← paquete de aviones
    └── AvionPrueba/
        └── ModuloPrueba.java
```

### Paso 5: Editar el archivo .pde

Reemplazar el contenido de `Game1982.pde` con:

```java
// Punto de entrada para Processing IDE.
// La logica real esta en Home/GameApp.java.
// Este archivo solo lanza la aplicacion.

void setup() {
  // Processing IDE requiere un setup() en el .pde,
  // pero nosotros usamos PApplet.main() en GameApp.
}

// Para ejecutar: usar Run (boton Play) o el menu Sketch > Run
// NOTA: si no funciona directamente, ejecutar desde terminal con run.sh
```

> **Nota importante**: Processing IDE tiene limitaciones con proyectos que usan paquetes Java
> en subdirectorios. Si el botón Play no funciona, usá la terminal con `./run.sh`.
> El script usa exactamente la misma librería `core.jar` de Processing.

### Paso 6: Copiar la fuente

Copiar la fuente al directorio `data/` del sketch:

```bash
mkdir -p ~/sketchbook/Game1982/data
cp src/main/Resources/fonts/PressStart2P-Regular.ttf ~/sketchbook/Game1982/data/
```

### Paso 7: Ejecutar

Hacer clic en el botón **▶ Play** del IDE de Processing, o usar `Sketch` → `Run`.

> Si Processing IDE no reconoce los paquetes, la forma más confiable es ejecutar
> desde la terminal con `./run.sh` (Linux/macOS) o `run.bat` (Windows).
> Ambos métodos usan Processing `core.jar` — es el mismo resultado.

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
