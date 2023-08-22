package com.carsoncoder.gpws;

import java.util.HashMap;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class gpwsSounds {

    public static enum SOUNDS {
        RETARD, PULL_UP, BANK_ANGLE, Y2500, Y500, Y400, Y300, Y200, Y100, Y70, Y60, Y50, Y40, Y30, Y20
    }

    private static final Identifier RETARD_ID = new Identifier("gpws-elytra:retard");
    private static SoundEvent RETARD = SoundEvent.of(RETARD_ID);

    private static final Identifier BANK_ANGLE_ID = new Identifier("gpws-elytra:bank-angle");
    private static SoundEvent BANK_ANGLE = SoundEvent.of(BANK_ANGLE_ID);

    private static final Identifier PULL_UP_ID = new Identifier("gpws-elytra:pull-up");
    private static SoundEvent PULL_UP = SoundEvent.of(PULL_UP_ID);

    private SOUNDS[] Sounds = {SOUNDS.Y2500, SOUNDS.Y500, SOUNDS.Y400, SOUNDS.Y300, SOUNDS.Y200, SOUNDS.Y100, SOUNDS.Y70, SOUNDS.Y60, SOUNDS.Y50, SOUNDS.Y40, SOUNDS.Y30, SOUNDS.Y20}; 

    private HashMap<SOUNDS, SoundEvent> SoundsDict = new HashMap<SOUNDS, SoundEvent>();

    void init() {
        Registry.register(Registries.SOUND_EVENT, RETARD_ID, RETARD);
        Registry.register(Registries.SOUND_EVENT, BANK_ANGLE_ID, BANK_ANGLE);
        Registry.register(Registries.SOUND_EVENT, PULL_UP_ID, PULL_UP);

        SoundsDict.put(SOUNDS.BANK_ANGLE, BANK_ANGLE);
        SoundsDict.put(SOUNDS.PULL_UP, PULL_UP);
        SoundsDict.put(SOUNDS.RETARD, RETARD);

        for (int i=0; i<12; i++) {
            SOUNDS currentSound = Sounds[i];

            gpwsElytraClient.LOGGER.info("Loading Sound: {}", currentSound.name());

            Identifier id = new Identifier("gpws-elytra:" + currentSound.name().substring(1));
            SoundEvent event = SoundEvent.of(id);
            SoundsDict.put(currentSound, event);
            Registry.register(Registries.SOUND_EVENT, id, event);
        }
    }

    public SoundEvent GetSound(SOUNDS sound) {
        return SoundsDict.get(sound);
    }
}
