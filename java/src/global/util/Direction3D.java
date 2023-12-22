package global.util;

/**
 * An enum for directions. Can increment x/y/z.
 */
public enum Direction3D {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    UP,
    DOWN;
    
    /**
     * @return The opposite Direction
     */
    public Direction3D opposite() {
        return switch(this) {
            case NORTH  -> SOUTH;
            case SOUTH  -> NORTH;
            case EAST   -> WEST;
            case WEST   -> EAST;
            case UP     -> DOWN;
            case DOWN   -> UP;
        };
    }
    
    /**
     * Converts X -> next X based on this direction
     */
    public int convertX(int x, int d) {
        return switch(this) {
            case NORTH  -> x;
            case SOUTH  -> x;
            case EAST   -> x + d;
            case WEST   -> x - d;
            case UP     -> x;
            case DOWN   -> x;
        };
    }
    
    /**
     * Converts X -> next X based on this direction
     */
    public long convertX(long x, long d) {
        return switch(this) {
            case NORTH  -> x;
            case SOUTH  -> x;
            case EAST   -> x + d;
            case WEST   -> x - d;
            case UP     -> x;
            case DOWN   -> x;
        };
    }
    
    /**
     * Converts X -> next X based on this direction
     */
    public double convertX(double x, double d) {
        return switch(this) {
            case NORTH  -> x;
            case SOUTH  -> x;
            case EAST   -> x + d;
            case WEST   -> x - d;
            case UP     -> x;
            case DOWN   -> x;
        };
    }
    
    /**
     * Converts Y -> next Y based on this direction
     */
    public int convertY(int y, int d) {
        return switch(this) {
            case NORTH  -> y - d;
            case SOUTH  -> y + d;
            case EAST   -> y;
            case WEST   -> y;
            case UP     -> y;
            case DOWN   -> y;
        };
    }
    
    /**
     * Converts Y -> next Y based on this direction
     */
    public long convertY(long y, long d) {
        return switch(this) {
            case NORTH  -> y - d;
            case SOUTH  -> y + d;
            case EAST   -> y;
            case WEST   -> y;
            case UP     -> y;
            case DOWN   -> y;
        };
    }
    
    /**
     * Converts Y -> next Y based on this direction
     */
    public double convertY(double y, double d) {
        return switch(this) {
            case NORTH  -> y - d;
            case SOUTH  -> y + d;
            case EAST   -> y;
            case WEST   -> y;
            case UP     -> y;
            case DOWN   -> y;
        };
    }
    
    /**
     * Converts Z -> next Z based on this direction
     */
    public int convertZ(int z, int d) {
        return switch(this) {
            case NORTH  -> z;
            case SOUTH  -> z;
            case EAST   -> z;
            case WEST   -> z;
            case UP     -> z + d;
            case DOWN   -> z - d;
        };
    }
    
    /**
     * Converts Z -> next Z based on this direction
     */
    public long convertZ(long z, long d) {
        return switch(this) {
            case NORTH  -> z;
            case SOUTH  -> z;
            case EAST   -> z;
            case WEST   -> z;
            case UP     -> z + d;
            case DOWN   -> z - d;
        };
    }
    
    /**
     * Converts Z -> next Z based on this direction
     */
    public double convertZ(double z, double d) {
        return switch(this) {
            case NORTH  -> z;
            case SOUTH  -> z;
            case EAST   -> z;
            case WEST   -> z;
            case UP     -> z + d;
            case DOWN   -> z - d;
        };
    }
    
    /**
     * Converts a coordinate pair based on this direction 
     * 
     * @param coord
     * @return
     */
    public Coord3D convert3D(Coord3D coord, int d) {
        return new Coord3D(convertX(coord.x(), d), convertY(coord.y(), d), convertZ(coord.z(), d));
    }
    
    /**
     * Converts a coordinate pair based on this direction 
     * 
     * @param coord
     * @return
     */
    public CoordF3D convertF3D(CoordF3D coord, long d) {
        return new CoordF3D(convertX(coord.x(), d), convertY(coord.y(), d), convertZ(coord.z(), d));
    }
    
    /**
     * Converts a coordinate pair based on this direction 
     * 
     * @param coord
     * @return
     */
    public CoordL3D convertL3D(CoordL3D coord, long d) {
        return new CoordL3D(convertX(coord.x(), d), convertY(coord.y(), d), convertZ(coord.z(), d));
    }
}