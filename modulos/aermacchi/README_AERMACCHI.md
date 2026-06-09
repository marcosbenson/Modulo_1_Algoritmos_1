# Módulo Aermacchi MB-339

**Avión:** MB-339 Aermacchi  
**Clase principal:** `ModuloAermacchi`

## Descripción
Shooter vertical de defensa aérea. Piloteá el MB-339 para derribar cazas enemigos, barcos de guerra y un portaviones jefe.

## Controles
| Tecla | Acción |
|-------|--------|
| Flechas / WASD | Mover avión |
| Espacio | Disparar |
| P | Pausar / Reanudar |
| M (en pausa) | Volver al menú |

## Power-ups
- ❤️ Vida: suma una vida
- 🛡️ Invulnerable: 5 segundos de invulnerabilidad
- ⚡ Rápido: disparo con mayor cadencia
- 🔱 Triple: disparo triple

## Enemigos
- **Caza Enemigo** — rápido, dispara hacia abajo
- **Caza Pesado** — resistente, disparo doble
- **Batería Enemiga** — barco, dispara en abanico hacia el jugador
- **Portaviones** (jefe) — aparece cada 10.000 pts, dispara en abanico amplio

## Assets necesarios en `data/`
Renombrá tus imágenes originales con el prefijo `aermacchi_`:

| Nombre esperado | Imagen original |
|---|---|
| `aermacchi_avion.png` | `aermacchi.png` |
| `aermacchi_caza_enemigo.png` | `CazaEnemigo.png` |
| `aermacchi_caza_pesado.png` | `CazaPesado.png` |
| `aermacchi_barco_enemigo.png` | `BarcoEnemigo.png` |
| `aermacchi_jefe.png` | `Jefe.png` |
| `aermacchi_fondo_menu.png` | `FondoMenu.png` |
| `aermacchi_corazon.png` | `Corazon.png` |
| `aermacchi_escudo.png` | `Escudo.png` |
| `aermacchi_rapido.png` | `Rapido.png` |
| `aermacchi_triple.png` | `Triple.png` |

## Integración con el Lobby
El lobby debe agregar en `Game1982.pde`:
```java
homeJuego.registrarModulo(new ModuloAermacchi());
```

## Input del módulo
El módulo expone métodos públicos para input que el lobby puede conectar:
```java
modulo.onKeyPressed(keyCode, key);
modulo.onKeyReleased(keyCode, key);
modulo.onMousePressed(mouseX, mouseY, this);
```
