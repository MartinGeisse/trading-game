
setStateCookie = function(name, value) {
	Cookies.set(name, value, {
		expires: 365,
		path: '/',
		secure: false
	});
};

getStateCookie = function(name) {
	var result = Cookies.getJSON(name);
	if ((typeof result) === 'undefined') {
		return null;
	} else {
		return result;
	}
};
