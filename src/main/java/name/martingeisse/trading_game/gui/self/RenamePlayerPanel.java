/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui.self;

import name.martingeisse.trading_game.game.NameAlreadyUsedException;
import name.martingeisse.trading_game.gui.gamepage.MainMenuTabbedPanel;
import name.martingeisse.trading_game.gui.util.AjaxRequestUtil;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import name.martingeisse.wicket.simpleform.SimpleFormPanel;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.model.Model;

/**
 * Allows to rename the player.
 */
public class RenamePlayerPanel extends AbstractPanel {

	private String newName;

	/**
	 * Constructor
	 *
	 * @param id the wicket id
	 */
	public RenamePlayerPanel(String id) {
		super(id);
		SimpleFormPanel formPanel = new SimpleFormPanel<RenamePlayerPanel>("formPanel", Model.of(this)) {
			@Override
			protected void onSubmit() {
				try {
					getPlayer().setName(newName);
					MainMenuTabbedPanel.replaceTabPanel(this, SelfPlayerPanel::new, AjaxRequestUtil.getAjaxRequestTarget());
				} catch (NameAlreadyUsedException e) {
					get("form:formBlocks:1:decoratedBody:textField").error("This name is already taken.");
				}
			}
		};
		formPanel.prepareDecorator().withLabel("New name").withModel("newName").addTextField();
		formPanel.prepareSpecialFormBlock().withText("Rename").addSubmitButton();
		formPanel.getForm().add(new AjaxFormSubmitBehavior("submit") {
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.setPreventDefault(true);
			}
		}).setOutputMarkupId(true);
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
