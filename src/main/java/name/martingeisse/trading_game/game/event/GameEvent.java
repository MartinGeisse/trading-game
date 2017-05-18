package name.martingeisse.trading_game.game.event;

/**
 * TODO should have two subclasses:
 * <p>
 * DatabaseEvent is fired for changes in the database, containing the row,
 * new data, maybe old data or delta. Does not contain information what
 * happens on a higher level or WHY the database was changed that way.
 * <p>
 * DomainEvent is fired when something happens beyond changes in data. Contains
 * domain-level information what happens and why. Does not indicate new data,
 * since DatabaseEvent already does that.
 * <p>
 * Example: Player A transfers money to player B.
 * - DatabaseEvent for player A containing the new, reduced money amount
 * - DatabaseEvent for player B containing the new, increased money amount
 * - DomainEvent (MoneyTransferEvent) saying "player A transferred amount X
 * to player B", e.g. to show a message to player B.
 * <p>
 * This way there is no redundancy in the event classes.
 * <p>
 * However, this will only work if all database changes are captured including all
 * data, which requires database support. This isn't really possible to fake.
 */
public abstract class GameEvent {
}
