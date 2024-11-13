import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { GalleryComponent } from './gallery/gallery.component';

export const appRoutes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'gallery', component: GalleryComponent }
];
