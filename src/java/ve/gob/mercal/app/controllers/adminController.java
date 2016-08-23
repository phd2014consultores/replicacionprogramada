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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import ve.gob.mercal.app.models.nombreTienda;
import ve.gob.mercal.app.services.WsFuncionApp;
import ve.gob.mercal.app.services.WsQuery;
/**
 *
 * @author phd2014
 */
@Controller
@Scope("request")
public class adminController {
    @Autowired
    public WsFuncionApp WsFuncion;
    
    @Autowired
    public WsQuery wsQuery;
    
    private String ejec = "";
    
    public boolean existeCampo(String json,String palabra){   
        
        if(json.toLowerCase().contains(palabra.toLowerCase())){
        return true;
        }
        
    return false;
    }
    
    @RequestMapping(value = {"/gestioncargas"}, method = {RequestMethod.GET})
    public ModelAndView getCargas(){
        ModelAndView model= new ModelAndView();
    
        model.setViewName("gestioncargas");
        return model;
    }
    //Obtener cargas planificadas
    @RequestMapping(value = {"/cargasplanif"}, method = {RequestMethod.POST})
    public ModelAndView getCargas_planif(){
        ModelAndView model= new ModelAndView();
        String plan="";
        String result = "";
        String valor="";
        String aux="";
        List<String> listStringPlan = new ArrayList<>();
        List<String> lista_plan = new ArrayList<>();
        try {
                plan=wsQuery.getConsulta("SELECT pe.id_plan_ejecucion, pe.nro_control_plan, t.tienda, j.job,pe.timestamp_planificacion, pe.nro_control_ejec, pe.observaciones\n" +
                    "  FROM public.plan_ejecuciones as pe, public.pasos_plan_ejecucion as ppe, public.tiendas as t, public.jobs as j\n" +
                    "  WHERE pe.activo=TRUE and pe.tipo_ejecucion='planificada' and pe.id_plan_ejecucion=ppe.id_plan_ejecucion and ppe.status_plan='en espera' and pe.id_tienda=t.id_tienda and pe.id_job=j.id_job;");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        
         if(!plan.equals("[]")){
             JsonParser parser = new JsonParser();
             JsonElement elementObject;
             plan = plan.substring(1, plan.length()-1);
             StringTokenizer st = new StringTokenizer(plan,"}");
            
             while (st.hasMoreTokens()) {
                    plan = st.nextToken()+"}";
                    if (plan.substring(0,1).equals(",")){
                        plan = plan.substring(1);                          
                    }
                    elementObject = parser.parse(plan);
                    valor = elementObject.getAsJsonObject().get("id_plan_ejecucion").getAsString();
                    result= result + "Id Plan Ejecucion: "+valor+"\n";
                    if(existeCampo(plan,"nro_control_plan")){
                    valor = elementObject.getAsJsonObject().get("nro_control_plan").getAsString();
                    result= result + "Nro Control Plan= "+valor+"\n";
                    }
                    if(existeCampo(plan,"tienda")){
                    valor = elementObject.getAsJsonObject().get("tienda").getAsString();
                    result= result + "Tienda = "+valor+"\n";
                    }
                    if(existeCampo(plan,"job")){
                    valor = elementObject.getAsJsonObject().get("job").getAsString();
                    result= result + "Job = "+valor+"\n";
                    }
                    if(existeCampo(plan,"nro_control_ejec")){
                    valor = elementObject.getAsJsonObject().get("nro_control_ejec").getAsString();
                    result= result + "Nro Control Ejecucion= "+valor+"\n";
                    }
                    if(existeCampo(plan,"observaciones")){
                    valor = elementObject.getAsJsonObject().get("observaciones").getAsString();
                    result= result + "Observaciones= "+valor+"\n";
                    }
                    if(existeCampo(plan,"timestamp_planificacion")){
                        valor = elementObject.getAsJsonObject().get("timestamp_planificacion").getAsString();
                        result= result + "Tiempo de la planificacion = "+valor+"\n";
                    }
                    aux=elementObject.getAsJsonObject().get("id_plan_ejecucion").getAsString(); 
                    listStringPlan.add(result);
                    lista_plan.add("<option value="+aux+">"+
                                    aux+"</option>");
                    valor="";
                    result="";
                    aux="";
                }
             
            model.addObject("planificado",listStringPlan);
            model.addObject("plan_list", lista_plan); //Para listar las que se anularan
         }else{
         
            model.addObject("mensaje_plan","error");
         }
        
        model.setViewName("gestioncargas");
        return model;
    }
    //Obtener cargas en ejecucion
     @RequestMapping(value = {"/cargasejec"}, method = {RequestMethod.POST})
    public ModelAndView getCargas_ejec(){
        ModelAndView model= new ModelAndView();
        String ejec="";
        String result="";
        String aux="";
        String valor="";
        List<String> listStringEjec = new ArrayList<>();
        List<String> lista_ejec = new ArrayList<>();
        
        try {
                ejec=wsQuery.getConsulta("SELECT pe.id_plan_ejecucion,pe.nro_control_plan, t.tienda, j.job, pe.timestamp_planificacion, pe.nro_control_ejec, pe.revisado, pe.observaciones\n" +
                    "  FROM public.plan_ejecuciones  as pe, public.pasos_plan_ejecucion as ppe, public.tiendas as t, public.jobs as j\n" +
                    "  WHERE pe.activo=TRUE and pe.id_tienda=t.id_tienda and pe.id_job=j.id_job and pe.id_plan_ejecucion=ppe.id_plan_ejecucion and ppe.status_plan='a ejecucion';");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        if(!ejec.equals("[]")){
            JsonParser parser = new JsonParser();
            JsonElement elementObject;
                ejec = ejec.substring(1, ejec.length()-1);
                StringTokenizer st = new StringTokenizer(ejec,"}");
            while (st.hasMoreTokens()) {
                    ejec = st.nextToken()+"}";
                    if (ejec.substring(0,1).equals(",")){
                        ejec = ejec.substring(1);                          
                    }
                    elementObject = parser.parse(ejec);
                    valor = elementObject.getAsJsonObject().get("id_plan_ejecucion").getAsString();
                    result= result + "Id Plan Ejecucion: "+valor+"\n";
                     if(existeCampo(ejec,"nro_control_pla")){
                    valor = elementObject.getAsJsonObject().get("nro_control_plan").getAsString();
                    result= result + "Nro Control Plan= "+valor+"\n";
                    }
                    if(existeCampo(ejec,"tienda")){
                    valor = elementObject.getAsJsonObject().get("tienda").getAsString();
                    result= result + "Tienda = "+valor+"\n";
                    }
                    if(existeCampo(ejec,"job")){
                    valor = elementObject.getAsJsonObject().get("job").getAsString();
                    result= result + "Job = "+valor+"\n";
                    }
                    if(existeCampo(ejec,"nro_control_ejec")){
                    valor = elementObject.getAsJsonObject().get("nro_control_ejec").getAsString();
                    result= result + "Nro Control Ejecucion= "+valor+"\n";
                    }
                     if(existeCampo(ejec,"revisado")){
                        valor = elementObject.getAsJsonObject().get("revisado").getAsString();
                        result= result + "Revisado = "+valor+"\n";
                    }
                    if(existeCampo(ejec,"observaciones")){
                    valor = elementObject.getAsJsonObject().get("observaciones").getAsString();
                    result= result + "Observaciones= "+valor+"\n";
                    }
                    if(existeCampo(ejec,"timestamp_planificacion")){
                        valor = elementObject.getAsJsonObject().get("timestamp_planificacion").getAsString();
                        result= result + "Tiempo de la planificacion = "+valor+"\n";
                    }
                    aux=elementObject.getAsJsonObject().get("id_plan_ejecucion").getAsString();; 
                    listStringEjec.add(result);
                    lista_ejec.add("<option value="+aux+ ">"+
                                    aux+"</option>");
                    valor="";
                    result="";
                    aux="";

                }
        
            model.addObject("ejecutado",listStringEjec);
            model.addObject("plan_ejec",lista_ejec );
        }else{
        
            model.addObject("mensaje_ejec","error");
        }
        model.setViewName("gestioncargas");
        return model;
    }
    
    @RequestMapping(value = {"/planifica"}, method = RequestMethod.POST)
    public ModelAndView planifETL(
            @RequestParam(value = "planificadas",required = false) String planificadas)
    {
        ModelAndView model = new ModelAndView();     
        String etl_plan="";
        String id_usr="";
        int funcion = 0;
        int actual=0;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        int plan;
        
       
        if(!planificadas.equals("NONE")) {
            plan=Integer.parseInt(planificadas);
            
            try {
                id_usr = wsQuery.getConsulta("SELECT id_usuario\n" +
                                "FROM public.usuarios \n" +
                                "WHERE usuario= '"+name+"'");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            id_usr = id_usr.substring(1, id_usr.length()-1);
            JsonParser parser = new JsonParser();
            JsonElement elementObject;
            elementObject = parser.parse(id_usr);
            actual=elementObject.getAsJsonObject().get("id_usuario").getAsInt();
            
            try {
                funcion = WsFuncion.getConsulta("public.insert_pasos_plan_ejecucion("+plan+","+"'anulada'"+","+actual+");");
            } catch (Exception ex) {
                Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            if(funcion>0){
            model.addObject("msj_planif","Planificacion anulada con Exito !!");
            }else{
            model.addObject("param1", plan);
            model.addObject("param2", actual);
            
            model.addObject("msj_planif","Error al anular la tarea planificada");
            model.addObject("error", funcion);
            }
            
             
        }
        
        model.setViewName("gestioncargas");
        return model;
    }
    
    
    @RequestMapping(value = {"/gestionusuarioadmin"}, method = {RequestMethod.GET})
        public ModelAndView getgestionusuarioAdmin(){
        ModelAndView model= new ModelAndView();
        model.setViewName("gestionusuarioadmin");
        return model;
    }
        
        @RequestMapping(value = {"/CrearUsuario"}, method = {RequestMethod.GET})
        public ModelAndView getcrearusuarioAdmin(){
        ModelAndView model= new ModelAndView();
        model.setViewName("CrearUsuario");
        return model;
    }
        
        @RequestMapping(value = {"/CrearUsuario"}, method = {RequestMethod.POST})
        public ModelAndView postcrearusuarioAdmin(@RequestParam(value = "pseudonimo", required = false) String pseudonimo,
                                                @RequestParam(value = "nombre", required = false) String nombre,
                                                @RequestParam(value = "apellido", required = false) String apellido,
                                                @RequestParam(value = "email", required = false) String email,
                                                @RequestParam(value = "pass", required = false) String pass,
                                                @RequestParam(value = "tipo", required = false) String tipo){
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        int aux=0;
        int result=-999;
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        if(tipo.equals("administrador")){
         aux=1;
         }
         if(tipo.equals("publicador")){
         aux=2;
         }
         if(tipo.equals("suscriptor")){
         aux=3;
         }
        try {
                name = wsQuery.getConsulta("SELECT id_usuario FROM usuarios WHERE usuario='"+name +"';");
                name = name.substring(1, name.length()-1);
                elementObject = parser.parse(name);
                name = elementObject.getAsJsonObject()
                    .get("id_usuario").getAsString();
               
                result = WsFuncion.getConsulta("public.insert_usuario('"+pseudonimo+"','"+nombre+"','"+apellido+"','"+email+"','"+pass+"',"+aux+","+name+");");

            } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
//        
        if(result>=1){
            model.addObject("exito","Usuario Creado");
        }else{
            model.addObject("exito","Fallo al crear el Usuario");
        }

        model.setViewName("CrearUsuario");
        return model;
    }     
        
    @RequestMapping(value = {"/ModificarUsuario"}, method = {RequestMethod.GET})
        public ModelAndView getmodificarusuarioAdmin(){
        ModelAndView model= new ModelAndView();
        model.setViewName("ModificarUsuario");
        return model;
    }    
        
        
    
    
      @RequestMapping(value = {"/gestioncp"}, method = {RequestMethod.GET})
        public ModelAndView getgestioncpAdmin(){
        ModelAndView model= new ModelAndView();
        model.setViewName("gestioncp");
        return model;
    }
    
}
