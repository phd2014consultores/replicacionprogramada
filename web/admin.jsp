<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
        <head>
        <meta charset="UTF-8">
        <meta charset="windows-1252">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Cluster</title>
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
        
    </head>
    
<body>  
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

    	<!-- BARRA DE NAVEGACION -->
    	
    	<div class="row-fluid">
    		<div class="span12">
				<div id="barra_navegacion_desk" class="container navbar visible-desktop">
					<div id="barra" class="row navbar-inner">
						<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
								<span class="icon-th-list"></span>
						</a>
						<div class="span12 nav-collapse nav pull-left" style="text-align=center; width: 100%;">
							<ul id="dropmenu" class="menu_redondeado" style="display:inline-block;">
<li class="page_item page-item-5"><a href="/PublicacionySuscripcion/admin.jsp">Cluster</a></li>
<li class="page_item page-item-26084"><a href="/PublicacionySuscripcion/pdi.jsp">PDI</a></li>
<li class="page_item page-item-8"><a href="/PublicacionySuscripcion/tiendaadmin.jsp">Tienda</a></li>
<li class="page_item page-item-10"><a href="/PublicacionySuscripcion/gestioncargas.jsp">Gestionar Cargas</a></li>
<li class="page_item page-item-22"><a href="/PublicacionySuscripcion/confcargas.jsp">Configurar Cargas</a></li>
<li class="page_item page-item-22"><a href="/PublicacionySuscripcion/gusuarios.jsp">Gestión Usuarios</a></li>
							</ul>
						</div>
					</div>		
				</div>
    		</div>
    	</div>    	
    </div>
    <br>

    <h1 align="center">Configuración Cluster</h1>
    <br>

    <h3 align="center">Activos - Desactivar </h3>
    <br>
    <div id="Nodos">
    	<label for="message">Nodos Activos :</label> 
    	<textarea id="lnodos" cols="50" rows="15" required></textarea>  <span> <input type="submit" onclick=" this.value='Cargado'"value="Cargar"/> </span>  
    <br>
    <br>
    		<span > Desabilitar: </span> <input type="text" placeholder="Ip Nodo" required />
	<input type="submit" onclick=" this.value='Enviado'"value="Enviar"/>  
    </div>

    <br>
    <br>
    <h3 align="center">Añadir Nodo </h3>
    <br>
    <div id="agregarnodos">
    	<br>
    	<label for="message" >IP :</label> 
    	<input type="text" placeholder="Ip Nodo" required />
    	<label for="message">Tipo Nodo :</label> 
    	<input type="text" placeholder="Tipo" required />

    	<label for="message">ColumnFamily :</label> 
    	<input type="text" placeholder="ColumnFamily" required />
     	<label for="message">KeySpace :</label> 
    	<input type="text" placeholder="KeySpace" required />
		<br>
    	<input id="agregarnodoboton" type="submit" onclick=" this.value='Enviado'"value="Enviar"/> 
    <br>
    <br>
    </div>

    	<br>

    		<div class="row-fluid">
				<div class="span12">
					<div id="copy" align="bottom">
						<h4>Copyright (C) 2015 Mercado de Alimentos MERCAL, C.A. Rif: G-200035919</h4><a href="http://phd2014consultores.com/">Impulsado por PhD 2014 Consultores C.A.</a>
					</div>
				</div>
			</div>  
	


	<c:url value="/logout" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form>
	<script>
		function formSubmit() {
			document.getElementById("logoutForm").submit();
		}
	</script>

	<c:if test="${pageContext.request.userPrincipal.name != null}">
            <h2 style="float:right;">
			Bienvenido : ${pageContext.request.userPrincipal.name} | <a
				href="javascript:formSubmit()"> Salir</a>
		</h2>
	</c:if>

</body>
</html>