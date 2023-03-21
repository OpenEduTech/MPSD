let item = window.localStorage.getItem("item");
let id = window.localStorage.getItem("id");
let path = "/" + item + "/" + id;
let dom = document.querySelector("#container");
let myChart = echarts.init(dom);
let graphData;
let append = false;//是否为追加模式
let cube = false;//是否为3D模式
let update = false;//图数据是否刷新
let fix = true;
let gData;
let colors = ['#d74848', '#68b344', '#c74ca6', '#cb9c32', '#5470c6'];
getData(path);

const appendModeDom = document.querySelector(".append_mode");
const updateModeDom = document.querySelector(".update_mode");
const fixModeDom = document.querySelector(".fix_mode");
appendModeDom.addEventListener("click", function () {
    appendModeDom.classList.add("select_mode");
    updateModeDom.classList.remove("select_mode");
    fixModeDom.classList.remove("select_mode");
    append = true;
    fix = false;
});
updateModeDom.addEventListener("click", function () {
    updateModeDom.classList.add("select_mode");
    appendModeDom.classList.remove("select_mode");
    fixModeDom.classList.remove("select_mode");
    append = false;
    fix = false;
});
fixModeDom.addEventListener("click", function () {
    fix = true;
    fixModeDom.classList.add("select_mode");
    appendModeDom.classList.remove("select_mode");
    updateModeDom.classList.remove("select_mode");
})

const changModeDom = document.querySelector("#change_mode");
const cubeContainerDom = document.querySelector("#container_3d");
changModeDom.addEventListener("click", function () {
    if (cube){//切换为2D
        $("#change_mode").css("background-image", 'url("/images/3d.svg")');
        $("#container_3d").css("width", "0px");
        $("#container_3d").css("height", "0px");
        $("#category_3d").css("display", "none");

        $("#info").css("background-color", "#fff");
        $("#info").removeClass("info3d");
        $("#info").addClass("info2d");
        $("main").css("background-color", "#fff");

        cube = false;
        update = false;
    }else {//切换为3D
        $("#change_mode").css("background-image", 'url("/images/2d.svg")');
        $("#container_3d").css("width", "100%");
        $("#container_3d").css("height", "100%");
        $("#category_3d").css("display", "block");

        $("#info").css("background-color", "#000011");
        $("#info").removeClass("info2d");
        $("#info").addClass("info3d");
        $("main").css("background-color", "#000011");

        cube = true;
        if (update){
            let gData = changeData3D(graphData);
            graph3d(gData, 3);
        }
    }
})

function loading3D(tip) {
    $("#loading_page").css("display", "block");
    $(".loading_tip").text(tip);
}

function hiddenLoading3D() {
    $("#loading_page").css("display", "none");
}

function getData(sendPath) {
    $.ajax({
        url: sendPath,
        type: "GET",
        dataType: "json",
        success: function (data) {
            update = true;
            updateInfo(data.item, data.info);
            if (append){
                appendData(data.graphData);
            }else {
                graphData = data.graphData;
                graph();
            }
            gData = changeData3D(graphData);
            graph3d(gData, 3);
        }
    });
}

function addData(data) {
    for (let d of data.nodes){
        if (whetherInsertNode(d, graphData)){
            graphData.nodes.push(d);
        }
    }
    for (let e of data.links){
        graphData.links.push(e);
    }
}

function appendData(data) {
    addData(data);
    myChart.hideLoading();
    myChart.setOption({
        series: [
            {
                roam: true,
                data: graphData.nodes,
                edges: graphData.links
            }
        ]
    });
}

function whetherInsertNode(node, gd) {
    for (let g of gd.nodes){
        if (node.id == g.id){
            return false;
        }
    }
    return true;
}

function Node3D(id, name, group) {
    this.id = id;
    this.name = name;
    this.group = group;
}

function Edge3D(source, target, val) {
    this.source = source;
    this.target = target;
    this.value = 2;
    this.val = val;
}

function GraphData3D(nodes, links) {
    this.nodes = nodes;
    this.links = links;
}

function changeData3D(data) {
    let nodes = data.nodes;
    let links = data.links;
    let node3ds = new Array();
    let link3ds = new Array();
    for (let node of nodes){
        node3ds.push(new Node3D(node.id, node.name, node.category));
    }
    for (let link of links){
        link3ds.push(new Edge3D(link.source, link.target, getRelationByChar(link.source, link.target)));
    }
    let data3d = new GraphData3D(node3ds, link3ds);
    return data3d;
}

