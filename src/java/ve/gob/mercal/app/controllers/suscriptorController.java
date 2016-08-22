/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mercal.app.controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.phdconsultores.ws.exception.ExcepcionServicio;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ve.gob.mercal.app.models.existeCampo;
import ve.gob.mercal.app.services.WsFuncionApp;
import ve.gob.mercal.app.services.WsQuery;

/**
 *
 * @author phd2014
 */

@Controller
@Scope("request")
public class suscriptorController {
    
    @Autowired
    public WsQuery wsQuery;
    @Autowired
    public WsFuncionApp wsFuncionApp;
    @Autowired
    public existeCampo existeCampo;
    
    private String tienda = "";
    public List<String> listString = new ArrayList<>();
    public List<String> listString2 = new ArrayList<>();
    public List<String> listString3 = new ArrayList<>();
    public List<String> listString4 = new ArrayList<>();
    public Model prueba;
    
    
    @RequestMapping(value = {"/Suscriptor"}, method = {RequestMethod.GET})
    public ModelAndView getTienda(){
        ModelAndView model= new ModelAndView();
        String s = "NULL";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        try {
            s = wsQuery.getConsulta("SELECT t.tienda FROM public.susc_tiendas as st"
                    + ", public.tiendas as t, public.usuarios as u WHERE st.activo=TRUE and "
                    + "t.id_tienda=st.id_tienda and st.id_usuario=u.id_usuario and "
                    + "u.usuario='"+name+"';");
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }              
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        s = s.substring(1, s.length()-1);
        StringTokenizer st = new StringTokenizer(s,",");
        while (st.hasMoreTokens()) {
            s = st.nextToken();
            elementObject = parser.parse(s);
            if(existeCampo.existeCampo(s,"tienda")){
                this.tienda = elementObject.getAsJsonObject()
                        .get("tienda").getAsString();
            }
            listString.add("<option value=\""+this.tienda+ "\" type=\"submit\">"+
                                    this.tienda+"</option>");            
        }                          
        model.addObject("tienda", listString);
        model.setViewName("Suscriptor");
        return model;
    }
        
        
        @RequestMapping(value = {"/Suscriptor"}, method = RequestMethod.POST)
    public ModelAndView publicacionesEjecutadas(@RequestParam (value = "listString", required = false) 
                                                    String nameTienda){
        ModelAndView model = new ModelAndView();
        model=getTienda();
        String s2 = "NULL";
        if(!nameTienda.equals("NONE")) {
            String valor="";
            try {
                s2 = wsQuery.getConsulta("SELECT pe.nro_control_plan, t.tienda, j.job, "
                        + "pe.tipo_ejecucion, pe.timestamp_planificacion, pe.nro_control_ejec, "
                        + "pe.revisado, pe.observaciones FROM public.plan_ejecuciones as pe , "
                        + "public.tiendas as t, public.jobs as j WHERE pe.activo=TRUE and "
                        + "pe.id_tienda=t.id_tienda and pe.id_job=j.id_job and "
                        + "t.tienda='"+nameTienda+"';");
            } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
            if(s2=="[]"){
            model.addObject("publicacionnull","No posee planificación asociada");
            }else{
            JsonParser parser = new JsonParser();
            JsonElement elementObject;
            s2 = s2.substring(1, s2.length()-1);
            StringTokenizer st = new StringTokenizer(s2,"}");
            int planif=1;
            String result="";
            while (st.hasMoreTokens()) {
                s2 = st.nextToken()+"}";
                if (s2.substring(0,1).equals(",")){
                    s2 = s2.substring(1);                           
                }
                elementObject = parser.parse(s2);
                result= result + "Planificación: "+planif+"\n";
                if(existeCampo.existeCampo(s2,"nro_control_plan")){
                    valor = elementObject.getAsJsonObject().get("nro_control_plan").getAsString();
                    result= result + "Nro_control_plan = "+valor+"\n";
                    listString3.add(valor);
                }
                if(existeCampo.existeCampo(s2,"tienda")){
                    valor = elementObject.getAsJsonObject().get("tienda").getAsString();
                    result= result + "Tienda = "+valor+"\n";
                }
                if(existeCampo.existeCampo(s2,"job")){
                    valor = elementObject.getAsJsonObject().get("job").getAsString();
                    result= result + "Job = "+valor+"\n";
                }
                if(existeCampo.existeCampo(s2,"tipo_ejecucion")){
                    valor = elementObject.getAsJsonObject().get("tipo_ejecucion").getAsString();
                    result= result + "Tipo_ejecución = "+valor+"\n";
                }
                if(existeCampo.existeCampo(s2,"timestamp_planificacion")){
                    valor = elementObject.getAsJsonObject().get("timestamp_planificacion").getAsString();
                    result= result + "Timestamp_planificación = "+valor+"\n";
                }
                if(existeCampo.existeCampo(s2,"nro_control_ejec")){
                    valor = elementObject.getAsJsonObject().get("nro_control_ejec").getAsString();
                    result= result + "Nro_control_ejec = "+valor+"\n";
                }
                if(existeCampo.existeCampo(s2,"revisado")){
                    valor = elementObject.getAsJsonObject().get("revisado").getAsString();
                    result= result + "Revisado = "+valor+"\n";
                }
                if(existeCampo.existeCampo(s2,"observaciones")){
                    valor = elementObject.getAsJsonObject().get("observaciones").getAsString();
                    result= result + "Observaciones = "+valor+"\n";
                }
                listString2.add(result+"\n\n");
                planif++;
                valor="";
                result="";
            }
            model.addObject("vaciar","vaciar");
            model.addObject("publicacion3",listString3);
            model.addObject("publicacion2", nameTienda);
            model.addObject("publicacion", listString2);
            }
            model.setViewName("Suscriptor");
        }
 
        return model;
    }
        
