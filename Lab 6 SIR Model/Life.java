/******************************************************************************
 *
 *  John Conway's Game of Life: object-oriented implementation
 *
 *  Compilation:  javac Life.java
 *  Execution:    java Life [int grid size] [double population density] [long frame duration] [int max generations]
 *
 *  Usage example:  java Life 20 0.33 250 100
 *      Animates a 20x20 grid of cells, about 33% of which are occupied at startup, and animates
 *      the grid at a speed of 250 milliseconds per frame (4 frames per second) for up to 100 generations.
 *
 *  Usage example: java Life
 *      Animates a Game of Life with the default simulation parameters: a 10x10 grid of cells, about
 *      60% of which are occupied at startup, and animates the grid at a speed of 500 ms per frame
 *      (2 frames per second) for up to 10 generations.
 *
 *  @author Caitrin Eaton, 2021
 ******************************************************************************/
public class Life {

    /**
     * @param grid LifeGrid, a 2D array of LifeCells that drives game mechanics
     * @param vis LifeVis, a visualization tool for LifeGrid
     */
    public static void simulate( LifeGrid grid, LifeVis vis ) {

        while( (grid.getFlux() > 0) && (grid.getGeneration() < grid.getMaxGenerations()) ){
            grid.step();
            vis.update();
        }
    }

    /**
     * @param size int, the number of LifeCells along the width (and height) of the square LifeGrid
     * @param maxGenerations int, the number of generations (time steps) at which the simulation will stop
     * @param populationDensity double, the fraction of cells which should be occupied at startup
     * @return grid LifeGrid, the 2D array of LifeCells that drives game mechanics
     */
    public static LifeGrid init_grid( int size, int maxGenerations, double populationDensity ){
        LifeGrid grid = new LifeGrid( size );
        grid.setMaxGenerations( maxGenerations );
        grid.populate( populationDensity );
        return grid;
    }

    /**
     * @param grid LifeGrid, the 2D array of LifeCells that drives game mechanics
     * @param frameDuration long, the duration in milliseconds of each frame in the animation
     * @return vis LifeVis, a graphical representation of the LifeGrid for the user's viewing pleasure
     */
    public static LifeVis init_vis(LifeGrid grid, long frameDuration ) {
        LifeVis vis = new LifeVis( grid );
        vis.setFrameTime( frameDuration );
        return vis;
    }

    /**
     * Runs a randomly initialized Game of LIfe.
     * @param args String[], command line arguments (optional):
     *             args[0] = grid size (int) in cells
     *             args[1] = population density (double) between 0.0 (unpopulated) and 1.0 (fully populated)
     *             args[2] = frame duration (long) in milliseconds
     *             args[3] = maximum simulation length (int) in number of generations (frames)
     */
    public static void main(String[] args) {

        // Establish default simulation parameters, in case the user doesn't supply all/any command line arguments.
        double populationDensity = 0.60;
        int gridSize = 10;
        int simLength = 10;
        long frameDuration = 500;

        // Parse command line arguments, allowing the user to customize the simulation
        if (args.length > 0) {
            gridSize = Integer.parseInt( args[0] );
        }
        if (args.length > 1) {
            populationDensity = Double.parseDouble( args[1] );
        }
        if (args.length > 2) {
            frameDuration = Long.parseLong( args[2] );
        }
        if (args.length > 3) {
            simLength = Integer.parseInt( args[3] );
        }

        // Use command line parameters (or default values, if still intact) to configure a new
        // game of Life, comprised of a set of LifeCells in a LifeGrid and a LifeVis display.
        LifeGrid grid = init_grid( gridSize, simLength, populationDensity );
        LifeVis vis = init_vis( grid, frameDuration );

        // Run the simulation
        simulate( grid, vis );
    }
}