function getCategoryName(category) {
    let categoryName = "";
    switch (category) {
        case 0:
            categoryName = "药剂";
            break;
        case 1:
            categoryName = "草药";
            break;
        case 2:
            categoryName = "病症";
            break;
        case 3:
            categoryName = "典籍";
            break;
        case 4:
            categoryName = "地理位置";
            break;
    }
    return categoryName;
}

function getEnglishCategoryName(category) {
    let categoryName = "";
    switch (category) {
        case 0:
            categoryName = "prescription";
            break;
        case 1:
            categoryName = "herb";
            break;
        case 2:
            categoryName = "disease";
            break;
        case 3:
            categoryName = "book";
            break;
        case 4:
            categoryName = "geography";
            break;
    }
    return categoryName;
}

function getEnglishCategoryNameByChar(category) {
    let categoryName = "";
    switch (category) {
        case "p":
            categoryName = "prescription";
            break;
        case "h":
            categoryName = "herb";
            break;
        case "d":
            categoryName = "disease";
            break;
        case "b":
            categoryName = "book";
            break;
        case "g":
            categoryName = "geography";
            break;
        case "hi":
            categoryName = "herbInfo";
            break;
    }
    return categoryName;
}

function getNodeByNodeId(data, nodeId) {
    for (let i=0; i<data.length; i++){
        if (data[i].id == nodeId){
            return data[i];
        }
    }
}

function getRelation(source, target, tip) {
    let txt = "";
    if (tip){
        txt = "关系：";
    }
    if (source.category == 0 && target.category == 3){
        return txt + "收录";
    }else if (source.category == 3 && target.category == 1){
        return txt + "成分";
    }else if (source.category == 1 && target.category == 3){
        return txt + "收录";
    }else if (source.category == 3 && target.category == 2){
        return txt + "治疗";
    }else if (source.category == 3 && target.category == 4){
        return txt + "位置";
    }else if (source.category == 1 && target.category == 0){
        return txt + "构成";
    }else if (source.category == 3 && target.category == 0){
        return txt + "收录";
    }else if (source.category == 1 && target.category == 1){
        return txt + "别称";
    }else if (source.category == 1 && target.category == 2){
        return txt + "治疗";
    }else if (source.category == 1 && target.category == 4){
        return txt + "位置";
    }
}

function getRelationByChar(source, target) {
    let s = source.substring(0,1);
    let t = target.substring(0,1);
    if (s == "p" && t == "b"){
        return "收录";
    }else if (s == "b" && t == "h"){
        return "成分";
    }else if (s == "h" && t == "b"){
        return "收录";
    }else if (s == "b" && t == "d"){
        return "治疗";
    }else if (s == "b" && t == "g"){
        return "位置";
    }else if (s == "h" && t == "p"){
        return "构成";
    }else if (s == "b" && t == "p"){
        return "收录";
    }else if (s == "h" && t == "h"){
        return "别称";
    }else if (s == "h" && t == "d"){
        return "治疗";
    }else if (s == "h" && t == "g"){
        return "位置";
    }
}

