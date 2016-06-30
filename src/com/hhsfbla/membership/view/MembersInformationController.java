package com.hhsfbla.membership.view;

import com.hhsfbla.membership.Main;
import com.hhsfbla.membership.model.Member;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;



public class MembersInformationController {

	//Table fields to output tables
	@FXML private TableView<Member> table;
	@FXML private TableColumn<Member, String> fNameColumn;
	@FXML private TableColumn<Member, String> lNameColumn;
	private ObservableList<Member> members;
	// Labels for show information on the left hand side
	@FXML private Label fNameLabel;
	@FXML private Label lNameLabel;
	@FXML private Label numLabel;
	@FXML private Label gradeLabel;
	@FXML private Label schoolLabel;
	@FXML private Label stateLabel;
	@FXML private Label emailLabel;
	@FXML private Label yrJoinedLabel;
	@FXML private Label activityLabel;
	@FXML private Label amtOwedLabel;
	@FXML private TextField filterField;
	private Main main; //Main class

	public MembersInformationController(){}

	/**
	 * Initializes table columns and table
	 */
	@FXML private void initialize(){
		//Initializes columns
		fNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());
		lNameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());
		//Does not show members' details at first
		showMemberDetails(null);
		table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showMemberDetails(newValue));
	}

	/**
	 * Sets main to the main so the controller can work with the main class
	 * @param main
	 */
	public void setMain(Main main){
		this.main = main;
		table.setItems(main.getPersonData());
	}

	/**
	 * Shows member details. If member is null, shows "not selected"
	 * @param member
	 */
	private void showMemberDetails(Member member){
		if(member == null){ //Shows not selected - member is null
			fNameLabel.setText("Not Selected");
			lNameLabel.setText("Not Selected");
			numLabel.setText("Not Selected");
			gradeLabel.setText("Not Selected");
			schoolLabel.setText("Not Selected");
			stateLabel.setText("Not Selected");
			emailLabel.setText("Not Selected");
			yrJoinedLabel.setText("Not Selected");
			activityLabel.setText("Not Selected");
			amtOwedLabel.setText("Not Selected");
		}
		else{ //Shows information of a member - member is not null
			fNameLabel.setText(member.getFirstName());
			lNameLabel.setText(member.getLastName());
			numLabel.setText(Integer.toString(member.getMembershipNumber()));
			gradeLabel.setText(member.getGrade());
			schoolLabel.setText(member.getSchool());
			stateLabel.setText(member.getState());
			emailLabel.setText(member.getEmail());
			yrJoinedLabel.setText(Integer.toString(member.getYearJoined()));
			if(member.getActiveStatus())
				activityLabel.setText(String.valueOf("Active"));
			else
				activityLabel.setText(String.valueOf("Inactive"));

			amtOwedLabel.setText(Integer.toString(member.getAmountOwed()));
		}
	}
	
	/**
	 * Activates filter, when user types in textfield, automatically filters.
	 */
	public void activateFilter() {
		members = main.getPersonData();
		//Wraps the ObservableList in a FilteredList
		FilteredList<Member> filteredData = new FilteredList<>(members, p -> true);

		//Sets the filter Predicate whenever the filter changes
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(person -> {
				//If filter text is empty, display all members
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				//Compare first name and last name of every person with filter text
				String lowerCaseFilter = newValue.toLowerCase();

				if (person.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
					return true; //Filter matches first name
				} else if (person.getLastName().toLowerCase().contains(lowerCaseFilter)) {
					return true; //Filter matches last name
				}
				return false; //Does not match
			});
		});

		//Wraps the FilteredList in a SortedList
		SortedList<Member> sortedData = new SortedList<>(filteredData);

		//Binds the SortedList comparator to the TableView comparator
		sortedData.comparatorProperty().bind(table.comparatorProperty());

		//Add sorted (and filtered) data to the table
		table.setItems(sortedData);
	}

	/**
	 * Handles deleting a person.
	 */
	@FXML protected void handleDeletePerson() {
		int selectedIndex = table.getSelectionModel().getSelectedIndex(); //Creates selection index
		
		if (selectedIndex >= 0) { //If selection index is valid, delete
			members.remove(selectedIndex);
		} else { //Nothing selected.
			Alert alert = new Alert(AlertType.WARNING); //Creates alert to warn user - not selected
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Person Selected");
			alert.setContentText("Please select a person in the table.");

			alert.showAndWait();
		}
	}

	/**
	 * Handles creating a new member.
	 */
	@FXML private void handleNewPerson() {
		Member member = new Member(); //create new member
		boolean okClicked = main.showPersonEditDialog(member); //opens person editing dialog
		if (okClicked) {
			main.getPersonData().add(member); //when ok is clicked adds member to list
		}
	}

	/**
	 * Handles the editing of a member.
	 */
	@FXML private void handleEditPerson() {
		Member member = table.getSelectionModel().getSelectedItem(); //gets selected member
		if (member != null) {
			boolean okClicked = main.showPersonEditDialog(member); //opens edit dialog with this member
			if (okClicked) {
				showMemberDetails(member); //shows member details when finished
			}

		} else { //nothing selected
			Alert alert = new Alert(AlertType.WARNING); //creates alert to warn user
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Person Selected");
			alert.setContentText("Please select a person in the table.");

			alert.showAndWait();
		}
	}

	/**
	 * Handles creating a report
	 */
	@FXML private void handleCreateReport(){
		main.showMembersReport(); //creates a report
	}

	/**
	 * Handles creating a seniors report
	 */
	@FXML private void handleCreateSeniorsReport(){
		main.showSeniorsReport(); //creates a senior report
	}
}
