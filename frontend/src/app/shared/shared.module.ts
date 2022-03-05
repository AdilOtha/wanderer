import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

//prime-ng imports
import {PanelModule} from 'primeng/panel';
import {ImageModule} from 'primeng/image';
import {CardModule} from 'primeng/card';
import {DividerModule} from 'primeng/divider';
import { ButtonModule } from 'primeng/button';
import {DialogModule} from 'primeng/dialog';
import {MessagesModule} from 'primeng/messages';
import {MessageModule} from 'primeng/message';
import { InputTextModule } from 'primeng/inputtext';
import {AvatarModule} from 'primeng/avatar';
import {FileUploadModule} from 'primeng/fileupload';
import {InputNumberModule} from 'primeng/inputnumber';
import {ToastModule} from 'primeng/toast';

//ngx-spinner import
import { NgxSpinnerModule } from "ngx-spinner";
import { MeterToKilometerPipe } from './pipes/meter-to-kilometer/meter-to-kilometer.pipe';

//ngx-toastr import
import { ToastrModule } from 'ngx-toastr';

@NgModule({
  declarations: [
    MeterToKilometerPipe
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    PanelModule,
    ImageModule,
    CardModule,
    DividerModule,
    ButtonModule,
    DialogModule,
    MessagesModule,
    MessageModule,
    ReactiveFormsModule,
    InputTextModule,
    AvatarModule,
    FileUploadModule,
    NgxSpinnerModule,
    InputNumberModule,
    FormsModule,
    ToastModule
  ],
  exports: [
    HttpClientModule,
    PanelModule,
    ImageModule,
    CardModule,
    DividerModule,
    ButtonModule,
    DialogModule,
    ReactiveFormsModule,
    MessagesModule,
    MessageModule,
    InputTextModule,
    AvatarModule,
    FileUploadModule,
    NgxSpinnerModule,
    InputNumberModule,
    FormsModule,
    MeterToKilometerPipe,
    ToastModule  
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: []
})
export class SharedModule { }
