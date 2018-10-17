'use strict';
import { TestBed, async } from '@angular/core/testing';
import { FileUploadModule } from 'primeng/primeng';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { Subject } from 'rxjs';
import { JL } from 'jsnlog';
import { StorageServiceModule, SESSION_STORAGE } from 'angular-webstorage-service';

import { FileUploadComponent } from './file-upload.component';
import { FileService } from './../../../services/file.service';
import { WebContentSvc } from './../../../services/content-management.service';
import { CustomHttpClient } from '../../../services/httpclient.service';
import { LoggerService } from './../../../services/logger.service';
import { SessionStoreService, CUSTOM_STORAGE } from './../../../services/session.store';
import { AppInitService } from './../../../services/app.init.service';

let fixture, app, fileservice;

class MockFileService {
    public addFile$: Subject<any>;
    public errorEmitter$: Subject<any>;
    
    metaData: any;

    constructor() {
        this.addFile$ = new Subject<any>();
        this.errorEmitter$ = new Subject<any>();
    }

    uploadFile(a) {
        this.addFile$.next({test: 123});
    }
}

describe('FileUploadComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
          FileUploadComponent
       ],
       imports: [
           FileUploadModule,
           HttpClientModule,
           HttpModule,
           StorageServiceModule
       ],
       providers: [
           {provide: FileService, useClass: MockFileService},
           { provide: 'JSNLOG', useValue: JL },
           { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
           WebContentSvc,
           CustomHttpClient,
           LoggerService,
           AppInitService
       ]
    }).compileComponents();
    fixture = TestBed.createComponent(FileUploadComponent);
    app = fixture.debugElement.componentInstance;
    fileservice = TestBed.get(FileService);
  }));

  it('should create the app', async(() => {
    expect(app).toBeTruthy();
  }));

  it('get _value should return value', async(() => {
    app.value = 123;
    expect(app.value).toEqual(123);
  }));

  it('registerOnTouched should update app.onTouched', async(() => {
    app.registerOnTouched(123);
    expect(app.onTouched).toEqual(123);
  }));

  it('registerOnChange should update app.onChange', async(() => {
    app.registerOnChange(456);
    expect(app.onChange).toEqual(456);
  }));

  it('ngOnInit should update fileservice.metaData', async(() => {
    app.element = { config: { uiStyles: { attributes: { metaData: 'test' } } } };
    app.ngOnInit();
    expect(fileservice.metaData).toEqual('test');
  }));

  it('hasfile() should return 0', async(() => {
    const file = { name: 'testt', size: 321 };
    app.selectedFiles = [{ name: 'testt', size: 321 }, {}, {}];
    app.hasFile(file);
    expect(app.hasFile(file)).toEqual(0);
  }));

  it('removeFiles() should update app.value and app.selectedFiles', async(() => {
    const eve = { file: '' };
    spyOn(app, 'hasFile').and.returnValue('-1');
    app.selectedFiles = 'test';
    app.removeFiles(eve);
    expect(app.value).toEqual(app.selectedFiles);
  }));

  it('removeFiles() should update app.value', async(() => {
    const eve = { file: '' };
    spyOn(app, 'hasFile').and.returnValue('0');
    app.selectedFiles = [1, 2];
    app.removeFiles(eve);
    expect(app.value).toEqual([2]);
  }));

  it('addFiles() should update selectedFiles as []', async(() => {
    const eve = { originalEvent: { dataTransfer: { files: [{ postUrl: '' }] } } };
    app.element = { config: { uiStyles: { attributes: { url: '/test.com' } } } };
    app.multipleFiles = false;
    spyOn(app, 'hasFile').and.returnValue('-1');
    spyOn(app.pfu, 'isFileTypeValid').and.returnValue(false);
    app.addFiles(eve);
    expect(app.selectedFiles).toEqual([]);
  }));

  it('addFiles() should update selectedFiles[]', async(() => {
    const eve = { originalEvent: { dataTransfer: { files: [{ postUrl: '' }] } } };
    app.element = { config: { uiStyles: { attributes: { url: '/test.com' } } } };
    app.multipleFiles = true;
    app.selectedFiles = [];
    spyOn(app, 'hasFile').and.returnValue('-1');
    spyOn(app.pfu, 'isFileTypeValid').and.returnValue(true);
    app.addFiles(eve);
    expect(app.selectedFiles).toEqual([{ postUrl: '/test.com' }]);
  }));

  it('addFiles() should not call fileservice.uploadFile', async(() => {
    const eve = { originalEvent: { dataTransfer: { files: [{ postUrl: '' }] } } };
    app.element = { config: { uiStyles: { attributes: { url: '/test.com' } } } };
    spyOn(app, 'hasFile').and.returnValue('1');
    spyOn(fileservice, 'uploadFile').and.callThrough();
    app.addFiles(eve);
    expect(fileservice.uploadFile).not.toHaveBeenCalled();
  }));

});