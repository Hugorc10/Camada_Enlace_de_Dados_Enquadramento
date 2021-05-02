package uesb.redes.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import uesb.redes.controller.ButtonActions;
import uesb.redes.model.LinePositions;
import uesb.redes.util.Codifications;

import java.util.Objects;

public class ScreenView {
    
    public static boolean violacaoMeioFisico;
    private BorderPane rootPane;
    private Button startBtn;
    private Button stopBtn;
    private Button clearBtn;
    public static TextField inputTxt;
    public static TextField receiverTxt;
    public static TextArea bitsTArea;
    public static ToggleGroup group;
    public static RadioButton binaryRad;
    public static RadioButton manchesterRad;
    public static RadioButton difManchesterRad;
    public static CheckBox framingChk;
    public static CheckBox characterStufingChk;
    public static CheckBox bitStufingChk;
    public static CheckBox violacaoChk;
    public static Slider speedSld;
    
    public static Line[] binaryLine1 = new Line[13];
    public static Line[] manchesterLine1 = new Line[13];
    public static Line[] manchesterLine2 = new Line[13];
    public static Line[] manchesterLine3 = new Line[13];
    public static Line[] difManchesterLine1 = new Line[13];
    public static Line[] difManchesterLine2 = new Line[13];
    public static Line[] difManchesterLine3 = new Line[13];
    
    
    public Parent crateContent() {
        rootPane = new BorderPane();
    
        HBox hBoxTop = addHBoxTop();
        VBox vBoxLeft = addVBoxLeft();
        Pane paneCenter = addPaneCenter();
        HBox hBoxBottom = addHBoxBottom();
    
        rootPane.setTop(hBoxTop);
        rootPane.setCenter(paneCenter);
        rootPane.setLeft(vBoxLeft);
    
        addStackPane(hBoxTop);
        
        rootPane.setBottom(hBoxBottom);
    
        return rootPane;
    }
    
    private HBox addHBoxTop() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #527999;");
        
        startBtn = new Button("Start");
        startBtn.setDefaultButton(true);
        startBtn.setPrefSize(75, 50);
        startBtn.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ButtonActions.onClickStart();
            }
        });
        
        stopBtn = new Button("Stop");
        stopBtn.setPrefSize(75, 50);
        stopBtn.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        stopBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ButtonActions.onClickStop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        clearBtn = new Button("Clear");
        clearBtn.setPrefSize(75, 50);
        clearBtn.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ButtonActions.onClickClear();
            }
        });
        
        inputTxt = new TextField();
        inputTxt.setFont(Font.font("Verdana", FontWeight.MEDIUM, 18));
        inputTxt.setPrefHeight(48);
        inputTxt.setPrefColumnCount(20);
        inputTxt.setPromptText("Type the text");
        
        Label receiverLbl = new Label("Receiver");
        receiverLbl.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));
        receiverLbl.setTextFill(Color.BLACK);
        receiverLbl.setTranslateX(20);
        receiverLbl.setTranslateY(10);
        
        receiverTxt = new TextField();
        receiverTxt.setPrefHeight(48);
        receiverTxt.setPrefColumnCount(20);
        receiverTxt.setFont(Font.font("Verdana", FontWeight.MEDIUM, 18));
        receiverTxt.setEditable(false);
        receiverTxt.setTranslateX(20);
        
        hBox.getChildren().addAll(startBtn, stopBtn, clearBtn, inputTxt, receiverLbl, receiverTxt);
        
        return hBox;
    }
    
    private VBox addVBoxLeft() {
        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(15, 0, 0, 10));
        vBox1.setSpacing(13);
        vBox1.setAlignment(Pos.TOP_LEFT);
        
        VBox vBox2 = new VBox();
        vBox2.setPadding(new Insets(25, 0, 0, 0));
        vBox2.setSpacing(13);
        vBox2.setAlignment(Pos.TOP_LEFT);
        
        VBox vBox3 = new VBox();
        vBox3.setPadding(new Insets(10, 0, 0, 60));
        vBox3.setAlignment(Pos.CENTER);
        
        Text title1 = new Text("Select the codification");
        title1.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        
        group = new ToggleGroup();
        binaryRad = new RadioButton("Binary");
        binaryRad.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        binaryRad.setToggleGroup(group);
        binaryRad.setSelected(false);
        
        manchesterRad = new RadioButton("Manchester");
        manchesterRad.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        manchesterRad.setToggleGroup(group);
        manchesterRad.setSelected(false);
        
        difManchesterRad = new RadioButton("Differential Manchester");
        difManchesterRad.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        difManchesterRad.setToggleGroup(group);
        difManchesterRad.setSelected(false);
        
        Text title2 = new Text("Select the framing");
        title2.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
