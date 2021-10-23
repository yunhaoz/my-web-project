// Load the Visualization API and the piechart package.
google.charts.load('current', {'packages':['corechart']});

// Set a callback to run when the Google Visualization API is loaded.
google.charts.setOnLoadCallback(initChart);

// BK: expose data table to global scope
var gData;

function initChart() {
    // Create the data table.
    gData = new google.visualization.DataTable();
    gData.addColumn('string', 'Topping');
    gData.addColumn('number', 'Slices');
    drawChart();
}

// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.
function drawChart() {
    // Set chart options
    var options = {pieHole: 0.4,
                        'chartArea': {'width': '100%', 'height': '80%'},
                        'legend': {'position': 'bottom'},
                    backgroundColor: 'transparent'
                };

    // Instantiate and draw our chart, passing in some options.
    var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
    chart.draw(gData, options);
}

window.onresize = function() {
    drawChart();
};