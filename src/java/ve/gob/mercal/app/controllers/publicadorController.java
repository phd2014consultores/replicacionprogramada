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
public class publicadorController {
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
        @RequestMapping(value = {"/Publicador"}, method = {RequestMethod.GET})
	public ModelAndView pagegetpublicador(){
        ModelAndView model= new ModelAndView();
        String s = "NULL";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        try {
            s = wsQuery.getConsulta("SELECT t.tienda FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and t.activo=true and pt.id_usuario=u.id_usuario and u.usuario='"+name+"';");
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
            listString.add(this.tienda);
            listString2.add("<option value=\""+this.tienda+ "\" type=\"submit\">"+
                                    this.tienda+"</option>"); 
        }                         
        model.addObject("tienda", listString);
        model.addObject("tienda2", listString2);
        model.setViewName("Publicador");
        return model;
	}
        
        @RequestMapping(value = {"/Publicador"}, method = {RequestMethod.POST})
	public ModelAndView pagepostpublicador(@RequestParam (value = "listString", required = false)
                                                    String nameTienda){
        ModelAndView model = new ModelAndView();
       
        int s2 = -999;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String s="";
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        String s3="";
        if(!nameTienda.equals("NONE")) {
            String valor="";
            try {
                 s = wsQuery.getConsulta("SELECT id_tienda FROM tiendas\n" +
                        "  WHERE tienda='"+nameTienda+"';");
                 s = s.substring(1, s.length()-1);
                 elementObject = parser.parse(s);
                 s = elementObject.getAsJsonObject()
                    .get("id_tienda").getAsString();
                 s3 = wsQuery.getConsulta("SELECT id_usuario FROM usuarios "
                         + "WHERE usuario='"+name +"';");
                 s3 = s3.substring(1, s3.length()-1);
                 elementObject = parser.parse(s3);
                 s3 = elementObject.getAsJsonObject()
                    .get("id_usuario").getAsString();
                s2 = wsFuncionApp.getConsulta("public.delete_tiendas("+s+","+s3+",false);");
            } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
            }
        model=pagegetpublicador();
        
        if(s2==1){
            model.addObject("mensaje","exito");
            
        }else{
            model.addObject("mensaje","error");
        }
        model.setViewName("Publicador");
        return model;
	}
       
        /******************************************************************/
        
        @RequestMapping(value = {"/Psuscriptor"}, method = {RequestMethod.GET})
    public ModelAndView gettiendaPsuscriptor(){
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
        if(!s.equals("[]")){
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
        }else{
            model.addObject("existe", "error");
        }
        model.setViewName("Psuscriptor");
        return model;
    }
        
        
        @RequestMapping(value = {"/Psuscriptor"}, method = RequestMethod.POST)
    public ModelAndView publicacionesEjecutadasPsuscriptor(@RequestParam (value = "listString", required = false) 
                                                    String nameTienda){
        ModelAndView model = new ModelAndView();

        List<String> listString3 = new ArrayList<>();
        model=gettiendaPsuscriptor();
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
            model.setViewName("Psuscriptor");
        }
 