function graph(){
    myChart.hideLoading();
    let graph = graphData;
    graph.nodes.forEach(function (node) {
        node.itemStyle = null;
        node.symbolSize = 18;
        node.value = node.symbolSize;
        node.x = node.y = null;
        node.draggable = true;
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
                    let category = params.data.category;
                    let categoryName = getCategoryName(category);
                    let name = params.name;
                    let tip = `<div class="hidden-text">
                                    <span class="point" style="background: ${colors[category]}"></span>
                                    <span class="tip-text">${categoryName}：${name}</span>
                               </div>`
                    return tip;
                }else if (params.dataType == "edge"){
                    let sourceNode = getNodeByNodeId(graph.nodes, params.data.source);
                    let targetNode = getNodeByNodeId(graph.nodes, params.data.target);
                    let relation = getRelation(sourceNode, targetNode, true);
                    return relation;
                }
            }
        },
        // toolbox: {
        //     feature: {
        //         saveAsImage: {
        //             width: 30,
        //             height: 30
        //         }
        //     },
        //     right: 50,
        //     bottom: 10
        // },
        color: colors,
        legend: [
            {
                type: 'scroll',
                orient: 'vertical',
                right: 10,
                top: 20,
                bottom: 20,
                selectedMode: false,
                textStyle:{
                    fontSize: 16,
                    lineHeight: 18
                },
                data: graph.categories.map(function (a) {
                    return a.name;
                })
            }
        ],
        backgroundColor: '#fff',
        animation: false,
        series : [
            {
                name: '',
                type: 'graph',
                layout: 'force',
                data: graph.nodes,
                edges: graph.links,
                categories: graph.categories,
                animation: false,
                roam: true,
                zoom: 2,
                label: {
                    show: true,
                    position: 'right',
                    fontStyle: 'normal',
                    fontSize: 16
                },
                force: {
                    repulsion: 150
                },
                lineStyle:{
                    width: 1
                },
                force: {
                    repulsion: 200//节点之间的斥力因子。值越大则斥力越大
                }
            }
        ]
    };

    myChart.setOption(option, false);
    myChart.on('click', function (params) {
        if(params.dataType === 'node'){
            let pojoItem = params.data.id.substring(0, 1);
            if (pojoItem == "g"){
                let geographyName = params.data.name;
                overlayer(geographyName);
                $(".geography-layer").css("height", "100%");
            }else {
                if(!fix){
                    myChart.showLoading();
                    let nId = params.data.id.match(/[0-9]+/g);
                    let itemChar = params.data.id.match(/[a-z]+/ig);
                    let item = getEnglishCategoryNameByChar(itemChar[0]);
                    let sendPath = "/" + item + "/" + nId;
                    getData(sendPath);
                }
            }
        }
    });
}

function findNodeGroupByTarget(nodes, target) {
    for (let node of nodes){
        if (node.id == target){
            return node.group;
        }
    }
}

let clickNode = null;
let Graph;
function graph3d(gData, idx){
    //hiddenLoading3D();
    const elem = document.getElementById('container_3d');
    Graph = ForceGraph3D()
    (elem)
        .nodeLabel(node => {
            let type = getCategoryName(node.group);
            return type + "：" + node.name;
        })
        .linkLabel((link)=>{//鼠标移上连线展示信息
            let label = "关系：" + link.val;
            return label;
        })
        .nodeColor(node => {
            return colors[node.group]
        })
        // .nodeAutoColorBy('group')
        .linkAutoColorBy(d => {
            return findNodeGroupByTarget(gData.nodes, d.target);
        })
        .graphData(gData)
        // .backgroundColor('rgba(255,255,255,1.0)')
        .nodeThreeObject(node=>{
            return this.addSpriteText(node);
        })
        .nodeThreeObjectExtend((node=>{
            return true
        }))
        .linkThreeObjectExtend(true)
        .linkWidth(0.75)
        // .linkThreeObject(link => {
        //     const sprite = new SpriteText(`${link.val}`);
        //     sprite.color = link.color;
        //     sprite.textHeight = 2.5;
        //     return sprite;
        // })
        // .linkPositionUpdate((sprite, { start, end }) => {
        //     const middlePos = Object.assign(...['x', 'y', 'z'].map(c => ({
        //         [c]: start[c] + (end[c] - start[c]) / 2 // calc middle point
        //     })));
        //     Object.assign(sprite.position, middlePos);
        // })
        .width($("#container").width())
        .height($("#container").height())
        .onNodeHover(node => elem.style.cursor = node ? 'pointer' : null)
        .onNodeClick(node => {
            moveCamera(node);
            clickNode = node;
            let pojoItem = node.id.substring(0,1);
            if (pojoItem == "g"){
                let geographyName = node.name;
                overlayer(geographyName);
                $(".geography-layer").css("height", "100%");
            }else {
                if (!fix){//如果不是固定模式
                    //loading3D("数据获取中");
                    let nId = node.id.match(/[0-9]+/g);
                    let itemChar = node.id.match(/[a-z]+/ig);
                    let item = getEnglishCategoryNameByChar(itemChar[0]);
                    let sendPath = "/" + item + "/" + nId;
                    getData(sendPath);
                }
            }
        });
    Graph.numDimensions(idx);
    Graph.d3Force('charge').strength(-100);
}

function find3DNodeBySId(sid) {
    if (Graph && gData){
        for (let node of gData.nodes) {
            if (node.id == sid){
                return node;
            }
        }
    }
    return null;
}

