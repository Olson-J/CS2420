import java.util.Random;


public class TreeTester {
    // Test program
    public static void main(String[] args) {
        long seed = 436543;
        Random generator = new Random(seed);  // Don't use a seed if you want the 
        // numbers to be different each time
        final String ENDLINE = "\n";

        // test number 1
        Integer[] list1 = {25, 10, 60, 55, 58, 56, 14, 63, 8, 50, 6, 9,
                15,27,29,61};
        Tree<Integer> treeOne = new Tree<Integer>(list1, "TreeOne:",true);


        //Problem 1
        System.out.println(treeOne.toString());
        System.out.println(treeOne.toString2());

        // test tree 2
        Integer[] list2 = {4,5,6,8,33,16,80,121,22,25, 36};
        Tree<Integer> treeTwo = new Tree<Integer>(list2, "TreeTwo:", false);
        System.out.println(treeTwo.toString());


//        //Problem 2
        treeTwo.flip();
        treeTwo.changeName("Tree Two Now flipped");
        System.out.println( treeTwo.toString());
        treeTwo.flip();
        treeTwo.changeName("TreeTwo");
        System.out.println(treeTwo.toString());
        final int SIZE = 10;
        Integer[] list3 = new Integer[SIZE];
        for (int i = 0; i < SIZE ; i++) {
            int t = generator.nextInt(200);
            list3[i] = t;
        }
        Tree<Integer> treeThree = new Tree<Integer>(list3, "TreeThree:", true);
        System.out.println(treeThree.toString());


        //Problem 3
        System.out.println("Deepest Node of treeOne " + treeOne.deepestNode());
        System.out.println("Deepest Node of treeThree " + treeThree.deepestNode());


        //Problem 4
        int mylevel=3;
        System.out.println("Number nodes at level " + mylevel + " is " +
                treeThree.nodesInLevel(mylevel));
        mylevel=4;
        System.out.println("Number nodes at level " + mylevel + " is " +
                treeThree.nodesInLevel(mylevel));


        //Problem 5
        System.out.println("All paths from treeThree");
        treeThree.printAllPaths();
        Integer[] list4= {21, 8, 25, 6, 7, 19, 10, 40, 43, 52, 64, 80};
        Tree<Integer> treeFour = new Tree<Integer>(list4, "treeFour", false);


        System.out.println(treeFour.toString());
        //Problem 6
        treeFour.pruneK(60);
        treeFour.changeName("treeFour after pruning 60");
        System.out.println(treeFour.toString());
        System.out.println(treeTwo.toString());
        treeTwo.pruneK(290);
        treeTwo.changeName("treeTwo after pruning 290");
        System.out.println(treeTwo.toString());


        //Problem 7
        System.out.println(treeOne.toString());
        System.out.println("treeOne Least Common Ancestor of (10,15) " +
                treeOne.lca(10, 15) + ENDLINE);
        System.out.println("treeOne Least Common Ancestor of (55,61) " +
                treeOne.lca(55, 61) + ENDLINE);
        System.out.println("treeOne Least Common Ancestor of (9,50) " +
                treeOne.lca(9, 50) + ENDLINE);
        System.out.println("treeOne Least Common Ancestor of (29,61) " +
                treeOne.lca(29, 61) + ENDLINE);


        //Problem 8
        Integer[] v8 = {15, 1,2,3,5,10, 65, 66,67,68,83, 70, 90,69,6,8};
        Tree<Integer> treeFive = new Tree<Integer>(v8, "TreeFive:",true);
        System.out.println(treeFive.toString());
        treeFive.balanceTree();
        treeFive.changeName("treeFive after balancing");
        System.out.println(treeFive.toString());


        //Problem 9
        System.out.println(treeOne.toString());
        treeOne.keepRange(14, 50);
        treeOne.changeName("treeOne after keeping only nodes between 14 and 50");
        System.out.println(treeOne.toString());
        treeFive.changeName("treeFive");
        System.out.println(treeFive.toString());
        treeFive.keepRange(3, 69);
        treeFive.changeName("treeFive after keeping only nodes between 3  and 69");
        System.out.println(treeFive.toString());


        // Problem 10
        treeTwo = new Tree<Integer>(list2, "TreeTwo:", false);
        System.out.println(treeTwo.toString());
        System.out.println("treeTwo Contains BST: " + treeTwo.countBST());
        treeFour = new Tree<Integer>(list4, "treeFour", false);
        System.out.println(treeFour.toString());
        System.out.println("treeFour Contains BST: " + treeFour.countBST());

//
//        //Bonus
//        Integer[] inorder = {4, 2, 1, 7, 5, 8, 3, 6};
//        Integer[] preorder = {1, 2, 4, 3, 5, 7, 8, 6};
//        Tree<Integer> treeBonus = new Tree<Integer>("TreeBonus");
//        treeBonus.buildTreeTraversals(inorder, preorder);
//        treeBonus.changeName("TreeBonus built from inorder and preorder
//                traversals");
//                System.out.println(treeBonus.toString());
    }
}