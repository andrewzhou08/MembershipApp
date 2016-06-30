package com.hhsfbla.membership.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import com.hhsfbla.membership.model.Member;

public class MemberEditDialogController {

	// FXML text fields for entering information into a new member
	@FXML private TextField memNumField;
	@FXML private TextField fNameField;
	@FXML private TextField lNameField;
	@FXML private ChoiceBox<String> gradeChoiceBox;
	@FXML private TextField schoolField;
	@FXML private TextField stateField;
	@FXML private TextField emailField;
	@FXML private TextField yrJoinedField;
	@FXML private ToggleButton activityField;
	@FXML private TextField amtOwedField;
	private ObservableList<String> grade = FXCollections.observableArrayList();
	private Stage dialogStage; // the stage for the dialog
	private Member member; //the new member being created
	private boolean okClicked = false; //whether or not ok was clicked
	private static String prevSchool = "";

	/**
	 * Initializes layout. Adds options to grade choicebox.
	 */
	@FXML private void initialize(){
		grade.add("9");
		grade.add("10");
		grade.add("11");
		grade.add("12");
		grade.add("Other");
		gradeChoiceBox.setItems(grade);
	}

	/**
	 * Code for checking if member active, then automatically activating/disactivating button based on this
	 */
	@FXML private void onActiveToggle(){
		String buttonText = activityField.getText(); //gets activity
		if(buttonText.equalsIgnoreCase("Toggled Active")) //checks if activity is active or inactive
			activityField.setText("Toggled Inactive");
		else
			activityField.setText("Toggled Active");
	}

	/**
	 * Sets the stage
	 * @param dialogStage
	 */
	public void setDialog(Stage dialogStage){
		this.dialogStage = dialogStage;
		this.dialogStage.setResizable(false);
		this.dialogStage.getIcons().add(new Image("file:resources/img/edit-icon.png")); //sets stage icon
	}

	/**
	 * Sets a member to a given member
	 * @param member
	 */
	public void setMember(Member member){ //Sets text fields to member's fields
		if(member.getIsNewMember()){
			schoolField.setText(prevSchool);
			member.setIsNewMember(false);
			this.member = member;
			activityField.setText("Toggled Active");
			}
		else{
			this.member = member;
			memNumField.setText(Integer.toString(member.getMembershipNumber()));
			fNameField.setText(member.getFirstName());
			lNameField.setText(member.getLastName());
			gradeChoiceBox.setValue(member.getGrade());
			schoolField.setText(member.getSchool());
			stateField.setText(member.getState());
			emailField.setText(member.getEmail());
			yrJoinedField.setText(Integer.toString(member.getYearJoined()));
			activityField.setSelected(!member.getActiveStatus());
			if(member.getActiveStatus())
				activityField.setText("Toggled Active");
			else
				activityField.setText("Toggled Inactive");
			amtOwedField.setText(Integer.toString(member.getAmountOwed()));
		}
	}

	/**
	 * 
	 * @return whether or not ok is clicked
	 */
	public boolean isOkClicked(){
		return okClicked;
	}

	/**
	 * When Ok is clicked, sets new member to entered values
	 */
	@FXML private void handleOk(){
		if(isInputValid()){ //makes sure input is valid
			// sets member's fields to given fields
			member.setMembershipNumber(Integer.parseInt(memNumField.getText()));
			member.setFirstName(fNameField.getText());
			member.setLastName(lNameField.getText());
			member.setGrade(gradeChoiceBox.getValue());
			member.setSchool(schoolField.getText());
			member.setState(stateField.getText());
			member.setEmail(emailField.getText());
			member.setYearJoined(Integer.parseInt(yrJoinedField.getText()));
			member.setActiveStatus(!activityField.isSelected());
			member.setAmountOwed(Integer.parseInt(amtOwedField.getText()));
			prevSchool = schoolField.getText();

			//sets okclicked to true and closes dialog
			okClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Closes dialog when cancel is clicked
	 */
	@FXML private void handleCancel(){
		dialogStage.close();
	}

	/**
	 * Tests if the input is valid
	 * @return true if input is valid, false otherwise
	 */
	private boolean isInputValid(){
		String errorMessage = "";

		//if membership number is not inputted or is not an integer, creates error
		if(memNumField == null || memNumField.getText().length() == 0)
			errorMessage += "No valid membership number!\n";
		else{
			try{
				Integer.parseInt(memNumField.getText()); //tests if field is integer
			}
			catch (NumberFormatException e){
				errorMessage += "No valid membership number (must be an integer)!\n";
			}
		}

		//tests if first name is inputted
		if(fNameField.getText() == null || fNameField.getText().length() == 0)
			errorMessage += "No valid first name!\n";

		//tests if last name is inputted
		if(lNameField.getText() == null || lNameField.getText().length() == 0)
			errorMessage += "No valid last name!\n";
		
		// tests if school is inputted
		if(schoolField.getText() == null || schoolField.getText().length() == 0)
			errorMessage += "No valid school!\n";
		
		//tests if grade is inputted
		if(gradeChoiceBox.getValue() == null || gradeChoiceBox.getValue().length() == 0)
			errorMessage += "No valid grade!\n";
		
		//tests if state is inputted
		if(stateField.getText() == null || stateField.getText().length() == 0)
			errorMessage += "No valid state!\n";

		//tests if email is inputted
		if(emailField.getText() == null || emailField.getText().length() == 0)
			errorMessage += "No valid email!\n";

		//tests if year joined is inputted
		if(yrJoinedField.getText() == null || yrJoinedField.getText().length() == 0)
			errorMessage += "No valid year joined!\n";
		else{
			try{
				Integer.parseInt(yrJoinedField.getText()); //tests if field is integer
			} catch (NumberFormatException e){
				errorMessage += "No valid year joined (must be an integer)!\n";
			}
		}

		//tests if amount owed is inputted and is an integer
		if(amtOwedField.getText() == null || amtOwedField.getText().length() == 0)
			errorMessage += "No valid amount owed!\n";
		else{
			try{
				Integer.parseInt(amtOwedField.getText());
			} catch(NumberFormatException e){
				errorMessage += "No valid amount owed (must be an integer)!\n";
			}
		}
		//if there is no error message, returns true
		if (errorMessage.length() == 0) {
			return true;
		} else { //if there is an error message, create alert with error messages printed
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}













