package uboat.view.body.config;

import uboat.view.body.config.rotors.SingleRotorController;
import uboat.view.resources.Constants;
import dto.codeconfig.CodeConfigInfo;
import dto.loadedmachine.LoadedMachineDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uboat.view.body.UBoatCenterController;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UBoatMachineController {

    @FXML
    private TableView<MachineData> tableViewMachineDetails;

    @FXML
    private TableColumn<MachineData, String> tableColumnABC;

    @FXML
    private TableColumn<MachineData, Integer> tableColumnRotorsCount;

    @FXML
    private TableColumn<MachineData, Integer> tableColumnAvailableRotors;

    @FXML
    private TableColumn<MachineData, Integer> tableColumnAvailableReflectors;

    @FXML
    private ComboBox<String> comboBoxReflector;

    @FXML
    private Button buttonRetryReflector;

    @FXML
    private Button buttonRetryRotors;

    @FXML
    private Button buttonSetCodeConfig;

    @FXML
    private Button buttonGenerateCodeConfig;

    @FXML
    private FlowPane flowPaneRotorsBank;

    @FXML
    private TableView<IntegerProperty> tableViewRotors;

    @FXML
    private TableColumn<IntegerProperty, Integer> rotors;

    @FXML
    private TableView<StringProperty> tableViewStartingPos;

    @FXML
    private TableColumn<StringProperty, String> startingPos;

    @FXML
    private TableView<StringProperty> tableViewReflector;

    @FXML
    private TableColumn<StringProperty, String> reflector;

    private List<Integer> chosenRotors = null;

    private String rotorsStartingPoint = null;

    private String chosenReflector = null;

    private Map<Integer, SingleRotorController> rotorsBank = null;

    private BooleanProperty isCodeReadyProperty = null;

    private BooleanProperty isReflectorReadyProperty = null;

    private BooleanProperty isRotorsReadyProperty = null;

    private UBoatCenterController centerController = null;



    @FXML
    public void initialize(){
        this.chosenRotors = new ArrayList<>();
        this.rotorsStartingPoint = "";
        this.isCodeReadyProperty = new SimpleBooleanProperty(false);
        this.isReflectorReadyProperty = new SimpleBooleanProperty(false);
        this.isRotorsReadyProperty = new SimpleBooleanProperty(false);
        this.isCodeReadyProperty.bind(Bindings.and(isReflectorReadyProperty, isRotorsReadyProperty));
        this.tableColumnABC.setCellValueFactory(
                cellData -> cellData.getValue().abcProperty()
        );
        this.tableColumnAvailableRotors.setCellValueFactory(
                cellData -> cellData.getValue().availableRotorsProperty().asObject()
        );
        this.tableColumnRotorsCount.setCellValueFactory(
                cellData -> cellData.getValue().rotorsCountProperty().asObject()
        );
        this.tableColumnAvailableRotors.setCellValueFactory(
                cellData -> cellData.getValue().availableRotorsProperty().asObject()
        );
        this.tableColumnAvailableReflectors.setCellValueFactory(
                cellData -> cellData.getValue().availableReflectorsProperty().asObject()
        );
        this.reflector.setCellValueFactory(
                cellData -> cellData.getValue()
        );
        this.rotors.setCellValueFactory(
                cellData -> cellData.getValue().asObject()
        );
        this.startingPos.setCellValueFactory(
                cellData -> cellData.getValue()
        );
        this.rotorsBank = new HashMap<>();
        this.buttonSetCodeConfig.disableProperty().bind(this.isCodeReadyProperty.not());
        this.comboBoxReflector.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.chosenReflector = newValue;
            this.tableViewReflector.getItems().add(new SimpleStringProperty(newValue));
            this.comboBoxReflector.setDisable(true);
            this.isReflectorReadyProperty.set(true);
        });
    }


    @FXML
    void onRetryRotors(ActionEvent event) {
        this.isRotorsReadyProperty.set(false);

        for(Integer rotorID  : chosenRotors){
            this.rotorsBank.get(rotorID).setDisable(false);
        }

        this.tableViewRotors.getItems().clear();
        this.tableViewStartingPos.getItems().clear();
        this.chosenRotors.clear();
        this.rotorsStartingPoint = "";
    }

    @FXML
    void onRetryReflector(ActionEvent event) {
        this.isReflectorReadyProperty.set(false);
        this.comboBoxReflector.setDisable(false);
        this.chosenReflector = "";
        this.tableViewReflector.getItems().clear();
    }

    @FXML
    void onSetCodeConfig(ActionEvent event) throws IOException {
        this.isRotorsReadyProperty.set(false);
        this.isReflectorReadyProperty.set(false);
        this.centerController.setCode(
                new CodeConfigInfo(
                        new ArrayList<>(this.chosenRotors),
                        this.rotorsStartingPoint,
                        this.chosenReflector
                )
        );
        this.clearChoices();
    }

    @FXML
    void onGenerateCodeConfig(ActionEvent event) {
        this.clearChoices();
        this.centerController.generateCode();
    }

    public void setCenterController(UBoatCenterController centerController){
        this.centerController = centerController;
    }

    public void fileLoaded(LoadedMachineDTO loadedMachineDTO){
        this.clearAll();
        this.tableViewMachineDetails.getItems().add(
                new MachineData(
                        loadedMachineDTO.getAbc(),
                        loadedMachineDTO.getRotorsCount(),
                        loadedMachineDTO.getAvailableRotors().size(),
                        loadedMachineDTO.getAvailableReflectors().size()
                ));

        for(String reflectorID : loadedMachineDTO.getAvailableReflectors()){
            this.comboBoxReflector.getItems().add(reflectorID);
        }

        for(Integer rotorID : loadedMachineDTO.getAvailableRotors()){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(Constants.ROTOR_VIEW));
                Node rotor = loader.load();

                SingleRotorController singleRotorController = loader.getController();
                singleRotorController.setMachineController(this);
                singleRotorController.initialize(rotorID);

                this.flowPaneRotorsBank.getChildren().add(rotor);
                this.rotorsBank.put(rotorID, singleRotorController);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void clearAll() {
        this.tableViewMachineDetails.getItems().clear();
        this.comboBoxReflector.getItems().clear();
        this.rotorsBank.clear();
        this.flowPaneRotorsBank.getChildren().clear();
        this.clearChoices();
        this.isReflectorReadyProperty.set(false);
        this.isRotorsReadyProperty.set(false);
    }

    private void clearChoices() {
        this.flowPaneRotorsBank.setDisable(false);
        for(Integer rotorID  : chosenRotors){
            this.rotorsBank.get(rotorID).setDisable(false);
        }
        this.rotorsStartingPoint = "";
        this.chosenReflector = "";
        this.chosenRotors.clear();
        this.tableViewReflector.getItems().clear();
        this.tableViewStartingPos.getItems().clear();
        this.tableViewRotors.getItems().clear();
    }

    public void rotorChosen(int rotorsID) {
        this.chosenRotors.add(rotorsID);
        this.showPositionPopUp();
        this.tableViewRotors.getItems().add(new SimpleIntegerProperty(rotorsID));

        if(this.chosenRotors.size() == this. tableColumnRotorsCount.getCellData(0)){
            this.flowPaneRotorsBank.setDisable(true);
            this.isRotorsReadyProperty.set(true);
        }
    }

    private void showPositionPopUp() {
        Stage popUpWindow = new Stage();
        popUpWindow.setTitle("Choose Starting Position");
        popUpWindow.initModality(Modality.APPLICATION_MODAL);
        popUpWindow.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Choose a position and click 'Set'");
            alert.showAndWait();
            event.consume();
        });

        Text instructions = new Text("Please choose a starting position for the rotor.");
        instructions.textAlignmentProperty().set(TextAlignment.CENTER);
        instructions.fontProperty().set(Font.font(15));
        instructions.wrappingWidthProperty().set(200);

        ComboBox<String> positions = new ComboBox<>();
        positions.setPromptText("Starting Positions");
        for (Character ch : this.tableColumnABC.getCellData(0).toCharArray()){
            positions.getItems().add(ch.toString());
        }

        Button setPosition = new Button("Set");
        setPosition.setOnAction(event -> {
            this.tableViewStartingPos.getItems().add(new SimpleStringProperty(positions.getValue()));
            this.rotorsStartingPoint += positions.getValue();
            popUpWindow.close();
        });

        VBox layout = new VBox();
        layout.getChildren().addAll(instructions, positions, setPosition);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.spacingProperty().set(50);

        Scene scene = new Scene(layout, 200, 200);
        popUpWindow.setScene(scene);
        popUpWindow.showAndWait();
    }

    public void contestEnded() {
        this.clearViews();
        this.chosenReflector = "";
        this.chosenRotors.clear();
        this.rotorsStartingPoint = "";
        this.comboBoxReflector.getItems().clear();
        this.rotorsBank.clear();
        this.clearProperties();
    }

    private void clearProperties() {
        this.isRotorsReadyProperty.set(false);
        this.isCodeReadyProperty.set(false);
        this.isReflectorReadyProperty.set(false);
    }

    private void clearViews() {
        this.tableViewMachineDetails.getItems().clear();
        this.tableViewReflector.getItems().clear();
        this.tableViewRotors.getItems().clear();
        this.tableViewStartingPos.getItems().clear();
        this.flowPaneRotorsBank.getChildren().clear();
    }
}