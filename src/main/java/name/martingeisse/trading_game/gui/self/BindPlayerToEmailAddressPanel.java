/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui.self;

import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.gui.gamepage.GuiNavigationUtil;
import name.martingeisse.trading_game.gui.util.AjaxRequestUtil;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import name.martingeisse.wicket.simpleform.SimpleFormPanel;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.model.Model;

/**
 * Allows to bind the player to an email address.
 */
public class BindPlayerToEmailAddressPanel extends AbstractPanel {

	private String emailAddress;

	/**
	 * Constructor
	 *
	 * @param id the wicket id
	 */
	public BindPlayerToEmailAddressPanel(String id) {
		super(id);
		SimpleFormPanel formPanel = new SimpleFormPanel<BindPlayerToEmailAddressPanel>("formPanel", Model.of(this)) {
			@Override
			protected void onSubmit() {
				Player player = getPlayer();
				System.out.println("*** TODO send email to " + emailAddress + " with login token " + player.getLoginToken()); // TODO
				player.setEmailAddress(emailAddress);
				GuiNavigationUtil.setPanel(this, SelfPlayerPanel::new, AjaxRequestUtil.getAjaxRequestTarget());
			}
		};
		formPanel.prepareDecorator().withLabel("Email address").withModel("emailAddress").addTextField();
		formPanel.prepareSpecialFormBlock().withText("Send").addSubmitButton();
		formPanel.getForm().add(new AjaxFormSubmitBehavior("submit") {
			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.setPreventDefault(true);
			}
		}).setOutputMarkupId(true);
		add(formPanel);
	}

	/**
	 * Getter method.
	 *
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Setter method.
	 *
	 * @param emailAddress the emailAddress
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
