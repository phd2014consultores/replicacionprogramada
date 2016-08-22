/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mercal.app.models;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author phd2014
 */
@Component
@Scope(value = "session")
public class existeCampo {
    private String json;
    private String palabra;
    
    public boolean existeCampo(String json,String palabra){   
        this.json = json;
        this.palabra = palabra;
        return this.json.toLowerCase().contains(this.palabra.toLowerCase());
    }    
}