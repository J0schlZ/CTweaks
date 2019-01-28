# CTweaks

Some tweaks for CT..

### Aktuelle Funktionen:
..* Bei Tod des Enderdrachen innerhalb einer End-Welt, wird �ber dem Brunnen ein neues Drachenei gespawned.
..* Monitoring von div. Events `/ctweaks info` & `/ctweaks monitor`
..* Auflisten aller NPC (Server, Welt, Radius) `/ctweaks listmobs <radius|all>` (Ohne Parameter = Aktuelle Welt)
..* Blockierung von Redstone (BlockRedstoneEvent) `/ctweaks <enable|disable> redstone`
..* Blockierung des Mob-Spawning (CreatureSpawnEvent) `/ctweaks <enable|disable> spawning`
..* Blockierung von Wasser/Lava -Fluss (BlockFromToEvent) `/ctweaks <enable|disable> liquid`
..* Blockierung von CommandBlocks (BlockCommandSender in ServerCommandEvent) `/ctweaks <enable|disable> commandblock`

### Spawn Limitierungen (Optimierungen):
..* Bei jedem Spawn-Versuch eines NPC (Monster/Tier/Villager) wird die Anzahl der NPC des jeweiligen Typ innerhalb eines Radius von 64 Bl�cken ermittelt.
Finden sich mehr als 50 NPC eines Typ bei der Z�hlung, wird der Spawn-Versuch abgebrochen.
Au�erdem werden von den gefundenen NPC (ausgenommen jener, welche einen Nametag besitzen) so viele Einheiten despawned, bis die Gesamtanzahl 50 betr�gt.

..* Bei jedem Spawn-Versuch (innerhalb von Ozean-Biomen) wird, um massivem Fisch-Spawning entgegen zu wirken, die Anzahl der gefundenen Fische eines Typs
innerhalb eines Radius von 192 Bl�cken ermittelt. Finden sich mehr als 30 Fische eines Typ bei der Z�hlung, wird der Spawn-Versuch abgebrochen.
Au�erdem werden von den gefundenen Fischen (ausgenommen jener, welche einen Nametag besitzen) so viele Einheiten despawned, bis die Gesamtanzahl 30 betr�gt.

..* Bei jedem Spawn-Versuch eines Guardian, Endermen oder Zombie-Pigmen wird die Anzahl der Mobs nach dem oben beschriebenen Verfahren Limitiert,
sollten sich bei der Z�hlung mehr als 40 Monster eines Typs in einem Radius von 128 Bl�cken finden.

### Benachrichtigungen bei blockiertem Spawn-Versuch:
..* Bei einem blockiertem Versuch ein Tier zu z�chten (SpawnReason: BREEDING) werden (alle) Spieler in einem Umkreis von 32 Bl�cken benachrichtigt.
..* Bei einem blockiertem Versuch ein K�ken durch den wurf eines Ei zu spawnen (SpawnReason: EGG) werden (alle) Spieler in einem Umkreis von 32 Bl�cken benachrichtigt.
..* Bei einem blockiertem Versuch einen Golem oder Wither zu spawnen (SpawnReason: BUILD_IRONGOLEM, BUILD_SNOWMAN, BUILD_WITHER) werden (alle) Spieler in einem Umkreis von 8 Bl�cken benachrichtigt.
..* Bei einem blockiertem Versuch eines Mob-Spawners, ein Mob zu spawnen (SpawnReason: SPAWNER), werden (alle) Spieler in einem Umkreis von 16 Bl�cken benachrichtigt.
..* Bei einem blockiertem Versuch eine Kreatur mit einem Spawn-Ei zu spawnen (SpawnReason: SPAWNER_EGG), werden (alle) Spieler in einem Umkreis von 8 Bl�cken benachrichtigt.