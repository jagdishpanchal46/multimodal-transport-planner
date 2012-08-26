function createRequestObjectResults() {
    var tmpXmlHttpObject;
    
    if (window.XMLHttpRequest) { 
        // Mozilla, Safari would use this method ...
        tmpXmlHttpObject = new XMLHttpRequest();
	
    } else if (window.ActiveXObject) { 
        // IE would use this method ...
        tmpXmlHttpObject = new ActiveXObject("Microsoft.XMLHTTP");
    }
    
    return tmpXmlHttpObject;
}
//call the above function to create the XMLHttpRequest object
var httpResults = createRequestObjectResults();
httpResults.timeout = 4000;
httpResults.ontimeout = function () { alert("Timed out!!!"); }

function makeGetRequestResults(srcName, destName, date) {
    httpResults.open('get', 'scripts/fetchResults.jsp?srcName=' + srcName + '&destName='+destName + '&date='+date);
	
    //assign a handler for the response
    httpResults.onreadystatechange = processResponseResults;
	
    //actually send the request to the server
    httpResults.send(null);
}
function populateResults(json_parsed)
{
//   alert(JSON.stringify(json_parsed));
  $("#results").empty();
  $("#results").append("<table id='resultsTable' name='resultsTable'></table>");
  var parent = "resultsTable";
  for(var i=0;i<json_parsed.length;j++)
  {
    var obj = json_parsed[i];
    var type = obj.type;
    
    if(type=="0") // Direct Flight
    {
      var dt,dd,from,at,ad,to,aln,pr;
      dt = obj.dt;
      dd = obj.dd;
      from = obj.from;
      at = obj.at;
      ad = obj.ad;
      to = obj.to;
      aln = obj.aln;
      pr = obj.pr;
      var insert = "<tr><td>Flight</td><td>"+from+"</td>"+"<td>"+dd+"</td>"+"<td>"+dt+"</td>"+"<td>"+to+"</td>"+"<td>"+ad+"</td>"+"<td>"+at+"</td>"+"<td>"+aln+"</td>"+"<td>"+pr+"</td></tr>";
      $(resultsTable).append(insert);
    }
    else if(type=="1") // Direct Train
    {
      var dt,dd,from,at,ad,to,aln,pr;
      dt = obj.dt;
      dd = obj.dd;
      from = obj.from;
      at = obj.at;
      ad = obj.ad;
      to = obj.to;
      aln = obj.aln;
      pr = obj.pr;
      var insert = "<tr><td>Train</td><td>"+from+"</td>"+"<td>"+dd+"</td>"+"<td>"+dt+"</td>"+"<td>"+to+"</td>"+"<td>"+ad+"</td>"+"<td>"+at+"</td>"+"<td>"+aln+"</td>"+"<td>"+pr+"</td></tr>";
      $(resultsTable).append(insert);
    }
    else if(type=="2") // FT
    {
      var dt,dd,from,at,ad,to,aln,pr;
      dt1 = obj.dt1;
      dd1 = obj.dd1;
      dt2 = obj.dt2;
      dd2 = obj.dd2;
      from = obj.from;
      via = obj.via;
      at1 = obj.at1;
      ad1 = obj.ad1;
      at2 = obj.at2;
      ad2 = obj.ad2;
      to = obj.to;
      aln1 = obj.aln1;
      aln2 = obj.aln2;
      pr = obj.pr;
      pr1 = obj.pr1;
      pr2 = obj.pr2;
      var insert1 = "<tr><td>Flight</td><td>"+from+"</td>"+"<td>"+dd1+"</td>"+"<td>"+dt1+"</td>"+"<td>"+via+"</td>"+"<td>"+ad1+"</td>"+"<td>"+at1+"</td>"+"<td>"+aln1+"</td>"+"<td>"+pr1+"</td></tr>";
      var insert2 = "<tr><td>Train</td><td>"+via+"</td>"+"<td>"+dd2+"</td>"+"<td>"+dt2+"</td>"+"<td>"+to+"</td>"+"<td>"+ad2+"</td>"+"<td>"+at2+"</td>"+"<td>"+aln2+"</td>"+"<td>"+pr2+"</td></tr>";
      $(resultsTable).append(insert1);
      $(resultsTable).append(insert2);
    }
    else if(type=="3") // FT
    {
      var dt,dd,from,at,ad,to,aln,pr;
      dt1 = obj.dt1;
      dd1 = obj.dd1;
      dt2 = obj.dt2;
      dd2 = obj.dd2;
      from = obj.from;
      via = obj.via;
      at1 = obj.at1;
      ad1 = obj.ad1;
      at2 = obj.at2;
      ad2 = obj.ad2;
      to = obj.to;
      aln1 = obj.aln1;
      aln2 = obj.aln2;
      pr = obj.pr;
      pr1 = obj.pr1;
      pr2 = obj.pr2;
      var insert1 = "<tr><td>Train</td><td>"+from+"</td>"+"<td>"+dd1+"</td>"+"<td>"+dt1+"</td>"+"<td>"+via+"</td>"+"<td>"+ad1+"</td>"+"<td>"+at1+"</td>"+"<td>"+aln1+"</td>"+"<td>"+pr1+"</td></tr>";
      var insert2 = "<tr><td>Flight</td><td>"+via+"</td>"+"<td>"+dd2+"</td>"+"<td>"+dt2+"</td>"+"<td>"+to+"</td>"+"<td>"+ad2+"</td>"+"<td>"+at2+"</td>"+"<td>"+aln2+"</td>"+"<td>"+pr2+"</td></tr>";
      $(resultsTable).append(insert1);
      $(resultsTable).append(insert2);
    }
    $(resultsTable).append("<td></td>");
  }
}
function processResponseResults() {
    //check if the response has been received from the server
    if(httpResults.readyState == 4){
	
        //read and assign the response from the server
        var response = httpResults.responseText;
// 	alert(response);
        var json_parsed = jQuery.parseJSON(response);
	populateResults(json_parsed);
    }
//     else
//       alert(httpResults.readyState);
}
function displayResults()
{
  var srcName = $("#srcName").val();
  var destName = $("#destName").val();
  var date = $.datepicker.parseDate('dd/mm/yy',$(".datepicker").val());
  var dateStr = $.datepicker.formatDate('yymmdd',date);
  makeGetRequestResults(srcName, destName, dateStr);
}