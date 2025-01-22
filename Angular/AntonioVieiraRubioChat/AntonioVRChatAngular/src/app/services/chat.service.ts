import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import SockJS from 'sockjs-client';
import { HttpClient } from '@angular/common/http';




// Marca la clase ChatService como inyectable y disponible en el nivel raíz del módulo.
@Injectable({
  providedIn: 'root'
})
// Define la clase ChatService.
export class ChatService {
  // Define una propiedad stompClient para manejar la conexión WebSocket.
  private stompClient!: Stomp.Client;
  // Define un Subject para manejar los mensajes entrantes.
  private messageSubject = new Subject<any>();
  // Define un BehaviorSubject para manejar el estado de la conexión.
  private isConnectedSubject = new BehaviorSubject<boolean>(false);
  // Define la URL base de la API de mensajes.
  private API_URL = 'http://localhost:8080/api/mensajes';

  // Constructor que inyecta el servicio HttpClient.
  constructor(private http: HttpClient) {}

  // Método para conectar al WebSocket.
  conectar() {
    // Verifica si ya está conectado.
    if (this.isConnectedSubject.value) {
      console.log('Ya estás conectado');
      return;
    }

    // Crea una nueva conexión SockJS.
    const socket = new SockJS('http://localhost:8080/chat-websocket');
    // Inicializa stompClient con la conexión SockJS.
    this.stompClient = Stomp.over(socket);

    // Conecta al WebSocket y maneja el frame de conexión.
    this.stompClient.connect({}, (frame) => {
      console.log('Conectado: ' + frame);
      // Actualiza el estado de la conexión a true.
      this.isConnectedSubject.next(true);
    });
  }

  // Método para desconectar del WebSocket.
  desconectar() {
    // Verifica si stompClient está inicializado.
    if (this.stompClient) {
      // Desconecta del WebSocket.
      this.stompClient.disconnect(() => {
        console.log('Desconectado');
        // Actualiza el estado de la conexión a false.
        this.isConnectedSubject.next(false);
      });
    }
  }

  // Método para enviar un mensaje a través del WebSocket.
  sendMessage(mensaje: any) {
    // Verifica si stompClient está conectado.
    if (this.stompClient && this.stompClient.connected) {
      // Envía el mensaje al WebSocket.
      this.stompClient.send('/app/chat', {}, JSON.stringify(mensaje));
    }
  }

  // Método para obtener los mensajes guardados desde la API.
  getMensajesGuardados(): Observable<any> {
    // Hace una solicitud GET a la API de mensajes.
    return this.http.get(this.API_URL);
  }

  get conectado() {
    return this.isConnectedSubject.value;
  }


    // Recuperar mensajes pasados por username
    getMensajesPorUsuario(username: string): Observable<any[]> {
      return this.http.get<any[]>(`${this.API_URL}/usuario/${username}`);
    }
}
