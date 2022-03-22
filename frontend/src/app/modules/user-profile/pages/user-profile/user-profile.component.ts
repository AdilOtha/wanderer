import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { UserProfile } from 'src/app/data/schema/user-profile';
import { UserProfileService } from 'src/app/data/service/user-profile/user-profile.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
})
export class UserProfileComponent implements OnInit {
  title: string = 'User Profile';

  editProfileButtonLabel: string = 'Edit Profile Details';

  modalDisplay: boolean = false;

  submitted: boolean = false;

  profileImage: any =
    'https://images.unsplash.com/photo-1645785538675-f81e7dbab4b4?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80';

  email: string = 'adil@gmail.com';

  fileLimit: number = 1;
  uploadedFilename: string = '';

  saveForm: FormGroup = this.fb.group({
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    emailId: [{ value: '', disabled: true }],
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
    console.log('User-profile component loaded');
    this.userProfileService.getUserDetails().subscribe({
      next: (data: any) => {
        console.log(data);
        const userData = data.payload;
        this.saveForm.controls['firstName'].patchValue(userData.firstName);
        this.saveForm.controls['lastName'].patchValue(userData.lastName);
        this.saveForm.controls['emailId'].patchValue(userData.emailId);
        if (userData.image) {
          const unsafeImageUrl = userData.image;
          this.saveForm.controls['profileImage'].patchValue(
            this.sanitizer.bypassSecurityTrustResourceUrl(unsafeImageUrl)
          );
        } else {
          this.saveForm.controls['profileImage'].patchValue(
            userData.googlePhotoUrl
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
      this.userProfileService.updateUserDetails(userProfile).subscribe({
        next: (data: any) => {
          const updatedUser = data.payload;

          console.log(data);

          const reader = new FileReader();
          reader.readAsDataURL(this.profileImage);
          reader.onloadend = () => {
            this.saveForm.controls['profileImage'].patchValue(reader.result);
            this.spinner.hide();
          };
          this.submitted = false;
          this.modalDisplay = false;
        },
        error: (err: any) => {
          console.log(err);
          const error = err.error;
          this.toast.error(error?.payload?.message);
        },
      });
    }
  }

  myUploader(event: any) {
    console.log(event);
    this.profileImage = event.files[0];
    // this.saveForm.controls['profileImage'].setValue(event.files[0]);
    // console.log(this.saveForm.controls['profileImage'].value);
  }

  onProfileImageClear(event: any) {
    console.log(event);
    this.profileImage = '';
    this.uploadedFilename = '';
    console.log('Cancel Triggered');
  }
}
