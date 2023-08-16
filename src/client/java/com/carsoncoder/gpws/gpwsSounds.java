package com.carsoncoder.gpws;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class gpwsSounds {

    public enum Sounds {
        RETARD, PULL_UP, BANK_ANGLE, Y2500, Y500, Y400, Y300, Y200, Y100, Y70, Y60, Y50, Y40, Y30, Y20
    }

    public static final Identifier RETARD_ID = new Identifier("gpws-elytra:retard");
    public static SoundEvent RETARD = SoundEvent.of(RETARD_ID);

    public static final Identifier BANK_ANGLE_ID = new Identifier("gpws-elytra:retard");
    public static SoundEvent BANK_ANGLE = SoundEvent.of(BANK_ANGLE_ID);

    public static final Identifier PULL_UP_ID = new Identifier("gpws-elytra:retard");
    public static SoundEvent PULL_UP = SoundEvent.of(PULL_UP_ID);

    void init() {
        Registry.register(Registries.SOUND_EVENT, RETARD_ID, RETARD);
        Registry.register(Registries.SOUND_EVENT, BANK_ANGLE_ID, BANK_ANGLE);
        Registry.register(Registries.SOUND_EVENT, PULL_UP_ID, PULL_UP);
    }
}
