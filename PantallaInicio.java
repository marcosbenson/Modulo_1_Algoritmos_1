import processing.core.*;

public class PantallaInicio {
  private Boton botonStart;

  public PantallaInicio(int anchoVentana, int altoVentana) {
    float bAncho = 280;
    float bAlto = 50;
    botonStart = new Boton(anchoVentana / 2f - bAncho / 2f, altoVentana * 0.65f, bAncho, bAlto, "PRESS START");
    botonStart.setSeleccionado(true);
  }

  public void dibujar(PApplet app) {
    app.background(0);

    // título principal
    app.fill(0, 255, 0);
    app.textSize(36);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.text("1982", app.width / 2f, app.height * 0.22f);

    app.fill(0, 200, 0);
    app.textSize(11);
    app.text("CONFLICTO DEL ATLANTICO SUR", app.width / 2f, app.height * 0.33f);

    // línea decorativa
    app.stroke(0, 120, 0);
    app.strokeWeight(1);
    app.line(50, app.height * 0.40f, app.width - 50, app.height * 0.40f);

    app.fill(0, 120, 0);
    app.textSize(8);
    app.text("UN SHOOTER AEREO DE LA GUERRA DE MALVINAS", app.width / 2f, app.height * 0.48f);

    botonStart.dibujar(app);

    app.fill(0, 120, 0);
    app.textSize(7);
    app.textAlign(PApplet.CENTER, PApplet.BOTTOM);
    app.text("(C) 1982  FUERZA AEREA ARGENTINA", app.width / 2f, app.height - 20);
  }

  public boolean clicEnStart(float mx, float my) {
    return botonStart.estaEncima(mx, my);
  }
}
