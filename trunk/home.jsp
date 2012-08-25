<html>
<head>
<title>
Plan Your Route
</title>
<script type="text/javascript" src="crawler.js"></script>
<!--<script>
$(function() {
  $( "#datepicker" ).datepicker();
});
</script>-->
<link rel="stylesheet" type="text/css" href="css.css" />
</head>
<body>
<div class="inputForm">
  <form name="input" id="inputForm">
    Source<input type="text" name="srcName" id="srcName"></input><br />
    <input type="hidden" name="srcID" id="srcID"></input>
    Destination<input type="text" name="destName" id="destName"></input><br />
    <input type="hidden" name="destID" id="destID"></input>
    Start Date<input type="text" class="datepicker" name="depDateStart" id="depDateStart">
    End Date<input type="text" class="datepicker" name="depDateEnd" id="depDateEnd"></input>
    <input type="button" id="inputFormSubmit" value="GO" onclick="displayResults()"></input>
  </form>
</div>
</body>
</html>