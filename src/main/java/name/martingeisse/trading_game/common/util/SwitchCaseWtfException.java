package name.martingeisse.trading_game.common.util;

/**
 * This exception gets thrown when a switch/case encounters an "impossible" case. This can happen, for example, when
 * a new constant gets added to an enum type and a switch/case for that type wasn't adjusted to deal with the new
 * constant.
 */
public class SwitchCaseWtfException extends WtfException {
}
