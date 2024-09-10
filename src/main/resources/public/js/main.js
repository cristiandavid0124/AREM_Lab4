function loadGetMsg() {
    let nameVar = document.getElementById("log").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        createTable(JSON.parse(this.responseText));
    };
    xhttp.open("GET", "/log?msg=" + nameVar);
    xhttp.send();
}

function createTable(data) {
    let tableHtml = "<table><tr><th>#</th><th>Log</th><th>Date</th></tr>";
    for (let i = 0; i < data.length; i++) {
        tableHtml += "<tr><td>" + (i+1) + "</td><td>" + data[i].log + "</td><td>" + data[i].date.$date + "</td></tr>";
    }
    tableHtml += "</table>";
    document.getElementById("logsTable").innerHTML = tableHtml;
}
