package global.util;

/**
 * An enum for directions. Can increment x/y.
 */
public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;
    
    /**
     * @return The opposite Direction
     */
    public Direction opposite() {
        return switch(this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST  -> WEST;
            case WEST  -> EAST;
        };
    }
    
    /**
     * Returns the change in pairity when turning from d to this direction.
     * When a walk around a well-formed loop is completed, pairity will be positive if the enclosed
     * space is on the right hand side, and negative if the enclosed space is on the left hand side
     * 
     * @param d
     * @return
     */
    public int pairity(Direction d) {
        return switch(d) {
            case NORTH  -> switch(this) {
                case NORTH  -> 0;   // no change
                case SOUTH  -> 0;   // invalid
                case EAST   -> 1;   // RH
                case WEST   -> -1;  // LH
            };
            case SOUTH  -> switch(this) {
                case NORTH  -> 0;   // invalid
                case SOUTH  -> 0;   // no change
                case EAST   -> -1;  // LH
                case WEST   -> 1;   // RH
            };
            case EAST   -> switch(this) {
                case NORTH  -> -1;  // LH
                case SOUTH  -> 1;   // RH
                case EAST   -> 0;   // no change
                case WEST   -> 0;   // invalid
            };
            case WEST   -> switch(this) {
                case NORTH  -> 1;   // RH
                case SOUTH  -> -1;  // LH
                case EAST   -> 0;   // invalid
                case WEST   -> 0;   // no change
            };
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
        };
    }
    
    /**
     * Converts a coordinate pair based on this direction 
     * 
     * @param coord
     * @return
     */
    public Coord2D convert2D(Coord2D coord, int d) {
        return new Coord2D(convertX(coord.x(), d), convertY(coord.y(), d));
    }
    
    /**
     * Converts a coordinate pair based on this direction 
     * 
     * @param coord
     * @return
     */
    public CoordF2D convertF2D(CoordF2D coord, long d) {
        return new CoordF2D(convertX(coord.x(), d), convertY(coord.y(), d));
    }
    
    /**
     * Converts a coordinate pair based on this direction 
     * 
     * @param coord
     * @return
     */
    public CoordL2D convertL2D(CoordL2D coord, long d) {
        return new CoordL2D(convertX(coord.x(), d), convertY(coord.y(), d));
    }
}