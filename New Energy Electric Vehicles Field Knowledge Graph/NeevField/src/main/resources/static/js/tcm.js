function canvas(dom, file, path, item) {
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    myChart.showLoading();
    $.get(file, function (xml) {
        console.log
        myChart.hideLoading();
        var graph = echarts.dataTool.gexf.parse(xml);
        var categories = [];
        for (var i = 0; i < 9; i++) {
            categories[i] = {
                name: '类目' + i
            };
        }
        graph.nodes.forEach(function (node) {
            node.itemStyle = null;
            node.value = node.symbolSize;
            node.symbolSize /= 1.5;
            node.label = {
                normal: {
                    show: node.symbolSize > 10
                }
            };
            node.category = node.attributes.modularity_class;
        });
        option = {
            title: {
                text: '',
                subtext: '',
                top: 'bottom',
                left: 'right'
            },
            tooltip: {
                formatter: function (params, ticket, callback) {
                    if (params.dataType == "node") {
                        return params.name;
                    }else{
                        return '';
                    }
                }
            },
            legend: {
                show: false
            },
            animationDurationUpdate: 1500,
            animationEasingUpdate: 'quinticInOut',
            series: [
                {
                    name: '',
                    type: 'graph',
                    layout: 'circular',
                    circular: {
                        rotateLabel: true
                    },
                    data: graph.nodes,
                    links: graph.links,
                    categories: categories,
                    roam: false,
                    label: {
                        position: 'right',
                        formatter: '{b}'
                    },
                    lineStyle: {
                        color: 'source',
                        curveness: 0.3
                    }
                }
            ]
        };

        myChart.setOption(option);
        myChart.on('click', function(params){
            let dataType = params.dataType;
            if (dataType == "node"){
                $(location).attr('href', path);
                window.localStorage.setItem("item", item);
                window.localStorage.setItem("id", params.data.id);
            }
        });
    } , 'xml');;

    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
}

const companyDom = document.getElementById("company");
canvas(companyDom, '/file/company.gexf', "/tcm/info", "company");

const industryDom = document.getElementById("industry");
canvas(industryDom, '/file/industry.gexf', "/tcm/info", "industry");

const productDom = document.getElementById("product");
canvas(productDom, '/file/product.gexf', "/tcm/info", "product");

function recovery(gIndex) {
    let num = gIndex + 1;
    $(".zoom:nth-child("+num+")").css("padding", "0px");
    $(".zoom:nth-child("+num+") p").css("color", "#707070");
}
function expand(gIndex) {
    let num = gIndex + 1;
    $(".zoom:nth-child("+num+")").css("padding-top", "10px");
    $(".zoom:nth-child("+num+")").css("padding-left", "10px");
    $(".zoom:nth-child("+num+")").css("padding-right", "10px");
    $(".zoom:nth-child("+num+") p").css("color", "#000");
}
let move = 0;
function changeGraph(num) {
    let width = $("main").width();
    move = num * width;
    $("main").animate({
        scrollLeft: move
    }, 500);

    recovery(index);
    expand(num);
    index = num;

    if (num == 0){
        rightArrowDom.setAttribute("style", "display:block;");
        leftArrowDom.setAttribute("style", "display:none;");
    }else if (num == 4){
        rightArrowDom.setAttribute("style", "display:none;");
        leftArrowDom.setAttribute("style", "display:block;");
    }else {
        leftArrowDom.setAttribute("style", "display:block;");
        rightArrowDom.setAttribute("style", "display:block;");
    }
}
const leftArrowDom = document.querySelector(".arrow-left");
const rightArrowDom = document.querySelector(".arrow-right");
const navArrDom = document.querySelectorAll(".zoom");
let index = 0;
for (let i=0; i<navArrDom.length; i++){
    navArrDom[i].addEventListener("click", function () {
        changeGraph(i);
    })
}
leftArrowDom.addEventListener("click", function () {
    if (index != 0){
        changeGraph(index - 1);
    }
});
rightArrowDom.addEventListener("click", function () {
    if (index != 4){
        changeGraph(index + 1);
    }
});

//实现拖拽滚动
const boxDom = document.querySelector("main");
let mx = 0;
let my = 0;
let disx = 0;

function mouseMove(e){
    disx = mx - e.pageX;
    if (-200 < disx && disx < 200){
        if ((move + disx) > 0){
            boxDom.scroll(disx + move, 0);
        }
    }else if (disx > 200){
        boxDom.removeEventListener("mousemove", mouseMove);
        if (index != 4){
            changeGraph(index + 1);
        }
    }else if (disx < -200){
        boxDom.removeEventListener("mousemove", mouseMove);
        if (index != 0){
            changeGraph(index - 1);
        }
    }
}
boxDom.addEventListener("mousedown", function (e) {
    mx = e.pageX;
    my = e.pageY;
    boxDom.addEventListener("mousemove", mouseMove);
});
boxDom.addEventListener("mouseup", function (e) {
    if (-200 <= disx && disx <= 200){
        $("main").animate({
            scrollLeft: move
        }, 100);
    }
    boxDom.removeEventListener("mousemove", mouseMove);
});