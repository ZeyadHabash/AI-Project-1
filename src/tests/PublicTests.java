package tests;


import code.WaterSortSearch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class PublicTests {

    String grid0 = "3;" +
            "4;" +
            "r,y,r,y;" +
            "y,r,y,r;" +
            "e,e,e,e;";
    String grid1 = "5;" +
            "4;" +
            "b,y,r,b;" +
            "b,y,r,r;" +
            "y,r,b,y;" +
            "e,e,e,e;" +
            "e,e,e,e;";
    String grid2 = "5;" +
            "4;" +
            "b,r,o,b;" +
            "b,r,o,o;" +
            "r,o,b,r;" +
            "e,e,e,e;" +
            "e,e,e,e;";
    String grid3 = "6;" +
            "4;" +
            "g,g,g,r;" +
            "g,y,r,o;" +
            "o,r,o,y;" +
            "y,o,y,b;" +
            "r,b,b,b;" +
            "e,e,e,e;";
    String grid4 = "6;" +
            "3;" +
            "r,r,y;" +
            "b,y,r;" +
            "y,b,g;" +
            "g,g,b;" +
            "e,e,e;" +
            "e,e,e;";

    @Test(timeout = 60000)
    public void testa0() throws Exception {
        String solution = WaterSortSearch.solve(grid0, "BF", false);
        solution = solution.replace(" ", "")+";";
        System.out.println(solution);
        Checker pc = new Checker(grid0);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid0, solution));
    }
    @Test(timeout = 60000)
    public void testa1() throws Exception {
        String solution = WaterSortSearch.solve(grid1, "BF", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid1);
assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid1, solution));
    }
    @Test(timeout = 60000)
    public void testa2() throws Exception {
        String solution = WaterSortSearch.solve(grid2, "BF", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid2);
        System.out.println(solution);

        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid2, solution));
    }
    @Test(timeout = 60000)
    public void testa3() throws Exception {
        String solution = WaterSortSearch.solve(grid3, "BF", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid3);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid3, solution));
    }

    @Test(timeout = 60000)
    public void testa4() throws Exception {
        String solution = WaterSortSearch.solve(grid4, "BF", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid4);
        System.out.println(solution);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid4, solution));
    }

    @Test(timeout = 60000)
    public void testb0() throws Exception {
        String solution = WaterSortSearch.solve(grid0, "DF", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid0);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid0, solution));
    }

    @Test(timeout = 60000)
    public void testb1() throws Exception {
        String solution = WaterSortSearch.solve(grid1, "DF", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid1);
assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid1, solution));
    }

    @Test(timeout = 60000)
    public void testb2() throws Exception {
        String solution = WaterSortSearch.solve(grid2, "DF", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid2);
        assertTrue("The output actions do not lead to a goal state.",  pc.applyPlan(grid2, solution));
    }

    @Test(timeout = 60000)
    public void testb3() throws Exception {
        String solution = WaterSortSearch.solve(grid3, "DF", false);
        solution = solution.replace(" ", "")+";";
        System.out.println(solution);
        Checker pc = new Checker(grid3);
        assertTrue("The output actions do not lead to a goal state.",pc.applyPlan(grid3, solution));
    }

    @Test(timeout = 60000)
    public void testb4() throws Exception {
        String solution = WaterSortSearch.solve(grid4, "DF", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid4);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid4, solution));
    }


    @Test(timeout = 60000)
    public void testc0() throws Exception {
        String solution = WaterSortSearch.solve(grid0, "UC", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid0);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid0, solution));
    }
    @Test(timeout = 60000)
    public void testc1() throws Exception {
        String solution = WaterSortSearch.solve(grid1, "UC", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid1);
assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid1, solution));
    }

    @Test(timeout = 60000)
    public void testc2() throws Exception {
        String solution = WaterSortSearch.solve(grid2, "UC", false);
        solution = solution.replace(" ", "")+";";
        System.out.println(solution);
        Checker pc = new Checker(grid2);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid2, solution));
    }

    @Test(timeout = 60000)
    public void testc3() throws Exception {
        String solution = WaterSortSearch.solve(grid3, "UC", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid3);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid3, solution));
    }

    @Test(timeout = 60000)
    public void testc4() throws Exception {
        String solution = WaterSortSearch.solve(grid4, "UC", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid4);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid4, solution));
    }

    @Test(timeout = 60000)
    public void testd0() throws Exception {
        String solution = WaterSortSearch.solve(grid0, "ID", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid0);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid0, solution));
    }

    @Test(timeout = 60000)
    public void testd1() throws Exception {
        String solution = WaterSortSearch.solve(grid1, "ID", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid1);
assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid1, solution));
    }

    @Test(timeout = 60000)
    public void testd2() throws Exception {
        String solution = WaterSortSearch.solve(grid2, "ID", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid2);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid2, solution));
    }

    @Test(timeout = 60000)
    public void testd3() throws Exception {
        String solution = WaterSortSearch.solve(grid3, "ID", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid3);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid3, solution));
    }

    @Test(timeout = 60000)
    public void testd4() throws Exception {
        String solution = WaterSortSearch.solve(grid4, "ID", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid4);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid4, solution));
    }


    @Test(timeout = 60000)
    public void teste0() throws Exception {
        String solution = WaterSortSearch.solve(grid0, "GR1", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid0);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid0, solution));
    }

    @Test(timeout = 60000)
    public void teste1() throws Exception {
        String solution = WaterSortSearch.solve(grid1, "GR1", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid1);
assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid1, solution));
    }

    @Test(timeout = 60000)
    public void teste2() throws Exception {
        String solution = WaterSortSearch.solve(grid2, "GR1", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid2);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid2, solution));
    }

    @Test(timeout = 60000)
    public void teste3() throws Exception {
        String solution = WaterSortSearch.solve(grid3, "GR1", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid3);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid3, solution));
    }

    @Test(timeout = 60000)
    public void teste4() throws Exception {
        String solution = WaterSortSearch.solve(grid4, "GR1", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid4);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid4, solution));
    }


    @Test(timeout = 60000)
    public void testf0() throws Exception {
        String solution = WaterSortSearch.solve(grid0, "GR2", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid0);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid0, solution));
    }

    @Test(timeout = 60000)
    public void testf1() throws Exception {
        String solution = WaterSortSearch.solve(grid1, "GR2", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid1);
assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid1, solution));
    }

    @Test(timeout = 60000)
    public void testf2() throws Exception {
        String solution = WaterSortSearch.solve(grid2, "GR2", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid2);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid2, solution));
    }

    @Test(timeout = 60000)
    public void testf3() throws Exception {
        String solution = WaterSortSearch.solve(grid3, "GR2", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid3);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid3, solution));
    }

    @Test(timeout = 60000)
    public void testf4() throws Exception {
        String solution = WaterSortSearch.solve(grid4, "GR2", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid4);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid4, solution));
    }

    @Test(timeout = 60000)
    public void testg0() throws Exception {
        String solution = WaterSortSearch.solve(grid0, "AS1", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid0);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid0, solution));
    }

    @Test(timeout = 60000)
    public void testg1() throws Exception {
        String solution = WaterSortSearch.solve(grid1, "AS1", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid1);
assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid1, solution));
    }

    @Test(timeout = 60000)
    public void testg2() throws Exception {
        String solution = WaterSortSearch.solve(grid2, "AS1", false);
        solution = solution.replace(" ", "")+";";
        System.out.println(solution);
        Checker pc = new Checker(grid2);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid2, solution));
    }

    @Test(timeout = 60000)
    public void     testg3() throws Exception {
        String solution = WaterSortSearch.solve(grid3, "AS1", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid3);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid3, solution));
    }

    @Test(timeout = 60000)
    public void testg4() throws Exception {
        String solution = WaterSortSearch.solve(grid4, "AS1", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid4);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid4, solution));
    }


    @Test(timeout = 60000)
    public void testh0() throws Exception {
        String solution = WaterSortSearch.solve(grid0, "AS2", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid0);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid0, solution));
    }

    @Test(timeout = 60000)
    public void testh1() throws Exception {
        String solution = WaterSortSearch.solve(grid1, "AS2", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid1);
assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid1, solution));
    }

    @Test(timeout = 60000)
    public void testh2() throws Exception {
        String solution = WaterSortSearch.solve(grid2, "AS2", false);
        solution = solution.replace(" ", "")+";";
        System.out.println(solution);
        Checker pc = new Checker(grid2);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid2, solution));
    }

    @Test(timeout = 60000)
    public void testh3() throws Exception {
        String solution = WaterSortSearch.solve(grid3, "AS2", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid3);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid3, solution));
    }

    @Test(timeout = 60000)
    public void testh4() throws Exception {
        String solution = WaterSortSearch.solve(grid4, "AS2", false);
        solution = solution.replace(" ", "")+";";
        Checker pc = new Checker(grid4);
        assertTrue("The output actions do not lead to a goal state.", pc.applyPlan(grid4, solution));
    }


}

