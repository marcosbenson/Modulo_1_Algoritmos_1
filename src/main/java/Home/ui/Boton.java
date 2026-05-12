package Home.ui;

import processing.core.PApplet;

/**
 * Boton reutilizable para la interfaz del Home.
 * Detecta hover y clics. Se dibuja con estilo retro.
 */
public class Boton {

    private float x;       // posicion X del centro
    private float y;       // posicion Y del centro
    private float ancho;
    private float alto;
    private String texto;

    public Boton(String texto, float x, float y, float ancho, float alto) {
        this.texto = texto;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    /**
     * Dibuja el boton. Si el mouse esta encima, cambia el color.
     */
    public void dibujar(PApplet app) {
        boolean hover = estaEncima(app.mouseX, app.mouseY);

        // Fondo del boton
        if (hover) {
            app.fill(0, 200, 0);       // verde claro al hacer hover
        } else {
            app.fill(0, 120, 0);       // verde oscuro normal
        }
        app.noStroke();
        app.rectMode(PApplet.CENTER);
        app.rect(x, y, ancho, alto, 5);

        // Texto del boton
        app.fill(255);                  // blanco
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.textSize(14);
        app.text(texto, x, y - 2);
    }

    /**
     * Verifica si el mouse esta dentro del boton.
     */
    public boolean estaEncima(float mouseX, float mouseY) {
        return mouseX > x - ancho / 2 && mouseX < x + ancho / 2
            && mouseY > y - alto / 2 && mouseY < y + alto / 2;
    }

    public String getTexto() { return texto; }
}
