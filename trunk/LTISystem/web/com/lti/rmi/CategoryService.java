package com.lti.rmi;

public interface CategoryService {
	public int newCategory(CategoryBean categoryBean);
	
	public boolean deleteCategory(int categoryId);
	
	public boolean updateCategory(CategoryBean categoryBean);
	
	public int getCategoryID(String categoryName);

}
