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
 * @author pbonillo
 */
@Component
@Scope(value = "session")
public class nombreTienda {
    
    private String nameTienda;
    
    public String setnombreTienda (String nameTienda){    
        this.nameTienda = nameTienda;     
        return this.nameTienda;
    }
    
    public String getnombreTienda (){
        return this.nameTienda;
    }
}
