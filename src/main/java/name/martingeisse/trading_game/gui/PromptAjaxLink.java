package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.platform.util.JavascriptAssemblerUtil;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;

/**
 *
 */
public abstract class PromptAjaxLink<T> extends AjaxLink<T> {

	public PromptAjaxLink(String id) {
		super(id);
	}

	public PromptAjaxLink(String id, IModel<T> model) {
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
		onClick(target, getRequest().getRequestParameters().getParameterValue("promptInput").toString());
	}

	protected abstract void onClick(AjaxRequestTarget target, String promptInput);

}
