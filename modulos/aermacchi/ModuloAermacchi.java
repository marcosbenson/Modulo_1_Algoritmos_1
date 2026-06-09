import processing.core.*;
import java.util.*;

public class ModuloAermacchi implements ModuloJuego {

  // ── Estados internos del módulo ─────────────────────────────────────────────
  static final int ESTADO_MENU        = 0;
  static final int ESTADO_JUGANDO     = 1;
  static final int ESTADO_GAME_OVER   = 2;
  static final int ESTADO_ESTADISTICAS = 3;
  static final int ESTADO_CONTROLES   = 4;

  int estadoJuegoInterno = ESTADO_MENU;
  int opcionSeleccionada = 0;

  // ── Contrato ModuloJuego ────────────────────────────────────────────────────
  private EstadoJuego        estadoActual;
  private ContextoJuego      contexto;
  private List<IModuloObserver> observers;
  private AermacchiEstadisticasModulo estadisticas;
  private AermacchiEstadisticasGenerales estadisticasGenerales;

  // ── Elementos del juego ─────────────────────────────────────────────────────
  AermacchiJugador jugador;
  ArrayList<AermacchiElemento> elementos;
  AermacchiGestorColisiones gestorColisiones;

  boolean pausado;
  boolean jefeActivo;
  int proximoUmbralJefe;

  boolean izquierda, derecha, arriba, abajo, disparo;
  PImage imgFondoMenu;

  private long tiempoInicio;
  private boolean listenerRegistrado = false;
  private PApplet appRef = null;

  // ── Constructor ─────────────────────────────────────────────────────────────
  public ModuloAermacchi() {
    estadoActual          = new NoIniciadoState();
    observers             = new ArrayList<>();
    elementos             = new ArrayList<>();
    gestorColisiones      = new AermacchiGestorColisiones();
    estadisticas          = new AermacchiEstadisticasModulo();
    estadisticasGenerales = new AermacchiEstadisticasGenerales();
  }

  public ModuloAermacchi(PApplet app) {
    this(); // delega al constructor principal
  }

  // ── Contrato: identidad ──────────────────────────────────────────────────────
  public String getNombreModulo() { return "MB339 Aermacchi"; }
  public String getDescripcion()  { return "Shooter de aviones: defiende el cielo con el MB-339"; }
  public String getNombreAvion()  { return "MB-339 Aermacchi"; }

  // ── Contrato: ciclo de vida ──────────────────────────────────────────────────
  public void inicializarContexto(ContextoJuego ctx) {
    this.contexto = ctx;
  }

  public void iniciar() throws EstadoInvalidoException {
    estadoActual.iniciar(this);
    estadoActual  = new IniciandoState();
    tiempoInicio  = System.currentTimeMillis();
    pausado       = false;
    // El lobby ya mostró su menú y eligió este módulo → arrancamos directo al juego.
    // ESTADO_MENU propio del módulo queda reservado solo para uso interno (estadísticas/controles).
    estadoJuegoInterno = ESTADO_JUGANDO;
    notificar(new ModuloEvento(ModuloEvento.Tipo.INICIADO, getNombreModulo(), "Iniciando..."));
    estadoActual = new EnEjecucionState();
  }

  public void pausar() throws EstadoInvalidoException {
    estadoActual.pausar(this);
    estadoActual = new PausadoState();
    pausado = true;
    notificar(new ModuloEvento(ModuloEvento.Tipo.PAUSADO, getNombreModulo()));
  }

  public void reanudar() throws EstadoInvalidoException {
    estadoActual.reanudar(this);
    estadoActual = new EnEjecucionState();
    pausado = false;
    notificar(new ModuloEvento(ModuloEvento.Tipo.REANUDADO, getNombreModulo()));
  }

  public void finalizar() throws EstadoInvalidoException {
    estadoActual.finalizar(this);
    estadoActual = new FinalizadoState();
    notificar(new ModuloEvento(ModuloEvento.Tipo.FINALIZADO, getNombreModulo()));
  }

