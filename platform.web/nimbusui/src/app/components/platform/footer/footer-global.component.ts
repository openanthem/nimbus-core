import { Component, Input, OnInit } from '@angular/core';

var _uniqueId = 0;

@Component({
    selector: 'nm-footer-global',
    templateUrl: './footer-global.component.html'
})

export class FooterGlobal implements OnInit {

    @Input() message: string;
    
    private links : Array<string>;

    constructor(){
        
    }

    ngOnInit(){
        this.links = ['Site Map', 'Terms of Use', 'Protected Marks', 'Privacy Policy'];
        
        this.message = `© 2005 - 2017 copyright of Anthem Insurance Companies, Inc. Serving Colorado, Connecticut, Georgia,
        Indiana, Kentucky, Maine, Missouri (excluding 30 counties in the Kansas City area), Nevada, New Hampshire, Ohio,
        Virginia (excluding the Northern Virginia suburbs of Washington, D.C.), and Wisconsin.`;
    }

}