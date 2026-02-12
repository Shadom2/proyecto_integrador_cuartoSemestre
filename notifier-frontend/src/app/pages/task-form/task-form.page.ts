import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import {
    IonHeader, IonToolbar, IonTitle, IonContent, IonButton, IonIcon,
    IonItem, IonLabel, IonInput, IonTextarea, IonSelect, IonSelectOption,
    IonDatetime, IonDatetimeButton, IonModal, IonButtons,
    ToastController, LoadingController
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { saveOutline, arrowBackOutline, calendarOutline } from 'ionicons/icons';
import { TaskService } from '../../services/task.service';
import { AuthService } from '../../services/auth.service';
import { Priority, Status, TaskRequest } from '../../models';

@Component({
    selector: 'app-task-form',
    templateUrl: './task-form.page.html',
    styleUrls: ['./task-form.page.scss'],
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        IonHeader, IonToolbar, IonTitle, IonContent, IonButton, IonIcon,
        IonItem, IonLabel, IonInput, IonTextarea, IonSelect, IonSelectOption,
        IonDatetime, IonModal, IonButtons
    ]
})
export class TaskFormPage implements OnInit {
    taskForm: FormGroup;
    isEditMode = false;
    taskId?: number;
    currentUserId?: number;

    constructor(
        private fb: FormBuilder,
        private taskService: TaskService,
        private authService: AuthService,
        private router: Router,
        private route: ActivatedRoute,
        private toastController: ToastController,
        private loadingController: LoadingController
    ) {
        addIcons({ saveOutline, arrowBackOutline, calendarOutline });

        this.taskForm = this.fb.group({
            title: ['', [Validators.required, Validators.maxLength(100)]],
            description: [''],
            dueDate: ['', Validators.required],
            priority: ['MEDIUM' as Priority, Validators.required],
            status: ['PENDING' as Status]
        });
    }

    // Getter para la fecha mínima (hoy)
    get minDate(): string {
        return new Date().toISOString();
    }

    async ngOnInit() {
        const user = await this.authService.getCurrentUser();
        this.currentUserId = user?.id;

        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.isEditMode = true;
            this.taskId = parseInt(id);
            this.loadTask(this.taskId);
        }
    }

    loadTask(id: number) {
        this.taskService.getById(id).subscribe({
            next: (task) => {
                this.taskForm.patchValue({
                    title: task.title,
                    description: task.description,
                    dueDate: task.dueDate,
                    priority: task.priority,
                    status: task.status
                });
            },
            error: (error) => {
                this.showToast(error.message || 'Error al cargar la tarea', 'danger');
                this.router.navigate(['/tasks']);
            }
        });
    }

    async onSubmit() {
        if (this.taskForm.invalid || !this.currentUserId) {
            this.showToast('Por favor completa todos los campos requeridos', 'warning');
            return;
        }

        const loading = await this.loadingController.create({
            message: this.isEditMode ? 'Actualizando tarea...' : 'Creando tarea...',
            spinner: 'crescent'
        });
        await loading.present();

        const taskData: TaskRequest = {
            ...this.taskForm.value,
            studentId: this.currentUserId
        };

        const operation = this.isEditMode && this.taskId
            ? this.taskService.update(this.taskId, taskData)
            : this.taskService.create(taskData);

        operation.subscribe({
            next: async () => {
                await loading.dismiss();
                this.showToast(
                    this.isEditMode ? 'Tarea actualizada exitosamente' : 'Tarea creada exitosamente',
                    'success'
                );
                this.router.navigate(['/tasks']);
            },
            error: async (error) => {
                await loading.dismiss();
                this.showToast(error.message || 'Error al guardar la tarea', 'danger');
            }
        });
    }

    goBack() {
        this.router.navigate(['/tasks']);
    }

    async showToast(message: string, color: string) {
        const toast = await this.toastController.create({
            message,
            duration: 2000,
            position: 'top',
            color
        });
        toast.present();
    }
}

export default TaskFormPage;