  private void desregistrarListener(PApplet app) {
    if (app != null && listenerRegistrado) {
      try { app.unregisterMethod("keyEvent", this); } catch (Exception ignored) {}
      if (appRef != null && listenerRegistrado) {
      try { appRef.unregisterMethod("keyEvent", this); } catch (Exception ignored) {}
    }
    listenerRegistrado = false;
    appRef = null;
    }
  }

  public EstadoJuego getEstado() { return estadoActual; }

  public EstadisticasGenerales getEstadisticasGenerales() {
    long tiempoSeg = tiempoInicio > 0
        ? (System.currentTimeMillis() - tiempoInicio) / 1000 : 0;
    return new EstadisticasGenerales(
        getNombreModulo(),
        estadisticasGenerales.puntajeTotal,
        estadisticasGenerales.partidasJugadas,
        estadisticasGenerales.partidasGanadas,
        estadisticasGenerales.partidasPerdidas,
        estadisticasGenerales.enemigosDestruidos,
        tiempoSeg
    );
  }

  public void agregarObserver(IModuloObserver obs)  { observers.add(obs); }
  public void removerObserver(IModuloObserver obs)   { observers.remove(obs); }

  public void reset() {
    estadoActual          = new NoIniciadoState();
    pausado               = false;
    jefeActivo            = false;
    tiempoInicio          = 0;
    estadoJuegoInterno    = ESTADO_MENU;
    opcionSeleccionada    = 0;
    jugador               = null;
    elementos.clear();
    estadisticas          = new AermacchiEstadisticasModulo();
    estadisticasGenerales = new AermacchiEstadisticasGenerales();
    observers.clear();
    if (appRef != null && listenerRegistrado) {
      try { appRef.unregisterMethod("keyEvent", this); } catch (Exception ignored) {}
    }
    listenerRegistrado = false;
    appRef = null;
  }

  // ── Contrato: loop ───────────────────────────────────────────────────────────
  public void actualizar(PApplet app) {
    if ("FINALIZADO".equals(estadoActual.getNombre())) return;
    if (pausado) return;
    if (estadoJuegoInterno != ESTADO_JUGANDO) return;

    // Inicializar la partida la primera vez (cubre arranque directo y re-intentos)
    if (!listenerRegistrado) {
      try { app.unregisterMethod("keyEvent", this); } catch (Exception ignored) {}
      app.registerMethod("keyEvent", this);
      listenerRegistrado = true;
      appRef = app;
    }
    if (jugador == null) inicializarPartida(app);

    moverJugador();
    if (disparo) dispararJugador(app);

    actualizarEstadoJefe();
    verificarSpawnJefe(app);
    if (!jefeActivo) generarEnemigos(app);

    for (int i = 0; i < elementos.size(); i++) elementos.get(i).actualizar(app);
    gestorColisiones.verificarColisiones(elementos, estadisticas, app);
    eliminarInactivos();
    actualizarEstadoJefe();
    verificarFinPartida();
  }

  public void dibujar(PApplet app) {

    switch (estadoJuegoInterno) {
      case ESTADO_MENU:         dibujarJuego(app);        break; // lobby maneja su propio menú
      case ESTADO_JUGANDO:      dibujarJuego(app);        break;
      case ESTADO_GAME_OVER:    dibujarGameOver(app);     break;
      case ESTADO_ESTADISTICAS: dibujarEstadisticas(app); break;
      case ESTADO_CONTROLES:    dibujarControles(app);    break;
    }
  }

  // ── Inicialización de partida ────────────────────────────────────────────────
  void inicializarPartida(PApplet app) {
    elementos.clear();
    jugador = new AermacchiJugador(app.width / 2, app.height - 120, app);
    elementos.add(jugador);
    estadisticas.reiniciarEstadisticas();
    proximoUmbralJefe = 10000;
    jefeActivo = false;
    izquierda = derecha = arriba = abajo = disparo = false;
  }

