package name.martingeisse.trading_game.gui.cache;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Super-simple and dirty cache implementation.
 */
public final class GuiCache {

	public static volatile String staticSpaceObjectData;

	// prevent instantiation
	private GuiCache() {
	}

}
