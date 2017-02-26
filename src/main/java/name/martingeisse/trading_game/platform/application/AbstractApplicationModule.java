/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.trading_game.platform.application;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.multibindings.Multibinder;
import name.martingeisse.trading_game.game.action.ActionSerializer;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.event.GameEventListener;
import name.martingeisse.trading_game.game.item.ItemTypeSerializer;
import name.martingeisse.trading_game.game.skill.SkillSerializer;
import name.martingeisse.trading_game.platform.application.configuration.ConfigurationParticipant;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

/**
 *
 */
public abstract class AbstractApplicationModule extends AbstractModule {

	@Override
	protected void configure() {

		// various services
		bind(ItemTypeSerializer.class).to(GameDefinition.class);
		bind(ActionSerializer.class).to(GameDefinition.class);
		bind(SkillSerializer.class).to(GameDefinition.class);

		// configuration participants
		defineExtensionPoint(ConfigurationParticipant.class);
		extend(ConfigurationParticipant.class, PostgresService.class);

		// game listeners
		defineExtensionPoint(GameEventListener.class);

	}

	/**
	 * Defines an extension point.
	 *
	 * @param extensionPointInterface the interface that extensions for the defined
	 * extension point must implement
	 */
	public <T> void defineExtensionPoint(Class<T> extensionPointInterface) {
		Multibinder.newSetBinder(binder(), extensionPointInterface);
	}

	/**
	 * Adds an extension for an extension point.
	 *
	 * The extension must be an instantiable class that implements the extension point
	 * interface.
	 *
	 * Creating a concrete implementation is done by Guice, so it will have its dependencies
	 * injected. Decorate the extension class with {@link Inject} and scope annotations
	 * as usual for Guice.
	 *
	 * There is no requirement that the extension point gets defined *before* all its
	 * extensions, only that it gets defined at some point.
	 *
	 * @param extensionPointInterface the extension point interface
	 * @param extensionClass the concrete implementation class for this extension
	 */
	public <T> void extend(Class<T> extensionPointInterface, Class<? extends T> extensionClass) {
		Multibinder.newSetBinder(binder(), extensionPointInterface).addBinding().to(extensionClass);
	}

}
