import processing.core.*;

public abstract class AermacchiElemento {
  float x, y, ancho, alto, velocidad;
  boolean activo = true;

  AermacchiElemento(float x, float y, float ancho, float alto, float velocidad) {
    this.x = x; this.y = y;
    this.ancho = ancho; this.alto = alto;
    this.velocidad = velocidad;
  }

  public abstract void actualizar(PApplet app);
  public abstract void dibujar(PApplet app);

  public AermacchiRect getHitbox() {
    return new AermacchiRect(x, y, ancho, alto);
  }

  public boolean estaActivo() { return activo; }
  public void desactivar()    { activo = false; }
}
