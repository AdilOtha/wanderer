import { Component, OnInit } from '@angular/core';
import { Firestore, collectionData, collection } from '@angular/fire/firestore';
import { debounceTime, finalize, Observable, Subject, map } from 'rxjs';
import { MouseEvent } from '@agm/core';
import { PinService } from 'src/app/data/service/pin-service/pin.service';
import { Pin } from 'src/app/data/schema/pin';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  providers: [],
})
export class HomeComponent implements OnInit {
  //Firestore Data
  pinCreationUpdates!: Observable<any>;
  readonly PIN_UPDATE_COLLECTION: string = 'pin_updates';

  // Pin Center and Radius Subjects for 2-way communication between UI and Keyboard input
  centerChangeSubject: Subject<any> = new Subject();
  radiusChangeSubject: Subject<any> = new Subject();

  // google maps default zoom level
  zoom: number = 8;

  // default radius (in meters)
  radius: number = 50000;

  // initial/default center position for the map
  latitude: number = 21.7645;
  longitude: number = 72.1519;

  // Saved Pins
  savedPins: Pin[] = [];

  // Pins created by User
  newlyCreatedPins: Pin[] = [];

  // currently updating pins
  updatingPinsMap: any = {};

  // Pin Modal Data
  readonly mapPanelHeader: string = 'Navigator';
  readonly pinModalHeaderLabel: string = 'Add/Edit Pin Details';
  displayPinModal: boolean = false;
  enablePinModalSaveButton: boolean = true;

  currentPin!: Pin;
  currentPinIndex!: number;

  pinForm: FormGroup = this.fb.group({
    locationName: ['', [Validators.required]],
    pinLatitude: ['', [Validators.required]],
    pinLongitude: ['', [Validators.required]],
  });
  submitted: boolean = false;

  readonly pinIconUrl: any = {
    temporaryPinIcon: {
      url: 'assets/svgs/pin_alt_yellow.svg',
      scaledSize: {
        width: 50,
        height: 50,
      },
    },
    savedPinIcon: {
      url: 'assets/svgs/pin_alt_red.svg',
      scaledSize: {
        width: 50,
        height: 50,
      },
    },
    editablePinIcon: {
      url: 'assets/svgs/pin_alt_blue.svg',
      scaledSize: {
        width: 50,
        height: 50,
      },
    },
  };

  constructor(
    private firestore: Firestore,
    private pinService: PinService,
    private fb: FormBuilder,
    private toast: ToastrService,
    private spinner: NgxSpinnerService
  ) {}

  ngOnInit(): void {
    // get user's current location
    this.setCurrentLocation();

    // firestore setup
    const dataList = collection(this.firestore, this.PIN_UPDATE_COLLECTION);
    this.pinCreationUpdates = collectionData(dataList);

    // subscribe to pin creation updates from firestore
    this.pinCreationUpdates.pipe(debounceTime(0)).subscribe((update) => {
      console.log(update);
      this.getPinsByRadiusFromService();
    });

    // listen to center change events
    this.centerChangeSubject.pipe(debounceTime(0)).subscribe((data: any) => {
      this.latitude = data.lat;
      this.longitude = data.lng;
      this.getPinsByRadiusFromService();
    });

    // listen to radius change events
    this.radiusChangeSubject
      .pipe(debounceTime(1000))
      .subscribe((newRadius: any) => {
        // console.log(newRadius);

        this.radius = parseInt(newRadius.toString());

        this.getPinsByRadiusFromService();
      });
  }

  // render pin on map
  createPin(event: MouseEvent) {
    // console.log($event);
    // console.log(this.savedPins);

    // remove last rendered pin from map
    if (
      this.savedPins.length > 0 &&
      !this.savedPins[this.savedPins.length - 1].isSaved
    ) {
      this.savedPins.splice(this.savedPins.length - 1, 1);
    }

    // render new pin on map
    this.savedPins.push({
      latitude: event.coords.lat,
      longitude: event.coords.lng,
      locationName: '',
      isSaved: false,
      isDraggable: true,
      pinId: -1,
      iconUrl: this.pinIconUrl.temporaryPinIcon,
    });
  }

