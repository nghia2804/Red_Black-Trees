import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class RBT<E extends Comparable<E>> {
	
	private static final int RED = 1;
	private static final int BLACK = 0;
	
	Node<E> root;
	private int size;
	private List<E> list;
	
	public RBT(){
		size = 0;
	}
	
	/**
	 * This method process a binary search until it reaches external node
	 * this node then
	 * 
	 * @param data
	 */
	public void add (E data){
		//start building tree if root is empty
		if (isEmpty()) {
			root = new Node<E>(data, BLACK);
			makeLeaf(root);
			System.out.println("Added root: " + data + " " + root.color());
		//add to pre-existing tree 
		} else {
			Node <E> node = add(root, data);
			//Reconstruct the tree
			while ( !(node == root) && node.parent().isRed() ){
				node = updateProperty(node);
				if (node == root){
					root.setColor(BLACK);
					System.out.println("recoloring: " + root.data()+ ": "+ root.color());
				}
			}
			System.out.println("Added node: " + data + " ");
		}
		size++;
	}
	
	private Node<E> updateProperty(Node<E> node) {
		// TODO Auto-generated method stub
		Node <E> parent = node.parent();
		// Uncle node - the other child of grand-parent
		Node <E> uncle = node.uncle();
		
		// Check if parent is the left node of grand-parent
		System.out.println((parent == parent.parent().left()));
		if ( parent == parent.parent().left())
		{	
			if ( uncle.isRed() ){
				// case 1: both 3 node, parent and uncle are red ( recoloring ) 
				node = recoloring(node);
			} else {
				if ( node.equals(parent.right()) ) {
					// case 2: uncle is black, zig-zag rotation
					System.out.println("LeftRotation: Before " + node.data() + ": " + node.color());
					node = leftRotation(node);
					System.out.println("LeftRotation: After " + node.data() + ": " + node.color());
				}
				//case 3: uncle is black, zig-zig rotation and recoloring
				System.out.println("rightRotRecolor: Before " + node.data() + ": " + node.color());
				node = rightRotRecolor(node);
				System.out.println("rightRotRecolor: After " + node.data() + ": " + node.color());
			}
		} else { // Check if parent is the right node of grand-parent
			if ( uncle.isRed() ){
				// case 1: both 3 node, parent and uncle are red ( recoloring ) 
				node = recoloring(node);
			} else {
				System.out.println(uncle.isRed());
				if ( node.equals(parent.left()) ) {
					// case 2: uncle is black, right rotation
					System.out.println("RightRotation: Before " + node.data() + ": " + node.color());
					node = rightRotation(node);
					System.out.println("RightRotation: After " + node.data() + ": " + node.color());
				}
				//case 3: uncle is black, right rotation and recoloring
				System.out.println("leftRotRecolor: Before " + node.data() + ": " + node.color());
				node = leftRotRecolor(node);
				System.out.println("leftRotRecolor: After " + node.data() + ": " + node.color());
			}
			
		}
		return node;
	}
	
	private Node<E> recoloring(Node<E> node) {
		// TODO Auto-generated method stub
		Node <E> parent = node.parent();
		Node <E> uncle = node.uncle();
		parent.setColor(BLACK);
		uncle.setColor(BLACK);
		parent.parent().setColor(RED);
		System.out.println("recoloring:   " + node.data() + ": " + node.color() + " " +
				parent.data() + ": " + parent.color() + " " + 
				uncle.data() + ": " + uncle.color() + " " + 
				parent.parent().data() + ": " + parent.parent().color() + " ");
		return parent.parent();
	}
	
	private Node<E> leftRotation(Node<E> node) {
		// TODO Auto-generated method stub
		Node <E> parent = node.parent();
		Node <E> grparent = parent.parent();
		grparent.setLeft(node);
		parent.setRight(node.left());
		node.setLeft(parent);
		return parent;
	}
	
	private Node<E> rightRotation(Node<E> node) {
		// TODO Auto-generated method stub
		Node <E> parent = node.parent();
		Node <E> grparent = parent.parent();
		grparent.setRight(node);
		parent.setLeft(node.right());
		node.setRight(parent);
		return parent;
	}

	private Node<E> leftRotRecolor(Node<E> node) {
		// TODO Auto-generated method stub
		Node <E> parent = node.parent();
		Node <E> grparent = parent.parent();
		parent.setRight(parent.left());
		parent.setLeft(grparent.left());
		grparent.setRight(node);
		grparent.setLeft(parent);
		
		//Swap data between grand parent and parent
		E temp = parent.data();
		parent.setData(grparent.data());
		grparent.setData(temp);
		return node;
	}
	
	private Node<E> rightRotRecolor(Node<E> node) {
		// TODO Auto-generated method stub
		Node <E> parent = node.parent();
		Node <E> grparent = parent.parent();
		parent.setLeft(parent.right());
		parent.setRight(grparent.right());
		grparent.setLeft(node);
		grparent.setRight(parent);
		
		//Swap data between grparent and parent
		E temp = parent.data();
		parent.setData(grparent.data());
		grparent.setData(temp);
		return node;
	}

	private Node<E> add(Node<E> currentNode, E data) {
		//base case: find the position to add
		// Do nothing when duplicated data adding on the tree
		if ( currentNode.data().compareTo(data) == 0 ) return currentNode;
		
		if( (currentNode.right().isNIL()) 
				&& (currentNode.data().compareTo(data) < 0)){
			// Add new node into the right of current node
			currentNode.setRight(new Node<E>(data));
			System.out.println("Add right before rotation: " + currentNode.right().data() + " Parent: " + currentNode.right().parent());
			makeLeaf(currentNode.right());
			return currentNode.right();	
		}else if( (currentNode.left().isNIL()) 
				&& (currentNode.data().compareTo(data) > 0)){
			currentNode.setLeft(new Node<E>(data));
			System.out.println("Add left before rotation: " + currentNode.left().data() + " Parent: " + currentNode.left().parent());
			makeLeaf(currentNode.left());
			return currentNode.left();
		}
		
		//if data smaller than node.data
		//keep traverse to the left side of the tree
		if(currentNode.data().compareTo(data) > 0)	
			currentNode = add(currentNode.left(), data);	
		//if data bigger than node.data
		//keep traverse to the right side of the tree
		else
			currentNode = add(currentNode.right(), data);
		return currentNode;
	}

	/**
	 * Leaf node is always BLACK and doesn't store any thing
	 * A last node that stores data is passed in, this method will create
	 * leaf children for it
	 * @param data the last node that has data
	 */
	private void makeLeaf(Node<E> node) {
		node.setLeft(new Node<E>());
		node.setRight(new Node<E>());
	}

	/**
	 * This method add a collection to a tree or build a RBT from
	 * @param c A collection of elements
	 */
	public void addAll(Collection<? extends E> c){
		for (E data : c){
			add(data);
		}
	}
	
	/**
	 * This will be freaking complicated
	 * @param data
	 * @return the Element itself if it exists, null otherwise
	 */
	public E remove (E data){
		return null;
	}
	
	/**
	 * If the element can be gotten, it must be in the tree 
	 * @param data Element need to find
	 * @return True if element is in the tree, False otherwise
	 */
	public boolean contains (E data){
		return get(data) != null;
	}
	
	/**
	 * Recurse through the tree to find the element is asked for
	 * return null if that element doesn't exist
	 * @param data Element need to find
	 * @return the Element if it is in the tree, null otherwise
	 */
	public E get (E data){
		if(isEmpty()) return null;
		Node<E> node = get(root, data);
		return (node == null) ? null : node.data();
	}
	
	private Node<E> get(Node<E> node, E data) {
		//base case 1: find the node
		if(node.data().compareTo(data) == 0){
			System.out.println("Found node: " + node.data() + ": " + node.color());
			System.out.println("Found node- parent: " + node.parent());
			return node;
		}
		//base case 2: node is not in the tree
		else if (node.left() == null || node.right() == null)
			return null;
		else {
			if (node.data().compareTo(data) > 0)
				node = get(node.left(), data);
			else
				node = get(node.right(), data);
		}
		return node;
	}

	/**
	 * Test if the tree is empty
	 * @return True if the tree is empty, False otherwise
	 */
	public boolean isEmpty(){
		return size == 0;
	}
	
	/**
	 * Return the number of element in the tree
	 * @return 
	 */
	public int size(){
		return size;
	}
	
	/**
	 * Remove all elements in the tree
	 */
	public void clear(){
		root = null;
		size = 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<E> preOrder(){
		if (isEmpty()) return null;
		list = new ArrayList<E>();
		return preOrder (root, list);
	}

	private List<E> preOrder(Node<E> node, List<E> list) {
		list.add(node.data());
		//base case
		if (node.left() != null){
			preOrder(node.left(), list);
		} 
		if (node.right() != null) {
			preOrder(node.right(), list);
		}
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<E> inOrder() {
		if (isEmpty()) return null;
		list = new ArrayList<E>();
		return inOrder(root, list);
	}
	
	private List<E> inOrder(Node<E> node, List<E> list){
		//base case
		if(node.left() == null && node.right() == null){
			list.add(node.data());
		} else {
			if(node.left() != null){
				inOrder(node.left(),list);
			}
			list.add(node.data());
			if(node.right() != null){
				inOrder(node.right(),list);
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<E> postOrder() {
		if (isEmpty()) return null;
		list = new ArrayList<E>();
		return postOrder(root, list);
	}
	
	private List<E> postOrder(Node<E> node, List<E> list){

		if(node.left() != null){
			postOrder(node.left(),list);
		}
		if(node.right() != null){
			postOrder(node.right(),list);
		}
		list.add(node.data());
		
		return list;
	}
	
	/**
	 * 
	 * @param preOrder
	 */
	public void reconstruct(List<? extends E> preOrder) {
		this.clear();
		for (E item : preOrder){
			add(item);
		}
	}
}
