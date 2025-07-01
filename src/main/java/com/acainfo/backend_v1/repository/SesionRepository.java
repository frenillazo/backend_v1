package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SesionRepository extends JpaRepository<Sesion, Long> { }