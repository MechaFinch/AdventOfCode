package global.util;

public record Coord2D(int x, int y) {
    /**
     * Returns true if this point is in ([0, w), [0, h)) 
     * @param w
     * @param h
     * @return
     */
    public boolean checkBounds(int w, int h) {
        return this.x >= 0 && this.x < w && this.y >= 0 && this.y < h;
    }
}
