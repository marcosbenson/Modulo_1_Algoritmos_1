import processing.core.*;

// ── Proyectil aliado ──────────────────────────────────────────────────────────
public class AermacchiProyectilAliado extends AermacchiElemento {
  float vx, vy;
  int danio = 1;

  AermacchiProyectilAliado(float x, float y, float vx, float vy) {
    super(x, y, 6, 12, 0);
    this.vx = vx;
    this.vy = vy;
  }

  public int getDanio() { return danio; }

  public void actualizar(PApplet app) {
    x += vx; y += vy;
    if (y < -20 || y > app.height + 20) desactivar();
  }

  public void dibujar(PApplet app) {
    app.fill(255, 255, 0); app.noStroke();
    app.rect(x, y, ancho, alto);
  }
}

// ── Proyectil enemigo ─────────────────────────────────────────────────────────
class AermacchiProyectilEnemigo extends AermacchiElemento {
  float vx, vy;
  int danio = 1;

  AermacchiProyectilEnemigo(float x, float y, float vx, float vy) {
    super(x, y, 6, 12, 0);
    this.vx = vx;
    this.vy = vy;
  }

  public int getDanio() { return danio; }

  public void actualizar(PApplet app) {
    x += vx; y += vy;
    if (x < -20 || x > app.width + 20 || y < -20 || y > app.height + 20) desactivar();
  }

  public void dibujar(PApplet app) {
    app.fill(255, 0, 0); app.noStroke();
    app.rect(x, y, ancho, alto);
  }
}