  // ── Lógica del juego ─────────────────────────────────────────────────────────
  void moverJugador() {
    if (jugador == null) return;
    if (izquierda) jugador.mover("izquierda");
    if (derecha)   jugador.mover("derecha");
    if (arriba)    jugador.mover("arriba");
    if (abajo)     jugador.mover("abajo");
  }

  void dispararJugador(PApplet app) {
    if (jugador == null || !jugador.puedeDisparar(app)) return;
    if (jugador.tieneDisparoTriple) {
      float xc = jugador.x + jugador.ancho / 2 - 4;
      float ys = jugador.y;
      elementos.add(new AermacchiProyectilAliado(xc,  ys, 0,    -8));
      elementos.add(new AermacchiProyectilAliado(xc,  ys, -2.5f, -7.5f));
      elementos.add(new AermacchiProyectilAliado(xc,  ys,  2.5f, -7.5f));
      jugador.tiempoUltimoDisparo = app.millis();
      estadisticas.registrarDisparo(false);
    } else {
      AermacchiProyectilAliado p = jugador.disparar(app);
      elementos.add(p);
      estadisticas.registrarDisparo(false);
    }
  }

  void generarEnemigos(PApplet app) {
    if (app.frameCount % 90   == 0)  elementos.add(new AermacchiCazaEnemigo(app.random(40, app.width - 40), -40, app, this));
    if (app.frameCount % 1050 == 0)  elementos.add(new AermacchiBateriaEnemiga(app.random(60, app.width - 60), -50, app, this));
    if (app.frameCount % 450  == 0)  elementos.add(new AermacchiCazaPesado(app.random(90, app.width - 90), -80, app, this));
  }

  void actualizarEstadoJefe() {
    jefeActivo = false;
    for (AermacchiElemento e : elementos) {
      if (e instanceof AermacchiJefe && e.estaActivo()) { jefeActivo = true; return; }
    }
  }

  void verificarSpawnJefe(PApplet app) {
    if (jefeActivo) return;
    while (estadisticas.puntajeTotal >= proximoUmbralJefe) {
      elementos.add(new AermacchiJefe(app.width / 2f, app.height, app, this));
      proximoUmbralJefe += 10000;
      jefeActivo = true;
      break;
    }
  }

  void eliminarInactivos() {
    for (int i = elementos.size() - 1; i >= 0; i--) {
      if (!elementos.get(i).estaActivo()) elementos.remove(i);
    }
  }

  void verificarFinPartida() {
    if (jugador != null && jugador.estaDestruido()) {
      estadoJuegoInterno = ESTADO_GAME_OVER;
      opcionSeleccionada = 0;
      estadisticasGenerales.registrarDerrota();
      estadisticasGenerales.enemigosDestruidos += estadisticas.enemigosDerribados;
      if (estadisticas.puntajeTotal > estadisticasGenerales.puntajeTotal) {
        estadisticasGenerales.puntajeTotal = estadisticas.puntajeTotal;
      }
      // NO llamamos finalizar() aquí — el lobby volvería al menú sin mostrar game over.
      // finalizar() se llama desde seleccionarOpcionGameOver() cuando el jugador elige "VOLVER AL MENÚ".
    }
  }

  // ── Dibujo ───────────────────────────────────────────────────────────────────
  void dibujarJuego(PApplet app) {
    app.background(10, 20, 40);
    for (AermacchiElemento e : elementos) { if (e != jugador) e.dibujar(app); }
    if (jugador != null) jugador.dibujar(app);
    dibujarHUD(app);
    if (pausado) dibujarPausa(app);
  }

