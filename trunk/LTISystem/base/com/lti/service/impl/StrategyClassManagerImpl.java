package com.lti.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lti.service.bo.StrategyClass;
import com.lti.type.Menu;


public class StrategyClassManagerImpl extends DAOManagerImpl implements com.lti.service.StrategyClassManager, Serializable{


	private static final long serialVersionUID = 1L;
	

	@Override
	public void remove(long id){
		Object obj = getHibernateTemplate().get(com.lti.service.bo.StrategyClass.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public StrategyClass get(Long id) {
		return (StrategyClass) getHibernateTemplate().get(StrategyClass.class, id);
	}

	
	@Override
	public Long save(StrategyClass strategyclass) {
		getHibernateTemplate().save(strategyclass);
		return strategyclass.getID();
	}

	
	@Override
	public void saveOrUpdate(StrategyClass strategyclass) {
		getHibernateTemplate().saveOrUpdate(strategyclass);
	}

	
	@Override
	public void update(StrategyClass strategyclass) {
		getHibernateTemplate().update(strategyclass);
	}

	
	@Override
	public void delete(long id) {
		
		Stack<StrategyClass> stack=new Stack<StrategyClass>();
		
		StrategyClass assetClass=this.get(id);
		
		if(assetClass==null)return;
		
		stack.push(this.get(id));
		
		while(!stack.empty()){
			
			StrategyClass top=stack.pop();
			
			List<StrategyClass> list=this.getChildClass(top.getID());
			
			if(list!=null&&list.size()!=0){
				
				stack.push(top);
				
				stack.addAll(list);
				
			}
			
			else{
				
				this.remove(top.getID());
				
			}
		}
	}
	
	@Override
	public Long add(StrategyClass strategyclass) {
		return this.save(strategyclass);
	}
	
	@Override
	public List<com.lti.service.bo.StrategyClass> getChildClass(long id) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(StrategyClass.class); 
		
		detachedCriteria.add(Restrictions.eq("ParentID", id));
		
		return findByCriteria(detachedCriteria);
		
	}

	
	
	@Override
	public com.lti.service.bo.StrategyClass getParent(long id) {
		
		StrategyClass self=this.get(id);
		
		StrategyClass parent=this.get(self.getParentID());
		
		return parent;
		
	}
	
	@Override
	public void addChildClass(long parentID, com.lti.service.bo.StrategyClass child) {
		
		StrategyClass entity=new StrategyClass();
		
		com.lti.util.CopyUtil.Translate(child, entity);
		
		entity.setParentID(parentID);
		
		this.save(entity);
		
	}

	
	@Override
	public com.lti.service.bo.StrategyClass get(String name) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(StrategyClass.class); 
		
		detachedCriteria.add(Restrictions.eq("Name", name));
		
		List<StrategyClass> acs= findByCriteria(detachedCriteria);
		
		if(acs.size()>=1)return acs.get(0);
		
		else return null;
	}
	
	@Override
	public StrategyClass getChildClass(String Name, long parent) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(StrategyClass.class); 
		
		detachedCriteria.add(Restrictions.eq("Name", Name));
		
		detachedCriteria.add(Restrictions.eq("ParentID", parent));
		
		List<StrategyClass> acs= findByCriteria(detachedCriteria);
		
		if(acs.size()>=1)return acs.get(0);
		
		else return null;
	}

	
	@Override
	public List<StrategyClass> getClasses() {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(StrategyClass.class); 
		
		List<StrategyClass> acs= findByCriteria(detachedCriteria);
		
		return acs;
	}
	
	@Override
	public List<String> getAbsoluteClassName(long id){
		
		if(id==0l)return null;
		
		List<String> returnList=new ArrayList<String>();
		
		Stack<String> stack=new Stack<String>();
		
		try {
			
			StrategyClass ac=this.get(id);
			if(ac==null)return null;
			
			stack.push(ac.getName());
			
			while(ac.getParentID()!=0){
				ac=this.get(ac.getParentID());
				//avoid circle
				if(stack.indexOf(ac.getName())!=-1)return null;
				stack.push(ac.getName());
			}
			
			String className="";
			while(!stack.empty()){
				className=stack.pop();
				returnList.add(className);
			}
			
			className=className.substring(0, className.length()-2);
			
			return returnList;
			
		} catch (RuntimeException e) {
			
			//e.printStackTrace();
			
			return null;
			
		}
	}
	private List<Integer> ids;
	
	private Menu getMenu(Integer id,String url,String para){
		Menu menu=new Menu();
		StrategyClass ac=this.get(id.longValue());
		if(ac==null)return null;
		
		if(ids.contains(ac.getID().intValue()))return null;
		else ids.add(ac.getID().intValue());
		
		menu.setText(ac.getName());
		menu.setId(id);
		menu.setHref(url+para+"="+menu.getId());
		List<StrategyClass> acs=this.getChildClass(id.longValue());
		if(acs==null||acs.size()==0){
			menu.setLeaf(true);
			menu.setCls("file");
			
		}else{
			List<Menu> menus=new ArrayList<Menu>();
			menu.setLeaf(false);
			menu.setExpanded(true);
			menu.setCls("folder");
			for(int i=0;i<acs.size();i++){
				Menu m=this.getMenu(acs.get(i).getID().intValue(),url,para);
				if(m!=null)menus.add(m);
			}
			menu.setChildren(menus);
		}
		
		return menu;
	}
	@Override
	public Menu getMenu(String url,String para){
		if(url==null&&url.length()<=1)return null;
		if(url.indexOf('?')!=-1)url=url+"&";
		else url=url+"?";
		
		
		ids=new ArrayList<Integer>();
		ids.add(0);
		Menu menu=new Menu();
		menu.setId(0);
		
		menu.setLeaf(false);
		menu.setCls("folder");
		menu.setText("Strategy Class");
		menu.setExpanded(true);
		List<StrategyClass> acs=this.getChildClass(0l);
		if(acs!=null){
			List<Menu> menus=new ArrayList<Menu>();
			for(int i=0;i<acs.size();i++){
				Menu m=this.getMenu(acs.get(i).getID().intValue(),url,para);
				if(m!=null)menus.add(m);
			}
			menu.setChildren(menus);
		}
		return menu;
	}
	
	@Override
	public Object[] getClassIDs(Long classid){
		if(classid==null)return null;
		StrategyClass cla=(StrategyClass) getHibernateTemplate().get(StrategyClass.class, classid);
		
		if(cla==null)return null;
		
		List<Long> IDList=new ArrayList<Long>();
		
		//IDList.add(classid);
		
		Queue<Long> IDQueue=new LinkedList<Long>();

		IDQueue.add(classid);
		
		while(!IDQueue.isEmpty()){
			Long ID=IDQueue.remove();
			if(!IDList.contains(ID))
			IDList.add(ID);

			List<StrategyClass> childClasses=findByHQL("from StrategyClass ac where ac.ParentID="+ID);
			
			if(childClasses!=null&&childClasses.size()>0){
			
				for(int i=0;i<childClasses.size();i++){
					if(!IDList.contains(childClasses.get(i).getID())&&!IDQueue.contains(childClasses.get(i).getID()))IDQueue.add(childClasses.get(i).getID());
				}
			
			}
		}
		
		Object[] IDs= IDList.toArray();
		return IDs;
	}

	
}
	