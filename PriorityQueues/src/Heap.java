public class Heap<AnyType extends Comparable<? super AnyType>>{
    /**
     * constructs and formats the node class and its parts
     */
    static class Node<AnyType>
    {
        // Constructors
        Node(Task theElement )
        {
            this(theElement, null, null );
        }

        Node(Task theElement, Node<AnyType> lt, Node<AnyType> rt )
        {
            element  = theElement;
            left     = lt;
            right    = rt;
            height   = 0;
        }

        Task           element;      // The data in the node
        Node<AnyType> left;         // Left child
        Node<AnyType> right;        // Right child
        int               height;       // Height
    }

    /** The tree root. */
    Node<AnyType> root;

    /**
     * insert node into the heap/tree and rebalance
     * @param toInsert the item to be inserted
     * @param treeRoot the node that roots the subtree
     * @return the new root of the subtree after balancing
     */
    public Node<AnyType> insert( AnyType toInsert, Node<AnyType> treeRoot ) {
        if( treeRoot == null ) {
            root = new Node<>((Task) toInsert, null, null);
            return root;
        }
        if (treeRoot.right != null) {
            insert (toInsert, treeRoot.right);
        } else {
            treeRoot.right = new Node<>((Task) toInsert, null, null);
        }
        return balance( treeRoot );
    }

    /**
     * traverse heap/tree and rearrange nodes as needed to make
     * tree a min leftist heap again
     * @param treeRoot the node that roots the subtree
     * @return the new root of the balanced heap/tree
     */
    public Node<AnyType> balance( Node<AnyType> treeRoot ) {
        if( treeRoot == null ) {
            return treeRoot;
        }

        Node<AnyType> temp = new Node<AnyType>((Task) null, null,null);

        if (treeRoot.left != null) {
            balance(treeRoot.left);

            if ( treeRoot.element.compareTo(treeRoot.left.element) > 0) {
                // left child is smaller than parent
                temp.element = treeRoot.element;
                treeRoot.element = treeRoot.left.element;
                treeRoot.left.element = temp.element;
            }
        }
        if (treeRoot.right != null) {
            balance(treeRoot.right);
            if (treeRoot.element.compareTo(treeRoot.right.element) > 0 ){
                // right child is smaller
                temp.element = treeRoot.element;
                treeRoot.element = treeRoot.right.element;
                treeRoot.right.element = temp.element;
            }
        }
        // if only right child, swap to left
        if (treeRoot.left == null && treeRoot.right != null) {
            treeRoot.left = treeRoot.right;
            treeRoot.right = null;
        }
        // if right side npl is bigger than left side npl, swap children
        if (rightNPL(treeRoot.left) < rightNPL(treeRoot.right)){
            temp = treeRoot.left;
            treeRoot.left = treeRoot.right;
            treeRoot.right = temp;
        }
        return treeRoot;
    }

    /**
     * find the NPL of the right most branch of the heap
     * @param treeRoot the node that roots the subtree
     * @return length of npl
     */
    private int rightNPL(Node<AnyType> treeRoot ){
        if(treeRoot == null || treeRoot.right == null ) {
            return 0;
        } else {
            return rightNPL(treeRoot.right) + 1;
        }
    }

    /**
     * variables to track late tasks and time
     */
    public int lateCount = 0;
    public int lateTime = 0;

    /**
     * search for tasks that can be started and check if task is late once completed
     * @param offset difference between the current time and a task's startTime
     * @param node the task being considered
     * @param root the root of the tree
     * @param time current time
     * @return new current time
     */
    public int search(int offset, Node<Task> node, Node<Task> root, int time){
        if (node == null){
            return time;
        }
        if (node.element.compareTo(root.element) == offset) {
            // has same condition as root
            if (node.element.printCount > 0) {
                time++;
                if (node.element.printCount == 1) {
                    // done
                    System.out.print("Time: " + time + " Task " + node.element.ID + " **");
                    // check for lateness
                    if (time > node.element.deadline) {
                        System.out.println(" Late " + (time - node.element.deadline));
                        lateCount++;
                        lateTime += time - node.element.deadline;
                    } else {System.out.println("");}
                } else{
                    // not done
                    System.out.println("Time: " + time + " Task " + node.element.ID);
                }
                node.element.printCount--;
            }
        }
        if (node.left != null){
            time = search(offset, node.left, root, time);
        }
        if (node.right != null){
            time = search(offset, node.right, root, time);
        }

        return time;
    }

    /**
     * find max node in heap
     * @param treeRoot the node that roots the subtree
     * @param max highest node value
     * @return the new root of the subtree after balancing
     */
    public int findMax(Node<AnyType> treeRoot, int max) {
        if( treeRoot == null ) {
            return 0;
        }
        int tempR = 0;
        int tempL = 0;
        if (treeRoot.right != null) {
            tempR = findMax(treeRoot.right, max);
        }
        if (treeRoot.left != null){
            tempL = findMax(treeRoot.left, max);
        }
        int firstMax = Math.max(tempR, tempL);
        return Math.max(firstMax, treeRoot.element.compareTo(root.element));
    }
}