        @RequestMapping(value = {"/Suscriptor2"}, method = RequestMethod.POST)
    public ModelAndView detalle(@RequestParam (value = "listString2", required = false) 
                                                    String nroControl){
        ModelAndView model = new ModelAndView();
        model=getTienda();

        String s2 = "NULL";
        try {
                s2 = wsQuery.getConsulta("SELECT et.id_etl,e.etl, et.status_ejec, "
                        + "et.reg_insertados, et.reg_actualizados FROM public.ejecucion_etls as "
                        + "et,public.etls as e WHERE et.id_ejecucion="+nroControl+
                        " and et.activo=TRUE and et.id_etl=e.id_etl;");
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(s2=="[]"){
            model.addObject("detalle1", "No posee detalle asociado");
        }else{
            listString2.clear();
            s2 = s2.substring(1, s2.length()-1);
            StringTokenizer st = new StringTokenizer(s2,"}");
            JsonParser parser = new JsonParser();
            JsonElement elementObject;
            String result="";
            String valor="";
            while (st.hasMoreTokens()) {
                s2 = st.nextToken()+"}";
                if (s2.substring(0,1).equals(",")){
                    s2 = s2.substring(1);                           
                }
                elementObject = parser.parse(s2);
                if(existeCampo.existeCampo(s2,"id_etl")){
                    valor = elementObject.getAsJsonObject().get("id_etl").getAsString();
                    result= result + "Id del etl = "+valor+"\n";
                }
                if(existeCampo.existeCampo(s2,"etl")){
                    valor = elementObject.getAsJsonObject().get("etl").getAsString();
                    result= result + "Etl = "+valor+"\n";
                }
                if(existeCampo.existeCampo(s2,"status_ejec")){
                    valor = elementObject.getAsJsonObject().get("status_ejec").getAsString();
                    result= result + "Estatus de la Ejecución = "+valor+"\n";
                }
                if(existeCampo.existeCampo(s2,"reg_insertados")){
                    valor = elementObject.getAsJsonObject().get("reg_insertados").getAsString();
                    result= result + "Registros Insertados = "+valor+"\n";
                }
                if(existeCampo.existeCampo(s2,"reg_actualizados")){
                    valor = elementObject.getAsJsonObject().get("reg_actualizados").getAsString();
                    result= result + "Registros Actualizados = "+valor+"\n";
                }
                listString2.add(result+"\n\n");
                valor="";
                result="";
            }
            model.addObject("vaciar","vaciar");
            model.addObject("detalle", listString2);
        }
        model.setViewName("Suscriptor");
        return model;
    }
    
    @RequestMapping(value = {"/Consulta"}, method = RequestMethod.GET)
    public ModelAndView getConsulta(){
        ModelAndView model = new ModelAndView();        
        
        model.setViewName("Consulta");
        return model;
    }
    
    @RequestMapping(value = {"/Consulta"}, method = RequestMethod.POST)
    public ModelAndView postConsulta(@RequestParam (value = "query", required = false) String query, 
                                        @RequestParam (value = "fechaIni", required = false) String fechaIni, 
                                        @RequestParam (value = "fechaFin", required = false) String fechaFin){
        ModelAndView model = new ModelAndView();        
        String s = "NULL";
        if(fechaIni.equals("") && fechaFin.equals("")){
        
            s = "ejecuto el query sin fechas";
            
        }else{
            if(!fechaIni.equals("") && !fechaFin.equals("")){
            
                s = "Ejecuto el query con ambas fechas";
                
            }else{
                if(!fechaIni.equals("")){
                
                    s = "ejecuto el query con fecha de inicio";
                
                }else{
                
                    s = "ejecuto el query con fecha de fin";
                    
                }
            }
        }
        model.addObject("resultado", s);
        model.setViewName("Consulta");
        return model;
    }
    
    @RequestMapping(value = {"/SuscriptorPrincipal"}, method = {RequestMethod.GET})
    public ModelAndView getsuscriptorprincipal(){
        ModelAndView model= new ModelAndView();
        String s = "NULL";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        try {
            s = wsQuery.getConsulta("SELECT  t.tienda\n" +
"  FROM public.tiendas as t\n" +
"  WHERE t.activo=true ;");
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }              
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        s = s.substring(1, s.length()-1);
        StringTokenizer st = new StringTokenizer(s,",");
        while (st.hasMoreTokens()) {
            s = st.nextToken();
            elementObject = parser.parse(s);
            if(existeCampo.existeCampo(s,"tienda")){
                this.tienda = elementObject.getAsJsonObject()
                        .get("tienda").getAsString();
            }
            listString.add("<option value=\""+this.tienda+ "\"type=\"submit\">"+
                                    this.tienda+"</option>");            
        }                          
        model.addObject("tienda", listString);
        model.setViewName("SuscriptorPrincipal");
        return model;
    }
    
    @RequestMapping(value = {"/SuscriptorPrincipal"}, method = {RequestMethod.POST})
    public ModelAndView postsuscriptorprincipal(@RequestParam (value = "listString", required = false) 
                                                    String nameTienda){
        ModelAndView model= new ModelAndView();
        model=getsuscriptorprincipal();
        String s = "NULL";
        String s2 = "NULL";
        String s3 = "NULL";
        int result=-999;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        try {
                 s = wsQuery.getConsulta("SELECT id_tienda FROM tiendas\n" +
                        "  WHERE tienda='"+nameTienda+"';");
                 s = s.substring(1, s.length()-1);
                 elementObject = parser.parse(s);
                 if(existeCampo.existeCampo(s,"tienda")){
                    s = elementObject.getAsJsonObject()
                       .get("id_tienda").getAsString();
                 }
                 s3 = wsQuery.getConsulta("SELECT id_usuario FROM usuarios "
                         + "WHERE usuario='"+name +"' and activo=TRUE;");
                 s3 = s3.substring(1, s3.length()-1);
                 elementObject = parser.parse(s3);
                if(existeCampo.existeCampo(s3,"id_usuario")){
                    s3 = elementObject.getAsJsonObject()
                       .get("id_usuario").getAsString();
                }
                 s2 = wsQuery.getConsulta("SELECT id_usuario, id_tienda\n" +
"  FROM public.susc_tiendas\n" +
"  WHERE activo=true and id_usuario='"+s3+"' and id_tienda='"+s+"';");
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        if(s2=="[]"){          
            try {
                result = wsFuncionApp.getConsulta("public.insert_susc_tiendas("+s3+", "+s+", "+s3+");");
            } catch (ExcepcionServicio e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(result>0){    
                model.addObject("exite", "Exito al suscribirse a la tienda");
            }else{
                model.addObject("exite", "Error al suscribirse a la tienda");
            }
        }else{
            model.addObject("exite", "Usted ya se encuentra suscrito a la tienda");  
        }
            model.setViewName("SuscriptorPrincipal");
        return model;
    }  
}