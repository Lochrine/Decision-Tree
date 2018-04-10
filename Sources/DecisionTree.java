import java.util.*;
import java.io.*;

/**
 * The DecisionTree class implements a simple binary tree.
 * Tree elements are read from a given file and then the decision
 * tree can be evaluated based on user input using the evaluate method.
 *
 * @author Michael McLaughlin
 */
public class DecisionTree implements DecisionTreeInterface {

    private class Element<T> {
        private T element;

        public Element(T element) {
            this.element = element;
        }
        public T getElement() {
            return element;
        }
        public String toString() {
            return element.toString();
        }
    }

    private class Node<T> {
        private int nodeID;
        private T element;
        private Node left = null;
        private Node right = null;
        
        public Node(T element) {
            this.nodeID = 0;
            this.element = element;
        }

        public Node(int nodeID, T element) {
            this.nodeID = nodeID;
            this.element = element;
        }
        
        public T getElement() {
            return element;
        }
        
        public Node<T> getLeft() {
            return left;
        }
        
        public Node<T> getRight() {
            return right;
        }
        
        public void setLeft(Node<T> left) {
            this.left = left;
        }
        
        public void setRight(Node<T> right) {
            this.right = right;
        }
        
        @Override
        //PreOrder Traversal
        public String toString() {
            String print = "Node ID: " + nodeID + " - Element: " + element + "\n";
            if (left == null && right == null) {return print;}
            return print + left.toString() + right.toString();
        }
    }

    private class BinaryTree<T> {
        private Node<T> root;

        public BinaryTree(T element) {
            root = new Node(element);
        }
        
        public Node<T> getRoot() {
            return root;
        }
        
        public String toString() {
            return root.toString();
        }
           
        //Method to add a new node or leaf onto the decision tree
        public void add(Node nodeToCheck, T element, int nodeID, int parentID) {
            //Creates new node, checks for an empty tree
            Node nodeToAdd = new Node(nodeID, element);
            if (this.root == null) { this.root = nodeToAdd; }
            //Searches for parent node, then appends node to be added accordingly
            Node search = findNode(parentID, nodeToCheck);
            if (search != null) {
                if (search.left == null && search.right == null) {search.setLeft(nodeToAdd); return;}
                if (search.left != null && search.right == null) {search.setRight(nodeToAdd); return;}
                if (search.left == null && search.right != null) {search.setLeft(nodeToAdd); return;}
                if (search.left != null && search.right != null) {System.out.println("Searching error!"); return;}
            } else System.out.println("Error adding node!");
        }
        
        /**
         * Returns a reference to the specified target element if it is found in
         * this binary tree.
         *
         * @param parentNodeID the nodeID of the element being sought in this tree
         * @param next the element to begin searching from
         */
        public Node<T> findNode(int parentNodeID, Node<T> next) {
            //Creates temporary node to search, then traverses tree recursively
            //to find and return the desired node
            Node foundNode = null;
            if (next == null || next.nodeID == parentNodeID) {return next;}
            if (next.left != null) {foundNode = findNode(parentNodeID, next.left);}
            if (foundNode == null) {foundNode = findNode(parentNodeID, next.right);}
            return foundNode;
        }
    }

    private BinaryTree<Element> tree;

    /**
     * Builds the decision tree based on the contents of the given file
     *
     * @param filename the name of the input file
     * @throws FileNotFoundException if the input file is not found
     */
    public DecisionTree(String filename) throws FileNotFoundException {
        File inputFile = new File(filename);
        Scanner scan = new Scanner(inputFile);
        // Create the tree containing only the root node
        tree = new BinaryTree<>(new Element(scan.nextLine()));
        //Reads in the elements of the tree from the text file and populates
        //tree accordingly
        int nodeID = 1;
        int parentNodeID = 0;
        while (scan.hasNext()) {
            String element = scan.nextLine();
            parentNodeID = Integer.parseInt(String.valueOf(element.charAt(0)));
            tree.add(tree.getRoot(), new Element(element.substring(2)), nodeID, parentNodeID);
            nodeID++;
        }
    }

    /**
     * Follows the decision tree based on user responses.
     */
    public void evaluate() {
        //Begins evaluation of tree using recursive method
        System.out.println("Let's figure out what's wrong!");
        Node<Element> current = tree.getRoot();
        evalHelper(current);
    }
    
    public void evalHelper(Node current) {
        //Checks if the current node is a leaf.  If so, print answer. If not,
        //recur accordingly based on user input until a leaf is reached
        if (current.left == null && current.right == null) {System.out.println(current.element); return;}
        Scanner scan = new Scanner(System.in);
        System.out.print(current.element + " --> Please enter 'Yes' or 'No': ");
        String answer = scan.nextLine();
        boolean yesOrNo = this.getYesNo(answer);
        if (!yesOrNo) {
            evalHelper(current.left);
        } else if (yesOrNo) {
            evalHelper(current.right);
        }
    }
    
    private boolean getYesNo(String s) {
        s = s.toLowerCase();
        if (s.equals("No") || s.charAt(0) == 'n') return false;
        return true;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        DecisionTree decisionTree = new DecisionTree("tree1.txt");
        decisionTree.evaluate();
        
        //Evaluate a second tree
        System.out.println();
        DecisionTree decisionTree2 = new DecisionTree("tree.txt");
        decisionTree2.evaluate();
    }
}
