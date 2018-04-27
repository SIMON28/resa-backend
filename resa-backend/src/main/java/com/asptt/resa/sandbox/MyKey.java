package com.asptt.resa.sandbox;

public class MyKey {
	private int id;

	public MyKey(int id) {
		super();
		this.id = id;
	}

	@Override
	public int hashCode() {
		System.out.println("Calling hashCode()");
		return id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
