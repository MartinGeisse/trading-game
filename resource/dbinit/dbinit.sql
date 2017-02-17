
CREATE SCHEMA "game";

CREATE TYPE "game"."SpaceObjectType" AS ENUM ('Planet', 'Asteroid', 'PlayerShip');

CREATE TABLE "game"."SpaceObjectBaseData" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"type" "game"."SpaceObjectType" NOT NULL,
	"name" character varying(2000) NOT NULL,
	"x" bigint not null,
	"y" bigint not null
);
CREATE INDEX "SpaceObjectBaseData_nameIndex" ON "game"."SpaceObjectBaseData" ("name");
