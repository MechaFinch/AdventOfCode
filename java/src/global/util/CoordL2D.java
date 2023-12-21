package global.util;

public record CoordL2D(long x, long y) {
    /**
     * Returns true if this point is in ([0, w), [0, h)) 
     * @param w
     * @param h
     * @return
     */
    public boolean checkBounds(long w, long h) {
        return this.x >= 0 && this.x < w && this.y >= 0 && this.y < h;
    }
}