  void dibujarHUD(PApplet app) {
    AermacchiJefe jefeActual = null;
    for (AermacchiElemento e : elementos) {
      if (e instanceof AermacchiJefe && e.estaActivo()) { jefeActual = (AermacchiJefe) e; break; }
    }
    float hudY = 20;
    if (jefeActual != null && jefeActual.estaEnCombate()) {
      app.fill(255, 200, 80); app.textSize(18); app.textAlign(PApplet.CENTER);
      app.text("PORTAVIONES", app.width / 2f, hudY);
      float bw = 650, bh = 15, bx = (app.width - bw) / 2f, by = hudY + 20;
      float pct = jefeActual.vidaMaxima == 0 ? 0 : (float)jefeActual.vida / jefeActual.vidaMaxima;
      app.fill(60, 60, 60); app.noStroke(); app.rect(bx, by, bw, bh);
      int bc = pct < 0.30f ? app.color(255,60,60) : pct < 0.50f ? app.color(255,160,50) : app.color(80,220,120);
      app.fill(bc); app.rect(bx, by, bw * pct, bh);
      hudY = by + bh + 20;
    }
    app.fill(255); app.textSize(16); app.textAlign(PApplet.LEFT);
    if (jugador != null) app.text("Vidas: " + jugador.vida, 20, hudY);
    app.text("Puntaje: " + estadisticas.puntajeTotal, 20, hudY + 25);
  }

  void dibujarPausa(PApplet app) {
    app.fill(10, 15, 30, 200); app.rect(0, 0, app.width, app.height);
    app.textAlign(PApplet.CENTER, PApplet.CENTER); app.textSize(45);
    app.fill(255, 200, 50); app.text("JUEGO PAUSADO", app.width / 2f, app.height / 2f - 40);
    app.textSize(20); app.fill(200, 220, 255);
    app.text("Presiona P para Reanudar",      app.width / 2f, app.height / 2f + 20);
    app.text("Presiona M para Salir al Menu", app.width / 2f, app.height / 2f + 65);
  }

  void dibujarMenu(PApplet app) {
    if (imgFondoMenu != null) app.image(imgFondoMenu, 0, 0, app.width, app.height);
    else { app.background(10, 20, 40); }

    app.textSize(48); app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.fill(0, 40, 80, 150);
    app.text("MODULO MB-339",  app.width / 2f + 4, app.height * 0.26f + 4);
    app.text("AERMACCHI",      app.width / 2f + 4, app.height * 0.33f + 4);
    app.fill(255, 200, 50); app.text("MODULO MB-339", app.width / 2f, app.height * 0.26f);
    app.fill(255);            app.text("AERMACCHI",   app.width / 2f, app.height * 0.33f);

    String[] opciones = {"NUEVA PARTIDA", "ESTADISTICAS", "CONTROLES", "SALIR"};
    float startY = app.height * 0.48f, spacing = 65;
    for (int i = 0; i < opciones.length; i++) {
      float itemY = startY + i * spacing;
      boolean sel = (opcionSeleccionada == i);
      if (sel) {
        app.fill(255, 200, 50, 40); app.stroke(255, 200, 50, 150); app.strokeWeight(2);
        app.rect(app.width / 2f - 180, itemY - 28, 360, 40, 8);
        app.noStroke(); app.fill(255, 200, 50);
        float p = (float)Math.sin(app.frameCount * 0.1) * 4;
        app.triangle(app.width/2f-200-p, itemY-12, app.width/2f-210-p, itemY-18, app.width/2f-210-p, itemY-6);
        app.triangle(app.width/2f+200+p, itemY-12, app.width/2f+210+p, itemY-18, app.width/2f+210+p, itemY-6);
      }
      app.textAlign(PApplet.CENTER, PApplet.CENTER); app.textSize(24);
      app.fill(sel ? app.color(255,220,100) : app.color(180,200,230));
      app.text(opciones[i], app.width / 2f, itemY - 10);
    }
  }

