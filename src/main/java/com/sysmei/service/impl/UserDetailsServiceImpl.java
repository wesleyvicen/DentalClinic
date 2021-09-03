/**
 *
 */
package com.sysmei.service.impl;

import com.sysmei.model.Usuario;
import com.sysmei.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Classe de Serviço que implementa a interface UserDetailsService
 */
@Service public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired UsuarioServiceImpl usuarioService;

    @Override public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.getUsuarioWithLogin(login);

        if (usuario == null) {
            throw new UsernameNotFoundException(login);
        }
        return new UserSS(usuario.getId(), usuario.getLogin(), usuario.getSenha(),
            usuario.getPerfis());
    }

}
