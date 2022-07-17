/*
 * Copyright (c) 2022 Chunky contributors
 *
 * This file is part of Chunky.
 *
 * Chunky is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Chunky is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Chunky. If not, see <http://www.gnu.org/licenses/>.
 */
package dev.thatredox.chunkyaov;

import dev.thatredox.chunkyaov.raytracer.AlbedoTracer;
import dev.thatredox.chunkyaov.raytracer.AmbientOcclusionTracer;
import dev.thatredox.chunkyaov.raytracer.DepthTracer;
import dev.thatredox.chunkyaov.raytracer.NormalTracer;
import se.llbit.chunky.Plugin;
import se.llbit.chunky.main.Chunky;
import se.llbit.chunky.main.ChunkyOptions;
import se.llbit.chunky.ui.ChunkyFx;

public class ChunkyAOV implements Plugin {
    @Override
    public void attach(Chunky chunky) {
        AlbedoTracer.register();
        NormalTracer.register();
        AmbientOcclusionTracer.register();
        DepthTracer.register();
    }

    public static void main(String[] args) {
        // Start Chunky normally with this plugin attached.
        Chunky.loadDefaultTextures();
        Chunky chunky = new Chunky(ChunkyOptions.getDefaults());
        new ChunkyAOV().attach(chunky);
        ChunkyFx.startChunkyUI(chunky);
    }
}
