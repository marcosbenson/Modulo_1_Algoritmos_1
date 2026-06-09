import processing.core.*;

public class AermacchiCazaPesado extends AermacchiEnemigo {
  int tiempoUltimoDisparo = 0;
  int cadenciaDisparo = 2000;
  PImage img;

  AermacchiCazaPesado(float x, float y, PApplet app, ModuloAermacchi modulo) {
    super(x, y, 110, 110, 2, 3, 200, modulo);
    probabilidadDrop = 0.05f;
    img = app.loadImage("aermacchi_caza_pesado.png");
  }

  public void actualizar(PApplet app) {
    y += velocidad;
    if (y > app.height) { desactivar(); return; }
    if (puedeDisparar(app)) {
      modulo.elementos.add(new AermacchiProyectilEnemigo(x + 10,         y + alto, 0, 5.5f));
      modulo.elementos.add(new AermacchiProyectilEnemigo(x + ancho - 15, y + alto, 0, 5.5f));
      tiempoUltimoDisparo = app.millis();
    }
  }

  boolean puedeDisparar(PApplet app) {
    return y > 0 && app.millis() - tiempoUltimoDisparo >= cadenciaDisparo;
  }

  public void dibujar(PApplet app) {
    if (img != null) app.image(img, x, y, ancho, alto);
    else { app.fill(200, 50, 50); app.rect(x, y, ancho, alto); }
  }
}
