/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.platform.application;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.GameTicker;

/**
 * This class continues the work that {@link ServerApplicationBootstrapper} started.
 * In contrast to that class, this initializer can have its dependencies injected
 * by Guice.
 */
@Singleton
public final class ServerApplicationInitializer extends AbstractApplicationInitializer {

	@Inject
	public ServerApplicationInitializer(Injector injector) {
		super(injector);
	}

	@Override
	protected void run() {
		super.run();
		injector.getInstance(GameTicker.class).start();
	}
}
