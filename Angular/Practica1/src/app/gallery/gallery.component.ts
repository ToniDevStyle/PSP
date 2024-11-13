import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; 

@Component({
  selector: 'app-gallery',
  standalone: true,
  imports: [CommonModule], 
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent {
  cities = [
    {
      name: 'New York',
      description: 'The city that never sleeps.',
      imageUrl: 'assets/images/ny.jpg'
    },
    {
      name: 'Paris',
      description: 'The city of lights.',
      imageUrl: 'assets/images/paris.jpg'
    },
    {
      name: 'Tokyo',
      description: 'A city blending tradition with technology.',
      imageUrl: 'assets/images/tokyo.jpg'
    },
  ];
}
