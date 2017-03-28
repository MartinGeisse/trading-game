
DROP SCHEMA IF EXISTS "game" CASCADE;
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
	-- foreign key defined below; for player ship inventories this must be the ship's owner, but for space station
	-- inventories it can be any player. For asteroids it is NULL since they use the inventory structure to store
	-- mining yield, but that doesn't belong to anyone (and is actually a template that is cloned to create mined items)
	"playerId" bigint,
	"itemType" character varying(2000) NOT NULL,
	"quantity" int NOT NULL
);
CREATE INDEX "InventorySlot_inventoryIdItemTypeIndex" ON "game"."InventorySlot" ("inventoryId", "itemType");
CREATE INDEX "InventorySlot_playerIdinventoryIdItemTypeIndex" ON "game"."InventorySlot" ("playerId", "inventoryId", "itemType");


-----------------------------------------------------------------------------------------------------------------------
-- space objects
-----------------------------------------------------------------------------------------------------------------------

CREATE TYPE "game"."SpaceObjectType" AS ENUM ('ASTEROID', 'PLANET', 'PLAYER_SHIP', 'SPACE_STATION', 'STAR');

CREATE TABLE "game"."SpaceObjectBaseData" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"type" "game"."SpaceObjectType" NOT NULL,
	"name" character varying(2000) NOT NULL,
	"position" point NOT NULL,
	"inventoryId" bigint REFERENCES "game"."Inventory",
	"longField1" bigint
);
CREATE INDEX "SpaceObjectBaseData_nameIndex" ON "game"."SpaceObjectBaseData" ("name");
CREATE INDEX "SpaceObjectBaseData_inventoryIdIndex" ON "game"."SpaceObjectBaseData" ("inventoryId");
CREATE INDEX "SpaceObjectBaseData_positionIndex" ON "game"."SpaceObjectBaseData" USING GIST ("position" point_ops);

CREATE TYPE "game"."PlayerShipEquipmentSlotType" AS ENUM ('ENGINE', 'MINING', 'CARGO');

CREATE TABLE "game"."PlayerShipEquipmentSlot" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"spaceObjectBaseDataId" bigint NOT NULL REFERENCES "game"."SpaceObjectBaseData" ON DELETE CASCADE,
	"slotType" "game"."PlayerShipEquipmentSlotType" NOT NULL,
	"itemType" character varying(2000) NOT NULL
);
CREATE UNIQUE INDEX "PlayerShipEquipmentSlot_spaceObjectBaseDataIdSlotTypeIndex" ON "game"."PlayerShipEquipmentSlot" ("spaceObjectBaseDataId", "slotType");

-----------------------------------------------------------------------------------------------------------------------
-- actions
-----------------------------------------------------------------------------------------------------------------------

CREATE TABLE "game"."ActionQueue" (
	"id" bigserial NOT NULL PRIMARY KEY
);

CREATE TABLE "game"."ActionQueueSlot" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"actionQueueId" bigint NOT NULL REFERENCES "game"."ActionQueue" ON DELETE CASCADE,
	"prerequisite" boolean NOT NULL,
	"action" jsonb NOT NULL,
	"started" boolean NOT NULL
);
CREATE INDEX "ActionQueueSlot_mainIndex" ON "game"."ActionQueueSlot" ("actionQueueId" ASC, "prerequisite" DESC, "id" ASC);

-----------------------------------------------------------------------------------------------------------------------
-- players
-----------------------------------------------------------------------------------------------------------------------

CREATE TYPE "game"."PlayerAttributeKey" AS ENUM ('SHIP_MOVEMENT_SPEED', 'MAXIMUM_CARGO_MASS');

CREATE TABLE "game"."Player" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"name" character varying(500) NOT NULL,
	"loginToken" character varying(500),
	"emailAddress" character varying(500),
	"shipId" bigint NOT NULL REFERENCES "game"."SpaceObjectBaseData",
	"actionQueueId" bigint NOT NULL REFERENCES "game"."ActionQueue",
	"money" bigint NOT NULL
);
CREATE INDEX "Player_nameIndex" ON "game"."Player" ("name");
CREATE INDEX "Player_shipIdIndex" ON "game"."Player" ("shipId");
CREATE INDEX "Player_actionQueueId" ON "game"."Player" ("actionQueueId");
CREATE UNIQUE INDEX "Player_loginToken" ON "game"."Player" ("loginToken");
CREATE INDEX "Player_emailAddress" ON "game"."Player" ("emailAddress");

CREATE TABLE "game"."PlayerSkill" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"playerId" bigint NOT NULL REFERENCES "game"."Player" ON DELETE CASCADE,
	"skillType" character varying(2000) NOT NULL
);
CREATE INDEX "PlayerSkill_playerIdIndex" ON "game"."PlayerSkill" ("playerId");

CREATE TABLE "game"."PlayerSkillLearningQueueSlot" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"playerId" bigint NOT NULL REFERENCES "game"."Player" ON DELETE CASCADE,
	"skillType" character varying(2000) NOT NULL,
	"learningPoints" integer NOT NULL,
	"learningOrderIndex" integer -- entries with learningOrderIndex null aren't actively part of the queue, but
		-- have acquired learning points that should not be dropped
);
CREATE INDEX "PlayerSkillLearningQueueSlot_playerIdIndex" ON "game"."PlayerSkillLearningQueueSlot" ("playerId");

CREATE TABLE "game"."CachedPlayerAttribute" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"playerId" bigint NOT NULL REFERENCES "game"."Player" ON DELETE CASCADE,
	"key" "game"."PlayerAttributeKey" NOT NULL,
	"value" text NOT NULL
);
CREATE UNIQUE INDEX "CachedPlayerAttribute_playerIdKeyIndex" ON "game"."CachedPlayerAttribute" ("playerId", "key");

-----------------------------------------------------------------------------------------------------------------------
-- more foreign keys which could not be defined above due to table creation order, or because they are circular
-----------------------------------------------------------------------------------------------------------------------

ALTER TABLE "game"."InventorySlot" ADD FOREIGN KEY ("playerId") REFERENCES "game"."Player";
