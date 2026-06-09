// ── Estadísticas de sesión histórica ─────────────────────────────────────────
public class AermacchiEstadisticasGenerales {
  String nombreModulo  = "MB339 Aermacchi";
  int puntajeTotal     = 0;
  int partidasJugadas  = 0;
  int partidasGanadas  = 0;
  int partidasPerdidas = 0;
  int enemigosDestruidos = 0;
  long tiempoJugadoSegundos = 0;

  void registrarVictoria() { partidasJugadas++; partidasGanadas++; }
  void registrarDerrota()  { partidasJugadas++; partidasPerdidas++; }
}

// ── Estadísticas de partida actual ───────────────────────────────────────────
class AermacchiEstadisticasModulo extends AermacchiEstadisticasGenerales {
  int disparosTotales   = 0;
  int disparosAcertados = 0;
  int enemigosDerribados = 0;
  int rachaMaxDerribos  = 0;
  int rachaActual       = 0;
  double precision      = 0;

  void registrarDisparo(boolean acerto) {
    disparosTotales++;
    if (acerto) disparosAcertados++;
    calcularPrecision();
  }

  void registrarImpacto() {
    disparosAcertados++;
    calcularPrecision();
  }

  void registrarDerribo(int puntos) {
    enemigosDerribados++;
    puntajeTotal += puntos;
    enemigosDestruidos++;
    rachaActual++;
    if (rachaActual > rachaMaxDerribos) rachaMaxDerribos = rachaActual;
    calcularPrecision();
  }

  void registrarMuerteJugador() { rachaActual = 0; }

  double calcularPrecision() {
    precision = disparosTotales == 0 ? 0 : (disparosAcertados * 100.0) / disparosTotales;
    return precision;
  }

  void reiniciarEstadisticas() {
    puntajeTotal = 0; partidasJugadas = 0; partidasGanadas = 0;
    partidasPerdidas = 0; enemigosDestruidos = 0; tiempoJugadoSegundos = 0;
    disparosTotales = 0; disparosAcertados = 0; enemigosDerribados = 0;
    rachaMaxDerribos = 0; rachaActual = 0; precision = 0;
  }
}
