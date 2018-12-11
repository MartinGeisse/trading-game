
//
// simple jquery helpers
//

$.fn.awesomeDrag = function(dragHandler, clickHandler) {

	var dragging = false;
	var dragStartX, dragStartY, dragLastX, dragLastY;

	this.on('mousemove', function(event) {
		if (dragging) {
		    if (dragHandler) {
			    dragHandler(event.pageX - dragLastX, event.pageY - dragLastY);
		    }
			dragLastX = event.pageX;
			dragLastY = event.pageY;
		}
	});

	this.on('mousedown', function(event) {
		dragging = true;
		dragLastX = dragStartX = event.pageX;
		dragLastY = dragStartY = event.pageY;
	});

	this.on('mouseup', function(event) {
		dragging = false;
		if (clickHandler && Math.abs(event.pageX - dragStartX) < 2 && Math.abs(event.pageY - dragStartY) < 2) {
		    return clickHandler(event);
		} else {
		    return false;
		}
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
