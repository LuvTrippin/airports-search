package org.example;

import java.util.ArrayList;

class Node {
    char value;
    Node left;
    Node right;
    ArrayList<Pair> pairs;

    Node(char value, Pair pair) {
        this.value = value;
        left = null;
        right = null;
        pairs = new ArrayList<>();
        pairs.add(pair);
    }
}

public class BST {
    Node root;

    private Node addRecursive(Node current, Pair pair) {
        if (current == null) {
            return new Node(pair.name.charAt(1), pair);
        }

        if (pair.name.charAt(1) < current.value) {
            current.left = addRecursive(current.left, pair);
        } else if (pair.name.charAt(1) > current.value) {
            current.right = addRecursive(current.right, pair);
        } else {
            current.pairs.add(pair);
        }
        return current;
    }

    public void add(Pair pair) {
        root = addRecursive(root, pair);
    }

    private void searchRecursive(Node current, String prefix, ArrayList<Pair> results) {
        if (current == null) {
            return;
        }

        if (prefix == null || prefix.isEmpty()) {
            results.addAll(current.pairs);
            searchRecursive(current.left, prefix, results);
            searchRecursive(current.right, prefix, results);
            return;
        }

        if (prefix.charAt(0) < Character.toLowerCase(current.value)) {
            searchRecursive(current.left, prefix, results);
        } else if (prefix.charAt(0) > Character.toLowerCase(current.value)) {
            searchRecursive(current.right, prefix, results);
        } else {
            for (Pair pair : current.pairs) {
                if (pair.name.toLowerCase().startsWith("\"".concat(prefix))) {
                    results.add(pair);
                }
            }
        }
    }

    public ArrayList<Pair> search(String prefix) {
        ArrayList<Pair> results = new ArrayList<>();
        searchRecursive(root, prefix, results);
        return results;
    }
}
