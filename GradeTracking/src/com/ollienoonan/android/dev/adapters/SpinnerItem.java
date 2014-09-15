package com.ollienoonan.android.dev.adapters;

/**
 * DataStructure for use with SpinnerItemAdapter, mainly for in android.spinners
 * Allows any object to be put in a spinner
 * 
 * @author Ollie Noonan
 * @version 1 - 2014/07/30
 * @param <T> the Object type to be stored
 */
public class SpinnerItem<T>{
	//the text to display when in a spinner
	private String smallTitle;
	
	//the text to display when selecting spinner item
	private String bigTitle;
	
	//the data represented by this SpinnerItem
	private T data;

	/**
	 * Contructs a new SpinnerItem
	 * 
	 * @param smallTitle the text to display when in a spinner
	 * @param bigTitle the text to display when selecting spinner item
	 * @param data the data represented by this SpinnerItem
	 */
	public SpinnerItem(String smallTitle, String bigTitle, T data) {
		this.smallTitle = smallTitle;
		this.bigTitle = bigTitle;
		this.data = data;
	}

	/**
	 * @return the smallTitle
	 */
	public String getSmallTitle() {
		return smallTitle;
	}

	/**
	 * @return the bigTitle
	 */
	public String getBigTitle() {
		return bigTitle;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}
}