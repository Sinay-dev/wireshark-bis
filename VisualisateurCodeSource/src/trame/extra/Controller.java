package trame.extra;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import trame.ITrame;
import trame.ListeTrames;

public class Controller {

    private Scene scene;
    private Stage stage;
    private Parent root;
    private ListeTrames lt;
    private File selectedFile;

    @FXML
    public VBox vbox;

    @FXML
    public VBox vboxG;

    @FXML
    public Pane pane;

    @FXML
    public ScrollPane sp;
    @FXML
    public ScrollPane sp2;

    @FXML
    public Button filtreBut;

    @FXML
    public TextField textF;

    @FXML
    public AnchorPane ap;

    @FXML
    public void chooseFile(ActionEvent e) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Frame File");
        selectedFile = fileChooser.showOpenDialog(stage);
        lt = ListeTrames.loadTrames(selectedFile.getAbsolutePath());
        initDiag();
        filtreBut.setDisable(false);
        textF.setDisable(false);

    }

    private void initDiag() {

        vbox.getChildren().clear();
        NumberAxis yAxis = new NumberAxis(0, lt.getTrames().size(), 1);
        CategoryAxis xAxis = new CategoryAxis();
       

        yAxis.setLowerBound(lt.getTrames().size() + 1);
        yAxis.setUpperBound(0);
        xAxis.setSide(Side.TOP);
        xAxis.focusTraversableProperty().set(true);
        yAxis.autoRangingProperty().set(false);
       

        LineChart linechart = new LineChart<String, Number>(xAxis, yAxis);
        linechart.focusTraversableProperty().set(true);

        
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        Label te = new Label("Comment");
        te.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        te.setMinHeight(Control.USE_PREF_SIZE);
        te.setPrefHeight(30);
        vbox.setPadding(new javafx.geometry.Insets(13, 0, 30, 0));
        te.setMaxHeight(Control.USE_PREF_SIZE);
        vbox.getChildren().add(te);

        for (int i = 0; i < lt.getTrames().size(); i++) {
            series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data(lt.getTrames().get(i).getMesSrc(), i + 1));
            series.getData().add(new XYChart.Data(lt.getTrames().get(i).getMesDst(), i + 1));
            linechart.getData().add(series);

            Node node = series.getData().get(0).getNode();
            node.setScaleX(Double.parseDouble("0.0"));

            te = new Label(lt.getTrames().get(i).getComment());
            te.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            te.setMinHeight(Control.USE_PREF_SIZE);
            te.setPrefHeight(30);
            te.setMaxHeight(Control.USE_PREF_SIZE);
            vbox.getChildren().add(te);

        }
        linechart.setLegendVisible(false);

        linechart.horizontalGridLinesVisibleProperty();

        
        // lv.setPadding(new javafx.geometry.Insets(40, 0, 0, 0));
        // vbox.setPadding(new javafx.geometry.Insets(20, 0, 0, 0));
        linechart.setPadding(new javafx.geometry.Insets(0, 400, 0, 0));
        linechart.setPrefSize((lt.getListIP().size() + 5) * 150., (lt.getTrames().size() + 2) * 30.);
        linechart.setMaxSize(Control.USE_COMPUTED_SIZE, Control.USE_PREF_SIZE);
        linechart.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);

        ap.getChildren().add(linechart);
        sp.setContent(linechart);
        sp2.setContent(vbox);
        sp.vvalueProperty().bindBidirectional(sp2.vvalueProperty());
        

        /* WRITABLE IMAGE */
        
        WritableImage overlay = vbox.snapshot(new SnapshotParameters(), null);
        // WritableImage ips = vboxG.snapshot(new SnapshotParameters(), null);
        // c.getGraphicsContext2D().drawImage((Image)ips, 0, 0);
        
        Canvas c = new Canvas(linechart.getWidth(), linechart.getHeight());
        
        WritableImage image = linechart.snapshot(new SnapshotParameters(), null);

        c.getGraphicsContext2D().drawImage((Image)image, 0, 0);
        c.getGraphicsContext2D().drawImage((Image)overlay, c.getWidth() - 400, 0);

        WritableImage base = c.snapshot(new SnapshotParameters(), null);

        File file = new File("chart.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(base, null), "png", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filtrage(ActionEvent e) {
        lt = ListeTrames.loadTrames(selectedFile.getAbsolutePath());
        String text = textF.getText();
        String[] fil = text.split("\\ ");
        for (int i = 0; i < fil.length; i++) {
            if (fil[i].equals("-ip") || fil[i].equals("-i")) {
                i++;
                String[] ipa = fil[i].split("\\ ");
                lt.filtreParIp(ipa);
                initDiag();
            } else if (fil[i].equals("-protocol") || fil[i].equals("-p")) {
                {
                    i++;
                    String[] ipa = fil[i].split("\\ ");
                    lt.filtreParProtocol(ipa);
                    initDiag();
                }
            }
        }
    }

}
