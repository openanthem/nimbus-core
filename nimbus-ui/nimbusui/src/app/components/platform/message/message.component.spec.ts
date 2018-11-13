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
MessageService
];

describe('MessageComponent', () => {

  configureTestSuite();
  setup(MessageComponent, declarations, imports, providers);

  beforeEach(async function(this: TestContext<MessageComponent>){
    messageService = TestBed.get(MessageService);
  });

  it('should create the MessageComponent', async function (this: TestContext<MessageComponent>) {
    expect(this.hostComponent).toBeTruthy();
  });

  it('ngOnInit() should call updateMessageObject()', async function (this: TestContext<MessageComponent>) {
    spyOn(this.hostComponent, 'updateMessageObject').and.callThrough();
    this.hostComponent.ngOnInit();
    expect(this.hostComponent.updateMessageObject).toHaveBeenCalled();
  });

  it('updateMessageObject() should call messageService.addAll() for toast component', async function (this: TestContext<MessageComponent>) {
    this.hostComponent.messageContext = 'TOAST';
    this.hostComponent.messageArray = [{ severity: 'error', summary: 'Error Message', detail: 'test', life: 10000 }];
    spyOn(messageService, 'addAll').and.callThrough();
    this.hostComponent.ngOnInit();
    expect(messageService.addAll).toHaveBeenCalled();
  });

});