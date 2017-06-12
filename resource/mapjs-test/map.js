
AwesomeMap = {

	//
	// The viewport. Handles display transformation, panning, and zooming.
	//
	Viewport: (function() {

		function constructor() {

			// the factor by which displayed map features are larger than real map features
			this.zoom = 1;

			// the position of the map's origin on the page measured in pixels
			this.mapOriginX = 0;
			this.mapOriginY = 0;

			// used by the map internally
			this.drawCallback = function() {};

		}

		constructor.prototype = {

			applyToContext: function(context) {
				context.translate(this.mapOriginX, this.mapOriginY);
				context.scale(this.zoom, this.zoom);
			},

			applyToContextReverse: function(context) {
				context.scale(1.0 / this.zoom, 1.0 / this.zoom);
				context.translate(-this.mapOriginX, -this.mapOriginY);
			},

			applyDeltaToContext: function(context, baseTransform) {
				// TODO optimize
				this.applyToContext(context);
				baseTransform.applyToContextReverse(context);
			},

			transformPoint: function(point) {
				return {
					x: this.zoom * point.x + this.mapOriginX,
					y: this.zoom * point.y + this.mapOriginY,
				};
			},

			transformVector: function(vector) {
				return {
					x: this.zoom * vector.x,
					y: this.zoom * vector.y,
				};
			},

			transformDistance: function(distance) {
				return this.zoom * distance;
			},

			untransformPoint: function(point) {
				return {
					x: (point.x - this.mapOriginX) / this.zoom,
					y: (point.y - this.mapOriginY) / this.zoom,
				};
			},

			untransformVector: function(vector) {
				return {
					x: vector.x / this.zoom,
					y: vector.y / this.zoom,
				};
			},

			untransformDistance: function(distance) {
				return distance / this.zoom;
			},

			zoomAtPixelPosition: function(pixelX, pixelY, factor) {
				this.zoom *= factor;
				this.mapOriginX += (pixelX - this.mapOriginX) * (1 - factor);
				this.mapOriginY += (pixelY - this.mapOriginY) * (1 - factor);
				this.drawCallback();
			},

			panByPixelAmount: function(pixelDeltaX, pixelDeltaY) {
				this.mapOriginX += pixelDeltaX;
				this.mapOriginY += pixelDeltaY;
				this.drawCallback();
			},

			cloneForTransformation: function() {
				var clone = new AwesomeMap.Viewport();
				clone.zoom = this.zoom;
				clone.mapOriginX = this.mapOriginX;
				clone.mapOriginY = this.mapOriginY;
				return clone;
			},

		};

		return constructor;
	})(),


	//
	// a map layer containing objects to render
	//
	Layer: (function() {

		function constructor() {
			this.objects = [];
		}

		constructor.prototype = {

			//
			// Renders this layer.
			//
			// Expects the context's transformation to be the identity. Calling this method is surrounded by
			// context.save() / context.restore() to limit the effect of state changes. This method must return with
			// the state stack at the same size as when it is called.
			//
			render: function(context, viewport) {
				viewport.applyToContext(context);
				context.beginPath();
				for (var i in this.objects) {
					var o = this.objects[i];
					context.moveTo(o.x + o.r, o.y);
					context.arc(o.x, o.y, o.r, 0, 2 * Math.PI);
				}
				context.fill();
			},

		};

		return constructor;
	})(),

	//
	// the enclosing map object itself
	//
	Map: (function() {

		function constructor(canvas) {
			this.canvas = canvas;
			this.viewport = new AwesomeMap.Viewport();
			this.viewport.drawCallback = this.render.bind(this);
			this.layers = [];
			this._renderScheduled = false;
			this._resizeScheduled = false;
		}

		constructor.prototype = {

			//
			// Renders the map.
			//
			// This method is rate-limited so it can be called very often without actually redrawing every time.
			//
			render: function() {

				function renderNow() {
					var context = this.canvas.getContext('2d');

					// black background
					context.setTransform(1, 0, 0, 1, 0, 0);
					context.fillStyle = '#000000';
					context.fillRect(0, 0, context.canvas.width, context.canvas.height);

					// space objects
					context.fillStyle = '#0000ff';
					for (var i in this.layers) {
						context.save();
						this.layers[i].render(context, map.viewport);
						context.restore();
					}

					// TODO remove: show origin
					context.fillStyle = '#ff0000';
					context.beginPath();
					context.arc(0, 0, 50, 0, 2 * Math.PI);
					context.fill();

					this._renderScheduled = false;
				}

				if (!this._renderScheduled) {
					setTimeout(renderNow.bind(this), 10);
					this._renderScheduled = true;
				}

			},

			//
			// Install a resizer function. This function should determine the new desired size of the canvas and
			// apply that size to the canvas (which gets passed as an argument to the resizer function). The resizer
			// is rate-limited using the specified timeout (in milliseconds). Installing the resizer will call it
			// once initially to configure the initial size.
			//
			installResizer(resizer, timeout) {
				var map = this;

                function handleResize() {
                	resizer(map.canvas);
					map.render();
                    map._resizeScheduled = false;
                }

                $(window).resize(function() {
                	if (!map._resizeScheduled) {
                    	setTimeout(handleResize.bind(map), timeout);
                		map._resizeScheduled = true;
                	}
                });

				map._resizeScheduled = true;
                handleResize.bind(map)();

			},

		};

		return constructor;
	})(),

};