class Checker{
    private char[][] a;
    private int b;
    Checker(String description){
        String[] c = description.split(";");
        int d = Integer.parseInt(c[0]);
        b = 0;
        int e = Integer.parseInt(c[1]);
        a = new char[d][e];
        for (int f = 0; f< a.length; f++){
            String[] g = c[2+f].split(",");
            for (int h = 0; h < a[f].length; h++){
                a[f][h] = g[h].charAt(0);
            }
        }
    }
    public boolean i(String[] j) {
        for (int a=0; a<j.length; a++){
            String[] k = j[a].split("_");
            int l = Integer.parseInt(k[1]);
            int m = Integer.parseInt(k[2]);
            char[] n = this.a[l];
            char[] o = this.a[m];

            int p = 0;
            while (p<n.length && n[p]=='e'){
                p++;
            }
            int q = o.length-1;
            while (q>=0 && o[q]!='e'){
                q--;
            }
            int r = 0;
            char s = n[p];
            if (o[q]!= 'e' && o[q]!=s){
                System.out.println("action that failed: "+j[a] +", order: "+a);
                return false;
            }
            while (p<n.length && q >= 0 && n[p]==s){
                n[p++] = 'e';
                o[q--] = s;
                r++;
            }
            this.b += r;
        }
        return t();
    }

    boolean t(){
        for (int u = 0; u< a.length; u++){
            char[] b = a[u];
            int v = b.length-1;
            char w = b[v];
            while (v>=0 && b[v]==w){
                v--;
            }
            if (v>=0  && b[v]!='e'){
                return false;
            }
        }
        return true;
    }

    public boolean applyPlan(String grid, String solution){
        boolean x = true;
        solution = solution.toLowerCase();
        if (solution.equals("nosolution")) {
            return false;
        }
//        System.out.println(solution);
        String[] y  = solution.split(";");
        String z = y[0];
        int _a = Integer.parseInt(y[1]);
        z.replace(" ", "");
        z.replace("\n", "");
        z.replace("\r", "");
        z.replace("\n\r", "");
        z.replace("\t", "");

        String[] _b = z.split(",");

        x = i(_b);
        if(!x) {
            return false;
        }
        return t() && this.b == _a;
    }
}

