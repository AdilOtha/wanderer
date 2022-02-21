import { Component, OnInit } from '@angular/core';
import * as mapboxgl from 'mapbox-gl';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {  
  item$!: Observable<any[]>;
  map!: mapboxgl.Map;
  styleUrl: string = 'mapbox://styles/mapbox/streets-v11';
  lat = 37.75;
  lng = -122.41;   

  constructor() {
    console.log("home page loaded");
    // const collect = collection(firestore, 'test');
    // this.item$ = collectionData(collect);
  } 

  ngOnInit(): void {
    // this.initializeMap();
  }

}
