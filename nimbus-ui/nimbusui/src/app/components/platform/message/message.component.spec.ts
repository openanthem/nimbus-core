/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import { NmMessageService } from './../../../services/toastmessage.service';
'use strict';
import { TestBed, async } from '@angular/core/testing';
import { ToastModule } from 'primeng/toast';
import { MessageModule } from 'primeng/message';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
    FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
    ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule } from 'primeng/primeng';
import { MessageService } from 'primeng/api';
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import { MessageComponent } from './message.component';

let messageService;

const declarations = [
  MessageComponent
];
const imports = [
   ToastModule,
   MessageModule,
   FormsModule,
   ReactiveFormsModule,
   DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
   FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
   ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule
];
const providers = [
MessageService, NmMessageService
];
let fixture, hostComponent;
describe('MessageComponent', () => {

  configureTestSuite(() => {
    setup( declarations, imports, providers);
  });


  beforeEach(() => {
    fixture = TestBed.createComponent(MessageComponent);
    hostComponent = fixture.debugElement.componentInstance;
    messageService = TestBed.get(MessageService);
  });

  it('should create the MessageComponent', () => {
    fixture.whenStable().then(() => {
      expect(hostComponent).toBeTruthy();
    });
  });

  // it('ngOnInit() should call updateMessageObject()', () => {
  //   spyOn(hostComponent, 'updateMessageObject').and.callThrough();
  //   hostComponent.ngOnInit();
  //   expect(hostComponent.updateMessageObject).toHaveBeenCalled();
  // });

  // it('updateMessageObject() should call messageService.addAll() for toast component', () => {
  //   hostComponent.messageContext = 'TOAST';
  //   hostComponent.messageArray = [{ severity: 'error', summary: 'Error Message', detail: 'test', life: 10000 }];
  //   spyOn(messageService, 'addAll').and.callThrough();
  //   hostComponent.ngOnInit();
  //   expect(messageService.addAll).toHaveBeenCalled();
  // });

});