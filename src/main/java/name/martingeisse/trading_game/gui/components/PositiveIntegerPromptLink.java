package name.martingeisse.trading_game.gui.components;

import name.martingeisse.trading_game.game.GameLogicException;
import name.martingeisse.trading_game.platform.util.parser.IntegerParser;
import name.martingeisse.trading_game.platform.util.parser.Parser;
import name.martingeisse.trading_game.platform.util.parser.ParserException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

/**
 *
 */
public abstract class PositiveIntegerPromptLink<M> extends PromptAjaxLink<M, Integer> {

	public PositiveIntegerPromptLink(String id) {
		super(id);
	}

	public PositiveIntegerPromptLink(String id, IModel<M> model) {
		super(id, model);
	}

	@Override
	protected final Parser<Integer> getParser() {
		return IntegerParser.INSTANCE;
	}

	protected String getParseErrorMessage(ParserException e) throws GameLogicException {
		return "Please enter a number.";
	}

	protected String checkInputForValidationError(Integer parsedInput) throws GameLogicException {
		return (parsedInput < 0 ? "Negative input is not allowed" : null);
	}

	@Override
	protected final void onClick(AjaxRequestTarget target, Integer parsedInput) throws GameLogicException {
		if (parsedInput > 0) {
			onClick(target, parsedInput.intValue());
		}
	}

	protected abstract void onClick(AjaxRequestTarget target, int input) throws GameLogicException;

}