  openPinModal(pin: Pin, index: number) {
    this.setCurrentPin(pin, index);
    this.displayPinModal = true;
  }

  // set double clicked pin as current pin to set values in pin modal form
  setCurrentPin(pin: Pin, index: number) {
    this.currentPin = pin;
    this.currentPinIndex = index;
    this.pinForm.controls['locationName'].patchValue(pin.locationName);
    this.pinForm.controls['pinLatitude'].patchValue(pin.latitude);
    this.pinForm.controls['pinLongitude'].patchValue(pin.longitude);
    // console.log('Current Pin Index: ' + this.currentPinIndex);
  }

  // getter to access the pin form controls in template
  get pinFormControls() {
    return this.pinForm.controls;
  }

  // save or update pin to database
  savePin() {
    this.submitted = true;
    this.enablePinModalSaveButton = false;
    if (this.pinForm.valid) {
      // show spinner
      this.spinner.show();

      // console.log('Saved Pin Index: ' + this.currentPinIndex);

      this.savedPins[this.currentPinIndex].isSaved = true;

      let pin: Pin = {
        latitude: this.currentPin.latitude,
        longitude: this.currentPin.longitude,
        locationName: this.pinForm.controls['locationName'].value,
        pinId: -1,
        isSaved: true,
        isDraggable: false,
        iconUrl: this.pinIconUrl.savedPinIcon,
      };

      // if Pin exists then update Pin
      if (this.currentPin.pinId !== -1) {
        pin.pinId = this.currentPin.pinId;

        // call service to update pin
        this.pinService
          .updatePin(pin.pinId, pin.locationName, pin.latitude, pin.longitude)
          .pipe(
            finalize(() => {
              this.displayPinModal = false;
              this.submitted = false;
              this.enablePinModalSaveButton = true;

              // hide spinner
              this.spinner.hide();
            })
          )
          .subscribe({
            next: (data: any) => {
              if (data === null) {
                console.log('Error while updating pin');
              } else {
                console.log({
                  updatedPin: data,
                });
                data.isSaved = true;
                data.isDraggable = false;
                data.iconUrl = this.pinIconUrl.savedPinIcon;
                // this.newlyCreatedPins.push(data);
                this.savedPins[this.currentPinIndex] = data;
                // find index of updated pin in newlyCreatedPins array and update it with new data
                const index = this.newlyCreatedPins.findIndex(
                  (pin) => pin.pinId === data.pinId
                );
                this.newlyCreatedPins[index] = data;
                delete this.updatingPinsMap[data.pinId];
                console.log(this.savedPins[this.currentPinIndex]);
              }
            },
            error: (err: any) => {
              console.log(err);
            },
          });
      } else {
        // else if Pin does not exist then save Pin

        // call service to insert pin
        this.pinService
          .insertPin(pin)
          .pipe(
            finalize(() => {
              this.displayPinModal = false;
              this.submitted = false;
              this.enablePinModalSaveButton = true;

              // hide spinner
              this.spinner.hide();
            })
          )
          .subscribe({
            next: (data: any) => {
              if (data === null) {
                console.log('Error while saving pin');
              } else {
                console.log({ newPin: data });
                data.isSaved = true;
                data.isDraggable = false;
                data.iconUrl = this.pinIconUrl.savedPinIcon;
                this.newlyCreatedPins.push(data);
                this.savedPins[this.currentPinIndex] = data;
              }
            },
            error: (err: HttpErrorResponse) => {
              console.log(err);
            },
          });
      }
    }
  }

