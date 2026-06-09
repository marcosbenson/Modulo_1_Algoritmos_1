public class AermacchiRect {
  float x, y, w, h;

  AermacchiRect(float x, float y, float w, float h) {
    this.x = x; this.y = y; this.w = w; this.h = h;
  }

  public boolean intersecta(AermacchiRect otro) {
    return x < otro.x + otro.w && x + w > otro.x &&
           y < otro.y + otro.h && y + h > otro.y;
  }
}
