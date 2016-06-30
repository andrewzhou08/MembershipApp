package com.hhsfbla.membership.view;

import java.io.FileOutputStream;
import java.util.Date;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hhsfbla.membership.Main;
import com.hhsfbla.membership.model.Member;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SeniorsReportController {
	//tables and columns to store report information
	@FXML private TableView<Member> tableSeniors;
	@FXML private TableColumn<Member, String> gradeColumn;
	@FXML private TableColumn<Member, String> stateColumn;
	@FXML private TableColumn<Member, String> lNameColumn;
	@FXML private TableColumn<Member, String> fNameColumn;
	@FXML private TableColumn<Member, String> emailColumn;
	private Main main; //the main
	private ObservableList<Member> members = FXCollections.observableArrayList(); //members list
	//fields to control report
	private int numMembers;
	private Stage dialogStage;
	private boolean cancelClicked;

	/**
	 * Initializes fields, cancelClicked is false
	 */
	public SeniorsReportController(){
		cancelClicked = false;
	}

	/**
	 * Initializes the table. Sets columns to the information they are supposed to hold
	 */
	@FXML private void initialize(){
		//Initializes table, sets column cell values to information properties given
		stateColumn.setCellValueFactory(cellData -> cellData.getValue().getStateProperty());
		gradeColumn.setCellValueFactory(cellData -> cellData.getValue().getGradeProperty());
		fNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());
		lNameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());
		emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());
	}

	/**
	 * Sets the main of the program and deletes all non-seniors
	 * @param main
	 */
	public void setMain(Main main){
		this.main = main; //sets main to the main
		numMembers = this.main.getPersonData().size(); //sets size to the number of members
		for(int i = 0; i < numMembers; i++){ //loops through, copies over members to avoid passing by reference
			members.add(this.main.getPersonData().get(i)); //so data is not directly affected
		}
		numMembers = members.size();
		for(int i = numMembers-1; i >= 0; i--){ //deletes all non-seniors, searches and removes all that are not of grade 12
			if(!members.get(i).getGrade().equals("12"))
				members.remove(i);
		}
		tableSeniors.setItems(members); //sets seniors to table
	}

	/**
	 * Sets the stage.
	 * @param dialogStage
	 */
	public void setDialog(Stage dialogStage){
		this.dialogStage = dialogStage;
		this.dialogStage.setResizable(false);
		this.dialogStage.getIcons().add(new Image("file:resources/img/report-icon.png")); //sets stage icon
	}

	/**
	 * Handles cancel.
	 */
	@FXML private void handleCancel(){
		dialogStage.close(); //closes dialog
		cancelClicked = true;
	}

	/**
	 * 
	 * @return whether or not cancel has been clicked.
	 */
	public boolean isCancelClicked(){
		return cancelClicked;
	}

	/**
	 * Handles printing the table.
	 */
	@FXML private void handlePrint(){
		//Takes the senior page anchorpane from main and prints it
		AnchorPane page = Main.seniorPage;
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
		FileOutputStream out = null;
		Date fileNameDate = new Date(System.currentTimeMillis());
		String filename = "../../output/report_senior " + fileNameDate + ".xls";
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
			r.createCell(0).setCellValue(createHelper.createRichTextString("Number"));
			r.createCell(1).setCellValue("Grade");
			r.createCell(2).setCellValue("State");
			r.createCell(3).setCellValue("Last");
			r.createCell(4).setCellValue("First");
			r.createCell(5).setCellValue("Email");

			int numRow = 1;
			for(Member member : members){
				Row row = s.createRow(numRow);
				//Fills rows of xls file with member data
				row.createCell(0).setCellValue(member.getMembershipNumber());
				row.createCell(1).setCellValue(member.getGrade());
				row.createCell(2).setCellValue(member.getState());
				row.createCell(3).setCellValue(member.getLastName());
				row.createCell(4).setCellValue(member.getFirstName());
				row.createCell(5).setCellValue(member.getEmail());
				numRow++;
			}
			
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
