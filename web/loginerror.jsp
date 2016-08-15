<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
        <meta charset="UTF-8">
        <meta charset="windows-1252">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Bootstrap -->
	<style type="text/css">
            <%@include file="css/bootstrap.css" %>
        </style>    
	<!-- CSS para el Responsive Slider-->
        <style type="text/css">
            <%@include file="css/flexslider.css" %>
        </style>
	<!-- Bootstrap Responsive-->
        <style type="text/css">
            <%@include file="css/bootstrap-responsive.css" %>
        </style>
	<!-- Estilos de Banner	-->
        <style type="text/css">
            <%@include file="css/banner.css" %>
        </style>		<!-- Estilos Personales	-->
        <style type="text/css">
            <%@include file="css/miscss.css" %>
        </style>
       
        <!-- Jquery -->
        <script type="text/javascript" language="JavaScript">
            <%@ include file="jss/jquery-1.8.3.js" %>
        </script>
	<!-- FlexSlider -->
        <script type="text/javascript" language="JavaScript">
        <%@ include file="jss/jquery.flexslider.js" %>
        </script>
	<!-- Bootstrap	-->
       
        <script type="text/javascript" language="JavaScript">
         <%@ include file="jss/bootstrap.js" %>
        </script>

	<script>
		$(window).load(function(){
			$('.flexslider').flexslider({
				animation: "slide",
				start: function(slider){
					$('body').removeClass('loading');
				}
			});
		});
	</script>
        
        <script language="JavaScript" type="text/javascript">
        alert("Username o Password invalido.");
        </script>
        
<title>Login</title>
<style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

#login-box {
	width: 300px;
	padding: 20px;
	margin: 100px auto;
	background: #fff;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 1px solid #000;
}

.buttonHolder{ text-align: center; }
</style>
</head>
<body onload='document.loginForm.username.focus();'>
    
    	    <div class="row-fluid encabezado">
    	<div class="span12">
    		<div class="row-fluid">
				<div id="banner_ministerio" class="span12">
					<img src="/PyS-img/img/logo_ministerio.png" style="float:left; ">
					<img src="/PyS-img/img/logo_pueblo_victorioso.jpg" style="float:right; height:56px">
				</div>
			</div>
    	</div>
    </div>

        <div class="row-fluid">
		<div class="span12">
			<div id="div_slider_encabezado">
				<div class="flexslider">
		     		
		        <div class="flex-viewport" style="overflow: hidden; position: relative;">
		        	<ul class="slides" style="width: 1000%; transition-duration: 0s; transform: translate3d(-2014px, 0px, 0px);">
		        			<li class="clone" style="width: 1007px; float: left; display: block;">
						    	<img src="/PyS-img/img/slider_mercal.jpg">
							</li>
			          		<li class="" style="width: 1007px; float: left; display: block;">
						    	<img src="/PyS-img/img/base_de_misiones.jpg">
							</li>
							<li class="flex-active-slide" style="width: 1007px; float: left; display: block;">
						    	<img src="/PyS-img/img/0800mercal.jpg">
							</li>
							<li class="" style="width: 1007px; float: left; display: block;">
						    	<img src="/PyS-img/img/slider_mercal.jpg">
							</li>
							<li class="clone" style="width: 1007px; float: left; display: block;">
						    	<img src="/PyS-img/img/base_de_misiones.jpg">
							</li>
						</ul>
					</div>
					<ol class="flex-control-nav flex-control-paging">
						<li><a class="">1</a></li>
						<li><a class="flex-active">2</a></li>
						<li><a class="">3</a></li>
					</ol>
					<ul class="flex-direction-nav">
						<li><a class="flex-prev" href="http://www.mercal.gob.ve/?page_id=26086#">Previous</a></li>
						<li><a class="flex-next" href="http://www.mercal.gob.ve/?page_id=26086#">Next</a></li>
					</ul>
				</div>
		    </div>
		</div>
	</div>

	    <div id="contenido" class="container-fluid">
                <h1 align="center">Publicacion y Suscripcion</h1>

	<div id="login-box">

		<h3 align="center">Ingresar</h3>

		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>

		<form name='loginForm' action="<c:url value='/login' />" method='POST'>

			<table>
				<tr>
					<td>Usuario:</td>
					<td><input type='text' name='username' value=''></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type='password' name='password' /></td>
				</tr>
				<tr>
                                    <td colspan='2'><div class="buttonHolder"><input name="submit" type="submit"value="submit" /></div></td> <!--//OJOOOOOOOOO onclick=" location.href='/PublicacionySuscripcion/admin.jsp'-->
				</tr>
			</table>

			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />

		</form>
	</div>
    		<div class="row-fluid">
				<div class="span12">
					<div id="copy" align="bottom">
						<h4>Copyright (C) 2015 Mercado de Alimentos MERCAL, C.A. Rif: G-200035919</h4><a href="http://phd2014consultores.com/">Impulsado por PhD 2014 Consultores C.A.</a>
					</div>
				</div>
			</div>                            

</body>
</html>