 import { CustomHttpClient } from './httpclient.service';
import { Injectable, EventEmitter } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { HttpHeaders } from '@angular/common/http';
import {RequestOptions, Request, RequestMethod} from '@angular/http';

@Injectable()
export class FileService {

    

    addFile$: EventEmitter<any>;
    removeFile$: EventEmitter<any>;

    constructor(public http: CustomHttpClient) {
        this.addFile$ = new EventEmitter<any>();
        this.removeFile$ = new EventEmitter<any>();
    }

    uploadFile(file: File) {

        const formData: FormData = new FormData();
        formData.append('pfu', file, file.name);
        var url = file['postUrl'];
        
       return this.http.postFileData(url, formData)
            .subscribe(data => {
                // console.log("data", data);

                file['fileId'] = data;
                this.addFile$.next(file);
            },
                err => console.log(err),
                () => console.log('File uploaded ..')
            );
    }

    // removeFile(fileId: String){
    
    // var url="";
    // return this.http.removeFile(url)
    //     .subscribe(data => {
    //         // console.log("data", data);
    //     },
    //         err => console.log(err),
    //         () => console.log('File deleted ..')
    //     );

    // }
}
