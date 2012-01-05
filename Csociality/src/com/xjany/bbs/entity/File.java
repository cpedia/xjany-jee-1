package com.xjany.bbs.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractFile;

/**
 * File entity. @author LiXiang
 */
@Entity
@Table(name = "file", catalog = "cs_xjany")
public class File extends AbstractFile implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public File() {
	}

	/** full constructor */
	public File(Integer downId, String name, String path, Integer upId,
			Integer cmsDel) {
		super(downId, name, path, upId, cmsDel);
	}

}
