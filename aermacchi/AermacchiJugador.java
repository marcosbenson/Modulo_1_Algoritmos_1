import processing.core.*;

public class AermacchiJugador extends AermacchiElemento {
  int  vida = 5;
  int  cadenciaDisparo = 550;
  int  tiempoUltimoDisparo = 0;
  boolean tieneDisparoRapido = false;
  boolean tieneDisparoTriple = false;
  boolean invulnerable = false;
  int tiempoInicioInvulnerabilidad = 0;
  int duracionInvulnerabilidad = 0;
  PImage img;

  AermacchiJugador(float x, float y, PApplet app) {
    super(x, y, 100, 100, 7.5f);
    img = app.loadImage("aermacchi_avion.png");
  }

  public void actualizar(PApplet app) { /* movimiento gestionado desde ModuloAermacchi */ }

  public void mover(String dir) {
    if (dir.equals("izquierda")) x -= velocidad;
    if (dir.equals("derecha"))   x += velocidad;
    if (dir.equals("arriba"))    y -= velocidad;
    if (dir.equals("abajo"))     y += velocidad;
  }

  public boolean puedeDisparar(PApplet app) {
    float cad = tieneDisparoRapido ? cadenciaDisparo / 1.5f : cadenciaDisparo;
    return app.millis() - tiempoUltimoDisparo >= cad;
  }

  public AermacchiProyectilAliado disparar(PApplet app) {
    tiempoUltimoDisparo = app.millis();
    return new AermacchiProyectilAliado(x + ancho / 2 - 4, y, 0, -7.5f);
  }

  @Override
  public AermacchiRect getHitbox() {
    float hw = ancho * 0.45f, hh = alto * 0.6f;
    return new AermacchiRect(x + (ancho - hw)/2, y + alto * 0.2f, hw, hh);
  }

  public boolean esInvulnerable(PApplet app) {
    return invulnerable && (app.millis() - tiempoInicioInvulnerabilidad < duracionInvulnerabilidad);
  }

  public void activarInvulnerabilidad(int durMs, PApplet app) {
    invulnerable = true;
    tiempoInicioInvulnerabilidad = app.millis();
    duracionInvulnerabilidad = durMs;
  }

  public void recibirImpacto(int danio, PApplet app) {
    if (esInvulnerable(app)) return;
    vida -= danio;
    tieneDisparoRapido = false;
    tieneDisparoTriple = false;
    if (vida <= 0) desactivar();
    else activarInvulnerabilidad(2500, app);
  }

  public boolean estaDestruido() { return vida <= 0; }

  public void dibujar(PApplet app) {
    if (esInvulnerable(app) && (app.frameCount / 4) % 2 == 0) return;
    if (img != null) app.image(img, x, y, ancho, alto);
    else {
      app.fill(0, 200, 255); app.rect(x, y, ancho, alto);
    }
  }
}
