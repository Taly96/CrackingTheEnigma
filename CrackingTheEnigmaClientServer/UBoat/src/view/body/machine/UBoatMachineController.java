package view.body.machine;

import dto.loadedmachine.LoadedMachineDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.body.machine.rotors.SingleRotorController;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static view.resources.Resources.ROTOR_VIEW;

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

    private List<Integer> chosenRotors = null;

    private String rotorsStartingPoint = null;

    private String chosenReflector = null;

    private Map<Integer, SingleRotorController> rotorsBank = null;

    private BooleanProperty isCodeReadyProperty = null;

    private BooleanProperty isReflectorReadyProperty = null;

    private BooleanProperty isRotorsReadyProperty = null;



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
        this.rotorsBank = new HashMap<>();
        this.buttonSetCodeConfig.disableProperty().bind(this.isCodeReadyProperty);
        this.comboBoxReflector.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.chosenReflector = newValue;
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

        this.chosenRotors.clear();
        this.rotorsStartingPoint = "";
    }

    @FXML
    void onRetryReflector(ActionEvent event) {
        this.isReflectorReadyProperty.set(false);
        this.comboBoxReflector.setDisable(false);
        this.chosenReflector = "";
    }

    @FXML
    void onSetCodeConfig(ActionEvent event) {
        this.isCodeReadyProperty.set(false);
        //todo

    }

    @FXML
    void onGenerateCodeConfig(ActionEvent event) {
        //todo

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
                loader.setLocation(getClass().getResource(ROTOR_VIEW));
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
        this.rotorsStartingPoint = "";
        this.chosenReflector = "";
        this.chosenRotors.clear();
        this.isCodeReadyProperty.set(false);
    }

    public void rotorChosen(int rotorsID) {
        this.chosenRotors.add(rotorsID);
        this.showPositionPopUp();
        if(this.chosenRotors.size() == this. tableColumnRotorsCount.getCellData(0)){
            this.flowPaneRotorsBank.setDisable(true);
            this.isRotorsReadyProperty.set(true);
        }
    }

    private void showPositionPopUp() {
        Stage popUpWindow = new Stage();
        popUpWindow.setTitle("Choose Starting Position");
        popUpWindow.initModality(Modality.APPLICATION_MODAL);

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
}