function moveCamera(node) {
    if (node && Graph){
        const distance = 40;
        const distRatio = 1 + distance/Math.hypot(node.x, node.y, node.z);
        Graph.cameraPosition(
            { x: node.x * distRatio, y: node.y * distRatio, z: node.z * distRatio }, // new position
            node, // lookAt ({ x, y, z })
            3000  // ms transition duration
        );
    }
}

function addSpriteText(node){
    const sprite = new SpriteText(node.name);
    sprite.color = '#fff';
    sprite.textHeight = 3;
    sprite.position.set(0,12,0);
    return sprite;
}

function updateInfo(item, data){
    switch (item) {
        case "prescription":
            prescriptionInfo(data);
            break;
        case "book":
            diseaseAndBookInfo(data, "book");
            break;
        case "herb":
            herbInfo(data);
            break;
        case "herbInfo":
            oneHerbInfo(data);
            break;
        case "disease":
            diseaseAndBookInfo(data, "disease");
    }
}

function diseaseAndBookInfo(data, type) {
    let content = ``;
    if (type == 'book'){
        content = `<div>
                        <div class="title">典籍名称</div>
                        <div class="content location" data-sid="b${data.bookId}">${data.bookName}<span title="报错" class="error" data-type="book" data-id="${data.bookId}"></span></div>
                   </div>`;
    }else {
        content = `<div>
                        <div class="title">病症名称</div>
                        <div class="content location" data-sid="d${data.diseaseId}">${data.diseaseName}<span title="报错" class="error" data-type="disease" data-id="${data.diseaseId}"></span></div>
                   </div>`;
    }
    let herbInfoList = data.herbInfoList;
    let herbCount = 0;
    let herbPage = 0;
    if (herbInfoList.length > 0){
        content = content + `<div class="book_herb"><div class="herb_groups">草药（共${herbInfoList.length}种）</div><div class="groups_herb">`;
    }else {
        content = content + `<div class="book_herb"><div class="groups_herb">`;
    }
    for (let herbInfo of herbInfoList){
        if (herbCount == 0){
            content = content + `<div class="group_herb">`;
            herbPage = herbPage + 1;
        }
        content = content + herbInfoHtml(herbInfo, herbInfo.herbName);
        herbCount = herbCount + 1;
        if (herbCount == 10){
            content = content + `</div>`;
            herbCount = 0;
        }
    }
    if (herbCount % 10 != 0){
        content = content + `</div>`;
    }
    if (herbInfoList.length > 10){
        content = content + `</div><div class="arrow-left-herb"></div><div class="herb-page"><span class="herb-now-page">1</span>/${herbPage}</div><div class="arrow-right-herb"></div></div>`;
    }else {
        content = content + `</div></div>`;
    }

    let prescriptionInfoList = data.prescriptionInfoList;
    let prescriptionMap = computerSimplyPrescription(prescriptionInfoList);

    let count = 0;
    let prescriptionPage = 0;
    if (prescriptionMap.size > 0){
        content = content + `<div class="book_prescription"><div class="prescription_groups">药剂（共${prescriptionMap.size}剂）</div><div class="groups_pre">`;
    }else {
        content = content + `<div class="book_prescription"><div class="groups_pre">`;
    }

    for (let [prescriptionId, prescription] of prescriptionMap){
        if (count == 0){
            content = content + `<div class="group_pre">`;
            prescriptionPage = prescriptionPage + 1;
        }
        content = content + `<div class="pre_index" data-type="prescription" data-id="${prescriptionId}" data-sid="p${prescriptionId}">
                                <div class="name">${prescription.prescriptionName}</div>
                                <span class="error_icon" title="纠错"></span>
                                <span class="spr_icon"></span>
                            </div>
                            <div class="pre">`;

        let preInfoList = prescription.prescriptionInfoList;
        if (preInfoList.length == 1){
            let pInfo = preInfoList[0];
            content = content + prescriptionInfoHtml(pInfo);
        }else{
            for (let i = 0; i < preInfoList.length; i++) {
                let pInfo = preInfoList[i];
                content = content + prescriptionHtml(pInfo, `（${(i+1)}）${pInfo.prescriptionName}`);
            }
        }
        content = content + `</div>`;
        count = count + 1;
        if (count == 10){
            content = content + `</div>`;
            count = 0;
        }
    }
    if (count % 10 != 0){
        content = content + `</div>`;
    }
    if (prescriptionMap.size > 10){
        content = content + `</div><div class="arrow-left"></div><div class="prescription-page"><span class="pre-now-page">1</span>/${prescriptionPage}</div><div class="arrow-right"></div></div>`;
    }else {
        content = content + `</div></div>`;
    }

    $("#info").html(content);
    fold();
    preIndex = 0;
    herbIndex = 0;
    moveScrollBar();//必须写在fold()之后
}

