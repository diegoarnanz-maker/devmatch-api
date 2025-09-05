package com.devmatch.api.projectmessage.application.eventhandler;

import com.devmatch.api.projectmessage.application.port.out.MessageReadRepository;
import com.devmatch.api.projectmessage.domain.event.ProjectMessageReadEvent;
import com.devmatch.api.projectmessage.domain.model.MessageRead;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manejador de eventos para lecturas de mensajes.
 * Persiste automáticamente las lecturas en la base de datos.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MessageReadEventHandler {
    
    private final MessageReadRepository messageReadRepository;
    
    /**
     * Maneja el evento de mensaje leído.
     * Persiste la lectura en la base de datos.
     */
    @EventListener
    @Transactional
    public void handleMessageReadEvent(ProjectMessageReadEvent event) {
        log.info("Procesando evento de mensaje leído - ID: {}, Proyecto: {}, Lector: {}", 
                event.getMessageId(), event.getProjectId(), event.getReaderId());
        
        try {
            // Verificar si ya existe la lectura para evitar duplicados
            if (messageReadRepository.existsByMessageIdAndUserId(event.getMessageId(), event.getReaderId())) {
                log.debug("El mensaje {} ya fue marcado como leído por el usuario {}", 
                         event.getMessageId(), event.getReaderId());
                return;
            }
            
            // Crear nuevo registro de lectura
            MessageRead messageRead = new MessageRead(event.getMessageId(), event.getReaderId());
            
            // Guardar en la base de datos
            messageReadRepository.save(messageRead);
            
            log.info("Lectura del mensaje {} por usuario {} persistida exitosamente", 
                    event.getMessageId(), event.getReaderId());
            
        } catch (Exception e) {
            log.error("Error al persistir lectura del mensaje {} por usuario {}: {}", 
                     event.getMessageId(), event.getReaderId(), e.getMessage(), e);
            
            // No relanzamos la excepción para no romper el flujo principal
            // La lectura se puede intentar nuevamente manualmente
        }
    }
}
