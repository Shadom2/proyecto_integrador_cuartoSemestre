import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { TaskRequest, TaskResponse } from '../models';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class TaskService {
    private apiUrl = `${environment.apiUrl}/api/tasks`;

    constructor(private http: HttpClient) { }

    /**
     * Obtiene todas las tareas
     */
    getAll(): Observable<TaskResponse[]> {
        return this.http.get<TaskResponse[]>(this.apiUrl)
            .pipe(catchError(this.handleError));
    }

    /**
     * Obtiene una tarea por ID
     */
    getById(id: number): Observable<TaskResponse> {
        return this.http.get<TaskResponse>(`${this.apiUrl}/${id}`)
            .pipe(catchError(this.handleError));
    }

    /**
     * Obtiene todas las tareas de un estudiante
     */
    getByStudentId(studentId: number): Observable<TaskResponse[]> {
        return this.http.get<TaskResponse[]>(`${this.apiUrl}/student/${studentId}`)
            .pipe(catchError(this.handleError));
    }

    /**
     * Crea una nueva tarea
     */
    create(task: TaskRequest): Observable<TaskResponse> {
        return this.http.post<TaskResponse>(this.apiUrl, task)
            .pipe(catchError(this.handleError));
    }

    /**
     * Actualiza una tarea existente
     */
    update(id: number, task: TaskRequest): Observable<TaskResponse> {
        return this.http.put<TaskResponse>(`${this.apiUrl}/${id}`, task)
            .pipe(catchError(this.handleError));
    }

    /**
     * Elimina una tarea
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
            errorMessage = 'Tarea no encontrada';
        } else if (error.message) {
            errorMessage = error.message;
        }

        return throwError(() => ({ status: error.status, message: errorMessage }));
    }
}
