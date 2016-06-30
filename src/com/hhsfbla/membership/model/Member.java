package com.hhsfbla.membership.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member {

	private final IntegerProperty memNum;
	private final StringProperty fName;
	private final StringProperty lName;
	private final StringProperty grade;
	private final StringProperty school;
	private final StringProperty state;
	private final StringProperty email;
	private final IntegerProperty yrJoined;
	private final BooleanProperty activity;
	private final IntegerProperty amtOwed;
	private boolean isNewMember;

	/**
	 * Creates a member with no number and a blank name
	 */
	public Member(){
		memNum = new SimpleIntegerProperty();
		fName = new SimpleStringProperty();
		lName = new SimpleStringProperty();

		//Dummy data to fill in blanks automatically
		grade = new SimpleStringProperty();
		school = new SimpleStringProperty();
		state = new SimpleStringProperty();
		email = new SimpleStringProperty();
		yrJoined = new SimpleIntegerProperty();
		activity = new SimpleBooleanProperty(false);
		amtOwed = new SimpleIntegerProperty();
		
		isNewMember = true;
	}

	public void setIsNewMember(boolean isNewMember){
		this.isNewMember = isNewMember;
	}
	
	public boolean getIsNewMember(){
		return isNewMember;
	}
	
	/**
	 * Sets the first name as something else
	 * @param firstName
	 */
	public void setFirstName(String firstName){
		fName.set(firstName);
	}

	/**
	 * 
	 * @return the first name of the member
	 */
	public String getFirstName(){
		return fName.get();
	}

	/**
	 * 
	 * @return the property of the first name of the member
	 */
	public StringProperty getFirstNameProperty(){
		return fName;
	}

	

	/**
	 * Sets the last name of the member to something else
	 * @param lastName
	 */
	public void setLastName(String lastName){
		lName.set(lastName);
	}

	/**
	 * 
	 * @return the last name of the member
	 */
	public String getLastName(){
		return lName.get();
	}

	/**
	 * 
	 * @return the last name property of the member
	 */
	public StringProperty getLastNameProperty(){
		return lName;
	}


	
	/**
	 * Sets the membership number of the member
	 * @param membershipNumber
	 */
	public void setMembershipNumber(int membershipNumber){
		memNum.set(membershipNumber);
	}

	/**
	 * 
	 * @return the membership number
	 */
	public int getMembershipNumber(){
		return memNum.get();
	}

	/**
	 * 
	 * @return the property of the membership number
	 */
	public IntegerProperty getMembershipNumberProperty(){
		return memNum;
	}

	/**
	 * 
	 * @return the string property of the membership number
	 */
	public StringProperty getMembershipNumberPropertyString(){
		return new SimpleStringProperty(Integer.toString(memNum.get()));
	}



	/**
	 * Sets the grade of the member
	 * @param grade
	 */
	public void setGrade(String grade){
		this.grade.set(grade);
	}

	/**
	 * 
	 * @return the member's grade
	 */
	public String getGrade(){
		return grade.get();
	}

	/**
	 * 
	 * @return the property of the member's grade
	 */
	public StringProperty getGradeProperty(){
		return grade;
	}



	/**
	 * Sets the member's school
	 * @param school
	 */
	public void setSchool(String school){
		this.school.set(school);
	}

	/**
	 * 
	 * @return the member's school
	 */
	public String getSchool(){
		return school.get();
	}

	/**
	 * 
	 * @return the property of the member's school
	 */
	public StringProperty getSchoolProperty(){
		return school;
	}



	/**
	 * Sets the member's state
	 * @param state
	 */
	public void setState(String state){
		this.state.set(state);
	}

	/**
	 * 
	 * @return the member's state
	 */
	public String getState(){
		return state.get();
	}

	/**
	 * 
	 * @return the property of the member's state
	 */
	public StringProperty getStateProperty(){
		return state;
	}



	/**
	 * Sets the member's email
	 * @param email
	 */
	public void setEmail(String email){
		this.email.set(email);
	}

	/**
	 * 
	 * @return the email of the member
	 */
	public String getEmail(){
		return email.get();
	}

	/**
	 * 
	 * @return the property of the email of the member
	 */
	public StringProperty getEmailProperty(){
		return email;
	}


	/**
	 * Sets the year joined of the member
	 * @param yearJoined
	 */
	public void setYearJoined(int yearJoined){
		yrJoined.set(yearJoined);
	}

	/**
	 * 
	 * @return the member's year joined
	 */
	public int getYearJoined(){
		return yrJoined.get();
	}

	/**
	 * 
	 * @return the property of the member's year joined
	 */
	public IntegerProperty getYearJoinedProperty(){
		return yrJoined;
	}

	/**
	 * 
	 * @return the property of the member's year joined as a string
	 */
	public StringProperty getYearJoinedPropertyString(){
		String yrJoinedString = Integer.toString(yrJoined.get());
		return new SimpleStringProperty(yrJoinedString);
	}



	/**
	 * Sets the member's activity status
	 * @param activity
	 */
	public void setActiveStatus(boolean activity){
		this.activity.set(activity);
	}

	/**
	 * 
	 * @return the member's activity status
	 */
	public boolean getActiveStatus(){
		return activity.get();
	}

	/**
	 * 
	 * @return the member's activity status' property
	 */
	public BooleanProperty getActiveStatusProperty(){
		return activity;
	}



	/**
	 * Sets the amount owed of the member
	 * @param amountOwed
	 */
	public void setAmountOwed(int amountOwed){
		amtOwed.set(amountOwed);
	}

	/**
	 * 
	 * @return the amount owed of the member
	 */
	public int getAmountOwed(){
		return amtOwed.get();
	}

	/**
	 * 
	 * @return the property of the amount owed of the member
	 */
	public IntegerProperty getAmountOwedProperty(){
		return amtOwed;
	}

	/**
	 * 
	 * @return the property of the amount owed of the member as a string
	 */
	public StringProperty getAmountOwedPropertyString(){
		String amtOwedString = "$" + Integer.toString(amtOwed.get());
		return new SimpleStringProperty(amtOwedString);
	}

}
