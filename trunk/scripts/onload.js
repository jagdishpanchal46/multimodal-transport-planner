$(document).ready(function(){
  $("input[name='srcName']").keyup(function(event) {
    if(isAlpha(event.keyCode) || isBackspace(event.keyCode))
    {
      getCitySuggestionsSrc($(this).val());
    }
  });
  $("input[name='destName']").keyup(function(event) {
    if(isAlpha(event.keyCode) || isBackspace(event.keyCode))
    {
      getCitySuggestionsDest($(this).val());
    }
  });
  var arr = new Array();
  $("#srcName").autocomplete({
	  source: arr
  });
  $("#destName").autocomplete({
	  source: arr
  });
  $(function() {
	  $( ".datepicker" ).datepicker({ dateFormat: "dd/mm/yy" });
  });
});