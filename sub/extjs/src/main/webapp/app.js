Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext', '/assets/js/extjs');
Ext.require([
    'Ext.Msg',
    'Ext.panel.*'
]);


Ext.onReady(function(){

    console.log('i');

//    Ext.create('Ext.panel.Panel', {
//        renderTo: 'main2',
//        width: 100,
//        height: 100,
//        html: 'qwe',
//        title: 't'
//    });

    Ext.define('Game.test.TestClass', {
        extend:'Ext.panel.Panel',
        alias:'widget.test-class',
        field: "init field",
        method: function() {
            return 'ret';
        },
        initComponent: function() {
            Ext.define('User', {
                extend: 'Ext.data.Model',
                fields: ['id', 'name', 'email'],

                proxy: {
                    type: 'rest',
                    url : '/users'
                }
            });

            //get a reference to the User model class
            var User = Ext.ModelManager.getModel('User');
            //Uses the configured RestProxy to make a GET request to /users/123
            User.load(123, {
                success: function(user) {
                    console.log(user.getId()); //logs 123
                }
            });

            var user = Ext.create('User', {name: 'Ed Spencer', email: 'ed@sencha.com'});
            user.save(); //POST /users



            Ext.Ajax.request({
                url: '/spring/test/54a5b7c3e4b0d4685cd11f60',
                success : function(response, options) {
                    console.log("s");
                },
                failure : function(response, options) {
                    console.log("f");
                }
            });



            Ext.applyIf(this, {
                items : [{
                    xtype: 'panel',
                    html: 'h1000'
                }, {
                    xtype: 'panel',
                    html: 'h2000'
                }]
            });
            Game.test.TestClass.superclass.initComponent.call(this);
            console.log('init component 0000');
        }
    });

    var test1 = new Game.test.TestClass({
        renderTo: 'main'
    });

    var MyPanel = Ext.extend(Ext.panel.Panel, {
        alias: 'widget.MyPanel',
        initComponent: function() {
            Ext.applyIf(this, {
                items : [{
                    xtype: 'panel',
                    html: 'h1'
                }, {
                    xtype: 'panel',
                    html: 'h2'
                }]
            });
            MyPanel.superclass.initComponent.call(this);
            console.log('init component');
        }//,
//        constructor: function(config) {
//            console.log('in constructor');
//            this.callParent(config);
//            console.log('in constructor, after call to parent');
//
//        }
    });

    Ext.create('Ext.panel.Panel', {
        renderTo: 'main2',
        width: 100,
        height: 100,
//        html: 'qwe',
        items: {
            xtype: 'test-class'//,
//            html: 'as'
        },
        title: 't'
    });

//    var myPanel = new MyPanel({
//        b:2,
//        renderTo: 'main2'
//    });






//    Ext.create('Ext.container.Viewport', {
//        layout: 'border',
//        items: [{
//            region: 'center',
//            xtype: 'tabpanel', // TabPanel itself has no title
//            activeTab: 0,      // First tab active by default
//            items: {
//                title: 'Default Tab',
//                items: [{
//                    xtype: 'panel',
//                    html: 'p1'
//                }, {
//                    xtype: 'panel',
//                    html: 'p1'
//                }]
//            }
//        }]
//    });
});