  void dibujarEstadisticas(PApplet app) {
    if (imgFondoMenu != null) app.image(imgFondoMenu, 0, 0, app.width, app.height);
    else app.background(10, 20, 40);
    app.textAlign(PApplet.CENTER); app.textSize(34); app.fill(255, 200, 50);
    app.text("ESTADISTICAS HISTORICAS", app.width / 2f, app.height * 0.18f);
    app.stroke(100, 150, 200, 100); app.strokeWeight(2); app.fill(15, 25, 45, 200);
    app.rect(80, app.height * 0.25f, app.width - 160, app.height * 0.48f, 15);
    app.textAlign(PApplet.LEFT); app.textSize(20);
    float sy = app.height * 0.32f, sp = 45;
    app.fill(180, 200, 230);
    app.text("Partidas Jugadas:", 120, sy);
    app.text("Enemigos Derribados:", 120, sy + sp * 2);
    app.text("Maxima Puntuacion:", 120, sy + sp * 3);
    app.textAlign(PApplet.RIGHT); app.fill(255);
    app.text(estadisticasGenerales.partidasJugadas, app.width - 120, sy);
    app.text(estadisticasGenerales.partidasPerdidas, app.width - 120, sy + sp);
    app.text(estadisticasGenerales.enemigosDestruidos, app.width - 120, sy + sp * 2);
    app.fill(255, 200, 50);
    app.text(estadisticasGenerales.puntajeTotal + " pts", app.width - 120, sy + sp * 3);
    // Boton volver
    float btnY = app.height * 0.82f;
    app.fill(15, 25, 45, 150); app.stroke(100, 150, 200); app.strokeWeight(2);
    app.rect(app.width / 2f - 100, btnY - 22, 200, 38, 8);
    app.textAlign(PApplet.CENTER, PApplet.CENTER); app.textSize(20); app.fill(255);
    app.text("VOLVER", app.width / 2f, btnY - 6);
  }

  void dibujarControles(PApplet app) {
    if (imgFondoMenu != null) app.image(imgFondoMenu, 0, 0, app.width, app.height);
    else app.background(10, 20, 40);
    app.textAlign(PApplet.CENTER); app.textSize(34); app.fill(255, 200, 50);
    app.text("MANUAL DE CONTROLES", app.width / 2f, app.height * 0.18f);
    app.stroke(100, 150, 200, 100); app.strokeWeight(2); app.fill(15, 25, 45, 200);
    app.rect(80, app.height * 0.25f, app.width - 160, app.height * 0.48f, 15);
    app.textAlign(PApplet.LEFT); app.textSize(18);
    float sy = app.height * 0.32f, sp = 50;
    app.fill(255, 220, 100); app.text("MOVIMIENTO:", 120, sy);
    app.fill(180, 200, 230); app.text("Teclas FLECHAS o W, A, S, D", 120, sy + 25);
    app.fill(255, 220, 100); app.text("DISPARAR:", 120, sy + sp * 1.5f);
    app.fill(180, 200, 230); app.text("Tecla BARRA ESPACIADORA", 120, sy + sp * 1.5f + 25);
    app.fill(255, 220, 100); app.text("PAUSA:", 120, sy + sp * 3f);
    app.fill(180, 200, 230); app.text("Tecla P", 120, sy + sp * 3f + 25);
    float btnY = app.height * 0.82f;
    app.fill(15, 25, 45, 150); app.stroke(100, 150, 200); app.strokeWeight(2);
    app.rect(app.width / 2f - 100, btnY - 22, 200, 38, 8);
    app.textAlign(PApplet.CENTER, PApplet.CENTER); app.textSize(20); app.fill(255);
    app.text("VOLVER", app.width / 2f, btnY - 6);
  }

