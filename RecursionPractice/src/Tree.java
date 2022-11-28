// ******************ERRORS********************************
// Throws UnderflowException as appropriate

import java.util.ArrayList;
import java.util.Collections;

class UnderflowException extends RuntimeException {
    /**
     * Construct this exception object.
     *
     * @param message the error message.
     */
    public UnderflowException(String message) {
        super(message);
    }
}

public class Tree<E extends Comparable<? super E>> {
    private BinaryNode<E> root;  // Root of tree
    private String treeName;     // Name of tree

    /**
     * Create an empty tree
     * @param label Name of tree
     */
    public Tree(String label) {
        treeName = label;
        root = null;
    }

    /**
     * Create non ordered tree from list in preorder
     * @param arr   List of elements
     * @param label Name of tree
     */
    public Tree(E[] arr, String label, boolean ordered) {
        treeName = label;
        if (ordered) {
            root = null;
            for (int i = 0; i < arr.length; i++) {
                bstInsert(arr[i]);
            }
        } else root = buildUnordered(arr, 0, arr.length - 1);
    }

    /**
     * Build a NON BST tree by preorder
     * @param arr nodes to be added
     * @return new tree
     */
    private BinaryNode<E> buildUnordered(E[] arr, int low, int high) {
        if (low > high) return null;
        int mid = (low + high) / 2;
        BinaryNode<E> curr = new BinaryNode<>(arr[mid], null, null);
        curr.left = buildUnordered(arr, low, mid - 1);
        curr.right = buildUnordered(arr, mid + 1, high);
        return curr;
    }
    /**
     * Create BST from Array
     * @param arr   List of elements to be added
     * @param label Name of  tree
     */
    public Tree(E[] arr, String label) {
        root = null;
        treeName = label;
        for (int i = 0; i < arr.length; i++) {
            bstInsert(arr[i]);
        }
    }

    /**
     * Change name of tree
     * @param name new name of tree
     */
    public void changeName(String name) {
        this.treeName = name;
    }

    /**
     * Return a string displaying the tree contents as a tree with one node per line
     * SIZE COMPLEXITY: O(n)
     */
    public String toString() {
        if (root == null)
            return (treeName + " Empty tree\n");
        else
            return treeName + "\n" + toString2(root, "");
    }

    /**
     * Create a string displaying the tree contents as a tree with one node per line
     * SIZE COMPLEXITY: O(n)
     */
    private String toString(BinaryNode<E> t, String indent) {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }


    /**
     * Return a string displaying the tree contents as a single line
     * SIZE COMPLEXITY: O(n)
     */
    public String toString2() {
        if (root == null)
            return treeName + " Empty tree";
        else
            return treeName + " " + toString2(root, "");
    }

    /**
     * Internal method to return a string of items in the tree in order
     * SIZE COMPLEXITY: O(n)
     *
     * @param t the node that roots the subtree.
     * @param spaces the number of spaces needed to maintain formatting
     */

