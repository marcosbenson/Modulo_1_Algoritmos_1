# Modulo 1 - Lobby del Juego
## Módulos del juego (por grupo)

| Módulo | Descripción |
|--------|-------------|
| Lobby General | Home y pantalla de inicio del juego |
| Avión Skyhawk | Armada Argentina |
| Avión Pucará | Ejército Argentino |
| Avión Aermacchi MB-339 | Piloteado por Owen Crippa |
| Avión Mirage | Fuerza Aérea Argentina |
| Avión Super Etendard | Armada Argentina |
| Tutorial Interactivo | Tutorial de juego |

---
## Cronograma de entregas

| Fecha | Tipo | Entregables |
|-------|------|-------------|
| 05/05/2026 | Seguimiento I | Documentación de Análisis |
| 19/05/2026 | Seguimiento II | Documentación de Diseño |
| 09/06/2026 | Final | Todos los entregables + defensa oral |

> Los seguimientos parciales no son obligatorios pero impactan en el criterio de organización del grupo.

---

## Entregables

### Documentación de Análisis (`docs/analisis/`)

Contenidos mínimos:

- **p1.** Objetivo y Alcance
- **p2.** Descripción de alto nivel del sistema
- **p3.** Requerimientos funcionales más relevantes
- **p4.** Requerimientos no funcionales

### Documentación de Diseño (`docs/diseño/`)

El sistema debe diseñarse bajo el paradigma orientado a objetos contemplando:

- **Extensibilidad** — el modelo debe poder extenderse fácilmente
- **Mantenibilidad** — evitar duplicación de código y desacoplar subsistemas
- **Gestión de errores** — jerarquía de excepciones que capture todos los errores en tiempo de ejecución

Contenidos mínimos:

- **p5.** Diagrama Conceptual
- **p6.** Diagrama de clases del Modelo
- **p7.** Diagrama de la Vista y Controlador
- **p8.** Diagrama de Secuencia de al menos 5 Casos de Uso (alta interacción entre clases)
- **p9.** Diagrama de Navegación de GUI

Contenidos opcionales:
- Diagrama de clases de un módulo o subsistema relevante
- Diagrama de secuencia con su diagrama de clases asociado
- Diagrama de estados con su diagrama de clases asociado

> Cada vista se compone de diagrama + descripción. Las vistas deben ser consistentes entre sí.

### Código fuente (`src/`)

- Implementación en Java (versión 8 o superior)
- El código compila sin errores
- La ejecución no arroja excepciones inesperadas
- Documentación de uso (se puede usar Javadoc)
- Se valoran pruebas unitarias con JUnit

### Presentación (defensa oral)

Contenidos recomendados:
- Presentación de integrantes y responsabilidades
- Breve descripción del análisis del problema
- Detalle de la solución diseñada (al menos 1 caso de uso de los p5–p9)
- Problemas encontrados y cómo se resolvieron
- Posibles mejoras pendientes
- Estrategias para incorporar nueva funcionalidad
- Demostración funcional con un caso de uso
- Conclusiones

> **Todos los integrantes deben estar presentes.** Los docentes pueden preguntar a cualquier integrante sobre cualquier aspecto del trabajo.

---

## Criterios de evaluación

### Evaluación grupal

| Aspecto | Regular (1) | Bueno (2) | Muy Bueno (3) | Excelente (4) |
|---------|-------------|-----------|---------------|---------------|
| **Análisis del problema** | No se interpreta correctamente o no hay documentación | Documentación suficiente y acorde a lo solicitado | Completa y argumentada, relacionando temas de clase | Evidencia bibliografía e investigación adicional |
| **Diseño en POO** | Incompleto o no acorde al análisis | Solución básica en POO | Completo, con conceptos de diseño, extensible y mantenible | Usa patrones de diseño, reflexiona sobre principios y atributos de calidad |
| **Implementación** | No compila o arroja errores inesperados | Resuelve el problema sin errores en compilación/ejecución | Incorpora buenas prácticas y está alineado al diseño | Conocimiento profundo del lenguaje, eficiencia o pruebas unitarias |
| **Organización y presentación** | Fuera de término o formato inconsistente | Formato consistente y presentación preparada | Usa herramientas de colaboración, versionado y organización | Gestiona el ciclo de vida del trabajo |

### Evaluación individual

| Aspecto | Regular (1) | Bueno (2) | Muy Bueno (3) | Excelente (4) |
|---------|-------------|-----------|---------------|---------------|
| **Participación en el grupo** | Sin evidencia de participación | Plantea ideas o escucha a los demás | Participa activamente en discusiones y resolución | Contribuye proactivamente y fomenta la colaboración |
| **Defensa del trabajo** | No asiste o no participa | Expone su rol y aportes con claridad | Responde con seguridad las preguntas | Relaciona conceptos de la materia y argumenta decisiones de diseño |

---
