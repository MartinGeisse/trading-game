
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

