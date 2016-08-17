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
import ve.gob.mercal.app.services.WsFuncionApp;
import ve.gob.mercal.app.services.WsQuery;

/**
 *
 * @author wso2
 */
@Controller
@Scope("request")
public class publicadorControlador {
    @Autowired
    public WsFuncionApp WsFuncion;
    
    @Autowired
    public WsQuery wsQuery;
    
    
    private String tienda = "";
    private String aux = "";
    private String publicador = "";
    public List<String> listString = new ArrayList<>();
    public List<String> listString2 = new ArrayList<>();
    public List<String> listString3 = new ArrayList<>();
    public List<String> listString4 = new ArrayList<>();
    public List<String> listString5 = new ArrayList<>();
    public List<String> listString6 = new ArrayList<>();
   // public List<String> listString7 = new ArrayList<>();
    public Model prueba;
    
    @RequestMapping(value = {"/AgregarP"}, method = {RequestMethod.GET})
    public ModelAndView getTienda(){
        ModelAndView model= new ModelAndView();
        String s = "NULL";
        String s2 = "NULL";
        String s3 = "NULL";
        String s4 = "NULL";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        try {
            s = wsQuery.getConsulta("SELECT t.tienda" +
                        " FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u" +
                        " WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and pt.id_usuario=u.id_usuario and u.usuario='"+name+"';");
            
            
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
        
         try {
                  s2 = wsQuery.getConsulta("select usuario " +
                                            "from usuarios where id_tipo_usuario = 2 AND usuario != '"+name+"';");//Listar los publicadores, menos el actual
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
         //solo las tiendas en las que el usuario actual es manager 
          try {
            s3 = wsQuery.getConsulta("SELECT t.tienda" +
                        " FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u" +
                        " WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and pt.id_usuario=u.id_usuario and u.usuario='"+name+"' and u.id_usuario=t.id_manager ;");
            
            
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          
          try {
            s4 = wsQuery.getConsulta("SELECT  u.usuario" +
                    " FROM usuarios u, pub_tiendas pt, usuarios actual" +
                    " WHERE u.id_tipo_usuario = 2 AND u.usuario != '"+name+"' AND actual.usuario = '"+name+"' AND pt.id_usuario = u.id_usuario AND pt.id_creado_por = actual.id_usuario AND pt.activo=TRUE ;");
            
            
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
        
        //tiendas
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
        this.tienda="";
        JsonParser parser3 = new JsonParser();
        JsonElement elementObject3;
        s3 = s3.substring(1, s3.length()-1);
        StringTokenizer st3 = new StringTokenizer(s3,",");
        
        while (st3.hasMoreTokens()) {
            s3 = st3.nextToken();
            elementObject3 = parser3.parse(s3);
            this.tienda = elementObject3.getAsJsonObject()
                    .get("tienda").getAsString();
            listString3.add("<option value="+this.tienda+ ">"+
                                    this.tienda+"</option>");
            }
        
          //publicadores
        s2 = s2.substring(1, s2.length()-1); //quitarle los corchetes "[]"
        
        JsonParser parser2 = new JsonParser();
        JsonElement elementObject2;
        StringTokenizer st2 = new StringTokenizer(s2,"}");
       
        String valor="";
        while (st2.hasMoreTokens()) {
                s2 = st2.nextToken()+"}";
                if (s2.substring(0,1).equals(",")){
                    s2 = s2.substring(1);                          
                }
                elementObject2 = parser2.parse(s2);
                valor = elementObject2.getAsJsonObject().get("usuario").getAsString();
                this.tienda=valor;
                 listString4.add("<option value="+this.tienda+ ">"+
                                    this.tienda+"</option>");
                //listString2.add("\n");
                
                valor="";
               this.tienda="";
            }
        //publicadores agregados por actual
        s4 = s4.substring(1, s4.length()-1); //quitarle los corchetes "[]"
        
        JsonParser parser4 = new JsonParser();
        JsonElement elementObject4;
        StringTokenizer st4 = new StringTokenizer(s4,"}");
       
         valor="";
         this.tienda="";
        while (st4.hasMoreTokens()) {
                s4 = st4.nextToken()+"}";
                if (s4.substring(0,1).equals(",")){
                    s4 = s4.substring(1);                          
                }
                elementObject4 = parser4.parse(s4);
                valor = elementObject4.getAsJsonObject().get("usuario").getAsString();
                this.tienda=valor;
                 listString5.add("<option value="+this.tienda+ ">"+
                                    this.tienda+"</option>");
                //listString2.add("\n");
                
                valor="";
               this.tienda="";
            }
        
        
        model.addObject("tienda", listString); //todas las tiendas del usuario
        model.addObject("publicador",listString4 ); //lista de publicadores
        model.addObject("tienda2",listString3); //solo las que soy manager
        model.addObject("publicador2",listString5 ); //lista de publicadores agregados por actual
        model.setViewName("AgregarP");
        return model;
    }
    
    //controlador para mostrar los publicadores asociados a la tienda seleccionada
    
         @RequestMapping(value = {"/AgregarP"}, method = RequestMethod.POST)
    public ModelAndView listarPublicadores(@RequestParam (value = "listString", required = false)
                                                    String nameTienda){
        ModelAndView model = new ModelAndView();
        model=getTienda();
        String s2 = "NULL";
        nameTienda = nameTienda.substring(0,nameTienda.length()-13);
        if(!nameTienda.equals("NONE")) {
            String valor="";
            try {
                s2 = wsQuery.getConsulta("SELECT u.usuario\n" +
                "  FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u\n" +
                "  WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and t.tienda='"+nameTienda+"' and pt.id_usuario=u.id_usuario;");
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
                result= result + "Publicador: "+planif+"\n";
                valor = elementObject.getAsJsonObject().get("usuario").getAsString();
                result= result + "Usuario = "+valor+"\n";
                listString2.add(result);
                //listString2.add("\n");
                planif++;
                valor="";
                result="";
            }
            model.addObject("publicacion",listString2);
            model.setViewName("AgregarP");
        }
 
        return model;
    }
    
    //controlador para agregar publicadores a una tienda en especifico
     @RequestMapping(value = {"/AgregarPub"}, method = RequestMethod.POST)
     public ModelAndView agregarPublicadores(
        @RequestParam (value = "nameTienda", required = false) String nameTienda, 
        @RequestParam (value = "namePub", required = false) String namePub
                                                )
    {
        ModelAndView model = new ModelAndView();
        model = getTienda();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        int funcion=0;
        String validar = "";
        String param = "NULL";
        int id_u = 0; //id del usuario a insertar
        int id_t = 0; //id de la tienda para asignar al usuario
        int id_c_p = 0; // id del usuario actual.
       // int test = 0;
       if(!nameTienda.equals("NONE")  && !namePub.equals("NONE") ) {
          try {
                validar = wsQuery.getConsulta("SELECT pub.id_usuario" +
                        " FROM usuarios u, tiendas t, pub_tiendas pub" +
                        " WHERE t.tienda = '"+nameTienda+"' and u.usuario='"+namePub+"' and u.id_usuario = pub.id_usuario and t.id_tienda = pub.id_tienda;");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
            }
         
            if(!validar.equals("[]")){
              //muestro msj de que el usuario ya existe en pub_tiendas
              model.addObject("mensaje", "Este usuario ya esta asignado a la tienda especificada");
                
            }else{
                //agrego el publicador con la respectiva tienda
             
                //realizo una consulta SQL para obtener los parametros.
                try {
                    param = wsQuery.getConsulta("SELECT u.id_usuario, t.id_tienda, up.id_usuario as id_actual" +
                            " FROM usuarios u, tiendas t, usuarios up" +
                            " WHERE u.usuario ='"+namePub+"' and t.tienda = '"+nameTienda+"' and u.activo=TRUE and t.activo=TRUE and up.usuario='"+name+"';");
                } catch (ExcepcionServicio ex) {
                    Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            JsonParser parser = new JsonParser();
            JsonElement elementObject;
            param = param.substring(1, param.length()-1);
            elementObject = parser.parse(param);
            id_u = elementObject.getAsJsonObject().get("id_usuario").getAsInt();
            id_t = elementObject.getAsJsonObject().get("id_tienda").getAsInt();
            id_c_p = elementObject.getAsJsonObject().get("id_actual").getAsInt();
           try {
             funcion=WsFuncion.getConsulta("public.insert_pub_tiendas("+id_u+" ,"+ id_t+" ,"+ id_c_p+" );");
               
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
            }
           if(funcion>0){
           model.addObject("mensaje","Publicador agregado Exitosamente");
           }else{
           
           model.addObject("mensaje", " hubo un error: ");
           model.addObject("error", funcion);
           }
        }
           // test = id_u + id_t;
       // model.addObject("parametro1",id_u);
       // model.addObject("parametro2", test);
        model.setViewName("AgregarP");
        
    }
    return model;
    }
     
     
     //controlador para eliminar publicadores a una tienda en especifico
     @RequestMapping(value = {"/EliminarPub"}, method = RequestMethod.POST)
     public ModelAndView eliminarPublicadores(
        @RequestParam (value = "nameTienda2", required = false) String nameTienda, 
        @RequestParam (value = "namePub2", required = false) String namePub
                                                )
    {
        ModelAndView model = new ModelAndView();
        model = getTienda();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        int funcion=0;
        String validar = "";
        String param = "NULL";
        int id_u = 0; //id del usuario a insertar
        int id_t = 0; //id de la tienda para asignar al usuario
        int id_m_p = 0; // id del usuario actual.
       if(!nameTienda.equals("NONE")  && !namePub.equals("NONE") ) {
          try {
                validar = wsQuery.getConsulta("SELECT pub.id_usuario" +
                        " FROM usuarios u, tiendas t, pub_tiendas pub" +
                        " WHERE t.tienda = '"+nameTienda+"' and u.usuario='"+namePub+"' and u.id_usuario = pub.id_usuario and t.id_tienda = pub.id_tienda;");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
            }
         
            if(!validar.equals("[]")){

                //elimino el publicador con la respectiva tienda

                //realizo una consulta SQL para obtener los parametros de la funcion de eliminar.
                try {
                    param = wsQuery.getConsulta("SELECT u.id_usuario, t.id_tienda, up.id_usuario as id_actual" +
                          " FROM usuarios u, tiendas t, usuarios up" +
                          " WHERE u.usuario ='"+namePub+"' and t.tienda = '"+nameTienda+"' and u.activo=TRUE and t.activo=TRUE and up.usuario='"+name+"';");
                } catch (ExcepcionServicio ex) {
                    Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
                }

                  JsonParser parser = new JsonParser();
                  JsonElement elementObject;
                  param = param.substring(1, param.length()-1);
                  elementObject = parser.parse(param);
                  id_u = elementObject.getAsJsonObject().get("id_usuario").getAsInt();
                  id_t = elementObject.getAsJsonObject().get("id_tienda").getAsInt();
                  id_m_p = elementObject.getAsJsonObject().get("id_actual").getAsInt();
                 try {
                        funcion=WsFuncion.getConsulta("public.delete_pub_tiendas("+id_u+" ,"+ id_t+" ,"+ id_m_p+" );");
                  } catch (ExcepcionServicio ex) {
                        Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
                  }
                 if(funcion>0){
                    model.addObject("mensaje2","Publicador eliminado Exitosamente");
                 }else{

                 model.addObject("mensaje2", "El publicador ya no existe o hubo un error: "+funcion+"");
                 
                 }
                
            }else{
                  //muestro msj de que el usuario no existe en pub_tiendas
                 model.addObject("mensaje2", "Este usuario no esta asignado a la tienda especificada");
            }
        //model.addObject("parametro1",id_u);
        //model.addObject("parametro2", id_t);
        model.setViewName("AgregarP");
        
    }
    return model;
    }
     
     // Query para obtener la lista de publicadores asignados a tiendas que fueron agregados por el usuario actual.
//SELECT  u.usuario
//FROM usuarios u, pub_tiendas pt, usuarios actual
//WHERE u.id_tipo_usuario = 2 AND u.usuario != '"+name+"' AND actual.usuario = '"+name+"' AND pt.id_usuario = u.id_usuario AND pt.id_creado_por = actual.id_usuario ;
//     



    
    @RequestMapping(value = {"/Publicar"}, method = {RequestMethod.GET})
    public ModelAndView publicar(){
        ModelAndView model= new ModelAndView();
        model = getTienda();
        model.setViewName("Publicar");
        return model;
    }
    
    
    @RequestMapping(value = {"/Publicar"}, method = {RequestMethod.POST})
    public ModelAndView publicar_post(@RequestParam (value = "nameTienda2", required = true) String nameTienda)
    {
        
    ModelAndView model= new ModelAndView();
    String plan="";
    String valor="";
    String ejec="";
    String result="";
    
    model = publicar();
        if(!nameTienda.equals("NONE")) {
            nameTienda = nameTienda.substring(0,nameTienda.length()-13);
            try {
                plan=wsQuery.getConsulta("SELECT pe.nro_control_plan, t.tienda, j.job,pe.timestamp_planificacion, pe.nro_control_ejec, pe.observaciones\n" +
                "  FROM public.plan_ejecuciones as pe, public.pasos_plan_ejecucion as ppe, public.tiendas as t, public.jobs as j\n" +
                "  WHERE pe.activo=TRUE and pe.tipo_ejecucion='planificada' and pe.id_plan_ejecucion=ppe.id_plan_ejecucion and ppe.status_plan='en espera' and pe.id_tienda=t.id_tienda and pe.id_job=j.id_job\n" +
                "AND t.tienda='"+nameTienda+"';");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        //Planificadas
            if(!plan.equals("[]")){
            
                JsonParser parser = new JsonParser();
                JsonElement elementObject;
                plan = plan.substring(1, plan.length()-1);
                StringTokenizer st = new StringTokenizer(plan,"}");
                int planif=1;
                result="";
                while (st.hasMoreTokens()) {
                    plan = st.nextToken()+"}";
                    if (plan.substring(0,1).equals(",")){
                        plan = plan.substring(1);                          
                    }
                    elementObject = parser.parse(plan);
                    result= result + "Planificacion : "+planif+"\n";
                    valor = elementObject.getAsJsonObject().get("nro_control_plan").getAsString();
                    result= result + "Nro Control Plan= "+valor+"\n";
                    valor = elementObject.getAsJsonObject().get("tienda").getAsString();
                    result= result + "Tienda = "+valor+"\n";
                    valor = elementObject.getAsJsonObject().get("job").getAsString();
                    result= result + "Job = "+valor+"\n";
                    valor = elementObject.getAsJsonObject().get("nro_control_ejec").getAsString();
                    result= result + "Nro Control Ejecucion= "+valor+"\n";
                    valor = elementObject.getAsJsonObject().get("observaciones").getAsString();
                    result= result + "Observaciones= "+valor+"\n";
                    if(elementObject.getAsJsonObject().get("timestamp_planificacion").getAsString()!=null){
                        valor = elementObject.getAsJsonObject().get("timestamp_planificacion").getAsString();
                        result= result + "Tiempo de la planificacion = "+valor+"\n";
                    }
                  
                    listString6.add(result);
                    //listString2.add("\n");
                    planif++;
                    valor="";
                    result="";

                }
                
            model.addObject("planificado",listString6);
                
            }else{
            model.addObject("mensaje_plan","No hay tareas planificadas para la tienda: "+nameTienda+"");
            
            }
            //Ejecutadas
            try {
                ejec=wsQuery.getConsulta("SELECT pe.nro_control_plan, t.tienda, j.job, pe.timestamp_planificacion, pe.nro_control_ejec, pe.revisado, pe.observaciones\n" +
                "  FROM public.plan_ejecuciones  as pe, public.pasos_plan_ejecucion as ppe, public.tiendas as t, public.jobs as j\n" +
                "  WHERE pe.activo=TRUE and pe.id_tienda=t.id_tienda and pe.id_job=j.id_job and pe.id_plan_ejecucion=ppe.id_plan_ejecucion and ppe.status_plan='a ejecucion' \n" +
                "AND t.tienda='"+nameTienda+"';");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(!ejec.equals("[]")){
                
                JsonParser parser2 = new JsonParser();
                JsonElement elementObject2;
                ejec = ejec.substring(1, ejec.length()-1);
                StringTokenizer st2 = new StringTokenizer(ejec,"}");
                int planif=1;
                result="";
                listString6=null;
                while (st2.hasMoreTokens()) {
                    ejec = st2.nextToken()+"}";
                    if (ejec.substring(0,1).equals(",")){
                        ejec = ejec.substring(1);                          
                    }
                    elementObject2 = parser2.parse(ejec);
                    result= result + "Planificacion : "+planif+"\n";
                    valor = elementObject2.getAsJsonObject().get("nro_control_plan").getAsString();
                    result= result + "Nro Control Plan= "+valor+"\n";
                    valor = elementObject2.getAsJsonObject().get("tienda").getAsString();
                    result= result + "Tienda = "+valor+"\n";
                    valor = elementObject2.getAsJsonObject().get("job").getAsString();
                    result= result + "Job = "+valor+"\n";
                    valor = elementObject2.getAsJsonObject().get("nro_control_ejec").getAsString();
                    result= result + "Nro Control Ejecucion= "+valor+"\n";
                    valor = elementObject2.getAsJsonObject().get("observaciones").getAsString();
                    result= result + "Observaciones= "+valor+"\n";
                     if(elementObject2.getAsJsonObject().get("timestamp_planificacion").getAsString()!=null){
                        valor = elementObject2.getAsJsonObject().get("timestamp_planificacion").getAsString();
                        result= result + "Tiempo de la planificacion = "+valor+"\n";
                    }
                    listString6.add(result);
                    //listString2.add("\n");
                    planif++;
                    valor="";
                    result="";

                }
            model.addObject("ejecutado",listString6);
            }else{
            model.addObject("mensaje_ejec","No hay tareas en ejecucion para la tienda: "+nameTienda+"");
            
            }
        
        }
        
    return model;
    }
    
        @RequestMapping(value = {"/agregarPlanificacion"}, method = RequestMethod.GET)
    public ModelAndView getaddPublicacion(){
        ModelAndView model = new ModelAndView();
        model.setViewName("agregarPlanificacion");
        return model;
    }
    @RequestMapping(value = {"/agregarPlanificacion"}, method = RequestMethod.POST)
    public ModelAndView postaddPublicacion(@RequestParam(value = "nameTienda2", 
                                                    required = false) String nombre){
        ModelAndView model = new ModelAndView();
        model=publicar_post(nombre);
        
        model.setViewName("agregarPlanificacion");
        return model;
    }
    
      @RequestMapping(value = {"/GestionPublicar"}, method = RequestMethod.GET)
    public ModelAndView getgestiontiendaPublicacion(){
        ModelAndView model = new ModelAndView();
        model.addObject("tienda","prueba");
        model.setViewName("GestionPublicar");
        return model;
    }
    
    
}
