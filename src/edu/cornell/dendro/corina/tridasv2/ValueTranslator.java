package edu.cornell.dendro.corina.tridasv2;

public interface ValueTranslator<V, E> {
	public E translate(V o);
}