function oneHerbInfo(data) {
    let content = ``;
    let herbName = data.herbName;
    for (let attr in data){
        let chineseName = field.get(attr);
        if (chineseName){
            let describe = data[attr];
            if (describe){
                content = content + `
                    <div>
                        <div class="title">${chineseName}</div>
                        <div class="content ${attr}">${describe.replace(herbName, `<span class="key_word">${herbName}</span>`)}</div>
                    </div>
                    `;
            }
        }
    }
    $("#info").html(content);
}

function herbInfo(data) {
    let content = ``;
    let herbName = data.herbName;
    let herbInfoList = data.herbInfoList;
    if (herbInfoList.length == 0){
        content = content + `<div>
                                <div class="title">草药名称</div>
                                <div class="content location" data-sid="h${data.herbId}">${herbName}<span title="报错" class="error" data-type="herb" data-id="${data.herbId}"></span></div>
                                <div class="content">抱歉，数据库中暂未收录该草药的生境分布、性味、炮制方法等信息</div>
                            </div>`;
    }else {
        content = content + `<div>
                                <div class="title">草药名称</div>
                                <div class="content location" data-sid="h${data.herbId}">${herbName}<span title="报错" class="error" data-type="herb" data-id="${data.herbId}"></span></div>
                            </div>`;
        for (let herbInfo of herbInfoList){
            content = content + herbInfoHtmlUnion(herbInfo, herbName, data.herbId);
        }
    }


    $("#info").html(content);
    fold();
}

function herbInfoHtmlUnion(herbInfo, herbName, herbId) {
    let text = ``;
    if (herbInfo.herbName == herbName){
        text = text + `<div class="pre_index" data-type="herbInfo" data-id="${herbInfo.herbInfoId}" data-sid="hi${herbInfo.herbInfoId}" data-hsid="h${herbId}">`;
    }else {
        text = text + `<div class="pre_index" data-type="herbInfo" data-id="${herbInfo.herbInfoId}" data-sid="hi${herbInfo.herbInfoId}">`;
    }
    text = text + `<div class="name">${herbInfo.herbName}</div>
                            <span class="error_icon" title="纠错"></span>
                            <span class="spr_icon"></span>
                        </div>
                        <div class="pre">`;
    for (let attr in herbInfo){
        let chineseName = field.get(attr);
        if (chineseName && attr != "herbName"){
            let describe = herbInfo[attr];
            if (describe){
                text = text + `
                    <div>
                        <div class="title">${chineseName}</div>
                        <div class="content ${attr}">${describe.replace(herbName, `<span class="key_word">${herbName}</span>`)}</div>
                    </div>
                    `;
            }
        }
    }
    text = text + `</div>`;
    return text;
}

function herbInfoHtml(herbInfo, herbName) {
    let text = ``;
    text = text + `<div class="pre_index" data-type="herbInfo" data-id="${herbInfo.herbInfoId}" data-sid="hi${herbInfo.herbInfoId}">
                            <div class="name">${herbInfo.herbName}</div>
                            <span class="error_icon" title="纠错"></span>
                            <span class="spr_icon"></span>
                        </div>
                        <div class="pre">`;
    for (let attr in herbInfo){
        let chineseName = field.get(attr);
        if (chineseName && attr != "herbName"){
            let describe = herbInfo[attr];
            if (describe){
                text = text + `
                    <div>
                        <div class="title">${chineseName}</div>
                        <div class="content ${attr}">${describe.replace(herbName, `<span class="key_word">${herbName}</span>`)}</div>
                    </div>
                    `;
            }
        }
    }
    text = text + `</div>`;
    return text;
}

function prescriptionInfo(data) {
    let content = `<div>
                        <div class="title">药剂名称</div>
                        <div class="content location" data-sid="p${data.prescriptionId}">${data.prescriptionName}<span title="报错" class="error" data-type="prescription" data-id="${data.prescriptionId}"></span></div>
                   </div>`;
    let prescriptionInfoList = data.prescriptionInfoList;
    for (let i = 0; i < prescriptionInfoList.length; i++){
        let pInfo = prescriptionInfoList[i];
        content = content + prescriptionHtmlByBook(pInfo, pInfo.prescriptionProvenance.replace("。",""));
    }

    $("#info").html(content);
    fold();
}

