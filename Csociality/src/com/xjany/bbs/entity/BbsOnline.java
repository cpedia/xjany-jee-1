package com.xjany.bbs.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractBbsOnline;

/**
 * BbsOnline entity. @author LiXiang
 */
@Entity
@Table(name = "bbs_online", catalog = "cs_xjany")
public class BbsOnline extends AbstractBbsOnline implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public BbsOnline() {
	}

	/** minimal constructor */
	public BbsOnline(String onName, String onRole, String onIp,
			String onBrowser, String onLocate, Timestamp onTime) {
		super(onName, onRole, onIp, onBrowser, onLocate, onTime);
	}

	/** full constructor */
	public BbsOnline(String onName, String onRole, String onIp,
			String onBrowser, String onLocate, Timestamp onTime, Integer cmsDel) {
		super(onName, onRole, onIp, onBrowser, onLocate, onTime, cmsDel);
	}
	
	@Override
	public void recycle(boolean isRecycle) {
		// TODO Auto-generated method stub
		if(isRecycle)
			this.setCmsDel(1);
		else 
			this.setCmsDel(0);
	}
}
