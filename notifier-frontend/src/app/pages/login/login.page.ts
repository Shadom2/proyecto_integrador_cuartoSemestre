import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import {
    IonContent, IonCard, IonCardHeader, IonCardTitle, IonCardContent,
    IonItem, IonLabel, IonInput, IonButton, IonIcon, IonText,
    IonSpinner, ToastController, LoadingController
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { mailOutline, lockClosedOutline, logInOutline } from 'ionicons/icons';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.page.html',
    styleUrls: ['./login.page.scss'],
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        RouterLink,
        IonContent, IonCard, IonCardHeader, IonCardTitle, IonCardContent,
        IonItem, IonLabel, IonInput, IonButton, IonIcon, IonText, IonSpinner
    ]
})
export class LoginPage {
    loginForm: FormGroup;
    isLoading = false;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private toastController: ToastController,
        private loadingController: LoadingController
    ) {
        addIcons({ mailOutline, lockClosedOutline, logInOutline });

        this.loginForm = this.fb.group({
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    async onLogin() {
        if (this.loginForm.invalid) {
            this.showToast('Por favor completa todos los campos correctamente', 'warning');
            return;
        }

        const loading = await this.loadingController.create({
            message: 'Iniciando sesión...',
            spinner: 'crescent'
        });
        await loading.present();

        const { email, password } = this.loginForm.value;

        this.authService.login(email, password).subscribe({
            next: async () => {
                await loading.dismiss();
                this.showToast('¡Bienvenido!', 'success');
                this.router.navigate(['/tasks']);
            },
            error: async (error) => {
                await loading.dismiss();
                this.showToast(error.message || 'Error al iniciar sesión', 'danger');
            }
        });
    }

    async showToast(message: string, color: string) {
        const toast = await this.toastController.create({
            message,
            duration: 3000,
            position: 'top',
            color
        });
        toast.present();
    }
}

export default LoginPage;
