import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Storage } from '@ionic/storage-angular';
import { AuthResponse, LoginRequest, RegisterRequest, StudentResponse } from '../models';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly TOKEN_KEY = 'auth_token';
    private readonly USER_KEY = 'current_user';
    private authSubject = new BehaviorSubject<boolean>(false);
    public isAuthenticated$ = this.authSubject.asObservable();

    constructor(
        private http: HttpClient,
        private storage: Storage
    ) {
        this.initStorage();
    }

    async initStorage() {
        await this.storage.create();
        const token = await this.getToken();
        this.authSubject.next(!!token);
    }

    /**
     * Registra un nuevo estudiante
     */
    register(data: RegisterRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, data)
            .pipe(
                tap(response => this.saveAuthData(response)),
                catchError(this.handleError)
            );
    }

    /**
     * Inicia sesión con credenciales
     */
    login(email: string, password: string): Observable<AuthResponse> {
        const loginData: LoginRequest = { email, password };
        return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, loginData)
            .pipe(
                tap(response => this.saveAuthData(response)),
                catchError(this.handleError)
            );
    }

    /**
     * Cierra la sesión del usuario
     */
    async logout(): Promise<void> {
        await this.storage.remove(this.TOKEN_KEY);
        await this.storage.remove(this.USER_KEY);
        this.authSubject.next(false);
    }

    /**
     * Obtiene el token JWT almacenado
     */
    async getToken(): Promise<string | null> {
        return await this.storage.get(this.TOKEN_KEY);
    }

    /**
     * Obtiene el usuario actual
     */
    async getCurrentUser(): Promise<StudentResponse | null> {
        return await this.storage.get(this.USER_KEY);
    }

    /**
     * Verifica si el usuario está autenticado
     */
    async isAuthenticated(): Promise<boolean> {
        const token = await this.getToken();
        return !!token;
    }

    /**
     * Guarda los datos de autenticación (token + usuario)
     */
    private async saveAuthData(authResponse: AuthResponse): Promise<void> {
        await this.storage.set(this.TOKEN_KEY, authResponse.token);
        await this.storage.set(this.USER_KEY, authResponse.student);
        this.authSubject.next(true);
    }

    /**
     * Manejo de errores HTTP
     */
    private handleError(error: HttpErrorResponse): Observable<never> {
        let errorMessage = 'Ha ocurrido un error';

        if (error.error && error.error.error) {
            errorMessage = error.error.error;
        } else if (error.status === 0) {
            errorMessage = 'No se pudo conectar con el servidor';
        } else if (error.status === 401) {
            errorMessage = 'Credenciales inválidas';
        } else if (error.status === 409) {
            errorMessage = 'El email ya está registrado';
        } else if (error.message) {
            errorMessage = error.message;
        }

        return throwError(() => ({ status: error.status, message: errorMessage }));
    }
}
