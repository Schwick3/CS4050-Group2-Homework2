package assignment.birds;

public class OrderedDictionary implements OrderedDictionaryADT {

    Node root;

    OrderedDictionary() {
        root = new Node();
    }

    /**
     * Returns the Record object with key k, or it returns null if such a record
     * is not in the dictionary.
     *
     * @param k
     * @return
     * @throws assignment/birds/DictionaryException.java
     */
    @Override
    public BirdRecord find(DataKey k) throws DictionaryException {
        Node current = root;
        int comparison;
        if (root.isEmpty()) {         
            throw new DictionaryException("There is no record matches the given key");
        }

        while (true) {
            comparison = current.getData().getDataKey().compareTo(k);
            if (comparison == 0) { // key found
                return current.getData();
            }
            if (comparison == 1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getLeftChild();
            } else if (comparison == -1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getRightChild();
            }
        }

    }

    /**
     * Inserts r into the ordered dictionary. It throws a DictionaryException if
     * a record with the same key as r is already in the dictionary.
     *
     * @param r
     * @throws birds.DictionaryException
     */
    @Override
    public void insert(BirdRecord r) throws DictionaryException {
        // Write this method

        // Check if dictionary is empty.
        if (isEmpty()) {
             this.root.setData(r);
             return;
        }

        // Begin to traverse the dictionary.
        Node current = this.root;
        Node newNode = new Node(r); // Node of the BirdRecord to insert.
        int comparision;
        DataKey newKey = r.getDataKey(); // The key of the record to insert.

        while (true) {
            // Compare the 2 Data Keys.
            comparision = newKey.compareTo(current.getData().getDataKey());

            // If key has been found, throw an exception.
            if (comparision == 0) {
                throw new DictionaryException("There is already a record with the given key");
            }
            // If key < current insert in left subtree.
            else if (comparision == -1) {
                // Keep traversing if left subtree exists.
                if (current.hasLeftChild()) {
                    current = current.getLeftChild();
                }
                // If end of left subtree has been reached, stop traversing and insert.
                else {
                    current.setLeftChild(newNode);
                    break;
                }
            }
            // If key > current insert in right subtree.
            else if (comparision == 1) {
                // Keep traversing if right subtree exists.
                if (current.hasRightChild()) {
                    current = current.getRightChild();
                }
                // If end of right subtree has been reached, stop traversing and insert.
                else {
                    current.setRightChild(newNode);
                    break;
                }
            }
        }
    }

    /**
     * Removes the record with Key k from the dictionary. It throws a
     * DictionaryException if the record is not in the dictionary.
     *
     * @param k
     * @throws birds.DictionaryException
     */
    @Override
    public void remove(DataKey k) throws DictionaryException {
        // Write this method

        // Check if dictionary is empty.
        if (isEmpty()) {
            throw new DictionaryException("There is no record that matches the given key");
        }

        // Traverse the dictionary.
        Node current = root;
        int comparision = k.compareTo(current.getData().getDataKey());

        // Check if the key is actually present in the BST.
        while (current != null && comparision != 0) {
            comparision = k.compareTo(current.getData().getDataKey());
            if (comparision == -1)
                current = current.getLeftChild();
            else
                current = current.getRightChild();
        }

        // Key not found.
        if (current == null) {
            throw new DictionaryException("There is no record that matches the given key");
        }
        // Current is now the node to be removed.

        Node par = current.getParent(); // The Node's parent.

        // Check if the node to be deleted has at most one child.
        if ((!current.hasLeftChild()) || (!current.hasRightChild())) {
            Node newCurrent; // newCurrent will replace the node to be deleted.

            // If the left child does not exist, replace node with right child.
            if (!current.hasLeftChild())
                newCurrent = current.getRightChild();
            else // Replace with left child.
                newCurrent = current.getLeftChild();

            // Check if the node to be deleted is it's parent's left or right
            // child and then replace this with newCurr (complete replacement).
            if (current == par.getLeftChild())
                par.setLeftChild(newCurrent);
            else
                par.setRightChild(newCurrent);
        }

        // Node to be deleted has two children.
        else {
            Node successor;
            par = null; // Parent of the successor

            // Compute the inorder successor (will be the leftmost/smallest node in right subtree)
            successor = current.getRightChild();
            while (successor.hasLeftChild()) {
                par = successor;
                successor = successor.getLeftChild();
            }

            // Check if the parent of the inorder successor is current.
            if (par != null)
                // Make the left child of its parent equal to the successor's right child.
                par.setLeftChild(successor.getRightChild());

            // Node to be deleted is the parent of successor.
            else
                // Make the right child of the node to be deleted equal to the right child of the successor.
                current.setRightChild(successor.getRightChild());

            // Overwrite node to be deleted with successor.
            current.setData(successor.getData());
        }
    }

