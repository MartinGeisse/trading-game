package name.martingeisse.trading_game.gui.components;

import name.martingeisse.trading_game.game.GameLogicException;
import name.martingeisse.trading_game.platform.util.JavascriptAssemblerUtil;
import name.martingeisse.trading_game.platform.util.parser.Parser;
import name.martingeisse.trading_game.platform.util.parser.ParserException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;

/**
 * Shows a prompt dialog before sending the ajax request.
 * <p>
 * Thefact that this class currently uses the built-in browser prompt dialog should be considered an implementation
 * detail. That dialog doesn't fit the UI style and may be augmented with a warning or even be blocked entirely by
 * browsers.
 */
public abstract class PromptAjaxLink<M, P> extends AjaxLink<M> {

	public PromptAjaxLink(String id) {
		super(id);
	}

	public PromptAjaxLink(String id, IModel<M> model) {
		super(id, model);
	}

	protected abstract String getPrompt();

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		// TODO only works once, after that it takes the first input again and ignores the second one
		AjaxCallListener listener = new AjaxCallListener();
		listener.onInit("attrs.promptInput = prompt('" + JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(getPrompt()) + "');");
		listener.onInit("if (!attrs.ep) attrs.ep = [];");
		listener.onInit("attrs.ep.push({name: 'promptInput', value: attrs.promptInput});");
		listener.onPrecondition("return (typeof attrs.promptInput) == 'string' && attrs.promptInput != '';");
		attributes.getAjaxCallListeners().add(listener);
	}

	@Override
	public void onClick(AjaxRequestTarget target) {
		try {

			// prepare input
			String textInput = getRequest().getRequestParameters().getParameterValue("promptInput").toString();
			if (textInput == null) {
				onClickWithoutInput(target);
				return;
			}
			textInput = textInput.trim();
			if (textInput.isEmpty()) {
				onClickWithoutInput(target);
				return;
			}

			// parse input
			P parsedInput;
			try {
				parsedInput = getParser().parse(textInput);
			} catch (ParserException e) {
				onClickWithMalformedInput(target, e);
				return;
			}

			// validate input
			String validationError = checkInputForValidationError(parsedInput);
			if (validationError != null) {
				onClickWithValidationError(target, parsedInput, validationError);
			}

			// everything is OK
			onClick(target, parsedInput);

		} catch (GameLogicException e) {
			target.appendJavaScript("alert('" + JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(e.getMessage()) + "');");
		}
	}

	protected abstract Parser<P> getParser();

	protected void onClickWithoutInput(AjaxRequestTarget target) throws GameLogicException {
	}

	protected void onClickWithMalformedInput(AjaxRequestTarget target, ParserException e) throws GameLogicException {
		target.appendJavaScript("alert('" + JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(getParseErrorMessage(e)) + "');");
	}

	protected String getParseErrorMessage(ParserException e) throws GameLogicException {
		return "Invalid input";
	}

	protected String checkInputForValidationError(P parsedInput) throws GameLogicException {
		return null;
	}

	protected void onClickWithValidationError(AjaxRequestTarget target, P parsedInput, String validationError) throws GameLogicException {
		target.appendJavaScript("alert('" + JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(validationError) + "');");
	}

	protected abstract void onClick(AjaxRequestTarget target, P parsedInput) throws GameLogicException;

}