function Prescription(prescriptionId, prescriptionName) {
    this.prescriptionId = prescriptionId;
    this.prescriptionName = prescriptionName;
    this.prescriptionInfoList = new Array();
    this.addPrescriptionInfo = function (prescriptionInfo) {
        this.prescriptionInfoList.push(prescriptionInfo);
    }
}

function computerSimplyPrescription(prescriptionInfoList) {
    let prescriptionMap = new Map();
    for (let i = 0; i < prescriptionInfoList.length; i++) {
        let prescription = prescriptionMap.get(prescriptionInfoList[i].prescriptionId);
        if(prescription){
            prescription.addPrescriptionInfo(prescriptionInfoList[i]);
        }else {
            prescription = new Prescription(prescriptionInfoList[i].prescriptionId, prescriptionInfoList[i].prescriptionName);
            prescription.addPrescriptionInfo(prescriptionInfoList[i]);
            prescriptionMap.set(prescriptionInfoList[i].prescriptionId, prescription);
        }
    }
    return prescriptionMap;
}

function prescriptionHtmlByBook(pInfo, name) {
    let content = ``;
    let bsid = '';
    if (pInfo.bookList){
        bsid = 'b' + pInfo.bookList[0].bookId;
    }
    content = content + `<div class="pre_index" data-type="prescriptionInfo" data-id="${pInfo.prescriptionInfoId}" data-sid="pi${pInfo.prescriptionInfoId}" data-bsid="${bsid}">
                            <div class="name">${name}</div>
                            <span class="error_icon" title="纠错"></span>
                            <span class="spr_icon"></span>
                        </div>
                        <div class="pre">`;
    content = content + prescriptionInfoHtml(pInfo);
    content = content + `</div>`;
    return content;
}

function prescriptionHtml(pInfo, name) {
    let content = ``;
    content = content + `<div class="pre_index" data-type="prescriptionInfo" data-id="${pInfo.prescriptionInfoId}" data-sid="pi${pInfo.prescriptionInfoId}">
                            <div class="name">${name}</div>
                            <span class="error_icon" title="纠错"></span>
                            <span class="spr_icon"></span>
                        </div>
                        <div class="pre">`;
    content = content + prescriptionInfoHtml(pInfo);
    content = content + `</div>`;
    return content;
}

function prescriptionInfoHtml(pInfo) {
    let content = ``;
    for (let attr in pInfo){
        let chineseName = field.get(attr);
        if (chineseName && attr != "prescriptionName" && attr != "prescriptionProvenance"){
            let describe = pInfo[attr];
            if (describe){
                content = content + `
                    <div>
                        <div class="title">${chineseName}</div>
                        <div class="content">${describe}</div>
                    </div>
                `;
            }
        }
    }
    return content;
}

function turnPagePre(num){
    let width = $(".group_pre:nth-child(1)").width();
    let move = num * width;
    $(".groups_pre").animate({
        scrollLeft: move
    }, 500);
}

function turnPageHerb(num){
    let width = $(".group_herb:nth-child(1)").width();
    let move = num * width;
    $(".groups_herb").animate({
        scrollLeft: move
    }, 500);
}

function recoveryPre(index) {
    $(".group_pre:nth-child("+index+")").children(".pre").height(0);
    $(".group_pre:nth-child("+index+")").children(".pre_index").children(".spr_icon").css("transform", "rotate(-90deg)");
    for (let i=0;i<spread.length;i++){
        spread[i] = false;
    }
}

function recoveryHerb(index) {
    $(".group_herb:nth-child("+index+")").children(".pre").height(0);
    $(".group_herb:nth-child("+index+")").children(".pre_index").children(".spr_icon").css("transform", "rotate(-90deg)");
    for (let i=0;i<spread.length;i++){
        spread[i] = false;
    }
}

