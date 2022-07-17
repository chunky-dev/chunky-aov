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
package dev.thatredox.chunkyaov.raytracer;

import se.llbit.chunky.block.Air;
import se.llbit.chunky.block.Water;
import se.llbit.chunky.main.Chunky;
import se.llbit.chunky.renderer.PathTracingRenderer;
import se.llbit.chunky.renderer.WorkerState;
import se.llbit.chunky.renderer.scene.PreviewRayTracer;
import se.llbit.chunky.renderer.scene.RayTracer;
import se.llbit.chunky.renderer.scene.Scene;
import se.llbit.math.Ray;

public class AlbedoTracer implements RayTracer {
    private AlbedoTracer() {}

    public static void register() {
        Chunky.addRenderer(new PathTracingRenderer(
                "AovAlbedoTracer",
                "Albedo",
                "Surface albedo pass.",
                new AlbedoTracer()
        ));
    }

    /**
     * Ray tracer code from the
     * <a href="https://github.com/chunky-dev/chunky-denoiser/blob/4d4db51a4a81f77c24cc1def717686c9df67a95d/src/main/java/de/lemaik/chunky/denoiser/AlbedoRenderer.java">
     *     Denoiser Plugin
     * </a>.
     */
    @Override
    public void trace(Scene scene, WorkerState state) {
        Ray ray = state.ray;
        if (scene.isInWater(ray)) {
            ray.setCurrentMaterial(Water.INSTANCE, 0);
        } else {
            ray.setCurrentMaterial(Air.INSTANCE, 0);
        }

        while (true) {
            if (!PreviewRayTracer.nextIntersection(scene, ray)) {
                if (ray.getPrevMaterial().isWater()) {
                    // set water color to white
                    ray.color.set(1, 1, 1, 1);
                } else if (ray.depth == 0) {
                    // direct sky hit
                    if (!scene.transparentSky()) {
                        scene.sky().getSkyColorInterpolated(ray);
                    }
                }
                // ignore indirect sky hits
                break;
            }

            if (ray.getCurrentMaterial() != Air.INSTANCE && ray.color.w > 0.0D) {
                break;
            }

            ray.o.scaleAdd(1.0E-4D, ray.d);
        }
    }
}
