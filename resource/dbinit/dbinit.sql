
CREATE SCHEMA "game";

-----------------------------------------------------------------------------------------------------------------------
-- inventories
-----------------------------------------------------------------------------------------------------------------------

CREATE TABLE "game"."Inventory" (
	"id" bigserial NOT NULL PRIMARY KEY
);

-- TODO index on inventoryId?
CREATE TABLE "game"."InventorySlot" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"inventoryId" bigint NOT NULL REFERENCES "game"."Inventory" ON DELETE CASCADE,
	"quantity" int NOT NULL
	-- TODO item type
);


-----------------------------------------------------------------------------------------------------------------------
-- space objects
-----------------------------------------------------------------------------------------------------------------------

CREATE TYPE "game"."SpaceObjectType" AS ENUM ('Planet', 'Asteroid', 'PlayerShip');

-- TODO index on inventoryId?
CREATE TABLE "game"."SpaceObjectBaseData" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"type" "game"."SpaceObjectType" NOT NULL,
	"name" character varying(2000) NOT NULL,
	"x" bigint NOT NULL,
	"y" bigint NOT NULL,
	"inventoryId" bigint REFERENCES "game"."Inventory",
	"longField1" bigint
);
CREATE INDEX "SpaceObjectBaseData_nameIndex" ON "game"."SpaceObjectBaseData" ("name");


-----------------------------------------------------------------------------------------------------------------------
-- players
-----------------------------------------------------------------------------------------------------------------------

-- TODO index on shipId?
CREATE TABLE "game"."Player" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"name" character varying(2000) NOT NULL,
	"shipId" bigint NOT NULL REFERENCES "game"."SpaceObjectBaseData"
	-- TODO actions
	-- TODO skills
);
CREATE INDEX "Player_nameIndex" ON "game"."Player" ("name");
