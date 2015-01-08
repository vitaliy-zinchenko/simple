<!DOCTYPE html>
<html>
<head>
    <title>ExtJS</title>

    <link rel="stylesheet" type="text/css" href="/ext/resources/css/ext-all.css" />
    <script type="text/javascript" src="/ext/ext-all.js"></script>
    <%--<script type="text/javascript" src="/app.js"></script>--%>
</head>
<body>

<style>
    #content{
        width:80%;
        height:400px;
        padding:10px;
        border:1px solid #000;
    }
    #tables{
        float:left;
        width:40%;
        height:100%;
        border:1px solid #AAA;
        background-color:rgba(222, 222, 222, 1.0);
    }
    #mainRoom{
        float:left;
        width:55%;
        height:100%;
        margin-left:15px;
        border:1px solid #AAA;
        background-color:rgba(222, 222, 222, 1.0);
    }
    .table{
        background-color:rgba(254, 108, 98, 1.0);
        border-radius:2px;
        border:1px solid gray;
        width:64px;
        height:64px;
        margin:10px;
        color:#FFF;
        cursor:pointer;
        text-align:right;
        display: inline-block;
    }
    .valid-zone{
        background-color:rgba(157, 229, 86, 1.0) !important;
    }
    .selected{
        opacity:0.5;
    }
</style>

<script>
    Ext.application({
        name: 'DnD',
        launch: function() {

            var overrides = {
                startDrag: function(e) {
                    //shortcut to access our element later
                    if (!this.el) {
                        this.el = Ext.get(this.getEl());
                    }
                    //add a css class to add some transparency to our div
                    this.el.addCls('selected');
                    //when we drop our item on an invalid place  we need to return it to its initial position
                    this.initialPosition = this.el.getXY();
                },
                onDrag: function(e) {
                    this.el.moveTo(e.getPageX() - 32, e.getPageY() - 32);
                },
                onDragEnter: function(e, id) {
                    Ext.fly(id).addCls('valid-zone');
                },
                onDragOver: function(e, id) {
                    Ext.fly(id).addCls('valid-zone');
                },
                onDragOut: function(e, id) {
                    console.log('onDragOut');
                },
                onDragDrop: function(e, id) {
                    // change the item position to absolute
                    this.el.dom.style.position = 'absolute';
                    //move the item to the mouse position
                    this.el.moveTo(e.getPageX() - 32, e.getPageY() - 32);
                    Ext.fly(id).removeCls('valid-zone');

                },
                onInvalidDrop: function() {
                    this.el.removeCls('valid-zone');
                    this.el.moveTo(this.initialPosition[0], this.initialPosition[1]);
                },
                endDrag: function(e, id) {
                    this.el.removeCls('selected');
                    //Ext.fly(id).removeCls('drop-target');
                    this.el.highlight();
                }
            };

            var tables = Ext.get('tables').select('div');
            Ext.each(tables.elements, function(el) {
                var dd = Ext.create('Ext.dd.DD', el, 'tablesDDGroup', {
                    isTarget: false
                });
                Ext.apply(dd, overrides);
            });

            var mainTarget = Ext.create('Ext.dd.DDTarget', 'mainRoom', 'tablesDDGroup', {
                ignoreSelf: false
            });
        }
    });
</script>

<div id="content">
    <div id="tables">
        <div class="table">1</div>
        <div class="table">2</div>
        <div class="table">3</div>
        <div class="table">4</div>

        <div class="table">5</div>
        <div class="table">6</div>
        <div class="table">7</div>
        <div class="table">8</div>

        <div class="table">9</div>
        <div class="table">10</div>
        <div class="table">11</div>
        <div class="table">12</div>
    </div>
    <div id="mainRoom"></div>
</div>

</body>
</html>