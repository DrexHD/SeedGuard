[![Discord](https://img.shields.io/discord/904419828192927885.svg?logo=discord)](https://discord.gg/HeZayd6SxF)

# SeedGuard

Protects your world seed by altering feature and structure placement, preventing seed cracking.

## How does it work?

When generating worlds Minecraft builds the basic surface shape, then applies biomes and finally adds surface
decorations, which consist of features (trees, sugar cane, grass, ores...) and structures (villages,
pillager outposts...). 

Each feature / structure has its own seed, which is hardcoded in vanilla (to ensure a world with
the same seed always looks the same). SeedGuard replaces these seeds with randomly generated seeds, making it impossible
to derive the world seed from [features](https://minecraft.fandom.com/wiki/Feature) or [structures](https://minecraft.fandom.com/wiki/Structure).

## Side-by-side view

SeedGuard changes the placement of features / structure in Minecraft, below is a simple demo of a world with and without
the mod installed.

https://github.com/DrexHD/SeedGuard/assets/22878739/984e6e8d-a32a-46f3-adf4-ca9f0579bc02