import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { UserProfile } from 'src/app/data/schema/user-profile';
import { UserProfileService } from 'src/app/data/service/user-profile/user-profile.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
})
export class UserProfileComponent implements OnInit {
  readonly title: string = 'User Profile';

  readonly editProfileButtonLabel: string = 'Edit Profile Details';

  modalDisplay: boolean = false;

  submitted: boolean = false;

  readonly DEFAULT_PROFILE_IMAGE =
    'https://images.unsplash.com/photo-1645785538675-f81e7dbab4b4?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80';

  profileImage: any = this.DEFAULT_PROFILE_IMAGE;

  @ViewChild('imageUpload') imageUpload: any;
  fileLimit: number = 1;
  uploadedFilename: string = '';

  firstName: string = '';
  lastName: string = '';
  emailId: string = '';

  saveForm: FormGroup = this.fb.group({
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    profileImage: [''],
  });

  constructor(
    private fb: FormBuilder,
    private userProfileService: UserProfileService,
    private sanitizer: DomSanitizer,
    private spinner: NgxSpinnerService,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.spinner.show();
    this.userProfileService
      .getUserDetails()
      .pipe(
        finalize(() => {
          this.spinner.hide();
        })
      )
      .subscribe({
        next: (data: any) => {
          console.log(data);
          
          const userData = data.payload;
          this.firstName = userData.firstName;
          this.lastName = userData.lastName;
          this.emailId = userData.emailId;

          if (userData?.image) {
            const unsafeImageUrl = userData.image;
            this.saveForm.controls['profileImage'].patchValue(
              this.sanitizer.bypassSecurityTrustResourceUrl(unsafeImageUrl)
            );
          } else if (userData?.googlePhotoUrl) {
            this.saveForm.controls['profileImage'].patchValue(
              userData.googlePhotoUrl
            );
          } else {
            this.saveForm.controls['profileImage'].patchValue(
              this.DEFAULT_PROFILE_IMAGE
            );
          }
        },
        error: (err: any) => {
          console.log(err);
          const error = err.error;
          this.toast.error(error?.payload?.message);
        },
      });
  }

  get f() {
    return this.saveForm.controls;
  }

  showModal(): void {
    this.modalDisplay = true;
    this.saveForm.controls['firstName'].patchValue(this.firstName);
    this.saveForm.controls['lastName'].patchValue(this.lastName);
  }

  onSubmitClick() {
    this.submitted = true;
    if (this.saveForm.valid) {
      this.spinner.show();
      console.log('form submitted');
      const userProfile: UserProfile = {
        firstName: this.saveForm.controls['firstName'].value,
        lastName: this.saveForm.controls['lastName'].value,
        profileImage: this.profileImage,
      };
      this.userProfileService
        .updateUserDetails(userProfile)
        .pipe(
          finalize(() => {
            this.imageUpload.clear();
            this.submitted = false;
            this.modalDisplay = false;
          })
        )
        .subscribe({
          next: (data: any) => {
            console.log(data);
            
            const updatedUser = data.payload;
            this.firstName = updatedUser.firstName;
            this.lastName = updatedUser.lastName;

            this.saveForm.controls['firstName'].patchValue(
              updatedUser.firstName
            );
            this.saveForm.controls['lastName'].patchValue(updatedUser.lastName);

            console.log(data);

            if (this.profileImage instanceof Blob) {
              const reader = new FileReader();
              reader.readAsDataURL(this.profileImage);
              reader.onloadend = () => {                
                this.saveForm.controls['profileImage'].patchValue(
                  reader.result
                );
                this.spinner.hide();
              };
            } else {
              this.spinner.hide();
            }
            this.toast.info('Profile updated successfully');
          },
          error: (err: any) => {
            console.log(err);
            const error = err.error;
            this.toast.error(error?.payload?.message);
            this.spinner.hide();
          },
        });
    }
  }

  myUploader(event: any) {
    console.log(event.files[0]);
    // show error if file is of type svg
    if (event.files.length>0 && event.files[0].type === 'image/svg+xml') {
      this.toast.error('SVG files are not allowed');
      this.imageUpload.clear();
      return;
    }
    this.profileImage = event.files[0];
    // console.log(this.saveForm.controls['profileImage'].value);
  }

  onProfileImageClear(event: any) {
    // console.log(event);
    this.profileImage = '';
    this.uploadedFilename = '';
    // console.log('Cancel Triggered');
  }
}
