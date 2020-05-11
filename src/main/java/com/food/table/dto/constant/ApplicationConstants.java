package com.food.table.dto.constant;

import java.util.Arrays;
import java.util.List;

public class ApplicationConstants {

	public static final List<String> services = Arrays.asList("Breakfast", "Lunch", "Dinner", "Cafe", "Nightlife");

	public static final List<String> seatings = Arrays.asList("Indoor", "Outdoor");

	public static final List<String> payments = Arrays.asList("Card", "Cash", "UPI", "Paytm", "Gpay", "Phonepe");

	public static final List<String> types = Arrays.asList("DineIn", "SelfService", "TakeAway");

	public static final String deleteState = "Deleted";

	public static final String draftState = "Draft";

	public static final String confirmedState = "Confirmed";

	public static final String MESSAGE_DATA_TYPE = "String";

	public static final String NOTIFICATION_TYPE = "notification_type";

	public static final String NO_TOPIC_FOUND = "No matching topic supported!";

	public static final String SNS_JSON_PARSE_EXCEPTION = "Invalid Input file to parse message";

	public static final String GCM_MSG_STRUCTURE = "json";

	public static final String TEXT = "text";

	public static final String NOTIFICATION = "notification";

	public static final String GCM = "GCM";

	public static final String CREATE_ENDPOINT_FAILED = "Create device end point failed";

	public static final String PUSH_NOTIFICATION_FAILED = "Push notification failed";

}
