package ve.gob.mercal.app.models;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
//@Scope(value = "prototype")
@Scope(value = "session")
//@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UsuarioSession implements Serializable{
	
	
	private String uname;
	private String upassword;
	private String urole;
	private boolean inizializado;
	
	public UsuarioSession(){
		id = "0";
		uname = "NULL";
		upassword = "NULL";
		urole = "NULL";
		inizializado = false;
	}
	
	public boolean isInizializado() {
		return inizializado;
	}

	public void setInizializado(boolean inizializado) {
		this.inizializado = inizializado;
	}

	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpassword() {
		return upassword;
	}
	public void setUpassword(String upassword) {
		this.upassword = upassword;
	}
	public String getUrole() {
		return urole;
	}
	public void setUrole(String urole) {
		this.urole = urole;
	}

}
