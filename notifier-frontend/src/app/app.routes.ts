import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.page').then(m => m.LoginPage)
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register.page').then(m => m.RegisterPage)
  },
  {
    path: 'tasks',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/tasks/tasks.page').then(m => m.TasksPage)
  },
  {
    path: 'tasks/new',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/task-form/task-form.page').then(m => m.TaskFormPage)
  },
  {
    path: 'tasks/edit/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/task-form/task-form.page').then(m => m.TaskFormPage)
  },
  {
    path: 'tasks/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/task-detail/task-detail.page').then(m => m.TaskDetailPage)
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
