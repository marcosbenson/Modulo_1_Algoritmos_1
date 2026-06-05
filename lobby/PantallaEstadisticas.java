import processing.core.*;
import java.util.*;

public class PantallaEstadisticas {
  private Boton botonVolver;

  private static final int   MAX_MODULOS = 5;
  private static final float GAP         = 4;
  private static final float TEXT_TITULO = 11;
  private static final float TEXT_DATOS  = 10;

  public PantallaEstadisticas(int anchoVentana, int altoVentana) {
    float bAncho = 220;
    float bAlto  = 44;
    botonVolver = new Boton(anchoVentana / 2f - bAncho / 2f, altoVentana * 0.88f, bAncho, bAlto, "VOLVER");
    botonVolver.setSeleccionado(true);
  }

  public void dibujar(PApplet app, List<EstadisticasGenerales> estadisticas, int puntajeTotal) {
    app.pushStyle();
    app.background(0);

    // — Título —
    app.fill(255);
    app.textSize(22);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.text("ESTADISTICAS", app.width / 2f, app.height * 0.10f);

    app.stroke(0, 120, 0);
    app.strokeWeight(1);
    app.line(50, app.height * 0.17f, app.width - 50, app.height * 0.17f);

    if (estadisticas == null || estadisticas.isEmpty()) {
      app.fill(255);
      app.textSize(12);
      app.textAlign(PApplet.CENTER, PApplet.CENTER);
      app.text("SIN DATOS AUN", app.width / 2f, app.height * 0.45f);
    } else {
      float startY = app.height * 0.22f;
      float endY   = app.height * 0.78f;
      float totalH = endY - startY;

      // rowH siempre calculado para MAX_MODULOS (5), nunca se estira con menos filas
      float rowH = (totalH - GAP * (MAX_MODULOS - 1)) / MAX_MODULOS;

      int n = estadisticas.size();

      for (int i = 0; i < n; i++) {
        EstadisticasGenerales s = estadisticas.get(i);
        float cy = startY + i * (rowH + GAP);

        app.strokeWeight(1);
        app.stroke(0, 51, 0);
        app.fill(0, 24, 0);
        app.rectMode(PApplet.CORNER);
        app.rect(40, cy, app.width - 80, rowH, 3);

        float padX  = 55;
        float padY  = cy + rowH * 0.08f;
        float lineH = rowH / 3.5f;

        app.fill(255);
        app.textAlign(PApplet.LEFT, PApplet.TOP);

        app.textSize(TEXT_TITULO);
        app.text(s.getNombreModulo(), padX, padY);

        app.textSize(TEXT_DATOS);
        app.text("PUNTAJE: " + s.getPuntajeTotal()
            + "   PARTIDAS: " + s.getPartidasJugadas()
            + "   VICTORIAS: " + s.getPartidasGanadas(),
            padX, padY + lineH);
        app.text("ENEMIGOS: " + s.getEnemigosDestruidos()
            + "   TIEMPO: " + s.getTiempoJugadoSegundos() + "s",
            padX, padY + lineH * 2);
      }
    }

    // — Separador y puntaje total —
    app.strokeWeight(1);
    app.stroke(0, 120, 0);
    app.line(50, app.height * 0.80f, app.width - 50, app.height * 0.80f);

    app.fill(255);
    app.textSize(13);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.text("PUNTAJE TOTAL DEL CURSO: " + puntajeTotal, app.width / 2f, app.height * 0.84f);

    app.popStyle();

    botonVolver.dibujar(app);
  }

  public boolean clicEnVolver(float mx, float my) { return botonVolver.estaEncima(mx, my); }
}
