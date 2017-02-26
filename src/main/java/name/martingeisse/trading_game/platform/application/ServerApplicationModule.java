/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.trading_game.platform.application;

import name.martingeisse.trading_game.game.action.ActionSerializer;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.event.GameEventListener;
import name.martingeisse.trading_game.game.item.ItemTypeSerializer;
import name.martingeisse.trading_game.game.skill.SkillSerializer;
import name.martingeisse.trading_game.platform.application.configuration.ApplicationConfiguration;
import name.martingeisse.trading_game.platform.application.configuration.ConfigurationParticipant;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

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
	}

}
