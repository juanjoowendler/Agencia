package com.agencia.microservicio_pruebas.repositories;
import com.agencia.microservicio_pruebas.entities.Notificacion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends CrudRepository<Notificacion, Long> {
}