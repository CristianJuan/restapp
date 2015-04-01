package edu.uprm.ece.icom4035.list;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedCircularDoublyLinkedList<E extends Comparable<E>> implements SortedList<E> {
	private class Node {
		private E value;
		private Node next;
		private Node prev;

		public E getValue() {
			return value;
		}
		public void setValue(E value) {
			this.value = value;
		}
		public Node getNext() {
			return next;
		}
		public void setNext(Node next) {
			this.next = next;
		}
		public Node getPrev() {
			return prev;
		}
		public void setPrev(Node prev) {
			this.prev = prev;
		}
	}
	
	private class ListIterator implements Iterator<E>{
		
		private Node nextNode;
		
		public ListIterator(){
			this.nextNode = header.getNext();
		}
		public ListIterator(int index){
			if((index < 0) || (index>currentsize))
				throw new IndexOutOfBoundsException();

			int counter = 0;
			Node temp;

			for(temp = header.getNext(); counter < index; temp = temp.getNext(), counter++);
			this.nextNode = temp;
		}

		@Override
		public boolean hasNext() {
			return nextNode != null;
		}

		@Override
		public E next() {
			if (hasNext()){
				E result = this.nextNode.getValue();
				this.nextNode = this.nextNode.getNext();
				return result;
			}
			else {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
			
		}
		
	}
	
	private class ReverseListIterator implements ReverseIterator<E>{
	private Node prevNode;

	public ReverseListIterator(){
		this.prevNode = header.getPrev();
	}

	public ReverseListIterator(int index){
		int counter = currentsize;
		Node temp;

		for(temp = header.getPrev(); counter > currentsize-index; temp = temp.getPrev(), counter--);
		this.prevNode = temp;
	}

	@Override
	public boolean hasPrevious() {
		return prevNode != header;
	}
	@Override
	public E previous() {
		if (hasPrevious()){
			E result = prevNode.getValue();
			prevNode = prevNode.getPrev();
			return result;
		}
		else {
			throw new NoSuchElementException();

		}

	}

}
	//END OF PRIVATE CLASSES
	
	private int currentsize;
	private Node header;
	public SortedCircularDoublyLinkedList(){//Constructor
		header = new Node();
		header.setValue(null);
		header.setNext(header);
		header.setPrev(header);
		this.currentsize = 0;
	}

	
	@Override
	public Iterator<E> iterator() {
		return new ListIterator();
	}

	@Override
	public Iterator<E> iterator(int index) {
		return new ListIterator(index);
	}

	@Override
	public ReverseIterator<E> reverseIterator() {
		return new ReverseListIterator();
	}

	@Override
	public ReverseIterator<E> reverseIterator(int index) {
		return new ReverseListIterator(index);
	}

	@Override
	public boolean add(E obj) {
		if(obj == null)
			throw new IllegalArgumentException("Argument cannot be null.");

		boolean elementisAdded = false; 
		for(Node temp = header.getNext();temp.getValue()!= null; temp=temp.getNext()){
			if(obj.compareTo(temp.getValue()) < 0){
				if(temp.getPrev().getValue() == (header.getValue())){
					Node temp2= new Node();
					temp2.setValue(obj);
					temp2.setNext(temp);
					temp2.setPrev(header);
					header.setNext(temp2);
					temp.setPrev(temp2);
					elementisAdded=true;
					currentsize++;
					break;
				}

				else{
					Node temp2= new Node();
					temp2.setValue(obj);
					temp2.setNext(temp);
					temp2.setPrev(temp.getPrev());
					temp.getPrev().setNext(temp2);
					temp.setPrev(temp2);
					elementisAdded=true;
					currentsize++;
					break;
				}
			}
		}

		if(elementisAdded == false){
			Node temp = new Node();
			temp.setValue(obj);
			temp.setNext(header);
			temp.setPrev(header.getPrev());
			header.getPrev().setNext(temp);
			header.setPrev(temp);
			elementisAdded=true;
			currentsize++;
		}
		return elementisAdded;
	}
	
	@Override
	public boolean remove(E obj) {
		if (obj == null){
			throw new IllegalArgumentException("Parameter cannot be null.");
		}
		Node temp = null;
		for (temp = header.getNext(); temp != null; temp = temp.getNext()){
			if (temp.getValue().equals(obj)){
				// found first copy
				if (temp.getNext() != null){
					temp.getNext().setPrev(temp.getPrev());
				}
				temp.getPrev().setNext(temp.getNext());
				temp.setValue(null);
				temp.setNext(null);
				temp.setPrev(null);
				this.currentsize--;
				return true;
			}
		}
		return false;
	}
	

	@Override
	public boolean remove(int index) {
		if ((index < 0) || (index >= this.size())){
			throw new IndexOutOfBoundsException();
		}
		
		Node temp = null;
		int counter = 0;
		for (temp = header.getNext(); counter < index; temp = temp.getNext(), counter++);
		if (temp.getNext() != null){
			// i am not at the end of list
			temp.getNext().setPrev(temp.getPrev()); // null if temp is the last one
		}
		temp.getPrev().setNext(temp.getNext());
		temp.setValue(null);
		temp.setNext(null);
		temp.setPrev(null);
		this.currentsize--;
		return true;
	}
	
	@Override
	public int size() {
		return this.currentsize;
	}

	@Override
	public int removeAll(E obj) {
		int counter = 0;
		while(this.remove(obj)){
			counter++;
		}
		return counter;
	}

	@Override
	public E first() {
		if (this.isEmpty()){
			return null;
		}
		else {
			return header.getNext().getValue();
		}
	}

	@Override
	public E last() {
		if(this.isEmpty())
			return null;
		else{
			return header.getPrev().getValue();
		}
	}

	@Override
	public E get(int index) {
		if ((index < 0) || (index >= this.size())){
			throw new IndexOutOfBoundsException();
		}
		Node temp=null;
		int counter=0;
		for(temp=header.getNext();index < counter;temp=temp.getNext(),counter++);
		return temp.getValue();
	}

	@Override
	public void clear() {
		while (!this.isEmpty()){
			this.remove(0);
		}		
	}

	@Override
	public boolean contains(E e) {
		return this.firstIndex(e) >= 0;
	}

	@Override
	public boolean isEmpty() {
		return this.size()==0;
	}


	@Override
	public int firstIndex(E e) {
		if (e == null){
			throw new IllegalArgumentException("Parameter cannot be null.");
		}
		else {
			int counter = 0;
			Node temp = null;
			for (temp = header.getNext(); temp != null; temp = temp.getNext(), counter++){
				if (temp.getValue().equals(e)){
					return counter;
				}
			}
			return -1;
		}
	}

	@Override
	public int lastIndex(E e) {
		if (e == null){
			throw new IllegalArgumentException("Parameter cannot be null.");
		}
		else {
			int counter =0, lastSeen = -1;
			Node temp = null;
			for (temp = header.getNext(); temp != null; temp = temp.getNext(), counter++){
				if (temp.getValue().equals(e)){
					lastSeen = counter;
				}
			}
			return lastSeen;
		}
	}
	@Override
	public String toString(){
		String result="";
		for(Node temp=this.header.getNext();temp.getValue()!=null;temp=temp.getNext())
		{
			result=result+temp.getValue()+" ";
		}
		return result;
	}

}
