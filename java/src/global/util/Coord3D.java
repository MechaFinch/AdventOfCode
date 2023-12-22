package global.util;

public record Coord3D(int x, int y, int z) {
    /**
     * Returns true if this point is in ([0, w), [0, h), [0, d)) 
     * @param w
     * @param h
     * @param d
     * @return
     */
    public boolean checkBounds(int w, int h, int d) {
        return this.x >= 0 && this.x < w && this.y >= 0 && this.y < h && this.z >= 0 && this.z < d;
    }
}
