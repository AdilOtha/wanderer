import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
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

@NgModule({
  declarations: [],
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
    FileUploadModule
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
    FileUploadModule
  ]
})
export class SharedModule { }
