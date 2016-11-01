/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mercal.app.controllers;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.phdconsultores.ws.exception.ExcepcionServicio;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import ve.gob.mercal.app.models.existeCampo;
import ve.gob.mercal.app.services.WsFuncionApp;
import ve.gob.mercal.app.services.WsQuery;

/**
 *
 * @author phd2014
 */
@Controller
@Scope("request")
public class publicadorControlador {
    @Autowired
    public WsFuncionApp WsFuncion;
    
    @Autowired
    public WsQuery wsQuery;
    
    @Autowired
    public nombreTienda nombreTiendaUser;
    
    @Autowired
    public existeCampo existeCampo;
    
    private String tienda = "";
    private String aux = "";
    private String publicador = "";
    public List<String> listString = new ArrayList<>();
    public List<String> listString2 = new ArrayList<>();
    public List<String> listString3 = new ArrayList<>();
    public List<String> listString4 = new ArrayList<>();
    public List<String> listString5 = new ArrayList<>();
    public List<String> listString6 = new ArrayList<>();
    public Model prueba;
    public DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public Date fechaactual = new Date();
    
    
    
    @RequestMapping(value = {"/GestionAgregarP"}, method = {RequestMethod.GET})
    public ModelAndView getTienda(){
        ModelAndView model= new ModelAndView();
    
        model.setViewName("GestionAgregarP");
        return model;
    }
   
    
    //controlador para agregar publicadores a una tienda en especifico
    @RequestMapping(value = {"/CrearP"}, method = RequestMethod.GET)
     public ModelAndView getagregarPublicadores()
    {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String s = "NULL";
        String s2 = "NULL";
        try {
            s = wsQuery.getConsulta("SELECT t.tienda" +
                        " FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u" +
                        " WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and pt.id_usuario=u.id_usuario and u.usuario='"+name+"' and u.id_usuario=t.id_manager ;");
            
            
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //tiendas
        JsonParser parser3 = new JsonParser();
        JsonElement elementObject3;
        s = s.substring(1, s.length()-1);
        StringTokenizer st3 = new StringTokenizer(s,",");
        
        while (st3.hasMoreTokens()) {
            s = st3.nextToken();
            elementObject3 = parser3.parse(s);
            if(existeCampo.existeCampo(s,"tienda")){
                this.tienda = elementObject3.getAsJsonObject()
                        .get("tienda").getAsString();
            }       
            listString3.add("<option value=\""+this.tienda+ "\">"+
                                    this.tienda+"</option>");
            }
        
        try {
            s2 = wsQuery.getConsulta("select usuario " +
                                            "from usuarios where id_tipo_usuario = 2 AND usuario != '"+name+"';");//Listar los publicadores, menos el actual
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
             listString4.add("<option value=\""+this.tienda+ "\">"+
                                this.tienda+"</option>");
            //listString2.add("\n");

            valor="";
            this.tienda="";
        }
        
        model.addObject("tienda", listString3); //todas las tiendas del usuario
        model.addObject("publicador",listString4 ); //lista de publicadores
        model.setViewName("CrearP");
        return model;
        
    }
     
     
     @RequestMapping(value = {"/CrearP"}, method = RequestMethod.POST)
     public ModelAndView agregarPublicadores(
        @RequestParam (value = "nombreTienda", required = true) String nameTienda, 
        @RequestParam (value = "namePub", required = true) String namePub
                                                )
    {
        ModelAndView model = new ModelAndView();
        model = getagregarPublicadores();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        int funcion=0;
        String validar = "";
        String param = "NULL"; 
        int id_u = 0; //id del usuario a insertar
        int id_t = 0; //id de la tienda para asignar al usuario
        int id_c_p = 0; // id del usuario actual.

       if(!nameTienda.equals("NONE")  && !namePub.equals("NONE") ) {
          try {
                validar = wsQuery.getConsulta("SELECT pub.id_usuario" +
                        " FROM usuarios u, tiendas t, pub_tiendas pub" +
                        " WHERE t.tienda = '"+nameTienda+"' and u.usuario='"+namePub+"' and u.id_usuario = pub.id_usuario and t.id_tienda = pub.id_tienda;");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
            }
         
            if(!validar.equals("[]")){ //chequear que el registro existe
                  try {
                         validar = wsQuery.getConsulta("SELECT pub.id_usuario" +
                        " FROM usuarios u, tiendas t, pub_tiendas pub" +
                        " WHERE t.tienda = '"+nameTienda+"' and u.usuario='"+namePub+"' and u.id_usuario = pub.id_usuario and t.id_tienda = pub.id_tienda and pub.activo=FALSE;");
                    } catch (ExcepcionServicio ex) {
                        Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                if (!validar.equals("[]")){ // si el reistro existe, entonces verifico que este inactivo
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
                    if(existeCampo.existeCampo(param,"id_usuario")){
                        id_u = elementObject.getAsJsonObject().get("id_usuario").getAsInt();
                    }
                    
                    if(existeCampo.existeCampo(param,"id_tienda")){
                        id_t = elementObject.getAsJsonObject().get("id_tienda").getAsInt();
                    }
                    if(existeCampo.existeCampo(param,"id_actual")){    
                        id_c_p = elementObject.getAsJsonObject().get("id_actual").getAsInt();
                    }
                    try {
                        funcion=WsFuncion.getConsulta("public.update_pub_tiendas("+id_u+" ,"+ id_t+" ,"+ id_c_p+" );");
               
                            } catch (ExcepcionServicio ex) {
                       Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
                       }
                    if(funcion>0){
                     model.addObject("mensaje","exito");
                    }else{
                     model.addObject("mensaje", "error");
                    }
                    
                }else{
                 model.addObject("mensaje","existe");
                
                }
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
            if(existeCampo.existeCampo(param,"id_usuario")){
                id_u = elementObject.getAsJsonObject().get("id_usuario").getAsInt();
            }

            if(existeCampo.existeCampo(param,"id_tienda")){
                id_t = elementObject.getAsJsonObject().get("id_tienda").getAsInt();
            }
            if(existeCampo.existeCampo(param,"id_actual")){    
                id_c_p = elementObject.getAsJsonObject().get("id_actual").getAsInt();
            }
            try {
             funcion=WsFuncion.getConsulta("public.insert_pub_tiendas("+id_u+" ,"+ id_t+" ,"+ id_c_p+" );");
               
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
            }
           if(funcion>0){
            model.addObject("mensaje","exito");
           }else{
            model.addObject("mensaje", "error");
           }
         }
            
            model.setViewName("CrearP");

        }
    return model;
    }
     
     
     //controlador para eliminar publicadores a una tienda en especifico
     @RequestMapping(value = {"/RetirarP"}, method = RequestMethod.GET)
     public ModelAndView geteliminarPublicadores()
    {
        ModelAndView model = new ModelAndView();
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String s = "NULL";
        String s4 = "NULL";
        String valor = "";
        try {
            s = wsQuery.getConsulta("SELECT t.tienda" +
                        " FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u" +
                        " WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and pt.id_usuario=u.id_usuario and u.usuario='"+name+"' and u.id_usuario=t.id_manager ;");
            
            
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //tiendas
        JsonParser parser3 = new JsonParser();
        JsonElement elementObject3;
        s = s.substring(1, s.length()-1);
        StringTokenizer st3 = new StringTokenizer(s,",");
        
        while (st3.hasMoreTokens()) {
            s = st3.nextToken();
            elementObject3 = parser3.parse(s);
            if(existeCampo.existeCampo(s,"tienda")){
                this.tienda = elementObject3.getAsJsonObject()
                        .get("tienda").getAsString();
            }
            listString3.add("<option value=\""+this.tienda+ "\">"+
                                    this.tienda+"</option>");
        }
        //publicadores agregados por actual
        try {
            s4 = wsQuery.getConsulta("SELECT distinct u.usuario" +
                    " FROM usuarios u, pub_tiendas pt, usuarios actual" +
                    " WHERE u.id_tipo_usuario = 2 AND u.usuario != '"+name+"' AND actual.usuario = '"+name+"' AND pt.id_usuario = u.id_usuario AND pt.id_creado_por = actual.id_usuario AND pt.activo=TRUE ;");
            
            
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
                
        //publicadores agregados por actual
        s4 = s4.substring(1, s4.length()-1); //quitarle los corchetes "[]"
        
        JsonParser parser4 = new JsonParser();
        JsonElement elementObject4;
        StringTokenizer st4 = new StringTokenizer(s4,"}");
       
        valor="";
        while (st4.hasMoreTokens()) {
            s4 = st4.nextToken()+"}";
            if (s4.substring(0,1).equals(",")){
                s4 = s4.substring(1);                          
            }
            elementObject4 = parser4.parse(s4);
            if(existeCampo.existeCampo(s4,"usuario")){
                valor = elementObject4.getAsJsonObject().get("usuario").getAsString();
                 listString5.add("<option value=\""+valor+"\">"+
                                    valor+"</option>");
            }
            valor="";
        }
        
        model.addObject("tienda", listString3);
        model.addObject("publicador",listString5);
        model.setViewName("RetirarP");
        return model;
    }
     
     
     
     @RequestMapping(value = {"/RetirarP"}, method = RequestMethod.POST)
     public ModelAndView posteliminarPublicadores(
        @RequestParam (value = "nombreTienda", required = false) String nameTienda, 
        @RequestParam (value = "namePub", required = false) String namePub
                                                )
    {
        ModelAndView model = new ModelAndView();
        //model = geteliminarPublicadores();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        int funcion=0;
        String validar = "";
        String param = "NULL";
        int id_u = 0; //id del usuario a insertar
        int id_t = 0; //id de la tienda para asignar al usuario
        int id_m_p = 0; // id del usuario actual.
        model = geteliminarPublicadores();
        if(!nameTienda.equals("NONE")  && !namePub.equals("NONE") ) {
          try {
                validar = wsQuery.getConsulta("SELECT pub.id_usuario" +
                        " FROM usuarios u, tiendas t, pub_tiendas pub" +
                        " WHERE t.tienda = '"+nameTienda+"' and u.usuario='"+namePub+"' and u.id_usuario = pub.id_usuario and t.id_tienda = pub.id_tienda");
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
                if(existeCampo.existeCampo(param,"id_usuario")){
                    id_u = elementObject.getAsJsonObject().get("id_usuario").getAsInt();
                }

                if(existeCampo.existeCampo(param,"id_tienda")){
                    id_t = elementObject.getAsJsonObject().get("id_tienda").getAsInt();
                }
                if(existeCampo.existeCampo(param,"id_actual")){    
                    id_m_p = elementObject.getAsJsonObject().get("id_actual").getAsInt();
                }

                 try {
                        funcion=WsFuncion.getConsulta("public.delete_pub_tiendas("+id_u+" ,"+ id_t+" ,"+ id_m_p+" );");
                  } catch (ExcepcionServicio ex) {
                        Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
                  }
                 if(funcion>0){
                    model.addObject("mensaje2","exito");
                 }else{

                 model.addObject("mensaje2", "error");
                 
                 }
                
            }else{
                  //muestro msj de que el usuario no existe en pub_tiendas
                 model.addObject("mensaje2", "existe");
            }
        
        model.setViewName("RetirarP");    
    }
    return model;
    }

    @RequestMapping(value = {"/Publicar"}, method = {RequestMethod.GET})
    public ModelAndView publicar(){
        ModelAndView model= new ModelAndView();
        String tiendas="";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        try {
                tiendas = wsQuery.getConsulta("SELECT t.tienda\n" +
                    "  FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u\n" +
                    "  WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and pt.id_usuario=u.id_usuario"
                    + " and u.usuario='"+name+"';");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        JsonParser parser3 = new JsonParser();
        JsonElement elementObject3;
        tiendas = tiendas.substring(1, tiendas.length()-1);
        StringTokenizer st3 = new StringTokenizer(tiendas,",");
        
        while (st3.hasMoreTokens()) {
            tiendas = st3.nextToken();
            elementObject3 = parser3.parse(tiendas);
            this.tienda = elementObject3.getAsJsonObject()
                    .get("tienda").getAsString();
            listString3.add("<option value=\""+this.tienda+ "\">"+
                                    this.tienda+"</option>");
            }
        model.addObject("tienda", listString3);
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
    String ter="";
    String result="";
    String aux="";
    List<String> listStringPlan = new ArrayList<>();
    List<String> listStringEjec = new ArrayList<>();
    List<String> listStringTer = new ArrayList<>();
    List<String> lista_plan = new ArrayList<>();
    List<String> lista_ter = new ArrayList<>();
    List<String> lista_ejec = new ArrayList<>();
    nombreTiendaUser.setnombreTienda(nameTienda);
    model = publicar();

        if(!nameTienda.equals("NONE")) {
            try {
                plan=wsQuery.getConsulta("SELECT pe.id_plan_ejecucion,pe.nro_control_plan, t.tienda, j.job,pe.timestamp_planificacion, pe.nro_control_ejec, pe.observaciones\n" +
                "  FROM public.plan_ejecuciones as pe, public.pasos_plan_ejecucion as ppe, public.tiendas as t, public.jobs as j\n" +
                "  WHERE pe.activo=TRUE and pe.tipo_ejecucion='planificada' and pe.id_plan_ejecucion=ppe.id_plan_ejecucion and ppe.status_plan='en espera' and pe.id_tienda=t.id_tienda and pe.id_job=j.id_job\n" +
                "AND t.tienda='"+nameTienda+"' and pe.timestamp_inicio_ejec = 'NULL';");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        //Planificadas
            if(!plan.equals("[]")){
                model.addObject("tienda2",nombreTiendaUser.getnombreTienda());
                JsonParser parser = new JsonParser();
                JsonElement elementObject;
                plan = plan.substring(1, plan.length()-1);
                StringTokenizer st = new StringTokenizer(plan,"}");
                
                result="";
                while (st.hasMoreTokens()) {
                    plan = st.nextToken()+"}";
                    if (plan.substring(0,1).equals(",")){
                        plan = plan.substring(1);                          
                    }
                    elementObject = parser.parse(plan);
                    valor = elementObject.getAsJsonObject().get("id_plan_ejecucion").getAsString();
                    result= result + "Id Plan Ejecucion = "+valor+"\n";
                    if(existeCampo.existeCampo(plan,"nro_control_plan")){
                    valor = elementObject.getAsJsonObject().get("nro_control_plan").getAsString();
                    result= result + "Nro Control Plan = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(plan,"tienda")){
                    valor = elementObject.getAsJsonObject().get("tienda").getAsString();
                    result= result + "Tienda = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(plan,"job")){
                    valor = elementObject.getAsJsonObject().get("job").getAsString();
                    result= result + "Job = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(plan,"nro_control_ejec")){
                    valor = elementObject.getAsJsonObject().get("nro_control_ejec").getAsString();
                    result= result + "Nro Control Ejecucion = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(plan,"observaciones")){
                    valor = elementObject.getAsJsonObject().get("observaciones").getAsString();
                    result= result + "Observaciones = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(plan,"timestamp_planificacion")){
                        valor = elementObject.getAsJsonObject().get("timestamp_planificacion").getAsString();
                        result= result + "Tiempo de la planificacion = "+valor+"\n";
                    }
                    aux=elementObject.getAsJsonObject().get("id_plan_ejecucion").getAsString(); 
                    listStringPlan.add(result);
                    lista_plan.add("<option value=\""+aux+ "\">"+
                                    aux+"</option>");
                    valor="";
                    result="";
                    aux="";
                }   
            model.addObject("planificado",listStringPlan);
            model.addObject("plan_list", lista_plan); // para mostrar los id de las planificaciones
            }else{
            model.addObject("mensaje_plan","No hay tareas planificadas para la tienda");
            
            }
            //En ejecucion

             
            try {
                ejec=wsQuery.getConsulta("SELECT pe.id_plan_ejecucion,pe.nro_control_plan, t.tienda, j.job, pe.timestamp_planificacion, pe.nro_control_ejec, pe.revisado, pe.observaciones\n" +
                    "FROM public.plan_ejecuciones  as pe, public.pasos_ejecucion as ppe, public.tiendas as t, public.jobs as j\n" +
                    "WHERE pe.activo=TRUE and ppe.activo=TRUE and pe.id_tienda=t.id_tienda and pe.id_job=j.id_job and pe.id_plan_ejecucion=ppe.id_plan_ejecucion and ppe.status_ejecucion='en ejecucion'\n" +
                    "AND t.tienda='"+nameTienda+"';");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(!ejec.equals("[]")){
                
                JsonParser parser2 = new JsonParser();
                JsonElement elementObject2;
                ejec = ejec.substring(1, ejec.length()-1);
                StringTokenizer st2 = new StringTokenizer(ejec,"}");
                
                result="";
                while (st2.hasMoreTokens()) {
                    ejec = st2.nextToken()+"}";
                    if (ejec.substring(0,1).equals(",")){
                        ejec = ejec.substring(1);                          
                    }
                    elementObject2 = parser2.parse(ejec);
                    valor = elementObject2.getAsJsonObject().get("id_plan_ejecucion").getAsString();
                    result= result + "Id Plan Ejecucion = "+valor+"\n";
                     if(existeCampo.existeCampo(ejec,"nro_control_pla")){
                    valor = elementObject2.getAsJsonObject().get("nro_control_plan").getAsString();
                    result= result + "Nro Control Plan = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ejec,"tienda")){
                    valor = elementObject2.getAsJsonObject().get("tienda").getAsString();
                    result= result + "Tienda = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ejec,"job")){
                    valor = elementObject2.getAsJsonObject().get("job").getAsString();
                    result= result + "Job = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ejec,"nro_control_ejec")){
                    valor = elementObject2.getAsJsonObject().get("nro_control_ejec").getAsString();
                    result= result + "Nro Control Ejecucion = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ejec,"revisado")){
                        valor = elementObject2.getAsJsonObject().get("revisado").getAsString();
                        result= result + "Revisado = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ejec,"observaciones")){
                    valor = elementObject2.getAsJsonObject().get("observaciones").getAsString();
                    result= result + "Observaciones = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ejec,"timestamp_planificacion")){
                        valor = elementObject2.getAsJsonObject().get("timestamp_planificacion").getAsString();
                        result= result + "Tiempo de la planificacion = "+valor+"\n";
                    }
                    aux=elementObject2.getAsJsonObject().get("id_plan_ejecucion").getAsString();; 
                    listStringEjec.add(result);
                    lista_ejec.add("<option value=\""+aux+ "\">"+
                                    aux+"</option>");
                    valor="";
                    result="";
                    aux="";

                }
            model.addObject("ejecutado",listStringEjec);
            model.addObject("plan_ejec",lista_ejec );
            }else{
            model.addObject("mensaje_ejec","No hay tareas en ejecucion para la tienda");
            
            }
            // Terminadas
            try {
                ter=wsQuery.getConsulta("SELECT pe.id_plan_ejecucion,pe.nro_control_plan, t.tienda, j.job, pe.timestamp_planificacion, pe.nro_control_ejec, pe.revisado, pe.observaciones, ppe.status_ejecucion \n" +
                    "FROM public.plan_ejecuciones  as pe, public.pasos_ejecucion as ppe, public.tiendas as t, public.jobs as j\n" +
                    "WHERE pe.activo=TRUE and pe.id_tienda=t.id_tienda and pe.id_job=j.id_job and pe.id_plan_ejecucion=ppe.id_plan_ejecucion and ppe.status_ejecucion='finalizada' \n" +
                    "AND t.tienda='"+nameTienda+"';");
            } catch (ExcepcionServicio ex) {
                Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(!ter.equals("[]")){
                
                JsonParser parser3 = new JsonParser();
                JsonElement elementObject3;
                ter = ter.substring(1, ter.length()-1);
                StringTokenizer st3 = new StringTokenizer(ter,"}");
                result="";
                while (st3.hasMoreTokens()) {
                    ter = st3.nextToken()+"}";
                    if (ter.substring(0,1).equals(",")){
                        ter = ter.substring(1);                          
                    }
                    elementObject3 = parser3.parse(ter);
                    valor = elementObject3.getAsJsonObject().get("id_plan_ejecucion").getAsString();
                    result= result + "Id Plan Ejecucion = "+valor+"\n";
                    if(existeCampo.existeCampo(ter,"nro_control_pla")){
                    valor = elementObject3.getAsJsonObject().get("nro_control_plan").getAsString();
                    result= result + "Nro Control Plan = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ter,"tienda")){
                    valor = elementObject3.getAsJsonObject().get("tienda").getAsString();
                    result= result + "Tienda = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ter,"job")){
                    valor = elementObject3.getAsJsonObject().get("job").getAsString();
                    result= result + "Job = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ter,"nro_control_ejec")){
                    valor = elementObject3.getAsJsonObject().get("nro_control_ejec").getAsString();
                    result= result + "Nro Control Ejecucion = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ter,"revisado")){
                        valor = elementObject3.getAsJsonObject().get("revisado").getAsString();
                        result= result + "Revisado = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ter,"observaciones")){
                    valor = elementObject3.getAsJsonObject().get("observaciones").getAsString();
                    result= result + "Observaciones = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(ter,"timestamp_planificacion")){
                        valor = elementObject3.getAsJsonObject().get("timestamp_planificacion").getAsString();
                        result= result + "Tiempo de la planificacion = "+valor+"\n";
                    }
                    aux=elementObject3.getAsJsonObject().get("id_plan_ejecucion").getAsString();; 
                    listStringTer.add(result);
                    lista_ter.add("<option value="+aux+ ">"+
                                    aux+"</option>");
                    
                    
                    valor="";
                    result="";
                    aux="";

                }
            
               //model.addObject("tienda", nombreTiendaUser.getnombreTienda());
                model.addObject("terminado",listStringTer);
                model.addObject("plan_ter", lista_ter);
                
            }else{
                model.addObject("mensaje_ter","No hay tareas terminadas para la tienda");   
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

    
    @RequestMapping(value = {"/recuperarETL"}, method = RequestMethod.GET)
    public ModelAndView getrecuperarETL(){
        ModelAndView model = new ModelAndView();
        model.setViewName("recuperarETL");
        return model;
    }

    
    @RequestMapping(value = {"/ciagregarPlanificacion"}, method = RequestMethod.GET)
    public ModelAndView getciagregarPlanificacion(){
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String s = "NULL";
        try {
            s = wsQuery.getConsulta("SELECT t.tienda" +
                        " FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u" +
                        " WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and pt.id_usuario=u.id_usuario and u.usuario='"+name+"' and u.id_usuario=t.id_manager and t.activo=true ;");
            
            
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //tiendas
        
        JsonParser parser3 = new JsonParser();
        JsonElement elementObject3;
        s = s.substring(1, s.length()-1);
        StringTokenizer st3 = new StringTokenizer(s,",");
        
        while (st3.hasMoreTokens()) {
            s = st3.nextToken();
            elementObject3 = parser3.parse(s);
            this.tienda = elementObject3.getAsJsonObject()
                    .get("tienda").getAsString();
            listString3.add("<option value=\""+this.tienda+ "\">"+
                                    this.tienda+"</option>");
            }
        model.addObject("tienda",listString3);
        model.setViewName("ciagregarPlanificacion");
        return model;
    }
    
    @RequestMapping(value = {"/ciagregarPlanificacion"}, method = RequestMethod.POST)
    public ModelAndView postciagregarPlanificacion(@RequestParam(value = "fecha", 
                                                    required = false) String fecha,
                                                    @RequestParam(value = "hora", 
                                                    required = false) String hora,
                                                    @RequestParam(value = "nombreTienda", 
                                                    required = false) String tienda) throws ParseException{
        
        
        ModelAndView  model = getciagregarPlanificacion();
        int result = -999;
        int result2 = -999;
        String id_tienda = "NULL";
        String id_job = "NULL";        
        String id_user = "NULL";
        Date fechaact = new Date();
        String fechaparam = fecha+" "+hora;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = formatter.parse(fechaparam);
        
        if(date2.after(fechaact)){
        
        
                try{
                    id_tienda = wsQuery.getConsulta("SELECT id_tienda FROM public.tiendas WHERE tienda='"+tienda+"'and activo=TRUE;");
                    id_tienda = id_tienda.substring(1, id_tienda.length()-1);
                    JsonParser parser3 = new JsonParser();
                    JsonElement elementObject;
                    elementObject = parser3.parse(id_tienda);
                    if(existeCampo.existeCampo(id_tienda,"id_tienda")){
                        id_tienda = elementObject.getAsJsonObject()
                        .get("id_tienda").getAsString();
                    }
                    
                    id_job = wsQuery.getConsulta("SELECT id_job FROM public.jobs WHERE job='INICIAR_CARGA' and activo=TRUE;");
                    id_job = id_job.substring(1, id_job.length()-1);
                    JsonParser parser = new JsonParser();
                    JsonElement elementObject2;
                    elementObject2 = parser.parse(id_job);
                    if(existeCampo.existeCampo(id_job,"id_job")){
                        id_job = elementObject2.getAsJsonObject()
                        .get("id_job").getAsString();
                    }
                    
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String name = auth.getName(); //get logged in username
                    
                    
                    id_user = wsQuery.getConsulta("SELECT id_usuario FROM public.usuarios WHERE usuario='"+name+"';");
                    id_user = id_user.substring(1, id_user.length()-1);
                    JsonParser parser2 = new JsonParser();
                    JsonElement elementObject1;
                    elementObject1 = parser2.parse(id_user);
                    id_user = elementObject1.getAsJsonObject()
                    .get("id_usuario").getAsString();    
                    
                    result = WsFuncion.getConsulta("public.insert_plan_ejecuciones_planif("+id_tienda+","+id_job+",'"+fecha+" "+hora+"',"+ id_user+");");
                    if(result>0){
                    result2 = WsFuncion.getConsulta("public.insert_pasos_plan_ejecucion("+result+","+"'en espera'"+","+id_user+");");
                    }
                
                } catch (ExcepcionServicio ex) {
                    Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
                }
        
                if(result2>0){
                    model.addObject("mensaje","exito");                    
                }else{                   
                    model.addObject("mensaje", "error");
                }
                
        }else{
        model.addObject("mensaje5","errorfecha"); 
        }
        model.setViewName("ciagregarPlanificacion");
        return model;
        
    }
    
    @RequestMapping(value = {"/magregarPlanificacion"}, method = RequestMethod.GET)
    public ModelAndView getmagregarPlanificacion(){
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String s = "NULL";
        try {
            s = wsQuery.getConsulta("SELECT t.tienda" +
                        " FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u" +
                        " WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and pt.id_usuario=u.id_usuario and u.usuario='"+name+"' and u.id_usuario=t.id_manager and t.activo=true;");
            
            
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //tiendas
        
        JsonParser parser3 = new JsonParser();
        JsonElement elementObject3;
        s = s.substring(1, s.length()-1);
        StringTokenizer st3 = new StringTokenizer(s,",");
        
        while (st3.hasMoreTokens()) {
            s = st3.nextToken();
            elementObject3 = parser3.parse(s);
            this.tienda = elementObject3.getAsJsonObject()
                    .get("tienda").getAsString();
            listString3.add("<option value=\""+this.tienda+ "\">"+
                                    this.tienda+"</option>");
            }
        model.addObject("tienda",listString3);
        model.setViewName("magregarPlanificacion");
        return model;
    }
    @RequestMapping(value = {"/magregarPlanificacion"}, method = RequestMethod.POST)
    public ModelAndView postmagregarPlanificacion(@RequestParam(value = "fecha", 
                                                    required = false) String fecha,
                                                    @RequestParam(value = "hora", 
                                                    required = false) String hora,
                                                    @RequestParam(value = "dominio", 
                                                    required = false) String dominio,
                                                    @RequestParam(value = "nombreTienda", 
                                                    required = false) String tienda) throws ParseException{
        
        
        ModelAndView  model = getciagregarPlanificacion();
        int result = -999;
        int result2= -999;
        String id_tienda = "NULL";
        String id_job = "NULL";        
        String id_user = "NULL";
        Date fechaact = new Date();
        String fechaparam = fecha+" "+hora;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = formatter.parse(fechaparam);
        
        if(date2.after(fechaact)){
                try{
                    id_tienda = wsQuery.getConsulta("SELECT id_tienda FROM public.tiendas WHERE tienda='"+tienda+"'and activo=TRUE;");
                    id_tienda = id_tienda.substring(1, id_tienda.length()-1);
                    JsonParser parser3 = new JsonParser();
                    JsonElement elementObject;
                    elementObject = parser3.parse(id_tienda);
                    id_tienda = elementObject.getAsJsonObject()
                    .get("id_tienda").getAsString();
                    
//                    id_job = wsQuery.getConsulta("SELECT id_job FROM public.jobs WHERE job='INICIAR_MEDIACION' and activo=TRUE;");
//                    id_job = id_job.substring(1, id_job.length()-1);
//                    JsonParser parser = new JsonParser();
//                    JsonElement elementObject2;
//                    elementObject2 = parser.parse(id_job);
//                    if(existeCampo.existeCampo(id_job,"id_job")){
//                        id_job = elementObject2.getAsJsonObject()
//                        .get("id_job").getAsString();
//                    }
                    
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String name = auth.getName(); //get logged in username
                    
                    
                    id_user = wsQuery.getConsulta("SELECT id_usuario FROM public.usuarios WHERE usuario='"+name+"';");
                    id_user = id_user.substring(1, id_user.length()-1);
                    JsonParser parser2 = new JsonParser();
                    JsonElement elementObject1;
                    elementObject1 = parser2.parse(id_user);
                    id_user = elementObject1.getAsJsonObject()
                    .get("id_usuario").getAsString();
                     
                    
                    
                    result = WsFuncion.getConsulta("public.insert_plan_ejecuciones_planif("+id_tienda+","+dominio+",'"+fecha+" "+hora+"',"+ id_user+");");
                    if(result>0){
                    result2 = WsFuncion.getConsulta("public.insert_pasos_plan_ejecucion("+result+","+"'en espera'"+","+id_user+");");
                    }
                
                } catch (ExcepcionServicio ex) {
                    Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
                }
        
                if(result2>0){
                    model.addObject("mensaje","exito");                    
                }else{
                    model.addObject("mensaje", "error");
                }
                
        }else{
        model.addObject("mensaje5","errorfecha"); 
        }
        model.setViewName("magregarPlanificacion");
        return model;
    }
    
    @RequestMapping(value = {"/ciagregarPlanETL"}, method = RequestMethod.GET)
    public ModelAndView getciagregarPlanETL(){
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String s = "NULL";
        try {
            s = wsQuery.getConsulta("SELECT t.tienda" +
                        " FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u" +
                        " WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and pt.id_usuario=u.id_usuario and u.usuario='"+name+"' and u.id_usuario=t.id_manager and t.activo=true ;");
            
            
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //tiendas
        
        JsonParser parser3 = new JsonParser();
        JsonElement elementObject3;
        s = s.substring(1, s.length()-1);
        StringTokenizer st3 = new StringTokenizer(s,",");
        
        while (st3.hasMoreTokens()) {
            s = st3.nextToken();
            elementObject3 = parser3.parse(s);
            this.tienda = elementObject3.getAsJsonObject()
                    .get("tienda").getAsString();
            listString3.add("<option value=\""+this.tienda+ "\">"+
                                    this.tienda+"</option>");
        }
        
        model.addObject("tienda",listString3);
        model.setViewName("ciagregarPlanETL");
        return model;
    }
    @RequestMapping(value = {"/ciagregarPlanETL"}, method = RequestMethod.POST)
    public ModelAndView postciagregarPlanETL(@RequestParam(value = "nombreTienda", 
                                                    required = false) String nameTienda){
        ModelAndView model = new ModelAndView();
        nombreTiendaUser.setnombreTienda(nameTienda);
        model = getciagregarPlanETL();
        model.addObject("titulo",nameTienda);
        String ultima_ejecucion = "NULL";
        String id_tienda = "NULL";
        String id_plan_ejec = "NULL";
        String id_job = "NULL";
        String list_etl = "NULL";
        String list_etl2 = "NULL";
        listString.clear();
        listString2.clear();
        listString4.clear();
        try {
            id_tienda = wsQuery.getConsulta("SELECT id_tienda FROM public.tiendas WHERE tienda='"+nameTienda+"'and activo=TRUE;");
                    id_tienda = id_tienda.substring(1, id_tienda.length()-1);
                    JsonParser parser3 = new JsonParser();
                    JsonElement elementObject;
                    elementObject = parser3.parse(id_tienda);
                    id_tienda = elementObject.getAsJsonObject()
                    .get("id_tienda").getAsString();
            id_job = wsQuery.getConsulta("SELECT id_job FROM public.jobs WHERE job='INICIAR_CARGA' and activo=TRUE;");
                    id_job = id_job.substring(1, id_job.length()-1);
                    JsonParser parser = new JsonParser();
                    JsonElement elementObject2;
                    elementObject2 = parser.parse(id_job);
                    if(existeCampo.existeCampo(id_job,"id_job")){
                        id_job = elementObject2.getAsJsonObject()
                        .get("id_job").getAsString();
                    }
            ultima_ejecucion = wsQuery.getConsulta("SELECT max(id_plan_ejecucion) FROM public.plan_ejecuciones WHERE id_tienda="+id_tienda+" and id_job="+id_job+" and timestamp_fin_ejec != 'NULL';");
                    
                    if(!ultima_ejecucion.equals("[]")){
                        
                        id_plan_ejec = ultima_ejecucion.substring(1, ultima_ejecucion.length()-1);
                        JsonParser parser2 = new JsonParser();
                        JsonElement elementObject3;
                        elementObject3 = parser2.parse(id_plan_ejec);
                        id_plan_ejec = elementObject3.getAsJsonObject()
                        .get("max").getAsString();
                    
                    list_etl = wsQuery.getConsulta("SELECT e.etl FROM public.ejecucion_etls as ee, etls as e WHERE ee.id_ejecucion='"+id_plan_ejec+"' and ee.status_ejec='ejecutado' and ee.id_etl=e.id_etl;");
                        JsonParser parser4 = new JsonParser();
                        JsonElement elementObject4;
                        list_etl = list_etl.substring(1, list_etl.length()-1);
                        StringTokenizer st3 = new StringTokenizer(list_etl,",");              
                        while (st3.hasMoreTokens()) {
                            list_etl = st3.nextToken();
                            elementObject4 = parser4.parse(list_etl);
                            list_etl = elementObject4.getAsJsonObject()
                                    .get("etl").getAsString();
                            listString4.add(list_etl+"\n\n");
                        }
                        model.addObject("correctos",listString4);
                    
                                            
                    list_etl2 = wsQuery.getConsulta("SELECT e.etl FROM public.ejecucion_etls as ee, etls as e WHERE ee.id_ejecucion='"+id_plan_ejec+"' and ee.status_ejec='no ejecutado' and ee.id_etl=e.id_etl;");
                        JsonParser parser5 = new JsonParser();
                        JsonElement elementObject5;
                        list_etl2 = list_etl2.substring(1, list_etl2.length()-1);
                        StringTokenizer st4 = new StringTokenizer(list_etl2,",");
                        while (st4.hasMoreTokens()) {
                            list_etl2 = st4.nextToken();
                            elementObject5 = parser5.parse(list_etl2);
                            list_etl2 = elementObject5.getAsJsonObject()
                                    .get("etl").getAsString();
                            listString2.add(list_etl2+"\n\n");
                            listString.add("<option value=\""+list_etl2+ "\">"+list_etl2+"</option>");
                        }
                        model.addObject("incorrectos",listString2);
                        model.addObject("incorrectos2",listString);
    
                    }else{
                        model.addObject("mensaje", "vacio");
                    }
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model.setViewName("ciagregarPlanETL");
        return model;
    }
    @RequestMapping(value = {"/ci2agregarPlanETL"}, method = RequestMethod.POST)
    public ModelAndView getci2agregarPlanETL(@RequestParam(value = "fecha", 
                                                    required = false) String fecha,
                                                    @RequestParam(value = "hora", 
                                                    required = false) String hora,
                                                    @RequestParam(value = "nombreETL", 
                                                    required = false) String nameETL) throws ParseException{
        
        ModelAndView model = new ModelAndView();
        model = getciagregarPlanETL();      
        int result = -999;
        int result2 = -999;
        int result3 = -999;
        String id_tienda = "NULL";
        String id_job = "NULL";        
        String id_user = "NULL";
        
        Date fechaact = new Date();
        String fechaparam = fecha+" "+hora;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = formatter.parse(fechaparam);
        
        if(date2.after(fechaact)){
        
   
        
            if(!nameETL.equals("NONE")){
                try{
                    id_tienda = wsQuery.getConsulta("SELECT id_tienda FROM public.tiendas WHERE tienda='"+nombreTiendaUser.getnombreTienda()+"'and activo=TRUE;");
                    id_tienda = id_tienda.substring(1, id_tienda.length()-1);
                    JsonParser parser3 = new JsonParser();
                    JsonElement elementObject;
                    elementObject = parser3.parse(id_tienda);
                    id_tienda = elementObject.getAsJsonObject()
                    .get("id_tienda").getAsString();
                    
                    id_job = wsQuery.getConsulta("SELECT id_job FROM public.jobs WHERE job='INICIAR_CARGA_ETL' and activo=TRUE;");
                    id_job = id_job.substring(1, id_job.length()-1);
                    JsonParser parser = new JsonParser();
                    JsonElement elementObject2;
                    elementObject2 = parser.parse(id_job);
                    id_job = elementObject2.getAsJsonObject()
                    .get("id_job").getAsString();
                    
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String name = auth.getName(); //get logged in username
                                      
                    id_user = wsQuery.getConsulta("SELECT id_usuario FROM public.usuarios WHERE usuario='"+name+"';");
                    id_user = id_user.substring(1, id_user.length()-1);
                    JsonParser parser2 = new JsonParser();
                    JsonElement elementObject1;
                    elementObject1 = parser2.parse(id_user);
                    id_user = elementObject1.getAsJsonObject()
                    .get("id_usuario").getAsString();

                    result = WsFuncion.getConsulta("public.insert_plan_ejecuciones_planif("+id_tienda+","+id_job+",'"+fecha+" "+hora+"',"+ id_user+");");
                    if(result >0 ){
                        result2 = WsFuncion.getConsulta("public.insert_parametros_ejecucion("+result+",'transformaciones','"+nameETL+"',"+id_user+");");
                        if(result2>0){
                        result3 = WsFuncion.getConsulta("public.insert_pasos_plan_ejecucion("+result2+","+"'en espera'"+","+id_user+");");
                            if(result3>0){
                                model.addObject("mensaje2","exito");                    
                            }else{
                                model.addObject("mensaje2", "error");
                            }
                        }else{
                            model.addObject("mensaje2", "error");
                        }
                    }else{
                        model.addObject("mensaje2", "error");
                    }        
                
                } catch (ExcepcionServicio ex) {
                    Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                model.addObject("mensaje2", "error");
            }
            
        }else{
        model.addObject("mensaje5","errorfecha"); 
        }
        model.setViewName("ciagregarPlanETL");
        return model;
    }
    
    @RequestMapping(value = {"/magregarPlanETL"}, method = RequestMethod.GET)
    public ModelAndView getmagregarPlanETL(){
         ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        String s = "NULL";
        try {
            s = wsQuery.getConsulta("SELECT t.tienda" +
                        " FROM public.pub_tiendas as pt,public.tiendas as t,public.usuarios as u" +
                        " WHERE pt.activo=TRUE and pt.id_tienda=t.id_tienda and pt.id_usuario=u.id_usuario and u.usuario='"+name+"' and u.id_usuario=t.id_manager and t.activo=true;");
            
            
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        JsonParser parser3 = new JsonParser();
        JsonElement elementObject3;
        s = s.substring(1, s.length()-1);
        StringTokenizer st3 = new StringTokenizer(s,",");
        
        while (st3.hasMoreTokens()) {
            s = st3.nextToken();
            elementObject3 = parser3.parse(s);
            this.tienda = elementObject3.getAsJsonObject()
                    .get("tienda").getAsString();
            listString3.add("<option value=\""+this.tienda+ "\">"+
                                    this.tienda+"</option>");
        }
        
        model.addObject("tienda",listString3);
        model.setViewName("magregarPlanETL");
        return model;
    }
    @RequestMapping(value = {"/magregarPlanETL"}, method = RequestMethod.POST)
    public ModelAndView postmagregarPlanETL(@RequestParam(value = "nombreTienda", 
                                                    required = false) String nameTienda){
        ModelAndView model = new ModelAndView();
        nombreTiendaUser.setnombreTienda(nameTienda);
        model = getciagregarPlanETL();
        String ultima_ejecucion = "NULL";
        String id_tienda = "NULL";
        String id_plan_ejec = "NULL";
        String id_job = "NULL";
        String list_etl = "NULL";
        String list_etl2 = "NULL";
        listString.clear();
        listString2.clear();
        listString4.clear();
        
        try {
            id_tienda = wsQuery.getConsulta("SELECT id_tienda FROM public.tiendas WHERE tienda='"+nameTienda+"'and activo=TRUE;");
                    id_tienda = id_tienda.substring(1, id_tienda.length()-1);
                    JsonParser parser3 = new JsonParser();
                    JsonElement elementObject;
                    elementObject = parser3.parse(id_tienda);
                    id_tienda = elementObject.getAsJsonObject()
                    .get("id_tienda").getAsString();
            id_job = wsQuery.getConsulta("SELECT id_job FROM public.jobs WHERE job='INICIAR_CARGA' and activo=TRUE;");
                    id_job = id_job.substring(1, id_job.length()-1);
                    JsonParser parser = new JsonParser();
                    JsonElement elementObject2;
                    elementObject2 = parser.parse(id_job);
                    id_job = elementObject2.getAsJsonObject()
                    .get("id_job").getAsString();
            ultima_ejecucion = wsQuery.getConsulta("SELECT max(id_plan_ejecucion) FROM public.plan_ejecuciones WHERE id_tienda="+id_tienda+" and id_job="+id_job+" and timestamp_fin_ejec != 'NULL';");
                    
                    if(!ultima_ejecucion.equals("[]")){
                        
                        id_plan_ejec = ultima_ejecucion.substring(1, ultima_ejecucion.length()-1);
                        JsonParser parser2 = new JsonParser();
                        JsonElement elementObject3;
                        elementObject3 = parser2.parse(id_plan_ejec);
                        id_plan_ejec = elementObject3.getAsJsonObject()
                        .get("max").getAsString();
                    
                    list_etl = wsQuery.getConsulta("SELECT e.etl FROM public.ejecucion_etls as ee, etls as e WHERE ee.id_ejecucion='"+id_plan_ejec+"' and ee.status_ejec='ejecutado' and ee.id_etl=e.id_etl;");
                        JsonParser parser4 = new JsonParser();
                        JsonElement elementObject4;
                        list_etl = list_etl.substring(1, list_etl.length()-1);
                        StringTokenizer st3 = new StringTokenizer(list_etl,",");              
                        while (st3.hasMoreTokens()) {
                            list_etl = st3.nextToken();
                            elementObject4 = parser4.parse(list_etl);
                            list_etl = elementObject4.getAsJsonObject()
                                    .get("etl").getAsString();
                            listString4.add(list_etl+"\n\n");
                        }
                        model.addObject("correctos",listString4);
                    
                                            
                    list_etl2 = wsQuery.getConsulta("SELECT e.etl FROM public.ejecucion_etls as ee, etls as e WHERE ee.id_ejecucion='"+id_plan_ejec+"' and ee.status_ejec='no ejecutado' and ee.id_etl=e.id_etl;");
                        JsonParser parser5 = new JsonParser();
                        JsonElement elementObject5;
                        list_etl2 = list_etl2.substring(1, list_etl2.length()-1);
                        StringTokenizer st4 = new StringTokenizer(list_etl2,",");
                        while (st4.hasMoreTokens()) {
                            list_etl2 = st4.nextToken();
                            elementObject5 = parser5.parse(list_etl2);
                            list_etl2 = elementObject5.getAsJsonObject()
                                    .get("etl").getAsString();
                            listString2.add(list_etl2+"\n\n");
                            listString.add("<option value=\""+list_etl2+ "\">"+list_etl2+"</option>");
                        }
                        model.addObject("incorrectos",listString2);
                        model.addObject("incorrectos2",listString);
    
                    }else{
                        model.addObject("mensaje", "vacio");
                    }
        } catch (ExcepcionServicio e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model.setViewName("magregarPlanETL");
        return model;
    }
    
    @RequestMapping(value = {"/m2agregarPlanETL"}, method = RequestMethod.POST)
    public ModelAndView getm2agregarPlanETL(@RequestParam(value = "fecha", 
                                                    required = false) String fecha,
                                                    @RequestParam(value = "hora", 
                                                    required = false) String hora,
                                                    @RequestParam(value = "nombreETL", 
                                                    required = false) String nameETL) throws ParseException{
        
        ModelAndView model = new ModelAndView();
        model = getciagregarPlanETL();      
        int result = -999;
        int result2 = -999;
        int result3 = -999;
        String id_tienda = "NULL";
        String id_job = "NULL";        
        String id_user = "NULL";
        
                
        Date fechaact = new Date();
        String fechaparam = fecha+" "+hora;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date2 = formatter.parse(fechaparam);
        
        if(date2.after(fechaact)){
        
   
  
            if(!nameETL.equals("NONE")){
                try{
                    id_tienda = wsQuery.getConsulta("SELECT id_tienda FROM public.tiendas WHERE tienda='"+nombreTiendaUser.getnombreTienda()+"'and activo=TRUE;");
                    id_tienda = id_tienda.substring(1, id_tienda.length()-1);
                    JsonParser parser3 = new JsonParser();
                    JsonElement elementObject;
                    elementObject = parser3.parse(id_tienda);
                    id_tienda = elementObject.getAsJsonObject()
                    .get("id_tienda").getAsString();
                    
                    id_job = wsQuery.getConsulta("SELECT id_job FROM public.jobs WHERE job='INICIAR_MEDIACION_ETL' and activo=TRUE;");
                    id_job = id_job.substring(1, id_job.length()-1);
                    JsonParser parser = new JsonParser();
                    JsonElement elementObject2;
                    elementObject2 = parser.parse(id_job);
                    id_job = elementObject2.getAsJsonObject()
                    .get("id_job").getAsString();
                    
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String name = auth.getName(); //get logged in username
                                      
                    id_user = wsQuery.getConsulta("SELECT id_usuario FROM public.usuarios WHERE usuario='"+name+"';");
                    id_user = id_user.substring(1, id_user.length()-1);
                    JsonParser parser2 = new JsonParser();
                    JsonElement elementObject1;
                    elementObject1 = parser2.parse(id_user);
                    id_user = elementObject1.getAsJsonObject()
                    .get("id_usuario").getAsString();

                    result = WsFuncion.getConsulta("public.insert_plan_ejecuciones_planif("+id_tienda+","+id_job+",'"+fecha+" "+hora+"',"+ id_user+");");
                    if(result >0 ){
                        result2 = WsFuncion.getConsulta("public.insert_parametros_ejecucion("+result+",'transformaciones','"+nameETL+"',"+id_user+");");
                         if(result2>0){
                        result3 = WsFuncion.getConsulta("public.insert_pasos_plan_ejecucion("+result2+","+"'en espera'"+","+id_user+");");
                            if(result3>0){
                                model.addObject("mensaje2","exito");                    
                            }else{
                                model.addObject("mensaje2", "error");
                            }
                         }else{
                            model.addObject("mensaje2", "error");
                        }
                    }else{
                        model.addObject("mensaje2", "error");
                    }        
                
                } catch (ExcepcionServicio ex) {
                    Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                model.addObject("mensaje2", "error");
            }
            
                        
        }else{
        model.addObject("mensaje5","errorfecha"); 
        }
        model.setViewName("magregarPlanETL");
        return model;
    }
    
      @RequestMapping(value = {"/GestionPublicar"}, method = RequestMethod.GET)
    public ModelAndView getgestiontiendaPublicacion(){
        ModelAndView model = new ModelAndView();
        model.setViewName("GestionPublicar");
        return model;
    }
    

    @RequestMapping(value = {"/ejecutaETL"}, method = RequestMethod.POST)
    public ModelAndView ejecETL(
           
            @RequestParam(value = "ejecutadas",required = false) String ejecutadas)
    {
        ModelAndView model = new ModelAndView();
        model = publicar() ;
        String etl_ejec="";
        String result="";
        String valor="";
        List<String> listString6 = new ArrayList<>();
        int ejec;
        
        if(!ejecutadas.equals("NONE")) {
            ejec=Integer.parseInt(ejecutadas);
            try {
                   etl_ejec=wsQuery.getConsulta("SELECT  e.id_etl, e.etl, ee.status_ejec\n" +
                            "FROM public.plan_ejecuciones as pe, public.ejecucion_etls as ee, public.etls as e\n" +
                            "WHERE pe.id_plan_ejecucion="+ejec+" and pe.id_plan_ejecucion = ee.id_ejecucion and ee.id_etl = e.id_etl and e.activo=TRUE; ");
               } catch (ExcepcionServicio ex) {
                   Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
               }
            
            if(!etl_ejec.equals("[]")){
                JsonParser parser = new JsonParser();
                JsonElement elementObject;
                etl_ejec = etl_ejec.substring(1, etl_ejec.length()-1);
                StringTokenizer st = new StringTokenizer(etl_ejec,"}");
                result="";
                while (st.hasMoreTokens()) {
                    etl_ejec = st.nextToken()+"}";
                    if (etl_ejec.substring(0,1).equals(",")){
                        etl_ejec = etl_ejec.substring(1);                          
                    }
                    elementObject = parser.parse(etl_ejec);
                    if(existeCampo.existeCampo(etl_ejec,"id_etl")){
                        valor = elementObject.getAsJsonObject().get("id_etl").getAsString();
                        result= result + "Id Etl = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(etl_ejec,"etl")){
                        valor = elementObject.getAsJsonObject().get("etl").getAsString();
                        result= result + "Nombre Etl = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(etl_ejec,"status_ejec")){
                        valor = elementObject.getAsJsonObject().get("status_ejec").getAsString();
                        result= result + "Estatus ejecucion = "+valor+"\n"; 
                    }
                    listString6.add(result+"\n\n");
                    valor="";
                    result="";
                    
                }
             model.addObject("lista_etl_ejec",listString6);
            }else{
              model.addObject("msj_ejec","No hay etls asociados a esta planificacion");
            }
            
            
        }
        model.addObject("tienda2", nombreTiendaUser.getnombreTienda());
        model.setViewName("Publicar");
        return model;
    }
    
     @RequestMapping(value = {"/terminaETL"}, method = RequestMethod.POST)
    public ModelAndView terminaETL(
            @RequestParam(value = "terminadas",required = false) String terminadas)
    {
        ModelAndView model = new ModelAndView();
        model = publicar();        
        String etl_ter="";
        String result="";
        String valor="";
        List<String> listString6 = new ArrayList<>();
        int ter;
        
        
        if(!terminadas.equals("NONE")) {
            ter=Integer.parseInt(terminadas);
            try {
                   etl_ter=wsQuery.getConsulta("SELECT  e.id_etl, e.etl, ee.status_ejec\n" +
                            "FROM public.plan_ejecuciones as pe, public.ejecucion_etls as ee, public.etls as e\n" +
                            "WHERE pe.id_plan_ejecucion="+ter+" and pe.id_plan_ejecucion = ee.id_ejecucion and ee.id_etl = e.id_etl and e.activo=TRUE; ");
               } catch (ExcepcionServicio ex) {
                   Logger.getLogger(publicadorControlador.class.getName()).log(Level.SEVERE, null, ex);
               }
            
            if(!etl_ter.equals("[]")){
                JsonParser parser = new JsonParser();
                JsonElement elementObject;
                etl_ter = etl_ter.substring(1, etl_ter.length()-1);
                StringTokenizer st = new StringTokenizer(etl_ter,"}");
                result="";
                while (st.hasMoreTokens()) {
                    etl_ter = st.nextToken()+"}";
                    if (etl_ter.substring(0,1).equals(",")){
                        etl_ter = etl_ter.substring(1);                          
                    }
                    elementObject = parser.parse(etl_ter);
                    if(existeCampo.existeCampo(etl_ter,"id_etl")){
                        valor = elementObject.getAsJsonObject().get("id_etl").getAsString();
                        result= result + "Id Etl = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(etl_ter,"etl")){
                        valor = elementObject.getAsJsonObject().get("etl").getAsString();
                        result= result + "Nombre Etl = "+valor+"\n";
                    }
                    if(existeCampo.existeCampo(etl_ter,"status_ejec")){
                        valor = elementObject.getAsJsonObject().get("status_ejec").getAsString();
                        result= result + "Estatus ejecucion = "+valor+"\n"; 
                    }
                    listString6.add(result+"\n\n");
                    valor="";
                    result="";
                    
                }
             model.addObject("lista_etl_ter",listString6);
            }else{
              model.addObject("msj_ter","No hay etls asociados a esta planificacion");
            }
            
            
        }
        model.addObject("tienda2", nombreTiendaUser.getnombreTienda());
        model.setViewName("Publicar");
        return model;
    }
    
    
}
