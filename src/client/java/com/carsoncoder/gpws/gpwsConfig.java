package com.carsoncoder.gpws;

import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import net.minecraft.text.Text;

public class gpwsConfig {
    int PullUpRange = 50;
    int BankAngleAngle = -50;
    int Volume = 100;
    int SoundDelay = 30;

    boolean test = false;

    public gpwsConfig() {
        YetAnotherConfigLib thing = YetAnotherConfigLib.createBuilder()
        .title(Text.literal("Used for narration. Could be used to render a title in the future."))
        .category(ConfigCategory.createBuilder()
                .name(Text.literal("Name of the category"))
                .tooltip(Text.literal("This text will appear as a tooltip when you hover or focus the button with Tab. There is no need to add \n to wrap as YACL will do it for you."))
                .group(OptionGroup.createBuilder()
                        .name(Text.literal("Name of the group"))
                        .tooltip(Text.literal("This text will appear when you hover over the name or focus on the collapse button with Tab."))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.literal("Boolean Option"))
                                .tooltip(Text.literal("This text will appear as a tooltip when you hover over the option."))
                                .binding(true, () -> this.test, newVal -> this.test = newVal)
                                .controller(TickBoxController::new)
                                .build())
                        .build())
                .build())
        .build();
    }
}  