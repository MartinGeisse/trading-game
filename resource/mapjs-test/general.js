
//
// simple jquery helpers
//

$.fn.awesomeDrag = function(handler) {

	var dragging = false;
	var dragStartX, dragStartY;

	this.on('mousemove', function(event) {
		if (dragging) {
			handler(event.pageX - dragStartX, event.pageY - dragStartY);
			dragStartX = event.pageX;
			dragStartY = event.pageY;
		}
	});

	this.on('mousedown', function(event) {
		dragging = true;
		dragStartX = event.pageX;
		dragStartY = event.pageY;
	});

	this.on('mouseup', function(event) {
		dragging = false;
	});

};

getRelativePositionForMouseEvent = function(event) {
    return {
        x: event.offsetX || (event.pageX - $(event.target).offset().left),
        y: event.offsetY || (event.pageY - $(event.target).offset().top),
    };
    // seems to be equivalent, but I'll keep it here just in case problems pop up
//    return {
//        x: (event.offsetX || event.clientX - $(event.target).offset().left + window.pageXOffset),
//        y: (event.offsetY || event.clientY - $(event.target).offset().top + window.pageYOffset),
//    };
};
