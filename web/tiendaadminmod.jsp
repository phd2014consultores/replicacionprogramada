<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
        <head>
        <meta charset="UTF-8">
        <meta charset="windows-1252">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Administrar Tienda</title>
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
						<div class="span12 nav-collapse nav pull-left" style="text-align:center; width: 100%;">
							<ul id="dropmenu" class="menu_redondeado" style="display:inline-block;">
<li class="page_item page-item-22"><a href="/ReplicacionProgramada/gestionusuarioadmin">Gestionar Usuario</a></li>
<li class="page_item page-item-5"><a href="/ReplicacionProgramada/gestioncp">Gestionar Tienda</a></li>
<li class="page_item page-item-10"><a href="/ReplicacionProgramada/gestioncargas">Gestionar Replicación</a></li>

							</ul>
						</div>
					</div>		
				</div>
    		</div>
    	</div>    	
    </div>
    <br>
    <h1 align="center">Modificar Tienda</h1>
    <br>
    <div id="parametros">
        <form class="form-horizontal" action="tiendaadminmod" method="POST">
            <div id="parametros1">

                <label for="message">Usuario</label> 
                <input name="user" style="height:25px" type="text" placeholder="Usuario" value="${user}" title="Indique usuario de la Tienda" required  />


                <label for="message" >Nombre</label> 
                <input name="nombre" style="height:25px" type="text" placeholder="Nombre" value="${nombre}" title="Indique nombre de la Tienda" required />

            </div>
            <div id="parametros2">

                <label for="message">Contraseña</label> 
                <input name="pass" style="height:25px" type="text" placeholder="Contraseña" value="${pass}" title="Indique contraseña de la Tienda" required />


                <label for="message">Host Galera MariaDB</label> 
                <input name="host" style="height:25px" type="text" placeholder="IP" value="${host}" title="Indique Ip de la Tienda" required />


                <label for="message">Galera MariaDB</label> 
                <input name="bd" style="height:25px" type="text" placeholder="Name" value="posint_oficial" title="Nombre obligatorio de Galera" disabled />

                <br>
                <br>
                <input type="submit" value="Modificar" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </div>          
        </form>
        <c:if test="${mensaje == 'exito'}">
            <script language="JavaScript">
                {
                    alert("La tienda fue modificada correctamente..!!");
                }
            </script>   
        </c:if>
        <c:if test="${mensaje == 'error'}">
            <script language="JavaScript">
                {
                    alert("Fallo al modificar la tienda..!!");
                }
            </script>   
        </c:if>
            
        <c:if test="${mensaje == 'error2'}">
            <script language="JavaScript">
                {
                    alert("No existe ninguna tienda Registrada..!!");
                }
            </script>   
        </c:if>    
    </div>
        <div class="row-fluid">
            <div class="span12">
                <div id="copy" align="bottom">
                        <h4>Copyright (C) 2015 Mercado de Alimentos MERCAL, C.A. Rif: G-200035919</h4>
                        <p align="center"><a  href="http://phd2014consultores.com/">Impulsado por PhD 2014 Consultores C.A.</a><p>
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