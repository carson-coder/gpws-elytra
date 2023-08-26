package com.carsoncoder.gpws;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class gpwsConfig {
    int PullUpRange = 50;
    int BankAngleAngle = -50;
    float Volume = 1.0f;
    int SoundDelay = 30;

    boolean test = false;

    public static Screen create(Screen screen) {
        gpwsElytraClient.LOGGER.info("Building Config");
        YetAnotherConfigLib thing = YetAnotherConfigLib.createBuilder()
        .title(Text.literal("Used for narration. Could be used to render a title in the future."))
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
                                .binding(50, () -> {return Math.round(gpwsElytraClient.CONFIG.Volume*100);}, newVal -> gpwsElytraClient.CONFIG.Volume = newVal/100)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                    .range(0, 100)
                                    .step(1)
                                    .valueFormatter(val -> Text.literal(val + "%"))    
                                )
                                .build())
                        .build())
                .build())
        .build();

        return thing.generateScreen(screen);

        //MinecraftClient.getInstance().setScreen(thing.generateScreen(MinecraftClient.getInstance().currentScreen));
    }
}  