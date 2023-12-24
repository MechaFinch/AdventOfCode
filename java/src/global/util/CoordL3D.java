package global.util;

public record CoordL3D(long x, long y, long z) {
    /**
     * Returns true if this point is in ([0, w), [0, h), [0, d)) 
     * @param w
     * @param h
     * @param d
     * @return
     */
    public boolean checkBounds(long w, long h, long d) {
        return this.x >= 0 && this.x < w && this.y >= 0 && this.y < h && this.z >= 0 && this.z < d;
    }
    
    /**
     * @param other
     * @param multiplier
     * @return this + (other * multiplier)
     */
    public CoordL3D add(CoordL3D other, long multiplier) {
        return new CoordL3D(
            this.x + (multiplier * other.x),
            this.y + (multiplier * other.y),
            this.z + (multiplier * other.z)
        );
    }
    
    /**
     * @param other
     * @param multiplier
     * @return this - (other * multiplier)
     */
    public CoordL3D subtract(CoordL3D other, long multiplier) {
        return new CoordL3D(
            this.x - (multiplier * other.x),
            this.y - (multiplier * other.y),
            this.z - (multiplier * other.z)
        );
    }
}
