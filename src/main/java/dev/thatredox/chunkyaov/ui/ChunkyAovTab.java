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

package dev.thatredox.chunkyaov.ui;

import dev.thatredox.chunkyaov.raytracer.NormalTracer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import se.llbit.chunky.renderer.scene.Scene;
import se.llbit.chunky.ui.render.RenderControlsTab;
import se.llbit.json.Json;

public class ChunkyAovTab implements RenderControlsTab {
    private Scene scene = null;
    private final VBox box = new VBox(10.0);
    private final SimpleBooleanProperty nmPositive = new SimpleBooleanProperty(NormalTracer.MAP_POSITIVE);
    private final SimpleBooleanProperty nmWaterDisplacement = new SimpleBooleanProperty(NormalTracer.WATER_DISPLACEMENT);

    public ChunkyAovTab() {
        nmPositive.addListener((observable, oldValue, newValue) -> {
            NormalTracer.MAP_POSITIVE = newValue;
            if (scene != null) {
                scene.setAdditionalData("aov_normal_positive", Json.of(newValue));
            }
        });
        nmWaterDisplacement.addListener(((observable, oldValue, newValue) -> {
            NormalTracer.WATER_DISPLACEMENT = newValue;
            if (scene != null) {
                scene.setAdditionalData("aov_normal_water_displacement", Json.of(newValue));
            }
        }));

        box.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));

        CheckBox nmPositiveBox = new CheckBox("Normal Map Positive");
        nmPositiveBox.setTooltip(new Tooltip("Map normal map values to range between 0 and 1."));
        nmPositiveBox.selectedProperty().bindBidirectional(nmPositive);
        box.getChildren().add(nmPositiveBox);

        CheckBox nmWaterDisplacementBox = new CheckBox("Normal Map Water Displacement");
        nmWaterDisplacementBox.setTooltip(new Tooltip("Enable water displacement in normal map."));
        nmWaterDisplacementBox.selectedProperty().bindBidirectional(nmWaterDisplacement);
        box.getChildren().add(nmWaterDisplacementBox);
    }

    @Override
    public void update(Scene scene) {
        this.scene = scene;
        nmPositive.set(scene.getAdditionalData("aov_normal_positive").boolValue(nmPositive.get()));
        nmWaterDisplacement.set(scene.getAdditionalData("aov_normal_water_displacement").boolValue(nmWaterDisplacement.get()));
    }

    @Override
    public String getTabTitle() {
        return "AOV Plugin";
    }

    @Override
    public Node getTabContent() {
        return box;
    }
}
