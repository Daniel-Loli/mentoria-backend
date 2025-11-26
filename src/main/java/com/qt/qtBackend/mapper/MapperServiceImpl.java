package com.qt.qtBackend.mapper;

import com.qt.qtBackend.auth.dto.AlumnoAuthResponse;
import com.qt.qtBackend.auth.dto.DocenteAuthResponse;
import com.qt.qtBackend.auth.dto.InstitucionAuthResponse;
import com.qt.qtBackend.dto.agente.HistorialItemDto;
import com.qt.qtBackend.dto.alumno.AlumnoAllResponse;
import com.qt.qtBackend.dto.alumnoInstitucion.AInstitucionAllResponse;
import com.qt.qtBackend.dto.alumnoInstitucion.AlumnoIAllResponse;
import com.qt.qtBackend.dto.alumnoInstitucion.AlumnoInstitucionAllResponse;
import com.qt.qtBackend.dto.asignacion.AsignacionAllResponse;
import com.qt.qtBackend.dto.asignacion.AsignacionEvidenciaResponse;
import com.qt.qtBackend.dto.asignacion.AsignacionFilterAShortResponse;
import com.qt.qtBackend.dto.asignatura.AsignaturaAllResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionAllResponse;
import com.qt.qtBackend.dto.conversacion.ConversacionShortResponse;
import com.qt.qtBackend.dto.docente.DocenteAllResponse;
import com.qt.qtBackend.dto.docenteInstitucion.DInstitucionAllResponse;
import com.qt.qtBackend.dto.docenteInstitucion.DocenteIAllResponse;
import com.qt.qtBackend.dto.docenteInstitucion.DocenteInstitucionAllResponse;
import com.qt.qtBackend.dto.institucion.InstitucionAllResponse;
import com.qt.qtBackend.dto.materialAcademico.MaterialAcademicoAllResponse;
import com.qt.qtBackend.dto.materialAcademico.MaterialAcademicoShortResponse;
import com.qt.qtBackend.dto.matricula.MatriculaAllResponse;
import com.qt.qtBackend.dto.matricula.MatriculaShortResponse;
import com.qt.qtBackend.dto.mensaje.MensajeShortResponse;
import com.qt.qtBackend.dto.mision.MisionAllResponse;
import com.qt.qtBackend.dto.mision.MisionIAllResponse;
import com.qt.qtBackend.dto.misionAsignatura.MAsignaturaShortResponse;
import com.qt.qtBackend.dto.misionAsignatura.MisionAsignaturaShortResponse;
import com.qt.qtBackend.dto.unidad.UnidadAllResponse;
import com.qt.qtBackend.dto.unidad.UnidadShortResponse;
import com.qt.qtBackend.dto.unidadAsignatura.UnidadAsignaturaAllResponse;
import com.qt.qtBackend.dto.resultado.ResultadoAllResponse;
import com.qt.qtBackend.dto.rol.RolAllResponse;
import com.qt.qtBackend.dto.unidadAsignatura.UnidadAsignaturaShortResponse;
import com.qt.qtBackend.dto.usuario.UsuarioAllResponse;
import com.qt.qtBackend.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapperServiceImpl
    implements IMapperService {

    private final ModelMapper modelMapper;

    @Override
    public RolAllResponse convRolAll(Rol obj) {
        return modelMapper.map(obj,RolAllResponse.class);
    }

    public UsuarioAllResponse convUsuarioAll(Usuario obj) {
        return modelMapper.map(obj, UsuarioAllResponse.class);
    }

    @Override
    public InstitucionAllResponse convInstitucionAll(Institucion obj) {
        return modelMapper.map(obj, InstitucionAllResponse.class);
    }

    @Override
    public InstitucionAuthResponse convInstitucionAuth(Institucion obj) {
        return modelMapper.map(obj, InstitucionAuthResponse.class);
    }

    @Override
    public DocenteAllResponse convDocenteAll(Docente obj) {
        return modelMapper.map(obj, DocenteAllResponse.class);
    }

    @Override
    public DocenteAuthResponse convDocenteAuth(Docente obj) {
        return modelMapper.map(obj, DocenteAuthResponse.class);
    }


    @Override
    public AsignaturaAllResponse convAsignaturaAll(Asignatura obj) {
        return modelMapper.map(obj,AsignaturaAllResponse.class);
    }

    @Override
    public DocenteInstitucionAllResponse convDocenteInstitucionAll(DocenteInstitucion obj) {
        return modelMapper.map(obj,DocenteInstitucionAllResponse.class);
    }

    @Override
    public DInstitucionAllResponse convDInstitucionAll(DocenteInstitucion obj) {
        return modelMapper.map(obj,DInstitucionAllResponse.class);
    }

    @Override
    public DocenteIAllResponse convDocenteIAll(DocenteInstitucion obj) {
        return modelMapper.map(obj,DocenteIAllResponse.class);
    }

    @Override
    public AlumnoAllResponse convAlumnoAll(Alumno obj) {
        return modelMapper.map(obj,AlumnoAllResponse.class);
    }

    @Override
    public AlumnoAuthResponse convAlumnoAuth(Alumno obj) {
        return modelMapper.map(obj,AlumnoAuthResponse.class);
    }

    @Override
    public AlumnoInstitucionAllResponse convAlumnoInstitucionAll(AlumnoInstitucion obj) {
        return modelMapper.map(obj, AlumnoInstitucionAllResponse.class);
    }
    @Override
    public AInstitucionAllResponse convAInstitucionAll(AlumnoInstitucion obj) {
        return modelMapper.map(obj, AInstitucionAllResponse.class);
    }
    @Override
    public AlumnoIAllResponse convAlumnoIAll(AlumnoInstitucion obj) {
        return modelMapper.map(obj, AlumnoIAllResponse.class);
    }

    @Override
    public MisionAllResponse convMisionAll(Mision obj) {
        return modelMapper.map(obj,MisionAllResponse.class);
    }

    @Override
    public MisionIAllResponse convMisionIAll(Mision obj) {
        return modelMapper.map(obj,MisionIAllResponse.class);
    }

    @Override
    public ConversacionAllResponse convConversacionAll(Conversacion obj) {
        return modelMapper.map(obj,ConversacionAllResponse.class);
    }

    @Override
    public ConversacionShortResponse convConversacionShort(Conversacion obj) {
        return modelMapper.map(obj,ConversacionShortResponse.class);
    }

    @Override
    public MensajeShortResponse convMensajeShort(Mensaje obj) {
        return modelMapper.map(obj,MensajeShortResponse.class);
    }

    @Override
    public MisionAsignaturaShortResponse convMisionAsignaturaShort(MisionAsignatura obj) {
        return modelMapper.map(obj,MisionAsignaturaShortResponse.class);
    }

    @Override
    public MAsignaturaShortResponse convMAsignaturaShort(MisionAsignatura obj) {
        return modelMapper.map(obj,MAsignaturaShortResponse.class);
    }


    @Override
    public AsignacionAllResponse convAsignacionAll(Asignacion obj) {
        return modelMapper.map(obj,AsignacionAllResponse.class);
    }

    @Override
    public AsignacionEvidenciaResponse convAsignacionEvidencia(Asignacion obj) {
        return modelMapper.map(obj,AsignacionEvidenciaResponse.class);
    }

    @Override
    public AsignacionFilterAShortResponse convAsignacionFilterAShort(Asignacion obj) {
        return modelMapper.map(obj,AsignacionFilterAShortResponse.class);
    }

    @Override
    public MatriculaAllResponse convMatriculaAll(Matricula obj) {
        return modelMapper.map(obj,MatriculaAllResponse.class);
    }

    @Override
    public MatriculaShortResponse convMatriculaShort(Matricula obj) {
        return modelMapper.map(obj,MatriculaShortResponse.class);
    }

    @Override
    public ResultadoAllResponse convResultadoAll(Resultado obj) {
        return modelMapper.map(obj,ResultadoAllResponse.class);
    }

    @Override
    public UnidadAllResponse convUnidadAll(Unidad obj) {
        return modelMapper.map(obj, UnidadAllResponse.class);
    }

    @Override
    public UnidadShortResponse convUnidadShort(Unidad obj) {
        return modelMapper.map(obj, UnidadShortResponse.class);
    }

    @Override
    public UnidadAsignaturaAllResponse convUnidadAsignaturaAll(UnidadAsignatura obj) {
        return modelMapper.map(obj, UnidadAsignaturaAllResponse.class);
    }

    @Override
    public UnidadAsignaturaShortResponse convUnidadAsignaturaShort(UnidadAsignatura obj) {
        return modelMapper.map(obj, UnidadAsignaturaShortResponse.class);
    }

    @Override
    public MaterialAcademicoAllResponse convMaterialAcademicoAll(MaterialAcademico obj) {
        return modelMapper.map(obj, MaterialAcademicoAllResponse.class);
    }

    @Override
    public MaterialAcademicoShortResponse convMaterialAcademicoShort(MaterialAcademico obj) {
        return modelMapper.map(obj, MaterialAcademicoShortResponse.class);
    }

    @Override
    public HistorialItemDto convHistorialItem(MensajeShortResponse obj) {
        return modelMapper.map(obj, HistorialItemDto.class);
    }


}
