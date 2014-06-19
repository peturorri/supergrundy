import java.util.LinkedList;
import java.util.Collections;

public class SuperGrundy {

    public static boolean report = false;
    public static int MAX_CALCULATION = 20;

    public static int nimSum(LinkedList<Integer> l) {
        if(l.size() == 0) {
            return 0;
        }

        int ns = 0;
        for(Integer i : l) {
            ns = ns ^ i;
        }
        return ns;
    }

    public static boolean checkSlope(LinkedList<Integer> l) {
        int last = Integer.MAX_VALUE;
        for(Integer i : l) {
            if(!(last > i)) {
                return false;
            }
            last = i;
        }
        return true;
    }

    public static LinkedList<LinkedList<Integer>> partitionFixed(int n, int len) {
        LinkedList<LinkedList<Integer>> x = partition(n, n, new LinkedList<Integer>());
        // We don't want the trivial one element partition.
        x.remove();
        // Remove permutations which are not of correct length or have repeat elements.
        LinkedList<LinkedList<Integer>> purified = new LinkedList<LinkedList<Integer>>();
        for(LinkedList<Integer> l : x) {
            if(l.size() == len && checkSlope(l)) {
                purified.add(l);
            }
        }
        if(report) {
            System.out.println("Partitions of " + n + ": " + purified);
        }
        return purified;
    }

    public static LinkedList<LinkedList<Integer>> partitionAny(int n, int maxLen) {
        LinkedList<LinkedList<Integer>> x = partition(n, n, new LinkedList<Integer>());
        // We don't want the trivial one element partition.
        x.remove();
        // Remove permutations which are too long or have repeat elements.
        LinkedList<LinkedList<Integer>> purified = new LinkedList<LinkedList<Integer>>();
        for(LinkedList<Integer> l : x) {
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
    public static LinkedList<LinkedList<Integer>> partition(int n, int max,
            LinkedList<Integer> prefix)
    {
        LinkedList<LinkedList<Integer>> l = new LinkedList<LinkedList<Integer>>();
        if (n == 0) {
            l.add((LinkedList<Integer>)prefix.clone());
            return l;
        }

        for (int i = Math.min(max, n); i >= 1; i--) {
            //l.append(partition(n-i, i, prefix + " " + i));
            LinkedList<Integer> newPrefix = (LinkedList<Integer>)prefix.clone();
            newPrefix.add(i);
            for(LinkedList<Integer> m : partition(n-i, i, newPrefix)) {
                l.add((LinkedList<Integer>)m.clone());
            }
        }
        return l;
    }

    public static int mex(LinkedList<Integer> l) {
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

        LinkedList<Integer> followSG = new LinkedList<Integer>();

        for(LinkedList<Integer> part : partitionFixed(n, split)) {
            LinkedList<Integer> sgValues = new LinkedList<Integer>();
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

        return mex(followSG);
    }

    public static int superGrundyAny(int n, int maxSplit) {
        if(n < 0) {
            System.out.println("wat. n < 0?");
            System.exit(1);
        }

        if(n <= 2) {
            return 0;
        }

        LinkedList<Integer> followSG = new LinkedList<Integer>();

        for(LinkedList<Integer> part : partitionAny(n, maxSplit)) {
            LinkedList<Integer> sgValues = new LinkedList<Integer>();
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

        return mex(followSG);
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
        LinkedList<LinkedList<Integer>> p = partition(M);
        for(LinkedList<Integer> l : p) {
            for(Integer i : l) {
                System.out.print(" " + i);
            }
            System.out.println();
        }

        System.exit(0);
        */

        /*
        LinkedList<Integer> x = new LinkedList<Integer>();
        x.add(2);
        x.add(4);
        x.add(0);
        x.add(1);
        System.out.println(x);
        System.out.println(mex(x));
        System.exit(0);
        //*/
        /*
        LinkedList<Integer> x = new LinkedList<Integer>();
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
