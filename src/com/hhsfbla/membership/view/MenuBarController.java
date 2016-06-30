package com.hhsfbla.membership.view;

import java.io.File;

import com.hhsfbla.membership.Main;
import com.hhsfbla.membership.model.Member;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class MenuBarController{
	// Reference to the main application
	private Main main;
	private ObservableList<Member> members;
	private SplashScreenController scc;

	/**
	 * Sets main of class
	 * 
	 * @param mainApp
	 */
	public void setMainApp(Main main){
		this.main = main;
	}

	/**
	 * Creates an empty address book
	 */
	@FXML private void handleNew(){
		main.getPersonData().clear();
		main.setPersonFilePath(null);
	}

	/**
	 * Opens a FileChooser to let the user select an address book to load
	 */
	@FXML private void handleOpen(){
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(main.getPrimaryStage());

		if (file != null){
			main.loadPersonDataFromFile(file);
			SplashScreenController.saveToRecent(file.toString());
		}
	}

	/**
	 * Saves the file to the person file that is currently open. If there is no open file, the "save as" dialog is shown
	 */
	@FXML private void handleSave(){
		File personFile = main.getPersonFilePath();
		if (personFile != null){
			main.savePersonDataToFile(personFile);
			SplashScreenController.saveToRecent(personFile.toString());
		} else{
			handleSaveAs();
		}
	}

	/**
	 * Opens a FileChooser to let the user select a file to save to
	 */
	@FXML private void handleSaveAs(){
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(main.getPrimaryStage());

		if (file != null){
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".xml")){
				file = new File(file.getPath() + ".xml");
			}
			main.savePersonDataToFile(file);
			SplashScreenController.saveToRecent(file.toString());
		}
	}

	/**
	 * Opens an about dialog
	 */
	@FXML private void handleAbout(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("MembershipApp");
		alert.setHeaderText("About");
		alert.setContentText("Author: Andrew Zhou\nDesktop Application for FBLA-PBL\nStores, edits, and deletes membership data.");
		alert.showAndWait();
	}

	/**
	 * Opens a help dialog
	 */
	@FXML private void handleHelp(){
		main.showHelpDialog();
	}

	/**
	 * Deletes the last member from the list
	 */
	@FXML private void handleDelete(){
		members = main.getPersonData();
		int size = members.size();
		if(size > 0)
			members.remove(size - 1);
	}

	/**
	 * Adds a new member to the list
	 */
	@FXML private void handleAdd(){
		Member member = new Member();
		boolean okClicked = main.showPersonEditDialog(member);
		if (okClicked){
			main.getPersonData().add(member);
		}
	}

	/**
	 * Closes the application
	 */
	@FXML
	private void handleExit(){
		System.exit(0);
	}
}
