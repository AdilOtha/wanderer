import { Component, OnInit } from '@angular/core';
import { Firestore, collectionData, collection } from '@angular/fire/firestore';
import { Observable, BehaviorSubject } from 'rxjs';
import { MouseEvent } from '@agm/core';
import { PinService } from 'src/app/data/service/pin-service/pin.service';
import { Pin } from 'src/app/data/schema/pin';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
   
  pinCreationUpdates!: Observable<any>;
  coords:any[] = [];  

  // google maps zoom level
  zoom: number = 8;

  // default radius (in kms)
  radius: number = 50;

  // initial center position for the map
  latitude: number = 51.678418;
  longitude: number = 7.809007;  

  savedPins: Pin[] = [
	  {
		  latitude: 44.6476,
		  longitude: 63.5728,
		  locationName: 'A',		  
      isSaved: true
	  },
	  {
		  latitude: 51.373858,
		  longitude: 7.215982,
		  locationName: 'B',		  
      isSaved: true
	  },
	  {
		  latitude: 51.723858,
		  longitude: 7.895982,
		  locationName: 'C',
      isSaved: true
	  }
  ];

  newlyCreatedPins: Pin[] = [];

  constructor(private firestore: Firestore, private pinService: PinService) {
    const dataList = collection(this.firestore, 'pin_updates');
    this.pinCreationUpdates = collectionData(dataList);
    this.pinCreationUpdates.subscribe(update=>{
      console.log(update);
      this.pinService.getPinsByRadius(this.radius, this.latitude, this.longitude)
        .subscribe({
          next: (data:any)=>{
            console.log(data);
            for(let pin of data){
              pin.isSaved = true;
            }
            this.savedPins = data;
            this.savedPins = [...this.savedPins, ...this.newlyCreatedPins]
          },
          error: (err:HttpErrorResponse) => {
            console.log(err);
          }
        });
    });
  }

  ngOnInit(): void {
    // get user's current location
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(position => {        
       this.latitude = position.coords.latitude;
       this.longitude = position.coords.longitude;       
       console.log({
         latitude: position.coords.latitude,
         longitude: position.coords.longitude
       });
     });
   }
  }

  createPin($event: MouseEvent){
    console.log($event);
    if(this.savedPins[this.savedPins.length-1].isSaved===false){
      this.savedPins.splice(this.savedPins.length-1, 1);      
    }    
    this.savedPins.push({
      latitude: $event.coords.lat,
      longitude: $event.coords.lng,
      locationName: '',
      isSaved: false,
      pinId: -1
    });
  }

  savePin(selectedPin: any, index: number) {
    this.savedPins[index].isSaved=true;
    console.log(`clicked the marker: ${selectedPin.locationName || index}`);
    let pin: Pin = {
      latitude: selectedPin.latitude,
      longitude: selectedPin.longitude,
      locationName: '',
      isSaved: true,
    };

    this.pinService.insertPin(pin).subscribe({
      next: (data: any)=>{
        console.log(data);
        data.isSaved = true;
        this.newlyCreatedPins.push(data);
      },
      error: (err: HttpErrorResponse)=>{
        console.log(err);
      },
      complete: ()=>{
        
      }
    });
  }

  pinDragEnd(m: Pin, index: number, event: MouseEvent) {
    console.log('dragEnd', m, event);
    m.latitude = event.coords.lat;
    m.longitude = event.coords.lng;
  }

  identifyPin(index: number, pin: Pin){
    return pin.pinId;
  }

}

// interface marker {
// 	lat: number;
// 	lng: number;
// 	label: string;
// 	draggable: boolean;
//   isSaved: boolean;
// }