let preIndex = 0;
let herbIndex = 0;
function moveScrollBar() {
    const leftArrowDom = document.querySelector(".arrow-left");
    const rightArrowDom = document.querySelector(".arrow-right");
    if (leftArrowDom){
        leftArrowDom.addEventListener("click", function () {
            if (preIndex != 0){
                $(".group_pre:nth-child("+(preIndex+1)+")").children(".pre").height(0);
                preIndex = preIndex - 1;
                turnPagePre(preIndex);
                $(".pre-now-page").text(preIndex + 1);
            }
        });
    }
    if (rightArrowDom){
        rightArrowDom.addEventListener("click", function () {
            let max = $(".group_pre").length - 1;
            if (preIndex != max){
                recoveryPre(preIndex + 1);
                preIndex = preIndex + 1;
                turnPagePre(preIndex);
                $(".pre-now-page").text(preIndex + 1);
            }
        });
    }

    const leftArrowHerbDom = document.querySelector(".arrow-left-herb");
    const rightArrowHerbDom = document.querySelector(".arrow-right-herb");
    if (leftArrowHerbDom){
        leftArrowHerbDom.addEventListener("click", function () {
            if (herbIndex != 0){
                $(".group_herb:nth-child("+(herbIndex+1)+")").children(".pre").height(0);
                herbIndex = herbIndex - 1;
                turnPageHerb(herbIndex);
                $(".herb-now-page").text(herbIndex + 1);
            }
        });
    }
    if (rightArrowHerbDom){
        rightArrowHerbDom.addEventListener("click", function () {
            let max = $(".group_herb").length - 1;
            if (herbIndex != max){
                recoveryHerb(herbIndex + 1);
                herbIndex = herbIndex + 1;
                turnPageHerb(herbIndex);
                $(".herb-now-page").text(herbIndex + 1);
            }
        });
    }

}

let spread = new Array();
function fold() {
    let preIndexs = document.querySelectorAll(".pre_index");
    let pres = document.querySelectorAll(".pre");
    let icons = document.querySelectorAll(".spr_icon");
    let errors = document.querySelectorAll(".error_icon");
    spread = new Array(preIndexs.length);
    for (let i=0;i<spread.length;i++){
        spread[i] = false;
    }
    for (let i=0;i<preIndexs.length;i++){
        preIndexs[i].addEventListener("click", function(evt){
            if (spread[i]){
                pres[i].setAttribute("style", "height: 0px;");
                icons[i].setAttribute("style", "transform: rotate(-90deg);");
                spread[i] = false;
                errors[i].setAttribute("style", "display:none;");
            } else{
                let ch = pres[i].childElementCount;
                let height = 0;
                for (let j=0;j<ch;j++){
                    height = height + pres[i].children[j].clientHeight + 10;
                }
                height = height + 10;
                let parent = pres[i].parentElement;
                if (parent.className == "pre"){
                    let ph = parent.clientHeight + height;
                    parent.setAttribute("style", "height: "+ph+"px");
                }

                pres[i].setAttribute("style", "height: "+height+"px;");
                icons[i].setAttribute("style", "transform: rotate(0deg);");
                spread[i] = true;

                errors[i].setAttribute("style", "display:block;");
                errors[i].addEventListener("click", function (e) {
                    e.stopPropagation();
                    let type = preIndexs[i].getAttribute("data-type");
                    let id = preIndexs[i].getAttribute("data-id");
                    window.localStorage.setItem("errorType", type);
                    window.localStorage.setItem("errorId", id);
                    window.open("/tcm/correction");
                });

                if (cube){
                    let bsid = preIndexs[i].getAttribute("data-bsid");
                    let hsid = preIndexs[i].getAttribute("data-hsid");
                    if (bsid){
                        let node = find3DNodeBySId(bsid);
                        moveCamera(node);
                    }else if (hsid){
                        let node = find3DNodeBySId(hsid);
                        moveCamera(node);
                    }else {
                        let sid = preIndexs[i].getAttribute("data-sid");
                        let node = find3DNodeBySId(sid);
                        moveCamera(node);
                    }
                }
            }
        });
    }

    let esDom = document.querySelectorAll(".error");
    for (let es of esDom){
        es.addEventListener("click", function (e) {
            e.stopPropagation();
            let type = es.getAttribute("data-type");
            let id = es.getAttribute("data-id");
            window.localStorage.setItem("errorType", type);
            window.localStorage.setItem("errorId", id);
            window.open("/tcm/correction");
        });
    }

    let locationDom = document.querySelectorAll(".location");
    for (let location of locationDom) {
        location.addEventListener("click", function (e) {
            e.stopPropagation();
            if (cube){
                let sid = location.getAttribute("data-sid");
                let node = find3DNodeBySId(sid);
                moveCamera(node);
            }
        })
    }
}