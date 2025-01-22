import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login.component';
import { RegisterComponent } from './auth/register.component';
import { ChatComponent } from './pages/chat/chat.component';
import { AuthGuard } from './auth/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent},
  { path: 'chat', component: ChatComponent, canActivate: [AuthGuard] }, // Rutas protegidas
  { path: '', redirectTo: '/login', pathMatch: 'full' }, // Landing: redirige al login
  { path: '**', redirectTo: '/login' }, // Redirigir rutas no encontradas
];
