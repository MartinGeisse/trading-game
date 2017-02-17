/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.game.NameAlreadyUsedException;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import name.martingeisse.wicket.simpleform.SimpleFormPanel;
import org.apache.wicket.model.Model;

/**
 * This is the main page for controlling the game.
 */
public class RenamePlayerPage extends AbstractPage {

	private String newName;

	/**
	 * Constructor
	 */
	public RenamePlayerPage() {
		SimpleFormPanel formPanel = new SimpleFormPanel<RenamePlayerPage>("formPanel", Model.of(this)) {
			@Override
			protected void onSubmit() {
				try {
					getPlayer().setName(newName);
					setResponsePage(MainPage.class);
				} catch (NameAlreadyUsedException e) {
					get("form:formBlocks:1:decoratedBody:textField").error("This name is already taken.");
				}
			}
		};
		formPanel.prepareDecorator().withLabel("New name").withModel("newName").addTextField();
		add(formPanel);
		newName = getPlayer().getName();
	}

	/**
	 * Getter method.
	 *
	 * @return the newName
	 */
	public String getNewName() {
		return newName;
	}

	/**
	 * Setter method.
	 *
	 * @param newName the newName
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}

}
