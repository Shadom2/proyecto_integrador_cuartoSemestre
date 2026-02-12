import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import {
    IonHeader, IonToolbar, IonTitle, IonContent, IonButton, IonIcon, IonText,
    IonSegment, IonSegmentButton, IonLabel, IonCard, IonCardHeader,
    IonCardTitle, IonCardContent, IonBadge, IonFab, IonFabButton,
    IonRefresher, IonRefresherContent, ToastController, AlertController
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { add, logOutOutline, calendarOutline, alertCircleOutline, checkmarkCircleOutline } from 'ionicons/icons';
import { TaskService } from '../../services/task.service';
import { AuthService } from '../../services/auth.service';
import { TaskResponse, StudentResponse, Status } from '../../models';

@Component({
    selector: 'app-tasks',
    templateUrl: './tasks.page.html',
    styleUrls: ['./tasks.page.scss'],
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        RouterLink,
        IonHeader, IonToolbar, IonTitle, IonContent, IonButton, IonIcon, IonText,
        IonSegment, IonSegmentButton, IonLabel, IonCard, IonCardHeader,
        IonCardTitle, IonCardContent, IonBadge, IonFab, IonFabButton,
        IonRefresher, IonRefresherContent
    ]
})
export class TasksPage implements OnInit {
    currentUser: StudentResponse | null = null;
    tasks: TaskResponse[] = [];
    filteredTasks: TaskResponse[] = [];
    selectedFilter: Status | 'ALL' = 'ALL';

    constructor(
        private taskService: TaskService,
        private authService: AuthService,
        private router: Router,
        private toastController: ToastController,
        private alertController: AlertController
    ) {
        addIcons({ add, logOutOutline, calendarOutline, alertCircleOutline, checkmarkCircleOutline });
    }

    async ngOnInit() {
        this.currentUser = await this.authService.getCurrentUser();
    }

    // Se ejecuta cada vez que la página se vuelve activa
    async ionViewWillEnter() {
        // Recargar usuario desde storage (por si se hizo hard reload)
        if (!this.currentUser) {
            this.currentUser = await this.authService.getCurrentUser();
        }
        this.loadTasks();
    }

    loadTasks(event?: any) {
        if (this.currentUser) {
            this.taskService.getByStudentId(this.currentUser.id).subscribe({
                next: (tasks) => {
                    this.tasks = tasks;
                    this.filterTasks();
                    if (event) event.target.complete();
                },
                error: (error) => {
                    this.showToast(error.message || 'Error al cargar tareas', 'danger');
                    if (event) event.target.complete();
                }
            });
        }
    }

    filterTasks() {
        // Primero filtrar por estado
        let filtered = this.selectedFilter === 'ALL'
            ? this.tasks
            : this.tasks.filter(task => task.status === this.selectedFilter);

        // Luego ordenar por prioridad: HIGH primero, luego MEDIUM, luego LOW
        const priorityOrder = { 'HIGH': 1, 'MEDIUM': 2, 'LOW': 3 };
        this.filteredTasks = filtered.sort((a, b) => {
            return (priorityOrder[a.priority as keyof typeof priorityOrder] || 999) -
                (priorityOrder[b.priority as keyof typeof priorityOrder] || 999);
        });
    }

    onFilterChange(event: any) {
        this.selectedFilter = event.detail.value;
        this.filterTasks();
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

    isTaskOverdue(dueDate: string): boolean {
        return new Date(dueDate) < new Date();
    }

    async logout() {
        const alert = await this.alertController.create({
            header: 'Cerrar Sesión',
            message: '¿Estás seguro que deseas cerrar sesión?',
            buttons: [
                { text: 'Cancelar', role: 'cancel' },
                {
                    text: 'Salir',
                    role: 'confirm',
                    handler: async () => {
                        await this.authService.logout();
                        this.router.navigate(['/login']);
                    }
                }
            ]
        });
        await alert.present();
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

export default TasksPage;
