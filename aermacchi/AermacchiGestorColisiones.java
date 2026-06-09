import processing.core.*;
import java.util.*;

public class AermacchiGestorColisiones {

  public void verificarColisiones(ArrayList<AermacchiElemento> elementos,
                                   AermacchiEstadisticasModulo stats,
                                   PApplet app) {
    for (int i = 0; i < elementos.size(); i++) {
      AermacchiElemento a = elementos.get(i);
      for (int j = i + 1; j < elementos.size(); j++) {
        AermacchiElemento b = elementos.get(j);
        if (a.estaActivo() && b.estaActivo() &&
            a.getHitbox().intersecta(b.getHitbox())) {
          procesar(a, b, stats, app);
        }
      }
    }
  }

  void procesar(AermacchiElemento a, AermacchiElemento b,
                AermacchiEstadisticasModulo stats, PApplet app) {

    // Proyectil aliado vs Enemigo
    if (a instanceof AermacchiProyectilAliado && b instanceof AermacchiEnemigo)
      hitProyectilEnemigo((AermacchiProyectilAliado)a, (AermacchiEnemigo)b, stats, app);
    else if (b instanceof AermacchiProyectilAliado && a instanceof AermacchiEnemigo)
      hitProyectilEnemigo((AermacchiProyectilAliado)b, (AermacchiEnemigo)a, stats, app);

    // Proyectil enemigo vs Jugador
    if (a instanceof AermacchiProyectilEnemigo && b instanceof AermacchiJugador)
      hitJugador((AermacchiProyectilEnemigo)a, (AermacchiJugador)b, stats, app);
    else if (b instanceof AermacchiProyectilEnemigo && a instanceof AermacchiJugador)
      hitJugador((AermacchiProyectilEnemigo)b, (AermacchiJugador)a, stats, app);

    // Choque jugador vs enemigo
    if (a instanceof AermacchiJugador && b instanceof AermacchiEnemigo)
      choqueJugador((AermacchiJugador)a, (AermacchiEnemigo)b, stats, app);
    else if (b instanceof AermacchiJugador && a instanceof AermacchiEnemigo)
      choqueJugador((AermacchiJugador)b, (AermacchiEnemigo)a, stats, app);

    // PowerUp vs Jugador
    if (a instanceof AermacchiJugador && b instanceof AermacchiPowerUp)
      ((AermacchiPowerUp)b).aplicarEfecto((AermacchiJugador)a, app);
    else if (b instanceof AermacchiJugador && a instanceof AermacchiPowerUp)
      ((AermacchiPowerUp)a).aplicarEfecto((AermacchiJugador)b, app);
  }

  void hitProyectilEnemigo(AermacchiProyectilAliado p, AermacchiEnemigo e,
                            AermacchiEstadisticasModulo stats, PApplet app) {
    if (e instanceof AermacchiJefe && !((AermacchiJefe)e).estaEnCombate()) {
      p.desactivar(); return;
    }
    e.recibirImpacto(p.getDanio(), app);
    p.desactivar();
    stats.registrarImpacto();
    if (e.estaDestruido()) stats.registrarDerribo(e.getPuntosOtorgados());
  }

  void hitJugador(AermacchiProyectilEnemigo p, AermacchiJugador j,
                  AermacchiEstadisticasModulo stats, PApplet app) {
    j.recibirImpacto(p.getDanio(), app);
    p.desactivar();
    if (j.estaDestruido()) stats.registrarMuerteJugador();
  }

  void choqueJugador(AermacchiJugador j, AermacchiEnemigo e,
                     AermacchiEstadisticasModulo stats, PApplet app) {
    if (e instanceof AermacchiJefe && ((AermacchiJefe)e).fase == AermacchiJefe.FASE_ASCENSO) return;
    j.recibirImpacto(1, app);
    if (!(e instanceof AermacchiJefe) || ((AermacchiJefe)e).estaEnCombate()) e.desactivar();
    if (j.estaDestruido()) stats.registrarMuerteJugador();
  }
}
