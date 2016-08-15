package ve.gob.mercal.app.services;

import java.io.Serializable;

import org.springframework.stereotype.Service;
import com.phdconsultores.ws.clientews.ClienteQueryApp;
import com.phdconsultores.ws.exception.ExcepcionServicio;


@Service
public class WsQuery extends ClienteQueryApp implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String result;
	
	public WsQuery(){
		result = "NULL";
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public static long getSerialversionuid(){
		return serialVersionUID;
	}
	
	public String getConsulta(String query) throws ExcepcionServicio{
		
		String s = null;
		
		result = "NULL";
		
		try{
			s = this.queryapp(query);
		}catch (Exception e){
			throw new ExcepcionServicio(e.getMessage());
		}
		
		if(s != null){
			result = s;
		}
		
		return result;
	}
}