  void dibujarGameOver(PApplet app) {
    for (AermacchiElemento e : elementos) e.dibujar(app);
    app.fill(20, 5, 5, 210); app.noStroke(); app.rect(0, 0, app.width, app.height);
    app.textAlign(PApplet.CENTER); app.textSize(46);
    app.fill(120, 0, 0, 150); app.text("MODULO DESTRUIDO", app.width / 2f + 3, app.height * 0.22f + 3);
    app.fill(255, 80, 80);    app.text("MODULO DESTRUIDO", app.width / 2f,     app.height * 0.22f);
    app.stroke(255, 80, 80, 80); app.strokeWeight(2); app.fill(35, 15, 15, 200);
    app.rect(120, app.height * 0.32f, app.width - 240, app.height * 0.32f, 15);
    app.textAlign(PApplet.LEFT); app.textSize(18);
    float sy = app.height * 0.38f, sp = 35;
    app.fill(200, 180, 180);
    app.text("Puntaje Final:", 150, sy);
    app.text("Enemigos Derribados:", 150, sy + sp);
    app.text("Precision de Tiro:", 150, sy + sp * 2);
    app.textAlign(PApplet.RIGHT); app.fill(255, 200, 80);
    app.text(estadisticas.puntajeTotal + " pts", app.width - 150, sy);
    app.fill(255);
    app.text(estadisticas.enemigosDerribados, app.width - 150, sy + sp);
    app.text(PApplet.nf((float)estadisticas.calcularPrecision(), 1, 2) + "%", app.width - 150, sy + sp * 2);
    String[] opciones = {"REINTENTAR", "VOLVER AL MENU"};
    float startY = app.height * 0.70f, spacing = 60;
    for (int i = 0; i < opciones.length; i++) {
      float itemY = startY + i * spacing;
      boolean sel = (opcionSeleccionada == i);
      if (sel) {
        app.fill(255, 80, 80, 40); app.stroke(255, 80, 80, 150); app.strokeWeight(2);
        app.rect(app.width / 2f - 140, itemY - 22, 280, 36, 8);
      }
      app.textAlign(PApplet.CENTER, PApplet.CENTER); app.textSize(22);
      app.fill(sel ? app.color(255,120,120) : app.color(180,160,160));
      app.text(opciones[i], app.width / 2f, itemY - 6);
    }
  }

  // ── Contrato: input de teclado (delegado por HomeJuego) ─────────────────────

  /**
   * HomeJuego llama este método para cada tecla presionada,
   * excepto ESC y Q que son consumidos por el lobby.
   * Aquí manejamos tanto el juego en vuelo como los menús internos del módulo.
   */
  public void procesarTecla(int keyCode, char key) {
    if (estadoJuegoInterno == ESTADO_JUGANDO) {
      if (pausado) {
        // Teclas válidas mientras el módulo está en pausa interna (P)
        // Nota: ESC para pausa/reanudar lo maneja el lobby; P es pausa propia del módulo
        if (key == 'p' || key == 'P') pausado = false;
        if (key == 'm' || key == 'M') {
          pausado = false;
          try { finalizar(); } catch (EstadoInvalidoException e) { /* ok */ }
        }
      } else {
        if (keyCode == PApplet.LEFT  || key == 'a' || key == 'A') izquierda = true;
        if (keyCode == PApplet.RIGHT || key == 'd' || key == 'D') derecha   = true;
        if (keyCode == PApplet.UP    || key == 'w' || key == 'W') arriba    = true;
        if (keyCode == PApplet.DOWN  || key == 's' || key == 'S') abajo     = true;
        if (key == ' ') disparo = true;
        if (key == 'p' || key == 'P') pausado = true;
      }
    } else if (estadoJuegoInterno == ESTADO_MENU) {
      if (keyCode == PApplet.UP   || key == 'w' || key == 'W') opcionSeleccionada = (opcionSeleccionada - 1 + 4) % 4;
      if (keyCode == PApplet.DOWN || key == 's' || key == 'S') opcionSeleccionada = (opcionSeleccionada + 1) % 4;
      if (keyCode == PApplet.ENTER || key == '\n' || key == ' ') seleccionarOpcionMenu(null);
    } else if (estadoJuegoInterno == ESTADO_GAME_OVER) {
      if (keyCode == PApplet.UP   || key == 'w' || key == 'W') opcionSeleccionada = (opcionSeleccionada - 1 + 2) % 2;
      if (keyCode == PApplet.DOWN || key == 's' || key == 'S') opcionSeleccionada = (opcionSeleccionada + 1) % 2;
      if (keyCode == PApplet.ENTER || key == '\n' || key == ' ') seleccionarOpcionGameOver(null);
    } else if (estadoJuegoInterno == ESTADO_ESTADISTICAS || estadoJuegoInterno == ESTADO_CONTROLES) {
      estadoJuegoInterno = ESTADO_MENU;
      opcionSeleccionada = 0;
    }
  }

