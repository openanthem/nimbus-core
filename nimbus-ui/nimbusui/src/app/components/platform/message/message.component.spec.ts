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

import { MessageComponent } from './message.component';

let fixture, app, messageService;

describe('MessageComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          MessageComponent
       ],
       imports: [
           ToastModule,
           MessageModule,
           FormsModule,
           ReactiveFormsModule,
           DataTableModule, SharedModule, OverlayPanelModule, PickListModule, DragDropModule, CalendarModule, 
           FileUploadModule, ListboxModule, DialogModule, CheckboxModule, DropdownModule, RadioButtonModule, 
           ProgressBarModule, ProgressSpinnerModule, AccordionModule, GrowlModule, InputSwitchModule, TreeTableModule
       ],
       providers: [
        MessageService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(MessageComponent);
    app = fixture.debugElement.componentInstance;
    messageService = TestBed.get(MessageService);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('ngOnInit() should call updateMessageObject()', async(() => {
      spyOn(app, 'updateMessageObject').and.callThrough();
      app.ngOnInit();
    expect(app.updateMessageObject).toHaveBeenCalled();
  }));

  it('updateMessageObject() should call messageService.addAll() for toast component', async(() => {
      app.messageContext = 'TOAST';
      app.messageArray = [{severity: 'error', summary: 'Error Message', detail: 'test', life: 10000}];
      spyOn(messageService, 'addAll').and.callThrough();
      app.ngOnInit();
      expect(messageService.addAll).toHaveBeenCalled();
  }));

});