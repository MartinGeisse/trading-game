
setStateCookie = function(name, value) {
	Cookies.set(name, value, {
		expires: 365,
		path: '/',
		domain: 'localhost', // TODO
		secure: false
	});
};

getStateCookie = Cookies.getJSON;
