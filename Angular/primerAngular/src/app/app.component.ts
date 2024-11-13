import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink } from '@angular/router';




@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  // Propiedades
  nombre = 'Carlos López';
  edad = 35;
  sueldos = [2000, 2100, 2200];
  activo = true;
  contador = 1;
  posicion = 'Desarrollador';
  imagenURL = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTl6NaMnFTttEvSXFQOSiIdBArtI0GT4D2YWA&s';  // Para Property Binding
  habilitarBoton = true;  // Para Property Binding


  // Lista de proyectos del empleado
  proyectos = [
    { nombre: 'Sistema de Inventario', estado: 'Completo' },
    { nombre: 'Plataforma de e-Commerce', estado: 'En progreso' },
    { nombre: 'Sistema de Recursos Humanos', estado: 'Pendiente' }
  ];


  // Métodos para el template
  ultimos3Sueldos() {
    return this.sueldos.reduce((total, sueldo) => total + sueldo, 0);
  }


  esActivo() {
    return this.activo ? 'El empleado está activo' : 'El empleado no está activo';
  }


  incrementar() {
    this.contador++;
  }


  decrementar() {
    this.contador--;
  }


  cambiarEstado() {
    this.activo = !this.activo;
  }


  obtenerEstadoProyecto(proyecto: string) {
    const found = this.proyectos.find(p => p.nombre === proyecto);
    return found ? found.estado : 'No encontrado';
  }

  toggleBoton() {
    this.habilitarBoton = !this.habilitarBoton;
  }


}

