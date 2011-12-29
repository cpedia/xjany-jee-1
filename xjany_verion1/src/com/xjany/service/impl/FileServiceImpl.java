package com.xjany.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xjany.dao.FileDAO;
import com.xjany.entity.File;
import com.xjany.service.FileService;

@Service
@Transactional
public class FileServiceImpl implements FileService {

	private FileDAO fileDAO;

	@Resource
	public void setfileDAO(FileDAO fileDAO) {
		this.fileDAO = fileDAO;
	}

	@Override
	public void addFile(File file, int upId) {
		fileDAO.save(file, upId);
	}

	@Override
	public List<File> listFile() {
		return fileDAO.findAll();
	}

	@Override
	public File findById(int id) {
		return fileDAO.findById(id);
	}

	@Override
	public void updateFile(File file) {
		fileDAO.update(file);
	}

	@Override
	public void delFile(File file) {
		fileDAO.delete(file);
	}

	@Override
	public boolean checkFile(File file, List<File> propertyName, Object[] value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<File> findByUpId(int upId) {
		return fileDAO.findByUpId(upId);
	}

}
