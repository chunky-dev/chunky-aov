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

public class DepthTracer implements RayTracer {
    public static double NORMALIZATION_FACTOR = 100.0;
    public static boolean INFINITE_SKY_DISTANCE = true;

    private DepthTracer() {}

    public static void register() {
        Chunky.addRenderer(new PathTracingRenderer(
                "AovDepthTracer",
                "Depth Tracer",
                "Depth pass.",
                new DepthTracer()
        ));
    }

    /**
     * Ray tracer code from the
     * <a href="https://github.com/llbit/Chunky-DepthPlugin/blob/960328869f102c0e93849511b9859ffd0b1f7f31/src/main/java/se/llbit/chunky/plugin/DepthTracer.java">
     *     Depth Tracer demo plugin
     * </a>.
     */
    @Override
    public void trace(Scene scene, WorkerState state) {
        Ray ray = state.ray;
        double distance = INFINITE_SKY_DISTANCE ? Double.MAX_VALUE : 0.0;

        if (PreviewRayTracer.nextIntersection(scene, ray)) {
            distance = ray.distance;
        }
        distance /= NORMALIZATION_FACTOR;
        ray.color.set(distance, distance, distance, 1);
    }
}