        return model;
    }
        
        @RequestMapping(value = {"/Psuscriptor2"}, method = RequestMethod.POST)
    public ModelAndView detallePsuscriptor(@RequestParam (value = "listString2", required = false) 
                                                    String nroControl){
        ModelAndView model = new ModelAndView();
        model=gettiendaPsuscriptor();

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
        model.setViewName("Psuscriptor");
        return model;
    }
    
     @RequestMapping(value = {"/Crear"}, method = RequestMethod.GET)
    public ModelAndView getcrearPublicador(){
        ModelAndView model = new ModelAndView();
        model.setViewName("Crear");
        return model;
    }
    
    @RequestMapping(value = {"/Crear"}, method = RequestMethod.POST)
    public ModelAndView postcrearPublicador(@RequestParam(value = "nombre", required = false) String nombre,
                                                @RequestParam(value = "host", required = false) String host,
                                                @RequestParam(value = "bdoracle", required = false) String bdoracle,
                                                @RequestParam(value = "user", required = false) String user,
                                                @RequestParam(value = "pass", required = false) String pass){
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String name2 = "";
        int result=-999;
        int result2=-999;
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        try {
                name = wsQuery.getConsulta("SELECT id_usuario FROM usuarios WHERE usuario='"+name +"';");
                name = name.substring(1, name.length()-1);
                elementObject = parser.parse(name);
                name = elementObject.getAsJsonObject()
                    .get("id_usuario").getAsString();
                result = wsFuncionApp.getConsulta("public.insert_tiendas('"+nombre+"','"+host+"','"+user+"','"+pass+"','"+bdoracle+"',"+name+");");
                name2 = wsQuery.getConsulta("SELECT id_tienda\n" +
"  FROM public.tiendas\n" +
"  WHERE tienda='"+nombre+"';");
                name2 = name2.substring(1, name2.length()-1);
                elementObject = parser.parse(name2);
                if(existeCampo.existeCampo(name2, "id_tienda")){
                    name2 = elementObject.getAsJsonObject()
                        .get("id_tienda").getAsString();
                    result2 = wsFuncionApp.getConsulta("public.insert_pub_tiendas("+name+", "+name2+", "+name+");");
                }
            } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
//        
        if(result>=1){
            model.addObject("mensaje","exito");
        }else{
            model.addObject("mensaje","error");
        }

        model.setViewName("Crear");
        return model;
    }
    
    @RequestMapping(value = {"/Modificar"}, method = RequestMethod.GET)
    public ModelAndView getmodificarPublicador(){
        ModelAndView model= new ModelAndView();
        String s = "NULL";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        try {
            s = wsQuery.getConsulta("SELECT t.tienda FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and t.activo=true and pt.id_usuario=u.id_usuario and u.usuario='"+name+"';");
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(!s.equals("[]")){
            JsonParser parser = new JsonParser();
            JsonElement elementObject;
            s = s.substring(1, s.length()-1);
            StringTokenizer st = new StringTokenizer(s,",");
            while (st.hasMoreTokens()) {
                s = st.nextToken();
                elementObject = parser.parse(s);
                this.tienda = elementObject.getAsJsonObject()
                        .get("tienda").getAsString();
                listString2.add("<option value=\""+this.tienda+ "\" type=\"submit\">"+
                                        this.tienda+"</option>"); 
            }
        model.addObject("tienda2", listString2);
        }else{
        model.addObject("existe","no existe tiendas a modificar");
        
        }
        model.setViewName("Modificar");
        return model;
    }
    
    @RequestMapping(value = {"/Modificar"}, method = RequestMethod.POST)
    public ModelAndView postmodificarPublicador(@RequestParam (value = "listString", required = false)
                                                    String nameTienda){
        ModelAndView model = new ModelAndView();
        model=getmodificarPublicador();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String id="";
        String tien="";
        String host="";
        String user="";
        String pass="";
        String bd="";
        String idm="";
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        try {
                nameTienda = wsQuery.getConsulta("SELECT id_tienda, tienda, host_bd_oracle, usuario_bd_oracle, pass_usuario_bd_oracle, \n" +
"       bd_oracle, id_manager FROM tiendas\n" +
"  WHERE activo=true and tienda='"+nameTienda +"';");
                
        nameTienda = nameTienda.substring(1, nameTienda.length()-1);
        elementObject = parser.parse(nameTienda);
            
            if(existeCampo.existeCampo(nameTienda,"id_tienda")){
                id = elementObject.getAsJsonObject().get("id_tienda").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"tienda")){    
                tien = elementObject.getAsJsonObject().get("tienda").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"host_bd_oracle")){    
                host = elementObject.getAsJsonObject().get("host_bd_oracle").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"usuario_bd_oracle")){    
                user = elementObject.getAsJsonObject().get("usuario_bd_oracle").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"pass_usuario_bd_oracle")){    
                pass = elementObject.getAsJsonObject().get("pass_usuario_bd_oracle").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"bd_oracle")){    
                bd = elementObject.getAsJsonObject().get("bd_oracle").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"id_manager")){    
                idm = elementObject.getAsJsonObject().get("id_manager").getAsString();
            }           
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
            model.addObject("vaciar","vaciar");
            model.addObject("idt",id);
            model.addObject("tienda",tien);
            model.addObject("host",host);
            model.addObject("user",user);
            model.addObject("pass",pass);
            model.addObject("bd",bd);
            model.addObject("idm",idm);
            model.setViewName("Modificar");
        return model;
    }
    
    @RequestMapping(value = {"/Modificar2"}, method = RequestMethod.POST)
    public ModelAndView postmodificar2Publicador(@RequestParam(value = "nombre", required = false) String nombre,
                                                @RequestParam(value = "host", required = false) String host,
                                                @RequestParam(value = "bdoracle", required = false) String bdoracle,
                                                @RequestParam(value = "user", required = false) String user,
                                                @RequestParam(value = "pass", required = false) String pass,
                                                @RequestParam(value = "idt", required = false) String idt,
                                                @RequestParam(value = "idm", required = false) String idm){
        ModelAndView model = new ModelAndView();
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        int result=-999;
        try {
                name = wsQuery.getConsulta("SELECT id_usuario FROM usuarios WHERE usuario='"+name +"';");
                name = name.substring(1, name.length()-1);
                elementObject = parser.parse(name);
                name = elementObject.getAsJsonObject()
                    .get("id_usuario").getAsString();
            
                result = wsFuncionApp.getConsulta("public.update_tiendas("+idt+",'"+nombre+"','"+host+"', '"+user+"', '"+pass+"', '"+bdoracle+"', "+idm+","+name+");");
        model=getmodificarPublicador();    
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        
        if(result==1){
            model.addObject("exito","Tienda Modificada");
            model.setViewName("Modificar");
        }else{
            model.addObject("exito","Fallo al modifica la Tienda");
            model.addObject("error",result);
        }
        model.setViewName("Modificar");
        return model;
    }
    

    
     
    
    @RequestMapping(value = {"/GestionTienda"}, method = RequestMethod.GET)
    public ModelAndView getgestiontiendaPublicacion(){
        ModelAndView model = new ModelAndView();
        model.addObject("tienda","prueba");
        model.setViewName("GestionTienda");
        return model;
    }
    
    
     @RequestMapping(value = {"/Detalle"}, method = RequestMethod.GET)
    public ModelAndView getdetallePublicador(){
        ModelAndView model= new ModelAndView();
        String s = "NULL";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        try {
            s = wsQuery.getConsulta("SELECT t.tienda FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and t.activo=true and pt.id_usuario=u.id_usuario and u.usuario='"+name+"';");
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
            listString2.add("<option value=\""+this.tienda+ "\" type=\"submit\">"+
                                    this.tienda+"</option>"); 
        }                         
        model.addObject("tienda2", listString2);
        model.setViewName("Detalle");
        return model;
    }
    
    @RequestMapping(value = {"/Detalle"}, method = RequestMethod.POST)
    public ModelAndView postdetallePublicador(@RequestParam (value = "listString", required = false)
                                                    String nameTienda){
        ModelAndView model = new ModelAndView();
        model=getdetallePublicador();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String id="";
        String tien="";
        String host="";
        String user="";
        String pass="";
        String bd="";
        String idm="";
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        
        try {
                nameTienda = wsQuery.getConsulta("SELECT id_tienda, tienda, host_bd_oracle, usuario_bd_oracle, pass_usuario_bd_oracle, \n" +
"       bd_oracle, id_manager FROM tiendas\n" +
"  WHERE activo=true and tienda='"+nameTienda +"';");
                
        nameTienda = nameTienda.substring(1, nameTienda.length()-1);
        elementObject = parser.parse(nameTienda);
            if(existeCampo.existeCampo(nameTienda,"id_tienda")){
                id = elementObject.getAsJsonObject().get("id_tienda").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"tienda")){    
                tien = elementObject.getAsJsonObject().get("tienda").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"host_bd_oracle")){    
                host = elementObject.getAsJsonObject().get("host_bd_oracle").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"usuario_bd_oracle")){    
                user = elementObject.getAsJsonObject().get("usuario_bd_oracle").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"pass_usuario_bd_oracle")){    
                pass = elementObject.getAsJsonObject().get("pass_usuario_bd_oracle").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"bd_oracle")){    
                bd = elementObject.getAsJsonObject().get("bd_oracle").getAsString();
            }    
            if(existeCampo.existeCampo(nameTienda,"id_manager")){    
                idm = elementObject.getAsJsonObject().get("id_manager").getAsString();
            }           

            } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
            model.addObject("vaciar","vaciar");
            model.addObject("idt",id);
            model.addObject("tienda",tien);
            model.addObject("host",host);
            model.addObject("user",user);
            model.addObject("pass",pass);
            model.addObject("bd",bd);
            model.addObject("idm",idm);
            model.setViewName("Detalle");
        return model;
    }
}
