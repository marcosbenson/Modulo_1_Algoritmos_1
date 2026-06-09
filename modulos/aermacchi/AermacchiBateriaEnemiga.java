import processing.core.*;

public class AermacchiBateriaEnemiga extends AermacchiEnemigo {
  int tiempoUltimoDisparo = 0;
  int cadenciaDisparo = 2200;
  PImage img;

  AermacchiBateriaEnemiga(float x, float y, PApplet app, ModuloAermacchi modulo) {
    super(x, y, 145, 200, 0.8f, 8, 450, modulo);
    probabilidadDrop = 0.40f;
    img = app.loadImage("aermacchi_barco_enemigo.png");
  }

  public void actualizar(PApplet app) {
    y += velocidad;
    if (y > app.height) { desactivar(); return; }
    if (puedeDisparar(app)) {
      dispararHaciaJugador(app);
      tiempoUltimoDisparo = app.millis();
    }
  }

  boolean puedeDisparar(PApplet app) {
    return app.millis() - tiempoUltimoDisparo >= cadenciaDisparo;
  }

  void dispararHaciaJugador(PApplet app) {
    if (modulo.jugador == null) return;
    float ox = x + ancho/2, oy = y + alto/2;
    float tx = modulo.jugador.x + modulo.jugador.ancho/2;
    float ty = modulo.jugador.y + modulo.jugador.alto/2;
    float base = PApplet.atan2(ty - oy, tx - ox);
    float vel = 2.0f;
    float[] spread = {-0.25f, 0, 0.25f};
    for (float off : spread) {
      float a = base + off;
      modulo.elementos.add(new AermacchiProyectilEnemigo(ox, oy,
          PApplet.cos(a) * vel, PApplet.sin(a) * vel));
    }
  }

  public void dibujar(PApplet app) {
    if (img != null) app.image(img, x, y, ancho, alto);
    else { app.fill(100, 100, 200); app.rect(x, y, ancho, alto); }
  }
}
