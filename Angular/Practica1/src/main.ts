import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app/app.component';
import { LoginComponent } from './app/login/login.component';
import { GalleryComponent } from './app/gallery/gallery.component';
import { importProvidersFrom } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http'; 

// Rutas de la aplicación
const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'gallery', component: GalleryComponent }
];

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient(), 
    importProvidersFrom(RouterModule, FormsModule)
  ]
});
