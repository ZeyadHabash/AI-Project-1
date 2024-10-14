package code;

/**
 * A testing class
 */
public class Driver {

    public static void main(String[] args) throws InterruptedException {
        String initialState = "5;4;b,y,r,b;b,y,r,r;y,r,b,y;e,e,e,e;e,e,e,e;";
//        System.out.println(WaterSortSearch.solve(initialState, "BF", false));
//        System.out.println(WaterSortSearch.solve(initialState, "DF", false));
//        System.out.println(WaterSortSearch.solve(initialState, "UC", false));
//        System.out.println(WaterSortSearch.solve(initialState, "ID", false));
//        System.out.println(WaterSortSearch.solve(initialState, "GR1", false));
        System.out.println(WaterSortSearch.solve(initialState, "GR2", false));
//        System.out.println(WaterSortSearch.solve(initialState, "AS1", false));
//        System.out.println(WaterSortSearch.solve(initialState, "AS2", false));
    }
}
