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
import se.llbit.chunky.model.WaterModel;
import se.llbit.chunky.renderer.PathTracingRenderer;
import se.llbit.chunky.renderer.WorkerState;
import se.llbit.chunky.renderer.scene.PreviewRayTracer;
import se.llbit.chunky.renderer.scene.RayTracer;
import se.llbit.chunky.renderer.scene.Scene;
import se.llbit.math.Ray;
import se.llbit.math.Vector3;

public class NormalTracer implements RayTracer {
    public static boolean MAP_POSITIVE = false;
    public static boolean WATER_DISPLACEMENT = true;

    private NormalTracer() {}

    public static void register() {
        Chunky.addRenderer(new PathTracingRenderer(
                "AovNormalTracer",
                "Normal",
                "Surface normal pass.",
                new NormalTracer()
        ));
    }

    /**
     * Ray tracer code from the
     * <a href="https://github.com/chunky-dev/chunky-denoiser/blob/4d4db51a4a81f77c24cc1def717686c9df67a95d/src/main/java/de/lemaik/chunky/denoiser/NormalRenderer.java">
     *     Denoiser Plugin
     * </a>.
     */
    @Override
    public void trace(Scene scene, WorkerState state) {
        Ray ray = state.ray;
        if (PreviewRayTracer.nextIntersection(scene, ray)) {
            if (WATER_DISPLACEMENT && !scene.stillWaterEnabled()
                    && ray.getCurrentMaterial().isWater()) {
                WaterModel.doWaterDisplacement(ray);
            }

            if (MAP_POSITIVE) {
                Vector3 normal = new Vector3(ray.getNormal());
                normal.normalize();
                ray.color.set((normal.x + 1) / 2, (normal.y + 1) / 2, (normal.z + 1) / 2, 1);
            } else {
                Vector3 normal = ray.getNormal();
                ray.color.set(normal.x, normal.y, normal.z, 1);
            }
        }
    }
}
