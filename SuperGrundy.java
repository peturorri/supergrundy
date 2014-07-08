import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SuperGrundy {

    public static boolean report = false;
    public static int MAX_CALCULATION = 60;

    private static HashMap<Integer, Integer> cachedSG =
        new HashMap<Integer, Integer>();

    public static int nimSum(ArrayList<Integer> l) {
        if(l.size() == 0) {
            return 0;
        }

        int ns = 0;
        for(Integer i : l) {
            ns = ns ^ i;
        }
        return ns;
    }

    public static boolean checkSlope(ArrayList<Integer> l) {
        int last = Integer.MAX_VALUE;
        for(Integer i : l) {
            if(!(last > i)) {
                return false;
            }
            last = i;
        }
        return true;
    }

    public static ArrayList<ArrayList<Integer>> partitionFixed(int n, int len) {
        ArrayList<ArrayList<Integer>> x = partition(n, n, new ArrayList<Integer>());
        // We don't want the trivial one element partition.
        x.remove(0);
        // Remove permutations which are not of correct length or have repeat elements.
        ArrayList<ArrayList<Integer>> purified = new ArrayList<ArrayList<Integer>>();
        for(ArrayList<Integer> l : x) {
            if(l.size() == len && checkSlope(l)) {
                purified.add(l);
            }
        }
        if(report) {
            System.out.println("Partitions of " + n + ": " + purified);
        }

        return purified;
    }

    public static ArrayList<ArrayList<Integer>> partitionAny(int n, int maxLen) {
        ArrayList<ArrayList<Integer>> x = partition(n, n, new ArrayList<Integer>());
        // We don't want the trivial one element partition.
        x.remove(0);
        // Remove permutations which are too long or have repeat elements.
        ArrayList<ArrayList<Integer>> purified = new ArrayList<ArrayList<Integer>>();
        for(ArrayList<Integer> l : x) {
            if(l.size() <= maxLen && checkSlope(l)) {
                purified.add(l);
            }
        }
        if(report) {
            System.out.println("Partitions of " + n + ": " + purified);
        }

        return purified;
    }

    // Suppress the warnings about unchecked casts in this method
    // (we're casting the results of 'clone' and should be safe)
    @SuppressWarnings("unchecked")
    public static ArrayList<ArrayList<Integer>> partition(int n, int max,
            ArrayList<Integer> prefix)
    {
        ArrayList<ArrayList<Integer>> l = new ArrayList<ArrayList<Integer>>();
        if (n == 0) {
            l.add(prefix);
            return l;
        }

        for (int i = Math.min(max, n); i >= 1; i--) {
            //l.append(partition(n-i, i, prefix + " " + i));
            ArrayList<Integer> newPrefix = (ArrayList<Integer>)prefix.clone();
            newPrefix.add(i);
            for(ArrayList<Integer> m : partition(n-i, i, newPrefix)) {
                l.add(m);
            }
        }

        return l;
    }

    public static int mex(ArrayList<Integer> l) {
        if(l.size() == 0) {
            return 0;
        }
        Collections.sort(l);

        int last = -1;
        int pos = 0;
        while(true) {
            if(l.size() <= pos) {
                return last+1;
            }
            int curr = l.get(pos);
            if(curr - last > 1) {
                return last+1;
            }
            last = curr;
            pos++;
        }
    }

    public static int superGrundyFixed(int n, int split) {
        if(n < 0) {
            System.out.println("wat. n < 0?");
            System.exit(1);
        }

        if(n <= 2) {
            return 0;
        }

        if(cachedSG.containsKey(n)) {
            return cachedSG.get(n);
        }

        ArrayList<Integer> followSG = new ArrayList<Integer>();

        for(ArrayList<Integer> part : partitionFixed(n, split)) {
            ArrayList<Integer> sgValues = new ArrayList<Integer>();
            for(Integer m : part) {
                sgValues.add(superGrundyFixed(m, split));
            }
            followSG.add(nimSum(sgValues));
        }

        if(report) {
            System.out.print("Follow SG values of " + n + ":");
            for(Integer x: followSG) {
                System.out.print(" " + x);
            }
            System.out.println(" | mex: " + mex(followSG));
        }

        int ret = mex(followSG);
        cachedSG.put(n, ret);
        return ret;
    }

    public static int superGrundyAny(int n, int maxSplit) {
        if(n < 0) {
            System.out.println("wat. n < 0?");
            System.exit(1);
        }

        if(n <= 2) {
            return 0;
        }

        if(cachedSG.containsKey(n)) {
            return cachedSG.get(n);
        }

        ArrayList<Integer> followSG = new ArrayList<Integer>();

        for(ArrayList<Integer> part : partitionAny(n, maxSplit)) {
            ArrayList<Integer> sgValues = new ArrayList<Integer>();
            for(Integer m : part) {
                sgValues.add(superGrundyAny(m, maxSplit));
            }
            followSG.add(nimSum(sgValues));
        }

        if(report) {
            System.out.print("Follow SG values of " + n + ":");
            for(Integer x: followSG) {
                System.out.print(" " + x);
            }
            System.out.println(" | mex: " + mex(followSG));
        }

        int ret = mex(followSG);
        cachedSG.put(n, ret);
        return ret;
    }

    public static void main(String[] args) {


        if(args.length < 2) {
            System.out.println("Usage: java SuperGrundy <mode> <M>");
            System.out.println("    where mode can be 'any' or 'fixed'");
            System.out.println("    and M is the maximum split");
            System.exit(0);
        }

        int M = 0;
        try {
            M = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException e) {
            System.out.println("M should be a number > 2.");
            System.exit(0);
        }
        if(M < 2) {
            System.out.println("M too small: " + M + ". Must be at least 2.");
            System.exit(0);
        }


        /*
        ArrayList<ArrayList<Integer>> p = partition(M);
        for(ArrayList<Integer> l : p) {
            for(Integer i : l) {
                System.out.print(" " + i);
            }
            System.out.println();
        }

        System.exit(0);
        */

        /*
        ArrayList<Integer> x = new ArrayList<Integer>();
        x.add(2);
        x.add(4);
        x.add(0);
        x.add(1);
        System.out.println(x);
        System.out.println(mex(x));
        System.exit(0);
        //*/
        /*
        ArrayList<Integer> x = new ArrayList<Integer>();
        x.add(2);
        x.add(4);
        x.add(3);
        x.add(1);
        System.out.println(nimSum(x));
        System.exit(0);
        */


        String mode = args[0];
        if(mode.equals("any")) {
            System.out.println("Calculating SG values for any split with M = " + M);

            for(int i = 0; i < MAX_CALCULATION; i++) {
                if(report) {
                    System.out.println("---SG("+i+"): " + superGrundyAny(i, M));
                }
                else {
                    System.out.println(superGrundyAny(i, M));
                }
            }
        }
        else if(mode.equals("fixed")) {
            System.out.println("Calculating SG values for fixed split with M = " + M);
            for(int i = 0; i < MAX_CALCULATION; i++) {
                if(report) {
                    System.out.println("---SG("+i+"): " + superGrundyFixed(i, M));
                }
                else {
                    System.out.println(superGrundyFixed(i, M));
                }
            }
        }
        else {
            System.out.println("Invalid mode " + mode);
            System.exit(0);
        }

    }
}
