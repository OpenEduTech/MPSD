const prescriptionItemDom = document.querySelector(".prescription_item");
const herbItemDom = document.querySelector(".herb_item");
const bookItemDom = document.querySelector(".book_item");
const diseaseItemDom = document.querySelector(".disease_item");
const geographyItemDom = document.querySelector(".geography_item");
const companyItemDom = document.querySelector(".company_item");
const industryItemDom = document.querySelector(".industry_item");
const productItemDom = document.querySelector(".product_item");
const inputDom = document.querySelector("#search_input");
let type = 0;

prescriptionItemDom.addEventListener("click", function () {
    inputDom.setAttribute("class", "search_prescription");
    type = 0;
});
herbItemDom.addEventListener("click", function () {
    inputDom.setAttribute("class", "search_herb");
    type = 1;
});
diseaseItemDom.addEventListener("click", function () {
    inputDom.setAttribute("class", "search_disease");
    type = 2;
});
bookItemDom.addEventListener("click", function () {
    inputDom.setAttribute("class", "search_book");
    type = 3;
});
geographyItemDom.addEventListener("click", function () {
    inputDom.setAttribute("class", "search_geography");
    type = 4;
});

companyItemDom.addEventListener("click", function () {
    inputDom.setAttribute("class", "search_company");
    type = 0;
});
industryItemDom.addEventListener("click", function () {
    inputDom.setAttribute("class", "search_industry");
    type = 1;
});
productItemDom.addEventListener("click", function () {
    inputDom.setAttribute("class", "search_product");
    type = 2;
});

let scontent;
const searchButtonDom = document.querySelector(".search_button");
searchButtonDom.addEventListener("click", function () {
    scontent = $(".search_content").val();
    if (scontent){
        let sendData = {content: scontent};
        let path = "/" + typeMap.get(type) + "/search";
        $.ajax({
            url: path,
            data: sendData,
            type: "POST",
            dataType: "json",
            success: function (data) {
                let searchResult = data.data;
                for (let item in searchResult){
                    if (searchResult[item]){
                        buildTable(item, searchResult[item]);
                    }
                }
            }
        });
    }else {
        alert("您还未输入内容");
    }
});

function buildTable(item, data) {
    if (data.length == 0){
        let content = `<span class='key_word'>万分抱歉，数据库中未搜索到“${scontent}”的相关信息</span>`;
        $("#search_result").html(content);
        return;
    }
    switch (item){
        case "prescription":
            buildPrescriptionTable(data);
            break;
        case "herb":
            buildHerbTable(data);
            break;
        case "disease":
            buildDiseaseTable(data);
            break;
        case "book":
            buildBookTable(data);
            break;
        case "geography":
            buildGeographyTable(data);
            break;
        case "company":
            buildCompanyTable(data);
            break;
        case "industry":
            buildIndustryTable(data);
            break;
        case "product":
            buildProductTable(data);
            break;
    }
}
function buildCompanyTable(data){
    let content = `<div class="result company">`;
    for (let company of data) {
        content = content + `<div class="company_result" data-id="${company.comId}">${company.comName.replace(scontent, `<span class='key_word'>${scontent}</span>`)}</div>`;
    }
    content = content + `</div>`;
    $("#search_result").html(content);

    const resultDomList = document.querySelectorAll(".company_result");
    for (let resultDom of resultDomList){
        resultDom.addEventListener("click", function () {
            let id = resultDom.getAttribute("data-id");
            window.localStorage.setItem("item", "company");
            window.localStorage.setItem("id", id);
            window.open("/tcm/info");
        });
    }
}

