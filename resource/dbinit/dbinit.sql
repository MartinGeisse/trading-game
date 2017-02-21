
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
	"position" point NOT NULL,
	"inventoryId" bigint REFERENCES "game"."Inventory",
	"longField1" bigint
);
CREATE INDEX "SpaceObjectBaseData_nameIndex" ON "game"."SpaceObjectBaseData" ("name");
CREATE INDEX "SpaceObjectBaseData_inventoryIdIndex" ON "game"."SpaceObjectBaseData" ("inventoryId");
CREATE INDEX "SpaceObjectBaseData_positionIndex" ON "game"."SpaceObjectBaseData" USING GIST ("position" point_ops);

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

CREATE TABLE "game"."Player" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"name" character varying(2000) NOT NULL,
	"shipId" bigint NOT NULL REFERENCES "game"."SpaceObjectBaseData",
	"actionQueueId" bigint NOT NULL REFERENCES "game"."ActionQueue"
);
CREATE INDEX "Player_nameIndex" ON "game"."Player" ("name");
CREATE INDEX "Player_shipIdIndex" ON "game"."Player" ("shipId");
CREATE INDEX "Player_actionQueueId" ON "game"."Player" ("actionQueueId");

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
