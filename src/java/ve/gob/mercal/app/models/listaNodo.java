/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mercal.app.models;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author phd2014
 */
@Component
@Scope(value = "session")
public class listaNodo {
    private static List<String> listString = new ArrayList<>();
    private static String aux="";
    
    public static void setlistaNodo (String x){    
        listString.add(x);
    }
    
    public static String getlistaNodo (String ip){
        aux="";
        for (int i=0; i<listString.size();i++){
            if(!existeCampo(listString.get(i),"\""+ip+"\"")){
                aux = aux+listString.get(i)+",";
            }
        }
        listString.clear();
        return aux;
    }
    public static boolean existeCampo(String json,String palabra){   

        return json.toLowerCase().contains(palabra.toLowerCase());
    }    
}
