package ve.gob.mercal.app.services;

import java.io.Serializable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.phdconsultores.ws.clientews.ClienteLoginApp;
import com.phdconsultores.ws.exception.ExcepcionServicio;

@Service
//@Scope("prototype")
//@Scope(value = "session")
//@Scope("session")
// @Scope(value="session")
//@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
@Transactional
public class WsUsuario extends ClienteLoginApp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id_usuario;
	private String usuario;
	private String password;
	private String tipo_usuario;

	private void inicializarUsuario() {
		id_usuario = "NULL";
		usuario = "NULL";
		password = "NULL";
		tipo_usuario = "NULL";
	}

	public WsUsuario() {
		inicializarUsuario();
	}

	public String getId_usuario() {
		return id_usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getPassword() {
		return password;
	}

	public String getTipo_usuario() {
		return tipo_usuario;
	}

	public void consultarUsuario(String usuario, String password

	) throws ExcepcionServicio {

		String s = null;

		this.inicializarUsuario();

		try {
			s = this.loginapp(usuario, password);
		} catch (Exception e) {
			throw new ExcepcionServicio(e.getMessage());

		}

		if (s != null) {
			JsonParser parser = new JsonParser();
			JsonElement elementObject = parser.parse(s);
			this.id_usuario = elementObject.getAsJsonObject().get("id_usuario")
					.getAsString();
			this.usuario = elementObject.getAsJsonObject().get("usuario")
					.getAsString();
			this.password = elementObject.getAsJsonObject().get("password")
					.getAsString();
			this.tipo_usuario = elementObject.getAsJsonObject()
					.get("tipo_usuario").getAsString();
		}

	}

}
