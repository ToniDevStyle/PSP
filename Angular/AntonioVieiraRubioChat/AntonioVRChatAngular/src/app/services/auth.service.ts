import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

// Marca la clase AuthService como inyectable y disponible en el nivel raíz del módulo.
@Injectable({
  providedIn: 'root'
})
// Define la clase AuthService.
export class AuthService {
  // Define la URL base de la API de autenticación.
  private API_URL = 'http://localhost:8080/auth'; // Ajusta según tu backend

  // Constructor que inyecta el servicio HttpClient.
  constructor(private http: HttpClient) {}

  // Método auxiliar para obtener los encabezados de autenticación con el token.
  private getAuthHeaders() {
    // Obtiene el token del almacenamiento local.
    const token = localStorage.getItem('token');
    // Si hay un token, retorna los encabezados con el token de autorización.
    return token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
  }

  // Método de inicio de sesión.
  login(credentials: { username: string; password: string }): Observable<any> {
    // Envía una solicitud POST a la API de inicio de sesión con las credenciales del usuario.
    return this.http.post(`${this.API_URL}/login`, credentials, {
      responseType: 'text' // Asegura que la respuesta se trate como texto plano (token JWT)
    });
  }

  // Método de registro.
  register(data: { username: string; password: string }): Observable<any> {
    // Envía una solicitud POST a la API de registro con los datos del usuario.
    return this.http.post(`${this.API_URL}/register`, data);
  }

  // Verifica si el usuario está autenticado (basado en la presencia del token en el almacenamiento local).
  isAuthenticated(): boolean {
    // Retorna true si hay un token en el almacenamiento local, de lo contrario, false.
    return !!localStorage.getItem('token');
  }

  // Guarda el token JWT en el almacenamiento local.
  saveToken(token: string): void {
    // Almacena el token en el almacenamiento local.
    localStorage.setItem('token', token);
  }

  // Método de cierre de sesión (elimina el token del almacenamiento local).
  logout(): void {
    // Elimina el token del almacenamiento local.
    localStorage.removeItem('token');
  }

  // Method to make requests with authentication token in headers
  getUserData(): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.API_URL}/user`, { headers });
  }
}
