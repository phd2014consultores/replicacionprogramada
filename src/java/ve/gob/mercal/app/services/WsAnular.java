/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.mercal.app.services;
import java.io.Serializable;

import org.springframework.stereotype.Service;
import com.phdconsultores.ws.clientews.ClienteAnularEjecucion;
import com.phdconsultores.ws.exception.ExcepcionServicio;
/**
 *
 * @author wso2
 */
@Service
public class  WsAnular extends ClienteAnularEjecucion implements Serializable{
    private static final long serialVersionUID = 1L;
    private int result;
    
    public WsAnular(){
        result = 0;
    }
    
    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
    
    public static long getSerialversionuid(){
        return serialVersionUID;
    }
    
    public int anularKitchen(String id) throws ExcepcionServicio{
            
        result = -999;
        try{
            result = this.anularejecucion(id);
        }catch (Exception e){
            throw new ExcepcionServicio(e.getMessage());
        }
        return result;
    }
    
    
}
