<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
        <head>
        <meta charset="UTF-8">
        <meta charset="windows-1252">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Modificar Usuario</title>
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
<li class="page_item page-item-22"><a href="/PublicacionySuscripcion/gestionusuarioadmin">Gestión Usuarios</a></li>
<li class="page_item page-item-5"><a href="/PublicacionySuscripcion/gestioncp">Gestión Cluster-PDI-Tienda</a></li>
<li class="page_item page-item-10"><a href="/PublicacionySuscripcion/gestioncargas">Gestionar Cargas</a></li>

							</ul>
						</div>
					</div>		
				</div>
    		</div>
    	</div>    	
    </div>
	    	<br>

<div id="Bandejas" >

            <h2>Modificar Usuario</h2>

        <br>
        <c:if test="${not empty existe}">
            <script language="JavaScript">
                {
                    alert("No hay usuarios para modificar..!!");
                }
            </script>
        </c:if>
        <c:if test="${empty vaciar}">
                <form class="form-horizontal" action="ModificarUsuario" method="POST">
                    <select  name="listString" class="form-control" onchange="this.form.submit()">
                        <option value="NONE">Seleccione un usuario...</option>
                        <c:forEach items="${usuarios}" var="item">
                            ${item}
                        </c:forEach>
                    </select>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
         </c:if>
    <br>
    <c:if test="${not empty pseudonimo}"> 
    <form class="form-horizontal" action="ModificarUsuario2" method="POST">
        <c:if test="${ empty exito}">   
        <label for="message">Datos a Modificar :</label> 
            <label for="message">Pseudonimo :</label> 
            <input required name="pseudonimo" type="text" placeholder="Pseudonimo" value="${pseudonimo}" />
            <br>
            <label for="message">Nombre :</label> 
            <input required name="nombre" type="text" placeholder="Nombre" value="${nombre}" />
            <br>
            <label for="message">Apellido :</label> 
            <input required name="apellido" type="text" placeholder="Apellido" value="${apellido}" />
            <br>
            <label for="message">Email :</label> 
            <input required name="email" ype="text" placeholder="Email" value="${email}" />
            <br>
            <label for="message">Nueva Contraseña :</label> 
            <input name="pass" type="password" placeholder="Contraseña" required />
            <br>
            <label for="message">Tipo Usuario Actual :</label> 
            <input disabled name="tip" type="text" placeholder="Tipo" value="${tipo}" />
            <br>
            <label for="message">Nuevo Tipo Usuario :</label> 
                    <select  name="tipo" class="form-control" >
                        <option value="administrador">Administrador</option>
                        <option value="publicador">Publicador</option>
                        <option value="suscriptor">Suscriptor</option>
                    </select>
            <br>
            <input name="id" type="hidden" value=${id}>
            <input type="submit" value="Modificar"/>
            <br>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />   
        </form>
        </c:if>
    </c:if>

            <c:if test="${ exito == 'Usuario Modificado'}">
                    <script language="JavaScript">
                        {
                            alert("Usuario modificado exitosamente");
                        }
                    </script>
             </c:if>
             <c:if test="${ exito == 'Fallo al modificar el usuario'}">
                    <script language="JavaScript">
                        {
                            alert("No se pudo modificar el usuario");
                        }
                    </script>
             </c:if>       
            


</div>
        


<br>


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