package ve.gob.mercal.app.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import ve.gob.mercal.app.services.WsUsuario;

import com.phdconsultores.ws.exception.ExcepcionServicio;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired	
	public WsUsuario wsUsuario;
	
	private String id;
	private String uname;
	private String upassword;
	private String urole;
	
	public String getId() {
		return id;
	}

	public String getUname() {
		return uname;
	}

	public String getUpassword() {
		return upassword;
	}

	public String getUrole() {
		return urole;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		uname = authentication.getName();
        upassword = authentication.getCredentials().toString();
        
        
        //usuario = new WsUsuario();
        //Invocamos al servicio        
        try {
			wsUsuario.consultarUsuario(uname, upassword);
		} catch (ExcepcionServicio e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        id = new String(wsUsuario.getId_usuario());
        uname = new String(wsUsuario.getUsuario());
        upassword = new String(wsUsuario.getPassword());
        urole = new String(wsUsuario.getTipo_usuario());
        
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + urole));
        
        Authentication auth = new UsernamePasswordAuthenticationToken(uname, upassword, grantedAuths);
        
        return auth;
		
	}

	@Override	
	public boolean supports(Class<? extends Object> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
 
    }

}
