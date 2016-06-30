package com.hhsfbla.membership.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;

import com.hhsfbla.membership.Main;
import com.hhsfbla.membership.model.Member;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MembersReportController {
	//Table of the report
	@FXML private TableView<Member> table;
	@FXML private TableColumn<Member, String> stateColumn;
	@FXML private TableColumn<Member, String> numberColumn;
	@FXML private TableColumn<Member, String> lNameColumn;
	@FXML private TableColumn<Member, String> fNameColumn;
	@FXML private TableColumn<Member, String> joinDateColumn;
	@FXML private TableColumn<Member, String> gradeColumn;
	@FXML private TableColumn<Member, String> amtOwedColumn;
	//Labels in the footer displaying information
	@FXML private Label numInactiveMembers;
	@FXML private Label numActiveMembers;
	@FXML private Label numOwedMembers;
	@FXML private Label totalOwed;
	private Main main; //the main
	private int inactiveMembers, activeMembers, owedMembers, numMembers; //fields to display in labels
	private double numOwed;
	private ObservableList<Member> members = FXCollections.observableArrayList(); //members list
	private Stage dialogStage; //the main stage
	private boolean cancelClicked; //if cancel is clicked

	/**
	 * Creates a new MembersReportController. Initializes everything to 0 and cancelClicked to false.
	 */
	public MembersReportController(){
		inactiveMembers = activeMembers = owedMembers = 0;
		numOwed = 0;
		cancelClicked = false;
	}

	/**
	 * Initializes the table.
	 */
	@FXML private void initialize(){
		//Initializes columns so that it displays correct information.
		stateColumn.setCellValueFactory(cellData -> cellData.getValue().getStateProperty());
		numberColumn.setCellValueFactory(cellData -> cellData.getValue().getMembershipNumberPropertyString());
		fNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());
		lNameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());
		joinDateColumn.setCellValueFactory(cellData -> cellData.getValue().getYearJoinedPropertyString());
		gradeColumn.setCellValueFactory(cellData -> cellData.getValue().getGradeProperty());
		amtOwedColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOwedPropertyString());
	}

	/**
	 * Sets main to the main, deletes members not owing a balance
	 * @param main
	 */
	public void setMain(Main main){
		this.main = main; //sets main to the main
		numMembers = main.getPersonData().size(); //calculates size of the data
		for(int i = 0; i < numMembers; i++){ //copies data to member to avoid passing by reference so members can be deleted
			members.add(main.getPersonData().get(i)); //without affecting the database
		}
		numMembers = members.size();
		for(int i = numMembers-1; i >= 0; i--){ //removes members from the new copied object
			if(members.get(i).getAmountOwed() == 0)
				members.remove(i);
		}
		table.setItems(members); //sets table to these members
	}

	/**
	 * Sets the stage
	 * @param dialogStage
	 */
	public void setDialog(Stage dialogStage){
		this.dialogStage = dialogStage;
		//Does not allow resizing
		this.dialogStage.setResizable(false);
		this.dialogStage.getIcons().add(new Image("file:resources/img/report-icon.png"));
	}

	/**
	 * Calculates inactive members
	 */
	public void setInactiveMembers(){
		for(int i = 0; i < main.getPersonData().size(); i++){
			if(!main.getPersonData().get(i).getActiveStatus())
				//if active status is "no", increase inactive members by one
				inactiveMembers++;
		}
	}

	/**
	 * Calculates active members
	 */
	public void setActiveMembers(){
		for(int i = 0; i < main.getPersonData().size(); i++){
			if(main.getPersonData().get(i).getActiveStatus())
				//if active status is "yes", increase active members by one
				activeMembers++;
		}
	}

	/**
	 * Calculates number of members that owe money
	 */
	public void setOwedMembers(){
		for(int i = 0; i < main.getPersonData().size(); i++){
			if(main.getPersonData().get(i).getAmountOwed() != 0)
				//if amount owed is not 0, increment
				owedMembers++;
		}
	}

	/**
	 * Calculates total amount owed
	 */
	public void setTotalOwed(){
		for(int i = 0; i < main.getPersonData().size(); i++)
			//adds amount owed of each member to numOwed
			numOwed += main.getPersonData().get(i).getAmountOwed();
	}

	/**
	 * Sets fields to display on footer labels
	 */
	public void setLabels(){
		//Displays all fields to labels
		numInactiveMembers.setText(Integer.toString(inactiveMembers));
		numActiveMembers.setText(Integer.toString(activeMembers));
		numOwedMembers.setText(Integer.toString(owedMembers));
		totalOwed.setText(Double.toString(numOwed));
	}

	/**
	 * Cancels program, closes
	 */
	@FXML private void handleCancel(){
		dialogStage.close(); //closes program
	}

	/**
	 * 
	 * @return if cancel is clicked
	 */
	public boolean isCancelClicked(){
		return cancelClicked;
	}

	/**
	 * Handles printing for report
	 */
	@FXML private void handlePrint(){
		//Takes the report page anchorpane from main and prints it
		AnchorPane page = Main.page;
		print(page);
	}

	/**
	 * Prints a given node.
	 * @param node the inputted node
	 */
	private void print(Node node) {
		System.out.println("Creating a printer job...");

		//Creates new printerjob, makes sure it isn't null. Then prints.
		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null) {
			System.out.println(job.jobStatusProperty().asString());

			boolean printed = job.printPage(node);
			if (printed)job.endJob(); //Tests for printing success/fail
			else System.out.println("Printing failed.");
		}
		else System.out.println("Could not create a printer job."); //Printerjob is null.
	}

	/**
	 * Handles exporting the table information as an excel .xls file. Export path is the desktop.
	 * @throws Exception
	 */
	@FXML public void handleExport() throws Exception {
		Date fileNameDate = new Date(System.currentTimeMillis());
		String filename = "../../output/report " + fileNameDate + ".xls";
		FileOutputStream out = null;
		try {
			//Creates new workbook to create the xls file in
			Workbook[] wbs = new Workbook[] { new XSSFWorkbook() };
			Workbook wb = wbs[0];
			CreationHelper createHelper = wb.getCreationHelper();
			// create a new sheet
			Sheet s = wb.createSheet();

			// Define a row with headers - title row of spreadsheet
			Row r = s.createRow(0);
			//Sets each cell to given value
			r.createCell(0).setCellValue(createHelper.createRichTextString("State"));
			r.createCell(1).setCellValue("Number");
			r.createCell(2).setCellValue("Last");
			r.createCell(3).setCellValue("First");
			r.createCell(4).setCellValue("Join Date");
			r.createCell(5).setCellValue("Grade");
			r.createCell(6).setCellValue("Amount Owed");

			int numRow = 1;
			for(Member member : members){
				Row row = s.createRow(numRow);
				//Fills rows of xls file with member data
				row.createCell(0).setCellValue(createHelper.createRichTextString(member.getState()));
				row.createCell(1).setCellValue(member.getMembershipNumber());
				row.createCell(2).setCellValue(member.getLastName());
				row.createCell(3).setCellValue(member.getFirstName());
				row.createCell(4).setCellValue(member.getYearJoined());
				row.createCell(5).setCellValue(member.getGrade());
				row.createCell(6).setCellValue(member.getAmountOwed());
				numRow++;
			}

			//Adds statistics to bottom of spreadsheet
			//Non-Active members
			Row r1 = s.createRow(numRow + 2);
			r1.createCell(0).setCellValue("Number of Non-Active Members: ");
			r1.createCell(1).setCellValue(inactiveMembers);

			//Active members
			Row r2 = s.createRow(numRow + 3);
			r2.createCell(0).setCellValue("Number of Active Members: ");
			r2.createCell(1).setCellValue(activeMembers);

			//Members owing
			Row r3 = s.createRow(numRow + 4);
			r3.createCell(0).setCellValue("Number of Members Owing: ");
			r3.createCell(1).setCellValue(owedMembers);

			//Amount owed
			Row r4 = s.createRow(numRow + 5);
			r4.createCell(0).setCellValue("Total Amount Owed: ");
			r4.createCell(1).setCellValue("$" + numOwed);



			//Writes to xls file
			out = new FileOutputStream(filename);
			wb.write(out);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			Alert alert = new Alert(AlertType.INFORMATION); //Creates success alert
			alert.setTitle("Success!");
			alert.setHeaderText("Export successful");
			alert.setContentText("Saved xls file to the \"output\" folder in project directory with file name: " + filename);
			alert.showAndWait();
		} 
	}
}
