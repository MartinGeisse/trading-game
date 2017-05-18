package name.martingeisse.trading_game.common.util;

import java.util.Comparator;
import java.util.function.Function;

/**
 * A comparator that first maps the values to compare to other values, then
 * compares them either using their natural order or another comparator.
 *
 * @param <A> the type of values to compare
 * @param <B> the mapped value type
 */
public class MappedComparator<A, B> implements Comparator<A> {

	private final Function<A, B> mapper;
	private final Comparator<B> comparator;

	private MappedComparator(final Function<A, B> mapper, final Comparator<B> comparator) {
		this.mapper = mapper;
		this.comparator = comparator;
	}

	// override
	@Override
	public int compare(final A x, final A y) {
		return comparator.compare(mapper.apply(x), mapper.apply(y));
	}

	/**
	 * Creates a new instance that maps the values to compare to comparable values.
	 *
	 * @param mapper the mapper function
	 * @return the comparator
	 */
	public static final <A, B extends Comparable<B>> Comparator<A> of(Function<A, B> mapper) {
		return of(mapper, (x, y) -> x.compareTo(y));
	}

	/**
	 * Creates a new instance that maps the values to compare to other values and uses a
	 * second comparator for them.
	 *
	 * @param mapper     the mapper function
	 * @param comparator the second comparator to use on the mapped values
	 * @return the newly created comparator
	 */
	public static final <A, B> Comparator<A> of(Function<A, B> mapper, Comparator<B> comparator) {
		return new MappedComparator<>(mapper, comparator);
	}

}
