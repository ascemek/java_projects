/**
 * Lab 06: SIR Model v1
 * 
 * SIR MODEL BASED ON JOHN CONWOY'S GAME OF LIFE
 * This is the "top-level" class that ties all of the pieces of SIR
 * together and drives simulation.
 * 
 * Compilation:   javac SIR.java
 * Execution:     java SIR [double infectionRate] [double recoveryRate] [int maxDays] [int gridSize] [long frameDuration]
 * 
 * Usage example for SIRVis: java SIR 0.166 0.33 90 50
 * With default paramaters: java SIR 0.166 0.037 90 500 50
 * Usage example for SIRVisImg: java SIR 0.166 0.33 90 500
 * (use this with new grid^)
 * 
 * (Animates a 500x500 grid of cells, with an infection rate of 0.166 and a recovery rate of 0.033 
 * run for 90 days.) + generates a CSV file that has an auto-generated descriptive filenames.
 * 
 * @author Sami Cemek
 * Updated: 05/02/2021
 */

public class SIR implements CellularAutomaton  {

    private SIRGrid grid;
    //private SIRVis vis;
    private SIRVisImg vis;
    private int generation;
    private int maxGenerations;
    private SIRWriter writer;
    private int days;

    /**
     * Constructs a new SIR model.
     * @param infectionRate the probability (a double between 0.0 and 1.0) that a susceptible individual will become infected by each infected neighbor, each day. Defaults to 0.166.
     * @param recoveryRate the probability (a double between 0.0 and 1.0) that an infected individual will recover, each day. Defaults to 0.037.
     * @param maxDays maximum number of days the simulation runs (int). Defaults to 90.
     * @param gridSize the integer number of people (SIRCell objects) spanning the city, left to right. Defaults to 500.
     * @param frameDuration the long number of milliseconds for which each frame (day) will be displayed. Defaults to 50.
     */
    public SIR( double infectionRate, double recoveryRate, int maxDays, int gridSize, long frameDuration){
        // Initialize the grid of cells
        grid = new SIRGrid( gridSize );
        grid.populate( 0.01, infectionRate, recoveryRate); // 0.01 can be changed, it represents initial percentInfected

        // Initialize the simulation time
        generation = 0;
        maxGenerations = maxDays;

        // Initialize the visualization window in which the grid of cells will appear
        //vis = new SIRVis( grid );
        vis = new SIRVisImg( grid );
        vis.setFrameTime( frameDuration );

        days = maxDays;

        // Initialize the CSV writer that gives the percentages of S,I,R for each day
        writer = new SIRWriter();
        writer.open( maxDays, infectionRate, recoveryRate, gridSize );
    }

    /**
     * Simulate a single generation.
     */
    public void update() {
        generation++;
        grid.update();
        vis.update( generation );
        writer.update(generation, grid.getDemographics(), grid.getPercentages());
    }

    /**
     * Simulate the SIR model until either the cells stop changing (the grid has zero infectious) or the maximum
     * number of generations has been reached.
     */
    public void simulate() {
        generation = 0;
        while( (grid.getinf() > 0) && (generation < maxGenerations) ){
            update();
        }
        writer.close();
    }

    /**
     * Runs a randomly initialized SIR model.
     * @param args [double infectionRate] [double recoveryRate] [int maxDays] [int gridSize] [long frameDuration]
     *              args[0] = infectionRate (double) the probability that a susceptible individual will become infected by each infected neighbor, each day.
     *              args[1] = recoveryRate (double) the probability that an infected individual will recover, each day.
     *              args[2] = maxDays (int) maximum number of days the simulation runs.
     *              args[3] = grid size (int) in cells.
     *              args[4] = frameDuration (long) number of milliseconds for which each frame (day) will be displayed.
     */
    public static void main(String[] args) {

        // Establish default simulation parameters, in case the user doesn't supply all/any command line arguments.
        double infectionRate = 0.166;
        double recoveryRate = 0.037; 
        int maxDays = 90;
        int gridSize = 500;
        long frameDuration = 50;

        // Parse command line arguments, allowing the user to customize the simulation
        if (args.length > 0) {
            infectionRate = Double.parseDouble( args[0] );
        }
        if (args.length > 1) {
            recoveryRate = Double.parseDouble( args[1] );
        }
        if (args.length > 2) {
            maxDays = Integer.parseInt( args[2] );
        }
        if (args.length > 3) {
            gridSize = Integer.parseInt( args[3] );
        }
        if (args.length > 4) {
            frameDuration = Long.parseLong( args[4] );
        }

        // Use command line parameters (or default values, if still intact) to configure a new
        // SIR model, comprised of a set of SIRCells in a SIRGrid and a SIRVis display.
        SIR game = new SIR( infectionRate, recoveryRate, maxDays, gridSize, frameDuration );

        // Run the simulation
        game.simulate();

    }
}
