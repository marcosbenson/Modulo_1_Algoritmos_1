package Home.ui;

import processing.core.PApplet;
import Contrato.ModuloJuego;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de seleccion de modulos.
 * Muestra un boton por cada modulo registrado, mas botones de
 * ESTADISTICAS y VOLVER.
 */
public class PantallaSeleccion {

    private List<Boton> botonesModulos;
    private List<String> nombresModulos;
    private Boton botonEstadisticas;
    private Boton botonVolver;

    /**
     * Construye la pantalla con los modulos disponibles.
     */
    public PantallaSeleccion(List<ModuloJuego> modulos, float anchoPantalla, float altoPantalla) {
        botonesModulos = new ArrayList<Boton>();
        nombresModulos = new ArrayList<String>();

        float centroX = anchoPantalla / 2;
        float inicioY = 160;

        // Un boton por cada modulo
        for (int i = 0; i < modulos.size(); i++) {
            ModuloJuego modulo = modulos.get(i);
            String nombre = modulo.getNombreAvion();
            float y = inicioY + i * 60;
            botonesModulos.add(new Boton(nombre, centroX, y, 300, 45));
            nombresModulos.add(modulo.getNombreModulo());
        }

        // Boton estadisticas
        float yStats = inicioY + modulos.size() * 60 + 20;
        botonEstadisticas = new Boton("ESTADISTICAS", centroX, yStats, 300, 45);

        // Boton volver
        botonVolver = new Boton("VOLVER", centroX, yStats + 60, 300, 45);
    }

    public void dibujar(PApplet app) {
        app.background(0);

        // Titulo
        app.fill(0, 255, 0);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.textSize(24);
        app.text("SELECCIONAR AVION", app.width / 2, 60);

        // Linea decorativa
        app.stroke(0, 120, 0);
        app.line(100, 95, app.width - 100, 95);
        app.noStroke();

        // Botones de modulos
        for (Boton boton : botonesModulos) {
            boton.dibujar(app);
        }

        // Boton estadisticas y volver
        botonEstadisticas.dibujar(app);
        botonVolver.dibujar(app);
    }

    /**
     * Verifica si se hizo clic en algun modulo.
     * Devuelve el nombre del modulo o null si no se hizo clic en ninguno.
     */
    public String clicEnModulo(float mouseX, float mouseY) {
        for (int i = 0; i < botonesModulos.size(); i++) {
            if (botonesModulos.get(i).estaEncima(mouseX, mouseY)) {
                return nombresModulos.get(i);
            }
        }
        return null;
    }

    public boolean clicEnEstadisticas(float mouseX, float mouseY) {
        return botonEstadisticas.estaEncima(mouseX, mouseY);
    }

    public boolean clicEnVolver(float mouseX, float mouseY) {
        return botonVolver.estaEncima(mouseX, mouseY);
    }
}
