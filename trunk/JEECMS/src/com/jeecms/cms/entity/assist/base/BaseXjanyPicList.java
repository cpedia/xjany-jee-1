package com.jeecms.cms.entity.assist.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_friendlink table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_friendlink"
 */

public abstract class BaseXjanyPicList  implements Serializable {

	public static String PROP_NAME = "name";
	public static String PROP_PIC = "pic";
	public static String PROP_URL= "url";
	public static String PROP_ID = "id";


	// constructors
	public BaseXjanyPicList () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseXjanyPicList (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}


	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private java.lang.String url;
	public java.lang.String getUrl() {
		return url;
	}

	public void setUrl(java.lang.String url) {
		this.url = url;
	}

	public java.lang.String getPic() {
		return pic;
	}

	public void setPic(java.lang.String pic) {
		this.pic = pic;
	}



	private java.lang.String pic;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="friendlink_id"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: site_name
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: site_name
	 * @param name the site_name value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}



	public String toString () {
		return super.toString();
	}


}