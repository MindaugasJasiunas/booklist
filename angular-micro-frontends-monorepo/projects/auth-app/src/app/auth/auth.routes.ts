import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

export const AUTH_ROUTES: Routes = [
  { component: LoginComponent, path: 'login' },
  { component: RegisterComponent, path: 'register' },
];
