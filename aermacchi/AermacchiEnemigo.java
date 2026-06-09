import processing.core.*;

public abstract class AermacchiEnemigo extends AermacchiElemento {
  int vida;
  int puntosOtorgados;
  float probabilidadDrop = 0.0f;
  ModuloAermacchi modulo; // referencia al módulo en vez de variable global "juego"

  AermacchiEnemigo(float x, float y, float ancho, float alto, float vel,
                   int vida, int puntos, ModuloAermacchi modulo) {
    super(x, y, ancho, alto, vel);
    this.vida = vida;
    this.puntosOtorgados = puntos;
    this.modulo = modulo;
  }

  public void recibirImpacto(int danio, PApplet app) {
    vida -= danio;
    if (vida <= 0) morir(app);
  }

  public boolean estaDestruido() { return vida <= 0; }
  public int getPuntosOtorgados() { return puntosOtorgados; }

  protected void morir(PApplet app) {
    desactivar();
    if (this instanceof AermacchiJefe) {
      modulo.elementos.add(new AermacchiPowerUp(x + ancho/2 - 30, y + alto/2, "vida",     app));
      modulo.elementos.add(new AermacchiPowerUp(x + ancho/2 + 10, y + alto/2, tipoPowerUp(app), app));
    } else {
      if (app.random(1.0f) < probabilidadDrop) {
        modulo.elementos.add(new AermacchiPowerUp(x + ancho/2 - 15, y + alto/2, tipoPowerUp(app), app));
      }
    }
  }

  protected String tipoPowerUp(PApplet app) {
    float r = app.random(1.0f);
    if (r < 0.60f) return "invulnerable";
    if (r < 0.80f) return "vida";
    if (r < 0.95f) return "rapido";
    return "triple";
  }
}
