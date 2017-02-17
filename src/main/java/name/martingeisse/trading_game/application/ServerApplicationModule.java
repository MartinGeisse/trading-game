/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.trading_game.application;

import name.martingeisse.trading_game.application.configuration.ApplicationConfiguration;
import name.martingeisse.trading_game.application.configuration.ConfigurationParticipant;
import name.martingeisse.trading_game.postgres.PostgresService;

/**
 *
 */
public class ServerApplicationModule extends AbstractApplicationModule {

	private final ApplicationConfiguration configuration;

	public ServerApplicationModule(ApplicationConfiguration configuration) {
		this.configuration = configuration;
	}

	// override
	@Override
	protected void configure() {
		super.configure();

		// configuration participants
		defineExtensionPoint(ConfigurationParticipant.class);
		extend(ConfigurationParticipant.class, PostgresService.class);

	}

}
