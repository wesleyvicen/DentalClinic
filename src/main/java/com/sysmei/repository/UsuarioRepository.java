package com.sysmei.repository;

import com.sysmei.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query(value = "Select usuario from Usuario usuario where usuario.login = :login")
    Usuario getUsuarioWithLogin(@Param("login") String login);

    @Query(value = "Select usuario from Usuario usuario where usuario.login = :login and usuario.senha = :senha")
    Usuario getUsuarioWithLoginAndSenha(@Param("login") String login, @Param("senha") String senha);

    @Query(nativeQuery = true, value = "SELECT * FROM USUARIO\r\n" + "INNER JOIN CONTA\r\n"
        + "ON (USUARIO.LOGIN = CONTA.LOGIN_USUARIO) WHERE usuario.id= :id")
    List<Usuario> findUsuarioAndConta(@Param("id") Integer id);

    @Query("Select usuario from Usuario usuario where usuario.verificationCode = :code")
    public Usuario findByVerificationCode(String code);
}
