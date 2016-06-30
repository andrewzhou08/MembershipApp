package com.hhsfbla.membership;

import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.hhsfbla.membership.model.Member;
import com.hhsfbla.membership.model.MemberListWrapper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class BackupController {
	
	private ObservableList<Member> memberData;
	Main main;
	Date fileNameDate; //File name date for backup filenames
	
	/**
	 * Initializes and starts counter and save mechanism
	 */
	public void initialize(){
		//Creates new background Timeline to back the file up every 10 minutes
		Timeline t = new Timeline(new KeyFrame(Duration.minutes(10), new EventHandler<ActionEvent>() {

			//Handles what happens every 10 minutes
		    @Override
		    public void handle(ActionEvent event) {
		    	//Sets date for filename
		    	fileNameDate = new Date(System.currentTimeMillis());
		        File backup = new File("../../backups/backup " + fileNameDate + ".xml");
		        //Saves person to backup file if if's not null
		        if(backup != null){
		        	savePersonDataToFile(backup);
		        }
		    }
		}));
		//Sets timer to loop infinitely until program exit
		t.setCycleCount(Timeline.INDEFINITE);
		t.play();
	}
	
	/**
	 * Initializes ObservableList of members
	 * @param memberData
	 */
	public void setMemberData(ObservableList<Member> memberData){
		this.memberData = memberData;
	}
	
	/**
	 * Sets main
	 * @param main
	 */
	public void setMain(Main main){
		this.main = main;
	}
	
	/**
	 * Saves the person to the backup location encoded as an xml file
	 * @param file
	 */
	private void savePersonDataToFile(File file) {
		try {
			//Makes new JAXB Context, initializes Marshaller
			JAXBContext context = JAXBContext.newInstance(MemberListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our person data
			MemberListWrapper wrapper = new MemberListWrapper();
			wrapper.setMembers(memberData);

			// Marshalling and saving XML to the file
			m.marshal(wrapper, file);
			
		} catch (Exception e) { // catches any exception
			e.printStackTrace();
		}
	}
}
