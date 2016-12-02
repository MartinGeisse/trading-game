/**
 * Copyright (c) 2010 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui.wicket.page;

import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.gui.wicket.MyWicketApplication;
import name.martingeisse.trading_game.gui.wicket.MyWicketSession;
import org.apache.wicket.ajax.WicketAjaxDebugJQueryResourceReference;
import org.apache.wicket.ajax.WicketAjaxJQueryResourceReference;
import org.apache.wicket.ajax.WicketEventJQueryResourceReference;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;

/**
 * This page base class includes common CSS and JS resources.
 */
public class AbstractPage extends WebPage {

	/**
	 * Constructor.
	 */
	public AbstractPage() {
		super();
	}

	/**
	 * Constructor.
	 * @param model the page model
	 */
	public AbstractPage(final IModel<?> model) {
		super(model);
	}

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractPage(final PageParameters parameters) {
		super(parameters);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(new PriorityHeaderItem(CssHeaderItem.forReference(new CssResourceReference(AbstractPage.class, "common.css"))));
		response.render(new PriorityHeaderItem(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(AbstractPage.class, "bootstrap.js"))));
	}

	/**
	 *
	 */
	private void markJavascriptAlreadyRendered(IHeaderResponse response, ResourceReference reference) {
		JavaScriptHeaderItem jQueryHeaderItem = JavaScriptHeaderItem.forReference(reference);
		for (Object renderToken : jQueryHeaderItem.getRenderTokens()) {
			response.markRendered(renderToken);
		}
	}

	public final Game getGame() {
		return MyWicketApplication.get().getDependency(Game.class);
	}

	public final GameDefinition getGameDefinition() {
		return MyWicketApplication.get().getDependency(GameDefinition.class);
	}

	public final IModel<GameDefinition> gameDefinitionModel() {
		return new AbstractReadOnlyModel<GameDefinition>() {
			@Override
			public GameDefinition getObject() {
				return getGameDefinition();
			}
		};
	}

	public final <T> IModel<T> gameDefinitionModel(String propertyPath) {
		return new PropertyModel<T>(gameDefinitionModel(), propertyPath);
	}

	public final Player getPlayer() {
		return MyWicketSession.get().getPlayer();
	}

}
