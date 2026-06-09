import processing.core.*;

public class AermacchiJefe extends AermacchiEnemigo {
  static final int FASE_ASCENSO  = 0;
  static final int FASE_DESCENSO = 1;
  static final int FASE_COMBATE  = 2;

  int fase;
  int vidaMaxima;
  int tiempoUltimoDisparo = 0;
  float cadenciaDisparo;
  int cadenciaBase = 1600;
  float velocidadAscenso  = 5;
  float velocidadDescenso = 1.5f;
  float yCombate = 30;
  float velocidadLateral, velocidadLateralBase = 1;
  int   direccionLateral;
  int   tiempoUltimaOleada = 0;
  int   intervaloOleada = 7500;
  boolean combateIniciado = false;
  float spreadArc;
  int   numRays = 8;
  PImage img;

  AermacchiJefe(float x, float y, PApplet app, ModuloAermacchi modulo) {
    super(x, y, 300, 320, 0, 100, 1000, modulo);
    vidaMaxima           = vida;
    fase                 = FASE_ASCENSO;
    cadenciaDisparo      = cadenciaBase;
    velocidadLateral     = velocidadLateralBase;
    direccionLateral     = app.random(1) < 0.5f ? -1 : 1;
    spreadArc            = PApplet.PI / 1.5f;
    this.y               = app.height + 10;
    img = app.loadImage("aermacchi_jefe.png");
  }

  public void actualizar(PApplet app) {
    switch (fase) {
      case FASE_ASCENSO:
        y -= velocidadAscenso;
        if (y + alto < 0) { fase = FASE_DESCENSO; y = -alto - 10; }
        break;
      case FASE_DESCENSO:
        y += velocidadDescenso;
        if (y >= yCombate) { y = yCombate; fase = FASE_COMBATE; combateIniciado = true; }
        break;
      case FASE_COMBATE:
        actualizarFases();
        moverLateral(app);
        if (puedeDisparar(app)) dispararHaciaJugador(app);
        if (vidaPct() < 0.30f && app.millis() - tiempoUltimaOleada >= intervaloOleada) {
          invocarOleada(app);
          tiempoUltimaOleada = app.millis();
        }
        break;
    }
  }

  void actualizarFases() {
    if (vidaPct() < 0.50f) {
      cadenciaDisparo = cadenciaBase * 0.8f;
      velocidadLateral = velocidadLateralBase * 1.8f;
      spreadArc = PApplet.PI / 2.2f;
      numRays = 10;
    } else {
      cadenciaDisparo  = cadenciaBase;
      velocidadLateral = velocidadLateralBase;
    }
  }

  void moverLateral(PApplet app) {
    x += direccionLateral * velocidadLateral;
    if (x <= 15)                      { x = 15;                  direccionLateral =  1; }
    else if (x + ancho >= app.width - 15) { x = app.width - ancho - 15; direccionLateral = -1; }
  }

  boolean puedeDisparar(PApplet app) {
    return combateIniciado && app.millis() - tiempoUltimoDisparo >= cadenciaDisparo;
  }

  boolean estaEnCombate() { return combateIniciado; }
  float vidaPct() { return (float)vida / vidaMaxima; }

  void dispararHaciaJugador(PApplet app) {
    tiempoUltimoDisparo = app.millis();
    if (modulo.jugador == null) return;
    float ox = x + ancho/2, oy = y + alto/2;
    float tx = modulo.jugador.x + modulo.jugador.ancho/2;
    float ty = modulo.jugador.y + modulo.jugador.alto/2;
    float base = PApplet.atan2(ty - oy, tx - ox);
    float vel = 2f;
    for (int i = 0; i < numRays; i++) {
      float t = (numRays == 1) ? 0 : (float)i / (numRays - 1);
      float a = base + (-spreadArc/2 + t * spreadArc);
      modulo.elementos.add(new AermacchiProyectilEnemigo(ox, oy,
          PApplet.cos(a) * vel, PApplet.sin(a) * vel));
    }
  }

  void invocarOleada(PApplet app) {
    int cant = 5;
    for (int i = 0; i < cant; i++) {
      float offX = (i - (cant-1)/2.0f) * 150;
      float offY = PApplet.abs(i - (cant-1)/2.0f) * 150;
      float xs = PApplet.constrain(app.width/2f + offX - 25, 40, app.width - 80);
      modulo.elementos.add(new AermacchiCazaEnemigo(xs, -40 - offY, app, modulo));
    }
  }

  @Override
  public void recibirImpacto(int danio, PApplet app) {
    if (!combateIniciado) return;
    super.recibirImpacto(danio, app);
  }

  public void dibujar(PApplet app) {
    if (img != null) app.image(img, x, y, ancho, alto);
    else { app.fill(150, 50, 200); app.rect(x, y, ancho, alto); }
  }
}
