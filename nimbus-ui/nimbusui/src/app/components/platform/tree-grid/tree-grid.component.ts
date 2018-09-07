import { Component, Input } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ControlValueAccessor } from '@angular/forms/src/directives';
import { BaseElement } from './../base-element.component';
import { WebContentSvc } from '../../../services/content-management.service';
import { ParamConfig } from '../../../shared/param-config';
import { PageService } from '../../../services/page.service';
import { GenericDomain } from '../../../model/generic-domain.model';


@Component({
    selector: 'nm-treegrid',
    templateUrl: './tree-grid.component.html'
})
export class TreeGrid extends BaseElement  implements ControlValueAccessor {

    @Input() params: ParamConfig[];
    @Input() form: FormGroup;

    public onChange: any = (_) => { /*Empty*/ }
    public onTouched: any = () => { /*Empty*/ }

    public writeValue(obj: any): void {
        if (obj !== undefined) {
        }
    }

    public registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    public registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    files: any;

    constructor(private _wcs: WebContentSvc, private pageSvc: PageService, ) {
        super(_wcs);
    }

    ngOnInit() {
        super.ngOnInit();

        this.pageSvc.processEvent(this.element.path, '$execute', new GenericDomain(), 'GET', undefined);
        this.pageSvc.treeListUpdate$.subscribe((treeList: {data: {}}) => {
            console.log("treeList in ts file", treeList.data);
            this.files = treeList.data;
        });


        // this.files = [{"data":{"id":14,"firstName":"own","lastName":"onw"},"children":[{"data":{"id":114,"firstName":"child","lastName":"child"},"children":[{"data":{"id":214,"firstName":"child1_1","lastName":"child2_2"},"children":[]}]},{"data":{"id":214,"firstName":"child1","lastName":"child2"},"children":[]}]}]

        // this.files = 

        //     [  
        //         {  
        //             "data":{  
        //                 "name":"Applications",
        //                 "size":"200mb",
        //                 "type":"Folder"
        //             },
        //             "children":[  
        //                 {  
        //                     "data":{
        //                         "id": 4,  
        //                         "name":"Angular",
        //                         "size":"25mb",
        //                         "type":"Folder"
        //                     },
        //                     "children":[  
        //                         {  
        //                             "data":{  
        //                                 "name":"angular.app",
        //                                 "size":"10mb",
        //                                 "type":"Application"
        //                             }
        //                         },
        //                         {  
        //                             "data":{  
        //                                 "name":"cli.app",
        //                                 "size":"10mb",
        //                                 "type":"Application"
        //                             }
        //                         },
        //                         {  
        //                             "data":{  
        //                                 "name":"mobile.app",
        //                                 "size":"5mb",
        //                                 "type":"Application"
        //                             }
        //                         }
        //                     ]
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"editor.app",
        //                         "size":"25mb",
        //                         "type":"Application"
        //                     }
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"settings.app",
        //                         "size":"50mb",
        //                         "type":"Application"
        //                     }
        //                 }
        //             ]
        //         },
        //         {  
        //             "data":{  
        //                 "name":"Cloud",
        //                 "size":"20mb",
        //                 "type":"Folder"
        //             },
        //             "children":[  
        //                 {  
        //                     "data":{  
        //                         "name":"backup-1.zip",
        //                         "size":"10mb",
        //                         "type":"Zip"
        //                     }
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"backup-2.zip",
        //                         "size":"10mb",
        //                         "type":"Zip"
        //                     }
        //                 }
        //             ]
        //         },
        //         {  
        //             "data": {  
        //                 "name":"Desktop",
        //                 "size":"150kb",
        //                 "type":"Folder"
        //             },
        //             "children":[  
        //                 {  
        //                     "data":{  
        //                         "name":"note-meeting.txt",
        //                         "size":"50kb",
        //                         "type":"Text"
        //                     }
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"note-todo.txt",
        //                         "size":"100kb",
        //                         "type":"Text"
        //                     }
        //                 }
        //             ]
        //         },
        //         {  
        //             "data":{  
        //                 "name":"Documents",
        //                 "size":"75kb",
        //                 "type":"Folder"
        //             },
        //             "children":[
        //                 {  
        //                     "data":{  
        //                         "name":"Work",
        //                         "size":"55kb",
        //                         "type":"Folder"
        //                     },
        //                     "children":[  
        //                         {  
        //                             "data":{  
        //                                 "name":"Expenses.doc",
        //                                 "size":"30kb",
        //                                 "type":"Document"
        //                             }
        //                         },
        //                         {  
        //                             "data":{  
        //                                 "name":"Resume.doc",
        //                                 "size":"25kb",
        //                                 "type":"Resume"
        //                             }
        //                         }
        //                     ]
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"Home",
        //                         "size":"20kb",
        //                         "type":"Folder"
        //                     },
        //                     "children":[  
        //                         {  
        //                             "data":{  
        //                                 "name":"Invoices",
        //                                 "size":"20kb",
        //                                 "type":"Text"
        //                             }
        //                         }
        //                     ]
        //                 }
        //             ]
        //         },
        //         {  
        //             "data": {  
        //                 "name":"Downloads",
        //                 "size":"25mb",
        //                 "type":"Folder"
        //             },
        //             "children":[  
        //                 {  
        //                     "data": {  
        //                         "name":"Spanish",
        //                         "size":"10mb",
        //                         "type":"Folder"
        //                     },
        //                     "children":[  
        //                         {  
        //                             "data":{  
        //                                 "name":"tutorial-a1.txt",
        //                                 "size":"5mb",
        //                                 "type":"Text"
        //                             }
        //                         },
        //                         {  
        //                             "data":{  
        //                                 "name":"tutorial-a2.txt",
        //                                 "size":"5mb",
        //                                 "type":"Text"
        //                             }
        //                         }
        //                     ]
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"Travel",
        //                         "size":"15mb",
        //                         "type":"Text"
        //                     },
        //                     "children":[  
        //                         {  
        //                             "data":{  
        //                                 "name":"Hotel.pdf",
        //                                 "size":"10mb",
        //                                 "type":"PDF"
        //                             }
        //                         },
        //                         {  
        //                             "data":{  
        //                                 "name":"Flight.pdf",
        //                                 "size":"5mb",
        //                                 "type":"PDF"
        //                             }
        //                         }
        //                     ]
        //                 }
        //             ]
        //         },
        //         {  
        //             "data": {  
        //                 "name":"Main",
        //                 "size":"50mb",
        //                 "type":"Folder"
        //             },
        //             "children":[  
        //                 {  
        //                     "data":{  
        //                         "name":"bin",
        //                         "size":"50kb",
        //                         "type":"Link"
        //                     }
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"etc",
        //                         "size":"100kb",
        //                         "type":"Link"
        //                     }
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"var",
        //                         "size":"100kb",
        //                         "type":"Link"
        //                     }
        //                 }
        //             ]
        //         },
        //         {  
        //             "data":{  
        //                 "name":"Other",
        //                 "size":"5mb",
        //                 "type":"Folder"
        //             },
        //             "children":[  
        //                 {  
        //                     "data":{  
        //                         "name":"todo.txt",
        //                         "size":"3mb",
        //                         "type":"Text"
        //                     }
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"logo.png",
        //                         "size":"2mb",
        //                         "type":"Picture"
        //                     }
        //                 }
        //             ]
        //         },
        //         {  
        //             "data":{  
        //                 "name":"Pictures",
        //                 "size":"150kb",
        //                 "type":"Folder"
        //             },
        //             "children":[  
        //                 {  
        //                     "data":{  
        //                         "name":"barcelona.jpg",
        //                         "size":"90kb",
        //                         "type":"Picture"
        //                     }
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"primeng.png",
        //                         "size":"30kb",
        //                         "type":"Picture"
        //                     }
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"prime.jpg",
        //                         "size":"30kb",
        //                         "type":"Picture"
        //                     }
        //                 }
        //             ]
        //         },
        //         {  
        //             "data":{  
        //                 "name":"Videos",
        //                 "size":"1500mb",
        //                 "type":"Folder"
        //             },
        //             "children":[  
        //                 {  
        //                     "data":{  
        //                         "name":"primefaces.mkv",
        //                         "size":"1000mb",
        //                         "type":"Video"
        //                     }
        //                 },
        //                 {  
        //                     "data":{  
        //                         "name":"intro.avi",
        //                         "size":"500mb",
        //                         "type":"Video"
        //                     }
        //                 }
        //             ]
        //         }
        //     ]
    }



    showHeader(col: ParamConfig) {
        if (col.uiStyles && col.uiStyles.attributes.hidden === false &&
            col.uiStyles.attributes.alias === 'GridColumn') {
            return true;
        }
        return false;
    }
}


export interface TreeNode {
    data?: any;
    children?: TreeNode[];
    leaf?: boolean;
    expanded?: boolean;
}