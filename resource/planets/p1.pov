#include "colors.inc"
#include "textures.inc"
#include "shapes.inc"
#include "metals.inc"
#include "glass.inc"
#include "woods.inc"

global_settings {
	noise_generator 3
	max_trace_level 4
}        

camera {
   location <0, 0, -2.3>
   look_at <0, 0, 0>
   right x
}


light_source {
	<-40, 40, -15>
	color rgb <0.5, 0.5, 0.5>
} 

light_source {
	<-20, 20, -30>
	color rgb <0.5, 0.5, 0.5>
} 

light_source {
	<0, 0, -15>
	color rgb <0.2, 0.2, 0.2>
	shadowless
}

sphere {
	<0,0,0> 1
	texture {
		pigment {
			color White
		}
		normal {
			bump_map {
				png "textures/bumpmap.png"
				bump_size 10.0
			}
		}

	}
}
