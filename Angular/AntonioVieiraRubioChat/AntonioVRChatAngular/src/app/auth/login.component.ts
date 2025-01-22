import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router'; // Para redirigir después de un login exitoso
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,  // Indica que este componente es autónomo
  imports: [FormsModule, RouterModule]  // Necesario para usar ngModel
})
export class LoginComponent {
  username: string = '';  // Variable para el nombre de usuario
  password: string = '';  // Variable para la contraseña
  errorMessage: string = '';  // Variable para mostrar el mensaje de error

  constructor(private authService: AuthService, private router: Router) {}

  // Método que se ejecuta cuando el usuario envía el formulario
  login(): void {
    const credentials = {
      username: this.username,
      password: this.password
    };

    // Llamada al servicio para hacer login
    this.authService.login(credentials).subscribe({
      next: (token) => {
        console.log('Login exitoso', token);
        // Guarda el token en el almacenamiento local o en donde lo necesites
        this.authService.saveToken(token);
        // Redirige al usuario a la página del dashboard (o la ruta que elijas)
        this.router.navigate(['/chat']); // Cambia '/dashboard' por la ruta de los chats si es necesario
      },
      error: (err) => {
        console.error('Error al hacer login', err);
        // Si las credenciales son incorrectas, muestra un mensaje de error
        this.errorMessage = 'Credenciales inválidas, por favor intenta de nuevo.';
      }
    });
  }
}
