import processing.core.PApplet;
import processing.core.PImage;

public class SE_Exocet extends SE_Proyectil {

  // El sprite está orientado horizontalmente (nariz a la derecha).
  // En vuelo aliado, la nariz apunta hacia arriba → rotamos -90°.
  // En vuelo enemigo, la nariz apunta hacia abajo → rotamos +90°.
  //
  // Dimensiones de colisión: el sprite redimensionado queda ~40px de alto
  // (eje largo del misil). Al rotarlo verticalmente, el largo pasa a ser
  // el "alto" del hitbox y el ancho de la imagen pasa a ser el "ancho".
  // El sprite es cuadrado (1088×1088), redimensionado a alto=40 → 40×40.
  // El misil real ocupa ~75% del largo: ~30px largo, ~8px ancho tras rotar.

  public SE_Exocet(SE_GestorDeEntidades gestor, float x, float y, boolean esAliado) {
    super(gestor, x, y, esAliado);
    this.danio = 100;
    // Hitbox ajustado al cuerpo del misil (sin contar el vacío del fondo negro)
    // Tras rotar 90°: el largo del misil se convierte en el "alto" del hitbox
    this.ancho = 8;
    this.alto = 32;
    this.vx = 0;
    this.vy = esAliado ? -9 : 6;
  }

  @Override
  public void dibujar() {
    PApplet app = gestor.gp.getApp();
    PImage sprite = (gfx != null) ? gfx.getImagen("Exocet") : null;

    app.pushMatrix();
    app.translate(x, y);

    if (sprite != null) {
      // El sprite viene horizontal. Rotamos para que la nariz apunte
      // en la dirección de vuelo: arriba (aliado) o abajo (enemigo).
      float angulo = esAliado ? -PApplet.HALF_PI : PApplet.HALF_PI;
      app.rotate(angulo);

      app.imageMode(PApplet.CENTER);
      // El sprite es 40px de alto original → imagen cuadrada 40×40
      // Lo dibujamos centrado en el origen (ya aplicamos translate)
      app.image(sprite, 0, 0);

      // Llama de propulsión (se dibuja DESPUÉS del sprite,
      // en la coordenada "detrás" de la cola del misil, es decir,
      // al lado izquierdo del sprite horizontal antes de rotar).
      // En el sistema rotado, la cola queda a y negativo (arriba del sprite
      // antes de rotar), es decir, x negativo en el sistema original.
      // Sprite redimensionado: 40px de alto → ~20px mitad
      app.noStroke();
      app.fill(255, 180, 0, 70);
      app.ellipse(-22, 0, 8, 6);
      app.fill(255, 120, 0, 155);
      app.ellipse(-23, 0, 5, 4);
      app.fill(255, 235, 80, 220);
      app.ellipse(-24, 0, 3, 3);

    } else {
      // Fallback: dibuja el pixel art si el sprite no carga
      app.noStroke();
      if (!esAliado) app.rotate(PApplet.PI);

      app.fill(255, 180, 0, 70);
      app.ellipse(0, 18, 9, 8);
      app.fill(255, 120, 0, 150);
      app.ellipse(0, 17, 6, 6);
      app.fill(255, 230, 80, 220);
      app.ellipse(0, 16, 3, 4);

      app.fill(170, 175, 180);
      app.triangle(3, 10, 7, 15, 3, 15);
      app.triangle(-3, 10, -7, 15, -3, 15);

      app.fill(228, 230, 225);
      app.stroke(148, 150, 145);
      app.strokeWeight(1);
      app.rectMode(PApplet.CENTER);
      app.rect(0, 0, 6, 30);
      app.noStroke();

      app.fill(190, 196, 202);
      app.stroke(120);
      app.strokeWeight(1);
      app.triangle(3, 0, 12, 11, 3, 11);
      app.triangle(-3, 0, -12, 11, -3, 11);
      app.noStroke();

      app.fill(30, 30, 35);
      app.rectMode(PApplet.CENTER);
      app.rect(0, -10, 6, 5);
      app.fill(200, 165, 30);
      app.rect(0, -7, 6, 2);
      app.fill(150, 155, 158);
      app.triangle(3, -12, 2, -22, 3, -22);
      app.triangle(-3, -12, -2, -22, -3, -22);
      app.fill(205, 210, 208);
      app.triangle(-3, -12, 3, -12, 0, -23);
      app.fill(255, 255, 255, 80);
      app.rect(-1, -2, 2, 20);
    }

    app.popMatrix();
  }
}
