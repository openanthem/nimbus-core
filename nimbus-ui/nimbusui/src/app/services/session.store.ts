import { SESSION_STORAGE, StorageService } from 'angular-webstorage-service';
import { Inject, Injectable } from '@angular/core';
import { InjectionToken } from '@angular/core';

export const CUSTOM_STORAGE = new InjectionToken<StorageService>('CUSTOM_STORAGE');
 
@Injectable()
export class SessionStoreService {
 
    constructor(@Inject(CUSTOM_STORAGE) private storage: StorageService) {
 
    }

    set(key:string, value: any) {
        this.storage.set(key, value);
    }
 
    get(key:string): any {
        let val = this.storage.get(key);
        return val;
    }
}