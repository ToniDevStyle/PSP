import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { envConfig } from '../env.config';
@Component({
  selector: 'app-gallery',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent implements OnInit {
  cities = [
    {
      name: 'New York',
      description: 'The city that never sleeps.',
      imageUrl: 'assets/images/ny.jpg',
      weather: null as { temperature: string; description: string; icon: string } | null
    },
    {
      name: 'Paris',
      description: 'The city of lights.',
      imageUrl: 'assets/images/paris.jpg',
      weather: null as { temperature: string; description: string; icon: string } | null
    },
    {
      name: 'Tokyo',
      description: 'A city blending tradition with technology.',
      imageUrl: 'assets/images/tokyo.jpg',
      weather: null as { temperature: string; description: string; icon: string } | null
    }
  ];

  private apiKey = envConfig.OPENWEATHER_API_KEY;
 private apiUrl = 'https://api.openweathermap.org/data/2.5/weather';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchWeatherData();
  }

  fetchWeatherData(): void {
    this.cities.forEach(city => {
      const url = `${this.apiUrl}?q=${city.name}&units=metric&appid=${this.apiKey}`;
      this.http.get(url).subscribe({
        next: (data: any) => {
          //Para verfificar el response que me da la api
          console.log(`Datos para ${city.name}:`, data); 
          
          if (data.main && data.weather && data.weather[0]) {
            city.weather = {
              temperature: data.main.temp,
              description: data.weather[0].description,
              icon: `https://openweathermap.org/img/wn/${data.weather[0].icon}@2x.png`
            };
          } else {
            
            city.weather = { temperature: 'N/A', description: 'No data', icon: '' };
          }
        },
        error: (err) => {
          console.error(`Error fetching weather data for ${city.name}:`, err);
          city.weather = { temperature: 'N/A', description: 'No data', icon: '' };
        }
      });
    });
  }
  
}
