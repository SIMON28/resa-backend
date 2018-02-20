package com.asptt.resa.commons.lifecycle;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.asptt.resa.commons.exception.BadUsage;
import com.asptt.resa.commons.exception.BadUsageException;

/**
 *
 * @param <E>
 * @author maig7313
 */
public abstract class StateModelBase<E extends Enum<E>>
		implements StateModel<E> {

	private Map<E, Transition<E>> transitionMap;

	private Transition<E> firstTransition;

	private Class<E> type;

	/**
	 *
	 * @param type
	 */
	public StateModelBase(Class<E> type) {
		this.type = type;
	}

	private Transition<E> getTransition(E from) throws BadUsageException {
		Transition<E> transition;
		if (from == null) {
			transition = this.firstTransition;
		}
		else {
			if (this.getTransitionMap().containsKey(from)) {
				transition = this.getTransitionMap().get(from);
			}
			else {
				throw new BadUsageException(BadUsage.FLOW_UNKNOWN_STATE,
						this.getType().getSimpleName() + "=" + from);
			}
		}
		return transition;
	}

	/**
	 * @return first currentT
	 */
	@Override
	public Transition<E> getFirstTransition() {
		if (this.firstTransition == null) {
			this.draw();
		}
		return this.firstTransition;
	}

	/**
	 *
	 * @param from
	 * @param to
	 * @throws BadUsageException
	 */
	@Override
	public void checkTransition(E from, E to) throws BadUsageException {
		if (this.firstTransition == null) {
			this.draw();
		}
		Transition<E> transition = this.getTransition(from);
		if (transition.isFinal()) {
			throw new BadUsageException(BadUsage.FLOW_TRANSITION,
					"item is in final state: " + from.toString());
		}
		if (!transition.isAnAuthorizedTransition(to)) {
			throw new BadUsageException(BadUsage.FLOW_TRANSITION,
					"authorized: " + transition.getTo().toString());
		}

	}

	/**
	 *
	 * @param e1
	 * @return
	 */
	protected Transition<E> from(E e1) {
		Transition<E> transition;
		if (!this.getTransitionMap().containsKey(e1)) {
			transition = new Transition<E>(e1);
			this.getTransitionMap().put(e1, transition);
		}
		else {
			transition = this.getTransitionMap().get(e1);
		}
		return transition;
	}

	/**
	 *
	 * @return
	 */
	protected Transition<E> fromFirst() {
		this.firstTransition = new Transition<E>();
		return this.firstTransition;
	}

	/**
	 *
	 * @param e1
	 * @return
	 */
	protected Transition<E> fromFirst(E e1) {
		this.firstTransition = this.from(e1);
		return this.firstTransition;
	}

	/**
	 *
	 */
	protected abstract void draw();

	/**
	 * @return the transitionMap
	 */
	public Map<E, Transition<E>> getTransitionMap() {
		if (this.transitionMap == null) {
			this.transitionMap = new EnumMap<E, Transition<E>>(this.getType());
		}
		return this.transitionMap;
	}

	/**
	 * @return the enum type
	 */
	private Class<E> getType() {
		return this.type;
	}

	@Override
	public Set<E> getPossibleStates(E from) {
		final Transition<E> transition = this.getTransitionMap().get(from);
		if (transition == null || transition.getTo() == null) {
			return new HashSet<>();
		}

		return transition.getTo();
	}

}
