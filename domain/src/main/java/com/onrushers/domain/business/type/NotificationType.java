package com.onrushers.domain.business.type;

public enum NotificationType {

	Comment,
	Rushed,
	Fan,
	Event,
	Unknown;

	public static NotificationType from(String name) {
		if ("comment".equalsIgnoreCase(name)) {
			return NotificationType.Comment;
		} else if ("rushed".equalsIgnoreCase(name)) {
			return NotificationType.Rushed;
		} else if ("fan".equalsIgnoreCase(name)) {
			return NotificationType.Fan;
		} else if ("event".equalsIgnoreCase(name)) {
			return NotificationType.Event;
		}
		return NotificationType.Unknown;
	}
}
