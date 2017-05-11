
setStateCookie = function(name, value) {
	Cookies.set(name, value, {
		expires: 365,
		path: '/',
		secure: false
	});
};

getStateCookie = Cookies.getJSON;
