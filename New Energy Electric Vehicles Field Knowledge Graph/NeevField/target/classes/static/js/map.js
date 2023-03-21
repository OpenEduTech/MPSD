var map = new BMapGL.Map('map', {
    enableDblclickZoom: false,
    displayOptions: {
        building: false
    }
});
map.centerAndZoom(new BMapGL.Point(107.49878400303979,33.44109008071315), 8);
map.enableScrollWheelZoom(true);
var navi3DCtrl = new BMapGL.NavigationControl3D();  // 添加3D控件
map.addControl(navi3DCtrl);

function overlayer(geographyName) {
    var bd = new BMapGL.Boundary();
    bd.get(geographyName, function (rs) {
        // console.log('外轮廓：', rs.boundaries[0]);
        // console.log('内镂空：', rs.boundaries[1]);
        var hole = new BMapGL.Polygon(rs.boundaries, {
            fillColor: 'blue',
            fillOpacity: 0.2
        });
        map.addOverlay(hole);
        decoder(geographyName);
        gName(geographyName);
        contextMenu(geographyName);
    });
}

function decoder(geographyName) {
    //创建地址解析器实例
    var myGeo = new BMapGL.Geocoder();
    // 将地址解析结果显示在地图上，并调整地图视野
    myGeo.getPoint(geographyName, function(point){
        if(point){
            map.centerAndZoom(point, 8);
        }
    }, '中国');
}

function contextMenu(geographyName) {
    var menu = new BMapGL.ContextMenu();
    var txtMenuItem = [
        {
            text: '百度地图打开',
            callback: function () {
                window.open(`https://map.baidu.com/search/${geographyName}/@12649529,4105841.75,12z?querytype=s&da_src=shareurl&wd=${geographyName}&c=289&src=0&pn=0&sug=0&l=12&b=(13493505.31,3611546.64;13553025.31,3670682.64)&from=webmap&biz_forward=%7B%22scaler%22:2,%22styles%22:%22pl%22%7D&device_ratio=2`);
                //$(location).attr('href', `https://map.baidu.com/search/${geographyName}/@12649529,4105841.75,12z?querytype=s&da_src=shareurl&wd=${geographyName}&c=289&src=0&pn=0&sug=0&l=12&b=(13493505.31,3611546.64;13553025.31,3670682.64)&from=webmap&biz_forward=%7B%22scaler%22:2,%22styles%22:%22pl%22%7D&device_ratio=2`);
            }
        }
    ];
    for (var i = 0; i < txtMenuItem.length; i++) {
        menu.addItem(new BMapGL.MenuItem(txtMenuItem[i].text, txtMenuItem[i].callback, 100));
    }
    map.addContextMenu(menu);
}

function gName(geographyName) {
    //定义一个控件类
    function ZoomControl() {
        this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
        this.defaultOffset = new BMapGL.Size(20, 20)
    }
    //通过JavaScript的prototype属性继承于BMap.Control
    ZoomControl.prototype = new BMapGL.Control();

    //自定义控件必须实现自己的initialize方法，并且将控件的DOM元素返回
    //在本方法中创建个div元素作为控件的容器，并将其添加到地图容器中
    ZoomControl.prototype.initialize = function(map) {
        //创建一个dom元素
        var div = document.createElement('div');
        //添加文字说明
        div.appendChild(document.createTextNode(geographyName));
        // 设置样式
        div.style.cursor = "pointer";
        div.style.padding = "7px 10px";
        div.style.boxShadow = "0 2px 6px 0 rgba(27, 142, 236, 0.5)";
        div.style.borderRadius = "5px";
        div.style.backgroundColor = "white";
        // 添加DOM元素到地图中
        map.getContainer().appendChild(div);
        // 将DOM元素返回
        return div;
    }
    //创建控件元素
    var myZoomCtrl = new ZoomControl();
    //添加到地图中
    map.addControl(myZoomCtrl);
}

const closeMapDom = document.querySelector(".turn-off");
closeMapDom.addEventListener("click", function () {
    $(".geography-layer").css("height", "0px");
});