select * from "game"."SpaceObjectBaseData"
where "position" >> '(2.99, 0)'::point
and "position" << '(5.01, 0)'::point
and "position" >^ '(0, 2.00)'::point
and "position" <^ '(0, 5.01)'::point;
