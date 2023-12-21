package global.util;

public record CoordF2D(double x, double y) {
    /**
     * Returns true if this point is in ([0, w), [0, h)) 
     * @param w
     * @param h
     * @return
     */
    public boolean checkBounds(double w, double h) {
        return this.x >= 0 && this.x < w && this.y >= 0 && this.y < h;
    }
}
