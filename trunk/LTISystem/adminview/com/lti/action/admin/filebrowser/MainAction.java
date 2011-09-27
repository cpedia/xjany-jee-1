package com.lti.action.admin.filebrowser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONArray;

import com.lti.action.Action;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 3413141341L;

	private String content;
	
	private String name;
	
	private Boolean nofck;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private String root;
	private File root_;
	private int root_length;
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		if(nofck == null)
			nofck = false;
	}
	
	public MainAction(){
		super();
		root=ContextHolder.getServletPath()+"/jsp/";
		root_=new File(root);
		root_length=root_.getAbsolutePath().length();
		
	}

	public String update() throws Exception {

		File out=new File(root+"/"+name);
		File dir=out.getParentFile();
		if(!dir.exists()||!dir.isDirectory()){
			dir.mkdirs();
		}
		if(!out.exists()){
			out.createNewFile();
		}
		FileWriter fw=new FileWriter(out);
		boolean isFTL=name.toLowerCase().endsWith(".uftl");
		if(isFTL){
			String s_pro=content.replaceAll("\\]\\s*<br />", "]\n");
			fw.write(s_pro);
		}else{
			fw.write(content);
		}
		
		fw.flush();
		fw.close();
		return "read";

	}
	
	public String read() throws Exception {
		File in=new File(root+"/"+name);
		if(!in.exists()){
			content="NO_FOUND_FILE!";
		}else{
			FileReader fr=new FileReader(in);
			BufferedReader br=new BufferedReader(fr);
			StringBuffer sb=new StringBuffer();
			String line=br.readLine();
			boolean isFTL = name.toLowerCase().endsWith(".uftl")&&!nofck;
			while(line!=null){
				sb.append(line);
				if(isFTL&&line.endsWith("]"))sb.append("<br />");
				sb.append("\n");
				line=br.readLine();
			}
			content=sb.toString();
			br.close();
			fr.close();
		}
		return "read";
	}
	
	public String delete() throws Exception{
		File in=new File(root+"/"+name);
		content="File not exists or delete failed!";
		if(in.exists()&&in.delete())content="File delete OK.";
		return "read";
	}
	
	public String list() throws Exception {
		FileList menu=this.getFileList();
        try {
        	JSONArray jsonObject = JSONArray.fromObject(menu);
        	content = jsonObject.toString();
        } catch (Exception e) {
        	e.printStackTrace();
        	content = "ERROR";
        }
		return "list";
	}

	
	private FileList getFileList(File f){
		if(f.isFile()){
			String filename=f.getName().toLowerCase();
			if(!filename.endsWith(".css")
					&&!filename.endsWith(".jsp")
					&&!filename.endsWith(".ftl")
					&&!filename.endsWith(".uftl")
					&&!filename.endsWith(".js")
					&&!filename.endsWith(".html")
					&&!filename.endsWith(".htm")
					&&!filename.endsWith(".xml")
					&&!filename.endsWith(".csv")){
				return null;
				
			}
		}
		
		FileList fl=new FileList();
		
		fl.setText(f.getName());
		//fl.setPath(f.getParent());
		
		
		if(f.isFile()){
			fl.setLeaf(true);
			fl.setCls("file");
			fl.setHref("javascript:read(\'"+f.getAbsolutePath().substring(root_length).replaceAll("\\\\", "/")+"\');");
			
		}else{
			File[] files=f.listFiles();
			fl.setLeaf(false);
			fl.setExpanded(false);
			fl.setCls("folder");
			if(files!=null){
				List<FileList> menus=new ArrayList<FileList>();
				for(int i=0;i<files.length;i++){
					FileList m=this.getFileList(files[i]);
					if(m!=null)menus.add(m);
				}
				menus = this.filesSorter(menus);
				if(menus.size()!=0){
					fl.setChildren(menus);
				}else{
					fl=null;
				}
			}
		}
		
		return fl;
	}
	
	public FileList getFileList(){

		
		
		FileList fl=new FileList();
		//fl.setPath("");
		
		fl.setLeaf(false);
		fl.setCls("folder");
		fl.setText("jsp");
		fl.setExpanded(true);
		File[] files=root_.listFiles();
		if(files!=null&&files.length>=1){
			List<FileList> fls=new ArrayList<FileList>();
			for(int i=0;i<files.length;i++){
				FileList m=this.getFileList(files[i]);
				if(m!=null)fls.add(m);
			}
			fls = this.filesSorter(fls);
			fl.setChildren(fls);
		}
		return fl;
	}

	public Boolean getNofck() {
		return nofck;
	}

	public void setNofck(Boolean nofck) {
		this.nofck = nofck;
	}
	
	public List<FileList> filesSorter(List<FileList> fls){
		Collections.sort(fls, new Comparator<FileList>() { 
			public int compare(FileList o1, 
					FileList o2) { 
				int result = o1.getCls().compareToIgnoreCase(o2.getCls()); 
				if(result<0)
					return 1;
				else if(result==0)
					return o1.getText().compareToIgnoreCase(o2.getText());
				return 0;
			} 
		}); 
		return fls;
	}

}