//
        framingChk = new CheckBox("Byte count");
        framingChk.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        characterStufingChk = new CheckBox("Byte stuffing");
        characterStufingChk.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        bitStufingChk = new CheckBox("Bit stuffing");
        bitStufingChk.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        violacaoChk = new CheckBox("Physical layer coding violations");
        violacaoChk.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        
        framingChk.selectedProperty().addListener((isNowSelect) -> {
            Codifications.framingType = 1;
            characterStufingChk.setSelected(false);
            characterStufingChk.setDisable(true);
    
            bitStufingChk.setSelected(false);
            bitStufingChk.setDisable(true);
    
            violacaoChk.setSelected(false);
            violacaoChk.setDisable(true);
    
            if (!framingChk.isSelected()) {
                characterStufingChk.setDisable(false);
                bitStufingChk.setDisable(false);
                violacaoChk.setDisable(false);
            }
        });
        
        characterStufingChk.selectedProperty().addListener((isNowSelect) -> {
            Codifications.framingType = 2;
            framingChk.setSelected(false);
            framingChk.setDisable(true);
            
            bitStufingChk.setSelected(false);
            bitStufingChk.setDisable(true);
            
            violacaoChk.setSelected(false);
            violacaoChk.setDisable(true);
            
            if (!characterStufingChk.isSelected()) {
                framingChk.setDisable(false);
                bitStufingChk.setDisable(false);
                violacaoChk.setDisable(false);
            }
        });
        
        bitStufingChk.selectedProperty().addListener((isNowSelect) -> {
            Codifications.framingType = 3;
            characterStufingChk.setSelected(false);
            characterStufingChk.setDisable(true);
            
            framingChk.setSelected(false);
            framingChk.setDisable(true);
            
            violacaoChk.setSelected(false);
            violacaoChk.setDisable(true);
            
            if (!bitStufingChk.isSelected()) {
                characterStufingChk.setDisable(false);
                framingChk.setDisable(false);
                violacaoChk.setDisable(false);
            }
        });
        
        violacaoChk.selectedProperty().addListener((isNowSelect) -> {
            Codifications.framingType = 4;
            characterStufingChk.setSelected(false);
            characterStufingChk.setDisable(true);
            
            bitStufingChk.setSelected(false);
            bitStufingChk.setDisable(true);
            
            framingChk.setSelected(false);
            framingChk.setDisable(true);
            
            if (!violacaoChk.isSelected()) {
                characterStufingChk.setDisable(false);
                bitStufingChk.setDisable(false);
                framingChk.setDisable(false);
            }
        });
        
        Image image = new Image(Objects.requireNonNull(ScreenView.class.
                getResourceAsStream("/uesb/redes/img/computer-networks-a-tanenbaum.jpg")));
        
        ImageView networkImage = new ImageView();
        networkImage.setFitHeight(225);
        networkImage.setSmooth(true);
        networkImage.setPreserveRatio(true);
        networkImage.setImage(image);
        
        vBox1.getChildren().addAll(title1, binaryRad, manchesterRad, difManchesterRad);
        
        vBox2.getChildren().addAll(title2, framingChk, characterStufingChk, bitStufingChk/*,violacaoChk*/);
        
        vBox3.getChildren().add(networkImage);
        
        vBox1.getChildren().addAll(vBox2, vBox3);
        
        return vBox1;
    }
    
    public void addStackPane(HBox hb) {
        StackPane stack = new StackPane();
        
        Rectangle helpIcon = new Rectangle(70, 50);
        helpIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#4977A3")),
                new Stop(0.5, Color.web("#B0C6DA")),
                new Stop(1, Color.web("#9CB6CF"))));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);
        
        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));
        
        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);
        StackPane.setMargin(helpText, new Insets(0, 30, 0, 0)); // Center "?"
        
        hb.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);
    }
    
    public Pane addPaneCenter() {
        Pane pane = new Pane();
        pane.setPrefSize(1000, 500);
        pane.setTranslateX(75);
        
        Text speedTxt = new Text("Animation speed");
        speedTxt.setFill(Color.BLACK);
        speedTxt.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));
        speedTxt.setX(430);
        speedTxt.setY(50);
        speedTxt.setTextAlignment(TextAlignment.JUSTIFY);
        
        pane.getChildren().add(speedTxt);
        
        speedSld = new Slider(0, 500, 250);
        speedSld.setPrefWidth(350);
        speedSld.setOrientation(Orientation.HORIZONTAL);
        speedSld.setShowTickMarks(true);
        speedSld.setShowTickLabels(true);
        speedSld.setMajorTickUnit(125);
        speedSld.setBlockIncrement(25);
        speedSld.setTranslateX(350);
        speedSld.setTranslateY(75);
        
        pane.getChildren().add(speedSld);
        
        Label decreaseLbl = new Label("-");
        decreaseLbl.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 40));
        decreaseLbl.setLayoutX(325);
        decreaseLbl.setLayoutY(55);
        
        pane.getChildren().add(decreaseLbl);
        
        Label increaseLbl = new Label("+");
        increaseLbl.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 40));
        increaseLbl.setLayoutX(700);
        increaseLbl.setLayoutY(55);
        
        pane.getChildren().add(increaseLbl);
        
        Rectangle encodingRect = new Rectangle(175, 105);
        encodingRect.setStroke(Color.web("#D0E6FA"));
        encodingRect.setCache(true);
        encodingRect.setX(15);
        encodingRect.setY(247.5);
        encodingRect.setFill(Color.BLUE);
        
        Label encodingLbl = new Label("Signal");
        encodingLbl.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 22));
        encodingLbl.setTextFill(Color.WHITE);
        encodingLbl.setPrefSize(175, 105);
        encodingLbl.setLayoutX(10);
        encodingLbl.setLayoutY(247.5);
        encodingLbl.setAlignment(Pos.CENTER);
        
        // Linha usada para a divisao entre o layout esquerdo e central
        Line dividingLine = new Line();
        // Linhas verticais e horizontais que marcam o caminho que os sinais vao percorrer
        Line[] verticalLines = new Line[20];
        Line[] horizontalLines = new Line[2];
        
        dividingLine.setStroke(Color.BLACK);
        dividingLine.setStartX(0);
        dividingLine.setStartY(0);
        dividingLine.setEndX(0);
        dividingLine.setEndY(620);
        
        pane.getChildren().add(dividingLine);
        
        int x = LinePositions.POSITIVE_SIGNAL_Y.getValue();
        for (int i = 0; i < horizontalLines.length; i++) {
            horizontalLines[i] = new Line();
            horizontalLines[i].setStroke(Color.BLUEVIOLET);
    
            horizontalLines[i].setStartX(190);
            horizontalLines[i].setStartY(x);
            horizontalLines[i].setEndX(970);
            horizontalLines[i].setEndY(x);
    
            pane.getChildren().add(horizontalLines[i]);
    
            x += 100;
        }
        
        int z = 190;
        for (int i = 0; i < verticalLines.length; i++) {
            verticalLines[i] = new Line();
            verticalLines[i].setStroke(Color.BLUEVIOLET);
    
            verticalLines[i].setStartX(z);
            verticalLines[i].setStartY(LinePositions.POSITIVE_SIGNAL_Y.getValue());
            verticalLines[i].setEndX(z);
            verticalLines[i].setEndY(LinePositions.NEGATIVE_SIGNAL_Y.getValue());
    
            pane.getChildren().add(verticalLines[i]);
    
            z = z + LinePositions.CLOCK_LINE_WIDTH.getValue();
        }
        
        for (int i = 0; i < binaryLine1.length; i++) {
            binaryLine1[i] = new Line();
            binaryLine1[i].setStrokeWidth(3);
            pane.getChildren().add(binaryLine1[i]);
        }
        
        for (int i = 0; i < manchesterLine1.length; i++) {
            manchesterLine1[i] = new Line();
            manchesterLine2[i] = new Line();
            manchesterLine3[i] = new Line();
            manchesterLine1[i].setStrokeWidth(3);
            manchesterLine2[i].setStrokeWidth(3);
            manchesterLine3[i].setStrokeWidth(3);
            pane.getChildren().addAll(manchesterLine1[i], manchesterLine2[i], manchesterLine3[i]);
        }
        
        for (int i = 0; i < difManchesterLine1.length; i++) {
            difManchesterLine1[i] = new Line();
            difManchesterLine2[i] = new Line();
            difManchesterLine3[i] = new Line();
            difManchesterLine1[i].setStrokeWidth(3);
            difManchesterLine2[i].setStrokeWidth(3);
            difManchesterLine3[i].setStrokeWidth(3);
            pane.getChildren().addAll(difManchesterLine1[i], difManchesterLine2[i], difManchesterLine3[i]);
        }
        
        pane.getChildren().add(encodingRect);
        pane.getChildren().add(encodingLbl);
        
        return pane;
    }
    
    public HBox addHBoxBottom() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        
        Label bitsLbl = new Label();
        bitsLbl.setText("Bits");
        bitsLbl.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));
        bitsLbl.setTextFill(Color.BLACK);
        bitsLbl.setTranslateY(10);
        
        bitsTArea = new TextArea();
        bitsTArea.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));
        bitsTArea.setPrefHeight(48);
        bitsTArea.setPrefRowCount(0);
        bitsTArea.setPrefColumnCount(25);
        bitsTArea.setEditable(false);
        bitsTArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                bitsTArea.setScrollTop(Double.MIN_VALUE);
            }
        });
        
        hBox.getChildren().addAll(bitsLbl, bitsTArea);
        
        return hBox;
    }
}
