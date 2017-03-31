package name.martingeisse.trading_game.common.util;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 */
public final class CollectionUtils {

	// prevent instantiation
	private CollectionUtils() {
	}

	public static <A, B> List<B> map(Iterable<A> source, Function<A, B> mapper) {
		List<B> result = new ArrayList<B>();
		for (A element : source) {
			result.add(mapper.apply(element));
		}
		return result;
	}

	public static <A, B> Set<B> mapUnique(Iterable<A> source, Function<A, B> mapper) {
		Set<B> result = new HashSet<>();
		for (A element : source) {
			result.add(mapper.apply(element));
		}
		return result;
	}

	public static <A, B, C> List<C> createGroups(Iterable<A> source, Function<A, B> keyMapper, BiFunction<B, List<A>, C> groupConstructor) {
		Map<B, List<A>> rawGroups = new HashMap<>();
		for (A element : source) {
			B key = keyMapper.apply(element);
			List<A> rawGroup = rawGroups.get(key);
			if (rawGroup == null) {
				rawGroup = new ArrayList<>();
				rawGroups.put(key, rawGroup);
			}
			rawGroup.add(element);
		}
		List<C> result = new ArrayList<>();
		for (Map.Entry<B, List<A>> entry : rawGroups.entrySet()) {
			result.add(groupConstructor.apply(entry.getKey(), entry.getValue()));
		}
		return result;
	}

}
