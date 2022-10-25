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

import dev.thatredox.chunkyaov.raytracer.DepthTracer;
import dev.thatredox.chunkyaov.raytracer.NormalTracer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import se.llbit.chunky.renderer.scene.Scene;
import se.llbit.chunky.ui.DoubleAdjuster;
import se.llbit.chunky.ui.SliderAdjuster;
import se.llbit.chunky.ui.render.RenderControlsTab;
import se.llbit.json.Json;

public class ChunkyAovTab implements RenderControlsTab {
    private Scene scene = null;
    private final VBox box = new VBox(10.0);
    private final SimpleBooleanProperty nmPositive = new SimpleBooleanProperty(NormalTracer.MAP_POSITIVE);
    private final SimpleBooleanProperty nmWaterDisplacement = new SimpleBooleanProperty(NormalTracer.WATER_DISPLACEMENT);
    private final SimpleDoubleProperty depthNormFactor = new SimpleDoubleProperty(DepthTracer.NORMALIZATION_FACTOR);
    private final SimpleBooleanProperty infiniteSkyDepth = new SimpleBooleanProperty(DepthTracer.INFINITE_SKY_DISTANCE);

    public ChunkyAovTab() {
        nmPositive.addListener((observable, oldValue, newValue) -> {
            NormalTracer.MAP_POSITIVE = newValue;
            if (scene != null) {
                scene.setAdditionalData("aov_normal_positive", Json.of(newValue));
                scene.refresh();
            }
        });
        nmWaterDisplacement.addListener((observable, oldValue, newValue) -> {
            NormalTracer.WATER_DISPLACEMENT = newValue;
            if (scene != null) {
                scene.setAdditionalData("aov_normal_water_displacement", Json.of(newValue));
                scene.refresh();
            }
        });
        depthNormFactor.addListener((observable, oldValue, newValue) ->  {
            double value = newValue.doubleValue();
            DepthTracer.NORMALIZATION_FACTOR = value;
            if (scene != null) {
                scene.setAdditionalData("aov_depth_normalization", Json.of(value));
                scene.refresh();
            }
        });
        infiniteSkyDepth.addListener((observable, oldValue, newValue) -> {
            DepthTracer.INFINITE_SKY_DISTANCE = newValue;
            if (scene != null) {
                scene.setAdditionalData("aov_infinite_sky_depth", Json.of(newValue));
                scene.refresh();
            }
        });

        box.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));

        CheckBox nmPositiveBox = new CheckBox("Normal Map Positive");
        nmPositiveBox.setTooltip(new Tooltip("Map normal map values to range between 0 and 1."));
        nmPositiveBox.selectedProperty().bindBidirectional(nmPositive);
        box.getChildren().add(nmPositiveBox);

        CheckBox nmWaterDisplacementBox = new CheckBox("Normal Map Water Displacement");
        nmWaterDisplacementBox.setTooltip(new Tooltip("Enable water displacement in normal map."));
        nmWaterDisplacementBox.selectedProperty().bindBidirectional(nmWaterDisplacement);
        box.getChildren().add(nmWaterDisplacementBox);

        box.getChildren().add(new Separator());

        DoubleAdjuster depthNormAdjuster = new DoubleAdjuster();
        depthNormAdjuster.setName("Depth Normalization");
        depthNormAdjuster.setTooltip("Distance normalization factor for depth map.");
        depthNormAdjuster.valueProperty().bindBidirectional(depthNormFactor);
        depthNormAdjuster.setRange(1.0, 1000.0);
        depthNormAdjuster.clampMin();
        box.getChildren().add(depthNormAdjuster);

        CheckBox enableInfiniteSkyDepth = new CheckBox("Infinite Sky Depth");
        enableInfiniteSkyDepth.selectedProperty().bindBidirectional(infiniteSkyDepth);
        box.getChildren().add(enableInfiniteSkyDepth);

        box.getChildren().add(new Separator());

        Text text = new Text("For accurate results, set the Postprocessing filter in the Postprocessing tab to None.");
        text.setWrappingWidth(350);
        box.getChildren().add(text);
    }

    @Override
    public void update(Scene scene) {
        this.scene = scene;
        nmPositive.set(scene.getAdditionalData("aov_normal_positive").boolValue(nmPositive.get()));
        nmWaterDisplacement.set(scene.getAdditionalData("aov_normal_water_displacement").boolValue(nmWaterDisplacement.get()));
        depthNormFactor.set(scene.getAdditionalData("aov_depth_normalization").doubleValue(depthNormFactor.get()));
        infiniteSkyDepth.set(scene.getAdditionalData("aov_infinite_sky_depth").boolValue(infiniteSkyDepth.get()));
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
