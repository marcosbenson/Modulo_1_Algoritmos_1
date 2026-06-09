import processing.core.*;

public class AermacchiCazaEnemigo extends AermacchiEnemigo {
  int tiempoUltimoDisparo = 0;
  int cadenciaDisparo = 2000;
  PImage img;

  AermacchiCazaEnemigo(float x, float y, PApplet app, ModuloAermacchi modulo) {
    super(x, y, 70, 70, 3, 1, 100, modulo);
    probabilidadDrop = 0.01f;
    img = app.loadImage("aermacchi_caza_enemigo.png");
  }

  public void actualizar(PApplet app) {
    y += velocidad;
    if (y > app.height) { desactivar(); return; }
    if (puedeDisparar(app)) {
      modulo.elementos.add(new AermacchiProyectilEnemigo(x + ancho/2 - 4, y + alto, 0, 5.5f));
      tiempoUltimoDisparo = app.millis();
    }
  }

  boolean puedeDisparar(PApplet app) {
    return y > 0 && app.millis() - tiempoUltimoDisparo >= cadenciaDisparo;
  }

  public void dibujar(PApplet app) {
    if (img != null) app.image(img, x, y, ancho, alto);
    else { app.fill(255, 80, 80); app.triangle(x, y, x+ancho, y, x+ancho/2, y+alto); }
  }
}
