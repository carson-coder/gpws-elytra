package com.carsoncoder.gpws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class gpwsConfig {
    int PullUpRange = 50;
    int BankAngleAngle = -50;
    int Volume = 100;
    int SoundDelay = 30;
    int MinSoundDelay = 10;

    public static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("carsoncoder-gpws.json");
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static Screen create(Screen screen) {
        gpwsElytraClient.LOGGER.info("Building Config");
        YetAnotherConfigLib thing = YetAnotherConfigLib.createBuilder()
        .title(Text.literal("GPWS Elytra Config"))
        .category(ConfigCategory.createBuilder()
            .name(Text.literal("Settings"))
            .tooltip(Text.literal("The main settings for elytra GPWS"))
            .group(OptionGroup.createBuilder()
                    .name(Text.literal("Sounds"))
                    .option(Option.createBuilder(int.class)
                            .name(Text.literal("Volume"))
                            .description(OptionDescription.createBuilder()
                                .text(Text.literal("The volume of sounds"))
                                .build())
                            .binding(100, () -> {return gpwsElytraClient.CONFIG.Volume;}, newVal -> gpwsElytraClient.CONFIG.Volume = newVal)
                            .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                .range(0, 100)
                                .step(1)
                                .valueFormatter(val -> Text.literal(val + "%"))    
                            )
                            .build())
                    .option(Option.createBuilder(int.class)
                            .name(Text.literal("Delay"))
                            .description(OptionDescription.createBuilder()
                                .text(Text.literal("How long do we have to wait before playing the same sounds"))
                                .build())
                            .binding(30, () -> {return gpwsElytraClient.CONFIG.SoundDelay;}, newVal -> gpwsElytraClient.CONFIG.SoundDelay = newVal)
                            .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                .range(0, 100)
                                .step(1)
                                .valueFormatter(val -> Text.literal(val + ""))    
                            )
                            .build()) 
                    .option(Option.createBuilder(int.class)
                            .name(Text.literal("Minimum Delay"))
                            .description(OptionDescription.createBuilder()
                                .text(Text.literal("How long do we have to wait to play another sound (same or different)"))
                                .build())
                            .binding(30, () -> {return gpwsElytraClient.CONFIG.MinSoundDelay;}, newVal -> gpwsElytraClient.CONFIG.MinSoundDelay = newVal)
                            .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                .range(0, 100)
                                .step(1)
                                .valueFormatter(val -> Text.literal(val + ""))    
                            )
                            .build())
                    .build())
            .build())
            .save(gpwsElytraClient.CONFIG::save)
        .build();

        return thing.generateScreen(screen);

        //MinecraftClient.getInstance().setScreen(thing.generateScreen(MinecraftClient.getInstance().currentScreen));
    }

    public void init() {
        
    }

    public static gpwsConfig load() {
        gpwsConfig config = null;
        File file = CONFIG_FILE.toFile();

        if (file.exists()) {
            // An existing config is present, we should use its values
            try (BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            )) {
                // Parses the config file and puts the values into config object
                config = GSON.fromJson(fileReader, gpwsConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("Problem occurred when trying to load config: ", e);
            }
        }
        // gson.fromJson() can return null if file is empty
        if (config == null) {
            config = new gpwsConfig();
        }

        // Saves the file in order to write new fields if they were added
        config.save();
        return config;
    }

    public void save() {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(CONFIG_FILE.toFile()), StandardCharsets.UTF_8)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}  