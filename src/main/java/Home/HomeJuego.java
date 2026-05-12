package Home;

import processing.core.PApplet;
import Contrato.ModuloJuego;
import Contrato.ContextoJuego;
import Home.ui.*;
import java.util.List;

/**
 * Orquestador principal del Home/Lobby.
 * Coordina gestores, navegacion, pantallas y ciclo de vida de modulos.
 * Implementa IModuloObserver para recibir eventos de los modulos.
 */
public class HomeJuego implements IModuloObserver {

    private PApplet app;
    private GestorModulos gestorModulos;
    private GestorEstadisticas gestorEstadisticas;
    private ControladorNavegacion controladorNav;

    // Pantallas
    private PantallaInicio pantallaInicio;
    private PantallaSeleccion pantallaSeleccion;
    private PantallaEstadisticas pantallaEstadisticas;

    // Modulo actualmente en ejecucion (null si no hay ninguno)
    private ModuloJuego moduloActual;

    // Temporizador para el stub de prueba (cuanto falta para finalizar)
    private int tiempoJuegoFrames;

    public HomeJuego(PApplet app) {
        this.app = app;
        this.controladorNav = new ControladorNavegacion();

        // Crear repositorio de estadisticas en carpeta "estadisticas/"
        RepositorioEstadisticas repositorio = new RepositorioEstadisticasArchivo("estadisticas");
        this.gestorEstadisticas = new GestorEstadisticas(repositorio);
        this.gestorModulos = new GestorModulos();
    }

    /** Registra un modulo en el gestor */
    public void registrarModulo(ModuloJuego modulo) {
        gestorModulos.agregarModulo(modulo);
    }

    /** Inicializa el Home y prepara las pantallas */
    public void iniciarHome() {
        pantallaInicio = new PantallaInicio(app.width, app.height);
        pantallaSeleccion = new PantallaSeleccion(
            gestorModulos.listarModulos(), app.width, app.height
        );
        pantallaEstadisticas = new PantallaEstadisticas(app.width, app.height);
    }

    /** Se llama cada frame desde draw() */
    public void dibujar() {
        switch (controladorNav.getPantallaActual()) {
            case INICIO:
                pantallaInicio.dibujar(app);
                break;
            case SELECCION:
                pantallaSeleccion.dibujar(app);
                break;
            case ESTADISTICAS:
                List<EstadisticasGenerales> stats = gestorEstadisticas.obtenerTodas();
                int total = gestorEstadisticas.calcularPuntajeTotalCurso();
                pantallaEstadisticas.dibujar(app, stats, total);
                break;
            case JUEGO:
                dibujarJuego();
                break;
        }
    }

    /** Dibuja la pantalla de juego (stub simple) */
    private void dibujarJuego() {
        app.background(0);

        if (moduloActual == null) {
            controladorNav.irSeleccionModulo();
            return;
        }

        // Pantalla de "juego" simple
        app.fill(0, 255, 0);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.textSize(20);
        app.text("JUGANDO: " + moduloActual.getNombreAvion(), app.width / 2, 60);

        // Barra de progreso del tiempo de juego
        tiempoJuegoFrames--;
        float progreso = 1.0f - ((float) tiempoJuegoFrames / (5 * 60)); // 5 segundos a 60fps
        app.fill(0, 80, 0);
        app.rectMode(PApplet.CORNER);
        app.rect(100, app.height / 2 - 15, app.width - 200, 30);
        app.fill(0, 255, 0);
        app.rect(100, app.height / 2 - 15, (app.width - 200) * progreso, 30);

        app.fill(0, 200, 0);
        app.textSize(10);
        app.text("Simulacion de partida en curso...", app.width / 2, app.height / 2 + 40);
        app.text("ESC para volver al menu", app.width / 2, app.height / 2 + 60);

        // Auto-finalizar cuando se acaba el tiempo
        if (tiempoJuegoFrames <= 0) {
            finalizarModuloActual();
        }
    }

    /** Maneja clics del mouse */
    public void manejarClick(float mouseX, float mouseY) {
        switch (controladorNav.getPantallaActual()) {
            case INICIO:
                if (pantallaInicio.clicEnStart(mouseX, mouseY)) {
                    controladorNav.irSeleccionModulo();
                }
                break;
            case SELECCION:
                String nombreModulo = pantallaSeleccion.clicEnModulo(mouseX, mouseY);
                if (nombreModulo != null) {
                    seleccionarModulo(nombreModulo);
                }
                if (pantallaSeleccion.clicEnEstadisticas(mouseX, mouseY)) {
                    controladorNav.irEstadisticas();
                }
                if (pantallaSeleccion.clicEnVolver(mouseX, mouseY)) {
                    controladorNav.irHome();
                }
                break;
            case ESTADISTICAS:
                if (pantallaEstadisticas.clicEnVolver(mouseX, mouseY)) {
                    controladorNav.irSeleccionModulo();
                }
                break;
            case JUEGO:
                // No hay botones en la pantalla de juego
                break;
        }
    }

    /** Maneja teclas */
    public void manejarTecla(char key, int keyCode) {
        // ESC vuelve al menu desde el juego
        if (key == 27 || key == PApplet.ESC) { // ESC
            if (controladorNav.getPantallaActual() == Pantalla.JUEGO) {
                finalizarModuloActual();
                // Evitar que Processing cierre la ventana con ESC
                app.key = 0;
            }
        }
    }

    /** Selecciona y arranca un modulo */
    public void seleccionarModulo(String nombreModulo) {
        try {
            moduloActual = gestorModulos.buscarModulo(nombreModulo);
            moduloActual.agregarObserver(this);

            // Crear contexto e inicializar
            ContextoJuego contexto = new ContextoJuego("Jugador", app.width, app.height);
            moduloActual.inicializarContexto(contexto);
            moduloActual.iniciar();

            // Temporizador: 5 segundos a 60fps
            tiempoJuegoFrames = 5 * 60;

            controladorNav.iniciarModulo(moduloActual);
        } catch (ModuloNoEncontradoException e) {
            System.err.println(e.getMessage());
        }
    }

    /** Finaliza el modulo actual y guarda estadisticas */
    private void finalizarModuloActual() {
        if (moduloActual != null) {
            moduloActual.finalizar();

            // Guardar estadisticas
            EstadisticasGenerales stats = moduloActual.getEstadisticasGenerales();
            if (stats != null) {
                gestorEstadisticas.guardarEstadisticas(stats);
            }

            moduloActual.removerObserver(this);
            moduloActual = null;
        }
        controladorNav.irSeleccionModulo();
    }

    /** Muestra las estadisticas generales */
    public void mostrarEstadisticasGenerales() {
        controladorNav.irEstadisticas();
    }

    /** Cierra la aplicacion */
    public void salir() {
        app.exit();
    }

    // --- IModuloObserver ---

    public void onEventoModulo(ModuloEvento evento) {
        System.out.println("[Home] Evento recibido: " + evento.getTipo()
            + " de " + evento.getNombreModulo());

        if (evento.getTipo() == ModuloEvento.Tipo.FINALIZADO) {
            finalizarModuloActual();
        }
        if (evento.getTipo() == ModuloEvento.Tipo.ERROR) {
            System.err.println("[Home] Error en modulo: " + evento.getMensaje());
            finalizarModuloActual();
        }
    }
}
