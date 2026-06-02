import processing.core.*;

HomeJuego homeJuego;
PFont fuentePixel;

void setup() {
  size(800, 600);
  frameRate(60);

  fuentePixel = createFont("/home/federico/Documents/Projects/Modulo_1_Algoritmos_1/data/PressStart2P-Regular.ttf", 16, true);
  textFont(fuentePixel);

  homeJuego = new HomeJuego(this);
  homeJuego.iniciarHome();
  homeJuego.registrarModulo(new ModuloPrueba());
}

void draw() {
  homeJuego.dibujar();
}

void mousePressed() {
  homeJuego.manejarClick(mouseX, mouseY);
}

void keyPressed() {
  homeJuego.manejarTecla(keyCode, key);
}
