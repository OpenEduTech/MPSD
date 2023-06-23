// 全局变量，存储解析后的 CSV 数据
let csvData = {};

// 读取本地的 CSV 文件
function readCSVFile(file, tableId) {
    const reader = new FileReader();
    reader.onload = function(e) {
        const contents = e.target.result;
        csvData[tableId] = parseCSVData(contents);
        displayCSVData(tableId);
    };
    reader.readAsText(file);
}

// 解析 CSV 数据
function parseCSVData(data) {
    const rows = data.split('\n');
    const headerRow = rows[0].split(',');

    const tableData = [];

    for (let i = 1; i < rows.length; i++) {
        const cells = rows[i].split(',');
        const rowData = {};
        for (let j = 0; j < cells.length; j++) {
            rowData[headerRow[j]] = cells[j];
        }
        tableData.push(rowData);
    }

    return tableData;
}

// 展示指定表格的数据
function displayCSVData(tableId) {
    const table = document.getElementById(tableId);
    const data = csvData[tableId];

    let tableHTML = '<tr>';
    for (let header in data[0]) {
        tableHTML += '<th>' + header + '</th>';
    }
    tableHTML += '</tr>';

    for (let i = 0; i < data.length; i++) {
        tableHTML += '<tr>';
        for (let header in data[i]) {
            tableHTML += '<td>' + data[i][header] + '</td>';
        }
        tableHTML += '</tr>';
    }

    table.innerHTML = tableHTML;
}

// 根据筛选条件过滤表格数据
function filterData(tableId) {
    const table = document.getElementById(tableId);
    const data = csvData[tableId];
    const filterInput = document.getElementById('filterInput');
    const filterValue = filterInput.value.toLowerCase();

    let tableHTML = '<tr>';
    for (let header in data[0]) {
        tableHTML += '<th>' + header + '</th>';
    }
    tableHTML += '</tr>';

    for (let i = 0; i < data.length; i++) {
        let match = false;
        for (let header in data[i]) {
            if (data[i][header].toLowerCase().includes(filterValue)) {
                match = true;
                break;
            }
        }
        if (match) {
            tableHTML += '<tr>';
            for (let header in data[i]) {
                tableHTML += '<td>' + data[i][header] + '</td>';
            }
            tableHTML += '</tr>';
        }
    }

    table.innerHTML = tableHTML;
}

// 显示指定的表格
function showTable(tableId) {
    const tables = document.getElementsByTagName('table');
    for (let i = 0; i < tables.length; i++) {
        tables[i].style.display = 'none';
    }

    const table = document.getElementById(tableId);
    table.style.display = 'block';

    filterData(tableId);
}

// 监听文件选择框的变化
const filterInput = document.getElementById('filterInput');
filterInput.addEventListener('input', () => {
    const activeTable = document.querySelector('table[style="display: block;"]');
    const tableId = activeTable.id;
    filterData(tableId);
});
