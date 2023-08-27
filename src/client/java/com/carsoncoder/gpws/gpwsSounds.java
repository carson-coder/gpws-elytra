package com.carsoncoder.gpws;

import java.util.HashMap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

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
    public HashMap<String, SOUNDS> YSounds = new HashMap<String, SOUNDS>();

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

            YSounds.put(currentSound.name().substring(1), currentSound);
        }
    }

    public void PlaySound(SoundEvent sound) {
        MinecraftClient.getInstance().getSoundManager().play(new EntityTrackingSoundInstance(
            sound,
            SoundCategory.VOICE,
            gpwsElytraClient.CONFIG.Volume,
            1f,
            MinecraftClient.getInstance().player,
            Random.create().nextLong()));
        // if (MinecraftClient.getInstance().player == null) {return;}
        //     MinecraftClient.getInstance().world.playSound(
        //     MinecraftClient.getInstance().player, // Player - if non-null, will play sound for every nearby player *except* the specified player
        //     MinecraftClient.getInstance().player.getBlockPos(), // The position of where the sound will come from
        //     SoundEvents.BLOCK_FENCE_GATE_OPEN, // The sound that will play
        //     SoundCategory.BLOCKS, // This determines which of the volume sliders affect this sound
        //     1f, //Volume multiplier, 1 is normal, 0.5 is half volume, etc
        //     1f // Pitch multiplier, 1 is normal, 0.5 is half pitch, etc
		// );
    }

    public void PlaySound(gpwsSounds.SOUNDS sound) {
        PlaySound(GetSound(sound));
    }

    public SoundEvent GetSound(SOUNDS sound) {
        return SoundsDict.get(sound);
    }
}
