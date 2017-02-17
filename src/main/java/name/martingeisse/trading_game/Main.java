/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.trading_game;

import name.martingeisse.trading_game.platform.application.Launcher;

/**
 *
 */
public final class Main {

	// prevent instantiation
	private Main() {
	}

	/**
	 * The main method.
	 *
	 * @param args command-line arguments (ignored)
	 */
	public static void main(String[] args) throws Exception {
		Launcher.launch();
	}

}
