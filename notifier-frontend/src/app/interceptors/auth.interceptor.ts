import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { from, throwError } from 'rxjs';
import { switchMap, catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

/**
 * Interceptor funcional que agrega el token JWT a las peticiones HTTP
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    // Excluir rutas de autenticación (no necesitan token)
    if (req.url.includes('/auth/')) {
        return next(req);
    }

    // Agregar token JWT a las peticiones
    return from(authService.getToken()).pipe(
        switchMap(token => {
            if (token) {
                req = req.clone({
                    setHeaders: {
                        Authorization: `Bearer ${token}`
                    }
                });
            }
            return next(req);
        }),
        catchError((error: HttpErrorResponse) => {
            // Si el token expiró (401), hacer logout automático
            if (error.status === 401) {
                authService.logout();
                router.navigate(['/login']);
            }
            return throwError(() => error);
        })
    );
};
