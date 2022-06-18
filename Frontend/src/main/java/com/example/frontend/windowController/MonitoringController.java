package com.example.frontend.windowController;

import Model.ModelTools.*;
import com.example.frontend.FxmlLoader;
import Model.Model;

import com.example.frontend.SmallestEnclosingCircle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.chart.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MonitoringController implements Initializable {
    //................Data members.................//
    private List<Point2D> list = new ArrayList<>();

    //................GUI..........................//

    @FXML
    private MenuItem aileron;

    @FXML
    private MenuItem airSpeed_kt;

    @FXML
    private MenuItem vertSpeed;

    @FXML
    private MenuItem altitude;

    @FXML
    private MenuItem elevator;

    @FXML
    private MenuItem rudder;

    @FXML
    private MenuItem flaps;

    @FXML
    private MenuItem longitude;

    @FXML
    private MenuItem heading;

    @FXML
    private MenuItem latitude;

    @FXML
    private MenuItem pitchDeg;

    @FXML
    private MenuItem rollDeg;

    @FXML
    private MenuItem throttle_0;

    @FXML
    private MenuItem throttle_1;

    @FXML
    private MenuItem turnCoordinator;

    @FXML
    private SplitMenuButton splitMenuItem;
    @FXML
    private BorderPane joyStickBorderPane;
    @FXML
    private BorderPane clocksBorderPane;
    @FXML
    private BorderPane bigChartBorderPane;
    @FXML
    private BorderPane leftAreaChartBorderPane;
    @FXML
    private BorderPane rightAreaChartBorderPane;
    Model m;

    //.........................................//

    public void setModel(Model m) {
        this.m = m;
    }


    SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
    TimeSeries ts = new TimeSeries(
            "Frontend/src/main/java/Model/ModelTools/train.csv");

    public List<CorrelatedFeatures> findRequiredList(String name) {
        List<CorrelatedFeatures> correlatedFeatures = sad.listOfPairs;
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = new ArrayList<>();
        for (CorrelatedFeatures cf : correlatedFeatures) {
            if ((cf.getFeature1().equals(name) || cf.getFeature2().equals(name))) {
                correlatedFeatureOfWhatWeNeed.add(cf);
            }
        }
        return correlatedFeatureOfWhatWeNeed;
    }

//    public void aileron(ActionEvent event) {
//        sad.learnNormal(ts);
//        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList("aileron");
//        if (event.getSource() == aileron) {
//            if(correlatedFeatureOfWhatWeNeed.get(0).threshold > 0.95)
//                createLineCharts(correlatedFeatureOfWhatWeNeed);
//            if(correlatedFeatureOfWhatWeNeed.get(0).threshold < 0.95 || correlatedFeatureOfWhatWeNeed.get(0).threshold > 0.5)
//                createCircleGraph(correlatedFeatureOfWhatWeNeed);
//        }
//    }

    public void feature(ActionEvent event){
        sad.learnNormal(ts);
        String name = ((MenuItem) event.getSource()).getText();
        System.out.println(name);
        List<CorrelatedFeatures> correlatedFeatureOfWhatWeNeed = findRequiredList(name);
        if(correlatedFeatureOfWhatWeNeed.get(0).threshold > 0.95)
            createLineCharts(correlatedFeatureOfWhatWeNeed);
        if(correlatedFeatureOfWhatWeNeed.get(0).threshold < 0.95 || correlatedFeatureOfWhatWeNeed.get(0).threshold > 0.5)
            createCircleGraph(correlatedFeatureOfWhatWeNeed);

    }

    public double max(Vector<Double> v) {
        double max = v.get(0);
        for (int i = 1; i < v.size(); i++) {
            if (v.get(i) > max) {
                max = v.get(i);
            }
        }
        return max;
    }

    public double min(Vector<Double> v) {
        double min = v.get(0);
        for (int i = 1; i < v.size(); i++) {
            if (v.get(i) < min) {
                min = v.get(i);
            }
        }
        return min;
    }

    public void createCircleGraph(List<CorrelatedFeatures> cf) {
        List<Point> points = new ArrayList<>();
        //populate the points randomly
        for (int i = 0; i < 100; i++) {
            Point p = new Point((float) Math.random() * 500, (float) Math.random() * 500);
            points.add(p);
        }
        //create for loop that iterate points and find max and min from points
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        for (Point p : points) {
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }
        for (Point p : points) {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
        }
        double zoom = 0.5;
        double upperBoundX = maxX + (maxX - minX)*zoom;
        double lowerBoundX = minX - (maxX - minX)*zoom;
        double upperBoundY = maxY + (maxY - minY)*zoom;
        double lowerBoundY = minY - (maxY - minY)*zoom;
        double biggestUpperBoundXY = Math.max(upperBoundX, upperBoundY);
        double smallestLowerBoundXY = Math.min(lowerBoundX, lowerBoundY);
        upperBoundX = biggestUpperBoundXY;
        lowerBoundX = smallestLowerBoundXY;
        upperBoundY = biggestUpperBoundXY;
        lowerBoundY = smallestLowerBoundXY;
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        ScatterChart chart = new ScatterChart(xAxis, yAxis);
        chart.setTitle("Circle Chart");
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(lowerBoundX);
        xAxis.setUpperBound(upperBoundX);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(lowerBoundY);
        yAxis.setUpperBound(upperBoundY);
        XYChart.Series series1 = new XYChart.Series();
        for(int i = 0; i < points.size(); i++){
            series1.getData().add(new XYChart.Data(points.get(i).x, points.get(i).y));
        }
        bigChartBorderPane.setCenter(chart);
        List<com.example.frontend.Point> points2 = new ArrayList<>();
        for(Point p : points){
            points2.add(new com.example.frontend.Point(p.x, p.y));
        }
        com.example.frontend.Circle circle2 = SmallestEnclosingCircle.makeCircle(points2);
        XYChart.Series series2 = new XYChart.Series();
        for(int i = 0; i < 1000; i++){
            double x2 = circle2.c.x + circle2.r*Math.cos(2*Math.PI*i/1000);
            double y2 = circle2.c.y + circle2.r*Math.sin(2*Math.PI*i/1000);
            series2.getData().add(new XYChart.Data(x2, y2));
        }
        chart.getData().addAll(series2, series1);
    }

    public void createLineCharts(List<CorrelatedFeatures> cf) {
        //.................Create line charts.................//
        NumberAxis bigX = new NumberAxis();
        NumberAxis bigY = new NumberAxis();
        LineChart bigChart = new LineChart(bigX, bigY);
        SimpleAnomalyDetector sad = new SimpleAnomalyDetector();
        if (cf.isEmpty()) {
            return;  //if there are no correlated features, maybe we should show a message to the user
        }
        TimeSeries ts2 = new TimeSeries(
                "Frontend/src/main/java/Model/ModelTools/test.csv");

        double maxCorr = Double.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < cf.size(); i++) {
            if (cf.get(i).correlation > maxCorr) {
                index = i;
                maxCorr = cf.get(i).correlation;
            }
        }
        for (CorrelatedFeatures correlatedFeatures : cf) {
            if (correlatedFeatures != cf.get(index)) {
                cf.remove(correlatedFeatures);
            }
        }
        sad.listOfPairs = cf;
        sad.detect(ts2);
        List<AnomalyReport> reports = sad.listOfExp;
        Vector<Double> v1 = ts.getColByName(cf.get(0).getFeature1());
        Vector<Double> v2 = ts.getColByName(cf.get(0).getFeature2());
        int len = ts.getArray().size();
        XYChart.Series seriesBigChart = new XYChart.Series<>();
        seriesBigChart.setName("Big Chart");
        for (int i = 0; i < len; i++) {
            seriesBigChart.getData().add(new XYChart.Data<>(v1.get(i), v2.get(i)));
        }
        XYChart.Series linearRegressionSeries = new XYChart.Series();
        linearRegressionSeries.setName("Linear Regression");
        double max = max(v1);
        double min = min(v1);
        linearRegressionSeries.getData().add(new XYChart.Data<>(min, cf.get(0).lin_reg.f((float) min)));
        linearRegressionSeries.getData().add(new XYChart.Data<>(max, cf.get(0).lin_reg.f((float) max)));
        XYChart.Series anomalyPointsSeries = new XYChart.Series();
        anomalyPointsSeries.setName("Anomaly Points");
        for (int i = 0; i < sad.anomalyPoints.size(); i++) {
            anomalyPointsSeries.getData().add(new XYChart.Data<>(sad.anomalyPoints.get(i).x, sad.anomalyPoints.get(i).y));
        }
        bigChart.getData().addAll(seriesBigChart, linearRegressionSeries, anomalyPointsSeries);
        bigChartBorderPane.setCenter(bigChart);
        //.................Create area charts.................//
        NumberAxis leftX = new NumberAxis();
        NumberAxis leftY = new NumberAxis();
        AreaChart leftAreaChart = new AreaChart(leftX, leftY);
        leftAreaChart.setCreateSymbols(false);
        NumberAxis rightX = new NumberAxis();
        NumberAxis rightY = new NumberAxis();
        AreaChart rightAreaChart = new AreaChart(rightX, rightY);
        rightAreaChart.setCreateSymbols(false);
        XYChart.Series seriesLeftAreaChart = new XYChart.Series<>();
        seriesLeftAreaChart.setName("Left Area Chart");
        XYChart.Series seriesRightAreaChart = new XYChart.Series<>();
        seriesRightAreaChart.setName("Right Area Chart");
        for (int i = 0; i < len; i++) {
            seriesLeftAreaChart.getData().add(new XYChart.Data<>(i, v1.get(i))); //the x need to be the column of time
            seriesRightAreaChart.getData().add(new XYChart.Data<>(i, v2.get(i)));
        }
        leftX.setAutoRanging(false);
        leftX.setLowerBound(len - 20);
        leftX.setUpperBound(len);
        rightX.setAutoRanging(false);
        rightX.setLowerBound(len - 20);
        rightX.setUpperBound(len);
        leftAreaChart.getData().addAll(seriesLeftAreaChart);
        rightAreaChart.getData().addAll(seriesRightAreaChart);
        leftAreaChartBorderPane.setCenter(leftAreaChart);
        rightAreaChartBorderPane.setCenter(rightAreaChart);
    }

    public void createJoyStick() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane joyStickPane = new Pane();
        try {
            joyStickPane = fxmlLoader.load(FxmlLoader.class.getResource("JoyStick.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        joyStickBorderPane.setCenter(joyStickPane);
        JoyStickController joyStick = (JoyStickController) fxmlLoader.getController();
        //joyStick.disableJoyStick();
        joyStick.initViewModel(m);
    }

    public void createClocks() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane clocksPane = new Pane();
        try {
            clocksPane = fxmlLoader.load(FxmlLoader.class.getResource("Clocks.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        clocksBorderPane.setCenter(clocksPane);
        ClocksController clocks = (ClocksController) fxmlLoader.getController();
        //clocks.initViewModel(m);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        LineChart emptyChart = new LineChart(new NumberAxis(), new NumberAxis());
//
//        emptyChart.setShape(new Circle(360, 360, 360));
//        bigChartBorderPane.setCenter(emptyChart);

    }
}
