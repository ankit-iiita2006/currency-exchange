/*jshint sub:true*/
function logoutDisplayButton(){
    var xmlhttp;
    if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
      }else{// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
      xmlhttp.onload=function(){
          if (xmlhttp.readyState==4 && xmlhttp.status==200)
            {
            var responseData = jQuery.parseJSON(xmlhttp.response);
            if(responseData["email"]){
                document.getElementById("logOut").style.display="block";
                document.getElementById("add").style.display="block";
            }
          }

    };
    xmlhttp.open("GET","/isLoggedIn",true);
    xmlhttp.send();
}
