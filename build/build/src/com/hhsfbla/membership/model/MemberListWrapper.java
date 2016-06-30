package com.hhsfbla.membership.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of members. This is used for saving the list of members to XML.
 */
@XmlRootElement(name = "members")
public class MemberListWrapper {
	private List<Member> members;

	@XmlElement(name = "member")
	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> persons) {
		this.members = persons;
	}
}