    private String toString2(BinaryNode<E> t, String spaces) {
        if (t == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(toString2(t.right, spaces + " "));

        sb.append(spaces + " " + t.element.toString() + "\n");
        sb.append(toString2(t.left, spaces + " "));
        return sb.toString();
    }

    /**
     * return the deepest node of a binary tree
     * SIZE COMPLEXITY: O(n)
     */
    public String deepestNode() {
        Object[] array1;
        if (root == null) {
            return "Empty tree";
        }
        array1 = deepestNode2(root, 0);
        return String.valueOf(array1[1]);
    }

    /**
     * fin d and return the deepest node of a binary tree
     * SIZE COMPLEXITY: O(log n)
     * @params t place in binary tree
     * @params level level of tree node is on
     */
    public Object[] deepestNode2(BinaryNode<E> t, int level) {
        Object[] arrayLeft = {0,0};
        Object[] arrayRight = {0,0};
        if (t.right != null && t.left == null) {
            arrayRight = deepestNode2(t.right, level + 1);
            return arrayRight;
        } else if (t.right == null && t.left != null) {
            arrayLeft = deepestNode2(t.left, level + 1);
            return arrayLeft;
        } else if (t.right != null && t.left != null) {
            arrayLeft = deepestNode2(t.left, level + 1);
            arrayRight = deepestNode2(t.right, level + 1);
            if ((int ) arrayLeft[0] > (int) arrayRight[0]){
                return arrayLeft;
            } else {
                return arrayRight;
            }
        } else {
            arrayLeft[0] = level;
            arrayLeft[1] = t.element;
            return arrayLeft;
        }
    }


    /**
     * reverse left and right children recursively
     * SIZE COMPLEXITY: O(n)
     */
    public void flip() {
        if (root != null){
            flip2(root);
        }
    }

    /**
     * Internal method to recursively flip tree sections
     *SIZE COMPLEXITY: O(n)
     * @param t the node that roots the subtree.
     */

    private void flip2(BinaryNode<E> t) {
        BinaryNode<E> hold;
        if (t != null) {
            flip2(t.right);
            flip2(t.left);
            hold = t.right;
            t.right = t.left;
            t.left = hold;
        }
    }

    /**
     * Counts number of nodes in specified level
     * @param search Level in tree, root is zero
     * @return count of number of nodes at specified level
     */
    public int nodesInLevel(int search) {
        if (root == null) {
            return 0;
        }
        return nodesInLevel2(root, 0, search);
    }


    /**
     * Counts number of nodes in specified level
     * @param t node in tree
     * @param level current tree level
     * @param search desired level
     * @return count of number of nodes at specified level
     * SIZE COMPLEXITY: O(n)
     */
    public int nodesInLevel2(BinaryNode<E> t, int level, int search) {
        int left = 0;
        int right = 0;
        // if possible to go more right
        if (t.right != null) {
            right = nodesInLevel2(t.right, level + 1, search);
        }
        if (t.left != null) {
            left = nodesInLevel2(t.left, level + 1, search);
        }
        if (level == search) {
            return left + right + 1;
        } else {
            return left + right;
        }
    }

    /**
     * Print all paths from root to leaves
     * SIZE COMPLEXITY: O(n)
     */
    public void printAllPaths() {
        StringBuilder list = new StringBuilder();
        if (root == null) {
            System.out.println(treeName + " Empty tree");
        }
        else {
            pathFinder(root, list);
        }
    }

    /**
     * find and return all paths from root to leaves
     * @params t node in tree
     * @params list list of past nodes
     * SIZE COMPLEXITY: O(n)
     */
    public void pathFinder(BinaryNode<E> t, StringBuilder list) {
        StringBuilder list1 = new StringBuilder();
        StringBuilder list2 = new StringBuilder();
        list1.append(list + " " + t.element);
        list2.append(list + " " + t.element);

        if (t.left != null){
            pathFinder(t.left, list1);
        }
        if (t.right != null) {
            pathFinder(t.right, list2);
        }
        if (t.left == null && t.right == null) {
            System.out.println(list1.toString());
        }
    }


    /**
     * Counts all non-null binary search trees embedded in tree
     * @return Count of embedded binary search trees
     */
    public Integer countBST() {
        if (root == null) return 0;
        return -1;
    }

    /**
     * Insert into a bst tree; duplicates are allowed
     * @param x the item to insert.
     */
    public void bstInsert(E x) {

        root = bstInsert(x, root);
    }

    /**
     * Internal method to insert into a subtree.
     * In tree is balanced, this routine runs in O(log n)
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<E> bstInsert(E x, BinaryNode<E> t) {
        if (t == null)
            return new BinaryNode<E>(x, null, null);
        int compareResult = x.compareTo(t.element);
        if (compareResult < 0) {
            t.left = bstInsert(x, t.left);
        } else {
            t.right = bstInsert(x, t.right);
        }
        return t;
    }

    /**
     * Determines if item is in tree
     * @param item the item to search for.
     * @return true if found.
     */
    public boolean contains(E item) {
        return contains(item, root);
    }

    /**
     * Internal method to find an item in a subtree.
     * This routine runs in O(log n) as there is only one recursive call that is executed and the work
     * associated with a single call is independent of the size of the tree: a=1, b=2, k=0
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * @return node containing the matched item.
     */
    private boolean contains(E x, BinaryNode<E> t) {
        if (t == null)
            return false;

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            return contains(x, t.left);
        else if (compareResult > 0)
            return contains(x, t.right);
        else {
            return true;    // Match
        }
    }
    /**
     * Remove all paths from tree that sum to less than given value
     * @param sum: minimum path sum allowed in final tree
     * SIZE COMPLEXITY: O(n)
     */
    public void pruneK(Integer sum) {
        if (root != null) {
            if (!pruneK2((BinaryNode<Integer>) root, sum, 0)) {
                root = null;
            }
        }
    }

    /**
     * Remove all paths from tree that sum to less than given value
     * @param t node in tree
     * @param sum: minimum path sum allowed in final tree
     * @param total running total of path
     * SIZE COMPLEXITY: O(n)
     */
    public boolean pruneK2(BinaryNode<Integer> t, Integer sum, int total) {
        boolean left = false;
        boolean right = false;

        if (t.left != null){
            left = pruneK2(t.left, sum,t.element + total);
            if (!left) {
                t.left = null;
            }
        }
        if (t.right != null){
            right = pruneK2(t.right, sum, t.element + total);
            if (!right) {
                t.right = null;
            }
        }
        if (t.left == null && t.right == null) {
            return ((total + t.element) >= sum);
        } else {
            return true;
        }
    }
    /**
     * Build tree given inOrder and preOrder traversals.  Each value is unique
     * @param inOrder  List of tree nodes in inorder
     * @param preOrder List of tree nodes in preorder
     */
    public void buildTreeTraversals(E[] inOrder, E[] preOrder) {
        root = null;
    }

    /**
     * Find the least common ancestor of two nodes
     * @param a first node
     * @param b second node
     * @return String representation of ancestor
     * SIZE COMPLEXITY: O(n)
     */
    public String lca(E a, E b) {
        BinaryNode<E> ancestor = null;
        if (root == null) {
            return "Empty tree";
        }
        if (lca2(root, a, b) != null){
            return lca2(root, a, b).toString();
        }
        else {
            return "none";
        }
    }

    /**
     * Find and return  the least common ancestor of two nodes
     * @param a first node
     * @param b second node
     * @param t node in tree
     * @return String representation of ancestor
     * SIZE COMPLEXITY: O(log n)
     */
    public E lca2(BinaryNode<E> t, E a,E b ){
        E right = null;
        E left = null;
        E check = null;

        if (t.element == a || t.element == b) {
            check = t.element;
        }
        if (t.right != null) {

            right = lca2(t.right, a, b);
        }
        if (t.left != null) {

            left = lca2(t.left, a, b);
        }
        if (right != null && left != null){
            return t.element;
        } else if (check != null) {
            return check;
        } else if (right != null) {
            return right;
        } else if (left != null) {
            return left;
        }
        return null;
    }

    /**
     * Balance the tree
     * SIZE COMPLEXITY: O(n)
     */
    public void balanceTree() {
        ArrayList<Integer> nodeList = new ArrayList<Integer>();
        if (root != null) {
            balanceTree2(root, nodeList);
            Collections.sort(nodeList);
            Integer[] v9 = {};
            Tree<Integer> treeFive = new Tree<Integer>(nodeList.toArray(v9), this.treeName, false);
            root = (BinaryNode<E>) treeFive.root;
        }

    }

    /**
     * Balance the tree
     * SIZE COMPLEXITY: O(n)
     */
    public void balanceTree2(BinaryNode<E> t, ArrayList nodeList){
        nodeList.add(t.element);
        if (t.right != null) {
            balanceTree2(t.right, nodeList);
        }
        if (t.left != null) {
            balanceTree2(t.left, nodeList);
        }
    }

    /**
     * In a BST, keep only nodes between range
     *
     * @param a lowest value
     * @param b highest value
     */
    public void keepRange(E a, E b) {
        if (root != null) {
            if (!keepRange2(root, a, b)){
                root = null;
            }
        }
    }


    /**
     * fiind and keep only nodes in range
     *
     * @param a lowest value
     * @param b highest value
     */
    public boolean keepRange2(BinaryNode<E> t, E a, E b) {
        boolean left = false;
        boolean right = false;

        if (t.left != null){
            left = keepRange2(t.left, a, b);
            if (!left) {
                t.left = null;
                left = false;
            }
        }
        if (t.right != null) {
            right = keepRange2(t.right, a, b);
            if (!right) {
                t.right = null;
                right = false;
            }
        }
        if ((a.compareTo(t.element) <= 0) && (b.compareTo(t.element) >= 0)){
            return true;
        } else if (t.left != null) {
            t = t.left;
            return true;
        } else if (t.right != null) {
            t = t.right;
            return true;
        } else {
            return false;
        }
    }




    // Basic node stored in unbalanced binary  trees
    private static class BinaryNode<E> {
        E element;            // The data in the node
        BinaryNode<E> left;   // Left child
        BinaryNode<E> right;  // Right child

        // Constructors
        BinaryNode(E theElement) {
            this(theElement, null, null);
        }

        BinaryNode(E theElement, BinaryNode<E> lt, BinaryNode<E> rt) {
            element = theElement;
            left = lt;
            right = rt;
        }

        // toString for BinaryNode
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node:");
            sb.append(element);
            return sb.toString();
        }

    }
}
