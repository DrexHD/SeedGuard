package me.drex.seedguard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Map;

public class SeedManager {

    private static final Codec<Map<Holder<StructureSet>, Integer>> STRUCTURE_SEEDS_MAP_CODEC = Codec.unboundedMap(StructureSet.CODEC, Codec.INT);
    private static final Codec<Map<Holder<ConfiguredFeature<?, ?>>, Long>> FEATURE_SEEDS_MAP_CODEC = Codec.unboundedMap(ConfiguredFeature.CODEC, Codec.LONG);
    private static final String STRUCTURE_SEEDS_FILE = "structure-seeds.json";
    private static final String FEATURE_SEEDS_FILE = "feature-seeds.json";
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    private static final SecureRandom random = new SecureRandom();
    private static final Reference2IntMap<Holder<StructureSet>> structureSeeds = new Reference2IntOpenHashMap<>();
    private static final Reference2LongMap<Holder<ConfiguredFeature<?, ?>>> featureSeeds = new Reference2LongOpenHashMap<>();
    public static MinecraftServer MINECRAFT_SERVER = null;
    public static final Logger LOGGER = LoggerFactory.getLogger("SeedManager");

    public static void load(MinecraftServer server) {
        MINECRAFT_SERVER = server;
        structureSeeds.putAll(load(server, STRUCTURE_SEEDS_FILE, STRUCTURE_SEEDS_MAP_CODEC));
        featureSeeds.putAll(load(server, FEATURE_SEEDS_FILE, FEATURE_SEEDS_MAP_CODEC));

        server.registryAccess().lookupOrThrow(Registries.STRUCTURE_SET).listElements().forEach(holder -> {
            structureSeeds.computeIfAbsent(holder, ignored -> random.nextInt());
        });
        server.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).listElements().forEach(holder -> {
            featureSeeds.computeIfAbsent(holder, ignored -> random.nextLong());
        });
        save(server);
    }

    private static <T, U> Map<T, U> load(MinecraftServer server, String filePath, Codec<Map<T, U>> codec) {
        Path savePath = server.getWorldPath(LevelResource.ROOT).resolve(filePath);
        if (Files.isRegularFile(savePath)) {
            try (JsonReader jsonReader = new JsonReader(Files.newBufferedReader(savePath, StandardCharsets.UTF_8))) {
                jsonReader.setLenient(false);
                JsonElement jsonElement = JsonParser.parseReader(jsonReader);
                DataResult<Map<T, U>> result = codec.parse(RegistryOps.create(JsonOps.INSTANCE, server.registryAccess()), jsonElement);
                return result.resultOrPartial(LOGGER::error).orElseThrow();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Collections.emptyMap();
    }

    public static int getStructureSeed(Holder<StructureSet> holder) {
        return structureSeeds.getInt(holder);
    }

    public static long getFeatureSeed(Holder<ConfiguredFeature<?, ?>> holder) {
        return featureSeeds.getLong(holder);
    }

    public static void save(MinecraftServer server) {
        save(server, STRUCTURE_SEEDS_FILE, STRUCTURE_SEEDS_MAP_CODEC, structureSeeds);
        save(server, FEATURE_SEEDS_FILE, FEATURE_SEEDS_MAP_CODEC, featureSeeds);
    }

    private static <T, U> void save(MinecraftServer server, String filePath, Codec<Map<T, U>> codec, Map<T, U> data) {
        Path savePath = server.getWorldPath(LevelResource.ROOT).resolve(filePath);
        LOGGER.warn("the savepath");
        LOGGER.warn(savePath.toString());
//        HolderLookup.RegistryLookup<StructureSet> holderLookup = server.registryAccess().lookupOrThrow(Registries.STRUCTURE_SET);
//        holderLookup.listElements().forEach(structureSetReference -> LOGGER.error(structureSetReference.key().location().toString()));
        LOGGER.warn(data.toString());
        LOGGER.warn(codec.toString());

        DataResult<JsonElement> result = codec.encodeStart(RegistryOps.create(JsonOps.INSTANCE, server.registryAccess()), data);
        JsonElement jsonElement = result.resultOrPartial(LOGGER::error).orElseThrow();
        try (BufferedWriter writer = Files.newBufferedWriter(savePath, StandardCharsets.UTF_8)) {
            GSON.toJson(jsonElement, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save \"" + savePath + "\"", e);
        }
    }

}
