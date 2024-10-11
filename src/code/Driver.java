package code;

/**
 * A testing class
 */
public class Driver {

    public static void main(String[] args) throws InterruptedException {
        // System.out.println(WaterSortSearch.solve("5;4;b,y,r,b;b,y,r,r;y,r,b,y;e,e,e,e;e,e,e,e;", "BF", false));
        System.out.println(WaterSortSearch.solve("5;4;b,y,r,b;b,y,r,r;y,r,b,y;e,e,e,e;e,e,e,e;", "BF", false));
        System.out.println(WaterSortSearch.solve("5;4;b,y,r,b;b,y,r,r;y,r,b,y;e,e,e,e;e,e,e,e;", "DF", false));
        System.out.println(WaterSortSearch.solve("5;4;b,y,r,b;b,y,r,r;y,r,b,y;e,e,e,e;e,e,e,e;", "UC", false));
        System.out.println(WaterSortSearch.solve("5;4;b,y,r,b;b,y,r,r;y,r,b,y;e,e,e,e;e,e,e,e;", "AS2", false));

    }
}
