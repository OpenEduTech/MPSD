const E = window.wangEditor;

const editorConfig = { MENU_CONF: {} }
editorConfig.placeholder = '请输入内容'
editorConfig.MENU_CONF['uploadImage'] = {
    server: '/correction/upload', // 上传图片地址
    fieldName: 'files'
}
const toolbarConfig = {
    excludeKeys: [
        'insertVideo',
        'uploadVideo',
        'fullScreen',
        'fontSize',
        'fontFamily',
        'headerSelect',
        'header1',
        'header2',
        'header3',
        'header4',
        'header5',
        'bulletedList',
        'numberedList',
        'todo'
    ]
}

const editor = E.createEditor({
    selector: '#content',
    config: editorConfig
})

const toolbar = E.createToolbar({
    editor,
    selector: '#editor-toolbar',
    config: toolbarConfig,
    mode: 'simple'
})

let errorType = window.localStorage.getItem("errorType");
let errorId = window.localStorage.getItem("errorId");
let name;
if (errorType && errorId){
    let sendPath = "/" + errorType + "/name/" + errorId;
    $.ajax({
        url: sendPath,
        type: "GET",
        dataType: "json",
        success: function (data) {
            fullTip(data);
        }
    });
}

function fullTip(data){
    let type = data.type;
    name = data.name;
    if (type && name){
        let tip = `关于<span class="key_word">${type}：${name}</span>有什么建议吗？感谢您的支持`;
        $("#tip").html(tip);
    }
}

const buttonDom = document.querySelector("#button");
buttonDom.addEventListener("click", function () {
    let suggestion = editor.getHtml();
    let phone = $(".phone").val();
    let data = {"errorType": errorType,
                "errorId": errorId,
                "errorName": name,
                "suggestion": suggestion,
                "phone": phone};

    if (errorType && errorId && name && suggestion){
        $.ajax({
            url: "/correction/insert",
            type: "POST",
            dataType: "json",
            data: data,
            success: function (data) {
                if (data){
                    alert("提交成功，谢谢您的建议");
                    window.opener = null;
                    window.open('', '_self');
                    window.close()
                }
            }
        });
    }else {
        alert("您还没有填写内容");
    }
});