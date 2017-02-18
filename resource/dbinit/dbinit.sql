
CREATE SCHEMA "game";

-----------------------------------------------------------------------------------------------------------------------
-- inventories
-----------------------------------------------------------------------------------------------------------------------

CREATE TABLE "game"."Inventory" (
	"id" bigserial NOT NULL PRIMARY KEY
);

CREATE TABLE "game"."InventorySlot" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"inventoryId" bigint NOT NULL REFERENCES "game"."Inventory" ON DELETE CASCADE,
	"itemType" character varying(2000) NOT NULL,
	"quantity" int NOT NULL
);
CREATE INDEX "InventorySlot_inventoryIdItemTypeIndex" ON "game"."InventorySlot" ("inventoryId", "itemType");


-----------------------------------------------------------------------------------------------------------------------
-- space objects
-----------------------------------------------------------------------------------------------------------------------

CREATE TYPE "game"."SpaceObjectType" AS ENUM ('Planet', 'Asteroid', 'PlayerShip');

CREATE TABLE "game"."SpaceObjectBaseData" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"type" "game"."SpaceObjectType" NOT NULL,
	"name" character varying(2000) NOT NULL,
	"x" bigint NOT NULL, -- TODO cannot geo-index this since a 'point'-ops GiST index would require usage of the 'point'
		-- data type which consists of two float numbers, not bigint numbers
	"y" bigint NOT NULL,
	"inventoryId" bigint REFERENCES "game"."Inventory",
	"longField1" bigint
);
CREATE INDEX "SpaceObjectBaseData_nameIndex" ON "game"."SpaceObjectBaseData" ("name");
CREATE INDEX "SpaceObjectBaseData_inventoryIdIndex" ON "game"."SpaceObjectBaseData" ("inventoryId");


-----------------------------------------------------------------------------------------------------------------------
-- players
-----------------------------------------------------------------------------------------------------------------------

CREATE TABLE "game"."Player" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"name" character varying(2000) NOT NULL,
	"shipId" bigint NOT NULL REFERENCES "game"."SpaceObjectBaseData"
	-- TODO actions
	-- TODO skills
);
CREATE INDEX "Player_nameIndex" ON "game"."Player" ("name");
CREATE INDEX "Player_shipIdIndex" ON "game"."Player" ("shipId");
