package edu.cornell.dendro.corina.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public class ListUtil {
	/**
	 * Creates an immutable list view of another list that only contains 
	 * items of a specified type, guaranteed.
	 * 
	 * @param l
	 * @param type
	 * @return
	 */
	public static <E> List<E> subListOfType(List<?> l, Class<E> type) {
		return new SublistOfType<E>(l, type);
	}
	
	private static class SublistOfType<E> implements List<E>, RandomAccess {
		private final List<E> list;
		
		@SuppressWarnings("unchecked")
		public SublistOfType(List<?> l, Class<E> type) {
			list = new ArrayList<E>();

			for(Object item : l) {
				if(type.isInstance(item))
					list.add((E) item);
			}
		}

		public boolean add(E e) { throw new UnsupportedOperationException(); }
		public void add(int index, E element) { throw new UnsupportedOperationException(); }
		public boolean addAll(Collection<? extends E> c) { throw new UnsupportedOperationException(); }
		public boolean addAll(int index, Collection<? extends E> c) { throw new UnsupportedOperationException(); }
		public void clear() { throw new UnsupportedOperationException(); }

		public boolean contains(Object o) {
			return list.contains(o);
		}

		public boolean containsAll(Collection<?> c) {
			return list.containsAll(c);
		}

		public E get(int index) {
			return list.get(index);
		}

		public int indexOf(Object o) {
			return list.indexOf(o);
		}

		public boolean isEmpty() {
			return list.isEmpty();
		}

		public Iterator<E> iterator() {
			return list.iterator();
		}

		public int lastIndexOf(Object o) {
			return list.lastIndexOf(o);
		}

		public ListIterator<E> listIterator() {
			return list.listIterator();
		}

		public ListIterator<E> listIterator(int index) {
			return list.listIterator(index);
		}

		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		public E remove(int index) { throw new UnsupportedOperationException(); }
		public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }
		public E set(int index, E element) { throw new UnsupportedOperationException(); }

		public int size() {
			return list.size();
		}

		public List<E> subList(int fromIndex, int toIndex) {
			return list.subList(fromIndex, toIndex);
		}

		public Object[] toArray() {
			return list.toArray();
		}

		public <T> T[] toArray(T[] a) {
			return list.toArray(a);
		}		
	}
}
