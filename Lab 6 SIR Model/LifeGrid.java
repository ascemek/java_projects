import java.util.Random;

/**
 *  Defines the grid used in John Conway's Game of Life: object-oriented implementation
 *
 *  Can be run directly for testing purposes. The execution command below will:
 *  Creates a 10x10 grid of cells, and progresses through 10 iterations of the game,
 *  demonstrating cell survival, birth, and death.
 *
 *  Compilation:  javac LifeGrid.java
 *  Execution:    java LifeGrid
 *
 *  @author Caitrin Eaton, 2021
 *
 */
public class LifeGrid implements Grid {

    // State-related grid attributes
    private int maxGenerations = 10;
    private int flux;
    private int generation;
    private final int size;
    private LifeCell[][] population;

    /**
     * Construct a new grid for the Game of Life.
     * @param size int, the width (and height) of the grid, in cells
     */
    public LifeGrid(int size) {
        this.size = size;
        this.flux = 0;
        this.generation = 0;
        this.population = new LifeCell[size][size]; // references are initially null
    }

    /**
     * Modify the number of generations (time steps) after which the simulation will end.
     * @param endOfTheWorld int, the max allowable number of generations in a simulation.
     */
    public void setMaxGenerations(int endOfTheWorld) {
        // TODO: Could stand to add some error checking, here, e.g. is endOfTheWorld > 0?
        this.maxGenerations = endOfTheWorld;
    }

    /**
     * Read the number of generations (time steps) after which the simulation will end.
     * @return maxGenerations int, the max allowable number of generations in a simulation.
     */
    public int getMaxGenerations() {
        return this.maxGenerations;
    }

    /**
     * Read the size of the square population array.
     * @return size int, the number of cells along the width (and height) of the square population array.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Read the current generation of this Game of Life.
     * @return generation int, the number of time steps that have passed in this Game of Life.
     */
    public int getGeneration() {
        return this.generation;
    }

    /**
     * Read the current flux of this Game of Life, e.g. to determine if it has reached stasis.
     * @return flux int, the number of cells that experienced state changes in the most recent generation.
     */
    public int getFlux() {
        return this.flux;
    }

    /**
     * Randomly initialize the grid's population.
     * @param density double, the percent of occupied cells
     */
    public void populate(double density) {
        double occupancy;
        Random occupier = new Random();
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                occupancy = occupier.nextDouble();
                if (occupancy < density) {
                    this.population[row][col] = new LifeCell(LifeCell.ALIVE);
                    this.flux++;
                } else {
                    this.population[row][col] = new LifeCell(LifeCell.DEAD);
                }
            }
        }
        formNeighborhoods();
    }

    /**
     * Connect cells to their neighbors.
     */
    private void formNeighborhoods() {

        // Each cell in the grid has its own neighborhood
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {

                // Add this cell's 8 Moore neighbors to its neighborhood.
                for (int dr = -1; dr < 2; dr++) {
                    for (int dc = -1; dc < 2; dc++) {

                        // A cell shouldn't be its own neighbor.
                        if (dr != 0 || dc != 0) {

                            // Watch out for out of bounds exceptions.
                            if ((row + dr >= 0 && row + dr < this.size) && (col + dc >= 0 && col + dc < this.size)) {

                                // This neighbor is safe to add!
                                this.population[row][col].addNeighbor(this.population[row + dr][col + dc]);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Pretty printing grid objects.
     * @return summary String, a description of the LifeGrid object's current state
     */
    public String toString() {
        String summary = "\nGeneration: " + this.generation + ", Flux: " + this.flux;
        for (int row = 0; row < this.size; row++) {
            summary += "\n";
            for (int col = 0; col < this.size; col++) {
                if ( this.population[row][col] == null ) {
                    summary += 'N';
                } else if ( this.population[row][col].getCurrentState() == LifeCell.ALIVE ) {
                    summary += 'O';
                } else if ( this.population[row][col].getCurrentState() == LifeCell.DEAD )  {
                    summary += ' ';
                } else {
                    summary += '?';
                }
            }
        }
        return summary;
    }

    /**
     * Apply the rules of John Conway's Game of Life to progress the simulation by 1 time step.
     */
    public void step( ){

        // Ask each cell to predict its own next state, tracking the number of cells that will change (flux)
        int flux = 0;
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                this.population[row][col].updateNextState();
                // Keep track of changing cells in order to detect stasis
                if (this.population[row][col].getCurrentState() != this.population[row][col].getNextState()) {
                    flux++;
                }
            }
        }

        // Ask each cell to update its own state
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                this.population[row][col].updateCurrentState();
            }
        }

        // Update grid-level attributes related to recent changes in the cell population
        this.flux = flux;
        this.generation += 1;
    }

    /**
     * Apply the rules of John Conway's Game of Life until the population achieves stasis or the max # generations.
     * @param verbose boolean, whether the simulation will print the grid's state in each time step
     */
    public void simulate( boolean verbose ) {
        this.generation = 0;
        this.flux = this.size * this.size;
        while((this.flux > 0) && (this.generation < this.maxGenerations)){
            step();                                         // Progress the simulation by 1 time step
            if (verbose) { System.out.println( this ); }    // Maybe the user wants to see what's going on
        }
    }

    /**
     * Retrieve a copy of the population's current state fields, using a boolean 2D array to protect the cells
     * themselves from unwanted manipulation.
     * @return snapshot boolean[][], a grid of current states
     */
    public boolean[][] getPopulation(){
        boolean[][] snapshot = new boolean[this.size][this.size];
        for (int row=0; row<this.size; row++){
            for (int col=0; col<this.size; col++){
                snapshot[row][col] = this.population[row][col].getCurrentState();
            }
        }
        return snapshot;
    }

    /**
     * Test the LifeCell class by running a small simulation.
     * @param args String[], command line arguments
     */
    public static void main( String[] args ){
        double population_density = 0.33;
        LifeGrid testGrid = new LifeGrid( 10 );
        System.out.println( "\nNEWLY CONSTRUCTED GRID OBJECT:\n" + testGrid );
        testGrid.setMaxGenerations( 10 );
        testGrid.populate( population_density );
        System.out.println( "\nINITIAL POPULATION:\n" + testGrid );
        System.out.println( "\nSIMULATION:\n");
        testGrid.simulate( true );
        System.out.println( "\nFINAL POPULATION:\n" + testGrid );
    }
}


