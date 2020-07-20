<html>

<head><title>API Coverage Report</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
</head>

<style>
table {
	border-spacing: 0;
	border-collapse: collapse
}

th {
	text-align: left
}

.table {
	width: 65%;
	max-width: 100%;
	margin-bottom: 20px
}

.table>tbody>tr>td,
.table>tbody>tr>th,
.table>tfoot>tr>td,
.table>tfoot>tr>th,
.table>thead>tr>td,
.table>thead>tr>th {
	padding: 8px;
	line-height: 1.42857143;
	vertical-align: top;
	border-top: 1px solid #ddd
}

.table>tbody>tr.success>td,
.table>tbody>tr.success>th,
.table>tbody>tr>td.success,
.table>tbody>tr>th.success,
.table>tfoot>tr.success>td,
.table>tfoot>tr.success>th,
.table>tfoot>tr>td.success,
.table>tfoot>tr>th.success,
.table>thead>tr.success>td,
.table>thead>tr.success>th,
.table>thead>tr>td.success,
.table>thead>tr>th.success {
	background-color: #dff0d8
}

.table>tbody>tr.danger>td,
.table>tbody>tr.danger>th,
.table>tbody>tr>td.danger,
.table>tbody>tr>th.danger,
.table>tfoot>tr.danger>td,
.table>tfoot>tr.danger>th,
.table>tfoot>tr>td.danger,
.table>tfoot>tr>th.danger,
.table>thead>tr.danger>td,
.table>thead>tr.danger>th,
.table>thead>tr>td.danger,
.table>thead>tr>th.danger {
	background-color: #f2dede
}

html {
	font-size: 10px;
	-webkit-tap-highlight-color: rgba(0, 0, 0, 0)
}

body {
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
	font-size: 14px;
	line-height: 1.42857143;
	color: #333;
	text-align: -webkit-center;
	background-color: #fff
}
</style>

<body>

<#assign missedEndpointNum = (allEndpointsNum - coveredEndpointsNum)>
<#assign areaNumber = areaWiseEndpoints?size>

<script type="text/javascript">
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawGlobalCoverageChart);
google.charts.setOnLoadCallback(drawAreaWiseBarChart);

function drawGlobalCoverageChart() {
      var data = google.visualization.arrayToDataTable([
      ['Coverage', 'Percentage'],
      ['Covered', ${coveredEndpointsNum}],
      ['Missed', ${missedEndpointNum}]
    ]);

  var options = {
    'title': 'Global Endpoint Coverage',
    'width': 550,
    'height': 400,
    'slices': { 1: {offset: 0.2} },
    'colors': ['#96cc64', '#fd5a3e']};
  var chart = new google.visualization.PieChart(document.getElementById('piechart'));
  chart.draw(data, options);
}

function drawAreaWiseBarChart() {
        var data = google.visualization.arrayToDataTable([
          ['Area', 'Covered', 'Missing', 'Average'],
          <#list areaWiseEndpoints?keys as key>
              ['${key}', ${areaWiseEndpoints[key].getCoveredEndpoints()}, ${areaWiseEndpoints[key].getUncoveredEndpointNum()}, (${coveredEndpointsNum}/${areaNumber})], ${'\n'}
          </#list>
        ]);

        var options = {
          title : 'Area-wise Endpoint Coverages',
          vAxis: {title: 'Endpoints'},
          hAxis: {title: 'Areas'},
          animation: {startup: true, easing: 'inAndOut', duration: 700},
          seriesType: 'bars',
          colors: ['#96cc64', '#fd5a3e', '#d35ebe'],
          series: {2: {type: 'line'}}};
        var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
</script>

<div class="container">
<div>${currentDateAndTime}</div>
<div id="piechart"></div>
<div id="chart_div" style="width: 900px; height: 500px;">Endpoint List</div>
    <table class="table">
        <thead>
        <tr>
            <th>Area</th>
            <th>Method</th>
            <th>Endpoint</th>
            <th>#Tests</th>
        </tr>
        </thead>
        <tbody>
            <#list endpoints as endpoint>
            <#if endpoint.covered gt 0>
                <#assign lineClass = 'success'>
            <#else>
                <#assign lineClass = 'danger'>
            </#if>
                <tr class=${lineClass}>
                  <td>${endpoint.area}</td>
                  <td>${endpoint.method}</td>
                  <td>${endpoint.endpoint}</td>
                  <td>${endpoint.covered}</td>
                </tr>
            </#list>
		</tbody>
    </table>
</div>
</body>
</html>
