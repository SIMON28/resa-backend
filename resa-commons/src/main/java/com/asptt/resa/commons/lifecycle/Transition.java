package com.asptt.resa.commons.lifecycle;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumSet;

import org.springframework.util.Assert;

/**
 *
 * @author maig7313
 * @param <E>
 */
public class Transition<E extends Enum<E>> implements Serializable, Cloneable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6959414683555896418L;

	private E from;

	private EnumSet<E> to;

	/**
	 *
	 */
	public Transition() {
	}

	/**
	 *
	 * @param label
	 */
	public Transition(E label) {
		this.from = label;
	}

	/**
	 *
	 * @param enums
	 * @return
	 */
	@SafeVarargs
	public final Transition<E> to(final E... enums) {
		Assert.notEmpty(enums);
		this.to = EnumSet.noneOf(enums[0].getDeclaringClass());
		this.to.addAll(Arrays.asList(enums));
		return this;
	}

	/**
	 *
	 * @param e1
	 * @return
	 */
	public boolean isAnAuthorizedTransition(E e1) {
		return ((this.to != null) && this.to.contains(e1));
	}

	/**
	 *
	 * @return
	 */
	public E getFrom() {
		return this.from;
	}

	/**
	 *
	 * @return
	 */
	public EnumSet<E> getTo() {
		return this.to;
	}

	/**
	 *
	 * @return
	 */
	public boolean isFinal() {
		return ((this.to == null) || (this.to.isEmpty()));
	}

}
