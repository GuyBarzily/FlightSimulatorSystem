package com.example.frontend.windowController;

import Model.Model;
import com.example.frontend.ClocksViewModel;
import com.example.frontend.JoyStickViewModel;
import eu.hansolo.medusa.*;
import eu.hansolo.medusa.skins.ModernSkin;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;


public class ClocksController implements Initializable {
    @FXML
    BorderPane bp1 = new BorderPane();
    @FXML
    BorderPane bp2 = new BorderPane();
    @FXML
    BorderPane bp3 = new BorderPane();
    @FXML
    BorderPane bp4 = new BorderPane();
    @FXML
    BorderPane bp5 = new BorderPane();
    @FXML
    BorderPane bp6 = new BorderPane();

    ClocksViewModel vm;

    DoubleProperty compassDegree,speed,verticalSpeed;

    public ClocksController() {
        compassDegree = new SimpleDoubleProperty();
        verticalSpeed = new SimpleDoubleProperty();
        speed = new SimpleDoubleProperty();

    }

    public void initViewModel(Model m) {
        this.vm = new ClocksViewModel(m);
        vm.addObserver((Observer) this);
        vm.compassDegree.bindBidirectional(compassDegree);
        vm.verticalSpeed.bindBidirectional(verticalSpeed);
        vm.speed.bindBidirectional(speed);
    }

    public void paintAirSpeed() {
        //create an air speed gauge

        Gauge airSpeed = new Gauge();

        airSpeed.setSkin(new ModernSkin(airSpeed));  //ModernSkin : you guys can change the skin
        airSpeed.setTitle("AIRSPEED");  //title
        airSpeed.setUnit("Km / h");  //unit
        airSpeed.setUnitColor(Color.WHITE);
        airSpeed.setDecimals(0);
        airSpeed.setValue(this.speed.doubleValue()); //deafult position of needle on gauage
        airSpeed.setAnimated(true);
        //gauge.setAnimationDuration(500);

        airSpeed.setValueColor(Color.WHITE);
        airSpeed.setTitleColor(Color.WHITE);
        airSpeed.setSubTitleColor(Color.WHITE);
        airSpeed.setBarColor(Color.rgb(0, 214, 215));
        airSpeed.setNeedleColor(Color.RED);
        airSpeed.setThresholdColor(Color.RED);  //color will become red if it crosses threshold value
        airSpeed.setThreshold(85);
        airSpeed.setThresholdVisible(true);
        airSpeed.setTickLabelColor(Color.rgb(151, 151, 151));
        airSpeed.setTickMarkColor(Color.WHITE);
        airSpeed.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
        bp1.setCenter(airSpeed);

    }

    public void paintVerticalSpeed() {
        //create an air speed gauge

        Gauge verticalSpeed = GaugeBuilder.create()
                .skinType(Gauge.SkinType.VERTICAL)
                .title("100 FEET PER MINUTER")
                .unit("VERTICAL SPEED")
                .minValue(-20)
                .maxValue(20)
                .value(this.verticalSpeed.doubleValue())
                .animated(true)
                .foregroundBaseColor(Color.BLACK)
                .build();

        bp6.setCenter(verticalSpeed);
    }

    public void paintCompass() {
        Gauge compass = GaugeBuilder.create()
                .minValue(0)
                .maxValue(359)
                .startAngle(180)
                .angleRange(360)
                .autoScale(false)
                .customTickLabelsEnabled(true)
                .customTickLabels("N", "", "", "", "", "", "", "", "",
                        "E", "", "", "", "", "", "", "", "",
                        "S", "", "", "", "", "", "", "", "",
                        "W", "", "", "", "", "", "", "", "")
                .customTickLabelFontSize(72)
                .minorTickMarksVisible(false)
                .mediumTickMarksVisible(false)
                .majorTickMarksVisible(false)
                .valueVisible(false)
                .needleType(Gauge.NeedleType.FAT)
                .needleShape(Gauge.NeedleShape.ANGLED)
                .knobType(Gauge.KnobType.FLAT)
                .knobColor(Gauge.DARK_COLOR)
                .borderPaint(Gauge.DARK_COLOR)
                .animated(true)
                .animationDuration(500)
                .needleBehavior(Gauge.NeedleBehavior.OPTIMIZED)
                .build();
                compass.setValue(this.compassDegree.getValue());
        bp5.setCenter(compass);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paintAirSpeed();
        paintVerticalSpeed();
        paintCompass();
    }
}
