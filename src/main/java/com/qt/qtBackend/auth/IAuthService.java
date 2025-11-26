package com.qt.qtBackend.auth;


import com.qt.qtBackend.auth.dto.AuthResponse;
import com.qt.qtBackend.auth.dto.LoginRequest;
import com.qt.qtBackend.auth.dto.RegisterAdminRequest;
import com.qt.qtBackend.auth.dto.RegistrarAgenteRequest;
import com.qt.qtBackend.dto.base.ObjectResponse;

public interface IAuthService {
    ObjectResponse<AuthResponse> login(LoginRequest request);
    ObjectResponse<AuthResponse> registerAdmin(RegisterAdminRequest request);
    ObjectResponse<AuthResponse> registerAgente(RegistrarAgenteRequest request);
}
