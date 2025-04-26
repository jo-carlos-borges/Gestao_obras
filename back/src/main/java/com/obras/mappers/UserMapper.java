package com.obras.mappers;

import com.obras.domain.User;
import com.obras.records.UserRecordOuput;

public class UserMapper {
	
	private UserMapper() {
	
	}
	
	public static UserRecordOuput toRecord(User user) {
		return new UserRecordOuput(user.getId(), user.getName(), user.getCreationData());
	}
	
}