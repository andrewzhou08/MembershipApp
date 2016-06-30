package com.hhsfbla.membership.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.hhsfbla.membership.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SplashScreenController {

	private Main main;
	private Stage dialogStage;
	@FXML private TableView<File> recentFiles;
	@FXML private Label recent;
	String filename;

	/**
	 * Initializes the layout.
	 */
	public void initialize(){
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader("../recent.txt"));
			this.filename = br.readLine();
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		recent.setText(filename);
	}

	/**
	 * Sets main.
	 * @param main
	 */
	public void setMain(Main main){
		this.main = main;
	}

	/**
	 * Sets the stage.
	 * @param stage
	 */
	public void setStage(Stage stage){
		dialogStage = stage;
		this.dialogStage.setResizable(false);
	}

	/**
	 * Handles creating a new document.
	 */
	@FXML public void newDocument(){
		main.getPersonData().clear();
		main.setPersonFilePath(null);
		dialogStage.close();
	}

	/**
	 * Handles opening a document.
	 */
	@FXML public void openDocument(){
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(main.getPrimaryStage());

		if (file != null){
			main.loadPersonDataFromFile(file);
			saveToRecent(file.toString());
		}
		dialogStage.close();
	}

	/**
	 * Handles help.
	 */
	@FXML public void help(){
		main.showHelpDialog();
	}

	/**
	 * Handles exiting the program.
	 */
	@FXML public void exit(){
		System.exit(0);
	}

	/**
	 * Handles the about menu.
	 */
	@FXML public void about(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("MembershipApp");
		alert.setHeaderText("About");
		alert.setContentText("Author: Andrew Zhou\nDesktop Application for FBLA-PBL\nStores, edits, and deletes membership data.");
		alert.showAndWait();
	}

	/**
	 * Handles closing the screen.
	 */
	@FXML public void close(){
		dialogStage.close();
	}

	/**
	 * Handles saving to the recent so it is shown next time.
	 * @param filename
	 */
	public static void saveToRecent(String filename){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("../recent.txt"));
			
			int index = filename.lastIndexOf("/");
			String temp = filename.substring(index);
			
			bw.write(temp + "\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
