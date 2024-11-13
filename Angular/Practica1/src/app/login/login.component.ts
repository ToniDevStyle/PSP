import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';  
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule], 
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private router: Router) {}

  onSubmit() {
    // Verifica si username y password son exactamente 'admin'
    if (this.username.trim() === 'admin' && this.password.trim() === 'admin') {
      this.router.navigate(['/gallery']);
    } else {
      Swal.fire({
        title: 'Error!',
        text: 'Credenciales incorrectas.',
        icon: 'error',
        confirmButtonText: 'Aceptar',
        customClass: {
          title: 'text-danger',  
          popup: 'text-dark' 
        },
        backdrop: true,
        showCloseButton: true,
        timer: 5000, 
      });
    }
  }
  
}