function  buildIndustryTable(data){
    let content = `<div class="result industry">`;
    for (let industry of data) {
        content = content + `<div class="industry_result" data-id="${industry.indId}">${industry.indName.replace(scontent, `<span class='key_word'>${scontent}</span>`)}</div>`;
    }
    content = content + `</div>`;
    $("#search_result").html(content);

    const resultDomList = document.querySelectorAll(".industry_result");
    for (let resultDom of resultDomList){
        resultDom.addEventListener("click", function () {
            let id = resultDom.getAttribute("data-id");
            window.localStorage.setItem("item", "industry");
            window.localStorage.setItem("id", id);
            window.open("/tcm/info");
        });
    }
}

function buildProductTable(data){

}

function buildPrescriptionTable(data) {
    let content = `<div class="result prescription">`;
    for (let prescription of data) {
        content = content + `<div class="prescription_result" data-id="${prescription.prescriptionId}">${prescription.prescriptionName.replace(scontent, `<span class='key_word'>${scontent}</span>`)}</div>`;
    }
    content = content + `</div>`;
    $("#search_result").html(content);

    const resultDomList = document.querySelectorAll(".prescription_result");
    for (let resultDom of resultDomList){
        resultDom.addEventListener("click", function () {
            let id = resultDom.getAttribute("data-id");
            window.localStorage.setItem("item", "prescription");
            window.localStorage.setItem("id", id);
            window.open("/tcm/info");
        });
    }
}

function buildHerbTable(data) {
    let content = `<div class="result herb">`;
    for (let herb of data) {
        content = content + `<div class="herb_result" data-id="${herb.herbId}">${herb.herbName.replace(scontent, `<span class='key_word'>${scontent}</span>`)}</div>`;
    }
    content = content + `</div>`;
    $("#search_result").html(content);

    const resultDomList = document.querySelectorAll(".herb_result");
    for (let resultDom of resultDomList){
        resultDom.addEventListener("click", function () {
            let id = resultDom.getAttribute("data-id");
            window.localStorage.setItem("item", "herb");
            window.localStorage.setItem("id", id);
            window.open("/tcm/info");
        });
    }
}

function buildDiseaseTable(data) {
    let content = `<div class="result disease">`;
    for (let disease of data) {
        content = content + `<div class="disease_result" data-id="${disease.diseaseId}">${disease.diseaseName.replace(scontent, `<span class='key_word'>${scontent}</span>`)}</div>`;
    }
    content = content + `</div>`;
    $("#search_result").html(content);

    const resultDomList = document.querySelectorAll(".disease_result");
    for (let resultDom of resultDomList){
        resultDom.addEventListener("click", function () {
            let id = resultDom.getAttribute("data-id");
            window.localStorage.setItem("item", "disease");
            window.localStorage.setItem("id", id);
            window.open("/tcm/info");
        });
    }
}

function buildBookTable(data) {
    let content = `<div class="result book">`;
    for (let book of data) {
        content = content + `<div class="book_result" data-id="${book.bookId}">${book.bookName.replace(scontent, `<span class='key_word'>${scontent}</span>`)}</div>`;
    }
    content = content + `</div>`;
    $("#search_result").html(content);

    const resultDomList = document.querySelectorAll(".book_result");
    for (let resultDom of resultDomList){
        resultDom.addEventListener("click", function () {
            let id = resultDom.getAttribute("data-id");
            window.localStorage.setItem("item", "book");
            window.localStorage.setItem("id", id);
            window.open("/tcm/info");
        });
    }
}

function buildGeographyTable(data) {
    let content = `<div class="result geography">`;
    for (let geography of data) {
        content = content + `<div class="geography_result" data-name="${geography.geographySimply}">${geography.geographyFull.replace(scontent, `<span class='key_word'>${scontent}</span>`)}</div>`;
    }
    content = content + `</div>`;
    $("#search_result").html(content);

    const resultDomList = document.querySelectorAll(".geography_result");
    for (let resultDom of resultDomList){
        resultDom.addEventListener("click", function () {
            let name = resultDom.getAttribute("data-name");
            overlayer(name);
            $(".geography-layer").css("height", "100%");
        });
    }
}