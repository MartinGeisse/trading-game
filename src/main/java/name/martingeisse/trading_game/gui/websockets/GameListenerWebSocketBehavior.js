GameListenerWebSocketBehavior = function(timeoutMilliseconds) {

	// Any data transfer through the connection means it is kept alive. So we start a timer for each data transfer and
	// send an explicit keepalive frame only when *all* of them have expired. Note that right now, client-originated
	// messages sent via Wicket.WebSocket.INSTANCE.send() are missed by this logic (producing unnecessary keepalive
	// frames), but they are not used yet anyway -- all client-originated events use regular AJAX calls since that's
	// easier to handle in Wicket.

	var stackedKeepaliveTimers = 0;

	function startKeepaliveTimer() {
		stackedKeepaliveTimers++;
		setTimeout(onKeepaliveTimer, timeoutMilliseconds / 2);
	}

	function onKeepaliveTimer() {
		stackedKeepaliveTimers--;
		if (stackedKeepaliveTimers == 0) {
			Wicket.WebSocket.INSTANCE.send('keepalive');
			startKeepaliveTimer();
		}
	}

	var oldHandler = Wicket.WebSocket.INSTANCE.ws.onmessage;
	Wicket.WebSocket.INSTANCE.ws.onmessage = function(event) {
		startKeepaliveTimer();
		oldHandler(event);
	};

	startKeepaliveTimer();
};