    /**
     * Returns the successor of k (the record from the ordered dictionary with
     * smallest key larger than k); it returns null if the given key has no
     * successor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws birds.DictionaryException
     */
    @Override
    public BirdRecord successor(DataKey k) throws DictionaryException{
        // Write this method

        // Check if dictionary is empty.
        if (isEmpty()) {
            throw new DictionaryException("There is no record that matches the given key");
        }

        // Traverse the dictionary.
        Node current = root;
        int comparision;

        // Find key.
        while (true) {
            // Compare the 2 data keys
            comparision = k.compareTo(current.getData().getDataKey());

            if (comparision == 0) { // key found
                // Find the successor.
                Node successor;

                // Check if the key has a right subtree.
                if (current.hasRightChild()) {
                    // Find the leftmost/smallest node in the key's right subtree.
                    successor = current.getRightChild();
                    while (successor.hasLeftChild()) {
                        successor = successor.getLeftChild();
                    }
                    return successor.getData();
                }
                else { // Key has no right subtree, traverse the ancestors.
                    // Find the first ancestor where the key is in the left subtree of that ancestor.
                    // Find the parent of the first ancestor that is a left child of its parent.
                    successor = current.getParent();
                    while (successor != null && current == successor.getRightChild()) {
                        current = successor;
                        successor = successor.getParent();
                    }
                    if (successor != null) {
                        return successor.getData();
                    }

                    break; // Key found, but has no successor.
                }
            }
            else if (comparision == -1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record that matches the given key");
                }
                current = current.getLeftChild();
            } else if (comparision == 1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record that matches the given key");
                }
                current = current.getRightChild();
            }
        }

        return null; // Key found, but has no successor.
    }

   
    /**
     * Returns the predecessor of k (the record from the ordered dictionary with
     * largest key smaller than k; it returns null if the given key has no
     * predecessor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws birds.DictionaryException
     */
    @Override
    public BirdRecord predecessor(DataKey k) throws DictionaryException {
        // Write this method

        // Check if dictionary is empty.
        if (isEmpty()) {
            throw new DictionaryException("There is no record that matches the given key");
        }

        // Traverse the dictionary.
        Node current = root;
        int comparision;

        // Find key.
        while (true) {
            // Compare the 2 data keys
            comparision = k.compareTo(current.getData().getDataKey());

            if (comparision == 0) { // key found
                // Find the predecessor.
                Node predecessor;

                // Check if the key has a left subtree.
                if (current.hasLeftChild()) {
                    // Find the rightmost/largest node in the key's left subtree.
                    predecessor = current.getLeftChild();
                    while (predecessor.hasRightChild()) {
                        predecessor = predecessor.getRightChild();
                    }
                    return predecessor.getData();
                }
                else { // Key has no left subtree, traverse the ancestors.
                    // Find the first ancestor where the key is in the right subtree of that ancestor.
                    // Find the parent of the first ancestor that is a right child of its parent.
                    predecessor = current.getParent();
                    while (predecessor != null && current == predecessor.getLeftChild()) {
                        current = predecessor;
                        predecessor = predecessor.getParent();
                    }
                    if (predecessor != null) {
                        return predecessor.getData();
                    }

                    break; // Key found, but has no predecessor.
                }
            }
            else if (comparision == -1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record that matches the given key");
                }
                current = current.getLeftChild();
            } else if (comparision == 1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record that matches the given key");
                }
                current = current.getRightChild();
            }
        }

        return null; // Key found, but has no predecessor.
    }

    /**
     * Returns the record with smallest key in the ordered dictionary. Returns
     * null if the dictionary is empty.
     *
     * @return
     */
    @Override
    public BirdRecord smallest() throws DictionaryException {
        // Write this method

        // Check if dictionary is empty.
        if (isEmpty()) {
            return null;
        }

        // Traverse the left subtrees of the dictionary, until empty left subtree reached.
        Node smallest = root;

        while (smallest.hasLeftChild()) {
            smallest = smallest.getLeftChild();
        }

        return smallest.getData();
    }

    /*
	 * Returns the record with largest key in the ordered dictionary. Returns
	 * null if the dictionary is empty.
     */
    @Override
    public BirdRecord largest() throws DictionaryException {
        // Write this method

        // Check if dictionary is empty.
        if (isEmpty()) {
            return null;
        }

        // Traverse the right subtrees of the dictionary, until empty right subtree reached.
        Node largest = root;

        while (largest.hasLeftChild()) {
            largest = largest.getLeftChild();
        }

        return largest.getData();
    }
      
    /* Returns true if the dictionary is empty, and true otherwise. */
    @Override
    public boolean isEmpty (){
        return root.isEmpty();
    }
}
