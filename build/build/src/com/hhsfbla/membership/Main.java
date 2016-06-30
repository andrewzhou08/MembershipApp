package com.hhsfbla.membership;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.hhsfbla.membership.model.Member;
import com.hhsfbla.membership.model.MemberListWrapper;
import com.hhsfbla.membership.view.MemberEditDialogController;
import com.hhsfbla.membership.view.MembersInformationController;
import com.hhsfbla.membership.view.MembersReportController;
import com.hhsfbla.membership.view.MenuBarController;
import com.hhsfbla.membership.view.SeniorsReportController;
import com.hhsfbla.membership.view.SplashScreenController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 * @author Andrew Zhou
 * @version 1.0.0
 *
 */
public class Main extends Application {

	private Stage primaryStage; //Primary stage used
	private BorderPane layout; //BorderPane used in stage
	private ObservableList<Member> memberData = FXCollections.observableArrayList(); //List of members
	public static AnchorPane page;
	public static AnchorPane seniorPage;
	public static AnchorPane helpPage;

	public Main(){ //No dummy data
		
	}

	/**
	 *
	 * @return the hard-coded memberData 
	 */
	public ObservableList<Member> getPersonData(){
		return memberData;
	}

	/**
	 * Runs when program starts. Sets up stage and initializes the layouts.
	 */
	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		//Sets stage name to Membership App
		this.primaryStage.setTitle("Membership App");
		//Sets stage image, top bar
		this.primaryStage.getIcons().add(new Image("file:resources/img/main-icon.png"));
		this.primaryStage.setResizable(false);

		BackupController bc = new BackupController();
		bc.setMain(this);
		bc.setMemberData(memberData);
		bc.initialize();
		
		initializeLayout();
		initializeSplashScreen();
		showMembersInformation();
	}

	public void initializeSplashScreen(){
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/SplashScreen.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			//Sets the stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Welcome!"); //Sets title to Edit Member
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.setResizable(false);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Loads controller
			SplashScreenController controller = loader.getController();
			controller.setStage(dialogStage);
			controller.setMain(this);
			dialogStage.showAndWait(); // Waits until ok is clicked, then returns
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes the layout. Creates the menu bar at the top.
	 */
	public void initializeLayout() {
		try {
			// Load root layout from fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/MenuBar.fxml"));
			layout = (BorderPane) loader.load();

			// Show the scene containing the root layout
			Scene scene = new Scene(layout);
			primaryStage.setScene(scene);

			// Loads menu bar controller
			MenuBarController controller = loader.getController();
			controller.setMainApp(this);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Tries to load file
		File file = getPersonFilePath();
		if (file != null)
			loadPersonDataFromFile(file);
	}

	/**
	 * Shows the person overview inside the root layout.
	 */
	public void showMembersInformation() {
		try {
			// Loads membersinformation from fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/MembersInformation.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Sets up membership info controller
			MembersInformationController controller = loader.getController();
			controller.setMain(this);
			controller.activateFilter(); //Activates filter bar
			layout.setCenter(personOverview);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows a new window to add, edit, or delete members from the database.
	 * @param member the member to show the edit dialog
	 * @return boolean true if ok is clicked, false if it is not
	 */
	public boolean showPersonEditDialog(Member member) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/MemberEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			//Sets the stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Member"); //Sets title to Edit Member
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Loads controller
			MemberEditDialogController controller = loader.getController();
			controller.setDialog(dialogStage);
			controller.setMember(member);
			dialogStage.showAndWait(); // Waits until ok is clicked, then returns
			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Generates a members report, showing all member information.
	 */
	public void showMembersReport(){
		try{
			// Load the fxml file and create a new stage for the members report
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/MembersReport.fxml"));
			page = (AnchorPane) loader.load();

			//Sets the stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Members Owing Money Report"); //Sets title to arg
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Loads controller
			MembersReportController controller = loader.getController();

			//Sets stage and main to controller class
			controller.setDialog(dialogStage);
			controller.setMain(this);

			//Calculates footer labels
			controller.setInactiveMembers();
			controller.setActiveMembers();
			controller.setOwedMembers();
			controller.setTotalOwed();
			//Displays footer labels
			controller.setLabels();
			dialogStage.showAndWait();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Generates a senior report, which shows information of all seniors.
	 */
	public void showSeniorsReport(){
		try{
			// Load the fxml file and create a new stage for the seniors report
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/SeniorsReport.fxml"));
			seniorPage = (AnchorPane) loader.load();

			//Sets the stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Seniors Report"); //Sets title to Seniors Report
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(seniorPage);
			dialogStage.setScene(scene);

			// Load controller
			SeniorsReportController controller = loader.getController();
			// Set stage and main to controller class
			controller.setDialog(dialogStage);
			controller.setMain(this);
			dialogStage.showAndWait(); //Wait until report is closed
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates a help menu
	 */
	public void showHelpDialog(){
		// Load the fxml file and create a new stage for the seniors report
					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(Main.class.getResource("view/HelpDialog.fxml"));
						helpPage = (AnchorPane) loader.load();
						
						//Sets the stage
						Stage dialogStage = new Stage();
						dialogStage.setTitle("Help"); //Sets title to Seniors Report
						dialogStage.initModality(Modality.WINDOW_MODAL);
						dialogStage.initOwner(primaryStage);
						Scene scene = new Scene(helpPage);
						dialogStage.setScene(scene);

						dialogStage.showAndWait(); //Wait until report is closed
					} catch (IOException e) {
						e.printStackTrace();
					}
	}

	/**
	 * 
	 * @return the filepath of the person
	 */
	public File getPersonFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePath", null); //Creates string, sets to path
		if (filePath != null) { //Checks if path is null. If not, returns a file of string filePath. If so, returns nothing
			return new File(filePath);
		} else {
			return null;
		}
	}
	/**
	 * Saves the current person data to the specified file
	 * @param file
	 */
	public void setPersonFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if (file != null) { //Checks if file is null
			prefs.put("filePath", file.getPath()); //Sets filepath to the path specified
			primaryStage.setTitle("MembershipApp - " + file.getName()); //Sets file name to title with file name
		} else { //Removes filePath, sets title back to the title
			prefs.remove("filePath");
			primaryStage.setTitle("MembershipApp");
		}
	}

	/**
	 * Loads person data from the specified file. The current person data will be replaced
	 * @param file
	 */
	public void loadPersonDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(MemberListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling
			MemberListWrapper wrapper = (MemberListWrapper) um.unmarshal(file);

			memberData.clear();
			memberData.addAll(wrapper.getMembers());

			// Save the file path to the registry
			setPersonFilePath(file);

		} catch (Exception e) { // catches any exception
			Alert alert = new Alert(AlertType.ERROR); //creates an alert, warns user data could not be loaded
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * Saves the current person data to the specified file.
	 * @param file
	 */
	public void savePersonDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(MemberListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data
			MemberListWrapper wrapper = new MemberListWrapper();
			wrapper.setMembers(memberData);

			// Marshalling and saving XML to the file
			m.marshal(wrapper, file);

			// Save the file path to the registry
			setPersonFilePath(file);
		} catch (Exception e) { // catches any exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());
			alert.showAndWait();
		}
	}

	/**
	 * 
	 * @return the primary stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}