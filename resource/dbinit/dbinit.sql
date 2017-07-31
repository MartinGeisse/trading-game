
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
	"quantity" int NOT NULL CHECK ("quantity" > 0)
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
	"name" character varying(2000) NOT NULL CHECK ("name" != ''),
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
	"name" character varying(500) NOT NULL CHECK ("name" != ''),
	"loginToken" character varying(500) CHECK (LENGTH("loginToken") > 10), -- TODO include (IS NULL OR) in the check?
	"emailAddress" character varying(500) CHECK (LENGTH("loginToken") >= 5),
	"shipId" bigint NOT NULL REFERENCES "game"."SpaceObjectBaseData",
	"actionQueueId" bigint NOT NULL REFERENCES "game"."ActionQueue",
	"money" bigint NOT NULL CHECK ("money" >= 0),
	"spentFoldingCredits" bigint NOT NULL DEFAULT 0 CHECK ("spentFoldingCredits" >= 0),
	"remainingPlayTime" bigint NOT NULL DEFAULT 0 CHECK ("remainingPlayTime" >= 0),
	"foldingUserHash" character varying(200) CHECK (LENGTH("foldingUserHash") >= 5)
);
CREATE INDEX "Player_nameIndex" ON "game"."Player" ("name");
CREATE INDEX "Player_shipIdIndex" ON "game"."Player" ("shipId");
CREATE INDEX "Player_actionQueueId" ON "game"."Player" ("actionQueueId");
CREATE UNIQUE INDEX "Player_loginToken" ON "game"."Player" ("loginToken");
CREATE INDEX "Player_emailAddress" ON "game"."Player" ("emailAddress");

-- note: a player has "unlocked" a skill (made available for learning) when a row for that skill is in the table
CREATE TABLE "game"."PlayerSkill" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"playerId" bigint NOT NULL REFERENCES "game"."Player" ON DELETE CASCADE,
	"name" character varying(2000) NOT NULL,
	"learningPoints" integer NOT NULL DEFAULT 0 CHECK ("learningPoints" >= 0),
	"learningOrderIndex" integer, -- entries with learningOrderIndex null aren't part of the queue
	"learningFinished" boolean NOT NULL DEFAULT false
);
CREATE UNIQUE INDEX "PlayerSkill_uniqueIndex" ON "game"."PlayerSkill" ("playerId", "name");
CREATE INDEX "PlayerSkill_mainIndex" ON "game"."PlayerSkill" ("playerId", "learningFinished", "name");

CREATE TABLE "game"."CachedPlayerAttribute" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"playerId" bigint NOT NULL REFERENCES "game"."Player" ON DELETE CASCADE,
	"key" "game"."PlayerAttributeKey" NOT NULL,
	"value" text NOT NULL
);
CREATE UNIQUE INDEX "CachedPlayerAttribute_playerIdKeyIndex" ON "game"."CachedPlayerAttribute" ("playerId", "key");


-----------------------------------------------------------------------------------------------------------------------
-- trading
-----------------------------------------------------------------------------------------------------------------------

CREATE TYPE "game"."MarketOrderType" AS ENUM ('BUY', 'SELL');

CREATE TABLE "game"."MarketOrder" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"principalPlayerId" bigint NOT NULL REFERENCES "game"."Player" ON DELETE CASCADE,
	-- Location-less orders cannot be created by players for now; they can only be predefined by the game. The reason
	-- is that for SELL orders, besides matching BUY orders, the order also indicates items in escrow at that location.
	-- Allowing players to create them would allow them to teleport items. BUY orders would be okay, but a totally
	-- location-unrestricted player-owned BUY order is useless to players since space is big.
	"locationSpaceObjectBaseDataId" bigint REFERENCES "game"."SpaceObjectBaseData" ON DELETE CASCADE,
	"type" "game"."MarketOrderType" NOT NULL,
	"itemType" character varying(2000) NOT NULL,
	-- The quantity can be 0 temporary when awating deletion. Only the game's global market orders can have a NULL
	-- quantity (meaning the order is unlimited) since for player orders it would indicate an unlimited amount of money
	-- or an unlimited number of items in escrow.
	"quantity" int NULL CHECK ("quantity" IS NULL or "quantity" >= 0),
	"unitPrice" bigint NOT NULL CHECK ("unitPrice" >= 0)
);
CREATE INDEX "MarketOrder_principalPlayerId" ON "game"."MarketOrder" ("principalPlayerId");
CREATE INDEX "MarketOrder_locationSpaceObjectBaseDataId" ON "game"."MarketOrder" ("locationSpaceObjectBaseDataId");

-----------------------------------------------------------------------------------------------------------------------
-- peripherals
-----------------------------------------------------------------------------------------------------------------------

CREATE TABLE "game"."GameFeedback" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"timestamp" timestamp with time zone NOT NULL DEFAUlT NOW(),
	"sessionId" character varying(1000),
	"playerId" bigint REFERENCES "game"."Player" ON DELETE SET NULL,
	"context" jsonb NOT NULL, -- but may be JSON null to indicate no / unknown context
	"text" text NOT NULL
);

-----------------------------------------------------------------------------------------------------------------------
-- more foreign keys which could not be defined above due to table creation order, or because they are circular
-----------------------------------------------------------------------------------------------------------------------

ALTER TABLE "game"."InventorySlot" ADD FOREIGN KEY ("playerId") REFERENCES "game"."Player";
