import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import {
    IonContent, IonCard, IonCardHeader, IonCardTitle, IonCardContent,
    IonItem, IonLabel, IonInput, IonButton, IonIcon, IonText,
    ToastController, LoadingController
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { personOutline, mailOutline, lockClosedOutline, createOutline } from 'ionicons/icons';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-register',
    templateUrl: './register.page.html',
    styleUrls: ['./register.page.scss'],
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        RouterLink,
        IonContent, IonCard, IonCardHeader, IonCardTitle, IonCardContent,
        IonItem, IonLabel, IonInput, IonButton, IonIcon, IonText
    ]
})
export class RegisterPage {
    registerForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private toastController: ToastController,
        private loadingController: LoadingController
    ) {
        addIcons({ personOutline, mailOutline, lockClosedOutline, createOutline });

        this.registerForm = this.fb.group({
            firstName: ['', [Validators.required, Validators.maxLength(50)]],
            lastName: ['', [Validators.required, Validators.maxLength(50)]],
            email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
            password: ['', [Validators.required, Validators.minLength(6)]],
            confirmPassword: ['', [Validators.required]]
        }, { validators: this.passwordMatchValidator });
    }

    passwordMatchValidator(control: AbstractControl): { [key: string]: boolean } | null {
        const password = control.get('password');
        const confirmPassword = control.get('confirmPassword');

        if (!password || !confirmPassword) {
            return null;
        }

        return password.value === confirmPassword.value ? null : { passwordMismatch: true };
    }

    async onRegister() {
        if (this.registerForm.invalid) {
            this.showToast('Por favor completa todos los campos correctamente', 'warning');
            return;
        }

        const loading = await this.loadingController.create({
            message: 'Creando cuenta...',
            spinner: 'crescent'
        });
        await loading.present();

        const { firstName, lastName, email, password } = this.registerForm.value;

        this.authService.register({ firstName, lastName, email, password }).subscribe({
            next: async () => {
                await loading.dismiss();
                this.showToast('¡Cuenta creada exitosamente!', 'success');
                this.router.navigate(['/tasks']);
            },
            error: async (error) => {
                await loading.dismiss();
                this.showToast(error.message || 'Error al crear la cuenta', 'danger');
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

export default RegisterPage;
