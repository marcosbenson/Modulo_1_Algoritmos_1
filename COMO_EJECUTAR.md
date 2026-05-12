# Cómo ejecutar 1982

## Requisitos previos

- **Java 8 o superior** instalado (`java` y `javac` disponibles en la terminal)
- **Processing core.jar** ya incluido en `lib/` (no hace falta instalar nada más)

Para verificar que Java está instalado, ejecutá en la terminal:

```bash
java -version
javac -version
```

Si no aparece la versión, instalá Java:
- **Ubuntu/Debian**: `sudo apt install default-jdk`
- **macOS**: `brew install openjdk`
- **Windows**: Descargar desde [adoptium.net](https://adoptium.net/)

---

## Ejecutar el juego

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

---

## Qué hace el script

1. Limpia la carpeta `out/` (compilados anteriores)
2. Copia los recursos (imágenes, fuentes) a `out/`
3. Compila todos los `.java` contra `lib/core.jar`
4. Ejecuta `Home.GameApp` (el punto de entrada del juego)

---

## Problemas comunes

| Problema | Solución |
|----------|----------|
| `javac: command not found` | Instalar Java JDK (ver arriba) |
| `Error: Could not find or load main class` | Asegurate de ejecutar el script desde la raíz del proyecto |
| Pantalla negra sin nada | Verificar que `src/main/Resources/fonts/` contiene la fuente TTF |
| El juego no cierra | Cerrar la ventana con la X o presionar ESC |

---

## Estructura rápida

```
run.sh / run.bat     ← ejecutar esto
lib/core.jar         ← librería de Processing (incluida)
src/main/java/       ← código fuente
src/main/Resources/  ← imágenes y fuentes
out/                 ← se genera al compilar (ignorado por git)
```
