package name.martingeisse.trading_game.game.cqrs;

import name.martingeisse.trading_game.game.Game;

/**
 * A key object for a projection of type T. Keys must support equals() and hashCode(). Keys also implement the projection
 * logic for simplicity.
 *
 * TODO woher wird klar, wann die Projektion aktualisiert werden muss? Bei ES wäre das nach einem definierten
 * Verfahren: Der Projector bzw. einfacher der ProjectionKey gibt alle relevanten Events per Filter an, und man
 * sieht sofort, welche betroffen sind. Bei nicht-ES ist die Änderung (der Event) implizit und man müsste sich per
 * Listener in die jeweiligen Objekte hängen, aber das ist auch schnell sehr komplex.
 */
public interface ProjectionKey<T> {

	/**
	 * Creates the projection from the game state. The game object should be assumed to be owned by the calling
	 * thread for the duration of this call.
	 */
	public T project(Game game);

}
