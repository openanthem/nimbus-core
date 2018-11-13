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
import { configureTestSuite } from 'ng-bullet';
import { setup, TestContext } from '../../../setup.spec';
import * as data from '../../../payload.json';

let fixture, app, fileservice, param;

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

const declarations = [ FileUploadComponent ];
const imports = [
   FileUploadModule,
   HttpClientModule,
   HttpModule,
   StorageServiceModule
];
const providers = [
   {provide: FileService, useClass: MockFileService},
   { provide: 'JSNLOG', useValue: JL },
   { provide: CUSTOM_STORAGE, useExisting: SESSION_STORAGE },
   WebContentSvc,
   CustomHttpClient,
   LoggerService,
   AppInitService
];

describe('FileUploadComponent', () => {

  configureTestSuite();
  setup(FileUploadComponent, declarations, imports, providers);
  param = (<any>data).payload;

  beforeEach(async function(this: TestContext<FileUploadComponent>){
    this.hostComponent.element = param;
    fileservice = TestBed.get(FileService);
  });

  it('should create the FileUploadComponent', async function (this: TestContext<FileUploadComponent>) {
      expect(this.hostComponent).toBeTruthy();
  });

  it('get _value should return value', async function (this: TestContext<FileUploadComponent>) {
    this.hostComponent.value = 123;
    expect(this.hostComponent.value).toEqual(123);
  });

  it('registerOnTouched should update onTouched property', async function (this: TestContext<FileUploadComponent>) {
    this.hostComponent.registerOnTouched(123);
    expect(this.hostComponent.onTouched).toEqual(123);
  });

  it('registerOnChange should update onChange property', async function (this: TestContext<FileUploadComponent>) {
    this.hostComponent.registerOnChange(456);
    expect(this.hostComponent.onChange).toEqual(456);
  });

  it('ngOnInit() should update fileservice.metaData', async function (this: TestContext<FileUploadComponent>) {
    this.fixture.whenStable().then(() => {
      this.hostComponent.element.config.uiStyles.attributes.metaData = 'test';
      this.hostComponent.ngOnInit();
      expect(fileservice.metaData).toEqual('test');
    });
  });

  it('hasfile() should return 0', async function (this: TestContext<FileUploadComponent>) {
    const file: File = { name: 'testt', size: 321 } as File;
    this.hostComponent.selectedFiles = [file];
    this.hostComponent.hasFile(file);
    expect(this.hostComponent.hasFile(file)).toEqual(0);
  });

  it('removeFiles() should update value and selectedFiles properties', async function (this: TestContext<FileUploadComponent>) {
    const eve = { file: '' };
    spyOn(this.hostComponent, 'hasFile').and.returnValue('-1');
    const file: File = { name: 'testt', size: 321 } as File;
    this.hostComponent.selectedFiles = [file];
    this.hostComponent.removeFiles(eve);
    expect(this.hostComponent.value).toEqual(this.hostComponent.selectedFiles);
  });

  it('removeFiles() should update value property', async function (this: TestContext<FileUploadComponent>) {
    const eve = { file: '' };
    spyOn(this.hostComponent, 'hasFile').and.returnValue('0');
    const file: File = { name: 'testt', size: 321 } as File;
    this.hostComponent.selectedFiles = [file];
    this.hostComponent.removeFiles(eve);
    expect(this.hostComponent.selectedFiles).toEqual([]);
  });

  it('addFiles() should update selectedFiles as []', async function (this: TestContext<FileUploadComponent>) {
    this.fixture.whenStable().then(() => {
      const eve = { originalEvent: { dataTransfer: { files: [{ postUrl: '' }] } } };
      this.hostComponent.element.config.uiStyles.attributes.url = '/test.com';
      this.hostComponent.multipleFiles = false;
      spyOn(this.hostComponent, 'hasFile').and.returnValue('-1');
      spyOn(this.hostComponent.pfu, 'isFileTypeValid').and.returnValue(false);
      this.hostComponent.addFiles(eve);
      expect(this.hostComponent.selectedFiles).toEqual([]);
    });
  });

  it('addFiles() should update selectedFiles[]', async function (this: TestContext<FileUploadComponent>) {
    this.fixture.whenStable().then(() => {
      const eve = { originalEvent: { dataTransfer: { files: [{ postUrl: '' }] } } };
      this.hostComponent.element.config.uiStyles.attributes.url = '/test.com';
      this.hostComponent.multipleFiles = true;
      this.hostComponent.selectedFiles = [];
      spyOn(this.hostComponent, 'hasFile').and.returnValue('-1');
      spyOn(this.hostComponent.pfu, 'isFileTypeValid').and.returnValue(true);
      this.hostComponent.addFiles(eve);
      const res: any = { postUrl: '/test.com' };
      expect(this.hostComponent.selectedFiles).toEqual([res]);
    });
  });

  it('addFiles() should not call fileservice.uploadFile', async function (this: TestContext<FileUploadComponent>) {
    this.fixture.whenStable().then(() => {
      const eve = { originalEvent: { dataTransfer: { files: [{ postUrl: '' }] } } };
      this.hostComponent.element.config.uiStyles.attributes.url = '/test.com';
      spyOn(this.hostComponent, 'hasFile').and.returnValue('1');
      spyOn(fileservice, 'uploadFile').and.callThrough();
      this.hostComponent.addFiles(eve);
      expect(fileservice.uploadFile).not.toHaveBeenCalled();
    });
  });

});