package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.*;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class WebsocketsPage extends AbstractPage {

	private static final IWebSocketPushMessage newChatMessagePushMessage = new IWebSocketPushMessage() {
	};

	private static List<Pair<String, String>> messages = new ArrayList<>();

	private String inputName;
	private String inputText;

	/**
	 * Constructor.
	 */
	public WebsocketsPage() {

		add(new WebSocketBehavior() {

			@Override
			protected void onConnect(ConnectedMessage message) {
			}

			@Override
			protected void onClose(ClosedMessage message) {
			}

			@Override
			protected void onAbort(AbortedMessage message) {
			}

			@Override
			protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
				if (message == newChatMessagePushMessage) {
					handler.add(WebsocketsPage.this.get("messagesContainer"));
				}
			}

		});

		WebMarkupContainer messagesContainer = new WebMarkupContainer("messagesContainer");
		messagesContainer.setOutputMarkupId(true);
		add(messagesContainer);
		IModel<List<Pair<String, String>>> messagesModel = new LoadableDetachableModel<List<Pair<String, String>>>() {
			@Override
			protected List<Pair<String, String>> load() {
				synchronized (messages) {
					return new ArrayList<>(messages);
				}
			}
		};
		messagesContainer.add(new ListView<Pair<String, String>>("messages", messagesModel) {
			@Override
			protected void populateItem(ListItem<Pair<String, String>> item) {
				item.add(new Label("name", new PropertyModel<>(item.getModelObject(), "left")));
				item.add(new Label("text", new PropertyModel<>(item.getModelObject(), "right")));
			}
		});

		Form form = new Form("form");
		add(form);
		form.add(new TextField<>("name", new PropertyModel<>(this, "inputName")));
		TextField inputTextField = new TextField<>("text", new PropertyModel<>(this, "inputText"));
		inputTextField.setOutputMarkupId(true);
		form.add(inputTextField);
		AjaxSubmitLink submitLink = new AjaxSubmitLink("submit") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (inputName != null && inputText != null) {
					synchronized (messages) {
						messages.add(Pair.of(inputName, inputText));
					}
					target.add(inputTextField);
					target.appendJavaScript("$('#" + inputTextField.getMarkupId() + "').focus();");
					inputText = null;

					// update all clients via websockets
					IWebSocketConnectionRegistry registry = WebSocketSettings.Holder.get(getApplication()).getConnectionRegistry();
					for (IWebSocketConnection connection : registry.getConnections(getApplication())) {
						connection.sendMessage(newChatMessagePushMessage);
					}

				}
			}
		};
		form.add(submitLink);
		form.setDefaultButton(submitLink);

	}

	/**
	 * Getter method.
	 *
	 * @return the inputName
	 */
	public String getInputName() {
		return inputName;
	}

	/**
	 * Setter method.
	 *
	 * @param inputName the inputName
	 */
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	/**
	 * Getter method.
	 *
	 * @return the inputText
	 */
	public String getInputText() {
		return inputText;
	}

	/**
	 * Setter method.
	 *
	 * @param inputText the inputText
	 */
	public void setInputText(String inputText) {
		this.inputText = inputText;
	}

}
