package com.cursoJava.curso.controllers;
import com.cursoJava.curso.models.Evento;
import com.cursoJava.curso.dao.eventoDao;
import com.cursoJava.curso.utils.JWTUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class EventoController {

    @Autowired
    private eventoDao eventoDao;
    @Autowired
    private JWTUtil jwtUtil;
    @PersistenceContext
    EntityManager entityManager;


    @RequestMapping(value= "api/eventos/{idevento}", method = RequestMethod.PUT)
    public void editarEvento( @RequestBody Evento eventoActualizado, @PathVariable Long idevento){

        Evento eventoExistente = entityManager.find(Evento.class, idevento);

        if (eventoExistente == null){
            ResponseEntity.notFound().build();
        } else {

            eventoExistente.setNombre(eventoActualizado.getNombre());
            eventoExistente.setDescripcion(eventoActualizado.getDescripcion());
            eventoExistente.setDuracion(eventoActualizado.getDuracion());
            eventoExistente.setCantboletosdisponibles(eventoActualizado.getCantboletosdisponibles());
            eventoExistente.setFecha(eventoActualizado.getFecha());
            eventoExistente.setLugar(eventoActualizado.getLugar());
            eventoExistente.setEtiqueta(eventoActualizado.getEtiqueta());
            eventoExistente.setPrecioticket(eventoActualizado.getPrecioticket());
            eventoExistente.setGenero(eventoActualizado.getGenero());

            eventoDao.registrarEvento(eventoExistente);
        }

    }


    @RequestMapping(value= "api/eventos", method = RequestMethod.POST)
    public void registrarEvento(@RequestBody Evento evento){

        eventoDao.registrarEvento(evento);
    }

    @RequestMapping(value= "api/eventos", method = RequestMethod.GET)
    public List<Evento> getEventos(@RequestHeader(value= "Authorization")String token){
        String[] tokens = token.split(";");
        String tokenf = tokens[0];
        String id = tokens[1];

        String usuarioid = jwtUtil.getKey(tokenf);

        if(usuarioid == null ){
            return new ArrayList<>();
        }

        return eventoDao.listarEventosporId(Long.parseLong(id));
    }

    @RequestMapping(value= "api/eventos/{idevento}", method = RequestMethod.DELETE)
    public void eliminarEvento( @PathVariable Long idevento){

        eventoDao.eliminarEvento(idevento);
    }
    @RequestMapping(value= "api/eventos/{idevento}", method = RequestMethod.GET)
    public ResponseEntity<Evento>obtenerEvento(@PathVariable long idevento){
        Evento evento = eventoDao.encontrarEvento(idevento);
        if (evento != null){
            return ResponseEntity.ok(evento);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value= "api/eventosClienteLogin/{genero}", method = RequestMethod.GET)
    public List<Evento> getEventosClientes(@PathVariable String genero){
        return eventoDao.listarEventos(genero);
    }

    @RequestMapping(value="api/eventosBusqueda/{nombre}", method = RequestMethod.GET)
    public List<Evento> getBusquedaEventoPorNombre(@PathVariable String nombre){
        return eventoDao.buscarEventoPorNombre(nombre);
    }
    @RequestMapping(value= "api/eventosModelo", method = RequestMethod.GET)
    public List<Evento> getEventos() {
        return eventoDao.getEventos();
    }

}
