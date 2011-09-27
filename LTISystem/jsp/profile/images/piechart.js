
//圆饼图对象

function VMLPie(pContainer,pWidth,pHeight,pCaption){
this.Container=pContainer;//容器
this.Width= pWidth;//宽
this.Height=pHeight;//高
this.Caption = pCaption;//标题
this.backgroundColor="";//背景颜色
this.Shadow=false;//阴影
this.BorderWidth=0;//边框
this.BorderColor=null;//边框颜色
this.all=new Array();//存储数据
this.RandColor=function(){ //随机颜色
return "rgb("+ parseInt( Math.random() * 255) +"," +parseInt( Math.random() * 255) +"," +parseInt( Math.random() * 

255)+")";
}
this.VMLObject=null;//
}


//画圆饼图
VMLPie.prototype.Draw=function(){

//画外框
   var o=document.createElement("v:group");
   o.style.width=this.Width;
   o.style.height=this.Height;
   o.coordsize="21600,21600";

//添加一个背景层
   var vRect=document.createElement("v:rect");
   vRect.style.width="21600px";//矩形宽--相对于o
   vRect.style.height="21600px";//矩形高--相对于o
   o.appendChild(vRect);

   var vCaption=document.createElement("v:textbox");
   vCaption.style.fontSize="24px"; //标题字体的大小
   vCaption.style.height="24px";//标题的高
   vCaption.style.fontWeight="bold";//标题字体--是否加粗
   vCaption.innerHTML=this.Caption;//标题的内容
   vCaption.style.textAlign="center";//标题的对齐方式
   vRect.appendChild(vCaption);
//设置边框大小
   if(this.BorderWidth){
    vRect.strokeweight=this.BorderWidth;
   }
//设置边框颜色
   if(this.BorderColor){
    vRect.strokecolor=this.BorderColor;
   }
//设置背景颜色
   if(this.backgroundColor){
    vRect.fillcolor=this.backgroundColor;
   }
//设置是否出现阴影
   if(this.Shadow){
    var vShadow=document.createElement("v:shadow");
    vShadow.on="t";
    vShadow.type="single";
    vShadow.color="graytext";
    vShadow.offset="4px,4px";
    vRect.appendChild(vShadow);
   }
   this.VMLObject=o;
   this.Container.appendChild(o);

//开始画内部园
   var oOval=document.createElement("v:oval");
   oOval.style.width="15000px";
   oOval.style.height="14000px";
   oOval.style.top="4000px";
   oOval.style.left="1000px";
   oOval.fillcolor="#d5dbfb";

   //本来计划加入3D的效果，后来感觉确实不好控制，就懒得动手了
   var formatStyle=' <v:fill   rotate="t" angle="-135" focus="100%" type="gradient"/>';
   formatStyle+='<o:extrusion v:ext="view" backdepth="1in" on="t" viewpoint="0,34.72222mm"   viewpointorigin="0,.5"  skewangle="90" lightposition="-50000"    lightposition2="50000" type="perspective"/>';
   oOval.innerHTML=formatStyle;

   o.appendChild(oOval);
   this.CreatePie(o);

}
VMLPie.prototype.CreatePie=function(vGroup){
   var mX=Math.pow(2,16) * 360;
   //这个参数是划图形的关键
   //AE x y width height startangle endangle
   //x y表示圆心位置
   //width height形状的大小
   //startangle endangle的计算方法如下
   // 2^16 * 度数

   var vTotal=0;
   var startAngle=0;
   var endAngle=0;
   var pieAngle=0;
   var prePieAngle=0;

   var objRow=null;
   var objCell=null;

   for(i=0;i<this.all.length;i++){
    vTotal+=this.all[i].Value;
   }

   var objLegendRect=document.createElement("v:rect");

   var objLegendTable=document.createElement("table");
   objLegendTable.cellPadding=0;
   objLegendTable.cellSpacing=3;
   objLegendTable.width="100%";
   with(objLegendRect){
    style.left="17000px";
    style.top="1800px";
    style.width="4000px";
    style.height="19000px";
    fillcolor="#e6e6e6";
    strokeweight="1px";  
   }
   objRow=objLegendTable.insertRow();
   objCell=objRow.insertCell();
   objCell.colSpan="2";
   objCell.style.backgroundColor="black";
   objCell.style.padding="5px";
   objCell.style.color="window";
   objCell.style.font="caption";
   objCell.innerText="总数:"+vTotal;


   var vTextbox=document.createElement("v:textbox");
   vTextbox.appendChild(objLegendTable);
   objLegendRect.appendChild(vTextbox);

   var vShadow=document.createElement("v:shadow");
   vShadow.on="t";
   vShadow.type="single";
   vShadow.color="graytext";
   vShadow.offset="2px,2px";
   objLegendRect.appendChild(vShadow);


   vGroup.appendChild(objLegendRect);


   var strAngle="";
   for(i=0;i<this.all.length;i++){ //顺序的划出各个饼图
    var vPieEl=document.createElement("v:shape");
    var vPieId=document.uniqueID;
    vPieEl.style.width="15000px";
    vPieEl.style.height="14000px";
    vPieEl.style.top="4000px";
    vPieEl.style.left="1000px";
    vPieEl.coordsize="1500,1400";
    vPieEl.strokecolor="white";  
    vPieEl.id=vPieId;
  
    pieAngle= this.all[i].Value / vTotal;
    startAngle+=prePieAngle;
    prePieAngle=pieAngle;
    endAngle=pieAngle;
  
  
  
    vPieEl.path="M 750 700 AE 750 700 750 700 " + parseInt(mX * startAngle) +" " + parseInt(mX * endAngle) +" xe";
    vPieEl.title=this.all[i].Name +"\n所占比例:"+ endAngle * 100 +"%\n详细描述:" +this.all[i].TooltipText;
  
    var objFill=document.createElement("v:fill");
    objFill.rotate="t";
    objFill.focus="100%";
    objFill.type="gradient";
    objFill.angle=parseInt( 360 * (startAngle + endAngle /2));
    vPieEl.appendChild(objFill);
  
    var objTextbox=document.createElement("v:textbox");
    objTextbox.border="1px solid black";
    objTextbox.innerHTML=this.all[i].Name +":" + this.all[i].Value;
    //vPieEl.appendChild(objTextbox);
  
    var vColor=this.RandColor();
    vPieEl.fillcolor=vColor; //设置颜色
    //开始画图例
    objRow=objLegendTable.insertRow();
    objRow.style.height="16px";
  
    var objColor=objRow.insertCell();//颜色标记
    objColor.style.backgroundColor=vColor;
    objColor.style.width="16px";
  
    objColor.PieElement=vPieId;
    objColor.attachEvent("onmouseover",LegendMouseOverEvent);
    objColor.attachEvent("onmouseout",LegendMouseOutEvent);
  
    objCell=objRow.insertCell();
    objCell.style.font="icon";
    objCell.style.padding="3px";
    objCell.innerText=this.all[i].Name +":"+this.all[i].Value ;
  
    vGroup.appendChild(vPieEl);
   }

}
VMLPie.prototype.Refresh=function(){

}
VMLPie.prototype.Zoom=function (iValue){
var vX=21600;
var vY=21600;
this.VMLObject.coordsize=parseInt(vX / iValue) +","+parseInt(vY /iValue);
}
VMLPie.prototype.AddData=function(sName,sValue,sTooltipText){

var oData=new Object();
oData.Name=sName;
oData.Value=sValue;
oData.TooltipText=sTooltipText;
var iCount=this.all.length;
this.all[iCount]=oData;

}
VMLPie.prototype.Clear=function(){
this.all.length=0;
}
function LegendMouseOverEvent(){

var eSrc=window.event.srcElement;
eSrc.border="1px solid black";
}
function LegendMouseOutEvent(){
var eSrc=window.event.srcElement;
eSrc.border="";
}