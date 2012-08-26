function createRequestObject() {
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
var http = createRequestObject();

function makeGetRequestSrc(wordId) {
    http.open('get', 'scripts/getCity.jsp?str=' + wordId);
	
    //assign a handler for the response
    http.onreadystatechange = processResponseSrc;
	
    //actually send the request to the server
    http.send(null);
}
function makeGetRequestDest(wordId) {
    http.open('get', 'scripts/getCity.jsp?str=' + wordId);
    http.onreadystatechange = processResponseDest;
	
    //actually send the request to the server
    http.send(null);
}
function populateSrc(json_parsed)
{
  var cities=new Array();
  var CITY_NAME;
  var u=0;
  for (u = 0; u < json_parsed[0].length; u++){
      CITY_NAME = json_parsed[0][u].CITY_NAME;
      cities[u] = CITY_NAME;
  }
  for (u=0; u < json_parsed[1].length; u++){
      CITY_NAME = json_parsed[1][u].name;
      cities[u+json_parsed[0].length] = CITY_NAME;
  }
//   alert(cities);
  $("#srcName").autocomplete( "option", "source", cities);
}
function populateDest(json_parsed)
{
  var cities=new Array();
  var CITY_NAME;
  var u=0;
  for (u = 0; u < json_parsed[0].length; u++){
      CITY_NAME = json_parsed[0][u].CITY_NAME;
      cities[u] = CITY_NAME;
  }
  for (u=0; u < json_parsed[1].length; u++){
      CITY_NAME = json_parsed[1][u].name;
      cities[u+json_parsed[0].length] = CITY_NAME;
  }
//   alert(cities);
  $("#destName").autocomplete( "option", "source", cities);
}
function processResponseSrc() {
    //check if the response has been received from the server
    if(http.readyState == 4){
	
        //read and assign the response from the server
        var response = http.responseText;
        var json_parsed = jQuery.parseJSON(response);
	populateSrc(json_parsed);
    }
}
function processResponseDest() {
    //check if the response has been received from the server
    if(http.readyState == 4){
	
        //read and assign the response from the server
        var response = http.responseText;
        var json_parsed = jQuery.parseJSON(response);
	populateDest(json_parsed);
    }
}
function isAlpha(ch)
{
  if((ch>=65 && ch<=90) || (ch>=97 && ch<=122))
  {
    return true;
  }
  else
  {
    return false;
  }
}
function isBackspace(ch)
{
  return ch==8;
}
function getCitySuggestionsSrc(str)
{
  makeGetRequestSrc(str);
}
function getCitySuggestionsDest(str)
{
  makeGetRequestDest(str);
}