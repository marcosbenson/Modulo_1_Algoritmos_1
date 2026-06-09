import processing.core.*;

public class AermacchiPowerUp extends AermacchiElemento {
  String tipo;
  long tiempoCreacion;
  boolean parpadeando = false;
  PImage img;

  AermacchiPowerUp(float x, float y, String tipo, PApplet app) {
    super(x, y, 50, 50, 0.3f);
    this.tipo = tipo;
    this.tiempoCreacion = app.millis();
    if      (tipo.equals("vida"))        img = app.loadImage("aermacchi_corazon.png");
    else if (tipo.equals("invulnerable")) img = app.loadImage("aermacchi_escudo.png");
    else if (tipo.equals("rapido"))      img = app.loadImage("aermacchi_rapido.png");
    else if (tipo.equals("triple"))      img = app.loadImage("aermacchi_triple.png");
  }

  public void actualizar(PApplet app) {
    y += velocidad;
    if (y > app.height) { desactivar(); return; }
    long elapsed = app.millis() - tiempoCreacion;
    if (elapsed >= 7000)      desactivar();
    else if (elapsed >= 4500) parpadeando = true;
  }

  public void aplicarEfecto(AermacchiJugador jugador, PApplet app) {
    if      (tipo.equals("vida"))         jugador.vida++;
    else if (tipo.equals("invulnerable")) jugador.activarInvulnerabilidad(5500, app);
    else if (tipo.equals("rapido"))       jugador.tieneDisparoRapido = true;
    else if (tipo.equals("triple"))       jugador.tieneDisparoTriple = true;
    desactivar();
  }

  public void dibujar(PApplet app) {
    if (parpadeando && (app.frameCount / 4) % 2 == 0) return;
    if (img != null) app.image(img, x, y, ancho, alto);
    else { app.fill(0, 255, 200); app.ellipse(x + ancho/2, y + alto/2, ancho, alto); }
  }
}
