<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<title>Free CSS template by ChocoTemplates.com</title>
	<link rel="stylesheet" href="css/style.css" type="text/css" media="all" />
</head>
<body>
<body>
	<!-- Header -->
	<div id="header">
		<div class="shell">
			<!-- Logo -->
			<h1 id="logo" class="notext"><a href="#">Sport Zone Sport Portal</a></h1>
			<!-- End Logo -->
		</div>
	</div>
	<!-- End Header -->
	
	<!-- Navigation -->
	<div id="navigation">
		<div class="shell">
			<div class="cl">&nbsp;</div>
			<ul>
			    <li><a href="#">news &amp; events</a></li>
			    <li><a href="#">photo gallery</a></li>
			    <li><a href="#">video gallery</a></li>
			    <li><a href="#">community</a></li>
			    <li><a href="#">schedules</a></li>
			</ul>
			<div class="cl">&nbsp;</div>
		</div>
	</div>
	<!-- End Navigation -->
	
	<!-- Heading -->
	<div id="heading">
		<div class="shell">
			<div id="heading-cnt">
				
				<!-- Sub nav -->
<div class="inputForm">
  <form name="input" id="inputForm">
    Source<input type="text" class="city" name="srcName" id="srcName"></input><br />
    <input type="hidden" name="srcID" id="srcID"></input>
    Destination<input type="text" class="city" name="destName" id="destName"></input><br />
    <input type="hidden" name="destID" id="destID"></input>
    Start Date<input type="text" class="datepicker" name="depDateStart" id="depDateStart">
    End Date<input type="text" class="datepicker" name="depDateEnd" id="depDateEnd"></input><br />
    <input type="button" id="inputFormSubmit" value="GO" onclick="displayResults()"></input><br />
  </form>
</div>
<!-- End Form -->
				

<%--  <jsp:include page="scripts/dbConnect.jsp" />  --%>
<script type="text/javascript" src="scripts/crawler.js"></script>
<script type="text/javascript" src="scripts/jquery.js"></script>
<script>
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
function getCitySuggestions(str)
{
}
$(document).ready(function(){
  $("input[class='city']").keyup(function(event) {
    if(isAlpha(event.keyCode))
    {
      getCitySuggestions($(this).val());
    }
  });
});
</script>
<link rel="stylesheet" type="text/css" href="css.css" />
</body>
</html>
