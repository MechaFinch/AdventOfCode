package global.util;

/**
 * An enum for directions. Can increment x/y.
 */
public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;
    
    public Direction opposite() {
        return switch(this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST  -> WEST;
            case WEST  -> EAST;
        };
    }
    
    /**
     * Converts X -> next X based on this direction
     */
    public int convertX(int x) {
        return switch(this) {
            case NORTH  -> x;
            case SOUTH  -> x;
            case EAST   -> x + 1;
            case WEST   -> x - 1;
        };
    }
    
    /**
     * Converts X -> next X based on this direction
     */
    public long convertX(long x) {
        return switch(this) {
            case NORTH  -> x;
            case SOUTH  -> x;
            case EAST   -> x + 1;
            case WEST   -> x - 1;
        };
    }
    
    /**
     * Converts Y -> next Y based on this direction
     */
    public int convertY(int y) {
        return switch(this) {
            case NORTH  -> y - 1;
            case SOUTH  -> y + 1;
            case EAST   -> y;
            case WEST   -> y;
        };
    }
    
    /**
     * Converts Y -> next Y based on this direction
     */
    public long convertY(long y) {
        return switch(this) {
            case NORTH  -> y - 1;
            case SOUTH  -> y + 1;
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
    public Coord2D convert2D(Coord2D coord) {
        return new Coord2D((int) convertX(coord.x()), (int) convertY(coord.y()));
    }
}