package com.informationservice.exception;

public class BookInfoNotFoundException extends RuntimeException{
	public BookInfoNotFoundException(String id) {
		super("Could not find book info " + id);
	}
}
