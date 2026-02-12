import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SubTaskRequest, SubTaskResponse } from '../models';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class SubtaskService {
    private apiUrl = `${environment.apiUrl}/api/subtasks`;

    constructor(private http: HttpClient) { }

    /**
     * Obtiene todas las subtareas
     */
    getAll(): Observable<SubTaskResponse[]> {
        return this.http.get<SubTaskResponse[]>(this.apiUrl)
            .pipe(catchError(this.handleError));
    }

    /**
     * Obtiene una subtarea por ID
     */
    getById(id: number): Observable<SubTaskResponse> {
        return this.http.get<SubTaskResponse>(`${this.apiUrl}/${id}`)
            .pipe(catchError(this.handleError));
    }

    /**
     * Obtiene todas las subtareas de una tarea
     */
    getByTaskId(taskId: number): Observable<SubTaskResponse[]> {
        return this.http.get<SubTaskResponse[]>(`${this.apiUrl}/task/${taskId}`)
            .pipe(catchError(this.handleError));
    }

    /**
     * Crea una nueva subtarea
     */
    create(subtask: SubTaskRequest): Observable<SubTaskResponse> {
        return this.http.post<SubTaskResponse>(this.apiUrl, subtask)
            .pipe(catchError(this.handleError));
    }

    /**
     * Actualiza una subtarea existente
     */
    update(id: number, subtask: SubTaskRequest): Observable<SubTaskResponse> {
        return this.http.put<SubTaskResponse>(`${this.apiUrl}/${id}`, subtask)
            .pipe(catchError(this.handleError));
    }

    /**
     * Elimina una subtarea
     */
    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`)
            .pipe(catchError(this.handleError));
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
        } else if (error.status === 404) {
            errorMessage = 'Subtarea no encontrada';
        } else if (error.message) {
            errorMessage = error.message;
        }

        return throwError(() => ({ status: error.status, message: errorMessage }));
    }
}
