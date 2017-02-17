
SpaceObject
- id: bigint
- type (TODO field type?)
- name (varchar 100)
- x, y (bigint)
TODO:
- asteroid yield info and yield capacity
- player id (needed? player has shipId)
- inventory id (for player ship and space station)

Player
- id: bigint
- name (varchar 100)
- ship ID: bigint
- action queue (TODO ???)
- action execution (TODO ???)
TODO
- skills

Inventory
- id: bigint

InventorySlot
- id: bigint
- inventoryId: bigint
- item type (wie?)
- quantity: integer



-----



CREATE SCHEMA "game";

CREATE TYPE "game"."TestEnum" AS ENUM ('EINS', 'ZWEI');

CREATE TABLE "game"."MyTable" (
	"id" bigserial NOT NULL PRIMARY KEY,
	"foo" INTEGER NOT NULL,
	"bar" character varying(2000) NOT NULL,
	"baz" INTEGER NOT NULL,
	"testenum" "game"."TestEnum" NOT NULL
);
CREATE INDEX "MyTable_fooIndex" ON "game"."MyTable" ("foo");
CREATE INDEX "MyTable_fooBazIndex" ON "game"."MyTable" (("foo" != "baz"));

