import { Component, OnInit, OnDestroy } from '@angular/core';
import { ChatService } from '../../services/chat.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit, OnDestroy {
  mensajes: any[] = []; // Array para mensajes recibidos
  mensaje: string = ''; // Mensaje a enviar
  username: string = ''; // Nick personalizado por el usuario
  color: string = this.getRandomColor(); // Color único para el usuario
  conectado: boolean = false;


  constructor(public chatService: ChatService) {}


  ngOnInit(): void {
    this.chatService.getMessages().subscribe((mensaje) => {
      console.log('Nuevo mensaje recibido:', mensaje); // Depuración
      this.mensajes.push(mensaje); // Actualiza el array de mensajes
      console.log('Mensajes actuales:', this.mensajes); // Verifica los mensajes actualizados
    });
  }
 


  // Método ngOnDestroy que se ejecuta cuando el componente se destruye.
ngOnDestroy(): void {
  // Llama al método desconectar del servicio chatService para desconectar el WebSocket.
  this.chatService.desconectar(); // Desconectar al destruir el componente
}

// Método enviarMensaje que se ejecuta cuando el usuario envía un mensaje.
enviarMensaje() {
  // Crea un nuevo objeto de mensaje con el autor, nombre de usuario, color y contenido del mensaje.
  const nuevoMensaje = {
    autor: this.username || 'Usuario Anónimo', // Si no hay nombre de usuario, usa 'Usuario Anónimo'.
    username: this.username || 'Usuario Anónimo', // Si no hay nombre de usuario, usa 'Usuario Anónimo'.
    color: this.color, // Color del usuario.
    contenido: this.mensaje, // Contenido del mensaje.
  };
  // Llama al método sendMessage del servicio chatService para enviar el mensaje.
  this.chatService.sendMessage(nuevoMensaje);
  // Limpia el campo de entrada del mensaje.
  this.mensaje = ''; // Limpiar el campo de entrada
}

// Método privado getRandomColor que genera un color aleatorio para el usuario.
private getRandomColor(): string {
  // Define los caracteres hexadecimales.
  const letters = '0123456789ABCDEF';
  // Inicializa la variable color con '#'.
  let color = '#';
  // Genera un color aleatorio de 6 caracteres.
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  // Retorna el color generado.
  return color;
}

// Método conectar que se ejecuta para establecer la conexión WebSocket.
conectar() {
  // Llama al método conectar del servicio chatService para establecer la conexión WebSocket.
  this.chatService.conectar(); // Establecer conexión WebSocket
  // Llama al método getMensajesGuardados del servicio chatService para obtener los mensajes guardados.
  this.chatService.getMensajesGuardados().subscribe(
    (mensajes) => {
      // Asigna los mensajes obtenidos a la propiedad mensajes del componente.
      this.mensajes = mensajes; // Cargar los mensajes previos desde la base de datos
      // Imprime los mensajes cargados en la consola.
      console.log('Mensajes cargados desde la base de datos:', this.mensajes);
    }
  );
}
  desconectar() {
    this.chatService.desconectar();
    this.conectado = false; // Cambiar el estado a desconectado
  }
}
