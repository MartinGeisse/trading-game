/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui.util;

import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import name.martingeisse.wicket.serializable.SerializableComparator;
import name.martingeisse.wicket.serializable.SerializableFunction;
import name.martingeisse.wicket.serializable.SerializablePredicate;
import org.apache.wicket.model.AbstractReadOnlyModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Model for a dependency list.
 *
 * This model requires a comparator to determine the list order. If
 * no comparator is specified, it will use a default comparator that
 * sorts by implementation class, but will fail with an exception if
 * two implementations with the same class are found. This is because
 * the only alternative would be to leave the list unsorted, which may
 * produce a different order each time and cause wicket to route events
 * to the wrong component, e.g. making the user "click" a button he never
 * actually clicked.
 *
 * @param <T> the dependency type
 */
public class DependencyListModel<T> extends AbstractReadOnlyModel<List<T>> {

	private final Class<T> type;
	private final SerializableComparator<? super T> comparator;
	private final SerializablePredicate<? super T> filter;

	/**
	 * Constructor.
	 * @param type the dependency type
	 * @param comparator the comparator that determines the list order
	 * @param filter the filter to use, or null to accept all dependency objects
	 */
	public DependencyListModel(final Class<T> type, final SerializableComparator<? super T> comparator, SerializablePredicate<? super T> filter) {
		this.type = type;
		this.comparator = comparator;
		this.filter = (filter == null ? (x -> true) : filter);
	}

	/**
	 * Constructor.
	 * @param type the dependency type
	 * @param comparator the comparator that determines the list order
	 */
	public DependencyListModel(final Class<T> type, final SerializableComparator<? super T> comparator) {
		this(type, comparator, null);
	}

	/**
	 * Constructor.
	 * @param type the dependency type
	 * @param mapper a function that maps the dependency object to a comparable value
	 * @param filter the filter to use, or null to accept all dependency objects
	 */
	public <C extends Comparable<C>> DependencyListModel(final Class<T> type, final SerializableFunction<? super T, C> mapper, SerializablePredicate<? super T> filter) {
		this(type, (x, y) -> mapper.apply(x).compareTo(mapper.apply(y)), filter);
	}

	/**
	 * Constructor.
	 * @param type the dependency type
	 * @param mapper a function that maps the dependency object to a comparable value
	 */
	public <C extends Comparable<C>> DependencyListModel(final Class<T> type, final SerializableFunction<? super T, C> mapper) {
		this(type, mapper, null);
	}

	// override
	@Override
	public List<T> getObject() {
		final Set<T> dependencySet = MyWicketApplication.get().getDependencies(type);
		final List<T> dependencyList = dependencySet.stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
		Collections.sort(dependencyList, comparator);
		return dependencyList;
	}

}
