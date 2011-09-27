/*
 * Ext JS Library 2.0.2
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.onReady(function(){
    // shorthand
    var Tree = Ext.tree;
    
    var tree = new Tree.TreePanel({
        el:'tree-div',
        useArrows:true,
        autoScroll:true,
        animate:true,
        enableDD:false,
        containerScroll: true, 
        loader: new Tree.TreeLoader({
            dataUrl:'List.action?url=Save.action&para=ID'
        })
    });

    // set the root node
    var root = new Tree.AsyncTreeNode({
        text: 'LTI System',
        draggable:false,
        id:'source'
    });
    tree.setRootNode(root);

    // render the tree
    tree.render();
    root.expand();
 
});