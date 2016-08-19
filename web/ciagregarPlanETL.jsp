<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
    <head>
        <meta charset="UTF-8">
        <meta charset="windows-1252">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Planificación CargaInicialETL</title>
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
                    <div id="barra">
                        <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                            <span class="icon-th-list"></span>
			</a>
			<div class="span12 nav-collapse nav pull-left" style="text-align=center; width: 100%;">
                            <ul id="dropmenu" class="menu_redondeado" style="display:inline-block;">
                                <li class="page_item page-item-5"><a href="/PublicacionySuscripcion/GestionTienda">Gestionar Tiendas</a></li>
                                <li class="page_item page-item-22"><a href="/PublicacionySuscripcion/GestionAgregarP">Gestionar Publicador</a></li>
                                <li class="page_item page-item-5"><a href="/PublicacionySuscripcion/GestionPublicar">Publicar</a></li>
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
        
        
            <div>   
                <h2>Seleccione una Tienda</h2>        
                <select  name="nombreTienda" class="form-control">
                    <option value="NONE">Seleccione una tienda...</option>
                    <c:forEach items="${tienda}" var="item">
                        ${item}
                    </c:forEach>
                </select>
            </div>
            <div id="Bandeja1">
                <h4 color="red">Etl ejecutados correctamente: </h4>
                <h5>${mensaje_ejec}</h5>
                <c:if test="${empty lista_etl_ejec}">
                <textarea id="message2" cols="30" rows="15" disabled >
                    
                    <c:forEach items="${ejecutado}" var="item2">
                            ${item2}
                    </c:forEach>
                </textarea>
                </c:if>
                <h5>${msj_ejec}</h5>
                <c:if test="${not empty plan_ejec}">
                    <form class="form-horizontal" action="ejecutaETL" method="POST">
                        <select  name="ejecutadas" class="form-control" onchange="this.form.submit()">
                            <option value="NONE" >Seleccione una planificacion a detallar...</option>
                            <c:forEach items="${plan_ejec}" var="item">
                                ${item}
                            </c:forEach>
                        </select>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    </form>
               </c:if>
                
                <c:if test="${not empty lista_etl_ejec}">
                     <textarea id="message2" cols="30" rows="15" disabled >
                    
                    <c:forEach items="${lista_etl_ejec}" var="item2">
                            ${item2}
                    </c:forEach>
                </textarea>
                   
               </c:if>
                <br>
               
            </div>
            <div id="Bandeja1">
                <h4 color="red">Etl ejecutados con fallas:</h4>
                <h5>${mensaje_ter}</h5>
                <c:if test="${empty lista_etl_ter}">
                <textarea id="message2" cols="30" rows="15" disabled >
                    
                    <c:forEach items="${terminado}" var="item2">
                            ${item2}
                    </c:forEach>
                </textarea>
                </c:if>
                <h5>${msj_ter}</h5>
                <c:if test="${not empty plan_ter}">
                    <form class="form-horizontal" action="terminaETL" method="POST">
                        <select  name="terminadas" class="form-control" onchange="this.form.submit()">
                            <option value="NONE" >Seleccione una planificacion a detallar...</option>
                            <c:forEach items="${plan_ter}" var="item">
                                ${item}
                            </c:forEach>
                        </select>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    </form>
                </c:if>
                
                <c:if test="${not empty lista_etl_ter}">
                   <textarea id="message2" cols="30" rows="15" disabled >
                    
                    <c:forEach items="${lista_etl_ter}" var="item2">
                            ${item2}
                    </c:forEach>
                </textarea>
                   
               </c:if>
                <br>
                <br>
            </div>
            <div>   
                <h2>Seleccione un ETL</h2>        
                <select  name="nombreTienda" class="form-control">
                    <option value="NONE">Seleccione un ETL...</option>
                    <c:forEach items="${etl}" var="item">
                        ${item}
                    </c:forEach>
                </select>
            </div>
            <h4>Introduzca una Fecha</h4>
            <input type="text"  placeholder="yyyy-MM-dd" pattern="(?:20)[0-9]{2}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-9])|(?:(?!02)(?:0[1-9]|1[0-2])-(?:30))|(?:(?:0[13578]|1[02])-31))" name="fecha" required/>
            <br>
            <h4>Introduzca una Hora</h4>
            <input type="text" placeholder="hh-mm-ss" pattern="([01]?[0-9]{1}|2[0-3]{1}):[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}" name="hora" required/>
            <br>
            <input type="submit" value="planificar"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <br>

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