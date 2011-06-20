package edu.cornell.dendro.corina.tridasv2.support;

public interface ValueTranslator<V, E> {
	public E translate(V o);
}
