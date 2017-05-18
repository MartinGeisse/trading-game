/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui.util;

import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import name.martingeisse.wicket.serializable.SerializablePredicate;
import org.apache.wicket.model.AbstractReadOnlyModel;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Model for a dependency set.
 *
 * @param <T> the dependency type
 */
public class DependencySetModel<T> extends AbstractReadOnlyModel<Set<T>> {

	private final Class<T> type;
	private final Predicate<? super T> filter;

	/**
	 * Constructor.
	 * @param type the dependency type
	 * @param filter the filter to use, or null to accept all dependency objects
	 */
	public DependencySetModel(final Class<T> type, SerializablePredicate<? super T> filter) {
		this.type = type;
		this.filter = (filter == null ? (x -> true) : filter);
	}

	/**
	 * Constructor.
	 * @param type the dependency type
	 */
	public DependencySetModel(final Class<T> type) {
		this(type, null);
	}

	// override
	@Override
	public Set<T> getObject() {
		return MyWicketApplication.get().getDependencies(type).stream().filter(filter).collect(Collectors.toCollection(HashSet::new));
	}

}
