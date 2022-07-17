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

import se.llbit.chunky.main.Chunky;
import se.llbit.chunky.renderer.PathTracingRenderer;
import se.llbit.chunky.renderer.WorkerState;
import se.llbit.chunky.renderer.scene.PreviewRayTracer;
import se.llbit.chunky.renderer.scene.RayTracer;
import se.llbit.chunky.renderer.scene.Scene;
import se.llbit.math.Ray;

import java.util.Random;

public class AmbientOcclusionTracer implements RayTracer {
    private AmbientOcclusionTracer() {}

    public static void register() {
        Chunky.addRenderer(new PathTracingRenderer(
                "AovAmbientOcclusion",
                "Ambient Occlusion",
                "Ambient occlusion pass.",
                new AmbientOcclusionTracer()
        ));
    }

    /**
     * Ray tracer code from the
     * <a href="https://github.com/llbit/Chunky-AOPlugin/blob/adf994d858e7bd00fdc581af28b0573d7f3cd9d4/src/main/java/se/llbit/chunky/plugin/AmbientOcclusionTracer.java">
     *     Ambient Occlusion demo plugin
     * </a>.
     */
    @Override
    public void trace(Scene scene, WorkerState state) {
        Ray ray = state.ray;
        Random random = state.random;
        while (true) {
            if (!PreviewRayTracer.nextIntersection(scene, ray)) {
                // The ray exited the scene.
                ray.color.set(1, 1, 1, 1);
                break;
            } else if (!scene.kill(ray.depth + 1, random)) {
                ray.diffuseReflection(ray, random);
            } else {
                ray.color.set(0, 0, 0, 1);
                break;
            }
        }
    }
}
