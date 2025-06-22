package com.devmatch.api.user.domain.service;

// import com.devmatch.api.user.domain.model.User;
import org.springframework.stereotype.Service;

/**
 * Servicio de dominio que encapsula reglas de negocio y lógica que no pertenece naturalmente
 * a la entidad User. Este servicio opera exclusivamente con objetos del dominio y no tiene
 * dependencias con la infraestructura.
 */
@Service
public class UserDomainService {

    // /**
    //  * Valida si un usuario puede ser eliminado basado en reglas de negocio del dominio.
    //  * @param user Usuario a validar
    //  * @return true si el usuario puede ser eliminado
    //  */
    // public boolean canUserBeDeleted(User user) {
    //     return !user.isAdmin() && user.isActive();
    // }

    // /**
    //  * Valida si un usuario puede ser desactivado basado en reglas de negocio del dominio.
    //  * @param user Usuario a validar
    //  * @return true si el usuario puede ser desactivado
    //  */
    // public boolean canUserBeDeactivated(User user) {
    //     return user.isActive() && !user.isAdmin();
    // }

    // /**
    //  * Valida si un usuario puede ser reactivado basado en reglas de negocio del dominio.
    //  * @param user Usuario a validar
    //  * @return true si el usuario puede ser reactivado
    //  */
    // public boolean canUserBeReactivated(User user) {
    //     return !user.isActive() && !user.isDeleted();
    // }
} 