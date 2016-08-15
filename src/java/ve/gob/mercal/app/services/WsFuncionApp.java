package ve.gob.mercal.app.services;

import java.io.Serializable;

import org.springframework.stereotype.Service;
import com.phdconsultores.ws.clientews.ClienteFuncionApp;
import com.phdconsultores.ws.exception.ExcepcionServicio;


@Service
public class WsFuncionApp extends ClienteFuncionApp implements Serializable{
private static final long serialVersionUID = 1L;
private int result;
public WsFuncionApp(){
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
public int getConsulta(String query) throws ExcepcionServicio{
            
result = -999;
try{
result = this.funcionapp(query);
}catch (Exception e){
throw new ExcepcionServicio(e.getMessage());
}
return result;
}
}