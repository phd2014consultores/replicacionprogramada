package ve.gob.mercal.app.models;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class Tienda {
	
	private String tienda;
	private String host_bd_oracle;
	private String usuario_bd_oracle;
	private String pass_usuario_bd_oracle;
	private String bd_oracle;
	private String manager;
	
	

	public Tienda(String tienda, String host_bd_oracle,
			String usuario_bd_oracle, String pass_usuario_bd_oracle,
			String bd_oracle, String manager) {
		super();
		this.tienda = tienda;
		this.host_bd_oracle = host_bd_oracle;
		this.usuario_bd_oracle = usuario_bd_oracle;
		this.pass_usuario_bd_oracle = pass_usuario_bd_oracle;
		this.bd_oracle = bd_oracle;
		this.manager = manager;
	}

	public String getTienda() {
		return tienda;
	}

	public void setTienda(String tienda) {
		this.tienda = tienda;
	}

	public String getHost_bd_oracle() {
		return host_bd_oracle;
	}

	public void setHost_bd_oracle(String host_bd_oracle) {
		this.host_bd_oracle = host_bd_oracle;
	}

	public String getUsuario_bd_oracle() {
		return usuario_bd_oracle;
	}

	public void setUsuario_bd_oracle(String usuario_bd_oracle) {
		this.usuario_bd_oracle = usuario_bd_oracle;
	}

	public String getPass_usuario_bd_oracle() {
		return pass_usuario_bd_oracle;
	}

	public void setPass_usuario_bd_oracle(String pass_usuario_bd_oracle) {
		this.pass_usuario_bd_oracle = pass_usuario_bd_oracle;
	}

	public String getBd_oracle() {
		return bd_oracle;
	}

	public void setBd_oracle(String bd_oracle) {
		this.bd_oracle = bd_oracle;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
	
	

}
