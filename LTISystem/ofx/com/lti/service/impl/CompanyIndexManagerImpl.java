package com.lti.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.edgar.EdgarUtil;
import com.lti.service.CompanyIndexManager;
import com.lti.service.bo.CompanyIndex;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.StringUtil;

public class CompanyIndexManagerImpl extends DAOManagerImpl implements CompanyIndexManager {

	private static final long serialVersionUID = 1L;

	@Override
	public void deleteByHQL(String string) {
		super.deleteByHQL(string);
	}

	@Override
	public CompanyIndex get(long id) {
		return (CompanyIndex) getHibernateTemplate().get(CompanyIndex.class, id);
	}

	@Override
	public CompanyIndex get(String filename) {
		// TODO Auto-generated method stub
		return (CompanyIndex) getHibernateTemplate().get(CompanyIndex.class, filename);
		
	}
	@Override
	public List<CompanyIndex> get(String name, String type) {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CompanyIndex.class);
		detachedCriteria.add(Restrictions.eq("CompanyName", name));
		detachedCriteria.add(Restrictions.eq("FormType", type));
		detachedCriteria.addOrder(Order.asc("DateFiled"));
		return super.findByCriteria(detachedCriteria);
		
	}

	private boolean checkType(String type) {
		if (type.startsWith("13F-HR") || type.startsWith("N-Q") || type.startsWith("10Q") || type.startsWith("10K")) {
			return true;
		} else
			return false;
	}
	@Override
	public List findBySQL(String sql)throws Exception{
		return super.findBySQL(sql);
	}

	@Override
	public void importCompanyIndex(String file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		int count = 1;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		List<CompanyIndex> cis = new ArrayList<CompanyIndex>();
		while (line != null) {
			if (count < 11) {
				line = br.readLine();
				count++;
				continue;
			}

			if (line.trim().length() > 98) {
				try {
					String companyName = line.substring(0, 61).trim();

					String formType = line.substring(62, 73).trim();
					long cik = Long.parseLong(line.substring(74, 85).trim());
					Date dateFiled = null;
					try {
						dateFiled = sdf1.parse(line.substring(86, 97).trim());
					} catch (Exception e) {
						dateFiled = sdf2.parse(line.substring(86, 97).trim());
					}
					String fileName = line.substring(98).trim();
					if (companyName.equals("") || !checkType(formType) || fileName.equals("")) {
						line = br.readLine();
						count++;
						continue;
					}
					//System.out.println(id);
					CompanyIndex ci = null;//this.get(fileName);
					if (ci == null) {
						ci = new CompanyIndex();
					}else{
						System.out.println(ci.getFileName());
						System.out.println(ci.getCompanyName());
						System.out.println(ci.getFormType());
					}
					ci.setCompanyName(companyName);
					//System.out.println(ci.getCompanyID());
					ci.setFormType(formType);
					ci.setCIK(cik);
					ci.setDateFiled(dateFiled);
					ci.setFileName(fileName);
					
					ci.setCount(count);

					cis.add(ci);
					
					EdgarUtil.downloadFile(EdgarUtil.Edgar_Server + ci.getFileName(), Configuration.get13FDir() + "/" + ci.getFileName());

				} catch (Exception e) {
					System.out.println(StringUtil.getStackTraceString(e));
					System.out.println("line:" + count);
				}
			}

			line = br.readLine();
			count++;
		}
		super.saveOrUpdateAll(cis);
	}

	@Override
	public void remove(long id) {
		Object obj = getHibernateTemplate().get(com.lti.service.bo.CompanyIndex.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public void save(CompanyIndex c) {
		getHibernateTemplate().save(c);
	}

	@Override
	public void saveOrUpdate(CompanyIndex c) {
		getHibernateTemplate().saveOrUpdate(c);
	}

	@Override
	public void update(CompanyIndex c) {
		getHibernateTemplate().update(c);
	}

	public static void main(String[] args) {
		CompanyIndexManagerImpl cim = (CompanyIndexManagerImpl) ContextHolder.getInstance().getApplicationContext().getBean("companyIndexManager");
		for (int i = 1993; i < 2010; i++) {
			for (int j = 1; j < 5; j++) {
				try {
					System.out.println(i + "year" + j + "quarter");
					cim.importCompanyIndex("e:\\company\\" + i + "_QTR" + j + ".idx");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<CompanyIndex> getAll() {
		// TODO Auto-generated method stub
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CompanyIndex.class);
		return super.findByCriteria(detachedCriteria);
	}

}
