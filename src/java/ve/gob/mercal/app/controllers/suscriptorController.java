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
            this.tienda = elementObject.getAsJsonObject()
                    .get("tienda").getAsString();
            listString.add("<option value="+this.tienda+ "type=\"submit\">"+
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
        nameTienda = nameTienda.substring(0,nameTienda.length()-13);
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
                valor = elementObject.getAsJsonObject().get("nro_control_plan").getAsString();
                result= result + "Nro_control_plan = "+valor+"\n";
                listString3.add(valor);
                valor = elementObject.getAsJsonObject().get("tienda").getAsString();
                result= result + "Tienda = "+valor+"\n";
                valor = elementObject.getAsJsonObject().get("job").getAsString();
                result= result + "Job = "+valor+"\n";
                valor = elementObject.getAsJsonObject().get("tipo_ejecucion").getAsString();
                result= result + "Tipo_ejecución = "+valor+"\n";
                valor = elementObject.getAsJsonObject().get("timestamp_planificacion").getAsString();
                result= result + "Timestamp_planificación = "+valor+"\n";
                valor = elementObject.getAsJsonObject().get("nro_control_ejec").getAsString();
                result= result + "Nro_control_ejec = "+valor+"\n";
                valor = elementObject.getAsJsonObject().get("revisado").getAsString();
                result= result + "Revisado = "+valor+"\n";
                valor = elementObject.getAsJsonObject().get("observaciones").getAsString();
                result= result + "Observaciones = "+valor+"\n";
                listString2.add(result);
                planif++;
                valor="";
                result="";
            }
            model.addObject("tienda",nameTienda);
            model.addObject("publicacion3",listString3);
            model.addObject("publicacion2", nameTienda);
            model.addObject("publicacion", listString2);
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
        s2 = s2.substring(1, s2.length()-1);
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        elementObject = parser.parse(s2);
        String result="Datos:\n";
        result = result + "Número de Control = " + elementObject.getAsJsonObject().get("id_etl").getAsString() + "\n";
        result = result + "ETL = "+elementObject.getAsJsonObject().get("etl").getAsString()+"\n";
        result = result + "Estatus Ejecución = "+elementObject.getAsJsonObject().get("status_ejec").getAsString()+"\n";
        result = result + "Registros Insertados = "+elementObject.getAsJsonObject().get("reg_insertados").getAsString()+"\n";
        result = result + "Registros Actualizados = "+elementObject.getAsJsonObject().get("reg_actualizados").getAsString()+"\n";
       
        model.addObject("detalle", result);
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
   
}