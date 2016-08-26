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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ve.gob.mercal.app.models.existeCampo;
import ve.gob.mercal.app.models.listaNodo;
import ve.gob.mercal.app.services.WsAnular;
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
    @Autowired
    public existeCampo existeCampo;
    @Autowired
    public listaNodo listaNodo;
    
    @Autowired
    public WsAnular anular;
    
    private String ejec = "";
    public List<String> listString = new ArrayList<>();
    public List<String> listString2 = new ArrayList<>();
    public String aux="";
    
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
                    "  WHERE pe.activo=TRUE and pe.tipo_ejecucion='planificada' and pe.id_plan_ejecucion=ppe.id_plan_ejecucion and ppe.status_plan='en espera' and pe.id_tienda=t.id_tienda and pe.id_job=j.id_job and ppe.activo = TRUE;");
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
                    
                    if(existeCampo.existeCampo(plan,"id_plan_ejecucion")){
                        valor = elementObject.getAsJsonObject().get("id_plan_ejecucion").getAsString();
                        aux = valor;
                        result= result + "Id Plan Ejecucion = "+valor+"\n";
                    }
                    if(existeCampo(plan,"nro_control_plan")){
                        valor = elementObject.getAsJsonObject().get("nro_control_plan").getAsString();
                        result= result + "Nro Control Plan = "+valor+"\n";
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
                        result= result + "Nro Control Ejecucion = "+valor+"\n";
                    }
                    if(existeCampo(plan,"observaciones")){
                        valor = elementObject.getAsJsonObject().get("observaciones").getAsString();
                        result= result + "Observaciones = "+valor+"\n";
                    }
                    if(existeCampo(plan,"timestamp_planificacion")){
                        valor = elementObject.getAsJsonObject().get("timestamp_planificacion").getAsString();
                        result= result + "Tiempo de la planificacion = "+valor+"\n";
                    }
                    listStringPlan.add(result + "\n\n");
                    lista_plan.add("<option value=\""+aux+"\">"+
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
                            " FROM public.plan_ejecuciones  as pe, public.pasos_ejecucion as ppe, public.tiendas as t, public.jobs as j\n" +
                            "  WHERE pe.activo=TRUE and ppe.activo=TRUE and pe.id_tienda=t.id_tienda and pe.id_job=j.id_job and pe.id_plan_ejecucion=ppe.id_plan_ejecucion and ppe.status_ejecucion='en ejecucion';");
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
                    
                    if(existeCampo(ejec,"id_plan_ejecucion")){
                        valor = elementObject.getAsJsonObject().get("id_plan_ejecucion").getAsString();
                        aux = valor;
                        result= result + "Id Plan Ejecucion = "+valor+"\n";
                    }
                    if(existeCampo(ejec,"nro_control_pla")){
                        valor = elementObject.getAsJsonObject().get("nro_control_plan").getAsString();
                        result= result + "Nro Control Plan = "+valor+"\n";
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
                        result= result + "Nro Control Ejecucion = "+valor+"\n";
                    }
                    if(existeCampo(ejec,"revisado")){
                        valor = elementObject.getAsJsonObject().get("revisado").getAsString();
                        result= result + "Revisado = "+valor+"\n";
                    }
                    if(existeCampo(ejec,"observaciones")){
                        valor = elementObject.getAsJsonObject().get("observaciones").getAsString();
                        result= result + "Observaciones = "+valor+"\n";
                    }
                    if(existeCampo(ejec,"timestamp_planificacion")){
                        valor = elementObject.getAsJsonObject().get("timestamp_planificacion").getAsString();
                        result= result + "Tiempo de la planificacion = "+valor+"\n";
                    }
                    listStringEjec.add(result+"\n\n");
                    lista_ejec.add("<option value=\""+aux+ "\">"+
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
        String id_usr="";
        int funcion = 0;
        int update=0;
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
            
            try {
                update = WsFuncion.getConsulta("public.delete_pasos_plan_ejecucion("+plan+","+actual+",false);");
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
    
      @RequestMapping(value = {"/ejecucion"}, method = RequestMethod.POST)
    public ModelAndView ejecETL(
            @RequestParam(value = "ejecutadas",required = false) String ejecutadas)
    {
        ModelAndView model = new ModelAndView();     
        String id_usr="";
        int funcion = 0;
        int actual=0;
        int update=0;
        int result_anular=-1;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        int ejec;
        
       
        if(!ejecutadas.equals("NONE")) {
            ejec=Integer.parseInt(ejecutadas);
            
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
                funcion = WsFuncion.getConsulta("public.insert_pasos_ejecucion("+ejec+","+"'anulada'"+","+actual+");");
            } catch (Exception ex) {
                Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try{
                update = WsFuncion.getConsulta("public.delete_plan_ejecuciones("+ejec+","+actual+",false);");
               }  catch (Exception ex) {
                   Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
              }
                    model.addObject("msj_ejec","Planificacion anulada con Exito !!");
            
                // elimina el kitchen que se esta ejecutando

            if(funcion>0){

                    
             try{
                    result_anular=anular.anularKitchen(Integer.toString(ejec));
             } catch (ExcepcionServicio ex) {
                    Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
             }
       
            }else{
            model.addObject("param1", ejec);
            model.addObject("param2", actual);
            
            model.addObject("msj_ejec","Error al anular la tarea planificada");
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
        
        String s = "NULL";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        try {
            s = wsQuery.getConsulta("SELECT usuario FROM public.usuarios WHERE activo=true;");
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
                this.aux = elementObject.getAsJsonObject()
                        .get("usuario").getAsString();
                listString2.add("<option value=\""+this.aux+ "\" type=\"submit\">"+
                                        this.aux+"</option>"); 
            }
        model.addObject("usuarios", listString2);
        }else{
        model.addObject("existe","no existe usuarios a modificar");
        
        }
        model.setViewName("ModificarUsuario");
        return model;
    }    
    
        
    @RequestMapping(value = {"/ModificarUsuario"}, method = {RequestMethod.POST})
        public ModelAndView postmodificarusuarioAdmin(@RequestParam (value = "listString", required = false)
                                                    String usuario){
        ModelAndView model= new ModelAndView();
        model=getmodificarusuarioAdmin();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String id="";
        String pseudonimo="";
        String nombre="";
        String apellido="";
        String email="";
        String tipo="";
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        
        try {
                usuario = wsQuery.getConsulta("SELECT id_usuario, usuario, nombre, apellido, email,id_tipo_usuario FROM public.usuarios WHERE activo=true and usuario='"+usuario +"';");
                
        usuario = usuario.substring(1, usuario.length()-1);
        elementObject = parser.parse(usuario);

             id = elementObject.getAsJsonObject().get("id_usuario").getAsString();
             pseudonimo = elementObject.getAsJsonObject().get("usuario").getAsString();
             nombre = elementObject.getAsJsonObject().get("nombre").getAsString();
             apellido = elementObject.getAsJsonObject().get("apellido").getAsString();
             email = elementObject.getAsJsonObject().get("email").getAsString();
             tipo = elementObject.getAsJsonObject().get("id_tipo_usuario").getAsString();
 
            } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        
         if(tipo.equals("1")){
         tipo="administrador";
         }
         if(tipo.equals("2")){
         tipo="publicador";
         }
         if(tipo.equals("3")){
         tipo="suscriptor";
         }
            model.addObject("vaciar","vaciar");
            model.addObject("id",id);
            model.addObject("pseudonimo",pseudonimo);
            model.addObject("nombre",nombre);
            model.addObject("apellido",apellido);
            model.addObject("email",email);
            model.addObject("tipo",tipo);

        model.setViewName("ModificarUsuario");
        return model;
    }    
        
    @RequestMapping(value = {"/ModificarUsuario2"}, method = {RequestMethod.POST})
        public ModelAndView postmodificarusuario2Admin(@RequestParam(value = "nombre", required = false) String nombre,
                                                @RequestParam(value = "pseudonimo", required = false) String pseudonimo,
                                                @RequestParam(value = "apellido", required = false) String apellido,
                                                @RequestParam(value = "email", required = false) String email,
                                                @RequestParam(value = "pass", required = false) String pass,
                                                @RequestParam(value = "tipo", required = false) String tipo,
                                                @RequestParam(value = "id", required = false) String id){
        ModelAndView model= new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        int result=-999;
        int aux=0;
        
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
            
                result = WsFuncion.getConsulta("public.update_usuarios("+id+", '"+pseudonimo+"', '"+nombre+"', '"+apellido+"', '"+email+"', '"+pass+"', "+aux+",  "+name+");");
        model=getmodificarusuarioAdmin();    
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        
        if(result==1){
            model.addObject("exito","Usuario Modificado");
        }else{
            model.addObject("exito","Fallo al modificar el usuario");
            model.addObject("error",result);
        }
        
        
        model.setViewName("ModificarUsuario");
        return model;
    }    
    
    
        @RequestMapping(value = {"/EliminarUsuario"}, method = {RequestMethod.GET})
        public ModelAndView getEliminarUsuarioAdmin(){
        ModelAndView model= new ModelAndView();
        String s = "NULL";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        try {
            s = wsQuery.getConsulta("SELECT usuario FROM public.usuarios WHERE activo=true;");
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        s = s.substring(1, s.length()-1);
        StringTokenizer st = new StringTokenizer(s,",");
        aux="";
        while (st.hasMoreTokens()) {
            s = st.nextToken();
            elementObject = parser.parse(s);
            this.aux = elementObject.getAsJsonObject()
                    .get("usuario").getAsString();
            listString.add(this.aux);
            listString2.add("<option value=\""+this.aux+ "\" type=\"submit\">"+
                                    this.aux+"</option>"); 
        }
        model.addObject("usuario", listString);
        model.addObject("usuarios", listString2);
        model.setViewName("EliminarUsuario");
        return model;
    }
        
        @RequestMapping(value = {"/EliminarUsuario"}, method = {RequestMethod.POST})
	public ModelAndView postEliminarUsuarioAdmin(@RequestParam (value = "listString", required = false)
                                                    String usuario){
        ModelAndView model = new ModelAndView();
       
        int s2 = -999;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String s="";
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        String s3="";
        if(!usuario.equals("NONE")) {
            String valor="";
            try {
                 s = wsQuery.getConsulta("SELECT id_usuario FROM usuarios\n" +
                        "  WHERE usuario='"+usuario+"';");
                 s = s.substring(1, s.length()-1);
                 elementObject = parser.parse(s);
                 s = elementObject.getAsJsonObject()
                    .get("id_usuario").getAsString();
                 s3 = wsQuery.getConsulta("SELECT id_usuario FROM usuarios "
                         + "WHERE usuario='"+name +"';");
                 s3 = s3.substring(1, s3.length()-1);
                 elementObject = parser.parse(s3);
                 s3 = elementObject.getAsJsonObject()
                    .get("id_usuario").getAsString();
                s2 = WsFuncion.getConsulta("public.delete_usuarios("+s+","+s3+",false);");
            } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
            }
        model=getEliminarUsuarioAdmin();
        
        if(s2==1){
            model.addObject("exito","Usuario Eliminado");
        }else{
            model.addObject("exito","Error al eliminar");
            
        }
        model.setViewName("EliminarUsuario");
        return model;
	}
  
    @RequestMapping(value = {"/DetalleUsuario"}, method = {RequestMethod.GET})
        public ModelAndView getDetalleUsuarioAdmin(){
        ModelAndView model= new ModelAndView();
        String s = "NULL";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        try {
            s = wsQuery.getConsulta("SELECT usuario FROM public.usuarios WHERE activo=true;");
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        s = s.substring(1, s.length()-1);
        StringTokenizer st = new StringTokenizer(s,",");
        aux="";
        listString2.clear();
        while (st.hasMoreTokens()) {
            s = st.nextToken();
            elementObject = parser.parse(s);
            this.aux = elementObject.getAsJsonObject()
                    .get("usuario").getAsString();
            listString2.add("<option value=\""+this.aux+ "\" type=\"submit\">"+
                                    this.aux+"</option>"); 
        } 
        model.addObject("usuarios", listString2);
        model.setViewName("DetalleUsuario");
        return model;
    }
        
    
        
        @RequestMapping(value = {"/DetalleUsuario"}, method = {RequestMethod.POST})
        public ModelAndView postDetalleUsuarioAdmin(@RequestParam (value = "listString", required = false)
                                                    String usuario){
        ModelAndView model= new ModelAndView();
        model=getmodificarusuarioAdmin();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String pseudonimo="";
        String nombre="";
        String apellido="";
        String email="";
        String tipo="";
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        
        try {
                usuario = wsQuery.getConsulta("SELECT id_usuario, usuario, nombre, apellido, email,id_tipo_usuario FROM public.usuarios WHERE activo=true and usuario='"+usuario +"';");
                
        usuario = usuario.substring(1, usuario.length()-1);
        elementObject = parser.parse(usuario);

             pseudonimo = elementObject.getAsJsonObject().get("usuario").getAsString();
             nombre = elementObject.getAsJsonObject().get("nombre").getAsString();
             apellido = elementObject.getAsJsonObject().get("apellido").getAsString();
             email = elementObject.getAsJsonObject().get("email").getAsString();
             tipo = elementObject.getAsJsonObject().get("id_tipo_usuario").getAsString();
 
            } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        
         if(tipo.equals("1")){
         tipo="administrador";
         }
         if(tipo.equals("2")){
         tipo="publicador";
         }
         if(tipo.equals("3")){
         tipo="suscriptor";
         }
            model.addObject("vaciar","vaciar");
            model.addObject("pseudonimo",pseudonimo);
            model.addObject("nombre",nombre);
            model.addObject("apellido",apellido);
            model.addObject("email",email);
            model.addObject("tipo",tipo);

        model.setViewName("DetalleUsuario");
        return model;
    }    
        
        
    @RequestMapping(value = {"/gestioncp"}, method = {RequestMethod.GET})
        public ModelAndView getgestioncpAdmin(){
        ModelAndView model= new ModelAndView();
        model.setViewName("gestioncp");
        return model;
    }
    @RequestMapping(value = {"/gestionNodo"}, method = {RequestMethod.GET})
        public ModelAndView getnodos(){
        ModelAndView model= new ModelAndView();
        model.setViewName("gestionNodo");
        return model;
    }
    @RequestMapping(value = {"/agregarNodo"}, method = {RequestMethod.GET})
        public ModelAndView getagregarNodo(){
        ModelAndView model= new ModelAndView();
        model.setViewName("agregarNodo");
        return model;
    }
    @RequestMapping(value = {"/agregarNodo"}, method = {RequestMethod.POST})
        public ModelAndView postagregarNodo(@RequestParam (value = "ip", required = false)
                                                            String ip,
                                            @RequestParam (value = "tipo", required = false)
                                                            String tipo,
                                            @RequestParam (value = "column", required = false)
                                                            String column,
                                            @RequestParam (value = "keyspace", required = false)
                                                            String keyspace){
        ModelAndView model= new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String result = "NULL";
        String id_config = "NULL";
        String usuario = "NULL";
        String json = "";
        int resultado = -999;
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        try{
            usuario = wsQuery.getConsulta("SELECT id_usuario FROM usuarios WHERE usuario='"+name+"';");
            usuario = usuario.substring(1, usuario.length()-1);
            elementObject = parser.parse(usuario);
            usuario = elementObject.getAsJsonObject().get("id_usuario").getAsString();
            result = wsQuery.getConsulta("SELECT id_config, json_config FROM public.config WHERE elemento ='cluster';");
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(!existeCampo.existeCampo(result,"\""+ip+"\"")){
            if(result.equals("[]")){
                try {
                    json = "{\"nodos\":[{\"host\":\""+ip+"\",\"tipo\":\""+tipo+"\",\"status\":\"disponible\","
                            + "\"columnFamily\":\""+column+"\",\"keyspace\":\""+keyspace+"\"}]";    
                    resultado = WsFuncion.getConsulta("public.insert_config('cluster','"+json+"',"+usuario+");");
                } catch (ExcepcionServicio e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                result = result.substring(1, result.length()-1);
                elementObject = parser.parse(result);
                result = elementObject.getAsJsonObject().get("json_config").getAsString();
                id_config = elementObject.getAsJsonObject().get("id_config").getAsString();
                if(existeCampo.existeCampo(result, "nodos")){
                    try {
                        json = "{\"host\":\""+ip+"\",\"tipo\":\""+tipo+"\",\"status\":\"disponible\","
                                + "\"columnFamily\":\""+column+"\",\"keyspace\":\""+keyspace+"\"}]}";
                        result = result.substring(0, result.length()-2)+","+json;
                        resultado = WsFuncion.getConsulta("public.update_config("+id_config+",'cluster','"+result+"',"+usuario+");");
                    } catch (ExcepcionServicio e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else{
                    if(result.equals(" ")){
                        try {
                            json = "{\"nodos\":[{\"host\":\""+ip+"\",\"tipo\":\""+tipo+"\",\"status\":\"disponible\","
                                    + "\"columnFamily\":\""+column+"\",\"keyspace\":\""+keyspace+"\"}]}";
                            resultado = WsFuncion.getConsulta("public.update_config("+id_config+",'cluster','"+json+"',"+usuario+");");
                        } catch (ExcepcionServicio e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            json = "\"nodos\":[{\"host\":\""+ip+"\",\"tipo\":\""+tipo+"\",\"status\":\"disponible\","
                                    + "\"columnFamily\":\""+column+"\",\"keyspace\":\""+keyspace+"\"}]}";
                            result = result.substring(0, result.length()-1)+","+json;
                            resultado = WsFuncion.getConsulta("public.update_config("+id_config+",'cluster','"+result+"',"+usuario+");");
                        } catch (ExcepcionServicio e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }  
                    } 
                }
            }
            if(resultado>0){
                model.addObject("mensaje","exito");
            }else{
                model.addObject("mensaje","error");
            }
        }else{
            model.addObject("mensaje","existe");
        }
            
         model.addObject("prueba",result);
        model.setViewName("agregarNodo");
        return model;
    }
    
    @RequestMapping(value = {"/eliminarNodo"}, method = {RequestMethod.GET})
        public ModelAndView geteliminarNodo(){
        ModelAndView model= new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String result = "NULL";
        String usuario = "NULL";
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        listString.clear();
        listString2.clear();
            try{
                usuario = wsQuery.getConsulta("SELECT id_usuario FROM usuarios WHERE usuario='"+name+"';");
                usuario = usuario.substring(1, usuario.length()-1);
                elementObject = parser.parse(usuario);
                usuario = elementObject.getAsJsonObject().get("id_usuario").getAsString(); 

                result = wsQuery.getConsulta("SELECT json_config FROM public.config WHERE elemento ='cluster';");
            } catch (ExcepcionServicio e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(!result.equals("[ ]")){
               
                String jsonactual = "NULL";
                String nodos = "NULL";
                String valor = "NULL";
                String valor2 = "";
                result = result.substring(1, result.length()-1);
                elementObject = parser.parse(result);
                jsonactual = elementObject.getAsJsonObject().get("json_config").getAsString();
                elementObject = parser.parse(jsonactual);
                if(existeCampo.existeCampo(jsonactual, "nodos")){
                    nodos = elementObject.getAsJsonObject().get("nodos").toString();
                    nodos = nodos.substring(1, nodos.length()-1);
                    StringTokenizer st = new StringTokenizer(nodos,"}");
                    int planif=1;
                    result = "";
                    while (st.hasMoreTokens()) {
                        nodos = st.nextToken()+"}";
                        if (nodos.substring(0,1).equals(",")){
                            nodos = nodos.substring(1);                           
                        }
                        elementObject = parser.parse(nodos);
                        result= result + "Nodo = "+planif+"\n";
                        if(existeCampo.existeCampo(nodos,"host")){
                            valor = elementObject.getAsJsonObject().get("host").getAsString();
                            valor2 = valor2 + "{\"host\":\""+valor+"\",";
                            result= result + "Host = "+valor+"\n";
                            listString.add("<option value=\""+valor+ "\" type=\"submit\">"+
                                        valor+"</option>");
                        }
                        if(existeCampo.existeCampo(nodos,"tipo")){
                            valor = elementObject.getAsJsonObject().get("tipo").getAsString();
                            valor2 = valor2 + "\"tipo\":\""+valor+"\",";
                            result= result + "Tipo = "+valor+"\n";
                        }
                        if(existeCampo.existeCampo(nodos,"status")){
                            valor = elementObject.getAsJsonObject().get("status").getAsString();
                            valor2 = valor2 + "\"status\":\""+valor+"\",";
                            result= result + "Estatus = "+valor+"\n";
                        }
                        if(existeCampo.existeCampo(nodos,"columnFamily")){
                            valor = elementObject.getAsJsonObject().get("columnFamily").getAsString();
                            valor2 = valor2 + "\"columnFamily\":\""+valor+"\",";
                            result= result + "ColumnFamily = "+valor+"\n";
                        }
                        if(existeCampo.existeCampo(nodos,"keyspace")){
                            valor = elementObject.getAsJsonObject().get("keyspace").getAsString();
                            valor2 = valor2 + "\"keyspace\":\""+valor+"\"}";
                            result= result + "Keyspace = "+valor+"\n";
                        }
                        listaNodo.setlistaNodo(valor2);
                        listString2.add(result+"\n\n");
                        planif++;
                        valor="";
                        result="";
                        valor2="";
                    }
                    model.addObject("nodoActivo",listString2);
                    model.addObject("nodoIP",listString);
                }else{
                    model.addObject("mensaje","vacio");
                } 
              //model.addObject("prueba",jsonactual);  
            }else{
                model.addObject("mensaje","vacio");
            }
            
        model.setViewName("eliminarNodo");
        return model;
    }
    
    @RequestMapping(value = {"/eliminarNodo"}, method = {RequestMethod.POST})
        public ModelAndView posteliminarNodo(@RequestParam (value = "nodoE", required = false)
                                                    String ip){
        ModelAndView model= new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String result = "NULL";
        String id_config = "NULL";
        String usuario = "NULL";
        String json = "";
        int resultado = -999;
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        try{
            usuario = wsQuery.getConsulta("SELECT id_usuario FROM usuarios WHERE usuario='"+name+"';");
            usuario = usuario.substring(1, usuario.length()-1);
            elementObject = parser.parse(usuario);
            usuario = elementObject.getAsJsonObject().get("id_usuario").getAsString();
            result = wsQuery.getConsulta("SELECT id_config, json_config FROM public.config WHERE elemento ='cluster';");
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        result = result.substring(1, result.length()-1);
        elementObject = parser.parse(result);
        result = elementObject.getAsJsonObject().get("json_config").getAsString();
        id_config = elementObject.getAsJsonObject().get("id_config").getAsString();
        String aux = listaNodo.getlistaNodo(ip);
        if(aux.equals("")){
            try{
                elementObject = parser.parse(result);
                if(existeCampo.existeCampo(result, "max_cargas_paralelas")){
                    result = elementObject.getAsJsonObject().get("max_cargas_paralelas").getAsString();
                    json = "{\"max_cargas_paralelas\":"+result+"}";
                    resultado = WsFuncion.getConsulta("public.update_config("+id_config+",'cluster','"+json+"',"+usuario+");");
                }else{
                    resultado = WsFuncion.getConsulta("public.update_config("+id_config+",'cluster',' ',"+usuario+");");          
                }    
            } catch (ExcepcionServicio e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            try{
                elementObject = parser.parse(result);
                if(existeCampo.existeCampo(result, "max_cargas_paralelas")){
                    result = elementObject.getAsJsonObject().get("max_cargas_paralelas").getAsString();
                    json = "{\"max_cargas_paralelas\":"+result+",\"nodos\":["+aux;                   
                    json = json.substring(0, json.length()-1)+"]}";
                    resultado = WsFuncion.getConsulta("public.update_config("+id_config+",'cluster','"+json+"',"+usuario+");");
                }else{
                    json = "{\"nodos\":["+aux;          
                    json = json.substring(0, json.length()-1)+"]}";
                    resultado = WsFuncion.getConsulta("public.update_config("+id_config+",'cluster','"+json+"',"+usuario+");");
                    aux=""+resultado;
                }       
            } catch (ExcepcionServicio e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
        model = geteliminarNodo();
        if(resultado>0){          
            model.addObject("mensaje","exito");
        }else{
            model.addObject("mensaje","error");
        }
        model.addObject("prueba",aux);
        model.setViewName("eliminarNodo");
        return model;
    }
    
        
    @RequestMapping(value = {"/CargasenParalelo"}, method = {RequestMethod.GET})
        public ModelAndView getCargasenParalelo(){
        ModelAndView model= new ModelAndView();
        model.setViewName("CargasenParalelo");
        return model;
    }
    @RequestMapping(value = {"/CargasenParalelo"}, method = {RequestMethod.POST})
        public ModelAndView postCargasenParalelo(@RequestParam (value = "cargas", required = false)
                                                    int cargas){
            
            
        ModelAndView model= new ModelAndView();
        model=getCargasenParalelo();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        JsonElement elementObject2;
        String usuario = "NULL";
        String result="";
        int resultado = -999;
        String Json="";
        String jsonactual="";
        String nodos="";
        String id="";
        
        if(cargas>0){           
            try{
                usuario = wsQuery.getConsulta("SELECT id_usuario FROM usuarios WHERE usuario='"+name+"';");
                usuario = usuario.substring(1, usuario.length()-1);
                elementObject = parser.parse(usuario);
                usuario = elementObject.getAsJsonObject().get("id_usuario").getAsString(); 

                result = wsQuery.getConsulta("SELECT id_config, elemento, json_config FROM public.config WHERE elemento ='cluster';");
            } catch (ExcepcionServicio e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(result.equals("[]")){
                try {
                    Json = "{\"max_cargas_paralelas\":"+cargas+"}";    
                    resultado = WsFuncion.getConsulta("public.insert_config('cluster','"+Json+"',"+usuario+");");
                } catch (ExcepcionServicio e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(resultado>0){
                    model.addObject("mensaje","exito");
                }else{
                    model.addObject("mensaje","error");
                }
                }else{
                    try {

                    result = result.substring(1, result.length()-1);
                    elementObject = parser.parse(result);
                    jsonactual = elementObject.getAsJsonObject().get("json_config").getAsString();
                    id=elementObject.getAsJsonObject().get("id_config").getAsString();
                    elementObject2 = parser.parse(jsonactual);
                    Json = "{\"max_cargas_paralelas\":"+cargas+""; 
                    nodos =",\"nodos\":"+ elementObject2.getAsJsonObject().get("nodos").toString()+"}";
                    jsonactual="";
                    jsonactual=Json+nodos;

                    resultado = WsFuncion.getConsulta("public.update_config("+id+",'cluster','"+jsonactual+"',"+usuario+");");
                    } catch (ExcepcionServicio e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(resultado>0){
                        model.addObject("mensaje","exito");
                    }else{
                        model.addObject("mensaje","error");
                    }
                }
            }else{
                model.addObject("mensaje","El valor debe ser un numero positivo");
        }
        model.setViewName("CargasenParalelo");
        return model;
    }
        
        
    @RequestMapping(value = {"/tiendaadmin"}, method = {RequestMethod.GET})
        public ModelAndView gettiendaAdmin(){
        ModelAndView model= new ModelAndView();
        model.setViewName("tiendaadmin");
        return model;
    }
    @RequestMapping(value = {"/tiendaadmin"}, method = {RequestMethod.POST})
        public ModelAndView postiendaAdmin(@RequestParam (value = "user", required = false)
                                                    String user,
                                            @RequestParam (value = "nombre", required = false)
                                                            String nombre,
                                            @RequestParam (value = "fecha", required = false)
                                                            String fecha,
                                            @RequestParam (value = "format", required = false)
                                                            String format,
                                            @RequestParam (value = "pass", required = false)
                                                            String pass,
                                            @RequestParam (value = "host", required = false)
                                                            String host,
                                            @RequestParam (value = "bd", required = false)
                                                            String bd){
        ModelAndView model= new ModelAndView();
        String Json="";
        Json = "{\"usuario\":\""+user+"\",\"nombre\":\""+nombre+"\",\"fecha_base\":\""+fecha+"\",\"formato_fecha\":"+format+",\"contraseña\":\""+pass+"\",\"host_bd_oracle\":\""+host+"\",\"bd_oracle\":\""+bd+"\"}";
        String result="";
        String usuario = "NULL";
        int resultado = -999;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        
        try{
            usuario = wsQuery.getConsulta("SELECT id_usuario FROM usuarios WHERE usuario='"+name+"';");
            usuario = usuario.substring(1, usuario.length()-1);
            elementObject = parser.parse(usuario);
            usuario = elementObject.getAsJsonObject().get("id_usuario").getAsString(); 

            result = wsQuery.getConsulta("SELECT id_config FROM public.config WHERE elemento ='tienda';");
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if(result.equals("[]")){
            try {
                resultado = WsFuncion.getConsulta("public.insert_config('tienda','"+Json+"',"+usuario+");");
            } catch (ExcepcionServicio e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(resultado>0){
                model.addObject("mensaje","exito");
            }else{
                model.addObject("mensaje","error");
            }
        }else{
            try {
                result = result.substring(1, result.length()-1);
                elementObject = parser.parse(result);
                result = elementObject.getAsJsonObject().get("id_config").getAsString(); 
                
                resultado = WsFuncion.getConsulta("public.update_config("+result+",'tienda','"+Json+"',"+usuario+");");
            } catch (ExcepcionServicio e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(resultado>0){
                model.addObject("mensaje","exito");
            }else{
                model.addObject("mensaje","error");
            }
        }
        model.setViewName("tiendaadmin");
        return model;
    }
    @RequestMapping(value = {"/pdi"}, method = {RequestMethod.GET})
        public ModelAndView getpdi(){
        ModelAndView model= new ModelAndView();
        model.setViewName("pdi");
        return model;
    }
    @RequestMapping(value = {"/pdi"}, method = {RequestMethod.POST})
        public ModelAndView postpdi(@RequestParam (value = "directorioPDI", required = false)
                                                    String directorioPDI,
                                    @RequestParam (value = "nombrePDI", required = false)
                                                    String nombrePDI,
                                    @RequestParam (value = "user", required = false)
                                                    String user,
                                    @RequestParam (value = "pass", required = false)
                                                    String pass,
                                    @RequestParam (value = "log", required = false)
                                                    String log,
                                    @RequestParam (value = "nivel", required = false)
                                                    String nivel,
                                    @RequestParam (value = "nombreJOBCI", required = false)
                                                    String nombreJOBCI,
                                    @RequestParam (value = "directorioJOBCI", required = false)
                                                    String directorioJOBCI,
                                    @RequestParam (value = "nombreJOBM", required = false)
                                                    String nombreJOBM,
                                    @RequestParam (value = "directorioJOBM", required = false)
                                                    String directorioJOBM,
                                    @RequestParam (value = "nombreJOBCIE", required = false)
                                                    String nombreJOBCIE,
                                    @RequestParam (value = "directorioJOBCIE", required = false)
                                                    String directorioJOBCIE,
                                    @RequestParam (value = "nombreJOBME", required = false)
                                                    String nombreJOBME,
                                    @RequestParam (value = "directorioJOBME", required = false)
                                                    String directorioJOBME){
        ModelAndView model= new ModelAndView();
        String Json="";
        Json = "{\"directorio_pdi\":\""+directorioPDI+"\",\"repositorio\":\""+nombrePDI+"\","
                + "\"usuario_repositorio\":\""+user+"\",\"password\":"+pass+","
                + "\"directorio_logs\":\""+log+"\",\"nivel_logs\":\""+nivel+"\","
                + "\"nombre_job_ci\":\""+nombreJOBCI+"\",\"directorio_job_ci\":\""+directorioJOBCI+"\","
                + "\"nombre_job_m\":\""+nombreJOBM+"\",\"directorio_job_m\":\""+directorioJOBM+"\","
                + "\"nombre_job_ci_etl\":\""+nombreJOBCIE+"\",\"directorio_job_ci_etl\":\""+directorioJOBCIE+"\","
                + "\"nombre_job_m_etl\":\""+nombreJOBME+"\",\"directorio_job_m_etl\":\""+directorioJOBME+"\"}";
        String result="";
        String usuario = "NULL";
        int resultado = -999;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        JsonParser parser = new JsonParser();
        JsonElement elementObject;
        
        try{
            usuario = wsQuery.getConsulta("SELECT id_usuario FROM usuarios WHERE usuario='"+name+"';");
            usuario = usuario.substring(1, usuario.length()-1);
            elementObject = parser.parse(usuario);
            usuario = elementObject.getAsJsonObject().get("id_usuario").getAsString(); 

            result = wsQuery.getConsulta("SELECT id_config FROM public.config WHERE elemento ='pdi';");
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if(result.equals("[]")){
            try {
                resultado = WsFuncion.getConsulta("public.insert_config('pdi','"+Json+"',"+usuario+");");
            } catch (ExcepcionServicio e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(resultado>0){
                model.addObject("mensaje","exito");
            }else{
                model.addObject("mensaje","error");
            }
        }else{
            try {
                result = result.substring(1, result.length()-1);
                elementObject = parser.parse(result);
                result = elementObject.getAsJsonObject().get("id_config").getAsString(); 
                
                resultado = WsFuncion.getConsulta("public.update_config("+result+",'pdi','"+Json+"',"+usuario+");");
            } catch (ExcepcionServicio e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(resultado>0){
                model.addObject("mensaje","exito");
            }else{
                model.addObject("mensaje","error");
            }
        }
        model.setViewName("pdi");
        return model;
    }
}
