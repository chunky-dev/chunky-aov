package dev.thatredox.chunkyaov;

import se.llbit.chunky.Plugin;
import se.llbit.chunky.main.Chunky;
import se.llbit.chunky.main.ChunkyOptions;
import se.llbit.chunky.ui.ChunkyFx;

public class ChunkyAOV implements Plugin {
    @Override
    public void attach(Chunky chunky) {
        System.out.println("Hello");
    }

    public static void main(String[] args) {
        // Start Chunky normally with this plugin attached.
        Chunky.loadDefaultTextures();
        Chunky chunky = new Chunky(ChunkyOptions.getDefaults());
        new ChunkyAOV().attach(chunky);
        ChunkyFx.startChunkyUI(chunky);
    }
}
