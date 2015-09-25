/*jshint sub:true*/
function loadXMLDoc(){
    var xmlhttp;
    var valuation = document.getElementById("valuation").value;
    var from = document.getElementById("from").value;
    var to = document.getElementById("to").value;
    if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
      }else{// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
    xmlhttp.onreadystatechange=function(){
      if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            var responseData = jQuery.parseJSON(xmlhttp.response);
            response = xmlhttp.responseText;
            var fromCurrencyCode = document.getElementById("from").options[0].innerText;
            var options = document.getElementById("from").options;
            for(i =0; i <options.length;i++){
              if(options[i].value == from){
                 fromCurrencyCode = options[i].innerText;
              }
            }
            var toCurrencyCode = document.getElementById("to").options[0].innerText;
            options = document.getElementById("to").options;
            for(i =0; i <options.length;i++){
              if(options[i].value == to){
                 toCurrencyCode = options[i].innerText;
              }
            }

            var convertedValuation = responseData["amount"];
            var message = valuation + " in " + fromCurrencyCode + " is equivalent to " + convertedValuation + " " + toCurrencyCode;
            document.getElementById("myDiv").innerHTML=message;
        }
    };
    xmlhttp.open("POST","/currencyConvertorAjax?valuation=" + valuation + "&from=" + from + "&to=" + to ,true);
    xmlhttp.send();
}
