import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service'; // Importa AuthService

// Define el decorador del componente, que especifica el selector, la plantilla, los estilos y los módulos importados.
@Component({
  selector: 'app-register', // Define el selector del componente.
  templateUrl: './register.component.html', // Define la ruta de la plantilla HTML del componente.
  styleUrls: ['./register.component.css'], // Define la ruta de los estilos CSS del componente.
  standalone: true, // Indica que el componente es independiente.
  imports: [FormsModule, CommonModule], // Importa los módulos necesarios para el componente.
})
// Define la clase del componente RegisterComponent.
export class RegisterComponent {
  // Define las propiedades del componente para almacenar el nombre de usuario, la contraseña, la confirmación de la contraseña y el mensaje de error.
  username: string = '';
  password: string = '';
  confirmPassword: string = '';
  errorMessage: string = '';

  // Constructor que inyecta el servicio AuthService y el Router.
  constructor(private authService: AuthService, private router: Router) {}

  // Método register que se ejecuta cuando el usuario intenta registrarse.
  register() {
    // Verifica si alguno de los campos está vacío y muestra un mensaje de error si es así.
    if (!this.username || !this.password || !this.confirmPassword) {
      this.errorMessage = 'All fields are required';
      return;
    }

    // Verifica si las contraseñas no coinciden y muestra un mensaje de error si es así.
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    // Llama al método register del servicio AuthService para registrar al usuario.
    this.authService
      .register({ username: this.username, password: this.password })
      .subscribe(
        (response) => {
          // Guarda el token de autenticación recibido en la respuesta.
          this.authService.saveToken(response.token); // Guarda el token
          // Redirige al usuario a la página de chat.
          this.router.navigate(['/chat']); // Redirige al chat
        },
        (error) => {
          this.errorMessage = 'Registration failed. Please try again.';
          console.error(error);
        }
      );
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
