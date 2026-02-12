import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import {
    IonHeader, IonToolbar, IonTitle, IonContent, IonButton, IonIcon,
    IonCard, IonCardHeader, IonCardTitle, IonCardContent, IonBadge,
    IonList, IonItem, IonLabel, IonCheckbox, IonButtons,
    ToastController, AlertController, LoadingController
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
    arrowBackOutline, createOutline, trashOutline, calendarOutline,
    timeOutline, personOutline, addCircleOutline, checkmarkCircleOutline
} from 'ionicons/icons';
import { TaskService } from '../../services/task.service';
import { SubtaskService } from '../../services/subtask.service';
import { TaskResponse, SubTaskResponse } from '../../models';

@Component({
    selector: 'app-task-detail',
    templateUrl: './task-detail.page.html',
    styleUrls: ['./task-detail.page.scss'],
    standalone: true,
    imports: [
        CommonModule,
        RouterLink,
        IonHeader, IonToolbar, IonTitle, IonContent, IonButton, IonIcon,
        IonCard, IonCardHeader, IonCardTitle, IonCardContent, IonBadge,
        IonList, IonItem, IonLabel, IonCheckbox, IonButtons
    ]
})
export class TaskDetailPage implements OnInit {
    task?: TaskResponse;
    subtasks: SubTaskResponse[] = [];

    constructor(
        private taskService: TaskService,
        private subtaskService: SubtaskService,
        private router: Router,
        private route: ActivatedRoute,
        private toastController: ToastController,
        private alertController: AlertController,
        private loadingController: LoadingController
    ) {
        addIcons({
            arrowBackOutline, createOutline, trashOutline, calendarOutline,
            timeOutline, personOutline, addCircleOutline, checkmarkCircleOutline
        });
    }

    ngOnInit() {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.loadTask(parseInt(id));
            this.loadSubtasks(parseInt(id));
        }
    }

    loadTask(id: number) {
        this.taskService.getById(id).subscribe({
            next: (task) => {
                this.task = task;
            },
            error: (error) => {
                this.showToast(error.message || 'Error al cargar la tarea', 'danger');
                this.router.navigate(['/tasks']);
            }
        });
    }

    loadSubtasks(taskId: number) {
        this.subtaskService.getByTaskId(taskId).subscribe({
            next: (subtasks) => {
                this.subtasks = subtasks;
            },
            error: (error) => {
                this.showToast(error.message || 'Error al cargar subtareas', 'danger');
            }
        });
    }

    getPriorityColor(priority: string): string {
        const colors = {
            'HIGH': 'danger',
            'MEDIUM': 'warning',
            'LOW': 'success'
        };
        return colors[priority as keyof typeof colors] || 'medium';
    }

    getStatusColor(status: string): string {
        const colors = {
            'PENDING': 'medium',
            'IN_PROGRESS': 'primary',
            'COMPLETED': 'success'
        };
        return colors[status as keyof typeof colors] || 'medium';
    }

    getStatusLabel(status: string): string {
        const labels = {
            'PENDING': 'Pendiente',
            'IN_PROGRESS': 'En Progreso',
            'COMPLETED': 'Completada'
        };
        return labels[status as keyof typeof labels] || status;
    }

    async deleteTask() {
        const alert = await this.alertController.create({
            header: 'Eliminar Tarea',
            message: '¿Estás seguro que deseas eliminar esta tarea? Esta acción no se puede deshacer.',
            buttons: [
                { text: 'Cancelar', role: 'cancel' },
                {
                    text: 'Eliminar',
                    role: 'destructive',
                    handler: async () => {
                        if (!this.task) return;

                        const loading = await this.loadingController.create({
                            message: 'Eliminando tarea...'
                        });
                        await loading.present();

                        this.taskService.delete(this.task.id).subscribe({
                            next: async () => {
                                await loading.dismiss();
                                this.showToast('Tarea eliminada exitosamente', 'success');
                                this.router.navigate(['/tasks']);
                            },
                            error: async (error) => {
                                await loading.dismiss();
                                this.showToast(error.message || 'Error al eliminar la tarea', 'danger');
                            }
                        });
                    }
                }
            ]
        });
        await alert.present();
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

export default TaskDetailPage;
