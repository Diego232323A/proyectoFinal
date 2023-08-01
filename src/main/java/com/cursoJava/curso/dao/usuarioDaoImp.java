package com.cursoJava.curso.dao;

import com.cursoJava.curso.models.Usuario;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import de.mkammerer.argon2.*;
@Repository
@Transactional
public class usuarioDaoImp implements usuarioDao{

    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<Usuario> getUsuarios() {
        String query = "FROM Usuario";
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = entityManager.find(Usuario.class , id);
        entityManager.remove(usuario);
    }

    @Override
    public void registrar(Usuario usuario) {
        entityManager.merge(usuario);
    }

    @Override
    public Usuario obtenerUsuarioPorCredenciales(Usuario usuario) {

        String query = "FROM Usuario WHERE email =:email";
        List<Usuario> lista = entityManager.createQuery(query)
                .setParameter("email", usuario.getEmail())
                .getResultList();

        if(lista.isEmpty()){
            return null;
        }

        String password_hash = lista.get(0).getPassword();

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2i);
        if (argon2.verify(password_hash, usuario.getPassword())){
            return lista.get(0);

        }
        return null;

         //lo mimso de abajo pero de manera diferente
        //if (lista.isEmpty()){
        //    return false;
        //}else{
        //    return true;
        //}


    }


}
