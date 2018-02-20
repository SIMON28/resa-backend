package com.asptt.resa.commons.lifecycle;

import java.util.Set;

import com.asptt.resa.commons.exception.BadUsageException;

/**
 *
 * @param <T>
 * @author maig7313
 */
public interface StateModel<T extends Enum<T>> {

	/**
	 *
	 * @param from
	 * @param to
	 * @throws BadUsageException
	 */
	public void checkTransition(T from, T to) throws BadUsageException;

	/**
	 *
	 * @return
	 */
	public Transition<T> getFirstTransition();

	public Set<T> getPossibleStates(T from);

}