  /**
   * HomeJuego llama este método cuando el jugador suelta una tecla.
   * Imprescindible para detener el movimiento continuo de la nave.
   */
  public void procesarTeclaSoltada(int keyCode, char key) {
    if (estadoJuegoInterno == ESTADO_JUGANDO) {
      if (keyCode == PApplet.LEFT  || key == 'a' || key == 'A') izquierda = false;
      if (keyCode == PApplet.RIGHT || key == 'd' || key == 'D') derecha   = false;
      if (keyCode == PApplet.UP    || key == 'w' || key == 'W') arriba    = false;
      if (keyCode == PApplet.DOWN  || key == 's' || key == 'S') abajo     = false;
      if (key == ' ') disparo = false;
    }
  }

  public void onMousePressed(float mx, float my, PApplet app) {
    if (estadoJuegoInterno == ESTADO_MENU) {
      float startY = app.height * 0.48f, spacing = 65;
      for (int i = 0; i < 4; i++) {
        float itemY = startY + i * spacing;
        if (my > itemY-30 && my < itemY+15 && mx > app.width/2f-180 && mx < app.width/2f+180) {
          opcionSeleccionada = i; seleccionarOpcionMenu(app); return;
        }
      }
    } else if (estadoJuegoInterno == ESTADO_ESTADISTICAS || estadoJuegoInterno == ESTADO_CONTROLES) {
      float btnY = app.height * 0.82f;
      if (my > btnY-25 && my < btnY+15 && mx > app.width/2f-100 && mx < app.width/2f+100) {
        estadoJuegoInterno = ESTADO_MENU; opcionSeleccionada = 0;
      }
    } else if (estadoJuegoInterno == ESTADO_GAME_OVER) {
      float startY = app.height * 0.70f, spacing = 60;
      for (int i = 0; i < 2; i++) {
        float itemY = startY + i * spacing;
        if (my > itemY-25 && my < itemY+15 && mx > app.width/2f-150 && mx < app.width/2f+150) {
          opcionSeleccionada = i; seleccionarOpcionGameOver(app); return;
        }
      }
    }
  }

  void seleccionarOpcionMenu(PApplet app) {
    switch (opcionSeleccionada) {
      case 0:
        if (app != null) inicializarPartida(app);
        estadoJuegoInterno = ESTADO_JUGANDO;
        // re-activar estado del contrato si ya había finalizado
        if ("FINALIZADO".equals(estadoActual.getNombre())) estadoActual = new EnEjecucionState();
        break;
      case 1: estadoJuegoInterno = ESTADO_ESTADISTICAS; break;
      case 2: estadoJuegoInterno = ESTADO_CONTROLES; break;
      case 3:
        try { finalizar(); } catch (EstadoInvalidoException e) { /* ok */ }
        break;
    }
  }

  void seleccionarOpcionGameOver(PApplet app) {
    switch (opcionSeleccionada) {
      case 0: // REINTENTAR
        jugador = null;
        elementos.clear();
        if (app != null) inicializarPartida(app);
        estadoJuegoInterno = ESTADO_JUGANDO;
        estadoActual = new EnEjecucionState();
        break;
      case 1: // VOLVER AL MENÚ → devolver control al lobby
        try { finalizar(); } catch (EstadoInvalidoException e) { /* ya finalizado, ok */ }
        break;
    }
  }

  // ── Helpers ──────────────────────────────────────────────────────────────────
  // ── Workaround: listener directo de teclado ────────────────────────────────
  public void keyEvent(processing.event.KeyEvent e) {
    if (e.getAction() == processing.event.KeyEvent.PRESS) {
      procesarTecla(e.getKeyCode(), e.getKey());
    } else if (e.getAction() == processing.event.KeyEvent.RELEASE) {
      procesarTeclaSoltada(e.getKeyCode(), e.getKey());
    }
  }

  private void notificar(ModuloEvento evento) {
    for (IModuloObserver obs : new ArrayList<>(observers)) obs.onEventoModulo(evento);
  }
}
