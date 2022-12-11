import static java.lang.Math.min;

public class teacups {
    public void helper(){
        // Cup value array 1 by group size (12 entries)
        int[] valueArray1 = new int[]{1, 3, 5, 9, 10, 15, 17, 18, 19, 22, 25, 27};

// Cup value array 2 by group size (12 entries)
        int[] valueArray2 = new int[]{2, 5, 8, 9, 10, 15, 19, 23, 24, 29, 30, 32};

        cupInfo returnValue = new cupInfo(0, "");

// Part 1: print all set combinations for a set size of 10
        returnValue = cupSets(10, "", 1, 12, valueArray1, 0, true);


// Part 2: print max sale price results for both value data sets
        System.out.println("");
        System.out.println("Results for value array 1:");

        for(int i = 1; i <= 24; i++) {
            returnValue = cupSets(i,"",1,12,valueArray1,0,false);

            System.out.println("Best value sum for tea set size: " + i + " = $" + returnValue.maxValue + "  " + returnValue.maxSetList);
        }
        System.out.println(" ");
        System.out.println("Results for value array 2:");

        for(int i = 1; i <= 24; i++) {
            returnValue = cupSets(i,"",1,12,valueArray2,0,false);
            System.out.println("Best value sum for tea set size: " + i + " = $" + returnValue.maxValue + "  " + returnValue.maxSetList);
        }

    }

    public cupInfo cupSets(int cupQty, String setList, int minSet, int maxSet, int[] valueArray, int value, boolean prnt) {
        // Tea cup sales optimization program
        // Arguments:
        // cupQty = Number of cups still available for placing
        // setList = String with the listing of prior grouping choices
        // minSet = Minimum set size available for consideration
        // maxSet = Maximum set size available for consideration
        // valueArray = Array with $ values for each group size
        // value = Total $ value of prior group choices
        // prnt = Print flag

        String maxSetList = "";
        int maxValue = 0;

        String newSetList = "";
        int newValue;
        cupInfo current = new cupInfo(0, "");
        int newMaxSet;

        if (cupQty == 0) {
            // Found a workable combo
            maxSetList = setList;
            maxValue = value;
            if (prnt) {
                System.out.println(" Case combination: " + maxSetList);
            }
        } else if (cupQty >= minSet) {
            // If there are more cups than the min set size
            // iterate through the possible sizes
            newMaxSet = min(cupQty, maxSet);

            for (int i = minSet; i <= newMaxSet; i++) {
                newSetList = setList + " " + i;
                newValue = value + valueArray[i - 1];
                current = cupSets(cupQty - i, newSetList, i, maxSet, valueArray, newValue, prnt);
                if (current.maxValue > maxValue) {
                    maxValue = current.maxValue;
                    maxSetList = current.maxSetList;
                }
            }
        }
        return new cupInfo(maxValue, maxSetList);
    }
}
