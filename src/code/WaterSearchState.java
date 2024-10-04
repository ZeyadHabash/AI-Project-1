package code;

public class WaterSearchState extends State {
    char[][] arrayOfTubes;
    int[] arrayOfTopPointers;
    int bottleCapacity;
    int numOfBottles;

    public WaterSearchState() {}

    public WaterSearchState(int numOfBottles, int bottleCapacity) {
        this.arrayOfTubes = new char[numOfBottles][bottleCapacity];
        this.arrayOfTopPointers = new int[numOfBottles];

        this.bottleCapacity = bottleCapacity;
        this.numOfBottles = numOfBottles;

        // initially all tubes are empty
        for (int i = 0; i < numOfBottles; i++) {
            arrayOfTopPointers[i] = -1;
            for (int j = bottleCapacity - 1; j >= 0; j--) {
                arrayOfTubes[i][j] = 'e';
            }
        }
    }

    public static WaterSearchState copy(WaterSearchState state) {
        WaterSearchState newState = new WaterSearchState();

        newState.arrayOfTubes = new char[state.numOfBottles][state.bottleCapacity];
        newState.arrayOfTopPointers = new int[state.numOfBottles];

        newState.bottleCapacity = state.bottleCapacity;
        newState.numOfBottles = state.numOfBottles;

        // copy the given state
        for (int i = state.numOfBottles - 1; i >= 0; i--) {
            newState.arrayOfTubes[i] = new char[state.bottleCapacity];
            newState.arrayOfTopPointers[i] = -1;

            // traverse the tube bottom up till you reach highest non-empty layer
            for (int j = state.bottleCapacity - 1; j >= 0; j--) {
                if (state.arrayOfTubes[i][j] != 'e') {
                    newState.arrayOfTopPointers[i] = j; // set the bottle top pointer to the heights layer that contains a color
                }

                newState.arrayOfTubes[i][j] = state.arrayOfTubes[i][j];
            }
        }

        return newState;
    }
}
