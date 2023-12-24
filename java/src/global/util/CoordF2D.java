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
    
    /**
     * @param other
     * @param multiplier
     * @return this + (other * multiplier)
     */
    public CoordF2D add(CoordF2D other, double multiplier) {
        return new CoordF2D(
            this.x + (multiplier * other.x),
            this.y + (multiplier * other.y)
        );
    }
    
    /**
     * @param other
     * @param multiplier
     * @return this - (other * multiplier)
     */
    public CoordF2D subtract(CoordF2D other, double multiplier) {
        return new CoordF2D(
            this.x - (multiplier * other.x),
            this.y - (multiplier * other.y)
        );
    }
}