  editPinCoordinates() {
    this.displayPinModal = false;
    this.toast.info(
      'Please move the selected marker to change its coordinates'
    );
    this.savedPins[this.currentPinIndex].isDraggable = true;
    this.savedPins[this.currentPinIndex].iconUrl =
      this.pinIconUrl.editablePinIcon;
    this.updatingPinsMap[this.currentPin.pinId] = this.currentPin;
  }

  getPinsByRadiusFromService() {
    if (this.radius < 0) {
      this.radius = Math.abs(this.radius);
    }
    const radiusInKms: number = this.radius / 1000;
    this.pinService
      .getPinsByRadius(radiusInKms, this.latitude, this.longitude)
      .pipe(
        map((data: any) => {
          return this.performPinPostProcessing(data);
        })
      )
      .subscribe({
        next: (data: any) => {
          // for (let pin of data) {
          //   pin.isSaved = true;
          //   pin.iconUrl = this.pinIconUrl.savedPinIcon;
          // }
          console.log({ data, newlyCreatedPins: this.newlyCreatedPins });

          this.savedPins = data;
          // this.savedPins = [...this.savedPins, ...this.newlyCreatedPins];

          console.log({ updatedPins: this.updatingPinsMap });

          // refresh data of newly created pins
          if (this.newlyCreatedPins.length > 0) {

            // get pinIds from newlyCreatedPins array and pass to getPinsByIds service
            const pinIds = this.newlyCreatedPins.map((pin) => pin.pinId);

            this.pinService
              .getPinsByIds(pinIds)
              .pipe(
                map((data: any) => {
                  return this.performPinPostProcessing(data);
                })
              )
              .subscribe({
                next: (data: any) => {
                  console.log({ refreshedPins: data });
                  this.newlyCreatedPins = data;

                  const savedPinsMap: any = {};
                  for (let pin of this.savedPins.concat(
                    this.newlyCreatedPins
                  )) {
                    if (pin.pinId in this.updatingPinsMap) {
                      pin = this.updatingPinsMap[pin.pinId];
                    }
                    if (!(pin.pinId in savedPinsMap)) {
                      savedPinsMap[pin.pinId] = pin;
                    }
                  }

                  this.savedPins = Object.values(savedPinsMap);

                  console.log(this.savedPins);
                },
                error: (err: any) => {
                  console.log(err);
                },
              });
          }
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        },
      });
  }

  performPinPostProcessing(data: any) {
    if (data != null) {
      return data.map((pin: any) => {
        pin.isSaved = true;
        pin.isDraggable = false;
        pin.iconUrl = this.pinIconUrl.savedPinIcon;
        return pin;
      });
    } else {
      return [];
    }
  }

  pinDragEnd(m: Pin, index: number, event: MouseEvent) {
    console.log('dragEnd', m, event);
    m.latitude = event.coords.lat;
    m.longitude = event.coords.lng;
    console.log(m);
  }

  identifyPin(index: number, pin: Pin) {
    return pin.pinId;
  }

  centerChange(event: any) {
    this.centerChangeSubject.next(event);
  }

  shiftCenter(event: any) {
    this.centerChangeSubject.next(event.coords);
  }

  radiusChange(event: any) {
    this.radiusChangeSubject.next(event);
  }

  convertKilometerToMeter(valueInKilometers: number) {
    const unit = 1000.0;
    this.radius = valueInKilometers * unit;
  }

  setCurrentLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.latitude = position.coords.latitude;
        this.longitude = position.coords.longitude;
        this.toast.success('Location Access Granted');
      }, this.showError.bind(this));
    }
  }

  showError(error: any) {
    console.log(error);

    switch (error.code) {
      case error.PERMISSION_DENIED:
        this.toast.warning('User denied the request for Geolocation');
        break;
      case error.POSITION_UNAVAILABLE:
        this.toast.warning('Location information is unavailable');
        break;
      case error.TIMEOUT:
        this.toast.warning('Location information is unavailable');
        break;
      case error.UNKNOWN_ERROR:
        this.toast.warning('An unknown error occurred');
        break;
    }
  }
}
