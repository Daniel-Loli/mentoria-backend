package com.qt.qtBackend.security;

import com.qt.qtBackend.Enum.RolEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Access {

    public boolean isUgel() {
        return hasRole(RolEnum.UGEL.name());
    }
    public boolean isInstitucion() {return hasRole(RolEnum.INSTITUCION.name());}
    public boolean isDocente() {return hasRole(RolEnum.DOCENTE.name());}
    public boolean isAlumno() {return hasRole(RolEnum.ALUMNO.name());}
    public boolean isAgente() {return hasRole(RolEnum.AGENTE.name());}

    public boolean isAll(){
        return isUgel() || isInstitucion() || isDocente() || isAlumno() || isAgente();
    }


    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }
}
