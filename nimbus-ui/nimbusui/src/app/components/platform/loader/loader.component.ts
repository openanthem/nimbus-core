import { LoaderState } from './loader.state';
import { LoaderService } from './../../../services/loader.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';

@Component({
    selector: 'app-loader',
    templateUrl: 'loader.component.html',
    styleUrls: ['loader.component.css']
})

export class LoaderComponent implements OnInit {

show = false;
private subscription: Subscription;

constructor(private loaderService: LoaderService) { }

ngOnInit() { 
        this.subscription = this.loaderService.loaderUpdate
            .subscribe((state: LoaderState) => {
                this.show = state.show;
            });
    }

ngOnDestroy() {
        this.subscription.unsubscribe();
    }
}