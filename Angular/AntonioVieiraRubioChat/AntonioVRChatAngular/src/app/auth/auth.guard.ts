import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service'; // Importa tu servicio de autenticación

// Marca la clase AuthGuard como inyectable y disponible en el nivel raíz del módulo.
@Injectable({
  providedIn: 'root',
})
// Define la clase AuthGuard que implementa la interfaz CanActivate.
export class AuthGuard implements CanActivate {
  // Constructor que inyecta el servicio AuthService y el Router.
  constructor(private authService: AuthService, private router: Router) {}

  // Método canActivate que se ejecuta cuando se intenta activar una ruta protegida.
  canActivate(): boolean {
    // Imprime un mensaje en la consola cuando se activa el guardia de autenticación.
    console.log('AuthGuard triggered. Current route:', this.router.url);

    // Verifica si el usuario está autenticado llamando al método isAuthenticated del servicio AuthService.
    if (this.authService.isAuthenticated()) {
      // Si el usuario está autenticado, permite la activación de la ruta.
      return true;
    }

    // Si el usuario no está autenticado, redirige al usuario a la página de inicio de sesión.
    this.router.navigate(['/login']);
    // Retorna false para evitar la activación de la ruta.
    return false;
  }
}