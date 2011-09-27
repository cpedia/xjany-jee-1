Ext.onReady(function(){
    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
    var viewport = new Ext.Viewport({
        layout:'border',
        layoutConfig:document.body,
        items:[
	        {
		        region:'north',
		        height:80,
		        layout:'fit',
		        contentEl:"top"
	        },
	        {
	            region:'east',
	            title: 'Relevant News',
	            collapsible: true,
	            split:true,
	            width: 300,
	            minSize: 175,
	            maxSize: 400,
	            layout:'fit',
	            margins:'0 5 0 0',
	            contentEl:"news",
	            autoScroll:true

			},
			{
	            region:'center',
	            deferredRender:false,
	            layout:'fit',
	            title: 'Portfolio Chart',
	            contentEl:"flashcontent",
	            autoScroll:true
			}
        ]
	});
})