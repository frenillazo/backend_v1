package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> { }
