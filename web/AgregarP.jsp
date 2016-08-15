<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
        <head>
        <meta charset="UTF-8">
        <meta charset="windows-1252">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Agregar Publicador</title>
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
<li class="page_item page-item-5"><a href="/PublicacionySuscripcion/Publicador">Eliminar</a></li>
<li class="page_item page-item-5"><a href="/PublicacionySuscripcion/Crear">Crear</a></li>
<li class="page_item page-item-26084"><a href="/PublicacionySuscripcion/Modificar">Modificar</a></li>
<li class="page_item page-item-10"><a href="/PublicacionySuscripcion/Detalle">Detalle</a></li>
<li class="page_item page-item-22"><a href="/PublicacionySuscripcion/AgregarP">Agregar Publicador</a></li>
<li class="page_item page-item-5"><a href="/PublicacionySuscripcion/Publicar">Publicar</a></li>
<li class="page_item page-item-5"><a href="/PublicacionySuscripcion/Psuscriptor">Publicaciones</a></li>

							</ul>
						</div>
					</div>		
				</div>
    		</div>
    	</div>    	
    </div>
	    	<br>

<div id="Bandejas" >
	<h2>Listar Publicadores</h2>
	<br>
	<div>
                <form class="form-horizontal" action="AgregarP" method="POST">
                    <select  name="listString" class="form-control" onchange="this.form.submit()">
                        <option value="NONE">Seleccione una tienda...</option>
                        <c:forEach items="${tienda}" var="item">
                            ${item}
                        </c:forEach>
                    </select>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
               
            <c:if test="${not empty publicacion}">   
             <h3>Publicadores</h3>
                <textarea id="message2" cols="30" rows="15" >
                    
                    <c:forEach items="${publicacion}" var="item2">
                            ${item2}
                    </c:forEach>
                </textarea>
            </c:if>
	<br>

    <br>
    <!--div id="Bandeja1"-->
    	<h2 >Agregar Publicador</h2>
          
             <h4>${mensaje}</h4>
              <h4>${error}</h4>
             <h4>${parametro1}</h4>   
             <h4>${parametro2}</h4>   
             
        <form class="form-horizontal" method="POST" action="AgregarPub">
         <select  name="nameTienda" class="form-control"  required>
                        <option value="NONE">Seleccione una tienda...</option>
                        <c:forEach items="${tienda2}" var="item3">
                            ${item3}
                        </c:forEach>
        </select>
            
             <select  name="namePub" class="form-control"  required>
                        <option value="NONE">seleccione un publicador...</option>
                        <c:forEach items="${publicador}" var="item4">
                            ${item4}
                        </c:forEach>
            </select>
    	
            <input type="submit" class="btn btn-primary" value="Agregar"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                
        </form>
	

	
    <!--/div-->
	<br>
	<br>
	<br>
    <!--div id="Bandeja2"-->
    	<h2 color="red">Retirar Publicador</h2>
        
            <h4>${mensaje2}</h4>
              <h4>${error2}</h4>
        
    	<form class="form-horizontal" method="POST" action="EliminarPub">
         <select  name="nameTienda2" class="form-control"  required>
                        <option value="NONE">Seleccione una tienda...</option>
                        <c:forEach items="${tienda2}" var="item3">
                            ${item3}
                        </c:forEach>
        </select>
            
             <select  name="namePub2" class="form-control"  required>
                        <option value="NONE">seleccione un publicador...</option>
                        <c:forEach items="${publicador2}" var="item4">
                            ${item4}
                        </c:forEach>
            </select>
    	
            <input type="submit" class="btn btn-danger" value="Retirar"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                
        </form>
    <!--/div-->
    


</div>

<br>
    		<div class="row-fluid">
				<div class="span12">
					<div id="copy" align="bottom">
						<h4>Copyright (C) 2015 Mercado de Alimentos MERCAL, C.A. Rif: G-200035919</h4>
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