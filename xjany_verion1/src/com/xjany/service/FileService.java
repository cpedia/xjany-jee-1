package com.xjany.service;

import java.util.List;

import com.xjany.entity.File;

public interface FileService
{
	public void addFile(File file,int upId);
	public List<File> listFile();
	public File findById(int id);
	public List<File> findByUpId(int id);
	public void updateFile(File file);
	public void delFile(File file);
	public boolean checkFile(File file, List<File> propertyName, Object[] value);
}
